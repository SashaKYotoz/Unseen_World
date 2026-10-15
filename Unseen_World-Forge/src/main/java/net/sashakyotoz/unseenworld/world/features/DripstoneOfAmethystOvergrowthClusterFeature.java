
package net.sashakyotoz.unseenworld.world.features;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.sashakyotoz.unseenworld.UnseenWorld;

public class DripstoneOfAmethystOvergrowthClusterFeature extends RandomPatchFeature {
	public DripstoneOfAmethystOvergrowthClusterFeature() {
		super(RandomPatchConfiguration.CODEC);
	}

	public boolean place(FeaturePlaceContext<RandomPatchConfiguration> context) {
		WorldGenLevel level = context.level();
		if (!level.getLevel().dimension().equals(UnseenWorld.THE_DARKNESS))
			return false;
		return super.place(context);
	}
}
