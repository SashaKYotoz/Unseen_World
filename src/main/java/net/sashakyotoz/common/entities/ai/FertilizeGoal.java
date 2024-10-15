package net.sashakyotoz.common.entities.ai;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.sashakyotoz.common.entities.HarmonyWatcherEntity;

public class FertilizeGoal extends Goal {
    private final HarmonyWatcherEntity watcher;

    public FertilizeGoal(HarmonyWatcherEntity mob) {
        this.watcher = mob;
    }

    @Override
    public boolean canStart() {
        return !this.watcher.isAngry && !this.watcher.isDead() && this.watcher.getWorld().getBlockState(this.watcher.getBlockPos().down()).isIn(BlockTags.DIRT);
    }

    @Override
    public void start() {
        this.watcher.getNavigation().stop();
        BlockPos pos = this.watcher.getBlockPos().down();
        if (this.watcher.getWorld().getBlockState(pos).isIn(BlockTags.DIRT)){
            BlockState blockState = this.watcher.getWorld().getBlockState(pos);
            if (blockState.getBlock() instanceof Fertilizable fertilizable) {
                if (fertilizable.isFertilizable(this.watcher.getWorld(), pos, blockState, this.watcher.getWorld().isClient)) {
                    if (this.watcher.getWorld() instanceof ServerWorld world) {
                        if (fertilizable.canGrow(this.watcher.getWorld(), this.watcher.getWorld().random, pos, blockState))
                            fertilizable.grow(world, world.random, pos, blockState);
                    }
                }
            }
        }
    }
}