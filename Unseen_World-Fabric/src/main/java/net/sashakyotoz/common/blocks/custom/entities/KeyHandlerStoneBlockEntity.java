package net.sashakyotoz.common.blocks.custom.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.custom.KeyHandlerStoneBlock;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

public class KeyHandlerStoneBlockEntity extends BlockEntity {
    public int ticks = 0;
    public KeyHandlerStoneData data;

    public KeyHandlerStoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KEY_HANDLER, pos, state);
        this.data = state.get(Properties.LOCKED) ?
                new KeyHandlerStoneData(false, 0f, false, 0f, 0) :
                new KeyHandlerStoneData(true, 0f, true, 0f, 0);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.data = new KeyHandlerStoneData(
                nbt.getBoolean("firstKeyIn"),
                nbt.getFloat("firstKeyOffset"),
                nbt.getBoolean("secondKeyIn"),
                nbt.getFloat("secondKeyOffset"),
                nbt.getInt("cooldown"));
        super.readNbt(nbt);
    }

    public ItemStack keyToRenderer(@Nullable World world, BlockPos pos) {
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            if (state.contains(KeyHandlerStoneBlock.HANDLER_TYPE)) {
                return switch (state.get(KeyHandlerStoneBlock.HANDLER_TYPE)) {
                    case GOLDEN -> ModItems.AURIC_KEY.getDefaultStack();
                    case DARK_CURRANTSLATE -> ModItems.ABYSSAL_KEY.getDefaultStack();
                    case GLACIEMITE -> ModItems.GRIPCRYSTAL_KEY.getDefaultStack();
                };
            }
        }
        return ModItems.ABYSSAL_KEY.getDefaultStack();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("firstKeyIn", this.data.firstKeyIn());
        nbt.putBoolean("secondKeyIn", this.data.secondKeyIn());
        nbt.putFloat("firstKeyOffset", this.data.firstKeyOffset());
        nbt.putFloat("secondKeyOffset", this.data.secondKeyOffset());
        nbt.putFloat("cooldown", this.data.cooldown());
        super.writeNbt(nbt);
    }

    private static ParticleEffect getTypeParticle(BlockState state) {
        return switch (state.get(KeyHandlerStoneBlock.HANDLER_TYPE)) {
            case GLACIEMITE ->
                    new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.DIAMOND_BLOCK.getDefaultState());
            case DARK_CURRANTSLATE -> ParticleTypes.CRIMSON_SPORE;
            case GOLDEN -> new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.GOLD_BLOCK.getDefaultState());
        };
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, KeyHandlerStoneBlockEntity entity) {
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

    public static void serverTick(World world, BlockPos pos, BlockState state, KeyHandlerStoneBlockEntity entity) {
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