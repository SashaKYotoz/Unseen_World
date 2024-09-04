package net.sashakyotoz;

import net.fabricmc.api.ModInitializer;

import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.util.Identifier;
import net.sashakyotoz.common.UnseenWorldDataGenerator;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.items.ModItemGroups;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.ModWorldGeneration;
<<<<<<< Updated upstream
=======
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
		ModBlocks.register();
		ModItemGroups.register();
		UnseenWorldDataGenerator.registerBurnable();
		UnseenWorldDataGenerator.registerFuels();
		UnseenWorldDataGenerator.registerStripped();
=======
		ModItemGroups.register();
		ModBlocks.register();
		UnseenWorldDataGenerator.registerBurnable();
		UnseenWorldDataGenerator.registerFuels();
		UnseenWorldDataGenerator.registerStripped();
		ModTreePlacerTypes.register();
>>>>>>> Stashed changes
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