/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.items;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.PropickItem;
import net.dries007.tfc.common.items.ProspectResult;
import net.dries007.tfc.common.items.ToolItem;
import net.dries007.tfc.network.PacketHandler;
import net.dries007.tfc.network.ProspectedPacket;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.events.ProspectedEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
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
import java.util.function.Supplier;

/**
 * This is an edited copy of {@link PropickItem PropickItem}. <br>
 * The radius has been split into a primary and secondary radius and a displacement which are
 * Integer Suppliers (not IntSuppliers). <br>
 * {@link PropickItem#scanAreaFor(Level, BlockPos, int, TagKey) scanAreaFor()} has been modified
 * to take 6 ints which are given to {@link BlockPos#betweenClosed(int, int, int, int, int, int) BlockPos#BetweenClosed()}
 * instead of a BlockPos and int.<br>
 * The ints are produced by a switch statement in {@link ProspectorItem#useOn(UseOnContext)}.
 * Additionally, the TagKey given to the scan method is one provided to the item in its constructor,
 * rather than only {@link TFCTags.Blocks#PROSPECTABLE}.
 */

public class ProspectorItem extends ToolItem {

    public int COOLDOWN;
    public Supplier<Integer> PRIMARY_RADIUS;
    public Supplier<Integer> SECONDARY_RADIUS;
    public Supplier<Integer> DISPLACEMENT;
    public TagKey<Block> PROSPECT_TAG;

    private static final Random RANDOM = new Random();

    public static Object2IntMap<Block> scanAreaFor(Level level, TagKey<Block> tag, int pX1, int pY1, int pZ1, int pX2, int pY2, int pZ2) {
        Object2IntMap<Block> results = new Object2IntOpenHashMap<>();
        for (BlockPos cursor : BlockPos.betweenClosed(pX1, pY1, pZ1, pX2, pY2, pZ2)) {
            Block block = level.getBlockState(cursor).getBlock();
            // Coming soon™
            // Block representative = PropickItem.getRepresentative(block);
            // if (representative != null) {
            //     block = representative;
            // }
            if (Helpers.isBlock(block, tag)) {
                results.mergeInt(block, 1, Integer::sum);
            }
        }
        return results;
    }

    private final float falseNegativeChance;

    public ProspectorItem(Tier tier, float attackDamage, float attackSpeed, Properties properties, int cooldown, Supplier<Integer> primaryRadius, Supplier<Integer> secondaryRadius, Supplier<Integer> displacement, TagKey<Block> prospectTag) {
        super(tier, attackDamage, attackSpeed, TFCTags.Blocks.MINEABLE_WITH_PROPICK, properties);
        this.COOLDOWN = cooldown;
        this.PRIMARY_RADIUS = primaryRadius;
        this.SECONDARY_RADIUS = secondaryRadius;
        this.DISPLACEMENT = displacement;
        this.PROSPECT_TAG = prospectTag;

        this.falseNegativeChance = 0.3f - Mth.clamp(tier.getLevel(), 0, 5) * (0.3f / 5f);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        final Level level = context.getLevel();
        final Player player = context.getPlayer();
        final BlockPos pos = context.getClickedPos();
        final BlockState state = level.getBlockState(pos);
        final Direction direction = context.getClickedFace().getOpposite();

        int pR = PRIMARY_RADIUS.get();
        int sR = SECONDARY_RADIUS.get();
        int di = DISPLACEMENT.get();

        int pX1 = pos.getX() - pR;
        int pX2 = pos.getX() + pR;
        int pY1 = pos.getY() - pR;
        int pY2 = pos.getY() + pR;
        int pZ1 = pos.getZ() - pR;
        int pZ2 = pos.getZ() + pR;

        switch (direction) {
            case UP -> {
                pY1 = pos.getY() + di - sR;
                pY2 = pos.getY() + di + sR;
            }
            case DOWN -> {
                pY1 = pos.getY() - di - sR;
                pY2 = pos.getY() - di + sR;
            }
            case NORTH -> {
                pZ1 = pos.getZ() - di - sR;
                pZ2 = pos.getZ() - di + sR;
            }
            case SOUTH -> {
                pZ1 = pos.getZ() + di - sR;
                pZ2 = pos.getZ() + di + sR;
            }
            case EAST -> {
                pX1 = pos.getX() + di - sR;
                pX2 = pos.getX() + di + sR;
            }
            case WEST -> {
                pX1 = pos.getX() - di - sR;
                pX2 = pos.getX() - di + sR;
            }
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            final SoundType sound = state.getSoundType(level, pos, player);
            level.playSound(player, pos, sound.getHitSound(), SoundSource.PLAYERS, sound.getVolume(), sound.getPitch());

            context.getItemInHand().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
            player.getCooldowns().addCooldown(this, COOLDOWN);

            ProspectResult result;
            Block found = state.getBlock();
            RANDOM.setSeed(Helpers.hash(1564454769121215456L, pos));
            if (Helpers.isBlock(state, this.PROSPECT_TAG)) {
                result = ProspectResult.FOUND;
            } else if (RANDOM.nextFloat() < falseNegativeChance) {
                result = ProspectResult.NOTHING;
            } else {
                Object2IntMap<Block> states = scanAreaFor(level, PROSPECT_TAG, pX1, pY1, pZ1, pX2, pY2, pZ2);
                if (states.isEmpty()) {
                    result = ProspectResult.NOTHING;
                } else {
                    final ArrayList<Block> stateKeys = new ArrayList<>(states.keySet());
                    found = stateKeys.get(RANDOM.nextInt(stateKeys.size()));
                    final int amount = states.getOrDefault(found, 1);

                    if (amount < 10) result = ProspectResult.TRACES;
                    else if (amount < 20) result = ProspectResult.SMALL;
                    else if (amount < 40) result = ProspectResult.MEDIUM;
                    else if (amount < 80) result = ProspectResult.LARGE;
                    else result = ProspectResult.VERY_LARGE;
                }
            }

            MinecraftForge.EVENT_BUS.post(new ProspectedEvent(player, result, found));
            PacketHandler.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ProspectedPacket(found, result));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> text, TooltipFlag flag) {
        if (flag.isAdvanced()) {
            text.add(Component.translatable("tfc.tooltip.propick.accuracy", (int) (100 * (1 - falseNegativeChance))).withStyle(ChatFormatting.GRAY));
        }
    }
}
