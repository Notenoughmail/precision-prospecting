/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.integration.kubejs;

import com.notenoughmail.precisionprospecting.items.ProspectorItem;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CustomProsItemBuilder extends HandheldItemBuilder {

    public transient int coolDownBaseline;
    public transient int primaryRadiusBaseline;
    public transient int secondaryRadiusBaseline;
    public transient int displacementBaseline;
    public transient TagKey<Block> prospectableBaseline;

    public CustomProsItemBuilder(ResourceLocation resourceLocation) {
        super(resourceLocation, 3f, -2.4f);
        this.coolDownBaseline = 15;
        this.primaryRadiusBaseline = 12;
        this.secondaryRadiusBaseline = 12;
        this.displacementBaseline = 0;
        this.prospectableBaseline = TFCTags.Blocks.PROSPECTABLE;
    }


    public CustomProsItemBuilder coolDownBaseline(int i) {
        coolDownBaseline = i;
        return this;
    }

    public CustomProsItemBuilder primaryRadiusBaseline(int i) {
        primaryRadiusBaseline = i;
        return this;
    }

    public CustomProsItemBuilder secondaryRadiusBaseline(int i) {
        secondaryRadiusBaseline = i;
        return this;
    }

    public CustomProsItemBuilder displacementBaseline(int i) {
        displacementBaseline = i;
        return this;
    }

    public CustomProsItemBuilder dimensionsBaseline(int primary, int secondary, int displacement) {
        primaryRadiusBaseline = primary;
        secondaryRadiusBaseline = secondary;
        displacementBaseline = displacement;
        return this;
    }

    public CustomProsItemBuilder prospectBlockTag(ResourceLocation tag) {
        prospectableBaseline = TagKey.create(Registry.BLOCK_REGISTRY, tag);
        return this;
    }

    @Override
    public Item createObject() {
        return new ProspectorItem(toolTier, (int) attackDamageBaseline, speedBaseline, createItemProperties(), coolDownBaseline, primaryRadiusBaseline, secondaryRadiusBaseline, displacementBaseline, prospectableBaseline);
    }
}
