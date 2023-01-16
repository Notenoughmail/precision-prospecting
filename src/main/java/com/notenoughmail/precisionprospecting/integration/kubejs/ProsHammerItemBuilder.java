package com.notenoughmail.precisionprospecting.integration.kubejs;

import com.notenoughmail.precisionprospecting.items.ProsHammerItem;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ProsHammerItemBuilder extends HandheldItemBuilder {

    public ProsHammerItemBuilder(ResourceLocation resourceLocation) {
        super(resourceLocation, 3f, -2.4f);
    }

    @Override
    public Item createObject() {
        return new ProsHammerItem(toolTier, (int) attackDamageBaseline, speedBaseline, createItemProperties());
    }
}
