package net.sashakyotoz.unseenworld.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
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
            for (int y = -7; y < 7; y++) {
                for (int x = -7; x < 7; x++) {
                    for (int z = -7; z < 7; z++) {
                        BlockPos tmpPos = blockPos.offset(x,y,z);
                        if(ceilingLight(level,tmpPos)){
                            ceilingLightSetter(level,tmpPos);
                            break;
                        }else if(sideVerticalLight(level,tmpPos)){
                            sideVerticalLightSetter(level,tmpPos);
                            break;
                        }else if(sideVerticalLightEastWest(level,tmpPos)){
                            sideVerticalLightEastWestSetter(level,tmpPos);
                            break;
                        }
                    }
                }
            }
            return true;
        }
        else{
            return false;
        }
    }
    private void ceilingLightSetter(WorldGenLevel level,BlockPos blockPos){
        for (int x = -1; x < 1; x++) {
            for (int z = -1; z < 1; z++) {
                level.setBlock(blockPos.offset(x,0,z),Blocks.CALCITE.defaultBlockState(),2);
            }
        }
        level.setBlock(blockPos,UnseenWorldModBlocks.TANZASHROOM_LIGHT.get().defaultBlockState(),2);
    }
    private void sideVerticalLightEastWestSetter(WorldGenLevel level,BlockPos blockPos){
        for (int x = -1; x < 1; x++) {
            for (int y = -1; y < 1; y++) {
                level.setBlock(blockPos.offset(x,y,0),Blocks.CALCITE.defaultBlockState(),2);
            }
        }
        level.setBlock(blockPos,UnseenWorldModBlocks.GRIZZLY_LIGHT_BLOCK.get().defaultBlockState(),2);
    }
    private void sideVerticalLightSetter(WorldGenLevel level,BlockPos blockPos){
        for (int z = -1; z < 1; z++) {
            for (int y = -1; y < 1; y++) {
                level.setBlock(blockPos.offset(0,y,z),Blocks.CALCITE.defaultBlockState(),2);
            }
        }
        level.setBlock(blockPos,UnseenWorldModBlocks.TANZASHROOM_LIGHT.get().defaultBlockState(),2);
    }
    private boolean ceilingLight(WorldGenLevel level, BlockPos pos){
        return !level.getBlockState(pos.above()).isAir() && level.getBlockState(pos.below()).isAir();
    }
    private boolean sideVerticalLight(WorldGenLevel level,BlockPos pos){
        return (level.getBlockState(pos.north()).isAir() && !level.getBlockState(pos.south()).isAir())
                || (level.getBlockState(pos.south()).isAir() && !level.getBlockState(pos.north()).isAir());
    }
    private boolean sideVerticalLightEastWest(WorldGenLevel level,BlockPos pos){
        return (level.getBlockState(pos.west()).isAir() && !level.getBlockState(pos.east()).isAir())
                || (level.getBlockState(pos.east()).isAir() && !level.getBlockState(pos.west()).isAir());
    }
}
