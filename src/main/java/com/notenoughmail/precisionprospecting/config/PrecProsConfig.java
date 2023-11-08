/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.config;

import net.dries007.tfc.util.Alloy;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class PrecProsConfig {

    private static ForgeConfigSpec GENERAL_SPEC;

    public static void register() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        PrecProsConfig.registerServerConfig(builder);
        GENERAL_SPEC = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PrecProsConfig.GENERAL_SPEC, "precision-prospecting.toml");
    }

    public static ForgeConfigSpec.IntValue moldProsHammerCapacity;
    public static ForgeConfigSpec.IntValue moldProsDrillCapacity;
    public static ForgeConfigSpec.IntValue moldMineralProspectorCapacity;

    public static ForgeConfigSpec.IntValue prosHammerPrimaryRadius;
    public static ForgeConfigSpec.IntValue prosHammerSecondaryRadius;
    public static ForgeConfigSpec.IntValue prosHammerDisplacement;

    public static ForgeConfigSpec.IntValue prosDrillPrimaryRadius;
    public static ForgeConfigSpec.IntValue prosDrillSecondaryRadius;
    public static ForgeConfigSpec.IntValue prosDrillDisplacement;

    public static ForgeConfigSpec.IntValue minProsPrimaryRadius;
    public static ForgeConfigSpec.IntValue minProsSecondaryRadius;
    public static ForgeConfigSpec.IntValue minProsDisplacement;

    private static void registerServerConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("This is the config for Precision Prospecting");
        builder.push("molds");

        moldProsHammerCapacity = builder
                .comment("Tank capacity of a Prospector's Hammer head mold (in mB).")
                .defineInRange("moldProsHammerCapacity", 200, 0, Alloy.MAX_ALLOY);

        moldProsDrillCapacity = builder
                .comment("Tank capacity of a Prospector's Drill head mold (in mB).")
                .defineInRange("moldProsDrillCapacity", 400, 0, Alloy.MAX_ALLOY);

        moldMineralProspectorCapacity = builder
                .comment("Tank capacity of a Mineral Prospector head mold (in mB).")
                .defineInRange("moldMineralProspectorCapacity", 200, 0, Alloy.MAX_ALLOY );

        // TODO: better explanations/comments
        builder.pop().push("tools");
        builder.push("prosHammer");

        prosHammerPrimaryRadius = builder
                .comment("The distance the Prospector's Hammer will scan in non-axial directions")
                .defineInRange("prosHammerPrimaryRadius", 6, 0, 25);

        prosHammerSecondaryRadius = builder
                .comment("The distance the Prospector's Hammer will scan in the axial directions")
                .defineInRange("prosHammerSecondaryRadius", 6, 0, 25);

        prosHammerDisplacement = builder
                .comment("The distance to offset the Prospector's Hammer's scan along the axial direction")
                .defineInRange("prosHammerDisplacement", 0, 0, 25);

        builder.pop().push("prosDrill");

        prosDrillPrimaryRadius = builder
                .comment("The distance the Prospector's Drill will scan in non-axial directions")
                .defineInRange("prosDrillPrimaryRadius", 3, 0, 25);

        prosDrillSecondaryRadius = builder
                .comment("The distance the Prospector's Drill will scan in the axial directions")
                .defineInRange("prosDrillSecondaryRadius", 12, 0, 25);

        prosDrillDisplacement = builder
                .comment("The distance to offset the Prospector's Drill's scan along the axial direction")
                .defineInRange("prosDrillDisplacement", 10, 0, 25);

        builder.pop().push("minPros");

        minProsPrimaryRadius = builder
                .comment("The distance the Mineral Prospector will scan in non-axial directions")
                .defineInRange("minProspectorPrimaryRadius", 22, 0, 25);

        minProsSecondaryRadius = builder
                .comment("The distance the Mineral Prospector will scan in the axial directions")
                .defineInRange("minProspectorSecondaryRadius", 22, 0, 25);

        minProsDisplacement = builder
                .comment("The distance to offset the Mineral Prospector's scan along the axial direction")
                .defineInRange("minProspectorDisplacement", 0, 0, 25);

        builder.pop(2);
    }
}
