package net.sashakyotoz;

import net.fabricmc.api.ModInitializer;

import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.util.Identifier;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.UnseenWorldDataGenerator;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;
import net.sashakyotoz.common.items.ModItemGroups;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.ModWorldGeneration;
import net.sashakyotoz.common.world.features.ModFeatures;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnseenWorld implements ModInitializer {
	public static final String MOD_ID = "unseen_world";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CustomPortalBuilder.beginPortal()
				.frameBlock(ModBlocks.DARK_FROST_BRICKS)
				.lightWithItem(ModItems.ECLIPSE_KEYSTONE)
				.destDimID(makeID("chimeric_darkness"))
				.tintColor(0x000)
				.registerPortal();
		ModItems.register();
		ModItemGroups.register();
		ModBlocks.register();
		ModBlockEntities.register();

		TrackedDataHandlerRegistry.register(WarriorOfChimericDarkness.WARRIOR_POSE);

		ModEntities.register();
		ModFeatures.register();
		ModParticleTypes.register();

		UnseenWorldDataGenerator.registerBurnable();
		UnseenWorldDataGenerator.registerFuels();
		UnseenWorldDataGenerator.registerStripped();

		ModTreePlacerTypes.register();
		ModWorldGeneration.register();
	}
	public static Identifier makeID(String id) {
		return new Identifier(MOD_ID, id);
	}

	public static <T> T log(T message) {
		LOGGER.info(String.valueOf(message));
		return message;
	}
}