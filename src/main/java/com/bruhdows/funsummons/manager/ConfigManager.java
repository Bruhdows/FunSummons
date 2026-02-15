package com.bruhdows.funsummons.manager;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.model.AccessoryConfig;
import com.bruhdows.funsummons.model.SummonConfig;
import com.bruhdows.funsummons.model.WandConfig;
import com.bruhdows.funsummons.registry.DefaultProvider;
import com.bruhdows.funsummons.registry.impl.DefaultAccessories;
import com.bruhdows.funsummons.registry.impl.DefaultSummons;
import com.bruhdows.funsummons.registry.impl.DefaultWands;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final FunSummonsPlugin plugin;
    private final Map<String, SummonConfig> summons = new HashMap<>();
    private final Map<String, WandConfig> wands = new HashMap<>();
    private final Map<String, AccessoryConfig> accessories = new HashMap<>();
    
    private final DefaultProvider<SummonConfig> defaultSummons = new DefaultSummons();
    private final DefaultProvider<WandConfig> defaultWands = new DefaultWands();
    private final DefaultProvider<AccessoryConfig> defaultAccessories = new DefaultAccessories();
    
    public ConfigManager(FunSummonsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void loadAll() {
        loadConfigs("summons", summons, defaultSummons);
        loadConfigs("wands", wands, defaultWands);
        loadConfigs("accessories", accessories, defaultAccessories);
    }
    
    private <T> void loadConfigs(String folderName, Map<String, T> storage, DefaultProvider<T> provider) {
        File folder = new File(plugin.getDataFolder(), folderName);
        if (!folder.exists()) {
            folder.mkdirs();
            createDefaults(folder, provider);
        }
        
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try (Reader reader = new FileReader(file)) {
                    @SuppressWarnings("unchecked")
                    Class<T> clazz = (Class<T>) provider.getDefaults().getClass().getComponentType();
                    T config = plugin.getGson().fromJson(reader, clazz);
                    storage.put(provider.getId(config), config);
                } catch (IOException e) {
                    plugin.getLogger().warning("Failed to load " + folderName + ": " + file.getName());
                }
            }
        }
        plugin.getLogger().info("Loaded " + storage.size() + " " + folderName);
    }
    
    private <T> void createDefaults(File folder, DefaultProvider<T> provider) {
        for (T config : provider.getDefaults()) {
            File file = new File(folder, provider.getFileName(config));
            saveConfig(file, config);
        }
    }
    
    private void saveConfig(File file, Object config) {
        try (Writer writer = new FileWriter(file)) {
            plugin.getGson().toJson(config, writer);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save file: " + file.getName());
        }
    }
    
    public SummonConfig getSummon(String id) {
        return summons.get(id);
    }
    
    public WandConfig getWand(String id) {
        return wands.get(id);
    }
    
    public AccessoryConfig getAccessory(String id) {
        return accessories.get(id);
    }
    
    public Map<String, WandConfig> getAllWands() {
        return wands;
    }
    
    public Map<String, AccessoryConfig> getAllAccessories() {
        return accessories;
    }
}