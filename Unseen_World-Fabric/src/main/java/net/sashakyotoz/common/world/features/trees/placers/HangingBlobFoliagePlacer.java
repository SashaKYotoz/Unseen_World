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
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.material.Fluids;
import net.sashakyotoz.common.blocks.custom.plants.LeafDroppingLeaveBlock;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;
import org.jetbrains.annotations.NotNull;

public class HangingBlobFoliagePlacer extends BlobFoliagePlacer {
    public static final Codec<HangingBlobFoliagePlacer> CODEC = RecordCodecBuilder.create(
            instance -> blobParts(instance)
                    .and(Codec.floatRange(0F, 1F).fieldOf("hanging_leaves_chance").forGetter(foliagePlacer -> foliagePlacer.hangingLeavesChance))
                    .and(Codec.floatRange(0F, 1F).fieldOf("hanging_leaves_extension_chance").forGetter(foliagePlacer -> foliagePlacer.hangingLeavesExtensionChance))
                    .apply(instance, HangingBlobFoliagePlacer::new)
    );
    private final float hangingLeavesChance;
    private final float hangingLeavesExtensionChance;

    public HangingBlobFoliagePlacer(
            IntProvider radius,
            IntProvider offset,
            int height,
            float hangingLeavesChance,
            float hangingLeavesExtensionChance
    ) {
        super(radius, offset, height);
        this.hangingLeavesChance = hangingLeavesChance;
        this.hangingLeavesExtensionChance = hangingLeavesExtensionChance;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return ModTreePlacerTypes.HANGING_BLOB_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(
            LevelSimulatedReader world,
            FoliageSetter placer,
            RandomSource random,
            TreeConfiguration config,
            int trunkHeight,
            FoliageAttachment treeNode,
            int j,
            int k,
            int l
    ) {
        for (int yOffset = l; yOffset >= l - j; yOffset--) {
            int n = k + treeNode.radiusOffset() - 1 - yOffset;
            if (yOffset <= l - j) {
                this.placeLeavesRowWithHangingLeavesBelow(
                        world,
                        placer,
                        random,
                        config,
                        treeNode.pos(),
                        n,
                        yOffset,
                        treeNode.doubleTrunk(),
                        this.hangingLeavesChance,
                        this.hangingLeavesExtensionChance
                );
            } else {
                this.placeLeavesRow(
                        world,
                        placer,
                        random,
                        config,
                        treeNode.pos(),
                        n,
                        yOffset,
                        treeNode.doubleTrunk()
                );
            }
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
                blockState = blockState.setValue(LeafDroppingLeaveBlock.OVERGROWN, random.nextBoolean());
            placer.set(pos, blockState);
        }
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource, int x, int y, int z, int radius, boolean doubleTrunk) {
        boolean isCorner = x == radius && z == radius;
        if (y > 0) return isCorner && randomSource.nextBoolean();
        return isCorner;
    }
}