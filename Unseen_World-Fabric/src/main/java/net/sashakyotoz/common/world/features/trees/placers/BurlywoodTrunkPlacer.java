package net.sashakyotoz.common.world.features.trees.placers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BurlywoodTrunkPlacer extends TrunkPlacer {
    public static final Codec<BurlywoodTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillTrunkPlacerFields(instance).apply(instance, BurlywoodTrunkPlacer::new));

    public BurlywoodTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return ModTreePlacerTypes.BURLYWOOD_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(
            TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config
    ) {
        List<FoliagePlacer.TreeNode> list = Lists.newArrayList();
        BlockPos blockPos = startPos.down();
        setToDirt(world, replacer, random, blockPos, config);
        setToDirt(world, replacer, random, blockPos.east(), config);
        setToDirt(world, replacer, random, blockPos.south(), config);
        setToDirt(world, replacer, random, blockPos.south().east(), config);
        Direction direction = Direction.Type.HORIZONTAL.random(random);
        int i = height - random.nextInt(4);
        int j = 2 - random.nextInt(3);
        int k = startPos.getX();
        int l = startPos.getY();
        int m = startPos.getZ();
        int n = k;
        int o = m;
        int p = l + height - 1;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        Function<BlockState, BlockState> function = state -> state.withIfExists(PillarBlock.AXIS, direction.getAxis());
        for (int q = 0; q < height; q++) {
            if (q + 3 > height && q % 2 == 0)
                list.add(this.generateBranch(world, replacer, random, height, random.nextBoolean() ? startPos.up() : startPos.up(3), config, function, direction.getOpposite(), j, j < l - 1, mutable));
            if (q >= i && j > 0) {
                n += direction.getOffsetX();
                o += direction.getOffsetZ();
                j--;
            }

            int r = l + q;
            BlockPos blockPos2 = new BlockPos(n, r, o);
            if (TreeFeature.isAirOrLeaves(world, blockPos2)) {
                this.getAndSetState(world, replacer, random, blockPos2, config);
                this.getAndSetState(world, replacer, random, blockPos2.east(), config);
                this.getAndSetState(world, replacer, random, blockPos2.south(), config);
                this.getAndSetState(world, replacer, random, blockPos2.east().south(), config);
            }
        }

        list.add(new FoliagePlacer.TreeNode(new BlockPos(n, p-1, o), 0, true));

        for (int q = -1; q <= 2; q++) {
            for (int r = -1; r <= 2; r++) {
                if ((q < 0 || q > 1 || r < 0 || r > 1) && random.nextInt(3) <= 0) {
                    int s = random.nextInt(3) + 2;

                    for (int t = 0; t < s; t++) {
                        this.getAndSetState(world, replacer, random, new BlockPos(k + q, p - t - 1, m + r), config);
                    }

                    list.add(new FoliagePlacer.TreeNode(new BlockPos(n + q, p, o + r), 0, false));
                }
            }
        }
        return list;
    }
    private FoliagePlacer.TreeNode generateBranch(
            TestableWorld world,
            BiConsumer<BlockPos, BlockState> replacer,
            Random random,
            int height,
            BlockPos startPos,
            TreeFeatureConfig config,
            Function<BlockState, BlockState> withAxisFunction,
            Direction direction,
            int branchStartOffset,
            boolean branchBelowHeight,
            BlockPos.Mutable mutablePos
    ) {
        mutablePos.set(startPos).move(Direction.UP, branchStartOffset);
        int i = height - 1 + random.nextBetweenExclusive(-1,2);
        boolean bl = branchBelowHeight || i < branchStartOffset;
        int j = 3 + (bl ? 1 : 0);
        BlockPos blockPos = startPos.offset(direction, j).up(i);
        int k = bl ? 4 : 1;

        for (int l = 0; l < k; l++) {
            if (random.nextInt(k-l) == 0)
                this.getAndSetState(world, replacer, random, mutablePos.move(direction), config, withAxisFunction);
        }

        Direction direction2 = blockPos.getY() > mutablePos.getY() ? Direction.UP : Direction.DOWN;

        while (true) {
            int m = mutablePos.getManhattanDistance(blockPos);
            if (m == 0)
                return new FoliagePlacer.TreeNode(blockPos.up(), 0, true);

            float f = (float)Math.abs(blockPos.getY() - mutablePos.getY()) / (float)m;
            boolean bl2 = random.nextFloat() < f;
            mutablePos.move(bl2 ? direction2 : direction);
            this.getAndSetState(world, replacer, random, mutablePos, config, bl2 ? Function.identity() : withAxisFunction);
        }
    }
}