package io.github.notenoughmail.precisionprospecting;

import io.github.notenoughmail.precisionprospecting.items.PrecProsItems;
import io.github.notenoughmail.precisionprospecting.items.ProspectorType;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static io.github.notenoughmail.precisionprospecting.PrecProsDataEntry.*;

public class AdvProvider implements Provider, AdvancementProvider.AdvancementGenerator {

    @Override
    public void add(Consumer<? super DataProvider> ret, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper efh) {
        ret.accept(new AdvancementProvider(output, lookupProvider, efh, List.of(this)));
    }

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> ret, ExistingFileHelper efh) {
        for (ProspectorType type : ProspectorType.VALUES) {
            Advancement.Builder.recipeAdvancement() // #recipeAdvancement does not set telemetry to true
                    .addCriterion(type.toString(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(switch (type) {
                        case PROS_HAMMER -> PROS_HAMMER;
                        case PROS_DRILL -> PROS_DRILL;
                        case MIN_PROS -> MIN_PROS;
                    }).build()))
                    .display(
                            PrecProsItems.TOOLS.get(Metal.COPPER).get(type),
                            Component.translatable("precisionprospecting.advancements.story." + type + ".title"),
                            Component.translatable("precisionprospecting.advancements.story." + type + ".description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            false,
                            false
                    )
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .parent(Helpers.identifier("story/propick")) // This is deprecated, AdvancementHolder version would be a pain to use
                    .save(ret, PrecisionProspecting.id("story/" + type).toString());
        }
    }
}
