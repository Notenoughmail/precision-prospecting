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

    private static void registerServerConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("This is the config for Precision Prospecting");
        builder.push("molds");

        moldProsHammerCapacity = builder
                .comment("Tank capacity of a Prospector's Hammer mold (in mB).")
                .defineInRange("moldProsHammerCapacity", 200, 0, Alloy.MAX_ALLOY);

        builder.pop();
    }
}
