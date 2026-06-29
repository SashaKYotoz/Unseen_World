package net.sashakyotoz.common.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.common.entities.custom.HarmonyWatcherEntity;

public class FertilizeGoal extends Goal {
    private final HarmonyWatcherEntity watcher;

    public FertilizeGoal(HarmonyWatcherEntity mob) {
        this.watcher = mob;
    }

    @Override
    public boolean canUse() {
        return !this.watcher.isAngry && !this.watcher.isDeadOrDying() && this.watcher.level().getBlockState(this.watcher.blockPosition().below()).is(BlockTags.DIRT);
    }

    @Override
    public void start() {
        this.watcher.getNavigation().stop();
        BlockPos pos = this.watcher.blockPosition().below();
        if (this.watcher.level().getBlockState(pos).is(BlockTags.DIRT)){
            BlockState blockState = this.watcher.level().getBlockState(pos);
            if (blockState.getBlock() instanceof BonemealableBlock fertilizable) {
                if (fertilizable.isValidBonemealTarget(this.watcher.level(), pos, blockState, this.watcher.level().isClientSide)) {
                    if (this.watcher.level() instanceof ServerLevel world) {
                        if (fertilizable.isBonemealSuccess(this.watcher.level(), this.watcher.level().random, pos, blockState))
                            fertilizable.performBonemeal(world, world.random, pos, blockState);
                    }
                }
            }
        }
    }
}