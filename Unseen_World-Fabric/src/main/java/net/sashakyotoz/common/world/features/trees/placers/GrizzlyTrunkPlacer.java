package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiConsumer;

public class GrizzlyTrunkPlacer extends TrunkPlacer {
    public static final Codec<GrizzlyTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> trunkPlacerParts(instance)
            .and(Codec.intRange(0, 4).fieldOf("platform_height").forGetter(trunk -> trunk.platformHeight))
            .apply(instance, GrizzlyTrunkPlacer::new));
    private final int platformHeight;

    public GrizzlyTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight, int platformHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.platformHeight = platformHeight;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTreePlacerTypes.GRIZZLY_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> replacer, RandomSource random,
                                                 int height, BlockPos startPos, TreeConfiguration config) {
        StraightTrunkPlacer.setDirtAt(world, replacer, random, startPos.below(), config);

        height = this.getTreeHeight(random);

        List<FoliagePlacer.FoliageAttachment> list = new ArrayList<>();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int x = startPos.getX();
        int y = startPos.getY();
        int z = startPos.getZ();
        int dx, dz;
        int DX = 0, DZ = 0;
        Direction ultDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        OptionalInt optionalInt = OptionalInt.empty();
        int k = 0;
        for (int i = 0; i < platformHeight; i++) {
            this.placeLog(world, replacer, random, world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, startPos.north().relative(ultDirection, i)), config);
            this.placeLog(world, replacer, random, world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, startPos.east()), config);
            this.placeLog(world, replacer, random, world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, startPos.south().relative(ultDirection, i)), config);
            this.placeLog(world, replacer, random, world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, startPos.west()), config);
        }
        for (int dy = 0; dy < height; dy++) {
            int y1 = y + dy;

            if (dy > height * 0.4) {
                for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
                    dx = direction.getStepX();
                    dz = direction.getStepZ();
                    if (this.placeLog(world, replacer, random, mutableBlockPos.set(x + dx * k + DX, y1, z + dz * k + DZ), config)) {
                        optionalInt = OptionalInt.of(y1 + 1);
                    }
                }
                k++;
            } else if (dy <= height * 0.4) {
                if (this.placeLog(world, replacer, random, mutableBlockPos.set(x + DX, y1, z + DZ), config)) {
                    optionalInt = OptionalInt.of(y1 + 1);
                }
                if ((dy + 1) % 3 == 0) {
                    DX += ultDirection.getStepX();
                    DZ += ultDirection.getStepZ();
                }
            }

        }
        int k1 = k - 1;
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            if (optionalInt.isPresent()) {
                list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(x + DX + direction.getStepX() * k1, optionalInt.getAsInt() - 1, z + DZ + direction.getStepZ() * k1), 1, false));
            }
        }
        return list;
    }
}
