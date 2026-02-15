package com.bruhdows.funsummons.registry.impl;

import com.bruhdows.funsummons.model.Stats;
import com.bruhdows.funsummons.model.WandConfig;
import com.bruhdows.funsummons.registry.DefaultProvider;

import java.util.Arrays;

public class DefaultWands implements DefaultProvider<WandConfig> {
    
    @Override
    public WandConfig[] getDefaults() {
        return new WandConfig[] {
            new WandConfig(
                "imp_wand",
                "<red>Imp Summoning Wand</red>",
                Arrays.asList(
                    "<gray>Summons fiery imps to fight for you</gray>",
                    "",
                    "<aqua>+100 Max Mana</aqua>",
                    "<aqua>+1.0 Mana Regen/s</aqua>",
                    "<yellow>2 Summon Slots</yellow>",
                    "",
                    "<gold>Summon Cost: 20 Mana</gold>"
                ),
                "BLAZE_ROD",
                0,
                new Stats(100, 1.0, 2, 0.0, 0.0),
                "imp",
                20
            ),
            new WandConfig(
                "skeleton_wand",
                "<gray>Skeleton Summoning Wand</gray>",
                Arrays.asList(
                    "<gray>Summons skeleton archers to your aid</gray>",
                    "",
                    "<aqua>+120 Max Mana</aqua>",
                    "<aqua>+1.2 Mana Regen/s</aqua>",
                    "<yellow>2 Summon Slots</yellow>",
                    "",
                    "<gold>Summon Cost: 25 Mana</gold>"
                ),
                "BONE",
                0,
                new Stats(120, 1.2, 2, 0.0, 0.0),
                "skeleton_archer",
                25
            ),
            new WandConfig(
                "slime_wand",
                "<green>Slime Summoning Wand</green>",
                Arrays.asList(
                    "<gray>Summons bouncy slime minions</gray>",
                    "",
                    "<aqua>+150 Max Mana</aqua>",
                    "<aqua>+2.0 Mana Regen/s</aqua>",
                    "<yellow>3 Summon Slots</yellow>",
                    "",
                    "<gold>Summon Cost: 15 Mana</gold>"
                ),
                "SLIME_BALL",
                0,
                new Stats(150, 2.0, 3, 0.0, 0.0),
                "slime_minion",
                15
            ),
            new WandConfig(
                "phantom_wand",
                "<dark_purple>Phantom Summoning Wand</dark_purple>",
                Arrays.asList(
                    "<gray>Summons ethereal phantom wraiths</gray>",
                    "",
                    "<aqua>+140 Max Mana</aqua>",
                    "<aqua>+1.5 Mana Regen/s</aqua>",
                    "<yellow>2 Summon Slots</yellow>",
                    "",
                    "<gold>Summon Cost: 30 Mana</gold>"
                ),
                "PHANTOM_MEMBRANE",
                0,
                new Stats(140, 1.5, 2, 0.0, 0.0),
                "phantom_wraith",
                30
            )
        };
    }
    
    @Override
    public String getId(WandConfig config) {
        return config.getId();
    }
}