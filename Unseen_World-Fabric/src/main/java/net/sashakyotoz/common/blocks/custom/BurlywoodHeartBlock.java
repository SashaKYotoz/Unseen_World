package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.sashakyotoz.common.tags.ModTags;
import org.jetbrains.annotations.Nullable;

public class BurlywoodHeartBlock extends Block {
    public static final BooleanProperty PERSISTENT = Properties.PERSISTENT;

    public BurlywoodHeartBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(PERSISTENT, Boolean.FALSE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PERSISTENT);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        if (!state.get(PERSISTENT)) {
            world.playSoundAtBlockCenter(pos, SoundEvents.ENTITY_SHULKER_AMBIENT, SoundCategory.BLOCKS, 1.5f, 0.1f, true);
            if (!player.getWorld().isClient())
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 1));
            if (player.getWorld() instanceof ServerWorld world1)
                world1.spawnParticles(ParticleTypes.GLOW_SQUID_INK, pos.getX(), pos.getY(), pos.getZ(), 9, 0.5, 0.5, 0.5, 0.5);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextDouble() < 0.27 && world.getBlockState(pos.down()).isIn(ModTags.Blocks.CHIMERIC_DARKNESS_STONES)) {
            world.spawnParticles(ParticleTypes.GLOW, pos.getX(), pos.getY(), pos.getZ(), 9, 0.5, 0.5, 0.5, 0.5);
            world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_AZALEA_LEAVES_HIT, SoundCategory.BLOCKS, 1.5f, 0.5f, false);
            world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 3);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(PERSISTENT, Boolean.TRUE);
    }
}