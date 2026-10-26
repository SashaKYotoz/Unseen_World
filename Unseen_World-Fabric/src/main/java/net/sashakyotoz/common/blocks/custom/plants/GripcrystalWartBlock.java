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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.common.blocks.custom.fluids.DualWaterloggedBlock;
import net.sashakyotoz.common.effects.ModMobEffects;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.List;

public class GripcrystalWartBlock extends AmethystClusterBlock implements DualWaterloggedBlock {
    public GripcrystalWartBlock(Properties settings) {
        super(12, 3, settings);
        this.defaultBlockState().setValue(DualWaterloggedBlock.DARK_WATER, Boolean.FALSE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DualWaterloggedBlock.DARK_WATER, WATERLOGGED, FACING);
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
        if (world.isClientSide) {
            return;
        }
        List<LivingEntity> gripped = world.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(6), entity -> entity instanceof IGrippingEntity);
        if (gripped.isEmpty()) {
            return;
        }
        gripped.forEach(entity -> {
            if (!entity.hasEffect(ModMobEffects.GRIPPING_RESISTANCE.get())) {
                GrippingData.addGrippingSeconds((IGrippingEntity) entity, 6);
            }
        });
        world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1.2F, 1.8F);
        ActionsUtils.spawnParticle(ParticleTypes.BUBBLE_POP, world, pos.getX(), pos.getY(), pos.getZ(), 4f);
    }
}