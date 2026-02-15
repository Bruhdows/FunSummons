package com.bruhdows.funsummons.manager;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.model.AccessoryConfig;
import com.bruhdows.funsummons.model.PlayerData;
import com.bruhdows.funsummons.model.Stats;
import com.bruhdows.funsummons.model.WandConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AccessoryManager {
    private final FunSummonsPlugin plugin;
    
    public AccessoryManager(FunSummonsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void recalculateStats(Player player) {
        PlayerData data = plugin.getManaManager().getPlayerData(player);
        
        Stats totalStats = new Stats(100, 1.0, 2, 0.0, 0.0);
        
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
                    wandStats.getHealthBoost()
                );
            }
        }
        
        // Add accessory stats
        for (String accId : data.getAccessories().values()) {
            AccessoryConfig accConfig = plugin.getConfigManager().getAccessory(accId);
            if (accConfig != null) {
                totalStats.add(accConfig.getStats());
            }
        }
        
        data.setMaxMana(totalStats.getMaxMana());
        data.setManaRegen(totalStats.getManaRegen());
        data.setSummonSlots(totalStats.getSummonSlots());
    }
    
    public Stats getTotalStats(Player player) {
        PlayerData data = plugin.getManaManager().getPlayerData(player);
        Stats totalStats = new Stats(100, 1.0, 2, 0.0, 0.0);
        
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
                    wandStats.getHealthBoost()
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
}