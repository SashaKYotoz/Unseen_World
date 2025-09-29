package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
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
    public boolean generate(FeatureContext<BoulderFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        BoulderFeatureConfig config = context.getConfig();

        List<BlockPos> toPlace = new ArrayList<>();
        List<BlockPos> crystals = new ArrayList<>();

        int size = config.size().get(random);
        Direction primary = Direction.fromHorizontal(random.nextInt(4));
        Direction secondary = random.nextBoolean() ? primary.rotateYClockwise() : primary.rotateYCounterclockwise();

        BlockPos pos = new BlockPos(origin);
        while (pos.getY() < 128 && !(world.getBlockState(pos.up()).isOpaque()
                && world.getBlockState(pos).isOf(Blocks.AIR))) {
            pos = pos.up();
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
        if (config.block().get(random, pos).isOf(Blocks.AMETHYST_BLOCK))
            state = getPlacementState((AmethystClusterBlock) Blocks.MEDIUM_AMETHYST_BUD, world, pos);
        if (config.block().get(random, pos).isIn(ModTags.Blocks.GRIPPING_STONE_CAN_REPLACE))
            state = getPlacementState((AmethystClusterBlock) ModBlocks.GRIPCRYSTAL_WART, world, pos);
        for (BlockPos place : toPlace)
            this.setBlockStateIf(world, place, config.block().get(random, place),
                    block -> size > 0 && block.isTransparent(world, place));
        for (BlockPos place : crystals) {
            if (state.getBlock() instanceof AmethystClusterBlock amethyst) {
                state = getPlacementState(amethyst, world, place);
                if (state.contains(Properties.FACING) && !amethyst.canPlaceAt(state, world, pos))
                    continue;
            }
            this.setBlockStateIf(world, place, state,
                    block -> random.nextInt(5) == 0 && block.isTransparent(world, place));
        }


        if (size == 0 && Math.random() < 0.7) {
            FeatureContext<BoulderFeatureConfig> contextnext =
                    new FeatureContext<>(context.getFeature(),
                            context.getWorld(),
                            context.getGenerator(),
                            context.getRandom(),
                            context.getOrigin().east(random.nextBetween(-5, 5)).north(random.nextBetween(-5, 5)),
                            config);
            if (world.isChunkLoaded(contextnext.getOrigin()))
                this.generate(contextnext);
        }

        return true;
    }

    private BlockState getPlacementState(AmethystClusterBlock block, WorldView world, BlockPos pos) {
        List<Direction> possible = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (block.canPlaceAt(block.getDefaultState().with(Properties.FACING, dir), world, pos))
                possible.add(dir);
        }
        Collections.shuffle(possible);
        if (possible.isEmpty()) return Blocks.AIR.getDefaultState();
        return block.getDefaultState().with(Properties.FACING, possible.get(0));
    }

    public BlockPos move(BlockPos pos, Direction primary, Direction secondary, Random random, StructureWorldAccess world) {
        pos = pos.offset(primary);

        if (random.nextInt(4) == 0) pos = pos.offset(primary.rotateYClockwise());
        else if (random.nextInt(4) == 0) pos = pos.offset(primary.rotateYCounterclockwise());
        else if (random.nextInt(4) == 0) pos = pos.offset(secondary);

        if (!world.getBlockState(pos).isTransparent(world, pos))
            pos = pos.down();
        if (world.getBlockState(pos.up()).isTransparent(world, pos))
            pos = pos.up();

        return pos;
    }

    public void spike(List<BlockPos> world, BlockPos pos, int layer) {
        if (layer <= 0) {
            if (!world.contains(pos)) world.add(pos);
            return;
        }

        for (Direction dir : Direction.values())
            this.spike(world, pos.offset(dir), layer - 1);
    }
}