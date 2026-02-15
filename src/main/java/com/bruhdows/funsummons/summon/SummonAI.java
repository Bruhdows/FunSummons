package com.bruhdows.funsummons.summon;

import com.bruhdows.funsummons.FunSummonsPlugin;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

public class SummonAI {
    
    private final FunSummonsPlugin plugin;
    private final LivingEntity summon;
    private final Player owner;

    private LivingEntity target;
    private int orbitAngle;
    private static final double ORBIT_RADIUS = 2.5;
    private static final double TELEPORT_DISTANCE = 50.0;

    public SummonAI(FunSummonsPlugin plugin, LivingEntity summon, Player owner) {
        this.plugin = plugin;
        this.summon = summon;
        this.owner = owner;
        this.orbitAngle = (int) (Math.random() * 360);
    }
    
    public void tick() {
        if (!summon.isValid() || !owner.isOnline()) {
            summon.remove();
            return;
        }
        
        // Safety check: If summon somehow has owner as target, clear it immediately
        if (summon instanceof Mob mob) {
            if (mob.getTarget() != null && mob.getTarget().equals(owner)) {
                mob.setTarget(null);
            }
        }
        
        // Teleport if too far
        double distance = summon.getLocation().distance(owner.getLocation());
        if (distance > TELEPORT_DISTANCE) {
            summon.teleport(owner.getLocation());
            return;
        }
        
        // Find target
        if (target == null || !target.isValid() || target.isDead()) {
            target = findTarget();
        }
        
        // Double check target isn't owner before setting
        if (target != null && target.equals(owner)) {
            target = null;
        }
        
        if (target != null && summon instanceof Mob mob) {
            // Attack target (only if it's not the owner)
            mob.setTarget(target);
        } else {
            // Follow owner and orbit
            if (summon instanceof Mob mob) {
                mob.setTarget(null);
                
                // Calculate orbit position
                orbitAngle = (orbitAngle + 2) % 360;
                double radians = Math.toRadians(orbitAngle);
                
                Location ownerLoc = owner.getLocation();
                double x = ownerLoc.getX() + ORBIT_RADIUS * Math.cos(radians);
                double z = ownerLoc.getZ() + ORBIT_RADIUS * Math.sin(radians);
                
                // Get the ground level at target position
                Location targetLoc = new Location(ownerLoc.getWorld(), x, ownerLoc.getY(), z);
                Location groundLoc = getGroundLocation(targetLoc);
                
                // Only pathfind if far enough from orbit position
                double distanceToOrbit = summon.getLocation().distance(groundLoc);
                if (distanceToOrbit > 1.5) {
                    mob.getPathfinder().moveTo(groundLoc);
                }
            }
        }
    }
    
    private Location getGroundLocation(Location loc) {
        Location check = loc.clone();
        
        // Search down for solid ground (max 10 blocks)
        for (int i = 0; i < 10; i++) {
            if (check.getBlock().getType().isSolid()) {
                return check.add(0, 1, 0); // One block above the solid block
            }
            check.subtract(0, 1, 0);
        }
        
        // Search up for solid ground (max 10 blocks)
        check = loc.clone();
        for (int i = 0; i < 10; i++) {
            if (check.getBlock().getType().isSolid()) {
                return check.add(0, 1, 0);
            }
            check.add(0, 1, 0);
        }
        
        // If no ground found, return original location
        return loc;
    }
    
    private LivingEntity findTarget() {
        // Check if owner is targeting something
        if (owner.getTargetEntity(10) instanceof LivingEntity entity) {
            if (isValidTarget(entity)) {
                return entity;
            }
        }
        
        // Find nearby hostile mobs or players attacking owner
        for (LivingEntity entity : summon.getLocation().getNearbyLivingEntities(15)) {
            if (isValidTarget(entity)) {
                // Check if this entity recently damaged the owner
                if (entity instanceof Mob mob) {
                    if (mob.getTarget() != null && mob.getTarget().equals(owner)) {
                        return entity;
                    }
                }
            }
        }
        
        return null;
    }
    
    private boolean isValidTarget(LivingEntity entity) {
        // CRITICAL: Never target the owner
        if (entity.equals(owner)) return false;
        if (entity.equals(summon)) return false;
        if (entity.isDead()) return false;
        
        // Don't target friendly summons
        if (plugin.getSummonManager().isSummon(entity)) {
            Player summonOwner = plugin.getSummonManager().getSummonOwner(entity);
            if (summonOwner != null && summonOwner.equals(owner)) {
                return false;
            }
        }
        
        return entity instanceof Mob || entity instanceof Player;
    }
}