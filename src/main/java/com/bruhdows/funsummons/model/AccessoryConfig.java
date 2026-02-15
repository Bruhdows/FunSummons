package com.bruhdows.funsummons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessoryConfig {
    private String id;
    private String name;
    private List<String> lore;
    private Material material;
    private int customModelData;
    private Stats stats;
}
