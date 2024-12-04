package net.sashakyotoz.common.entities.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.custom.EldritchWatcherEntity;

public class EldritchWatcherPlaceBlockGoal extends Goal {
    private final EldritchWatcherEntity watcher;

    public EldritchWatcherPlaceBlockGoal(EldritchWatcherEntity watcher) {
        this.watcher = watcher;
    }

    @Override
    public boolean canStart() {
        if (!this.watcher.isCarringBlock())
            return false;
        else
            return this.watcher.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && this.watcher.getRandom().nextInt(toGoalTicks(2000)) == 0;

    }

    @Override
    public void tick() {
        Random random = this.watcher.getRandom();
        World world = this.watcher.getWorld();
        int i = MathHelper.floor(this.watcher.getX() - 1.0 + random.nextDouble() * 2.0);
        int j = MathHelper.floor(this.watcher.getY() + random.nextDouble() * 2.0);
        int k = MathHelper.floor(this.watcher.getZ() - 1.0 + random.nextDouble() * 2.0);
        BlockPos blockPos = new BlockPos(i, j, k);
        BlockState blockState = world.getBlockState(blockPos);
        BlockPos blockPos2 = blockPos.down();
        BlockState blockState2 = world.getBlockState(blockPos2);
        BlockState blockState3 = ModBlocks.GRIPCRYSTAL_WART.getDefaultState().with(Properties.FACING, Direction.UP);
        if (blockState3 != null) {
            blockState3 = Block.postProcessState(blockState3, this.watcher.getWorld(), blockPos);
            if (this.canPlaceOn(world, blockPos, blockState3, blockState, blockState2, blockPos2)) {
                world.setBlockState(blockPos, blockState3, Block.NOTIFY_ALL);
                world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Emitter.of(this.watcher, blockState3));
                this.watcher.setCarriedBlock(false);
            }
        }
    }

    private boolean canPlaceOn(World world, BlockPos posAbove, BlockState carriedState, BlockState stateAbove, BlockState state, BlockPos pos) {
        return stateAbove.isAir()
                && !state.isAir()
                && !state.isOf(Blocks.BEDROCK)
                && state.isFullCube(world, pos)
                && carriedState.canPlaceAt(world, posAbove)
                && world.getOtherEntities(this.watcher, Box.from(Vec3d.of(posAbove))).isEmpty();
    }
}