package io.github.notenoughmail.precisionprospecting;

import io.github.notenoughmail.precisionprospecting.items.PrecProsItems;
import io.github.notenoughmail.precisionprospecting.items.ProspectorType;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static io.github.notenoughmail.precisionprospecting.PrecProsDataEntry.*;

public class TagProvider implements Provider {

    public void add(Consumer<? super DataProvider> ret, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper efh) {
        ret.accept(new TagsProvider<>(output, Registries.BLOCK, lookupProvider, PrecisionProspecting.ID, efh) {
            @SuppressWarnings("unchecked")
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                tag(PrecisionProspecting.PROSPECTABLE_MINERAL)
                        .add(
                                TFCBlocks.ORES.values().stream()
                                        .flatMap(m -> m.values().stream())
                                        .map(id -> id.holder().getKey())
                                        .toArray(ResourceKey[]::new)
                        )
                        .add(
                                TFCBlocks.LIGNITE.key(),
                                TFCBlocks.BITUMINOUS_COAL.key(),
                                TFCBlocks.HALITE.key()
                        );
            }
        });
        ret.accept(new TagsProvider<>(output, Registries.ITEM, lookupProvider, PrecisionProspecting.ID, efh) {
            @SuppressWarnings("unchecked")
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                tag(TFCTags.Items.FIRED_MOLDS)
                        .add(
                                PrecProsItems.FIRED_MOLDS.values().stream()
                                        .map(DeferredHolder::getKey)
                                        .toArray(ResourceKey[]::new)
                        );
                tag(TFCTags.Items.UNFIRED_MOLDS)
                        .add(
                                PrecProsItems.UNFIRED_MOLDS.values().stream()
                                        .map(DeferredHolder::getKey)
                                        .toArray(ResourceKey[]::new)
                        );
                for (Metal metal : PrecProsItems.TOOL_METALS) {
                    tag(TagKey.create(Registries.ITEM, Helpers.identifier("tool/" + metal.getSerializedName())))
                            .add(
                                    PrecProsItems.TOOLS.get(metal).values().stream()
                                            .map(DeferredHolder::getKey)
                                            .toArray(ResourceKey[]::new)
                            );
                }
                tag(MIN_PROS)
                        .add(
                                PrecProsItems.TOOLS.values().stream()
                                        .map(m -> m.get(ProspectorType.MIN_PROS))
                                        .map(DeferredHolder::getKey)
                                        .toArray(ResourceKey[]::new)
                        );
                tag(PROS_HAMMER)
                        .add(
                                PrecProsItems.TOOLS.values().stream()
                                        .map(m -> m.get(ProspectorType.PROS_HAMMER))
                                        .map(DeferredHolder::getKey)
                                        .toArray(ResourceKey[]::new)
                        );
                tag(PROS_DRILL)
                        .add(
                                PrecProsItems.TOOLS.values().stream()
                                        .map(m -> m.get(ProspectorType.PROS_DRILL))
                                        .map(DeferredHolder::getKey)
                                        .toArray(ResourceKey[]::new)
                        );
                tag(Tags.Items.TOOLS)
                        .addTags(
                                MIN_PROS,
                                PROS_HAMMER,
                                PROS_DRILL
                        );
            }
        });
    }
}
