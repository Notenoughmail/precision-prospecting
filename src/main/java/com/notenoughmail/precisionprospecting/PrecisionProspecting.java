/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting;

import com.mojang.logging.LogUtils;
import com.notenoughmail.precisionprospecting.items.Registration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

@Mod(PrecisionProspecting.MODID)
public class PrecisionProspecting {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "precisionprospecting";
    public static final String MOD_NAME = "Precision Prospecting";

    public PrecisionProspecting() {

        Registration.init();

        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
    }
}
