/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.integration.kubejs;

import com.notenoughmail.precisionprospecting.items.ProspectorItem;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class CustomProsItemBuilder extends AreaHandheldItemBuilder {

    public CustomProsItemBuilder(ResourceLocation resourceLocation) {
        super(resourceLocation, 3f, -2.4f, 15, 12, 12, 0, TFCTags.Blocks.PROSPECTABLE);
    }

    @Override
    public Item createObject() {
        return new ProspectorItem(toolTier, (int) attackDamageBaseline, speedBaseline, createItemProperties(), coolDownBaseline, primaryRadiusBaseline, secondaryRadiusBaseline, displacementBaseline, prospectableBaseline);
    }
}
