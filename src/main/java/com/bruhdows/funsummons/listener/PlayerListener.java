package com.bruhdows.funsummons.listener;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.gui.AccessoryGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {
    private final FunSummonsPlugin plugin;
    
    public PlayerListener(FunSummonsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getManaManager().getPlayerData(event.getPlayer());
        plugin.getAccessoryManager().recalculateStats(event.getPlayer());
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getSummonManager().dismissAllSummons(event.getPlayer());
        plugin.getManaManager().hideManaBar(event.getPlayer());
        plugin.getManaManager().savePlayerData(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        plugin.getAccessoryManager().recalculateStats(event.getPlayer());
        plugin.getManaManager().updateManaBar(event.getPlayer());
    }
    
    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        if (event.getPlayer().isSneaking()) {
            event.setCancelled(true);
            new AccessoryGUI(plugin, event.getPlayer()).open();
        } else {
            plugin.getAccessoryManager().recalculateStats(event.getPlayer());
            plugin.getManaManager().updateManaBar(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction().isRightClick()) {
            if (event.getItem() != null) {
                String wandId = plugin.getItemUtil().getWandId(event.getItem());
                if (wandId != null) {
                    event.setCancelled(true);
                    var wandConfig = plugin.getConfigManager().getWand(wandId);
                    if (wandConfig != null && wandConfig.getSummonType() != null) {
                        plugin.getSummonManager().summon(event.getPlayer(), wandConfig.getSummonType(), wandConfig);
                    }
                }
            }
        }
    }
}