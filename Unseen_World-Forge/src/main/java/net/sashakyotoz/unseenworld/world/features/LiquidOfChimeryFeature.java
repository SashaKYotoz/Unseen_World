
package net.sashakyotoz.unseenworld.world.features;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.sashakyotoz.unseenworld.UnseenWorld;

public class LiquidOfChimeryFeature extends LakeFeature {
    public LiquidOfChimeryFeature() {
		super(LakeFeature.Configuration.CODEC);
	}

	public boolean place(FeaturePlaceContext<LakeFeature.Configuration> context) {
		WorldGenLevel level = context.level();
		if (!level.getLevel().dimension().equals(UnseenWorld.THE_DARKNESS))
			return false;
		return super.place(context);
	}
}
