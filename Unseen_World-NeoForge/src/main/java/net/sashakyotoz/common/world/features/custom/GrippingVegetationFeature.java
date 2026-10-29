package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.features.custom.configs.GrippingVegetationFeatureConfig;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class GrippingVegetationFeature extends Feature<GrippingVegetationFeatureConfig> {
    public static final Feature<GrippingVegetationFeatureConfig> INSTANCE = new GrippingVegetationFeature(GrippingVegetationFeatureConfig.CODEC);

    public GrippingVegetationFeature(Codec<GrippingVegetationFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<GrippingVegetationFeatureConfig> context) {
        WorldGenLevel structureWorldAccess = context.level();
        GrippingVegetationFeatureConfig config = context.config();
        RandomSource random = context.random();
        BlockPos blockPos = context.origin();
        Predicate<BlockState> predicate = state -> state.is(ModTags.Blocks.GRIPPING_STONE_CAN_REPLACE);
        int i = config.horizontalRadius().sample(random) + 1;
        int j = config.horizontalRadius().sample(random) + 1;
        Set<BlockPos> set = this.placeGroundAndGetPositions(structureWorldAccess, config, random, blockPos, predicate, i, j);
        this.generateVegetation(structureWorldAccess, config, random, set);
        return !set.isEmpty();
    }

    private Set<BlockPos> placeGroundAndGetPositions(
            WorldGenLevel world, GrippingVegetationFeatureConfig config, RandomSource random, BlockPos pos, Predicate<BlockState> replaceable, int radiusX, int radiusZ
    ) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        BlockPos.MutableBlockPos mutable2 = mutable.mutable();
        Direction direction = config.surface().getDirection();
        Direction direction2 = direction.getOpposite();
        Set<BlockPos> set = new HashSet();

        for (int i = -radiusX; i <= radiusX; i++) {
            boolean bl = i == -radiusX || i == radiusX;

            for (int j = -radiusZ; j <= radiusZ; j++) {
                boolean bl2 = j == -radiusZ || j == radiusZ;
                boolean bl3 = bl || bl2;
                boolean bl4 = bl && bl2;
                boolean bl5 = bl3 && !bl4;
                if (!bl4 && (!bl5 || config.extraEdgeColumnChance() != 0.0F && !(random.nextFloat() > config.extraEdgeColumnChance()))) {
                    mutable.setWithOffset(pos, i, 0, j);

                    for (int k = 0; world.isStateAtPosition(mutable, BlockBehaviour.BlockStateBase::isAir) && k < config.verticalRange(); k++) {
                        mutable.move(direction);
                    }

                    for (int var25 = 0; world.isStateAtPosition(mutable, state -> !state.isAir()) && var25 < config.verticalRange(); var25++) {
                        mutable.move(direction2);
                    }

                    mutable2.setWithOffset(mutable, config.surface().getDirection());
                    BlockState blockState = world.getBlockState(mutable2);
                    if (world.isEmptyBlock(mutable) && blockState.isFaceSturdy(world, mutable2, config.surface().getDirection().getOpposite())) {
                        int l = 1 + (config.extraBottomBlockChance() > 0.0F && random.nextFloat() < config.extraBottomBlockChance() ? 1 : 0);
                        BlockPos blockPos = mutable2.immutable();
                        boolean bl6 = this.placeGround(world, config, replaceable, random, mutable2, l);
                        if (bl6) {
                            set.add(blockPos);
                        }
                    }
                }
            }
        }

        return set;
    }

    private void generateVegetation(
            WorldGenLevel world,
            GrippingVegetationFeatureConfig config,
            RandomSource random,
            Set<BlockPos> positions
    ) {
        for (BlockPos blockPos : positions) {
            if (config.vegetationChance() > 0.0F && random.nextFloat() < config.vegetationChance())
                world.setBlock(blockPos.relative(config.surface().getDirection().getOpposite()),
                        ModBlocks.GRIPPING_SPIGELIA.defaultBlockState()
                                .setValue(BlockStateProperties.AGE_2,random.nextInt(2))
                                .setValue(BlockStateProperties.VERTICAL_DIRECTION, config.surface().getDirection().getOpposite()),
                        Block.UPDATE_ALL);
        }
    }

    protected boolean placeGround(
            WorldGenLevel world, GrippingVegetationFeatureConfig config, Predicate<BlockState> replaceable, RandomSource random, BlockPos.MutableBlockPos pos, int depth
    ) {
        for (int i = 0; i < depth; i++) {
            BlockState blockState = config.stoneState().getState(random, pos);
            BlockState blockState2 = world.getBlockState(pos);
            if (!blockState.is(blockState2.getBlock())) {
                if (!replaceable.test(blockState2)) {
                    return i != 0;
                }
                world.setBlock(pos, blockState, Block.UPDATE_CLIENTS);
                pos.move(config.surface().getDirection());
            }
        }
        return true;
    }
}