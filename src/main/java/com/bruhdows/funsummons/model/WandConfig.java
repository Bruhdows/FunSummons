package com.bruhdows.funsummons.model;

import lombok.Getter;

import java.util.List;

@Getter
public class WandConfig {
    private String id;
    private String name;
    private List<String> lore;
    private String material;
    private int customModelData;
    private Stats stats;
    private String summonType;
    private int summonCost;

    public WandConfig() {}

    public WandConfig(String id, String name, List<String> lore, String material, int customModelData, Stats stats, String summonType, int summonCost) {
        this.id = id;
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.customModelData = customModelData;
        this.stats = stats;
        this.summonType = summonType;
        this.summonCost = summonCost;
    }
}