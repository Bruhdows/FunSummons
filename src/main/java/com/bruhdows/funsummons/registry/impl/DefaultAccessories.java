package com.bruhdows.funsummons.registry.impl;

import com.bruhdows.funsummons.model.AccessoryConfig;
import com.bruhdows.funsummons.model.Stats;
import com.bruhdows.funsummons.registry.DefaultProvider;
import org.bukkit.Material;

import java.util.Arrays;

public class DefaultAccessories implements DefaultProvider<AccessoryConfig> {
    
    @Override
    public AccessoryConfig[] getDefaults() {
        return new AccessoryConfig[] {
            new AccessoryConfig(
                "mana_crystal",
                "<aqua>Mana Crystal</aqua>",
                Arrays.asList(
                    "<gray>A crystallized source of mana</gray>",
                    "",
                    "<aqua>+50 Max Mana</aqua>",
                    "<aqua>+0.5 Mana Regen/s</aqua>"
                ),
                Material.DIAMOND,
                0,
                new Stats(50, 0.5, 0, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0)
            ),
            new AccessoryConfig(
                "summoner_charm",
                "<yellow>Summoner's Charm</yellow>",
                Arrays.asList(
                    "<gray>Increases your summoning capacity</gray>",
                    "",
                    "<yellow>+1 Summon Slot</yellow>"
                ),
                Material.NETHER_STAR,
                0,
                new Stats(0, 0.0, 1, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0)
            ),
            new AccessoryConfig(
                "spirit_band",
                "<green>Spirit Band</green>",
                Arrays.asList(
                    "<gray>Empowers your summons</gray>",
                    "",
                    "<green>+15% Summon Damage</green>",
                    "<green>+10% Summon Health</green>"
                ),
                Material.EMERALD,
                0,
                new Stats(0, 0.0, 0, 0.15, 0.10, 0, 0, 0.0, 0.0, 0.0, 0.0)
            ),
            new AccessoryConfig(
                "necromancer_ring",
                "<dark_red>Necromancer's Ring</dark_red>",
                Arrays.asList(
                    "<gray>Ancient ring of dark power</gray>",
                    "",
                    "<aqua>+75 Max Mana</aqua>",
                    "<green>+20% Summon Damage</green>"
                ),
                Material.NETHERITE_INGOT,
                0,
                new Stats(75, 0.0, 0, 0.20, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0)
            ),
            new AccessoryConfig(
                "vitality_amulet",
                "<light_purple>Vitality Amulet</light_purple>",
                Arrays.asList(
                    "<gray>Strengthens your summons' life force</gray>",
                    "",
                    "<green>+25% Summon Health</green>",
                    "<aqua>+1.0 Mana Regen/s</aqua>"
                ),
                Material.REDSTONE,
                0,
                new Stats(0, 1.0, 0, 0.0, 0.25, 0, 0, 0.0, 0.0, 0.0, 0.0)
            ),
            new AccessoryConfig(
                "ring_of_haste",
                "<yellow>Ring of Haste</yellow>",
                Arrays.asList(
                    "<gray>Reduces wand cooldowns</gray>",
                    "",
                    "<aqua>+15% Cooldown Reduction</aqua>"
                ),
                Material.GOLD_INGOT,
                0,
                new Stats(0, 0.0, 0, 0.0, 0.0, 15, 0, 0.0, 0.0, 0.0, 0.0)
            ),
            new AccessoryConfig(
                "amulet_of_longevity",
                "<green>Amulet of Longevity</green>",
                Arrays.asList(
                    "<gray>Extends summon duration</gray>",
                    "",
                    "<aqua>+30s Summon Duration</aqua>"
                ),
                Material.CLOCK,
                0,
                new Stats(0, 0.0, 0, 0.0, 0.0, 0, 30, 0.0, 0.0, 0.0, 0.0)
            ),
            new AccessoryConfig(
                "lucky_charm",
                "<green>Lucky Charm</green>",
                Arrays.asList(
                    "<gray>Increases critical hit chance</gray>",
                    "",
                    "<aqua>+10% Crit Chance</aqua>"
                ),
                Material.CLAY_BALL,
                0,
                new Stats(0, 0.0, 0, 0.0, 0.0, 0, 0, 0.10, 0.0, 0.0, 0.0)
            ),
            new AccessoryConfig(
                "iron_boots_charm",
                "<gray>Iron Boots Charm</gray>",
                Arrays.asList(
                    "<gray>Prevents knockback</gray>",
                    "",
                    "<aqua>+25% Knockback Resistance</aqua>"
                ),
                Material.IRON_BOOTS,
                0,
                new Stats(0, 0.0, 0, 0.0, 0.0, 0, 0, 0.0, 0.25, 0.0, 0.0)
            ),
            new AccessoryConfig(
                "swift_boots_charm",
                "<aqua>Swift Boots Charm</aqua>",
                Arrays.asList(
                    "<gray>Increases movement speed</gray>",
                    "",
                    "<aqua>+15% Movement Speed</aqua>"
                ),
                Material.LEATHER_BOOTS,
                0,
                new Stats(0, 0.0, 0, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.15, 0.0)
            ),
            new AccessoryConfig(
                "shrinking_charm",
                "<dark_gray>Shrinking Charm</dark_gray>",
                Arrays.asList(
                    "<gray>Makes summons tiny but deadly</gray>",
                    "<red>-30% Summon Size</red>",
                    "<green>+25% Summon Damage</green>",
                    "<red>-20% Summon Health</red>"
                ),
                Material.FERMENTED_SPIDER_EYE,
                0,
                new Stats(0, 0.0, 0, 0.25, -0.20, 0, 0, 0.0, 0.0, 0.0, -0.30)
            ),
            new AccessoryConfig(
                "growth_charm",
                "<green>Growth Charm</green>",
                Arrays.asList(
                    "<gray>Makes summons huge and tanky</gray>",
                    "<green>+30% Summon Size</green>",
                    "<green>+25% Summon Health</green>",
                    "<red>-15% Summon Damage</red>"
                ),
                Material.BONE_MEAL,
                0,
                new Stats(0, 0.0, 0, -0.15, 0.25, 0, 0, 0.0, 0.0, 0.0, 0.30)
            )
        };
    }
    
    @Override
    public String getId(AccessoryConfig config) {
        return config.getId();
    }
}
