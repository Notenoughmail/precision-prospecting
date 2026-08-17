package io.github.notenoughmail.precisionprospecting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import net.neoforged.neoforge.client.model.generators.*;
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
        ret.accept(new BlockModelProvider(output, PrecisionProspecting.ID, efh) {
            @Override
            protected void registerModels() {
                mold(PrecProsItems.FIRED_MOLDS.get(ProspectorType.PROS_HAMMER))
                        .fullRow()
                        .fullRow()
                        .row("XXXXXXX     XX")
                        .row("XXXXXX      XX")
                        .row("XXXXX       XX")
                        .row("XXXX        XX")
                        .row("XXX         XX")
                        .row("XX         XXX")
                        .row("XX        XXXX")
                        .row("XX       XXXXX")
                        .row("XX      XXXXXX")
                        .row("XX     XXXXXXX")
                        .fullRow()
                        .fullRow();
                mold(PrecProsItems.FIRED_MOLDS.get(ProspectorType.PROS_DRILL))
                        .fullRow()
                        .fullRow()
                        .fullRow()
                        .row("XX   XXXXXXXXX")
                        .row("X      XXXXXXX")
                        .row("XX       XXXXX")
                        .row("XXXX       XXX")
                        .row("XXXXXX       X")
                        .row("XXXXXXXX     X")
                        .row("XXXXXXXXX    X")
                        .fullRow()
                        .fullRow()
                        .fullRow()
                        .fullRow();
                mold(PrecProsItems.FIRED_MOLDS.get(ProspectorType.MIN_PROS))
                        .fullRow()
                        .fullRow()
                        .fullRow()
                        .row("XXXXXXXX  XXXX")
                        .row("XXXXXX      XX")
                        .row("XXXX         X")
                        .row("XXX        XXX")
                        .row("XX       XXXXX")
                        .row("X       XXXXXX")
                        .row("XX     XXXXXXX")
                        .row("XXX   XXXXXXXX")
                        .row("XXXX XXXXXXXXX")
                        .fullRow()
                        .fullRow();
            }

            MoldLoaderBuilder mold(ItemLike item) {
                return getBuilder(BuiltInRegistries.ITEM.getKey(item.asItem()).withPrefix("mold/").toString())
                        .texture("0", "tfc:block/mold")
                        .texture("particle", "tfc:block/mold")
                        .customLoader(MoldLoaderBuilder::new);
            }

            @Override
            public BlockModelBuilder getBuilder(String path) {
                final ResourceLocation id = ResourceLocation.tryParse(path).withPrefix(folder + "/");
                existingFileHelper.trackGenerated(id, MODEL);
                return generatedModels.computeIfAbsent(id, factory);
            }
        });
    }

    private static class MoldLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {

        private final String[] pattern = new String[14];
        private int row = 0;

        protected MoldLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(Helpers.identifier("mold"), parent, existingFileHelper, true);
        }

        public MoldLoaderBuilder row(String str) {
            if (str.length() != 14) throw new IllegalArgumentException("Pattern must be 14 wide");
            pattern[row++] = str;
            return this;
        }

        public MoldLoaderBuilder fullRow() {
            return row("XXXXXXXXXXXXXX");
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            if (row != 14) throw new IllegalArgumentException("Pattern must be 14 high");
            super.toJson(json);
            final JsonArray arr = new JsonArray(14);
            for (String row : pattern) arr.add(row);
            json.add("pattern", arr);
            return json;
        }
    }
}
