package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
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
    public boolean generate(FeatureContext<GrippingVegetationFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        GrippingVegetationFeatureConfig config = context.getConfig();
        Random random = context.getRandom();
        BlockPos blockPos = context.getOrigin();
        Predicate<BlockState> predicate = state -> state.isIn(ModTags.Blocks.GRIPPING_STONE_CAN_REPLACE);
        int i = config.horizontalRadius().get(random) + 1;
        int j = config.horizontalRadius().get(random) + 1;
        Set<BlockPos> set = this.placeGroundAndGetPositions(structureWorldAccess, config, random, blockPos, predicate, i, j);
        this.generateVegetation(structureWorldAccess, config, random, set);
        return !set.isEmpty();
    }

    private Set<BlockPos> placeGroundAndGetPositions(
            StructureWorldAccess world, GrippingVegetationFeatureConfig config, Random random, BlockPos pos, Predicate<BlockState> replaceable, int radiusX, int radiusZ
    ) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        BlockPos.Mutable mutable2 = mutable.mutableCopy();
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
                    mutable.set(pos, i, 0, j);

                    for (int k = 0; world.testBlockState(mutable, AbstractBlock.AbstractBlockState::isAir) && k < config.verticalRange(); k++) {
                        mutable.move(direction);
                    }

                    for (int var25 = 0; world.testBlockState(mutable, state -> !state.isAir()) && var25 < config.verticalRange(); var25++) {
                        mutable.move(direction2);
                    }

                    mutable2.set(mutable, config.surface().getDirection());
                    BlockState blockState = world.getBlockState(mutable2);
                    if (world.isAir(mutable) && blockState.isSideSolidFullSquare(world, mutable2, config.surface().getDirection().getOpposite())) {
                        int l = 1 + (config.extraBottomBlockChance() > 0.0F && random.nextFloat() < config.extraBottomBlockChance() ? 1 : 0);
                        BlockPos blockPos = mutable2.toImmutable();
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
            StructureWorldAccess world,
            GrippingVegetationFeatureConfig config,
            Random random,
            Set<BlockPos> positions
    ) {
        for (BlockPos blockPos : positions) {
            if (config.vegetationChance() > 0.0F && random.nextFloat() < config.vegetationChance())
                world.setBlockState(blockPos.offset(config.surface().getDirection().getOpposite()),
                        ModBlocks.GRIPPING_SPIGELIA.getDefaultState()
                                .with(Properties.AGE_2,random.nextInt(2))
                                .with(Properties.VERTICAL_DIRECTION, config.surface().getDirection().getOpposite()),
                        Block.NOTIFY_ALL);
        }
    }

    protected boolean placeGround(
            StructureWorldAccess world, GrippingVegetationFeatureConfig config, Predicate<BlockState> replaceable, Random random, BlockPos.Mutable pos, int depth
    ) {
        for (int i = 0; i < depth; i++) {
            BlockState blockState = config.stoneState().get(random, pos);
            BlockState blockState2 = world.getBlockState(pos);
            if (!blockState.isOf(blockState2.getBlock())) {
                if (!replaceable.test(blockState2)) {
                    return i != 0;
                }
                world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
                pos.move(config.surface().getDirection());
            }
        }
        return true;
    }
}