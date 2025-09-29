
package net.sashakyotoz.unseenworld.world.features;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

import java.util.Set;

public class DripstoneOfAmethystOvergrowthClusterFeature extends RandomPatchFeature {
	private final Set<ResourceKey<Level>> generateDimensions = Set.of(ResourceKey.create(Registries.DIMENSION, new ResourceLocation("unseen_world:the_darkness")));

	public DripstoneOfAmethystOvergrowthClusterFeature() {
		super(RandomPatchConfiguration.CODEC);
	}

	public boolean place(FeaturePlaceContext<RandomPatchConfiguration> context) {
		WorldGenLevel world = context.level();
		if (!generateDimensions.contains(world.getLevel().dimension()))
			return false;
		return super.place(context);
	}
}
