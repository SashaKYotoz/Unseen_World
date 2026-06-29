package net.sashakyotoz.common.blocks.custom.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;

public class TranslocatoneBlockEntity extends BlockEntity {
    public int ticks = 0;
    public BlockPos pos;

    public TranslocatoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRANSLOCATONE, pos, state);
        this.pos = pos;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, TranslocatoneBlockEntity entity) {
        entity.ticks++;
        if (entity.ticks % 2 == 0)
            entity.pos = entity.getPosOfHandler(world, pos);
        if (entity.ticks % 50 == 0 && world.getServer() != null && world.getServer().getCustomBossEvents() == null)
            world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.TRIGGERED, false));
    }

    public BlockPos getPosOfHandler(Level world, BlockPos pos) {
        int radius = 3;
        int height = 31;
        for (int y = -height; y < height; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos1 = pos.offset(x, y, z);
                    if (world.getBlockState(pos1).is(ModBlocks.KEY_HANDLER_STONE))
                        return pos1;
                }
            }
        }
        return pos;
    }
}