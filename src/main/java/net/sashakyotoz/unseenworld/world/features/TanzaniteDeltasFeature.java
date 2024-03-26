
package net.sashakyotoz.unseenworld.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.BlockBlobFeature;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;

import java.util.Set;

public class TanzaniteDeltasFeature extends Feature<NoneFeatureConfiguration> {
    private final Set<ResourceKey<Level>> generateDimensions = Set.of(ResourceKey.create(Registries.DIMENSION, new ResourceLocation("unseen_world:the_darkness")));

    public TanzaniteDeltasFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();
        RandomSource randomsource = context.random();
        if (!generateDimensions.contains(worldgenlevel.getLevel().dimension()) || !worldgenlevel.isEmptyBlock(blockpos))
            return false;
        worldgenlevel.setBlock(blockpos, UnseenWorldModBlocks.TANZASHROOM_LIGHT.get().defaultBlockState(), 2);
        for (int i = 0; i < 1500; ++i) {
            BlockPos blockpos1 = blockpos.offset(randomsource.nextInt(8) - randomsource.nextInt(8), -randomsource.nextInt(12), randomsource.nextInt(8) - randomsource.nextInt(8));
            if (worldgenlevel.getBlockState(blockpos1).isAir()) {
                int j = 0;
                for (Direction direction : Direction.values()) {
                    if (worldgenlevel.getBlockState(blockpos1.relative(direction)).is(UnseenWorldModBlocks.TANZASHROOM_LIGHT.get()))
                        ++j;
                    if (j > 1)
                        break;
                }
                if (j == 1) {
                    worldgenlevel.setBlock(blockpos1, UnseenWorldModBlocks.TANZASHROOM_LIGHT.get().defaultBlockState(), 2);
                }
            }
        }
        return true;
    }
}
