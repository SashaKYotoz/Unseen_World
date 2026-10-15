
package net.sashakyotoz.unseenworld.world.features.underground;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.BlockBlobFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.sashakyotoz.unseenworld.UnseenWorld;

public class GrizzlyLightInCavernsFeature extends BlockBlobFeature {

	public GrizzlyLightInCavernsFeature() {
		super(BlockStateConfiguration.CODEC);
	}

	public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
		WorldGenLevel level = context.level();
		if (!level.getLevel().dimension().equals(UnseenWorld.THE_DARKNESS))
			return false;
		return super.place(context);
	}
}
