/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.items;

import net.dries007.tfc.common.TFCItemGroup;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.MoldItem;
import net.dries007.tfc.common.items.ToolItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.codec.language.bm.Lang;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static com.notenoughmail.precisionprospecting.PrecisionProspecting.MODID;
import static com.notenoughmail.precisionprospecting.config.PrecProsConfig.moldProsDrillCapacity;
import static com.notenoughmail.precisionprospecting.config.PrecProsConfig.moldProsHammerCapacity;

public class Registration {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
    }

    public static final Map<Metal.Default, RegistryObject<Item>> PROSHAMMERS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_hammer/" + metal.name(), () ->
                    new ProsHammerItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(0.6f, metal.toolTier()), -3f, metal_properties(), "minecraft")
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> PROSHAMMER_HEADS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_hammer_head/" + metal.name(), () ->
                    new HideableItem(metal_properties(), "minecraft")
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> PROSDRILLS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_drill/" + metal.name(), () ->
                    new ProsDrillItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(0.6f, metal.toolTier()), -3f, metal_properties(), "minecraft")
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> PROSDRILL_HEADS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_drill_head/" + metal.name(), () ->
                    new HideableItem(metal_properties(), "minecraft")
            )
    );
    public static final RegistryObject<Item>
            UNFIRED_PROSHAMMER_MOLD = ITEMS.register("ceramic/unfired_prospector_hammer_head_mold", () -> new Item(ceramic_properties())),
            FIRED_PROSHAMMER_MOLD = ITEMS.register("ceramic/prospector_hammer_head_mold", () -> new MoldItem(() -> moldProsHammerCapacity.get(), TFCTags.Fluids.USABLE_IN_TOOL_HEAD_MOLD, ceramic_properties())),
            UNFIRED_PROSDRILL_MOLD = ITEMS.register("ceramic/unfired_prospector_drill_head_mold", () -> new Item(ceramic_properties())),
            FIRED_PROSDRILL_MOLD = ITEMS.register("ceramic/prospector_drill_head_mold", () -> new MoldItem(() -> moldProsDrillCapacity.get(), TFCTags.Fluids.USABLE_IN_TOOL_HEAD_MOLD, ceramic_properties()))
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
