package com.bruhdows.funsummons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SummonConfig {
    private String id;
    private String name;
    private String entityType;
    private double health;
    private double damage;
    private double speed;
    private int manaCost;
    private String displayName;
}