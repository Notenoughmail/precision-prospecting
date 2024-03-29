/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.items;

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
    }

    public static final Map<Metal.Default, RegistryObject<Item>> PROSHAMMERS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_hammer/" + metal.name(), () ->
                    new ProsHammerItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(0.6f, metal.toolTier()), -3f, props())
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> PROSHAMMER_HEADS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_hammer_head/" + metal.name(), basicItem()
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> PROSDRILLS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_drill/" + metal.name(), () ->
                    new ProsDrillItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(0.6f, metal.toolTier()), -3f, props())
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> PROSDRILL_HEADS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/prospector_drill_head/" + metal.name(), basicItem()
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> MINERAL_PROSPECTORS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/mineral_prospector/" + metal.name(), () ->
                    new MineralProspectorItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(0.3f, metal.toolTier()), -2.8f, props())
            )
    );
    public static final Map<Metal.Default, RegistryObject<Item>> MINERAL_PROSPECTOR_HEADS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("metal/mineral_prospector_head/" + metal.name(), basicItem()
            )
    );
    public static final RegistryObject<Item> UNFIRED_PROSHAMMER_MOLD = register("ceramic/unfired_prospector_hammer_head_mold", basicItem());
    public static final RegistryObject<Item> FIRED_PROSHAMMER_MOLD = register("ceramic/prospector_hammer_head_mold", () -> new MoldItem(moldProsHammerCapacity, TFCTags.Fluids.USABLE_IN_TOOL_HEAD_MOLD, props()));
    public static final RegistryObject<Item> UNFIRED_PROSDRILL_MOLD = register("ceramic/unfired_prospector_drill_head_mold", basicItem());
    public static final RegistryObject<Item> FIRED_PROSDRILL_MOLD = register("ceramic/prospector_drill_head_mold", () -> new MoldItem(moldProsDrillCapacity, TFCTags.Fluids.USABLE_IN_TOOL_HEAD_MOLD, props()));
    public static final RegistryObject<Item> UNFIRED_MINERAL_PROSPECTOR_MOLD = register("ceramic/unfired_mineral_prospector_head_mold", basicItem());
    public static final RegistryObject<Item> FIRED_MINERAL_PROSPECTOR_MOLD = register("ceramic/mineral_prospector_head_mold", () -> new MoldItem(moldMineralProspectorCapacity, TFCTags.Fluids.USABLE_IN_TOOL_HEAD_MOLD, props()));

    private static Item.Properties props() {
        return new Item.Properties();
    }

    private static <T extends Item> RegistryObject<Item> register(String name, Supplier<T> item) {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }

    private static Supplier<Item> basicItem() {
        return () -> new Item(props());
    }
}
