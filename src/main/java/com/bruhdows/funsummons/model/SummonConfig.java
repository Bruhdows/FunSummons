package com.bruhdows.funsummons.model;

public class SummonConfig {
    private String id;
    private String name;
    private String entityType;
    private double health;
    private double damage;
    private double speed;
    private int manaCost;
    private String displayName;

    public SummonConfig() {}

    public SummonConfig(String id, String name, String entityType, double health, double damage,
                        double speed, int manaCost, String displayName) {
        this.id = id;
        this.name = name;
        this.entityType = entityType;
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.manaCost = manaCost;
        this.displayName = displayName;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEntityType() { return entityType; }
    public double getHealth() { return health; }
    public double getDamage() { return damage; }
    public double getSpeed() { return speed; }
    public int getManaCost() { return manaCost; }
    public String getDisplayName() { return displayName; }
}