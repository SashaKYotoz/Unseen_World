package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.client.particles.ModParticleTypes;

public class GrippingStoneBlock extends FacingBlock {
    public GrippingStoneBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.UP));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction.getOpposite()));
        return blockState.isOf(this) && blockState.get(FACING) == direction
                ? this.getDefaultState().with(FACING, direction.getOpposite())
                : this.getDefaultState().with(FACING, direction);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof IGrippingEntity entity1
                && entity.age % 10 == 0
                && entity1.getGrippingData() < 10)
            entity1.setGrippingData(entity1.getGrippingData() + 1);
        if (world.getBlockState(pos.up()).isAir() && world.getRandom().nextInt(4) == 1)
            world.addParticle(ModParticleTypes.GRIPPING_CRYSTAL, pos.getX(), pos.getY() + 0.25f, pos.getZ(), 0, 0.1f, 0);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.handleFallDamage(fallDistance, 0.4F, world.getDamageSources().fall());
    }
}