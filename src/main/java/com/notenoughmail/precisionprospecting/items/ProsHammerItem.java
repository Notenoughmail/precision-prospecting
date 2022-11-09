/* This is a near word-for-word copy of tfc/common/items/PropickItem
 * RADIUS, COOLDOWN,the first float defining falseNegativeChance, and the hash for the RANDOM seed all differ from the original
 * falseNegativeChance has been renamed to falseNegChance
 * the BlockPos value of scanAreaFor has been renamed from center to position
 */

/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.items;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.ProspectResult;
import net.dries007.tfc.common.items.ToolItem;
import net.dries007.tfc.network.PacketHandler;
import net.dries007.tfc.network.ProspectedPacket;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.events.ProspectedEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProsHammerItem extends ToolItem {

    public static final int RADIUS = 6;
    public static final int COOLDOWN = 15;

    private static final Random RANDOM = new Random();

    public static Object2IntMap<BlockState> scanAreaFor(Level level, BlockPos position, int radius, TagKey<Block> tag) {
        final Object2IntMap<BlockState> results = new Object2IntOpenHashMap<>();
        for (BlockPos cursor : BlockPos.betweenClosed(position.getX() - radius, position.getY() - radius, position.getZ() - radius, position.getX() + radius, position.getY() + radius, position.getZ() + radius)) {
            final BlockState state = level.getBlockState(cursor);
            if (Helpers.isBlock(state, tag)) {
                results.mergeInt(state, 1, Integer::sum);
            }
        }
        return results;
    }

    private final float falseNegChance;

    public ProsHammerItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, TFCTags.Blocks.MINEABLE_WITH_PROPICK, properties);

            this.falseNegChance = 0.33f - Mth.clamp(tier.getLevel(), 0, 5) * (0.3f / 5f);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        final Level level = context.getLevel();
        final Player player = context.getPlayer();
        final BlockPos pos = context.getClickedPos();
        final BlockState state = level.getBlockState(pos);

        if (player instanceof ServerPlayer serverPlayer) {
            final SoundType sound = state.getSoundType(level, pos, player);
            level.playSound(player, pos, sound.getHitSound(), SoundSource.PLAYERS, sound.getVolume(), sound.getPitch());

            context.getItemInHand().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
            player.getCooldowns().addCooldown(this, COOLDOWN);

            ProspectResult result;
            BlockState found = state;
            RANDOM.setSeed(Helpers.hash(1564454769121215456L, pos));
            if (Helpers.isBlock(state, TFCTags.Blocks.PROSPECTABLE)) {
                result = ProspectResult.FOUND;
            } else if (RANDOM.nextFloat() < falseNegChance) {
                result = ProspectResult.NOTHING;
            } else {
                final Object2IntMap<BlockState> states = scanAreaFor(level, pos, RADIUS, TFCTags.Blocks.PROSPECTABLE);
                if (states.isEmpty()) {
                    result = ProspectResult.NOTHING;
                } else {
                    final ArrayList<BlockState> stateKeys = new ArrayList<>(states.keySet());
                    found = stateKeys.get(RANDOM.nextInt(stateKeys.size()));
                    final int amount = states.getOrDefault(found, 1);

                    if (amount < 10) result = ProspectResult.TRACES;
                    else if (amount < 20) result = ProspectResult.SMALL;
                    else if (amount < 40) result = ProspectResult.MEDIUM;
                    else if (amount < 80) result = ProspectResult.LARGE;
                    else result = ProspectResult.VERY_LARGE;
                }
            }

            MinecraftForge.EVENT_BUS.post(new ProspectedEvent(player, result, found.getBlock()));
            PacketHandler.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ProspectedPacket(found.getBlock(), result));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> text, TooltipFlag flag) {
        if (flag.isAdvanced()) {
            text.add(Helpers.translatable("tfc.tooltip.propick.accuracy", (int) (100 * (1 - falseNegChance))).withStyle(ChatFormatting.GRAY));
        }
    }
}
