package io.github.notenoughmail.precisionprospecting;

import io.github.notenoughmail.precisionprospecting.items.PrecProsItems;
import io.github.notenoughmail.precisionprospecting.items.ProspectorType;
import net.dries007.tfc.common.TFCCreativeTabs;
import net.dries007.tfc.common.capabilities.ItemCapabilities;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

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
        if (event.getTab() == TFCCreativeTabs.METAL.tab().get()) {
            for (Metal metal : PrecProsItems.TOOL_METALS) {
                for (ProspectorType type : ProspectorType.VALUES) {
                    event.accept(PrecProsItems.TOOLS.get(metal).get(type));
                    event.accept(PrecProsItems.TOOL_HEADS.get(metal).get(type));
                }
            }
        } else if (event.getTab() == TFCCreativeTabs.MISC.tab().get()) {
            for (ProspectorType type : ProspectorType.VALUES) {
                event.accept(PrecProsItems.FIRED_MOLDS.get(type));
                event.accept(PrecProsItems.UNFIRED_MOLDS.get(type));
            }
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        final ItemLike[] molds = PrecProsItems.FIRED_MOLDS.values().toArray(ItemLike[]::new);

        event.registerItem(ItemCapabilities.MOLD, ItemCapabilities::forMold, molds);
        event.registerItem(ItemCapabilities.HEAT, ItemCapabilities::forMold, molds);
        event.registerItem(ItemCapabilities.FLUID, ItemCapabilities::forMold, molds);
    }
}
