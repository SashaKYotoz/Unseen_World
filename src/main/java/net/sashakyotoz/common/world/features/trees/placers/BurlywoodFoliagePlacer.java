package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

public class BurlywoodFoliagePlacer extends FoliagePlacer {
    public static final Codec<BurlywoodFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillFoliagePlacerFields(instance).apply(instance, BurlywoodFoliagePlacer::new));

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
        BlockPos blockPos = treeNode.getCenter().up(offset);
        boolean bl = treeNode.isGiantTrunk();
        if (bl) {
            this.generateSquare(world, placer, random, config, blockPos, radius + 1, -1, bl);
            this.generateSquare(world, placer, random, config, blockPos, radius + 2, 0, bl);
            this.generateSquare(world, placer, random, config, blockPos, radius + 1, 1, bl);
            if (random.nextBoolean()) {
                this.generateSquare(world, placer, random, config, blockPos.down(-3), radius, 2, bl);
                this.generateSquare(world, placer, random, config, blockPos.down(-2), radius+1, 2, bl);
            }
        } else {
            this.generateSquare(world, placer, random, config, blockPos, radius + 2, -1, bl);
            this.generateSquare(world, placer, random, config, blockPos, radius + 1, 0, bl);
        }
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return 2;
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return dx*dx + dz*dz > radius*radius;
    }
}