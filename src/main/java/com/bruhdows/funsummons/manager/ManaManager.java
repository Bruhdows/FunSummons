package com.bruhdows.funsummons.manager;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.model.PlayerData;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ManaManager {
    private final FunSummonsPlugin plugin;
    private final Map<UUID, PlayerData> playerData = new HashMap<>();
    private final Map<UUID, BossBar> manaBars = new HashMap<>();
    private BukkitTask regenTask;
    
    public ManaManager(FunSummonsPlugin plugin) {
        this.plugin = plugin;
        startManaRegen();
    }
    
    private void startManaRegen() {
        regenTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = getPlayerData(player);
                data.addMana(data.getManaRegen() / 20.0);
                updateManaBar(player);
            }
        }, 0L, 1L);
    }
    
    public void loadPlayerDataAsync(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerData data = loadPlayerData(player.getUniqueId());
                
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (data != null) {
                            playerData.put(player.getUniqueId(), data);
                        } else {
                            playerData.put(player.getUniqueId(), new PlayerData());
                        }
                        plugin.getAccessoryManager().recalculateStats(player);
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);
    }
    
    public PlayerData getPlayerData(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(), k -> {
            PlayerData data = loadPlayerData(player.getUniqueId());
            return data != null ? data : new PlayerData();
        });
    }
    
    public void savePlayerData(UUID uuid) {
        PlayerData data = playerData.get(uuid);
        if (data == null) return;
        
        File file = new File(plugin.getDataFolder(), "playerdata/" + uuid + ".json");
        file.getParentFile().mkdirs();
        
        try (Writer writer = new FileWriter(file)) {
            plugin.getGson().toJson(data, writer);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save player data: " + uuid);
        }
    }
    
    private PlayerData loadPlayerData(UUID uuid) {
        File file = new File(plugin.getDataFolder(), "playerdata/" + uuid + ".json");
        if (!file.exists()) return null;
        
        try (Reader reader = new FileReader(file)) {
            return plugin.getGson().fromJson(reader, PlayerData.class);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to load player data: " + uuid);
            return null;
        }
    }
    
    public void saveAllPlayerData() {
        for (UUID uuid : playerData.keySet()) {
            savePlayerData(uuid);
        }
    }
    
    public void updateManaBar(Player player) {
        if (!plugin.getItemUtil().isHoldingSummonerItem(player)) {
            hideManaBar(player);
            return;
        }
        
        PlayerData data = getPlayerData(player);
        BossBar bar = manaBars.get(player.getUniqueId());
        
        if (bar == null) {
            bar = BossBar.bossBar(
                Component.text("Mana"),
                (float) (data.getMana() / data.getMaxMana()),
                BossBar.Color.BLUE,
                BossBar.Overlay.PROGRESS
            );
            manaBars.put(player.getUniqueId(), bar);
            player.showBossBar(bar);
        }
        
        bar.name(plugin.getMiniMessage().deserialize(
            String.format("<aqua>Mana: <white>%.0f</white> / <white>%.0f</white></aqua>", 
                data.getMana(), data.getMaxMana())
        ));
        bar.progress((float) Math.max(0, Math.min(1, data.getMana() / data.getMaxMana())));
    }
    
    public void hideManaBar(Player player) {
        BossBar bar = manaBars.remove(player.getUniqueId());
        if (bar != null) {
            player.hideBossBar(bar);
        }
    }
    
    public void clearAll() {
        if (regenTask != null) {
            regenTask.cancel();
        }
        saveAllPlayerData();
        for (Player player : Bukkit.getOnlinePlayers()) {
            hideManaBar(player);
        }
        playerData.clear();
        manaBars.clear();
    }
}
