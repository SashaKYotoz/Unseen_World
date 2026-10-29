package net.sashakyotoz.common.items.custom;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.sashakyotoz.common.blocks.custom.fluids.DarkWaterlogging;
import org.jetbrains.annotations.Nullable;

public class DarkWaterBucketItem extends BucketItem {
    public DarkWaterBucketItem(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hit.getBlockPos();
            if (level.mayInteract(player, pos) && player.mayUseItemAt(pos, hit.getDirection(), stack)
                    && this.fillContainer(level, pos, player)) {
                player.awardStat(Stats.ITEM_USED.get(this));
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(stack, player), level.isClientSide());
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public boolean emptyContents(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult hit, @Nullable ItemStack container) {
        return (hit == null && this.fillContainer(level, pos, player)) || super.emptyContents(player, level, pos, hit, container);
    }

    private boolean fillContainer(Level level, BlockPos pos, @Nullable Player player) {
        if (!DarkWaterlogging.fill(level, pos, level.getBlockState(pos))) {
            return false;
        }
        level.playSound(player, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
        return true;
    }
}