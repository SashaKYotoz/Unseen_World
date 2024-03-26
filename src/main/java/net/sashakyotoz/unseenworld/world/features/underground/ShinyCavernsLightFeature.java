package net.sashakyotoz.unseenworld.world.features.underground;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;

public class ShinyCavernsLightFeature extends Feature<NoneFeatureConfiguration> {
    public ShinyCavernsLightFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos blockPos = context.origin();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        if(level.getBiome(blockPos).is(new ResourceLocation(UnseenWorldMod.MODID,"shiny_caverns"))){
            int height = 5 + random.nextIntBetweenInclusive(5,17);
            for (int y = 0; y < height; y++) {
                Direction direction = Direction.getRandom(random);
                BlockState state;
                int modifier = 1;
                if(y % 2 == 0){
                    modifier = random.nextIntBetweenInclusive(-2,2);
                    if (random.nextBoolean())
                        state = Blocks.CALCITE.defaultBlockState();
                    else{
                        if (random.nextBoolean())
                            state = UnseenWorldModBlocks.TANZANITE_BLOCK.get().defaultBlockState();
                        else
                            state = UnseenWorldModBlocks.GRIZZLY_LIGHT_BLOCK.get().defaultBlockState();
                    }
                    int x = 0;
                    int z = 0;
                    x += direction.getStepX();
                    z += direction.getStepZ();
                    level.setBlock(blockPos.offset(x, y, z), state, 2);
                    level.setBlock(blockPos.offset(x*modifier, y, z*modifier), state, 2);
                    level.setBlock(blockPos.offset(x*modifier/2, y, z*modifier/2), state, 2);
                    level.setBlock(blockPos.offset(x, y-1, z), state, 2);
                    level.setBlock(blockPos.offset(x*modifier, y-1, z*modifier), state, 2);
                }
            }
            return true;
        }
        else
            return false;
    }
}
