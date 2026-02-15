package com.bruhdows.funsummons.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.List;

@Getter
@Setter
public class WandConfig {
    private String id;
    private String name;
    private List<String> lore;
    private Material material;
    private int customModelData;
    private Stats stats;
    private String summonType;
    private int summonCost;
    private int cooldown;

    public WandConfig() {}

    public WandConfig(String id, String name, List<String> lore, Material material, int customModelData, Stats stats, String summonType, int summonCost) {
        this.id = id;
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.customModelData = customModelData;
        this.stats = stats;
        this.summonType = summonType;
        this.summonCost = summonCost;
        this.cooldown = 0;
    }
    
    public WandConfig(String id, String name, List<String> lore, Material material, int customModelData, Stats stats, String summonType, int summonCost, int cooldown) {
        this.id = id;
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.customModelData = customModelData;
        this.stats = stats;
        this.summonType = summonType;
        this.summonCost = summonCost;
        this.cooldown = cooldown;
    }
}
