package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.sashakyotoz.common.blocks.custom.plants.LeafDroppingLeaveBlock;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

public class BurlywoodFoliagePlacer extends FoliagePlacer {
    public static final Codec<BurlywoodFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillFoliagePlacerFields(instance).apply(instance, BurlywoodFoliagePlacer::new)
    );

    public BurlywoodFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return ModTreePlacerTypes.BURLYWOOD_FOLIAGE_PLACER;
    }

    @Override
    protected void generate(
            TestableWorld world,
            FoliagePlacer.BlockPlacer placer,
            Random random,
            TreeFeatureConfig config,
            int trunkHeight,
            FoliagePlacer.TreeNode treeNode,
            int foliageHeight,
            int radius,
            int offset
    ) {
        BlockPos center = treeNode.getCenter().up(offset);
        boolean giant = treeNode.isGiantTrunk();

        if (giant) {
            this.generateSquare(world, placer, random, config, center, radius, -1, giant);
            this.generateSquare(world, placer, random, config, center, radius + 1, 0, giant);
            this.generateSquare(world, placer, random, config, center, radius, 1, giant);
        } else {
            this.generateSquare(world, placer, random, config, center, radius + 1, -1, giant);
            this.generateSquare(world, placer, random, config, center, radius, 0, giant);
        }
    }

    @Override
    public void generateSquare(
            TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, TreeFeatureConfig config, BlockPos centerPos, int radius, int y, boolean giantTrunk
    ) {
        int i = giantTrunk ? 1 : 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int j = -radius; j <= radius + i; j++) {
            for (int k = -radius; k <= radius + i; k++) {
                if (!this.isPositionInvalid(random, j, y, k, radius, giantTrunk)) {
                    mutable.set(centerPos, j, y, k);
                    placeOvergrownFoliageBlock(world, placer, random, config, mutable);
                }
            }
        }
    }

    protected static void placeOvergrownFoliageBlock(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, BlockPos pos) {
        if (TreeFeature.canReplace(world, pos)) {
            BlockState blockState = config.foliageProvider.get(random, pos);
            if (blockState.contains(Properties.WATERLOGGED))
                blockState = blockState.with(Properties.WATERLOGGED, world.testFluidState(pos, fluidState -> fluidState.isEqualAndStill(Fluids.WATER)));
            if (blockState.contains(LeafDroppingLeaveBlock.OVERGROWN))
                blockState = blockState.with(LeafDroppingLeaveBlock.OVERGROWN, random.nextDouble() > 0.89f);
            placer.placeBlock(pos, blockState);
        }
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return 2 + random.nextInt(2);
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        if (y == -1 && (dx == radius || dz == radius) && random.nextFloat() < 0.35f) {
            return true;
        } else {
            boolean bl = dx == radius && dz == radius;
            boolean bl2 = radius > 2;
            return bl2 ? bl || dx + dz > radius * 2 - 2 && random.nextFloat() < 0.5f : bl && random.nextFloat() < 0.5f;
        }
    }
}