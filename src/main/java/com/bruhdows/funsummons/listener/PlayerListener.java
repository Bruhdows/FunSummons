package com.bruhdows.funsummons.listener;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.gui.AccessoryGUI;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {
    private final FunSummonsPlugin plugin;
    
    public PlayerListener(FunSummonsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getManaManager().loadPlayerDataAsync(event.getPlayer());
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
        org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> {
            plugin.getAccessoryManager().recalculateStats(event.getPlayer());
            plugin.getManaManager().updateManaBar(event.getPlayer());
        });
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
    public void onInteractEntity(org.bukkit.event.player.PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        
        if (event.getRightClicked() instanceof Allay allay) {
            if (plugin.getSummonManager().isPlayerFairy(allay) &&
                plugin.getSummonManager().getPlayerFairy(player) == allay) {
                event.setCancelled(true);
                if (!allay.getPassengers().contains(player)) {
                    allay.addPassenger(player);
                    player.sendActionBar(plugin.getMiniMessage().deserialize("<dark_purple>Mounted Fairy Companion!</dark_purple>"));
                }
            }
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
    
    @EventHandler
    public void onLeftClick(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        if (event.getEntity() instanceof Allay allay) {
            if (plugin.getSummonManager().isPlayerFairy(allay)) {
                event.setCancelled(true);
                if (allay.getPassengers().contains(player)) {
                    plugin.getSummonManager().playerLeftClick(player);
                }
            }
        }
    }
}
