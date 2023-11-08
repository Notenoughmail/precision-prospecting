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
import net.dries007.tfc.common.TFCCreativeTabs;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
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
        modbus.addListener(EventPriority.LOW, this::addItemsToCreativeTabs); // Hopefully ensure they're added after TFC's items
    }

    private void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFileInfo modFileInfo = ModList.get().getModFileById(PrecisionProspecting.MODID);
            if (modFileInfo == null) {
                PrecisionProspecting.LOGGER.error("Could not find Precision Prospecting mod file info; built-in resource packs will be missing");
                return;
            }
            IModFile modFile = modFileInfo.getFile();
            event.addRepositorySource(consumer -> {
                Pack pack = Pack.readMetaAndCreate(new ResourceLocation(PrecisionProspecting.MODID, "vexxed_items").toString(), Component.literal("Vexxed Visuals: Precision Prospecting"), false, id -> new ModFilePackResources(id, modFile, "resourcepacks/vexxed_compat"), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);
                if (pack != null) {
                    consumer.accept(pack);
                }
            });
        }
    }

    private void addItemsToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == TFCCreativeTabs.METAL.tab().get()) {
            Registration.PROSHAMMERS.values().forEach(event::accept);
            Registration.PROSHAMMER_HEADS.values().forEach(event::accept);
            Registration.PROSDRILLS.values().forEach(event::accept);
            Registration.PROSDRILL_HEADS.values().forEach(event::accept);
            Registration.MINERAL_PROSPECTORS.values().forEach(event::accept);
            Registration.MINERAL_PROSPECTOR_HEADS.values().forEach(event::accept);
        } else if (event.getTab() == TFCCreativeTabs.MISC.tab().get()) {
            event.accept(Registration.FIRED_PROSHAMMER_MOLD);
            event.accept(Registration.UNFIRED_PROSHAMMER_MOLD);
            event.accept(Registration.FIRED_PROSDRILL_MOLD);
            event.accept(Registration.UNFIRED_PROSDRILL_MOLD);
            event.accept(Registration.FIRED_MINERAL_PROSPECTOR_MOLD);
            event.accept(Registration.UNFIRED_MINERAL_PROSPECTOR_MOLD);
        }
    }
}
