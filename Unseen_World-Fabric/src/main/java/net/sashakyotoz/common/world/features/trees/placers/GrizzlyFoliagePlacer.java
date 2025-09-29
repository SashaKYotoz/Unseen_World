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

public class GrizzlyFoliagePlacer extends FoliagePlacer {
    public static final Codec<GrizzlyFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillFoliagePlacerFields(instance).apply(instance, GrizzlyFoliagePlacer::new));

    public GrizzlyFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return ModTreePlacerTypes.GRIZZLY_FOLIAGE_PLACER;
    }

    @Override
    protected void generate(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, int trunkHeight,
                            TreeNode treeNode, int foliageHeight, int radius, int offset) {

        BlockPos origin = treeNode.getCenter().down();
        if (!world.testBlockState(origin.up(), state -> state.isOf(config.trunkProvider.get(random,origin).getBlock())))
            radius = random.nextBetween(3, 5);
        else radius = random.nextBetween(2, 4);

        this.generateSquare(world, placer, random, config, origin, radius-1, -1, treeNode.isGiantTrunk());
        this.generateSquare(world, placer, random, config, origin, radius, 0, treeNode.isGiantTrunk());
        this.generateSquare(world, placer, random, config, origin.up(), radius, 0, treeNode.isGiantTrunk());
        this.generateSquare(world, placer, random, config, origin, radius-1, 1, treeNode.isGiantTrunk());
        this.generateSquare(world, placer, random, config, origin, radius-2, 2, treeNode.isGiantTrunk());
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return 1;
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return dx*dx + dz*dz > radius*radius + y/2;
    }
}