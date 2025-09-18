package io.github.notenoughmail.precisionprospecting;

import io.github.notenoughmail.precisionprospecting.items.PrecProsItems;
import io.github.notenoughmail.precisionprospecting.items.ProspectorType;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static io.github.notenoughmail.precisionprospecting.PrecProsDataEntry.*;

public class DMProvider implements Provider {

    @Override
    public void add(Consumer<? super DataProvider> ret, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper efh) {
        ret.accept(new DataManagerProvider<>(ItemSizeManager.MANAGER, output, lookupProvider) {
            @Override
            protected void addData(HolderLookup.Provider provider) {
                add("mineral_prospectors", new ItemSizeDefinition(
                        Ingredient.of(MIN_PROS),
                        Size.LARGE,
                        Weight.HEAVY
                ));
            }
        });
        ret.accept(new DataManagerProvider<>(HeatCapability.MANAGER, output, lookupProvider) {

            @Override
            protected void addData(HolderLookup.Provider provider) {
                METAL_HEATS.get().forEach((metal, fluidHeat) -> {
                    for (ProspectorType type : ProspectorType.VALUES) {
                        add(metal.getSerializedName() + "/" + type, new HeatDefinition(
                                Ingredient.of(PrecProsItems.TOOLS.get(metal).get(type)),
                                (fluidHeat.specificHeatCapacity()) / FLUID_HEAT_CAPACITY * (type.defMelt / 100F),
                                fluidHeat.meltTemperature() * 0.6F,
                                fluidHeat.meltTemperature() * 0.8F
                        ));
                        add(metal.getSerializedName() + "/" + type + "_head", new HeatDefinition(
                                Ingredient.of(PrecProsItems.TOOL_HEADS.get(metal).get(type)),
                                (fluidHeat.specificHeatCapacity() / FLUID_HEAT_CAPACITY) * (type.defMelt / 100F),
                                fluidHeat.meltTemperature() * 0.6F,
                                fluidHeat.meltTemperature() * 0.8F
                        ));
                    }
                });
            }
        });
    }
}
