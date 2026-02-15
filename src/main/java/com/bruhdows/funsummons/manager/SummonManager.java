package com.bruhdows.funsummons.manager;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.model.Stats;
import com.bruhdows.funsummons.model.SummonConfig;
import com.bruhdows.funsummons.model.WandConfig;
import com.bruhdows.funsummons.summon.SummonAI;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class SummonManager {
    private final FunSummonsPlugin plugin;
    private final Map<UUID, List<LivingEntity>> playerSummons = new HashMap<>();
    private final Map<UUID, SummonAI> summonAIs = new HashMap<>();
    private final Map<UUID, Long> playerFairyCooldowns = new HashMap<>();
    private final Map<UUID, Allay> playerFairies = new HashMap<>();
    private final Map<UUID, BossBar> fairyBossBars = new HashMap<>();
    private final Map<UUID, Location> fairyTargetLocations = new HashMap<>();
    
    public SummonManager(FunSummonsPlugin plugin) {
        this.plugin = plugin;
        startAITick();
        startFairyTimer();
    }
    
    private void startAITick() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (SummonAI ai : summonAIs.values()) {
                ai.tick();
            }
        }, 0L, 1L);
    }
    
    private void startFairyTimer() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Iterator<Map.Entry<UUID, Allay>> iterator = playerFairies.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<UUID, Allay> entry = iterator.next();
                UUID playerId = entry.getKey();
                Allay allay = entry.getValue();

                if (allay.isDead() || !allay.isValid()) {
                    removeFairy(playerId);
                    iterator.remove();
                    continue;
                }

                Player player = Bukkit.getPlayer(playerId);
                if (player != null && player.isOnline()) {
                    
                    String wandId = plugin.getItemUtil().getWandId(player.getInventory().getItemInMainHand());
                    boolean holdingFairyWand = "fairy_wand".equals(wandId);
                    boolean isRiding = allay.getPassengers().contains(player);
                    
                    if (isRiding && holdingFairyWand) {
                        Location eyeLoc = player.getEyeLocation();
                        Vector direction = eyeLoc.getDirection();
                        
                        double speed = 0.6;
                        Vector velocity = direction.clone().multiply(speed);
                        
                        double pitch = eyeLoc.getPitch();
                        if (pitch < -15) {
                            velocity.setY(0.4);
                        } else if (pitch > 15) {
                            velocity.setY(-0.4);
                        } else {
                            velocity.setY(0);
                        }
                        
                        allay.setVelocity(velocity);
                        
                        Location newLoc = allay.getLocation();
                        newLoc.setDirection(direction);
                        allay.teleport(newLoc);
                    } else {
                        allay.setVelocity(new Vector(0, 0, 0));
                    }
                }

                Long cooldownEnd = playerFairyCooldowns.get(playerId);
                if (cooldownEnd != null) {
                    long remaining = cooldownEnd - System.currentTimeMillis();
                    if (remaining <= 0) {
                        removeFairy(playerId);
                        iterator.remove();
                    } else {
                        updateFairyBossBar(playerId, remaining);
                    }
                }
            }
        }, 0L, 1L);
    }
    
    public boolean summon(Player player, String summonId, WandConfig wandConfig) {
        SummonConfig config = plugin.getConfigManager().getSummon(summonId);
        if (config == null) {
            return false;
        }
        
        if (summonId.equals("fairy_companion")) {
            return summonFairy(player, wandConfig);
        }
        
        List<LivingEntity> summons = playerSummons.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        int maxSlots = plugin.getManaManager().getPlayerData(player).getSummonSlots();
        
        if (summons.size() >= maxSlots) {
            player.sendActionBar(plugin.getMiniMessage().deserialize("<red>Maximum summons reached!</red>"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return false;
        }
        
        int manaCost = wandConfig.getSummonCost();
        
        if (!plugin.getManaManager().getPlayerData(player).consumeMana(manaCost)) {
            player.sendActionBar(plugin.getMiniMessage().deserialize(
                String.format("<red>Not enough mana! Need %d mana</red>", manaCost)
            ));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return false;
        }
        
        int cooldownTicks = calculateCooldown(wandConfig.getCooldown(), player);
        if (cooldownTicks > 0) {
            player.setCooldown(wandConfig.getMaterial(), cooldownTicks);
        }
        
        Location loc = player.getLocation();
        EntityType type = EntityType.valueOf(config.getEntityType());
        LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(loc, type);
        
        Stats playerStats = plugin.getAccessoryManager().getTotalStats(player);
        int durationBonus = playerStats.getSummonDuration();
        
        entity.customName(plugin.getMiniMessage().deserialize(config.getDisplayName()));
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);
        entity.setPersistent(true);
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
            double baseScale = 0.5;
            double sizeModifier = playerStats.getSummonSize();
            
            double finalScale = baseScale + (sizeModifier * 0.3);
            finalScale = Math.max(0.25, Math.min(2.0, finalScale));
            
            scale.setBaseValue(finalScale);
        }
        
        entity.setMetadata("summon", new FixedMetadataValue(plugin, player.getUniqueId().toString()));
        entity.setMetadata("summonConfig", new FixedMetadataValue(plugin, summonId));
        
        if (durationBonus > 0) {
            entity.setMetadata("summonDuration", new FixedMetadataValue(plugin, durationBonus));
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (entity.isValid() && !entity.isDead()) {
                        dismissSummon(player, entity);
                    }
                }
            }.runTaskLater(plugin, durationBonus * 20L);
        }
        
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
        
        plugin.getManaManager().updateManaBar(player);
        
        return true;
    }
    
    private boolean summonFairy(Player player, WandConfig wandConfig) {
        if (playerFairies.containsKey(player.getUniqueId())) {
            player.sendActionBar(plugin.getMiniMessage().deserialize("<red>You already have a fairy summoned!</red>"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return false;
        }
        
        int manaCost = wandConfig.getSummonCost();
        
        if (!plugin.getManaManager().getPlayerData(player).consumeMana(manaCost)) {
            player.sendActionBar(plugin.getMiniMessage().deserialize(
                String.format("<red>Not enough mana! Need %d mana</red>", manaCost)
            ));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return false;
        }
        
        int cooldownTicks = calculateCooldown(wandConfig.getCooldown(), player);
        if (cooldownTicks > 0) {
            player.setCooldown(wandConfig.getMaterial(), cooldownTicks);
        }
        
        Location loc = player.getLocation();
        Allay allay = (Allay) player.getWorld().spawnEntity(loc, EntityType.ALLAY);
        
        allay.customName(plugin.getMiniMessage().deserialize("<dark_purple>Fairy Companion</dark_purple>"));
        allay.setCustomNameVisible(true);
        
        allay.setInvulnerable(true);
        allay.setSilent(true);
        
        AttributeInstance scaleAttr = allay.getAttribute(Attribute.SCALE);
        if (scaleAttr != null) {
            scaleAttr.setBaseValue(3.0);
        }
        
        fairyTargetLocations.put(player.getUniqueId(), loc.clone());
        
        playerFairies.put(player.getUniqueId(), allay);
        playerFairyCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 300000);
        
        allay.addPassenger(player);
        
        createFairyBossBar(player);
        
        Location particleLoc = allay.getLocation().add(0, 2, 0);
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, particleLoc, 100, 2.0, 1.0, 2.0, 0.2);
        player.getWorld().spawnParticle(Particle.END_ROD, particleLoc, 50, 2.0, 1.0, 2.0, 0.1);
        player.getWorld().playSound(particleLoc, Sound.ENTITY_ALLAY_ITEM_GIVEN, 2.0f, 0.8f);
        
        player.sendActionBar(plugin.getMiniMessage().deserialize(
            String.format("<gold>-%d Mana</gold> <gray>|</gray> <dark_purple>Summoned Fairy Companion!</dark_purple>", manaCost)
        ));
        
        plugin.getManaManager().updateManaBar(player);
        
        return true;
    }
    
    public void updateDragonTarget(Player player, Location targetLoc) {
        if (playerFairies.containsKey(player.getUniqueId())) {
            fairyTargetLocations.put(player.getUniqueId(), targetLoc);
        }
    }
    
    private int calculateCooldown(int baseCooldown, Player player) {
        if (baseCooldown <= 0) return 0;
        
        Stats stats = plugin.getAccessoryManager().getTotalStats(player);
        int reduction = stats.getCooldownReduction();
        
        int reducedCooldown = baseCooldown - (baseCooldown * reduction / 100);
        return Math.max(0, reducedCooldown);
    }
    
    private void createFairyBossBar(Player player) {
        BossBar bossBar = BossBar.bossBar(
            plugin.getMiniMessage().deserialize("<dark_purple>Fairy Companion <gray>-</gray> <yellow>5:00</yellow>"),
            1.0f,
            BossBar.Color.PURPLE,
            BossBar.Overlay.PROGRESS
        );
        fairyBossBars.put(player.getUniqueId(), bossBar);
        player.showBossBar(bossBar);
    }
    
    private void updateFairyBossBar(UUID playerId, long remainingMs) {
        BossBar bossBar = fairyBossBars.get(playerId);
        if (bossBar == null) return;
        
        long seconds = remainingMs / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        bossBar.name(plugin.getMiniMessage().deserialize(
            String.format("<dark_purple>Fairy Companion <gray>-</gray> <yellow>%d:%02d</yellow>", minutes, seconds)
        ));
        bossBar.progress((float) remainingMs / 300000.0f);
    }
    
    private void removeFairy(UUID playerId) {
        Allay allay = playerFairies.remove(playerId);
        if (allay != null && allay.isValid()) {
            for (Entity passenger : allay.getPassengers()) {
                if (passenger instanceof Player) {
                    allay.removePassenger(passenger);
                }
            }
            allay.remove();
        }
        
        playerFairyCooldowns.remove(playerId);
        fairyTargetLocations.remove(playerId);
        
        BossBar bossBar = fairyBossBars.remove(playerId);
        if (bossBar != null) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.hideBossBar(bossBar);
                player.sendMessage(plugin.getMiniMessage().deserialize("<gray>Your <dark_purple>Fairy Companion</dark_purple> has despawned.</gray>"));
            }
        }
    }
    
    public Allay getPlayerFairy(Player player) {
        return playerFairies.get(player.getUniqueId());
    }
    
    public boolean isPlayerFairy(Entity entity) {
        return playerFairies.containsValue(entity);
    }
    
    public void playerLeftClick(Player player) {
        Allay allay = playerFairies.get(player.getUniqueId());
        if (allay == null || !allay.isValid()) return;
        
        if (!allay.getPassengers().contains(player)) return;
        
        Location eyeLoc = allay.getEyeLocation();
        Vector direction = eyeLoc.getDirection();
        
        Fireball fireball = (Fireball) player.getWorld().spawnEntity(eyeLoc.add(direction.multiply(3)), EntityType.FIREBALL);
        fireball.setDirection(direction);
        fireball.setYield(2.0f);
        fireball.setIsIncendiary(false);
        
        Location breathLoc = eyeLoc.clone().add(direction.multiply(2));
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, breathLoc, 30, 1.0, 0.5, 1.0, 0.1);
        player.getWorld().playSound(allay.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
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
        removeFairy(player.getUniqueId());
        
        List<LivingEntity> summons = playerSummons.remove(player.getUniqueId());
        if (summons != null) {
            for (LivingEntity summon : summons) {
                summonAIs.remove(summon.getUniqueId());
                summon.remove();
            }
        }
    }
    
    public void removeAllSummons() {
        for (UUID playerId : new HashSet<>(playerFairies.keySet())) {
            removeFairy(playerId);
        }
        
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
        return entity.hasMetadata("summon") || isPlayerFairy(entity);
    }
    
    public Player getSummonOwner(Entity entity) {
        if (!isSummon(entity)) return null;
        
        if (entity.hasMetadata("summon")) {
            String uuidString = entity.getMetadata("summon").get(0).asString();
            return Bukkit.getPlayer(UUID.fromString(uuidString));
        }
        
        for (Map.Entry<UUID, Allay> entry : playerFairies.entrySet()) {
            if (entry.getValue().equals(entity)) {
                return Bukkit.getPlayer(entry.getKey());
            }
        }
        
        return null;
    }
    
    public boolean hasWardenSummon(Player player) {
        List<LivingEntity> summons = getSummons(player);
        for (LivingEntity summon : summons) {
            if (summon.hasMetadata("summonConfig")) {
                String configId = summon.getMetadata("summonConfig").get(0).asString();
                if ("warden".equals(configId)) {
                    return true;
                }
            }
        }
        return false;
    }
}
