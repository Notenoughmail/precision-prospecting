package io.github.notenoughmail.precisionprospecting;

import io.github.notenoughmail.precisionprospecting.items.PrecProsItems;
import io.github.notenoughmail.precisionprospecting.items.ProspectorType;
import net.dries007.tfc.common.component.forge.ForgeRule;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.*;
import net.dries007.tfc.common.recipes.outputs.CopyForgingBonusModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.data.FluidHeat;
import net.dries007.tfc.util.data.KnappingPattern;
import net.dries007.tfc.util.data.KnappingType;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static io.github.notenoughmail.precisionprospecting.PrecProsDataEntry.METAL_HEATS;
import static io.github.notenoughmail.precisionprospecting.PrecProsDataEntry.commonTag;

public class RecipeProvider implements Provider {

    @Override
    public void add(Consumer<? super DataProvider> ret, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper efh) {
        ret.accept(new VanillaRecipeProvider(output, lookupProvider) {

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
                METAL_HEATS.get().forEach((metal, fluidHeat) -> {
                    for (ProspectorType type : ProspectorType.VALUES) {
                        accept(out, heat(
                                PrecProsItems.TOOLS.get(metal).get(type),
                                new FluidStack(meltFluid(metal), type.defMelt),
                                fluidHeat.meltTemperature(),
                                true
                        ), "heating/metal/" + metal.getSerializedName() + "/" + type);

                        accept(out, heat(
                                PrecProsItems.TOOL_HEADS.get(metal).get(type),
                                new FluidStack(meltFluid(metal), type.defMelt),
                                fluidHeat.meltTemperature(),
                                false
                        ), "heating/metal/" + metal.getSerializedName() + "/" + type + "_head");

                        final String[] pattern = type == ProspectorType.PROS_DRILL ?
                                new String[]{ "S ", "A ", " A" } :
                                new String[]{ "S", "A" };

                        accept(out, new AdvancedShapedRecipe(
                                ShapedRecipePattern.of(
                                        Map.of(
                                                'S', Ingredient.of(PrecProsItems.TOOL_HEADS.get(metal).get(type)),
                                                'A', Ingredient.of(Tags.Items.RODS_WOODEN)
                                        ),
                                        pattern
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
                    final FluidHeat fluidHeat = METAL_HEATS.get().get(metal);
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
        });
    }

    static Fluid meltFluid(Metal metal) {
        return metal == Metal.WROUGHT_IRON ? TFCFluids.METALS.get(Metal.CAST_IRON).getSource() : METAL_HEATS.get().get(metal).fluid();
    }

    static void accept(RecipeOutput out, Recipe<?> recipe, String path) {
        final ResourceLocation id = PrecisionProspecting.id(path);
        out.accept(
                id,
                recipe,
                out.advancement()
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                        .rewards(AdvancementRewards.Builder.recipe(id))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(id.withPrefix("recipes/"))
        );
    }

    static KnappingRecipe knap(ItemLike result, String... pattern) {
        return new KnappingRecipe(
                KnappingType.MANAGER.getReference(Helpers.identifier("clay")),
                KnappingPattern.from(false, pattern),
                Optional.empty(),
                result.asItem().getDefaultInstance()
        );
    }

    static HeatingRecipe heat(ItemLike in, ItemLike out, float temperature) {
        return new HeatingRecipe(
                Ingredient.of(in),
                ItemStackProvider.of(out),
                FluidStack.EMPTY,
                temperature,
                false
        );
    }

    static HeatingRecipe heat(ItemLike in, FluidStack out, float temperature, boolean useDurability) {
        return new HeatingRecipe(
                Ingredient.of(in),
                ItemStackProvider.empty(),
                out,
                temperature,
                useDurability && in.asItem().getDefaultInstance().getMaxDamage() > 0
        );
    }
}
