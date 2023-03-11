package com.notenoughmail.precisionprospecting.integration.vexxedvisuals;

import com.notenoughmail.precisionprospecting.PrecisionProspecting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;

@EventBusSubscriber
public class CommonEvents {

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                IModFileInfo modFileInfo = ModList.get().getModFileById(PrecisionProspecting.MODID);
                if (modFileInfo == null) {
                    PrecisionProspecting.LOGGER.error("Could not find Precision Prospecting mod file info; built-in resource packs will be missing");
                    return;
                }
                IModFile modFile = modFileInfo.getFile();
                event.addRepositorySource((consumer, constructor) -> {
                    consumer.accept(Pack.create(new ResourceLocation(PrecisionProspecting.MODID, "vexxed_items").toString(), false, () -> new ModFilePackResources("Vexxed Visuals: Precision Prospecting", modFile, "resourcepacks/vexxed_compat"), constructor, Pack.Position.TOP, PackSource.DEFAULT));
                });
            }
        }
    }
}
