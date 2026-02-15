package com.bruhdows.funsummons.model;

public class Stats {
    private int maxMana;
    private double manaRegen;
    private int summonSlots;
    private double damageBoost;
    private double healthBoost;
    private int cooldownReduction;
    private int summonDuration;
    private double critChance;
    private double knockbackResistance;
    private double speedBoost;
    private double summonSize;
    
    public Stats() {
        this.maxMana = 0;
        this.manaRegen = 0.0;
        this.summonSlots = 0;
        this.damageBoost = 0.0;
        this.healthBoost = 0.0;
        this.cooldownReduction = 0;
        this.summonDuration = 0;
        this.critChance = 0.0;
        this.knockbackResistance = 0.0;
        this.speedBoost = 0.0;
        this.summonSize = 0.0;
    }
    
    public Stats(int maxMana, double manaRegen, int summonSlots, double damageBoost, double healthBoost) {
        this.maxMana = maxMana;
        this.manaRegen = manaRegen;
        this.summonSlots = summonSlots;
        this.damageBoost = damageBoost;
        this.healthBoost = healthBoost;
        this.cooldownReduction = 0;
        this.summonDuration = 0;
        this.critChance = 0.0;
        this.knockbackResistance = 0.0;
        this.speedBoost = 0.0;
        this.summonSize = 0.0;
    }
    
    public Stats(int maxMana, double manaRegen, int summonSlots, double damageBoost, double healthBoost, 
                 int cooldownReduction, int summonDuration, double critChance, double knockbackResistance, double speedBoost) {
        this.maxMana = maxMana;
        this.manaRegen = manaRegen;
        this.summonSlots = summonSlots;
        this.damageBoost = damageBoost;
        this.healthBoost = healthBoost;
        this.cooldownReduction = cooldownReduction;
        this.summonDuration = summonDuration;
        this.critChance = critChance;
        this.knockbackResistance = knockbackResistance;
        this.speedBoost = speedBoost;
        this.summonSize = 0.0;
    }
    
    public Stats(int maxMana, double manaRegen, int summonSlots, double damageBoost, double healthBoost, 
                 int cooldownReduction, int summonDuration, double critChance, double knockbackResistance, 
                 double speedBoost, double summonSize) {
        this.maxMana = maxMana;
        this.manaRegen = manaRegen;
        this.summonSlots = summonSlots;
        this.damageBoost = damageBoost;
        this.healthBoost = healthBoost;
        this.cooldownReduction = cooldownReduction;
        this.summonDuration = summonDuration;
        this.critChance = critChance;
        this.knockbackResistance = knockbackResistance;
        this.speedBoost = speedBoost;
        this.summonSize = summonSize;
    }
    
    public void add(Stats other) {
        this.maxMana += other.maxMana;
        this.manaRegen += other.manaRegen;
        this.summonSlots += other.summonSlots;
        this.damageBoost += other.damageBoost;
        this.healthBoost += other.healthBoost;
        this.cooldownReduction += other.cooldownReduction;
        this.summonDuration += other.summonDuration;
        this.critChance += other.critChance;
        this.knockbackResistance += other.knockbackResistance;
        this.speedBoost += other.speedBoost;
        this.summonSize += other.summonSize;
    }
    
    public int getMaxMana() { return maxMana; }
    public double getManaRegen() { return manaRegen; }
    public int getSummonSlots() { return summonSlots; }
    public double getDamageBoost() { return damageBoost; }
    public double getHealthBoost() { return healthBoost; }
    public int getCooldownReduction() { return cooldownReduction; }
    public int getSummonDuration() { return summonDuration; }
    public double getCritChance() { return critChance; }
    public double getKnockbackResistance() { return knockbackResistance; }
    public double getSpeedBoost() { return speedBoost; }
    public double getSummonSize() { return summonSize; }
}
