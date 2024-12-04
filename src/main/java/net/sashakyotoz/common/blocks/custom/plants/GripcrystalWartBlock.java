package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.networking.data.GrippingData;
import net.sashakyotoz.utils.ActionsManager;
import net.sashakyotoz.utils.IEntityDataSaver;

public class GripcrystalWartBlock extends AmethystClusterBlock {
    public GripcrystalWartBlock(Settings settings) {
        super(12, 3, settings);
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient) {
            BlockPos blockPos = hit.getBlockPos();
            world.playSound(null, blockPos, SoundEvents.BLOCK_AZALEA_HIT, SoundCategory.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
            world.playSound(null, blockPos, SoundEvents.BLOCK_SCULK_CHARGE, SoundCategory.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof ServerPlayerEntity player && player.age % 10 == 0) {
            if (GrippingData.getGrippingTime((IEntityDataSaver) player) <= 0) {
                ActionsManager.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL, player.getServerWorld(), player.getX(), player.getY(), player.getZ(), 2);
                GrippingData.addGrippingSeconds((IEntityDataSaver) player, 6);
            }
        }
    }
}