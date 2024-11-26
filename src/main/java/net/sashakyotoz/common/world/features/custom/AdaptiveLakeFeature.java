package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.sashakyotoz.common.world.features.custom.configs.AdaptiveLakeFeatureConfig;

public class AdaptiveLakeFeature extends Feature<AdaptiveLakeFeatureConfig> {
    public static final Feature<AdaptiveLakeFeatureConfig> INSTANCE = new AdaptiveLakeFeature(AdaptiveLakeFeatureConfig.CODEC);

    public AdaptiveLakeFeature(Codec<AdaptiveLakeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<AdaptiveLakeFeatureConfig> context) {
        StructureWorldAccess level = context.getWorld();
        BlockPos pos = context.getOrigin();
        Random random = context.getRandom();
        int x = context.getConfig().xRadius().get(random);
        int z = context.getConfig().zRadius().get(random);
        int y = context.getConfig().deepness();
        int sx, sy, sz;
        sx = -x;
        for (int i = 0; i < x * 2; i++) {
            sy = -y;
            for (int j = 0; j < y; j++) {
                sz = -z;
                for (int l = 0; l < z * 2; l++) {
                    BlockPos pos1 = pos.add(sx, sy, sz);
                    if (level.getBlockState(pos1).isOpaque()
                            && (level.getFluidState(pos.add(sx + 1, sy, sz)).isIn(FluidTags.WATER) || level.getBlockState(pos.add(sx + 1, sy, sz)).isOpaque())
                            && (level.getFluidState(pos.add(sx - 1, sy, sz)).isIn(FluidTags.WATER) || level.getBlockState(pos.add(sx - 1, sy, sz)).isOpaque())
                            && (level.getFluidState(pos.add(sx, sy, sz - 1)).isIn(FluidTags.WATER) || level.getBlockState(pos.add(sx, sy, sz - 1)).isOpaque())
                            && (level.getFluidState(pos.add(sx, sy, sz + 1)).isIn(FluidTags.WATER) || level.getBlockState(pos.add(sx, sy, sz + 1)).isOpaque())) {
                        BlockState waterState = context.getConfig().state().get(context.getRandom(), context.getOrigin());
                        level.setBlockState(pos1, waterState, 3);
                    }
                    sz = sz + MathHelper.nextInt(context.getRandom(), 1, 2);
                }
                sy = sy + 1;
            }
            sx = sx + 1;
        }
        return true;
    }
}