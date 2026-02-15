package com.bruhdows.funsummons.registry.impl;

import com.bruhdows.funsummons.model.SummonConfig;
import com.bruhdows.funsummons.registry.DefaultProvider;

public class DefaultSummons implements DefaultProvider<SummonConfig> {
    
    @Override
    public SummonConfig[] getDefaults() {
        return new SummonConfig[] {
            new SummonConfig(
                "imp",
                "Imp",
                "ZOMBIE",
                20.0,
                3.0,
                0.3,
                20,
                "<red>Imp</red>"
            ),
            new SummonConfig(
                "skeleton_archer",
                "Skeleton Archer",
                "SKELETON",
                15.0,
                4.0,
                0.25,
                25,
                "<gray>Skeleton Archer</gray>"
            ),
            new SummonConfig(
                "slime_minion",
                "Slime Minion",
                "SLIME",
                25.0,
                2.0,
                0.6,
                15,
                "<green>Slime Minion</green>"
            ),
            new SummonConfig(
                "phantom_wraith",
                "Phantom Wraith",
                "PHANTOM",
                18.0,
                5.0,
                0.35,
                30,
                "<dark_purple>Phantom Wraith</dark_purple>"
            ),
            new SummonConfig(
                "iron_golem",
                "Iron Golem",
                "IRON_GOLEM",
                100.0,
                8.0,
                0.25,
                50,
                "<white>Iron Golem</white>"
            ),
            new SummonConfig(
                "warden",
                "Warden",
                "WARDEN",
                500.0,
                30.0,
                0.3,
                150,
                "<dark_blue>Warden</dark_blue>"
            ),
            new SummonConfig(
                "silverfish_swarm",
                "Silverfish Swarm",
                "SILVERFISH",
                8.0,
                3.0,
                0.35,
                12,
                "<gray>Silverfish Swarm</gray>"
            ),
            new SummonConfig(
                "fairy_companion",
                "Fairy Companion",
                "ALLAY",
                200.0,
                0.0,
                0.5,
                300,
                "<light_purple>Fairy Companion</light_purple>"
            )
        };
    }
    
    @Override
    public String getId(SummonConfig config) {
        return config.getId();
    }
}
