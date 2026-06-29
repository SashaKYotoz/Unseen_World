package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

public class GrizzlyFoliagePlacer extends FoliagePlacer {
    public static final Codec<GrizzlyFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            foliagePlacerParts(instance).apply(instance, GrizzlyFoliagePlacer::new));

    public GrizzlyFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModTreePlacerTypes.GRIZZLY_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader world, FoliageSetter placer, RandomSource random, TreeConfiguration config, int trunkHeight,
                            FoliageAttachment treeNode, int foliageHeight, int radius, int offset) {

        BlockPos origin = treeNode.pos().below();
        if (!world.isStateAtPosition(origin.above(), state -> state.is(config.trunkProvider.getState(random,origin).getBlock())))
            radius = random.nextIntBetweenInclusive(3, 5);
        else radius = random.nextIntBetweenInclusive(2, 4);

        this.placeLeavesRow(world, placer, random, config, origin, radius-1, -1, treeNode.doubleTrunk());
        this.placeLeavesRow(world, placer, random, config, origin, radius, 0, treeNode.doubleTrunk());
        this.placeLeavesRow(world, placer, random, config, origin.above(), radius, 0, treeNode.doubleTrunk());
        this.placeLeavesRow(world, placer, random, config, origin, radius-1, 1, treeNode.doubleTrunk());
        this.placeLeavesRow(world, placer, random, config, origin, radius-2, 2, treeNode.doubleTrunk());
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return 1;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return dx*dx + dz*dz > radius*radius + y/2;
    }
}