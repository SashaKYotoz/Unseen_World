package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BurlywoodFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<BurlywoodFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> foliagePlacerParts(instance)
            .and(Codec.floatRange(0.0F, 1.0F).fieldOf("skirt_chance").forGetter(placer -> placer.skirtChance))
            .apply(instance, BurlywoodFoliagePlacer::new));

    private static final int MAX_LEAF_REACH = 6;
    private static final float CROWN_EDGE_KEEP = 0.7F;
    private static final float BRIM_EDGE_KEEP = 0.85F;
    private static final double OVERGROWN_THRESHOLD = 0.89D;

    private final float skirtChance;

    public BurlywoodFoliagePlacer(IntProvider radius, IntProvider offset, float skirtChance) {
        super(radius, offset);
        this.skirtChance = skirtChance;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModTreePlacerTypes.BURLYWOOD_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        int radius = Math.max(foliageRadius + attachment.radiusOffset(), 1);
        BlockPos center = attachment.pos().above(offset);
        boolean giant = attachment.doubleTrunk();
        List<int[]> cells = new ArrayList<>();
        this.collectLayer(random, cells, 1, radius - 1, radius - 1.5F, CROWN_EDGE_KEEP, giant);
        this.collectLayer(random, cells, 0, radius, radius - 0.5F, BRIM_EDGE_KEEP, giant);
        this.collectLayer(random, cells, -1, radius, radius - 1.0F, this.skirtChance, giant);
        cells.sort(Comparator.comparingInt(cell -> cell[3]));

        Long2IntOpenHashMap distances = new Long2IntOpenHashMap();
        distances.defaultReturnValue(Integer.MAX_VALUE);
        int extra = giant ? 1 : 0;
        for (int x = 0; x <= extra; x++) {
            for (int z = 0; z <= extra; z++) {
                distances.put(BlockPos.asLong(x, -1, z), 0);
            }
        }
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int[] cell : cells) {
            int nearest = Integer.MAX_VALUE;
            for (Direction direction : Direction.values()) {
                nearest = Math.min(nearest, distances.get(BlockPos.asLong(cell[0] + direction.getStepX(), cell[1] + direction.getStepY(), cell[2] + direction.getStepZ())));
            }
            if (nearest >= MAX_LEAF_REACH) {
                continue;
            }
            cursor.setWithOffset(center, cell[0], cell[1], cell[2]);
            if (placeOvergrownFoliageBlock(level, foliageSetter, random, config, cursor)) {
                distances.put(BlockPos.asLong(cell[0], cell[1], cell[2]), nearest + 1);
            }
        }
    }

    private void collectLayer(RandomSource random, List<int[]> cells, int y, int radius, float innerRadius, float edgeKeepChance, boolean giant) {
        if (radius < 0) {
            return;
        }
        int extra = giant ? 1 : 0;
        float outerSq = (radius + 0.5F) * (radius + 0.5F);
        float innerSq = innerRadius < 0.0F ? -1.0F : innerRadius * innerRadius;
        boolean ringOnly = y < 0;
        for (int dx = -radius; dx <= radius + extra; dx++) {
            for (int dz = -radius; dz <= radius + extra; dz++) {
                int distanceX = this.axisDistance(dx, giant);
                int distanceZ = this.axisDistance(dz, giant);
                int distanceSq = distanceX * distanceX + distanceZ * distanceZ;
                if (distanceSq > outerSq) {
                    continue;
                }
                boolean edge = distanceSq > innerSq;
                if ((ringOnly && !edge) || (edge && random.nextFloat() >= edgeKeepChance)) {
                    continue;
                }
                cells.add(new int[]{dx, y, dz, distanceX + distanceZ + Math.abs(y + 1)});
            }
        }
    }

    private int axisDistance(int offset, boolean giant) {
        return giant && offset > 0 ? offset - 1 : Math.abs(offset);
    }

    protected static boolean placeOvergrownFoliageBlock(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, BlockPos pos) {
        if (!TreeFeature.validTreePos(level, pos)) {
            return false;
        }
        BlockState state = config.foliageProvider.getState(random, pos);
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            state = state.setValue(BlockStateProperties.WATERLOGGED, level.isFluidAtPosition(pos, fluid -> fluid.isSourceOfType(Fluids.WATER)));
        }
        if (state.hasProperty(LeafDroppingLeaveBlock.OVERGROWN)) {
            state = state.setValue(LeafDroppingLeaveBlock.OVERGROWN, random.nextDouble() > OVERGROWN_THRESHOLD);
        }
        foliageSetter.set(pos.immutable(), state);
        return true;
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return 2;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        return false;
    }
}