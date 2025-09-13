package io.github.notenoughmail.precisionprospecting;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;

import net.dries007.tfc.util.FluidAlloy;

import static net.neoforged.neoforge.common.ModConfigSpec.*;

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
        builder.push("prospecting-hammer");
        prosHammerMoldCapacity = val(builder, "Tank capacity for the prospector hammer mold", "prosHammerMoldCapacity", 200, FluidAlloy.MAX_ALLOY);
        prosHammerPrimaryRadius = val(builder, "The primary radius of prospector hammers", "prosHammerPrimaryRadius", 6, 25);
        prosHammerSecondaryRadius = val(builder, "The secondary radius of prospector hammers", "prosHammerSecondaryRadius", 6 , 25);
        prosHammerDisplacement = val(builder, "The displacement of prospector hammers", "prosHammerDisplacement", 0, 25);
        builder.pop().push("prospecting-drill");
        prosDrillMoldCapcaity = val(builder, "Tank capacity for the prospector drill mold", "prosDrillMoldCapacity", 400, FluidAlloy.MAX_ALLOY);
        prosDrillPrimaryRadius = val(builder, "The primary radius of prospector drills", "prosDrillPrimaryRadius", 3, 25);
        prosDrillSecondaryRadius = val(builder, "The secondary radius of prospector drills", "prosDrillSecondaryRadius", 12, 25);
        prosDrillDisplacement = val(builder, "The displacement of prospector drills", "prosDrillDisplacement", 10, 25);
        builder.pop().push("mineral-prospector");
        minProsMoldCapacity = val(builder, "Tank capacity for the mineral prospector mold", "minProsMoldCapacity", 200, FluidAlloy.MAX_ALLOY);
        minProsPrimaryRadius = val(builder, "The primary radius of mineral prospectors", "minProsPrimaryRadius", 22, 25);
        minProsSecondaryRadius = val(builder, "The secondary radius of mineral prospectors", "minProsSecondaryRadius", 22, 25);
        minProsDisplacement = val(builder, "The displacement of mineral prospectors", "minProsDisplacement", 0, 25);
        builder.pop();
        container.registerConfig(ModConfig.Type.SERVER, builder.build(), "precision-prospecting.toml");
    }

    private static IntValue val(Builder builder, String comment, String name, int def, int max) {
        return builder.comment(comment).defineInRange(name, def, 0, max);
    }
}
