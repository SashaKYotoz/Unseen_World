package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.world.features.custom.configs.BoulderFeatureConfig;

import java.util.ArrayList;
import java.util.List;

public class BoulderFeature extends Feature<BoulderFeatureConfig> {
    public static final Feature<BoulderFeatureConfig> INSTANCE = new BoulderFeature(BoulderFeatureConfig.CODEC);

    public BoulderFeature(Codec<BoulderFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BoulderFeatureConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BoulderFeatureConfig config = context.config();

        List<BlockPos> toPlace = new ArrayList<>();

        BlockPos pos = origin.mutable();
        int size = config.size().sample(random);
        Direction primary = Direction.from2DDataValue(random.nextInt(4));
        Direction secondary = random.nextBoolean() ? primary.getClockWise() : primary.getCounterClockWise();

        this.spike(toPlace, pos, 1);
        pos = this.move(pos, primary, secondary, random, world);
        for (int i = 0; i < size; i++) {
            this.spike(toPlace, pos, 2);
            pos = this.move(pos, primary, secondary, random, world);
        }
        this.spike(toPlace, pos, 1);

        for (BlockPos place : toPlace)
            this.safeSetBlock(world, place, config.block().getState(random, place), block -> block.propagatesSkylightDown(world, place));

        return true;
    }

    public BlockPos move(BlockPos pos, Direction primary, Direction secondary, RandomSource random, WorldGenLevel world) {
        pos = pos.relative(primary);

        if (random.nextInt(4) == 0) pos = pos.relative(primary.getClockWise());
        else if (random.nextInt(4) == 0) pos = pos.relative(primary.getCounterClockWise());
        else if (random.nextInt(4) == 0) pos = pos.relative(secondary);

        if (!world.getBlockState(pos).propagatesSkylightDown(world, pos))
            pos = pos.above();
        if (world.getBlockState(pos.below()).propagatesSkylightDown(world, pos))
            pos = pos.below();

        return pos;
    }

    public void spike(List<BlockPos> world, BlockPos pos, int layer) {
        if (layer <= 0) {
            world.add(pos);
            world.add(pos.above());
            world.add(pos.below());
            return;
        }

        for (Direction dir : Direction.values())
            this.spike(world, pos.relative(dir), layer - 1);
    }
}