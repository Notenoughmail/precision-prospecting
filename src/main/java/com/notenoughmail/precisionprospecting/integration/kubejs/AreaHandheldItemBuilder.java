/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.integration.kubejs;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AreaHandheldItemBuilder extends HandheldItemBuilder {

    public transient int coolDownBaseline;
    public transient int primaryRadiusBaseline;
    public transient int secondaryRadiusBaseline;
    public transient int displacementBaseline;
    public transient TagKey<Block> prospectableBaseline;

    public AreaHandheldItemBuilder(ResourceLocation i, float d, float s, int coolDown, int primaryRadius, int secondaryRadius, int displacement, TagKey<Block> prospectTag) {
        super(i, d, s);
        coolDownBaseline = coolDown;
        primaryRadiusBaseline = primaryRadius;
        secondaryRadiusBaseline = secondaryRadius;
        displacementBaseline = displacement;
        prospectableBaseline = prospectTag;
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

    public HandheldItemBuilder dimensionsBaseline(int primary, int secondary, int displacement) {
        primaryRadiusBaseline = primary;
        secondaryRadiusBaseline = secondary;
        displacementBaseline = displacement;
        return this;
    }

    // This is how KubeJS does it, hopefully it works
    public HandheldItemBuilder prospectBlockTag(ResourceLocation tag) {
        prospectableBaseline = TagKey.create(Registry.BLOCK_REGISTRY, tag);
        return this;
    }

    @Override
    public Item createObject() {
        return null;
    }
}
