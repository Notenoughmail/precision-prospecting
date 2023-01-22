package com.notenoughmail.precisionprospecting.items;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static com.notenoughmail.precisionprospecting.PrecisionProspecting.MODID;

public class Tags {

    public static class Blocks {
        public static final TagKey<Block> PROSPECTABLE_MINERAL = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(MODID, "prospectable_mineral"));
    }
}
