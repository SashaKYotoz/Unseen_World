package net.sashakyotoz.unseenworld.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;

public class DeadCoralTreesFeature extends Feature<NoneFeatureConfiguration> {

    public DeadCoralTreesFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos blockpos = context.origin();
        RandomSource randomsource = context.random();
        WorldGenLevel worldgenlevel = context.level();
        if(worldgenlevel.getBlockState(blockpos.below()).isSolid()){
            int randomHeight = randomsource.nextInt(3,8);
            int randomRadius = randomsource.nextInt(2,7);
            for (int y = 0; y < randomHeight; y++) {
                for (int x = -randomRadius; x < randomRadius; x++) {
                    for (int z = -randomRadius; z < randomRadius; z++) {
                        if(worldgenlevel.getBlockState(blockpos.offset(x,y,z)).is(UnseenWorldModBlocks.DARK_WATER.get())){
                            int tmpX =(int) Math.sqrt(Math.abs(x)) + (randomsource.nextInt(2)-2);
                            int tmpZ =(int) Math.sqrt(Math.abs(z))+ (randomsource.nextInt(2)-2);
                            worldgenlevel.setBlock(blockpos.offset(tmpX,y,tmpZ), randomCoral(randomsource).defaultBlockState(),3);
                        }
                    }
                }
            }
            return true;
        }else
            return false;
    }
    private Block randomCoral(RandomSource source){
        int randomBlock = source.nextInt(0,5);
        switch (randomBlock){
            default -> {
                return Blocks.DEAD_BRAIN_CORAL_BLOCK;
            }
            case 1 -> {
                return Blocks.DEAD_BUBBLE_CORAL_BLOCK;
            }
            case 2-> {
                return Blocks.DEAD_HORN_CORAL_BLOCK;
            }
            case 3-> {
                return Blocks.DEAD_TUBE_CORAL_BLOCK;
            }
            case 4-> {
                return UnseenWorldModBlocks.DARK_WATER.get();
            }
        }
    }
}
