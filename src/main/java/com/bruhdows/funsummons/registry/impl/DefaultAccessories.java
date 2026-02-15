package com.bruhdows.funsummons.registry.impl;

import com.bruhdows.funsummons.model.AccessoryConfig;
import com.bruhdows.funsummons.model.Stats;
import com.bruhdows.funsummons.registry.DefaultProvider;

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
                "DIAMOND",
                0,
                new Stats(50, 0.5, 0, 0.0, 0.0)
            ),
            new AccessoryConfig(
                "summoner_charm",
                "<yellow>Summoner's Charm</yellow>",
                Arrays.asList(
                    "<gray>Increases your summoning capacity</gray>",
                    "",
                    "<yellow>+1 Summon Slot</yellow>"
                ),
                "NETHER_STAR",
                0,
                new Stats(0, 0.0, 1, 0.0, 0.0)
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
                "EMERALD",
                0,
                new Stats(0, 0.0, 0, 0.15, 0.10)
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
                "NETHERITE_INGOT",
                0,
                new Stats(75, 0.0, 0, 0.20, 0.0)
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
                "REDSTONE",
                0,
                new Stats(0, 1.0, 0, 0.0, 0.25)
            )
        };
    }
    
    @Override
    public String getId(AccessoryConfig config) {
        return config.getId();
    }
}