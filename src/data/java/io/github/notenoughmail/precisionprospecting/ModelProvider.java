package io.github.notenoughmail.precisionprospecting;

import io.github.notenoughmail.precisionprospecting.items.PrecProsItems;
import io.github.notenoughmail.precisionprospecting.items.ProspectorType;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModelProvider implements Provider {

    @Override
    public void add(Consumer<? super DataProvider> ret, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper efh) {
        ret.accept(new ItemModelProvider(output, PrecisionProspecting.ID, efh) {
            @Override
            protected void registerModels() {
                for (ProspectorType type : ProspectorType.VALUES) {
                    for (Metal metal : PrecProsItems.TOOL_METALS) {
                        handheldItem(PrecProsItems.TOOLS.get(metal).get(type).asItem())
                                .texture("layer0", PrecisionProspecting.id("item/metal/" + type + "/" + metal.getSerializedName()));
                        basic(PrecProsItems.TOOL_HEADS.get(metal).get(type))
                                .texture("layer0", PrecisionProspecting.id("item/metal/" + type + "_head/" + metal.getSerializedName()));
                    }
                    basic(PrecProsItems.FIRED_MOLDS.get(type), ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "item/default"))
                            .texture("base", PrecisionProspecting.id("item/ceramic/" + type + "/head_mold_empty"))
                            .texture("fluid", PrecisionProspecting.id("item/ceramic/" + type + "/head_mold_overlay"))
                            .customLoader((t, efh) -> new CustomLoaderBuilder<>(Helpers.identifier("fluid_container"), t, efh, false) {});
                    basic(PrecProsItems.UNFIRED_MOLDS.get(type))
                            .texture("layer0", PrecisionProspecting.id("item/ceramic/" + type + "/unfired_head_mold"));
                }
            }

            ItemModelBuilder basic(ItemLike item) {
                return basic(item, ResourceLocation.withDefaultNamespace("item/generated"));
            }

            ItemModelBuilder basic(ItemLike item, ResourceLocation parent) {
                return getBuilder(BuiltInRegistries.ITEM.getKey(item.asItem()).toString())
                        .parent(new ModelFile.UncheckedModelFile(parent));
            }

            // By default, the item/block folder is simply discarded if the path contains slashes
            @Override
            public ItemModelBuilder getBuilder(String path) {
                final ResourceLocation id = ResourceLocation.tryParse(path).withPrefix(folder + "/");
                existingFileHelper.trackGenerated(id, MODEL);
                return generatedModels.computeIfAbsent(id, factory);
            }
        });
    }
}
