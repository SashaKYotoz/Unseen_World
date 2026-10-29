package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sashakyotoz.common.blocks.ModBlocks;

public class ChimericFreezeFeature extends Feature<NoneFeatureConfiguration> {
    public static final ChimericFreezeFeature INSTANCE = new ChimericFreezeFeature(NoneFeatureConfiguration.CODEC);

    public ChimericFreezeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        BlockPos.MutableBlockPos top = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos below = new BlockPos.MutableBlockPos();
        boolean changed = false;
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int x = origin.getX() + dx;
                int z = origin.getZ() + dz;
                top.set(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z), z);
                below.setWithOffset(top, Direction.DOWN);
                Biome biome = level.getBiome(top).value();
                if (biome.warmEnoughToRain(below)) {
                    continue;
                }
                if (isDarkWaterSource(level.getBlockState(below))) {
                    level.setBlock(below, ModBlocks.RIME_ICE.defaultBlockState(), 2);
                    changed = true;
                } else if (biome.shouldFreeze(level, below, false)) {
                    level.setBlock(below, Blocks.ICE.defaultBlockState(), 2);
                    changed = true;
                }
                if (biome.shouldSnow(level, top)) {
                    level.setBlock(top, Blocks.SNOW.defaultBlockState(), 2);
                    BlockState ground = level.getBlockState(below);
                    if (ground.hasProperty(SnowyDirtBlock.SNOWY)) {
                        level.setBlock(below, ground.setValue(SnowyDirtBlock.SNOWY, true), 2);
                    }
                    changed = true;
                }
            }
        }
        return changed;
    }

    public static boolean isDarkWaterSource(BlockState state) {
        return state.is(ModBlocks.DARK_WATER) && state.getFluidState().isSource();
    }
}
