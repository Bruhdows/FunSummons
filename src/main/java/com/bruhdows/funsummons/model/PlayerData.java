package com.bruhdows.funsummons.model;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    private double mana;
    private double maxMana;
    private double manaRegen;
    private int summonSlots;
    private final Map<Integer, String> accessories;
    private int cooldownReduction;
    private int summonDuration;
    private double critChance;
    private double knockbackResistance;
    private double speedBoost;
    private double summonSize;
    
    public PlayerData() {
        this.mana = 100;
        this.maxMana = 100;
        this.manaRegen = 1.0;
        this.summonSlots = 2;
        this.accessories = new HashMap<>();
        this.cooldownReduction = 0;
        this.summonDuration = 0;
        this.critChance = 0.0;
        this.knockbackResistance = 0.0;
        this.speedBoost = 0.0;
        this.summonSize = 0.0;
    }
    
    public double getMana() { return mana; }
    public void setMana(double mana) { this.mana = Math.max(0, Math.min(mana, maxMana)); }
    public double getMaxMana() { return maxMana; }
    public void setMaxMana(double maxMana) { this.maxMana = maxMana; }
    public double getManaRegen() { return manaRegen; }
    public void setManaRegen(double manaRegen) { this.manaRegen = manaRegen; }
    public int getSummonSlots() { return summonSlots; }
    public void setSummonSlots(int summonSlots) { this.summonSlots = summonSlots; }
    public Map<Integer, String> getAccessories() { return accessories; }
    
    public int getCooldownReduction() { return cooldownReduction; }
    public void setCooldownReduction(int cooldownReduction) { this.cooldownReduction = cooldownReduction; }
    public int getSummonDuration() { return summonDuration; }
    public void setSummonDuration(int summonDuration) { this.summonDuration = summonDuration; }
    public double getCritChance() { return critChance; }
    public void setCritChance(double critChance) { this.critChance = critChance; }
    public double getKnockbackResistance() { return knockbackResistance; }
    public void setKnockbackResistance(double knockbackResistance) { this.knockbackResistance = knockbackResistance; }
    public double getSpeedBoost() { return speedBoost; }
    public void setSpeedBoost(double speedBoost) { this.speedBoost = speedBoost; }
    public double getSummonSize() { return summonSize; }
    public void setSummonSize(double summonSize) { this.summonSize = summonSize; }
    
    public void addMana(double amount) {
        setMana(mana + amount);
    }
    
    public boolean consumeMana(double amount) {
        if (mana >= amount) {
            setMana(mana - amount);
            return true;
        }
        return false;
    }
}
