package com.notenoughmail.precisionprospecting.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

public class HideableItem extends Item {

    private final boolean isModLoaded;

    public HideableItem(Properties pProperties, String mod) {
        super(pProperties);

        this.isModLoaded = ModList.get().isLoaded(mod);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stack) {
        if (!this.isModLoaded) {
            return;
        }
        super.fillItemCategory(tab, stack);
    }
}
