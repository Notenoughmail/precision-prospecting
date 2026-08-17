package io.github.notenoughmail.precisionprospecting;

import net.dries007.tfc.util.FluidAlloy;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;

import static net.neoforged.neoforge.common.ModConfigSpec.Builder;
import static net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class PrecProsConfig {

    public final IntValue
        prosHammerMoldCapacity,
        prosHammerPrimaryRadius,
        prosHammerSecondaryRadius,
        prosHammerDisplacement,

        prosDrillMoldCapcaity,
        prosDrillPrimaryRadius,
        prosDrillSecondaryRadius,
        prosDrillDisplacement,

        minProsMoldCapacity,
        minProsPrimaryRadius,
        minProsSecondaryRadius,
        minProsDisplacement;

    public PrecProsConfig(ModContainer container) {
        final Builder builder = new Builder();
        builder.push("prospector-hammer");
        prosHammerMoldCapacity = val(builder, "Tank capacity for the Prospector's Hammer Mold", "prosHammerMoldCapacity", 200, FluidAlloy.MAX_ALLOY);
        prosHammerPrimaryRadius = val(builder, "The primary radius of Prospector's Hammers", "prosHammerPrimaryRadius", 6, 25);
        prosHammerSecondaryRadius = val(builder, "The secondary radius of Prospector's Hammers", "prosHammerSecondaryRadius", 6 , 25);
        prosHammerDisplacement = val(builder, "The displacement of Prospector's Hammers", "prosHammerDisplacement", 0, 25);
        builder.pop().push("prospector-drill");
        prosDrillMoldCapcaity = val(builder, "Tank capacity for the Prospector's Drill Mold", "prosDrillMoldCapacity", 400, FluidAlloy.MAX_ALLOY);
        prosDrillPrimaryRadius = val(builder, "The primary radius of Prospector's Drills", "prosDrillPrimaryRadius", 3, 25);
        prosDrillSecondaryRadius = val(builder, "The secondary radius of Prospector's Drills", "prosDrillSecondaryRadius", 12, 25);
        prosDrillDisplacement = val(builder, "The displacement of Prospector's Drills", "prosDrillDisplacement", 10, 25);
        builder.pop().push("mineral-prospector");
        minProsMoldCapacity = val(builder, "Tank capacity for the Mineral Prospector Mold", "minProsMoldCapacity", 200, FluidAlloy.MAX_ALLOY);
        minProsPrimaryRadius = val(builder, "The primary radius of Mineral Prospectors", "minProsPrimaryRadius", 22, 25);
        minProsSecondaryRadius = val(builder, "The secondary radius of Mineral Prospectors", "minProsSecondaryRadius", 22, 25);
        minProsDisplacement = val(builder, "The displacement of Mineral Prospectors", "minProsDisplacement", 0, 25);
        builder.pop();
        container.registerConfig(ModConfig.Type.SERVER, builder.build(), "precision-prospecting.toml");
    }

    private static IntValue val(Builder builder, String comment, String name, int def, int max) {
        return builder.comment(comment).defineInRange(name, def, 0, max);
    }
}
