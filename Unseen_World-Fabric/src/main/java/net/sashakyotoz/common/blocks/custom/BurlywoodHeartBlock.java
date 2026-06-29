package net.sashakyotoz.common.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.sashakyotoz.common.tags.ModTags;
import org.jetbrains.annotations.Nullable;

public class BurlywoodHeartBlock extends Block {
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    public BurlywoodHeartBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(PERSISTENT, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PERSISTENT);
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(world, player, pos, state, blockEntity, tool);
        if (!state.getValue(PERSISTENT)) {
            world.playLocalSound(pos, SoundEvents.SHULKER_AMBIENT, SoundSource.BLOCKS, 1.5f, 0.1f, true);
            if (!player.level().isClientSide())
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 1));
            if (player.level() instanceof ServerLevel world1)
                world1.sendParticles(ParticleTypes.GLOW_SQUID_INK, pos.getX(), pos.getY(), pos.getZ(), 9, 0.5, 0.5, 0.5, 0.5);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextDouble() < 0.27 && world.getBlockState(pos.below()).is(ModTags.Blocks.CHIMERIC_DARKNESS_STONES)) {
            world.sendParticles(ParticleTypes.GLOW, pos.getX(), pos.getY(), pos.getZ(), 9, 0.5, 0.5, 0.5, 0.5);
            world.playLocalSound(pos, SoundEvents.AZALEA_LEAVES_HIT, SoundSource.BLOCKS, 1.5f, 0.5f, false);
            world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 3);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(PERSISTENT, Boolean.TRUE);
    }
}