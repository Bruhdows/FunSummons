package com.bruhdows.funsummons.registry.impl;

import com.bruhdows.funsummons.model.WandConfig;
import com.bruhdows.funsummons.model.Stats;
import com.bruhdows.funsummons.registry.DefaultProvider;
import org.bukkit.Material;

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
                Material.BLAZE_ROD,
                0,
                new Stats(100, 1.0, 2, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                "imp",
                20,
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
                Material.BONE,
                0,
                new Stats(120, 1.2, 2, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                "skeleton_archer",
                25,
                30
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
                Material.SLIME_BALL,
                0,
                new Stats(150, 2.0, 3, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                "slime_minion",
                15,
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
                Material.PHANTOM_MEMBRANE,
                0,
                new Stats(140, 1.5, 2, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                "phantom_wraith",
                30,
                25
            ),
            new WandConfig(
                "golem_wand",
                "<white>Iron Golem Summoning Wand</white>",
                Arrays.asList(
                    "<gray>Summons mighty iron golems</gray>",
                    "",
                    "<aqua>+180 Max Mana</aqua>",
                    "<aqua>+1.0 Mana Regen/s</aqua>",
                    "<yellow>1 Summon Slot</yellow>",
                    "<green>+30% Summon Health</green>",
                    "",
                    "<gold>Summon Cost: 50 Mana</gold>"
                ),
                Material.IRON_INGOT,
                0,
                new Stats(180, 1.0, 1, 0.0, 0.30, 0, 0, 0.0, 0.0, 0.0, 0.0),
                "iron_golem",
                50,
                60
            ),
            new WandConfig(
                "warden_wand",
                "<dark_blue>Warden Summoning Wand</dark_blue>",
                Arrays.asList(
                    "<gray>Summons the OP Warden</gray>",
                    "<red>EXTREMELY POWERFUL</red>",
                    "",
                    "<aqua>+250 Max Mana</aqua>",
                    "<aqua>+3.0 Mana Regen/s</aqua>",
                    "<yellow>1 Summon Slot</yellow>",
                    "<green>+50% Summon Health</green>",
                    "<green>+40% Summon Damage</green>",
                    "",
                    "<gold>Summon Cost: 150 Mana</gold>"
                ),
                Material.ECHO_SHARD,
                0,
                new Stats(250, 3.0, 1, 0.40, 0.50, 0, 0, 0.0, 0.0, 0.0, 0.0),
                "warden",
                150,
                120
            ),
            new WandConfig(
                "silverfish_wand",
                "<gray>Silverfish Swarm Wand</gray>",
                Arrays.asList(
                    "<gray>Summons a swarm of aggressive silverfish</gray>",
                    "",
                    "<aqua>+120 Max Mana</aqua>",
                    "<aqua>+2.5 Mana Regen/s</aqua>",
                    "<yellow>4 Summon Slots</yellow>",
                    "",
                    "<gold>Summon Cost: 12 Mana</gold>"
                ),
                Material.STONE_PICKAXE,
                0,
                new Stats(120, 2.5, 4, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                "silverfish_swarm",
                12,
                10
            ),
            new WandConfig(
                "fairy_wand",
                "<light_purple>Fairy Wand</light_purple>",
                Arrays.asList(
                    "<gray>Summons a magical fairy companion</gray>",
                    "<gray>Right-click to mount and fly around</gray>",
                    "<gray>Left-click while riding to shoot magical fireballs</gray>",
                    "<red>Only 1 fairy per player</red>",
                    "<red>Despawns after 5 minutes</red>",
                    "",
                    "<aqua>+500 Max Mana</aqua>",
                    "<aqua>+5.0 Mana Regen/s</aqua>",
                    "<yellow>Special Flying Summon</yellow>",
                    "",
                    "<gold>Summon Cost: 300 Mana</gold>"
                ),
                Material.AMETHYST_SHARD,
                0,
                new Stats(500, 5.0, 1, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                "fairy_companion",
                300,
                600
            )
        };
    }
    
    @Override
    public String getId(WandConfig config) {
        return config.getId();
    }
}
