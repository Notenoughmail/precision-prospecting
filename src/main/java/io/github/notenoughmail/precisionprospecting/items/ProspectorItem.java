package io.github.notenoughmail.precisionprospecting.items;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.dries007.tfc.common.LevelTier;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.PropickItem;
import net.dries007.tfc.common.items.ProspectResult;
import net.dries007.tfc.common.items.ToolItem;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.IntSupplier;

/**
 * A modified copy of {@link PropickItem} which has a custom scan area and prospectable tag
 */
public class ProspectorItem extends ToolItem {

    public static Object2IntMap<Block> scanAreaFor(Level level, TagKey<Block> tag, int pX1, int pY1, int pZ1, int pX2, int pY2, int pZ2) {
        final Object2IntMap<Block> results = new Object2IntOpenHashMap<>();
        for (BlockPos pos : BlockPos.betweenClosed(pX1, pY1, pZ1, pX2, pY2, pZ2)) {
            final Block block = PropickItem.getRepresentative(level.getBlockState(pos).getBlock()); // ❤️
            if (Helpers.isBlock(block, tag)) {
                results.merge(block, 1, Integer::sum);
            }
        }
        return results;
    }

    public final IntSupplier
        primaryRadius,
        secondaryRadius,
        displacement;

    private final float falseNegativeChance;

    private final TagKey<Block> prospectTag;

    private final int cooldown;

    public ProspectorItem(Tier tier, int numericalTier, Properties properties, IntSupplier primaryRadius, IntSupplier secondaryRadius, IntSupplier displacement, TagKey<Block> prospectTag, int coolDown) {
        super(tier, TFCTags.Blocks.MINEABLE_WITH_PROPICK, properties);
        this.primaryRadius = primaryRadius;
        this.secondaryRadius = secondaryRadius;
        this.displacement = displacement;

        falseNegativeChance = 0.3F - Mth.clamp(numericalTier, 0, 5) * (0.3F / 5F);

        this.prospectTag = prospectTag;
        this.cooldown = coolDown;
    }

    public ProspectorItem(LevelTier tier, Properties properties, IntSupplier primaryRadius, IntSupplier secondaryRadius, IntSupplier displacement, TagKey<Block> prospectTag, int coolDown) {
        this(tier, tier.level(), properties, primaryRadius, secondaryRadius, displacement, prospectTag, coolDown);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        final Player player = ctx.getPlayer();
        if (player instanceof ServerPlayer serverPlayer) {
            final Level level = ctx.getLevel();
            final BlockPos pos = ctx.getClickedPos();
            final BlockState state = level.getBlockState(pos);
            final Direction dir = ctx.getClickedFace().getOpposite();

            int pR = primaryRadius.getAsInt(),
                sR = secondaryRadius.getAsInt(),
                di = displacement.getAsInt();

            int pX1 = pos.getX() - pR,
                pX2 = pos.getX() + pR,
                pY1 = pos.getY() - pR,
                pY2 = pos.getY() + pR,
                pZ1 = pos.getZ() - pR,
                pZ2 = pos.getZ() + pR;

            switch (dir) {
                case UP -> {
                    pY1 = pos.getY() + di - sR;
                    pY2 = pos.getY() + di + sR;
                }
                case DOWN -> {
                    pY1 = pos.getY() - di - sR;
                    pY2 = pos.getY() - di + sR;
                }
                case SOUTH -> {
                    pZ1 = pos.getZ() + di - sR;
                    pZ2 = pos.getZ() + di + sR;
                }
                case NORTH -> {
                    pZ1 = pos.getZ() - di - sR;
                    pZ2 = pos.getZ() - di + sR;
                }
                case EAST -> {
                    pX1 = pos.getX() + di - sR;
                    pX2 = pos.getX() + di + sR;
                }
                case WEST -> {
                    pX1 = pos.getX() - di - sR;
                    pX2 = pos.getX() - di + sR;
                }
            }

            final SoundType sound = state.getSoundType(level, pos, player);
            level.playSound(player, pos, sound.getHitSound(), SoundSource.PLAYERS, sound.getVolume(), sound.getPitch());

            Helpers.damageItem(ctx.getItemInHand(), player, ctx.getHand());
            player.getCooldowns().addCooldown(this, cooldown);

            final Random random = new Random();
            random.setSeed(Helpers.hash(1564454769121215456L, pos));

            Block block = state.getBlock();
            final ProspectResult result;
            if (Helpers.isBlock(block, prospectTag)) {
                result = ProspectResult.FOUND;
            } else if (random.nextFloat() < falseNegativeChance) {
                result = ProspectResult.NOTHING;
            } else {
                final Object2IntMap<Block> states = scanAreaFor(level, prospectTag, pX1, pY1, pZ1, pX2, pY2, pZ2);
                if (states.isEmpty()) {
                    result = ProspectResult.NOTHING;
                } else {
                    final List<Block> stateKeys = new ArrayList<>(states.keySet());
                    block = stateKeys.get(random.nextInt(stateKeys.size()));
                    final int amount = states.getOrDefault(block, 1);
                    result = amount < 10 ?
                            ProspectResult.TRACES :
                            amount < 20 ?
                                    ProspectResult.SMALL :
                                    amount < 40 ?
                                            ProspectResult.MEDIUM :
                                            amount < 80 ?
                                                    ProspectResult.LARGE :
                                                    ProspectResult.VERY_LARGE;
                }
            }

            NeoForge.EVENT_BUS.post(new ProspectedEvent(player, result, block));
            PacketDistributor.sendToPlayer(serverPlayer, new ProspectedPacket(block, result));
        }
        return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> text, TooltipFlag flags) {
        if (flags.isAdvanced()) {
            text.add(Component.translatable("tfc.tooltip.propick.accuracy", (int) (100 * (1 - falseNegativeChance))).withStyle(ChatFormatting.GRAY));
        }
    }
}
