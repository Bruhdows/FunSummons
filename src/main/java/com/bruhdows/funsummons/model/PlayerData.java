package com.bruhdows.funsummons.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
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

    public void setMana(double mana) { this.mana = Math.max(0, Math.min(mana, maxMana)); }

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
