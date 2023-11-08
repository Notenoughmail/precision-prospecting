/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static com.notenoughmail.precisionprospecting.PrecisionProspecting.MODID;

public class Tags {

    public static class Blocks {
        public static final TagKey<Block> PROSPECTABLE_MINERAL = TagKey.create(Registries.BLOCK, new ResourceLocation(MODID, "prospectable_mineral"));
    }
}
