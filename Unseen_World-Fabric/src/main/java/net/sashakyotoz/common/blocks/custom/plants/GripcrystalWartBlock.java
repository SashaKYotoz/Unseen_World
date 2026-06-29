package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.utils.ActionsUtils;

public class GripcrystalWartBlock extends AmethystClusterBlock {
    public GripcrystalWartBlock(Properties settings) {
        super(12, 3, settings);
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(world, state, pos, entity, fallDistance);
        gripNearby(entity.level(), entity.blockPosition());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt(9) == 1) {
            if (world.getBlockState(pos.above()).isAir())
                gripNearby(world, pos.above());
            if (world.getBlockState(pos.below()).isAir())
                gripNearby(world, pos.below(2));
        }
    }

    private void gripNearby(Level world, BlockPos pos) {
        world.getEntitiesOfClass(LivingEntity.class,
                new AABB(pos.getCenter(), pos.getCenter()).inflate(6), entity ->
                        entity instanceof IGrippingEntity).forEach(entity -> {
            GrippingData.addGrippingSeconds((IGrippingEntity) entity, 4);
            world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1.2F, 1.8F);
            ActionsUtils.spawnParticle(ParticleTypes.BUBBLE_POP, world, pos.getX(), pos.getY(), pos.getZ(), 4f);
        });
    }
}