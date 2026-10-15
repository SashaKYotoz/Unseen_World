
package net.sashakyotoz.unseenworld.world.features;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.ReplaceBlobsFeature;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
import net.sashakyotoz.unseenworld.UnseenWorld;

public class DarkBeachOozeFeature extends ReplaceBlobsFeature {
    public DarkBeachOozeFeature() {
        super(ReplaceSphereConfiguration.CODEC);
    }

    public boolean place(FeaturePlaceContext<ReplaceSphereConfiguration> context) {
        WorldGenLevel level = context.level();
        if (!(level.getLevel().dimension()).equals(UnseenWorld.THE_DARKNESS))
            return false;
        return super.place(context);
    }
}
