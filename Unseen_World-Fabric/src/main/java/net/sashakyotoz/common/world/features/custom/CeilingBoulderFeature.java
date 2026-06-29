package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.features.custom.configs.BoulderFeatureConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CeilingBoulderFeature extends Feature<BoulderFeatureConfig> {
    public static final Feature<BoulderFeatureConfig> INSTANCE = new CeilingBoulderFeature(BoulderFeatureConfig.CODEC);

    public CeilingBoulderFeature(Codec<BoulderFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BoulderFeatureConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BoulderFeatureConfig config = context.config();

        List<BlockPos> toPlace = new ArrayList<>();
        List<BlockPos> crystals = new ArrayList<>();

        int size = config.size().sample(random);
        Direction primary = Direction.from2DDataValue(random.nextInt(4));
        Direction secondary = random.nextBoolean() ? primary.getClockWise() : primary.getCounterClockWise();

        BlockPos pos = new BlockPos(origin);
        while (pos.getY() < 128 && !(world.getBlockState(pos.above()).canOcclude()
                && world.getBlockState(pos).is(Blocks.AIR))) {
            pos = pos.above();
        }
        if (pos.getY() >= 128) return false;

        this.spike(toPlace, pos, 1);
        this.spike(crystals, pos, 2);
        pos = this.move(pos, primary, secondary, random, world);
        for (int i = 0; i < size; i++) {
            this.spike(toPlace, pos, 2);
            this.spike(crystals, pos, 3);
            pos = this.move(pos, primary, secondary, random, world);
        }
        this.spike(toPlace, pos, 1);
        this.spike(crystals, pos, 2);
        BlockState state = getPlacementState((AmethystClusterBlock) Blocks.SMALL_AMETHYST_BUD, world, pos);
        if (config.block().getState(random, pos).is(Blocks.AMETHYST_BLOCK))
            state = getPlacementState((AmethystClusterBlock) Blocks.MEDIUM_AMETHYST_BUD, world, pos);
        if (config.block().getState(random, pos).is(ModTags.Blocks.GRIPPING_STONE_CAN_REPLACE))
            state = getPlacementState((AmethystClusterBlock) ModBlocks.GRIPCRYSTAL_WART, world, pos);
        for (BlockPos place : toPlace)
            this.safeSetBlock(world, place, config.block().getState(random, place),
                    block -> size > 0 && block.propagatesSkylightDown(world, place));
        for (BlockPos place : crystals) {
            if (state.getBlock() instanceof AmethystClusterBlock amethyst) {
                state = getPlacementState(amethyst, world, place);
                if (state.hasProperty(BlockStateProperties.FACING) && !amethyst.canSurvive(state, world, pos))
                    continue;
            }
            this.safeSetBlock(world, place, state,
                    block -> random.nextInt(5) == 0 && block.propagatesSkylightDown(world, place));
        }


        if (size == 0 && Math.random() < 0.7) {
            FeaturePlaceContext<BoulderFeatureConfig> contextnext =
                    new FeaturePlaceContext<>(context.topFeature(),
                            context.level(),
                            context.chunkGenerator(),
                            context.random(),
                            context.origin().east(random.nextIntBetweenInclusive(-5, 5)).north(random.nextIntBetweenInclusive(-5, 5)),
                            config);
            if (world.hasChunkAt(contextnext.origin()))
                this.place(contextnext);
        }

        return true;
    }

    private BlockState getPlacementState(AmethystClusterBlock block, LevelReader world, BlockPos pos) {
        List<Direction> possible = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (block.canSurvive(block.defaultBlockState().setValue(BlockStateProperties.FACING, dir), world, pos))
                possible.add(dir);
        }
        Collections.shuffle(possible);
        if (possible.isEmpty()) return Blocks.AIR.defaultBlockState();
        return block.defaultBlockState().setValue(BlockStateProperties.FACING, possible.get(0));
    }

    public BlockPos move(BlockPos pos, Direction primary, Direction secondary, RandomSource random, WorldGenLevel world) {
        pos = pos.relative(primary);

        if (random.nextInt(4) == 0) pos = pos.relative(primary.getClockWise());
        else if (random.nextInt(4) == 0) pos = pos.relative(primary.getCounterClockWise());
        else if (random.nextInt(4) == 0) pos = pos.relative(secondary);

        if (!world.getBlockState(pos).propagatesSkylightDown(world, pos))
            pos = pos.below();
        if (world.getBlockState(pos.above()).propagatesSkylightDown(world, pos))
            pos = pos.above();

        return pos;
    }

    public void spike(List<BlockPos> world, BlockPos pos, int layer) {
        if (layer <= 0) {
            if (!world.contains(pos)) world.add(pos);
            return;
        }

        for (Direction dir : Direction.values())
            this.spike(world, pos.relative(dir), layer - 1);
    }
}