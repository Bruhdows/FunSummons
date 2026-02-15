package com.bruhdows.funsummons.manager;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.model.Stats;
import com.bruhdows.funsummons.model.SummonConfig;
import com.bruhdows.funsummons.model.WandConfig;
import com.bruhdows.funsummons.summon.SummonAI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class SummonManager {
    private final FunSummonsPlugin plugin;
    private final Map<UUID, List<LivingEntity>> playerSummons = new HashMap<>();
    private final Map<UUID, SummonAI> summonAIs = new HashMap<>();
    
    public SummonManager(FunSummonsPlugin plugin) {
        this.plugin = plugin;
        startAITick();
    }
    
    private void startAITick() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (SummonAI ai : summonAIs.values()) {
                ai.tick();
            }
        }, 0L, 1L);
    }
    
    public boolean summon(Player player, String summonId, WandConfig wandConfig) {
        SummonConfig config = plugin.getConfigManager().getSummon(summonId);
        if (config == null) {
            return false;
        }
        
        List<LivingEntity> summons = playerSummons.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        int maxSlots = plugin.getManaManager().getPlayerData(player).getSummonSlots();
        
        if (summons.size() >= maxSlots) {
            player.sendActionBar(plugin.getMiniMessage().deserialize("<red>Maximum summons reached!</red>"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return false;
        }
        
        // Use wand's summon cost
        int manaCost = wandConfig.getSummonCost();
        
        if (!plugin.getManaManager().getPlayerData(player).consumeMana(manaCost)) {
            player.sendActionBar(plugin.getMiniMessage().deserialize(
                String.format("<red>Not enough mana! Need %d mana</red>", manaCost)
            ));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return false;
        }
        
        Location loc = player.getLocation();
        EntityType type = EntityType.valueOf(config.getEntityType());
        LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(loc, type);
        
        Stats playerStats = plugin.getAccessoryManager().getTotalStats(player);
        
        entity.customName(plugin.getMiniMessage().deserialize(config.getDisplayName()));
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);
        entity.setPersistent(true);
        
        // Make immune to fire
        entity.setFireTicks(0);
        entity.setVisualFire(false);

        AttributeInstance maxHealth = entity.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(config.getHealth());
            entity.setHealth(config.getHealth());
        }
        AttributeInstance movementSpeed = entity.getAttribute(Attribute.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            movementSpeed.setBaseValue(config.getSpeed());
        }
        AttributeInstance attackDamage = entity.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.setBaseValue(config.getDamage());
        }

        if (playerStats.getHealthBoost() > 0 && maxHealth != null) {
            NamespacedKey healthKey = new NamespacedKey(plugin, "summon_health_boost");
            AttributeModifier healthModifier = new AttributeModifier(
                healthKey,
                playerStats.getHealthBoost(),
                AttributeModifier.Operation.ADD_SCALAR,
                EquipmentSlotGroup.ANY
            );
            maxHealth.addModifier(healthModifier);
            entity.setHealth(maxHealth.getValue());
        }
        
        if (playerStats.getDamageBoost() > 0 && attackDamage != null) {
            NamespacedKey damageKey = new NamespacedKey(plugin, "summon_damage_boost");
            AttributeModifier damageModifier = new AttributeModifier(
                damageKey,
                playerStats.getDamageBoost(),
                AttributeModifier.Operation.ADD_SCALAR,
                EquipmentSlotGroup.ANY
            );
            attackDamage.addModifier(damageModifier);
        }

        AttributeInstance scale = entity.getAttribute(Attribute.SCALE);
        if (scale != null) {
            scale.setBaseValue(0.5);
        }
        
        entity.setMetadata("summon", new FixedMetadataValue(plugin, player.getUniqueId().toString()));
        entity.setMetadata("summonConfig", new FixedMetadataValue(plugin, summonId));
        
        summons.add(entity);
        
        SummonAI ai = new SummonAI(plugin, entity, player);
        summonAIs.put(entity.getUniqueId(), ai);
        
        Location particleLoc = entity.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(Particle.ENCHANT, particleLoc, 50, 0.5, 0.5, 0.5, 0.1);
        player.getWorld().spawnParticle(Particle.WITCH, particleLoc, 30, 0.3, 0.5, 0.3, 0.05);
        player.getWorld().playSound(particleLoc, Sound.ENTITY_EVOKER_CAST_SPELL, 1.0f, 1.2f);
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.8f, 1.5f);
        
        player.sendActionBar(plugin.getMiniMessage().deserialize(
            String.format("<gold>-%d Mana</gold> <gray>|</gray> <green>Summoned %s</green>", 
                manaCost, config.getName())
        ));
        
        // Update mana bar
        plugin.getManaManager().updateManaBar(player);
        
        return true;
    }
    
    public void dismissSummon(Player player, LivingEntity summon) {
        List<LivingEntity> summons = playerSummons.get(player.getUniqueId());
        if (summons != null) {
            summons.remove(summon);
            summonAIs.remove(summon.getUniqueId());
            summon.remove();
        }
    }
    
    public void dismissAllSummons(Player player) {
        List<LivingEntity> summons = playerSummons.remove(player.getUniqueId());
        if (summons != null) {
            for (LivingEntity summon : summons) {
                summonAIs.remove(summon.getUniqueId());
                summon.remove();
            }
        }
    }
    
    public void removeAllSummons() {
        for (List<LivingEntity> summons : playerSummons.values()) {
            for (LivingEntity summon : summons) {
                summon.remove();
            }
        }
        playerSummons.clear();
        summonAIs.clear();
    }
    
    public List<LivingEntity> getSummons(Player player) {
        return playerSummons.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }
    
    public boolean isSummon(Entity entity) {
        return entity.hasMetadata("summon");
    }
    
    public Player getSummonOwner(Entity entity) {
        if (!isSummon(entity)) return null;
        String uuidString = entity.getMetadata("summon").get(0).asString();
        return Bukkit.getPlayer(UUID.fromString(uuidString));
    }
}