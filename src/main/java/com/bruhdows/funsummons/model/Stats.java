package com.bruhdows.funsummons.model;

public class Stats {
    private int maxMana;
    private double manaRegen;
    private int summonSlots;
    private double damageBoost;
    private double healthBoost;
    
    public Stats() {
        this.maxMana = 0;
        this.manaRegen = 0.0;
        this.summonSlots = 0;
        this.damageBoost = 0.0;
        this.healthBoost = 0.0;
    }
    
    public Stats(int maxMana, double manaRegen, int summonSlots, double damageBoost, double healthBoost) {
        this.maxMana = maxMana;
        this.manaRegen = manaRegen;
        this.summonSlots = summonSlots;
        this.damageBoost = damageBoost;
        this.healthBoost = healthBoost;
    }
    
    public void add(Stats other) {
        this.maxMana += other.maxMana;
        this.manaRegen += other.manaRegen;
        this.summonSlots += other.summonSlots;
        this.damageBoost += other.damageBoost;
        this.healthBoost += other.healthBoost;
    }
    
    public int getMaxMana() { return maxMana; }
    public double getManaRegen() { return manaRegen; }
    public int getSummonSlots() { return summonSlots; }
    public double getDamageBoost() { return damageBoost; }
    public double getHealthBoost() { return healthBoost; }
}