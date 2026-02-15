package com.bruhdows.funsummons.listener;

import com.bruhdows.funsummons.FunSummonsPlugin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class SummonListener implements Listener {
    private final FunSummonsPlugin plugin;
    
    public SummonListener(FunSummonsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (plugin.getSummonManager().isSummon(event.getEntity())) {
            Player owner = plugin.getSummonManager().getSummonOwner(event.getEntity());
            if (owner != null) {
                plugin.getSummonManager().dismissSummon(owner, event.getEntity());
                owner.sendMessage(plugin.getMiniMessage().deserialize("<red>Your summon has died!</red>"));
            }
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        // Prevent summons from targeting their owner
        if (plugin.getSummonManager().isSummon(event.getEntity())) {
            Player owner = plugin.getSummonManager().getSummonOwner(event.getEntity());
            if (owner != null && event.getTarget() != null && event.getTarget().equals(owner)) {
                event.setCancelled(true);
                if (event.getEntity() instanceof Mob mob) {
                    mob.setTarget(null);
                }
            }
        }
        
        // Prevent summons from targeting other summons of the same owner
        if (plugin.getSummonManager().isSummon(event.getEntity()) && 
            event.getTarget() != null && 
            plugin.getSummonManager().isSummon(event.getTarget())) {
            Player damagerOwner = plugin.getSummonManager().getSummonOwner(event.getEntity());
            Player targetOwner = plugin.getSummonManager().getSummonOwner(event.getTarget());
            if (damagerOwner != null && damagerOwner.equals(targetOwner)) {
                event.setCancelled(true);
                if (event.getEntity() instanceof Mob mob) {
                    mob.setTarget(null);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Prevent summons from attacking their owner
        if (plugin.getSummonManager().isSummon(event.getDamager())) {
            Player owner = plugin.getSummonManager().getSummonOwner(event.getDamager());
            if (owner != null && event.getEntity().equals(owner)) {
                event.setCancelled(true);
            }
        }
        
        // Prevent friendly fire between summons of same owner
        if (plugin.getSummonManager().isSummon(event.getDamager()) &&
            plugin.getSummonManager().isSummon(event.getEntity())) {
            Player damagerOwner = plugin.getSummonManager().getSummonOwner(event.getDamager());
            Player victimOwner = plugin.getSummonManager().getSummonOwner(event.getEntity());
            if (damagerOwner != null && damagerOwner.equals(victimOwner)) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onEntityDamageGeneric(EntityDamageEvent event) {
        if (plugin.getSummonManager().isSummon(event.getEntity())) {
            EntityDamageEvent.DamageCause cause = event.getCause();
            
            // Cancel all fire-related damage
            if (cause == EntityDamageEvent.DamageCause.FIRE || 
                cause == EntityDamageEvent.DamageCause.FIRE_TICK || 
                cause == EntityDamageEvent.DamageCause.LAVA ||
                cause == EntityDamageEvent.DamageCause.HOT_FLOOR) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        // Prevent summons from catching fire
        if (plugin.getSummonManager().isSummon(event.getEntity())) {
            event.setCancelled(true);
        }
    }
}