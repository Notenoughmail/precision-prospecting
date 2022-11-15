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
import net.dries007.tfc.common.TFCTiers;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.notenoughmail.precisionprospecting.PrecisionProspecting.MODID;
import static com.notenoughmail.precisionprospecting.config.PrecProsConfig.moldProsHammerCapacity;

public class Registration {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
    }

    //This hurts, but I don't feel like finagling with Maps and whatnot right now
    public static final RegistryObject<Item>
            RED_STEEL_PROSHAMMER = ITEMS.register("metal/prospector_hammer/red_steel", () -> new ProsHammerItem(TFCTiers.RED_STEEL, (int) ToolItem.calculateVanillaAttackDamage(0.6f, TFCTiers.RED_STEEL), -3f, metal_properties())),
            RED_STEEL_PROSHAMMER_HEAD = ITEMS.register("metal/prospector_hammer_head/red_steel", () -> new Item(metal_properties())),
            BLUE_STEEL_PROSHAMMER = ITEMS.register("metal/prospector_hammer/blue_steel", () -> new ProsHammerItem(TFCTiers.BLUE_STEEL, (int) ToolItem.calculateVanillaAttackDamage(0.6f, TFCTiers.BLUE_STEEL), -3f, metal_properties())),
            BLUE_STEEL_PROSHAMMER_HEAD = ITEMS.register("metal/prospector_hammer_head/blue_steel", () -> new Item(metal_properties())),
            BLACK_STEEL_PROSHAMMER = ITEMS.register("metal/prospector_hammer/black_steel", () -> new ProsHammerItem(TFCTiers.BLACK_STEEL, (int) ToolItem.calculateVanillaAttackDamage(0.6f, TFCTiers.BLACK_STEEL), -3f, metal_properties())),
            BLACK_STEEL_PROSHAMMER_HEAD = ITEMS.register("metal/prospector_hammer_head/black_steel", () -> new Item(metal_properties())),
            STEEL_PROSHAMMER = ITEMS.register("metal/prospector_hammer/steel", () -> new ProsHammerItem(TFCTiers.STEEL, (int) ToolItem.calculateVanillaAttackDamage(0.6f, TFCTiers.STEEL), -3f, metal_properties())),
            STEEL_PROSHAMMER_HEAD = ITEMS.register("metal/prospector_hammer_head/steel", () -> new Item(metal_properties())),
            WROUGHT_IRON_PROSHAMMER = ITEMS.register("metal/prospector_hammer/wrought_iron", () -> new ProsHammerItem(TFCTiers.WROUGHT_IRON, (int) ToolItem.calculateVanillaAttackDamage(0.6f, TFCTiers.WROUGHT_IRON), -3f, metal_properties())),
            WROUGHT_IRON_PROSHAMMER_HEAD = ITEMS.register("metal/prospector_hammer_head/wrought_iron", () -> new Item(metal_properties())),
            BLACK_BRONZE_PROSHAMMER = ITEMS.register("metal/prospector_hammer/black_bronze", () -> new ProsHammerItem(TFCTiers.BLACK_BRONZE, (int) ToolItem.calculateVanillaAttackDamage(0.6f, TFCTiers.BLACK_BRONZE), -3f, metal_properties())),
            BLACK_BRONzE_PROSHAMMER_HEAD = ITEMS.register("metal/prospector_hammer_head/black_bronze", () -> new Item(metal_properties())),
            BIZ_BRONZE_PROSHAMMER =ITEMS.register("metal/prospector_hammer/bismuth_bronze", () -> new ProsHammerItem(TFCTiers.BISMUTH_BRONZE, (int) ToolItem.calculateVanillaAttackDamage(0.6f, TFCTiers.BISMUTH_BRONZE), -3f, metal_properties())),
            BIZ_BRONZE_PROSHAMMER_HEAD = ITEMS.register("metal/prospector_hammer_head/bismuth_bronze", () -> new Item(metal_properties())),
            BRONZE_PROSHAMMER = ITEMS.register("metal/prospector_hammer/bronze", () -> new ProsHammerItem(TFCTiers.BRONZE, (int) ToolItem.calculateVanillaAttackDamage(0.6f, TFCTiers.BRONZE), -3f, metal_properties())),
            BRONZE_PROSHAMMER_HEAD =ITEMS.register("metal/prospector_hammer_head/bronze", () -> new Item(metal_properties())),
            COPPER_PROSHAMMER = ITEMS.register("metal/prospector_hammer/copper", () -> new ProsHammerItem(TFCTiers.COPPER, (int) ToolItem.calculateVanillaAttackDamage(0.6f, TFCTiers.COPPER), -3f, metal_properties())),
            COPPER_PROSHAMMER_HEAD = ITEMS.register("metal/prospector_hammer_head/copper", () -> new Item(metal_properties())),
            UNFIRED_PROSHAMMER_MOLD = ITEMS.register("ceramic/unfired_prospector_hammer_head_mold", () -> new Item(ceramic_properties())),
            FIRED_PROSHAMMER_MOLD = ITEMS.register("ceramic/prospector_hammer_head_mold", () -> new MoldItem(() -> moldProsHammerCapacity.get(), TFCTags.Fluids.USABLE_IN_TOOL_HEAD_MOLD, ceramic_properties()))
                    ;//Big thanks to Alc on Discord for telling me you can just pass in a lambda like this, which makes sense now that I think about it

    public static Item.Properties metal_properties() {
        return new Item.Properties().tab(TFCItemGroup.METAL);
    }

    public static Item.Properties ceramic_properties() {
        return new Item.Properties().tab(TFCItemGroup.MISC);
    }

}
