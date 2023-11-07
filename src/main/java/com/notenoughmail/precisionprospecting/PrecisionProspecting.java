/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting;

import com.mojang.logging.LogUtils;
import com.notenoughmail.precisionprospecting.config.PrecProsConfig;
import com.notenoughmail.precisionprospecting.integration.vexxedvisuals.ModFilePackResources;
import com.notenoughmail.precisionprospecting.items.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import org.slf4j.Logger;

@Mod(PrecisionProspecting.MODID)
public class PrecisionProspecting {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "precisionprospecting";
    public static final String MOD_NAME = "Precision Prospecting";

    public PrecisionProspecting() {

        Registration.init();
        PrecProsConfig.register();

        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(this::addPackFinders);
    }

    private void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFileInfo modFileInfo = ModList.get().getModFileById(PrecisionProspecting.MODID);
            if (modFileInfo == null) {
                PrecisionProspecting.LOGGER.error("Could not find Precision Prospecting mod file info; built-in resource packs will be missing");
                return;
            }
            IModFile modFile = modFileInfo.getFile();
            event.addRepositorySource((consumer, constructor) -> {
                consumer.accept(Pack.m_10430_(new ResourceLocation(PrecisionProspecting.MODID, "vexxed_items").toString(), false, () -> new ModFilePackResources("Vexxed Visuals: Precision Prospecting", modFile, "resourcepacks/vexxed_compat"), constructor, Pack.Position.TOP, PackSource.DEFAULT));
            });
        }
    }
}
