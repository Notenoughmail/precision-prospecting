package io.github.notenoughmail.precisionprospecting;

import com.mojang.logging.LogUtils;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.data.FluidHeat;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

@EventBusSubscriber(modid = PrecisionProspecting.ID)
public class PrecProsDataEntry {

    static final Logger LOGGER = LogUtils.getLogger();

    static final float FLUID_HEAT_CAPACITY = 0.003F;

    static final TagKey<Item> MIN_PROS = tag("tools/mineral_prospector");
    static final TagKey<Item> PROS_HAMMER = tag("tools/prospector_hammer");
    static final TagKey<Item> PROS_DRILL = tag("tools/prospector_drill");

    static final Supplier<Map<Metal, FluidHeat>> METAL_HEATS = Lazy.of(() -> Map.of(
            Metal.COPPER, fluidHeat(Metal.COPPER, 0.35F, 1080),
            Metal.BISMUTH_BRONZE, fluidHeat(Metal.BISMUTH_BRONZE, 0.35F, 985),
            Metal.BLACK_BRONZE, fluidHeat(Metal.BLACK_BRONZE, 0.35F, 1070),
            Metal.BRONZE, fluidHeat(Metal.BRONZE, 0.35F, 950),
            Metal.WROUGHT_IRON, fluidHeat(Metal.WROUGHT_IRON, 0.35F, 1535),
            Metal.STEEL, fluidHeat(Metal.STEEL, 0.35F, 1540),
            Metal.BLACK_STEEL, fluidHeat(Metal.BLACK_STEEL, 0.35F, 1485),
            Metal.BLUE_STEEL, fluidHeat(Metal.BLUE_STEEL, 0.35F, 1540),
            Metal.RED_STEEL, fluidHeat(Metal.RED_STEEL, 0.35F, 1540)
    ));

    @SubscribeEvent
    public static void data(GatherDataEvent event) {

        LOGGER.info("Generating Precision Prospecting data...");

        final PackOutput output = event.getGenerator().getPackOutput();
        final ExistingFileHelper efh = event.getExistingFileHelper();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        if (event.includeServer()) {
            Stream.of(
                    new TagProvider(),
                    new DMProvider(),
                    new RecipeProvider(),
                    new AdvProvider()
            ).forEach(p -> p.add(event::addProvider, output, lookupProvider, efh));
        }

        if (event.includeClient()) {
            Stream.of(
                    new ModelProvider(),
                    new PatchouliProvider()
            ).forEach(p -> p.add(event::addProvider, output, lookupProvider, efh));
        }
    }

    static TagKey<Item> tag(String path) {
        return TagKey.create(Registries.ITEM, PrecisionProspecting.id(path));
    }

    static TagKey<Item> commonTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    static FluidHeat fluidHeat(Metal metal, float baseHeatCapacity, float meltTemperature) {
        return new FluidHeat(TFCFluids.METALS.get(metal).getSource(), meltTemperature, FLUID_HEAT_CAPACITY / baseHeatCapacity);
    }
}
