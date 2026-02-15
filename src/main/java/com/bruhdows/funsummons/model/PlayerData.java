package com.bruhdows.funsummons.model;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    private double mana;
    private double maxMana;
    private double manaRegen;
    private int summonSlots;
    private final Map<Integer, String> accessories;
    
    public PlayerData() {
        this.mana = 100;
        this.maxMana = 100;
        this.manaRegen = 1.0;
        this.summonSlots = 2;
        this.accessories = new HashMap<>();
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