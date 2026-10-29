package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.plants.TreeBushLikeBlock;
import net.sashakyotoz.common.world.features.custom.configs.TreeBushLikeConfig;

public class TreeBushLikeFeature extends Feature<TreeBushLikeConfig> {
    public static final Feature<TreeBushLikeConfig> INSTANCE = new TreeBushLikeFeature(TreeBushLikeConfig.CODEC);

    public TreeBushLikeFeature(Codec<TreeBushLikeConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeBushLikeConfig> context) {
        TreeBushLikeConfig config = context.config();
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        int semiHeight = Math.round(config.height().getMaxValue() / 2f);
        int branches = config.extraBranches().sample(random);
        for (int k = 0; k < branches; k++) {
            pos.offset(k * 2, 0, 0);
            for (int i = 0; i < semiHeight; i++) {
                this.setBlock(world, pos.above(i),
                        ModBlocks.GLOW_APPLE_BUSH.defaultBlockState().setValue(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.BushTypes.STEM));
            }
            BlockPos pos1;
            BlockPos pos2;
            this.setBlock(world, pos.above(semiHeight), ModBlocks.GLOW_APPLE_BUSH.defaultBlockState()
                    .setValue(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.BushTypes.TURN_STEM)
                    .setValue(TreeBushLikeBlock.FACING, random.nextBoolean() ? Direction.NORTH : Direction.EAST));
            if (world.getBlockState(pos.above(semiHeight)).getValue(TreeBushLikeBlock.FACING).getOpposite() == Direction.SOUTH) {
                this.setBlock(world, pos.above(semiHeight).north(), ModBlocks.GLOW_APPLE_BUSH.defaultBlockState()
                        .setValue(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.BushTypes.CROOKED_STEM)
                        .setValue(TreeBushLikeBlock.FACING, Direction.SOUTH));
                this.setBlock(world, pos.above(semiHeight).south(), ModBlocks.GLOW_APPLE_BUSH.defaultBlockState()
                        .setValue(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.BushTypes.CROOKED_STEM)
                        .setValue(TreeBushLikeBlock.FACING, Direction.NORTH));
                pos1 = pos.above(semiHeight + 1).north();
                pos2 = pos.above(semiHeight + 1).south();
            } else {
                this.setBlock(world, pos.above(semiHeight).east(), ModBlocks.GLOW_APPLE_BUSH.defaultBlockState()
                        .setValue(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.BushTypes.CROOKED_STEM)
                        .setValue(TreeBushLikeBlock.FACING, Direction.WEST));
                this.setBlock(world, pos.above(semiHeight).west(), ModBlocks.GLOW_APPLE_BUSH.defaultBlockState()
                        .setValue(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.BushTypes.CROOKED_STEM)
                        .setValue(TreeBushLikeBlock.FACING, Direction.EAST));
                pos1 = pos.above(semiHeight + 1).east();
                pos2 = pos.above(semiHeight + 1).west();
            }
            int height = config.height().sample(random);
            for (int i = 0; i < height; i++) {
                this.setBlock(world, pos1.above(i), ModBlocks.GLOW_APPLE_BUSH.defaultBlockState()
                        .setValue(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.BushTypes.STEM));
                this.setBlock(world, pos2.above(i), ModBlocks.GLOW_APPLE_BUSH.defaultBlockState()
                        .setValue(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.BushTypes.STEM));
                if (i + 1 == height) {
                    world.blockUpdated(pos.above(semiHeight), world.getBlockState(pos.above(semiHeight - 1)).getBlock());
                    this.setBlock(world, pos1.above(i), ModBlocks.GLOW_APPLE_BUSH.defaultBlockState()
                            .setValue(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.BushTypes.BUSH));
                    this.setBlock(world, pos2.above(i), ModBlocks.GLOW_APPLE_BUSH.defaultBlockState()
                            .setValue(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.BushTypes.BUSH).setValue(TreeBushLikeBlock.GROWN, random.nextBoolean()));
                    if (random.nextInt(4) == 1)
                        this.place(new FeaturePlaceContext<>(context.topFeature(), world,
                                context.chunkGenerator(), random, pos1, config));
                }
            }
        }
        BlockPos semiHeightPos = pos.above(semiHeight);
        if (world.isStateAtPosition(semiHeightPos.north(), blockState -> blockState.is(ModBlocks.GLOW_APPLE_BUSH))
                && world.isStateAtPosition(semiHeightPos.south(), blockState -> blockState.is(ModBlocks.GLOW_APPLE_BUSH))
                && world.isStateAtPosition(semiHeightPos.west(), blockState -> blockState.is(ModBlocks.GLOW_APPLE_BUSH))
                && world.isStateAtPosition(semiHeightPos.east(), blockState -> blockState.is(ModBlocks.GLOW_APPLE_BUSH)))
            this.setBlock(world, pos.above(semiHeight), ModBlocks.GLOW_APPLE_BUSH.defaultBlockState()
                    .setValue(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.BushTypes.CROSSING_STEM)
                    .setValue(TreeBushLikeBlock.FACING, Direction.NORTH));
        return world.getBlockState(pos.below()).is(BlockTags.DIRT);
    }
}
