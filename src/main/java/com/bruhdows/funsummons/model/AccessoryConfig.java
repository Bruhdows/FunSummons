package com.bruhdows.funsummons.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.List;

@Getter
@Setter
public class AccessoryConfig {
    private String id;
    private String name;
    private List<String> lore;
    private Material material;
    private int customModelData;
    private Stats stats;

    public AccessoryConfig() {}

    public AccessoryConfig(String id, String name, List<String> lore, Material material, int customModelData, Stats stats) {
        this.id = id;
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.customModelData = customModelData;
        this.stats = stats;
    }
}
