package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiConsumer;

public class GrizzlyTrunkPlacer extends TrunkPlacer {
    public static final Codec<GrizzlyTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillTrunkPlacerFields(instance).apply(instance, GrizzlyTrunkPlacer::new));

    public GrizzlyTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return ModTreePlacerTypes.GRIZZLY_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random,
                                                 int height, BlockPos startPos, TreeFeatureConfig config) {
        StraightTrunkPlacer.setToDirt(world, replacer, random, startPos.down(), config);

        height = this.getHeight(random);

        List<FoliagePlacer.TreeNode> list = new ArrayList<>();
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
        int x = startPos.getX();
        int y = startPos.getY();
        int z = startPos.getZ();
        int dx, dz;
        int DX = 0, DZ = 0;
        Direction ultDirection = Direction.Type.HORIZONTAL.random(random);
        OptionalInt optionalInt = OptionalInt.empty();
        int k = 0;
        for (int dy = 0; dy < height; dy++) {
            int y1 = y + dy;

            if (dy > height * 0.4) {
                for (Direction direction : Direction.Type.HORIZONTAL.stream().toList()) {
                    dx = direction.getOffsetX();
                    dz = direction.getOffsetZ();
                    if (this.getAndSetState(world, replacer, random, mutableBlockPos.set(x + dx * k + DX, y1, z + dz * k + DZ), config)) {
                        optionalInt = OptionalInt.of(y1 + 1);
                    }
                }
                k++;
            } else if (dy <= height * 0.4) {
                if (this.getAndSetState(world, replacer, random, mutableBlockPos.set(x + DX, y1, z + DZ), config)) {
                    optionalInt = OptionalInt.of(y1 + 1);
                }
                if ((dy + 1) % 3 == 0) {
                    DX += ultDirection.getOffsetX();
                    DZ += ultDirection.getOffsetZ();
                }
            }

        }
        int k1 = k - 1;
        for (Direction direction : Direction.Type.HORIZONTAL.stream().toList()) {
            if (optionalInt.isPresent()) {
                list.add(new FoliagePlacer.TreeNode(new BlockPos(x + DX + direction.getOffsetX() * k1, optionalInt.getAsInt() - 1, z + DZ + direction.getOffsetZ() * k1), 1, false));
            }
        }
        return list;
    }
}
