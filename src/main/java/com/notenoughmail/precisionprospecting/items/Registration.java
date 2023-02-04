/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.items;

import com.ljuangbminecraft.tfcchannelcasting.TFCChannelCasting;
import com.notenoughmail.precisionprospecting.integration.castwchannels.RegisterMoldItem;
import net.dries007.tfc.common.TFCItemGroup;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.MoldItem;
import net.dries007.tfc.common.items.ToolItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static com.notenoughmail.precisionprospecting.PrecisionProspecting.MODID;
import static com.notenoughmail.precisionprospecting.config.PrecProsConfig.*;

public class Registration {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        // This doesn't break anything even on a fresh instance without TFCCC, surprisingly, at least on Forge 40.2.0
        if (ModList.get().isLoaded(TFCChannelCasting.MOD_ID)) {
            RegisterMoldItem.register();
        }

    }

    public static final Map<Metal.Default, RegistryObject<Item>> PROSHAMMERS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_hammer/" + metal.name(), () ->
                    new ProsHammerItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(0.6f, metal.toolTier()), -3f, metal_properties())
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> PROSHAMMER_HEADS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_hammer_head/" + metal.name(), () ->
                    new Item(metal_properties())
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> PROSDRILLS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_drill/" + metal.name(), () ->
                    new ProsDrillItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(0.6f, metal.toolTier()), -3f, metal_properties())
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> PROSDRILL_HEADS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_drill_head/" + metal.name(), () ->
                    new Item(metal_properties())
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> MINERAL_PROSPECTORS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/mineral_prospector/" + metal.name(), () ->
                    new MineralProspectorItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(0.3f, metal.toolTier()), -2.8f, metal_properties())
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> MINERAL_PROSPECTOR_HEADS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/mineral_prospector_head/" + metal.name(), () ->
                    new Item(metal_properties())
            )
    );
    public static final RegistryObject<Item>
            UNFIRED_PROSHAMMER_MOLD = ITEMS.register("ceramic/unfired_prospector_hammer_head_mold", () -> new Item(ceramic_properties())),
            FIRED_PROSHAMMER_MOLD = ITEMS.register("ceramic/prospector_hammer_head_mold", () -> new MoldItem(() -> moldProsHammerCapacity.get(), TFCTags.Fluids.USABLE_IN_TOOL_HEAD_MOLD, ceramic_properties())),
            PROSHAMMER_TABLE_MOLD = ITEMS.register("mold/proshammer_head", () -> new Item(new Item.Properties())),
            UNFIRED_PROSDRILL_MOLD = ITEMS.register("ceramic/unfired_prospector_drill_head_mold", () -> new Item(ceramic_properties())),
            FIRED_PROSDRILL_MOLD = ITEMS.register("ceramic/prospector_drill_head_mold", () -> new MoldItem(() -> moldProsDrillCapacity.get(), TFCTags.Fluids.USABLE_IN_TOOL_HEAD_MOLD, ceramic_properties())),
            PROSDRILL_TABLE_MOLD = ITEMS.register("mold/prosdrill_head", () -> new Item(new Item.Properties())),
            UNFIRED_MINERAL_PROSPECTOR_MOLD = ITEMS.register("ceramic/unfired_mineral_prospector_head_mold", () -> new Item(ceramic_properties())),
            FIRED_MINERAL_PROSPECTOR_MOLD = ITEMS.register("ceramic/mineral_prospector_head_mold", () -> new MoldItem(() -> moldMineralProspectorCapacity.get(), TFCTags.Fluids.USABLE_IN_TOOL_HEAD_MOLD, ceramic_properties())),
            MINPROS_TABLE_MOLD = ITEMS.register("mold/minpros_head", () -> new Item(new Item.Properties()))
                    ;
    // Big thanks to Alc on Discord for telling me you can just pass in a lambda like this, which makes sense now that I think about it

    public static Item.Properties metal_properties() {
        return new Item.Properties().tab(TFCItemGroup.METAL);
    }

    public static Item.Properties ceramic_properties() {
        return new Item.Properties().tab(TFCItemGroup.MISC);
    }

    private static <T extends Item> RegistryObject<Item> register(String name, Supplier<T> item) {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
