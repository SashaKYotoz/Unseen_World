package net.sashakyotoz.common.blocks.custom.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.GlaciemiteTranslocatoneBlock;

public class GlaciemiteTranslocatoneBlockEntity extends BlockEntity {
    public int ticks = 0;
    public BlockPos pos;

    public GlaciemiteTranslocatoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GLACIEMITE_TRANSLOCATONE, pos, state);
        this.pos = pos;
    }

    public static void tick(World world, BlockPos pos, BlockState state, GlaciemiteTranslocatoneBlockEntity entity) {
        entity.ticks++;
        if (entity.ticks % 2 == 0)
            entity.pos = entity.getPosOfHandler(world, pos);
        if (entity.ticks % 50 == 0 && world.getServer() != null && world.getServer().getBossBarManager() == null)
            world.setBlockState(pos, state.with(Properties.TRIGGERED, false));
    }

    public BlockPos getPosOfHandler(World world, BlockPos pos) {
        int radius = 3;
        int height = 31;
        for (int y = -height; y < height; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos1 = pos.add(x, y, z);
                    if (world.getBlockState(pos1).isOf(ModBlocks.KEY_HANDLER_STONE))
                        return pos1;
                }
            }
        }
        return pos;
    }
}