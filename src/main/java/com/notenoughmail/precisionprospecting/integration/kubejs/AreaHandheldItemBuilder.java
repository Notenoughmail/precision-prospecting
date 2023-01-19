package com.notenoughmail.precisionprospecting.integration.kubejs;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class AreaHandheldItemBuilder extends HandheldItemBuilder {

    public transient int coolDownBaseline;
    public transient int primaryRadiusBaseline;
    public transient int secondaryRadiusBaseline;
    public transient int displacementBaseline;

    public AreaHandheldItemBuilder(ResourceLocation i, float d, float s, int coolDown, int primaryRadius, int secondaryRadius, int displacement) {
        super(i, d, s);
        coolDownBaseline = coolDown;
        primaryRadiusBaseline = primaryRadius;
        secondaryRadiusBaseline = secondaryRadius;
        displacementBaseline = displacement;
    }

    public HandheldItemBuilder coolDownBaseline(int i) {
        coolDownBaseline = i;
        return this;
    }

    public HandheldItemBuilder primaryRadiusBaseline(int i) {
        primaryRadiusBaseline = i;
        return this;
    }

    public HandheldItemBuilder secondaryRadiusBaseline(int i) {
        secondaryRadiusBaseline = i;
        return this;
    }

    public HandheldItemBuilder displacementBaseline(int i) {
        displacementBaseline = i;
        return this;
    }

    @Override
    public Item createObject() {
        return null;
    }
}
