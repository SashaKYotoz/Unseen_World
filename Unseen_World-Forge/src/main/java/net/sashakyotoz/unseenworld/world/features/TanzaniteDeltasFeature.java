
package net.sashakyotoz.unseenworld.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sashakyotoz.unseenworld.UnseenWorld;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;

public class TanzaniteDeltasFeature extends Feature<NoneFeatureConfiguration> {

    public TanzaniteDeltasFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos blockpos = context.origin();
        RandomSource randomsource = context.random();
        if (!level.getLevel().dimension().equals(UnseenWorld.THE_DARKNESS) || !level.isEmptyBlock(blockpos))
            return false;
        level.setBlock(blockpos, UnseenWorldBlocks.TANZASHROOM_LIGHT.get().defaultBlockState(), 2);
        for (int i = 0; i < 1500; ++i) {
            BlockPos blockpos1 = blockpos.offset(randomsource.nextInt(8) - randomsource.nextInt(8), -randomsource.nextInt(12), randomsource.nextInt(8) - randomsource.nextInt(8));
            if (level.getBlockState(blockpos1).isAir()) {
                int j = 0;
                for (Direction direction : Direction.values()) {
                    if (level.getBlockState(blockpos1.relative(direction)).is(UnseenWorldBlocks.TANZASHROOM_LIGHT.get()))
                        ++j;
                    if (j > 1)
                        break;
                }
                if (j == 1) {
                    level.setBlock(blockpos1, UnseenWorldBlocks.TANZASHROOM_LIGHT.get().defaultBlockState(), 2);
                }
            }
        }
        return true;
    }
}
