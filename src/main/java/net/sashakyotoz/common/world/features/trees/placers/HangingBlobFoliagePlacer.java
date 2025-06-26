package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;
import org.jetbrains.annotations.NotNull;

public class HangingBlobFoliagePlacer extends BlobFoliagePlacer {
    public static final Codec<HangingBlobFoliagePlacer> CODEC = RecordCodecBuilder.create(
            instance -> createCodec(instance)
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
    protected @NotNull FoliagePlacerType<?> getType() {
        return ModTreePlacerTypes.HANGING_BLOB_FOLIAGE_PLACER;
    }

    @Override
    protected void generate(
            TestableWorld world,
            BlockPlacer placer,
            Random random,
            TreeFeatureConfig config,
            int trunkHeight,
            TreeNode treeNode,
            int j,
            int k,
            int l
    ) {
        for (int yOffset = l; yOffset >= l - j; yOffset--) {
            int n = k + treeNode.getFoliageRadius() - 1 - yOffset;
            if (yOffset <= l - j) {
                this.generateSquareWithHangingLeaves(
                        world,
                        placer,
                        random,
                        config,
                        treeNode.getCenter(),
                        n,
                        yOffset,
                        treeNode.isGiantTrunk(),
                        this.hangingLeavesChance,
                        this.hangingLeavesExtensionChance
                );
            } else {
                this.generateSquare(
                        world,
                        placer,
                        random,
                        config,
                        treeNode.getCenter(),
                        n,
                        yOffset,
                        treeNode.isGiantTrunk()
                );
            }
        }
    }

    @Override
    protected boolean isInvalidForLeaves(Random randomSource, int x, int y, int z, int radius, boolean doubleTrunk) {
        boolean isCorner = x == radius && z == radius;
        if (y > 0) return isCorner && randomSource.nextBoolean();
        return isCorner;
    }
}