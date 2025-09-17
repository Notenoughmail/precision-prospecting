package io.github.notenoughmail.precisionprospecting;

import com.mojang.logging.LogUtils;
import io.github.notenoughmail.precisionprospecting.items.PrecProsItems;
import io.github.notenoughmail.precisionprospecting.items.ProspectorType;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.component.forge.ForgeRule;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.*;
import net.dries007.tfc.common.recipes.outputs.CopyForgingBonusModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.data.DataManager;
import net.dries007.tfc.util.data.FluidHeat;
import net.dries007.tfc.util.data.KnappingPattern;
import net.dries007.tfc.util.data.KnappingType;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = PrecisionProspecting.ID)
public class PrecProsDataEntry {

    static final Logger LOGGER = LogUtils.getLogger();

    static final float FLUID_HEAT_CAPACITY = 0.003F;

    @SubscribeEvent
    public static void data(GatherDataEvent event) {

        LOGGER.info("Generating Precision Prospecting data...");

        if (event.includeServer()) {
            final PackOutput output = event.getGenerator().getPackOutput();
            final ExistingFileHelper efh = event.getExistingFileHelper();
            final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

            final TagKey<Item> minPros = tag("tools/mineral_prospector");
            final TagKey<Item> prosHammer = tag("tools/prospector_hammer");
            final TagKey<Item> prosDrill = tag("tools/prospector_drill");

            final Map<Metal, FluidHeat> metalHeats = Map.of(
                    Metal.COPPER, fluidHeat(Metal.COPPER, 0.35F, 1080),
                    Metal.BISMUTH_BRONZE, fluidHeat(Metal.BISMUTH_BRONZE, 0.35F, 985),
                    Metal.BLACK_BRONZE, fluidHeat(Metal.BLACK_BRONZE, 0.35F, 1070),
                    Metal.BRONZE, fluidHeat(Metal.BRONZE, 0.35F, 950),
                    Metal.WROUGHT_IRON, fluidHeat(Metal.WROUGHT_IRON, 0.35F, 1535),
                    Metal.STEEL, fluidHeat(Metal.STEEL, 0.35F, 1540),
                    Metal.BLACK_STEEL, fluidHeat(Metal.BLACK_STEEL, 0.35F, 1485),
                    Metal.BLUE_STEEL, fluidHeat(Metal.BLUE_STEEL, 0.35F, 1540),
                    Metal.RED_STEEL, fluidHeat(Metal.RED_STEEL, 0.35F, 1540)
            );

            event.addProvider(new TagsProvider<Block>(output, Registries.BLOCK, lookupProvider, PrecisionProspecting.ID, efh) {
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

            event.addProvider(new TagsProvider<Item>(output, Registries.ITEM, lookupProvider, PrecisionProspecting.ID, efh) {
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
                    tag(minPros)
                            .add(
                                    PrecProsItems.TOOLS.values().stream()
                                            .map(m -> m.get(ProspectorType.MIN_PROS))
                                            .map(DeferredHolder::getKey)
                                            .toArray(ResourceKey[]::new)
                            );
                    tag(prosHammer)
                            .add(
                                    PrecProsItems.TOOLS.values().stream()
                                            .map(m -> m.get(ProspectorType.PROS_HAMMER))
                                            .map(DeferredHolder::getKey)
                                            .toArray(ResourceKey[]::new)
                            );
                    tag(prosDrill)
                            .add(
                                    PrecProsItems.TOOLS.values().stream()
                                            .map(m -> m.get(ProspectorType.PROS_DRILL))
                                            .map(DeferredHolder::getKey)
                                            .toArray(ResourceKey[]::new)
                            );
                    tag(Tags.Items.TOOLS)
                            .addTags(
                                    minPros,
                                    prosHammer,
                                    prosDrill
                            );
                }
            });

            event.addProvider(new DataManagerProvider<ItemSizeDefinition>(ItemSizeManager.MANAGER, output, lookupProvider) {
                @Override
                protected void addData(HolderLookup.Provider provider) {
                    add("mineral_prospectors", new ItemSizeDefinition(
                            Ingredient.of(minPros),
                            Size.LARGE,
                            Weight.HEAVY
                    ));
                }
            });

            event.addProvider(new DataManagerProvider<HeatDefinition>(HeatCapability.MANAGER, output, lookupProvider) {

                @Override
                protected void addData(HolderLookup.Provider provider) {
                    metalHeats.forEach((metal, fluidHeat) -> {
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

            event.addProvider(new VanillaRecipeProvider(output, lookupProvider) {

                @Override
                protected CompletableFuture<?> buildAdvancement(CachedOutput output, HolderLookup.Provider registries, AdvancementHolder advancement, ICondition... conditions) {
                    return CompletableFuture.allOf();
                }

                @Override
                protected void buildRecipes(RecipeOutput out) {
                    accept(out, knap(
                            PrecProsItems.UNFIRED_MOLDS.get(ProspectorType.MIN_PROS),
                            "XXXXX",
                            "    X",
                            "     ",
                            "XX   ",
                            "XXXX "
                    ), "knapping/mineral_prospector_head_mold");
                    accept(out, knap(
                            PrecProsItems.UNFIRED_MOLDS.get(ProspectorType.PROS_DRILL),
                            "XXXX ",
                            "XXX X",
                            "XX XX",
                            "X XXX",
                            " XXXX"
                    ), "knapping/prospector_drill_head_mold");
                    accept(out, knap(
                            PrecProsItems.UNFIRED_MOLDS.get(ProspectorType.PROS_HAMMER),
                            "XXXXX",
                            "X   X",
                            "     ",
                            "X   X",
                            "XXXXX"
                    ), "knapping/prospector_hammer_head_mold");

                    for (ProspectorType type : ProspectorType.VALUES) {
                        accept(out, heat(
                                PrecProsItems.UNFIRED_MOLDS.get(type),
                                PrecProsItems.FIRED_MOLDS.get(type),
                                1399F
                        ), "heating/" + type + "_head_mold");
                    }
                    metalHeats.forEach((metal, fluidHeat) -> {
                        for (ProspectorType type : ProspectorType.VALUES) {
                            accept(out, heat(
                                    PrecProsItems.TOOLS.get(metal).get(type),
                                    new FluidStack(fluidHeat.fluid(), type.defMelt),
                                    fluidHeat.meltTemperature(),
                                    true
                            ), "heating/metal/" + metal.getSerializedName() + "/" + type);
                            accept(out, heat(
                                    PrecProsItems.TOOL_HEADS.get(metal).get(type),
                                    new FluidStack(fluidHeat.fluid(), type.defMelt),
                                    fluidHeat.meltTemperature(),
                                    false
                            ), "heating/metal/" + metal.getSerializedName() + "/" + type + "_head");
                            accept(out, new AdvancedShapedRecipe(
                                    ShapedRecipePattern.of(
                                            Map.of(
                                                    'S', Ingredient.of(PrecProsItems.TOOL_HEADS.get(metal).get(type)),
                                                    'A', Ingredient.of(Tags.Items.RODS_WOODEN)
                                            ),
                                            "S",
                                            "A"
                                    ),
                                    true,
                                    ItemStackProvider.of(PrecProsItems.TOOLS.get(metal).get(type).toStack(), CopyForgingBonusModifier.INSTANCE),
                                    Optional.empty(),
                                    0,
                                    0
                            ), "crafting/metal/" + type + "/" + metal.getSerializedName());
                            final List<ForgeRule> anvilRules = new ArrayList<>();
                            final Ingredient in = switch (type) {
                                case MIN_PROS -> {
                                    anvilRules.add(ForgeRule.HIT_LAST);
                                    anvilRules.add(ForgeRule.DRAW_NOT_LAST);
                                    yield Ingredient.of(commonTag("double_ingots/" + metal.getSerializedName()));
                                }
                                case PROS_DRILL -> {
                                    anvilRules.add(ForgeRule.PUNCH_LAST);
                                    anvilRules.add(ForgeRule.HIT_ANY);
                                    anvilRules.add(ForgeRule.HIT_ANY);
                                    yield Ingredient.of(TFCItems.METAL_ITEMS.get(metal).get(Metal.ItemType.TUYERE));
                                }
                                case PROS_HAMMER -> {
                                    anvilRules.add(ForgeRule.PUNCH_LAST);
                                    anvilRules.add(ForgeRule.SHRINK_NOT_LAST);
                                    yield Ingredient.of(commonTag("double_ingots/" + metal.getSerializedName()));
                                }
                            };
                            accept(out, new AnvilRecipe(
                                    in,
                                    metal.tier(),
                                    anvilRules,
                                    true,
                                    ItemStackProvider.of(PrecProsItems.TOOL_HEADS.get(metal).get(type))
                            ), "anvil/metal/" + type + "/" + metal.getSerializedName());
                        }
                    });
                    for (Metal metal : List.of(Metal.COPPER, Metal.BLACK_BRONZE, Metal.BRONZE, Metal.BISMUTH_BRONZE)) {
                        final FluidHeat fluidHeat = metalHeats.get(metal);
                        for (ProspectorType type : ProspectorType.VALUES) {
                            accept(out, new CastingRecipe(
                                    Ingredient.of(PrecProsItems.FIRED_MOLDS.get(type)),
                                    SizedFluidIngredient.of(fluidHeat.fluid(), type.defMelt),
                                    ItemStackProvider.of(PrecProsItems.TOOL_HEADS.get(metal).get(type)),
                                    1
                            ), "casting/" + metal.getSerializedName() + "_" + type + "_head");
                        }
                    }
                }

                KnappingRecipe knap(ItemLike result, String... pattern) {
                    return new KnappingRecipe(
                            KnappingType.MANAGER.getReference(Helpers.identifier("clay")),
                            KnappingPattern.from(false, pattern),
                            Optional.empty(),
                            result.asItem().getDefaultInstance()
                    );
                }

                HeatingRecipe heat(ItemLike in, ItemLike out, float temperature) {
                    return new HeatingRecipe(
                            Ingredient.of(in),
                            ItemStackProvider.of(out),
                            FluidStack.EMPTY,
                            temperature,
                            false
                    );
                }

                HeatingRecipe heat(ItemLike in, FluidStack out, float temperature, boolean useDurability) {
                    return new HeatingRecipe(
                            Ingredient.of(in),
                            ItemStackProvider.empty(),
                            out,
                            temperature,
                            useDurability && in.asItem().getDefaultInstance().getMaxDamage() > 0
                    );
                }

                void accept(RecipeOutput out, Recipe<?> recipe, String path) {
                    out.accept(PrecisionProspecting.id(path), recipe, null);
                }
            });
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
