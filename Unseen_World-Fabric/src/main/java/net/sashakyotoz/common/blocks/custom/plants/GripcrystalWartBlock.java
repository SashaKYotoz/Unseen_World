package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.utils.ActionsUtils;

public class GripcrystalWartBlock extends AmethystClusterBlock {
    public GripcrystalWartBlock(Settings settings) {
        super(12, 3, settings);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.onLandedUpon(world, state, pos, entity, fallDistance);
        gripNearby(entity.getWorld(), entity.getBlockPos());
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(9) == 1) {
            if (world.getBlockState(pos.up()).isAir())
                gripNearby(world, pos.up());
            if (world.getBlockState(pos.down()).isAir())
                gripNearby(world, pos.down(2));
        }
    }

    private void gripNearby(World world, BlockPos pos) {
        world.getEntitiesByClass(LivingEntity.class,
                new Box(pos.toCenterPos(), pos.toCenterPos()).expand(6), entity ->
                        entity instanceof IGrippingEntity).forEach(entity -> {
            GrippingData.addGrippingSeconds((IGrippingEntity) entity, 4);
            world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.BLOCKS, 1.2F, 1.8F);
            ActionsUtils.spawnParticle(ParticleTypes.BUBBLE_POP, world, pos.getX(), pos.getY(), pos.getZ(), 4f);
        });
    }
}