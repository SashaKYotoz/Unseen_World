package net.sashakyotoz.unseenworld.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

public class TheDarknessCustomSkyProcedure {
	public static void execute(ResourceKey<Level> dimension) {
		if (dimension == null)
			return;
		if (dimension == (ResourceKey.create(Registries.DIMENSION, new ResourceLocation("unseen_world:the_darkness")))) {
		}
	}
}
