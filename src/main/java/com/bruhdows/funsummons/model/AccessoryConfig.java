package com.bruhdows.funsummons.model;

import com.bruhdows.funsummons.model.Stats;

import java.util.List;

public class AccessoryConfig {
    private String id;
    private String name;
    private List<String> lore;
    private String material;
    private int customModelData;
    private Stats stats;

    public AccessoryConfig() {}

    public AccessoryConfig(String id, String name, List<String> lore, String material, int customModelData, Stats stats) {
        this.id = id;
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.customModelData = customModelData;
        this.stats = stats;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<String> getLore() { return lore; }
    public String getMaterial() { return material; }
    public int getCustomModelData() { return customModelData; }
    public Stats getStats() { return stats; }
}