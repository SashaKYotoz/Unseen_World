package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.material.Fluids;
import net.sashakyotoz.common.blocks.custom.plants.LeafDroppingLeaveBlock;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

public class BurlywoodFoliagePlacer extends FoliagePlacer {
    public static final Codec<BurlywoodFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            foliagePlacerParts(instance).apply(instance, BurlywoodFoliagePlacer::new)
    );

    public BurlywoodFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModTreePlacerTypes.BURLYWOOD_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(
            LevelSimulatedReader world,
            FoliagePlacer.FoliageSetter placer,
            RandomSource random,
            TreeConfiguration config,
            int trunkHeight,
            FoliagePlacer.FoliageAttachment treeNode,
            int foliageHeight,
            int radius,
            int offset
    ) {
        BlockPos center = treeNode.pos().above(offset);
        boolean giant = treeNode.doubleTrunk();

        if (giant) {
            this.placeLeavesRow(world, placer, random, config, center, radius, -1, giant);
            this.placeLeavesRow(world, placer, random, config, center, radius + 1, 0, giant);
            this.placeLeavesRow(world, placer, random, config, center, radius, 1, giant);
        } else {
            this.placeLeavesRow(world, placer, random, config, center, radius + 1, -1, giant);
            this.placeLeavesRow(world, placer, random, config, center, radius, 0, giant);
        }
    }

    @Override
    public void placeLeavesRow(
            LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, TreeConfiguration config, BlockPos centerPos, int radius, int y, boolean giantTrunk
    ) {
        int i = giantTrunk ? 1 : 0;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int j = -radius; j <= radius + i; j++) {
            for (int k = -radius; k <= radius + i; k++) {
                if (!this.shouldSkipLocationSigned(random, j, y, k, radius, giantTrunk)) {
                    mutable.setWithOffset(centerPos, j, y, k);
                    placeOvergrownFoliageBlock(world, placer, random, config, mutable);
                }
            }
        }
    }

    protected static void placeOvergrownFoliageBlock(LevelSimulatedReader world, FoliageSetter placer, RandomSource random, TreeConfiguration config, BlockPos pos) {
        if (TreeFeature.validTreePos(world, pos)) {
            BlockState blockState = config.foliageProvider.getState(random, pos);
            if (blockState.hasProperty(BlockStateProperties.WATERLOGGED))
                blockState = blockState.setValue(BlockStateProperties.WATERLOGGED, world.isFluidAtPosition(pos, fluidState -> fluidState.isSourceOfType(Fluids.WATER)));
            if (blockState.hasProperty(LeafDroppingLeaveBlock.OVERGROWN))
                blockState = blockState.setValue(LeafDroppingLeaveBlock.OVERGROWN, random.nextDouble() > 0.89f);
            placer.set(pos, blockState);
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return 2 + random.nextInt(2);
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        if (y == -1 && (dx == radius || dz == radius) && random.nextFloat() < 0.35f) {
            return true;
        } else {
            boolean bl = dx == radius && dz == radius;
            boolean bl2 = radius > 2;
            return bl2 ? bl || dx + dz > radius * 2 - 2 && random.nextFloat() < 0.5f : bl && random.nextFloat() < 0.5f;
        }
    }
}