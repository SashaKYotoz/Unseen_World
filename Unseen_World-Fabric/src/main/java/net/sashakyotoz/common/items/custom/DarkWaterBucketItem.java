package net.sashakyotoz.common.items.custom;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class DarkWaterBucketItem extends BucketItem {
    private final FlowingFluid fluid;

    public DarkWaterBucketItem(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
        this.fluid = fluid;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hit.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (this.canWaterlog(level, pos, state)
                    && level.mayInteract(player, pos)
                    && player.mayUseItemAt(pos, hit.getDirection(), stack)
                    && this.emptyContents(player, level, pos, hit)) {
                this.checkExtraContent(player, level, stack, pos);
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
                }
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(stack, player), level.isClientSide());
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public boolean emptyContents(Player player, Level level, BlockPos pos, BlockHitResult hit) {
        BlockState state = level.getBlockState(pos);
        if (this.canWaterlog(level, pos, state)) {
            ((LiquidBlockContainer) state.getBlock()).placeLiquid(level, pos, state, this.fluid.getSource(false));
            this.playEmptySound(player, level, pos);
            return true;
        }
        return super.emptyContents(player, level, pos, hit);
    }

    private boolean canWaterlog(Level level, BlockPos pos, BlockState state) {
        return state.getBlock() instanceof LiquidBlockContainer container
                && container.canPlaceLiquid(level, pos, state, this.fluid)
                && !(level.dimensionType().ultraWarm() && this.fluid.is(FluidTags.WATER));
    }
}