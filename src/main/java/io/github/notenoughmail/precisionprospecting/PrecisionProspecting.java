package io.github.notenoughmail.precisionprospecting;

import io.github.notenoughmail.precisionprospecting.items.PrecProsItems;
import io.github.notenoughmail.precisionprospecting.items.ProspectorType;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.dries007.tfc.common.TFCCreativeTabs;
import net.dries007.tfc.common.capabilities.ItemCapabilities;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

// TODO: 2.0.1 | When TFC releases 4.0.4-beta, add mold table support
@Mod(PrecisionProspecting.ID)
public class PrecisionProspecting
{
    public static final String ID = "precisionprospecting";

    public static PrecProsConfig CONFIG;

    public static final TagKey<Block> PROSPECTABLE_MINERAL = TagKey.create(Registries.BLOCK, id("prospectable_mineral"));

    public PrecisionProspecting(IEventBus modBus, ModContainer container) {
        CONFIG = new PrecProsConfig(container);
        PrecProsItems.ITEMS.register(modBus);
        modBus.addListener(this::addItemToCreativeTabs);
        modBus.addListener(this::registerCapabilities);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    private void addItemToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == TFCCreativeTabs.TFC_METALS_INGREDIENTS.tab().get()) {
            forTargets(
                    event.getParentEntries(),
                    PrecProsItems.TOOL_METALS,
                    m -> TFCItems.METAL_ITEMS.get(m).get(Metal.ItemType.PROPICK_HEAD),
                    (m, s) -> insertInOrder(
                            s,
                            PrecProsItems.TOOL_HEADS.get(m)::get,
                            event
                    )
            );
            insertInOrder(
                    find(
                            event.getParentEntries(),
                            TFCItems.UNFIRED_MOLDS.get(Metal.ItemType.PROPICK_HEAD).asItem()
                    ).orElseThrow(),
                    PrecProsItems.UNFIRED_MOLDS::get,
                    event
            );
            insertInOrder(
                    find(
                            event.getParentEntries(),
                            TFCItems.MOLDS.get(Metal.ItemType.PROPICK_HEAD).asItem()
                    ).orElseThrow(),
                    PrecProsItems.FIRED_MOLDS::get,
                    event
            );
        } else if (event.getTab() == TFCCreativeTabs.TFC_TOOLS_UTILITIES.tab().get()) {
            forTargets(
                    event.getParentEntries(),
                    PrecProsItems.TOOL_METALS,
                    m -> TFCItems.METAL_ITEMS.get(m).get(Metal.ItemType.PROPICK),
                    (m, s) -> insertInOrder(
                            s,
                            PrecProsItems.TOOLS.get(m)::get,
                            event
                    )
            );
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        final ItemLike[] molds = PrecProsItems.FIRED_MOLDS.values().toArray(ItemLike[]::new);

        event.registerItem(ItemCapabilities.MOLD, ItemCapabilities::forMold, molds);
        event.registerItem(ItemCapabilities.HEAT, ItemCapabilities::forMold, molds);
        event.registerItem(ItemCapabilities.FLUID, ItemCapabilities::forMold, molds);
    }
    
    private static <K> void forTargets(
            ObjectSortedSet<ItemStack> entries,
            Iterable<K> keys,
            Function<K, ItemLike> target,
            BiConsumer<K, ItemStack> action
    ) {
        for (K k : keys) {
            find(entries, target.apply(k).asItem())
                    .ifPresent(s -> action.accept(k, s));
        }
    }

    private static Optional<ItemStack> find(ObjectSortedSet<ItemStack> entries, Item target) {
        return entries.stream()
                .filter(s -> s.is(target))
                .findFirst();
    }

    private static void insertInOrder(ItemStack after, Function<ProspectorType, DeferredItem<?>> item, BuildCreativeModeTabContentsEvent event) {
        for (ProspectorType t : ProspectorType.VALUES) {
            final ItemStack stack = item.apply(t).toStack();
            event.insertAfter(after, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            after = stack;
        }
    }
}
