package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.world.features.custom.configs.SpiralSpikesFeatureConfig;

public class SpiralSpikesFeature extends Feature<SpiralSpikesFeatureConfig> {
    public static final Feature<SpiralSpikesFeatureConfig> INSTANCE = new SpiralSpikesFeature(SpiralSpikesFeatureConfig.CODEC);

    public SpiralSpikesFeature(Codec<SpiralSpikesFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<SpiralSpikesFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        SpiralSpikesFeatureConfig config = context.getConfig();
        int baseHeight = config.size().get(random) + random.nextInt(config.size().get(random) + 3) + 2;
        int radius = config.size().get(random) - 2;
        BlockState baseBlock = config.state().get(random, origin);
        for (int y = 0; y < baseHeight; y++) {
            int currentRadius = Math.max(radius - y / 2, 2);
            double angle = y * (random.nextInt(config.size().get(random)) + 1);
            int offsetX = (int) (Math.cos(angle) * 2);
            int offsetZ = (int) (Math.sin(angle) * 2);
            BlockPos layerCenter = origin.add(offsetX, y, offsetZ);
            if (y < 2) {
                for (int i = 0; i > -5; i--) {
                    if (world.getBlockState(layerCenter.down(i + 1)).isAir() && random.nextInt(3) == 1)
                        this.setBlockState(world, layerCenter, baseBlock);
                }
            }
            for (int x = -currentRadius; x <= currentRadius; x++) {
                for (int z = -currentRadius; z <= currentRadius; z++) {
                    if (x * x + z * z <= currentRadius * currentRadius) {
                        BlockPos pos = layerCenter.add(x, 0, z);
                        if (world.getBlockState(pos).isAir() || !world.getBlockState(pos).isOpaque()) {
                            BlockState topBlock = config.state1().get(random, pos.up());
                            this.setBlockState(world, pos, random.nextInt(3) == 1 ? topBlock : baseBlock);
                            this.setBlockState(world, pos.up(), topBlock);
                            if (world.getBlockState(pos.down()).isAir() && world.getBlockState(pos.down(2)).isAir() && random.nextBoolean())
                                this.setBlockState(world, pos.down(), ModBlocks.GRIPCRYSTAL_WART.getDefaultState().with(Properties.FACING, Direction.DOWN));
                        }
                    }
                }
            }
        }
        BlockPos blockPos = origin.up(baseHeight);
        float f = (float) random.nextInt(3) + 3.0F;
        for (int i = 0; f > 0.5F; ++i) {
            for (int j = MathHelper.floor(-f); j <= MathHelper.ceil(f); ++j) {
                for (int k = MathHelper.floor(-f); k <= MathHelper.ceil(f); ++k) {
                    if ((float) (j * j + k * k) <= (f + 1.0F) * (f + 1.0F))
                        this.setBlockState(world, blockPos.add(j, i, k), baseBlock);
                }
            }
            f -= (float) random.nextInt(2) + 0.5F;
        }
        return true;
    }
}