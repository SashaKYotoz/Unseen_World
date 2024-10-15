package net.sashakyotoz.common.blocks.custom.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sashakyotoz.common.blocks.ModBlockEntities;

public class KeyHandlerStoneBlockEntity extends BlockEntity {
    public int ticks = 0;
    public KeyHandlerStoneData data;

    public KeyHandlerStoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KEY_HANDLER, pos, state);
        this.data = state.get(Properties.LOCKED) ?
                new KeyHandlerStoneData(false, 0f, false, 0f) :
                new KeyHandlerStoneData(true, 0f, true, 0f);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
            this.data = new KeyHandlerStoneData(
                    nbt.getBoolean("firstKeyIn"),
                    nbt.getFloat("firstKeyOffset"),
                    nbt.getBoolean("secondKeyIn"),
                    nbt.getFloat("secondKeyOffset"));
        super.readNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("firstKeyIn", this.data.firstKeyIn());
        nbt.putBoolean("secondKeyIn", this.data.secondKeyIn());
        nbt.putFloat("firstKeyOffset", this.data.firstKeyOffset());
        nbt.putFloat("secondKeyOffset", this.data.secondKeyOffset());
        super.writeNbt(nbt);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, KeyHandlerStoneBlockEntity entity) {
        entity.ticks++;
        if (entity.data.firstKeyIn() && entity.data.secondKeyIn() && entity.ticks % 5 == 0)
            world.addParticle(
                    new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.DIAMOND_BLOCK.getDefaultState()),
                    pos.getX() +0.5,
                    pos.getY() + 1,
                    pos.getZ() +0.5,
                    0,
                    1,
                    0);
        if (entity.data.firstKeyOffset() > 0)
            entity.data = new KeyHandlerStoneData(
                    entity.data.firstKeyIn(),
                    entity.data.firstKeyOffset() - 0.1f,
                    entity.data.secondKeyIn(),
                    entity.data.secondKeyOffset());
        if (entity.data.secondKeyOffset() < 0)
            entity.data = new KeyHandlerStoneData(
                    entity.data.firstKeyIn(),
                    entity.data.firstKeyOffset(),
                    entity.data.secondKeyIn(),
                    entity.data.secondKeyOffset() + 0.1f);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, KeyHandlerStoneBlockEntity entity) {
        entity.ticks++;
        if (entity.data.firstKeyOffset() > 0)
            entity.data = new KeyHandlerStoneData(
                    entity.data.firstKeyIn(),
                    entity.data.firstKeyOffset() - 0.1f,
                    entity.data.secondKeyIn(),
                    entity.data.secondKeyOffset());
        if (entity.data.secondKeyOffset() < 0)
            entity.data = new KeyHandlerStoneData(
                    entity.data.firstKeyIn(),
                    entity.data.firstKeyOffset(),
                    entity.data.secondKeyIn(),
                    entity.data.secondKeyOffset() + 0.1f);
    }
}