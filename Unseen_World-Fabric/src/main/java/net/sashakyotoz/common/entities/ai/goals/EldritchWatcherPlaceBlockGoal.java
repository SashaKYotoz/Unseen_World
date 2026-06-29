package net.sashakyotoz.common.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.custom.EldritchWatcherEntity;

public class EldritchWatcherPlaceBlockGoal extends Goal {
    private final EldritchWatcherEntity watcher;

    public EldritchWatcherPlaceBlockGoal(EldritchWatcherEntity watcher) {
        this.watcher = watcher;
    }

    @Override
    public boolean canUse() {
        if (!this.watcher.isCarringBlock())
            return false;
        else
            return this.watcher.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.watcher.getRandom().nextInt(reducedTickDelay(2000)) == 0;
    }

    @Override
    public void tick() {
        RandomSource random = this.watcher.getRandom();
        Level world = this.watcher.level();
        int i = Mth.floor(this.watcher.getX() - 1.0 + random.nextDouble() * 2.0);
        int j = Mth.floor(this.watcher.getY() + random.nextDouble() * 2.0);
        int k = Mth.floor(this.watcher.getZ() - 1.0 + random.nextDouble() * 2.0);
        BlockPos blockPos = new BlockPos(i, j, k);
        BlockState blockState = world.getBlockState(blockPos);
        BlockPos blockPos2 = blockPos.below();
        BlockState blockState2 = world.getBlockState(blockPos2);
        BlockState blockState3 = ModBlocks.GRIPCRYSTAL_WART.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP);
        if (blockState3 != null) {
            blockState3 = Block.updateFromNeighbourShapes(blockState3, this.watcher.level(), blockPos);
            if (this.canPlaceOn(world, blockPos, blockState3, blockState, blockState2, blockPos2)) {
                world.setBlock(blockPos, blockState3, Block.UPDATE_ALL);
                world.gameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(this.watcher, blockState3));
                this.watcher.setCarriedBlock(false);
            }
        }
    }

    private boolean canPlaceOn(Level world, BlockPos posAbove, BlockState carriedState, BlockState stateAbove, BlockState state, BlockPos pos) {
        return stateAbove.isAir()
                && !state.isAir()
                && !state.is(Blocks.BEDROCK)
                && state.isCollisionShapeFullBlock(world, pos)
                && carriedState.canSurvive(world, posAbove)
                && world.getEntities(this.watcher, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(posAbove))).isEmpty();
    }
}