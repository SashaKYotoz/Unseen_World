package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.plants.TreeBushLikeBlock;
import net.sashakyotoz.common.world.features.custom.configs.TreeBushLikeConfig;

public class TreeBushLikeFeature extends Feature<TreeBushLikeConfig> {
    public static final Feature<TreeBushLikeConfig> INSTANCE = new TreeBushLikeFeature(TreeBushLikeConfig.CODEC);

    public TreeBushLikeFeature(Codec<TreeBushLikeConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<TreeBushLikeConfig> context) {
        TreeBushLikeConfig config = context.getConfig();
        BlockPos pos = context.getOrigin();
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        int semiHeight = Math.round(config.height().getMax() / 2f);
        int branches = config.extraBranches().get(random);
        for (int k = 0; k < branches; k++) {
            pos.add(k * 2, 0, 0);
            for (int i = 0; i < semiHeight; i++) {
                this.setBlockState(world, pos.up(i),
                        ModBlocks.GLOW_APPLE_BUSH.getDefaultState().with(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.TreeBushLikeTypes.STEM));
            }
            BlockPos pos1;
            BlockPos pos2;
            this.setBlockState(world, pos.up(semiHeight), ModBlocks.GLOW_APPLE_BUSH.getDefaultState()
                    .with(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.TreeBushLikeTypes.TURN_STEM)
                    .with(TreeBushLikeBlock.FACING, random.nextBoolean() ? Direction.NORTH : Direction.EAST));
            if (world.getBlockState(pos.up(semiHeight)).get(TreeBushLikeBlock.FACING).getOpposite() == Direction.SOUTH) {
                this.setBlockState(world, pos.up(semiHeight).north(), ModBlocks.GLOW_APPLE_BUSH.getDefaultState()
                        .with(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.TreeBushLikeTypes.CROOKED_STEM)
                        .with(TreeBushLikeBlock.FACING, Direction.SOUTH));
                this.setBlockState(world, pos.up(semiHeight).south(), ModBlocks.GLOW_APPLE_BUSH.getDefaultState()
                        .with(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.TreeBushLikeTypes.CROOKED_STEM)
                        .with(TreeBushLikeBlock.FACING, Direction.NORTH));
                pos1 = pos.up(semiHeight + 1).north();
                pos2 = pos.up(semiHeight + 1).south();
            } else {
                this.setBlockState(world, pos.up(semiHeight).east(), ModBlocks.GLOW_APPLE_BUSH.getDefaultState()
                        .with(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.TreeBushLikeTypes.CROOKED_STEM)
                        .with(TreeBushLikeBlock.FACING, Direction.WEST));
                this.setBlockState(world, pos.up(semiHeight).west(), ModBlocks.GLOW_APPLE_BUSH.getDefaultState()
                        .with(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.TreeBushLikeTypes.CROOKED_STEM)
                        .with(TreeBushLikeBlock.FACING, Direction.EAST));
                pos1 = pos.up(semiHeight + 1).east();
                pos2 = pos.up(semiHeight + 1).west();
            }
            int height = config.height().get(random);
            for (int i = 0; i < height; i++) {
                this.setBlockState(world, pos1.up(i), ModBlocks.GLOW_APPLE_BUSH.getDefaultState()
                        .with(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.TreeBushLikeTypes.STEM));
                this.setBlockState(world, pos2.up(i), ModBlocks.GLOW_APPLE_BUSH.getDefaultState()
                        .with(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.TreeBushLikeTypes.STEM));
                if (i + 1 == height) {
                    this.setBlockState(world, pos1.up(i), ModBlocks.GLOW_APPLE_BUSH.getDefaultState()
                            .with(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.TreeBushLikeTypes.BUSH));
                    this.setBlockState(world, pos2.up(i), ModBlocks.GLOW_APPLE_BUSH.getDefaultState()
                            .with(TreeBushLikeBlock.TYPE, TreeBushLikeBlock.TreeBushLikeTypes.BUSH));
                }
            }
        }
        return world.getBlockState(pos.down()).isIn(BlockTags.DIRT);
    }
}
