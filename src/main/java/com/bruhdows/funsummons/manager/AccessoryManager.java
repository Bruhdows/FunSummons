package com.bruhdows.funsummons.manager;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.model.AccessoryConfig;
import com.bruhdows.funsummons.model.PlayerData;
import com.bruhdows.funsummons.model.Stats;
import com.bruhdows.funsummons.model.WandConfig;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;

public class AccessoryManager {
    private final FunSummonsPlugin plugin;
    
    public AccessoryManager(FunSummonsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void recalculateStats(Player player) {
        PlayerData data = plugin.getManaManager().getPlayerData(player);
        
        Stats totalStats = new Stats(100, 1.0, 2, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0);
        
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        String wandId = plugin.getItemUtil().getWandId(mainHand);
        if (wandId != null) {
            WandConfig wandConfig = plugin.getConfigManager().getWand(wandId);
            if (wandConfig != null) {
                Stats wandStats = wandConfig.getStats();
                totalStats = new Stats(
                    wandStats.getMaxMana(),
                    wandStats.getManaRegen(),
                    wandStats.getSummonSlots(),
                    wandStats.getDamageBoost(),
                    wandStats.getHealthBoost(),
                    wandStats.getCooldownReduction(),
                    wandStats.getSummonDuration(),
                    wandStats.getCritChance(),
                    wandStats.getKnockbackResistance(),
                    wandStats.getSpeedBoost(),
                    wandStats.getSummonSize()
                );
            }
        }
        
        for (String accId : data.getAccessories().values()) {
            AccessoryConfig accConfig = plugin.getConfigManager().getAccessory(accId);
            if (accConfig != null) {
                totalStats.add(accConfig.getStats());
            }
        }
        
        data.setMaxMana(totalStats.getMaxMana());
        data.setManaRegen(totalStats.getManaRegen());
        data.setSummonSlots(totalStats.getSummonSlots());
        data.setCooldownReduction(totalStats.getCooldownReduction());
        data.setSummonDuration(totalStats.getSummonDuration());
        data.setCritChance(totalStats.getCritChance());
        data.setKnockbackResistance(totalStats.getKnockbackResistance());
        data.setSpeedBoost(totalStats.getSpeedBoost());
        data.setSummonSize(totalStats.getSummonSize());
        
        applySpeedBoost(player, totalStats.getSpeedBoost());
        applyKnockbackResistance(player, totalStats.getKnockbackResistance());
    }
    
    public Stats getTotalStats(Player player) {
        PlayerData data = plugin.getManaManager().getPlayerData(player);
        Stats totalStats = new Stats(100, 1.0, 2, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0);
        
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        String wandId = plugin.getItemUtil().getWandId(mainHand);
        if (wandId != null) {
            WandConfig wandConfig = plugin.getConfigManager().getWand(wandId);
            if (wandConfig != null) {
                Stats wandStats = wandConfig.getStats();
                totalStats = new Stats(
                    wandStats.getMaxMana(),
                    wandStats.getManaRegen(),
                    wandStats.getSummonSlots(),
                    wandStats.getDamageBoost(),
                    wandStats.getHealthBoost(),
                    wandStats.getCooldownReduction(),
                    wandStats.getSummonDuration(),
                    wandStats.getCritChance(),
                    wandStats.getKnockbackResistance(),
                    wandStats.getSpeedBoost(),
                    wandStats.getSummonSize()
                );
            }
        }
        
        for (String accId : data.getAccessories().values()) {
            AccessoryConfig accConfig = plugin.getConfigManager().getAccessory(accId);
            if (accConfig != null) {
                totalStats.add(accConfig.getStats());
            }
        }
        
        return totalStats;
    }
    
    private void applySpeedBoost(Player player, double speedBoost) {
        AttributeInstance movementSpeed = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            NamespacedKey speedKey = new NamespacedKey(plugin, "summon_speed_boost");
            movementSpeed.removeModifier(speedKey);
            
            if (speedBoost > 0) {
                AttributeModifier speedModifier = new AttributeModifier(
                    speedKey,
                    speedBoost,
                    AttributeModifier.Operation.ADD_SCALAR,
                    EquipmentSlotGroup.ANY
                );
                movementSpeed.addModifier(speedModifier);
            }
        }
    }
    
    private void applyKnockbackResistance(Player player, double knockbackResistance) {
        AttributeInstance kbResist = player.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
        if (kbResist != null) {
            NamespacedKey kbKey = new NamespacedKey(plugin, "summon_kb_resistance");
            kbResist.removeModifier(kbKey);
            
            if (knockbackResistance > 0) {
                AttributeModifier kbModifier = new AttributeModifier(
                    kbKey,
                    knockbackResistance,
                    AttributeModifier.Operation.ADD_SCALAR,
                    EquipmentSlotGroup.ANY
                );
                kbResist.addModifier(kbModifier);
            }
        }
    }
}
