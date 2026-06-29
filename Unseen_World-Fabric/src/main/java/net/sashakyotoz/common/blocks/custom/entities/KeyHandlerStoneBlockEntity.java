package net.sashakyotoz.common.blocks.custom.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.custom.KeyHandlerStoneBlock;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

public class KeyHandlerStoneBlockEntity extends BlockEntity {
    public int ticks = 0;
    public KeyHandlerStoneData data;

    public KeyHandlerStoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KEY_HANDLER, pos, state);
        this.data = state.getValue(BlockStateProperties.LOCKED) ?
                new KeyHandlerStoneData(false, 0f, false, 0f, 0) :
                new KeyHandlerStoneData(true, 0f, true, 0f, 0);
    }

    @Override
    public void load(CompoundTag nbt) {
        this.data = new KeyHandlerStoneData(
                nbt.getBoolean("firstKeyIn"),
                nbt.getFloat("firstKeyOffset"),
                nbt.getBoolean("secondKeyIn"),
                nbt.getFloat("secondKeyOffset"),
                nbt.getInt("cooldown"));
        super.load(nbt);
    }

    public ItemStack keyToRenderer(@Nullable Level world, BlockPos pos) {
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            if (state.hasProperty(KeyHandlerStoneBlock.HANDLER_TYPE)) {
                return switch (state.getValue(KeyHandlerStoneBlock.HANDLER_TYPE)) {
                    case GOLDEN -> ModItems.AURIC_KEY.getDefaultInstance();
                    case DARK_CURRANTSLATE -> ModItems.ABYSSAL_KEY.getDefaultInstance();
                    case GLACIEMITE -> ModItems.GRIPCRYSTAL_KEY.getDefaultInstance();
                };
            }
        }
        return ModItems.ABYSSAL_KEY.getDefaultInstance();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putBoolean("firstKeyIn", this.data.firstKeyIn());
        nbt.putBoolean("secondKeyIn", this.data.secondKeyIn());
        nbt.putFloat("firstKeyOffset", this.data.firstKeyOffset());
        nbt.putFloat("secondKeyOffset", this.data.secondKeyOffset());
        nbt.putFloat("cooldown", this.data.cooldown());
        super.saveAdditional(nbt);
    }

    private static ParticleOptions getTypeParticle(BlockState state) {
        return switch (state.getValue(KeyHandlerStoneBlock.HANDLER_TYPE)) {
            case GLACIEMITE ->
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIAMOND_BLOCK.defaultBlockState());
            case DARK_CURRANTSLATE -> ParticleTypes.CRIMSON_SPORE;
            case GOLDEN -> new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GOLD_BLOCK.defaultBlockState());
        };
    }

    public static void clientTick(Level world, BlockPos pos, BlockState state, KeyHandlerStoneBlockEntity entity) {
        entity.ticks++;
        if (entity.data.cooldown() > 0)
            entity.data = new KeyHandlerStoneData(
                    entity.data.firstKeyIn(),
                    entity.data.firstKeyOffset(),
                    entity.data.secondKeyIn(),
                    entity.data.secondKeyOffset(),
                    entity.data.cooldown() - 1
            );
        if (entity.data.firstKeyIn() && entity.data.secondKeyIn() && entity.ticks % 5 == 0)
            world.addParticle(
                    getTypeParticle(state),
                    pos.getX() + 0.5,
                    pos.getY() + 1,
                    pos.getZ() + 0.5,
                    0,
                    1,
                    0);
        if (entity.data.firstKeyOffset() > 0)
            entity.data = new KeyHandlerStoneData(
                    entity.data.firstKeyIn(),
                    entity.data.firstKeyOffset() - 0.1f,
                    entity.data.secondKeyIn(),
                    entity.data.secondKeyOffset(),
                    entity.data.cooldown());
        if (entity.data.secondKeyOffset() < 0)
            entity.data = new KeyHandlerStoneData(
                    entity.data.firstKeyIn(),
                    entity.data.firstKeyOffset(),
                    entity.data.secondKeyIn(),
                    entity.data.secondKeyOffset() + 0.1f,
                    entity.data.cooldown());
    }

    public static void serverTick(Level world, BlockPos pos, BlockState state, KeyHandlerStoneBlockEntity entity) {
        entity.ticks++;
        if (entity.data.cooldown() > 0)
            entity.data = new KeyHandlerStoneData(
                    entity.data.firstKeyIn(),
                    entity.data.firstKeyOffset(),
                    entity.data.secondKeyIn(),
                    entity.data.secondKeyOffset(),
                    entity.data.cooldown() - 1
            );
        if (entity.data.firstKeyOffset() > 0)
            entity.data = new KeyHandlerStoneData(
                    entity.data.firstKeyIn(),
                    entity.data.firstKeyOffset() - 0.1f,
                    entity.data.secondKeyIn(),
                    entity.data.secondKeyOffset(),
                    entity.data.cooldown());
        if (entity.data.secondKeyOffset() < 0)
            entity.data = new KeyHandlerStoneData(
                    entity.data.firstKeyIn(),
                    entity.data.firstKeyOffset(),
                    entity.data.secondKeyIn(),
                    entity.data.secondKeyOffset() + 0.1f,
                    entity.data.cooldown());
    }
}