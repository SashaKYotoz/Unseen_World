
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnseenWorldModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UnseenWorldMod.MODID);
	public static final RegistryObject<CreativeModeTab> UNSEEN_WORLD_TOOLS = REGISTRY.register("unseen_world_tools",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.unseen_world.unseen_world_tools")).icon(() -> new ItemStack(UnseenWorldModItems.VOID_INGOT_PICKAXE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(UnseenWorldModItems.DEEP_GEM_PICKAXE.get());
				tabData.accept(UnseenWorldModItems.DEEP_GEM_AXE.get());
				tabData.accept(UnseenWorldModItems.DEEP_GEM_SHOVEL.get());
				tabData.accept(UnseenWorldModItems.DEEP_GEM_HOE.get());
				tabData.accept(UnseenWorldModItems.VOID_INGOT_PICKAXE.get());
				tabData.accept(UnseenWorldModItems.VOID_INGOT_AXE.get());
				tabData.accept(UnseenWorldModItems.VOID_INGOT_SHOVEL.get());
				tabData.accept(UnseenWorldModItems.VOID_INGOT_HOE.get());
				tabData.accept(UnseenWorldModItems.RED_TITANIUM_PICKAXE.get());
				tabData.accept(UnseenWorldModItems.RED_TITANIUM_AXE.get());
				tabData.accept(UnseenWorldModItems.RED_TITANIUM_SHOVEL.get());
				tabData.accept(UnseenWorldModItems.RED_TITANIUM_HOE.get());
				tabData.accept(UnseenWorldModItems.NATURERIUM_PICKAXE.get());
				tabData.accept(UnseenWorldModItems.NATURERIUM_AXE.get());
				tabData.accept(UnseenWorldModItems.NATURERIUM_SHOVEL.get());
				tabData.accept(UnseenWorldModItems.NATURERIUM_HOE.get());
				tabData.accept(UnseenWorldModItems.UNSEEN_PICKAXE.get());
				tabData.accept(UnseenWorldModItems.UNSEEN_AXE.get());
				tabData.accept(UnseenWorldModItems.UNSEEN_SHOVEL.get());
				tabData.accept(UnseenWorldModItems.UNSEEN_HOE.get());
				tabData.accept(UnseenWorldModItems.VOID_UPGRADE_SMITHING_TEMPLATE.get());
				tabData.accept(UnseenWorldModItems.THE_DARKNESS.get());
				tabData.accept(UnseenWorldModItems.DARK_GOLEM_HEART.get());
				tabData.accept(UnseenWorldModItems.DARKPEARL.get());
				tabData.accept(UnseenWorldModItems.FIRE_PEARL.get());
				tabData.accept(UnseenWorldModItems.DEEP_GEM.get());
				tabData.accept(UnseenWorldModBlocks.DEEP_GEM_ORE.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DEEP_GEM_BLOCK.get().asItem());
				tabData.accept(UnseenWorldModItems.BLUE_VOID.get());
				tabData.accept(UnseenWorldModItems.VOID_INGOT_INGOT.get());
				tabData.accept(UnseenWorldModBlocks.VOID_INGOT_ORE.get().asItem());
				tabData.accept(UnseenWorldModBlocks.VOID_INGOT_BLOCK.get().asItem());
				tabData.accept(UnseenWorldModItems.RAW_RED_TITANIUM.get());
				tabData.accept(UnseenWorldModItems.RED_TITANIUM_INGOT.get());
				tabData.accept(UnseenWorldModBlocks.RED_TITANIUM_ORE.get().asItem());
				tabData.accept(UnseenWorldModBlocks.RED_TITANIUM_BLOCK.get().asItem());
				tabData.accept(UnseenWorldModItems.RAW_UNSEENIUM.get());
				tabData.accept(UnseenWorldModItems.UNSEEN_INGOT.get());
				tabData.accept(UnseenWorldModBlocks.UNSEEN_ORE.get().asItem());
				tabData.accept(UnseenWorldModBlocks.UNSEEN_BLOCK.get().asItem());
				tabData.accept(UnseenWorldModItems.NATURERIUM_INGOT.get());
				tabData.accept(UnseenWorldModBlocks.NATURERIUM_BLOCK.get().asItem());
				tabData.accept(UnseenWorldModItems.RED_BLAZE_ROD.get());
				tabData.accept(UnseenWorldModItems.BLAZE_SHIELD_SHARD.get());
				tabData.accept(UnseenWorldModItems.TANZANITE_SHARD.get());
				tabData.accept(UnseenWorldModItems.CHLORITE_SLATE_STONE_SHARD.get());
				tabData.accept(UnseenWorldModItems.TEALIVE_STONY_SHARD.get());
				tabData.accept(UnseenWorldModItems.DARK_FREE_SOUL.get());
			}).withSearchBar().build());
	public static final RegistryObject<CreativeModeTab> UNSEEN_WORLD_BLOCKS = REGISTRY.register("unseen_world_blocks",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.unseen_world.unseen_world_blocks")).icon(() -> new ItemStack(UnseenWorldModBlocks.COLD_DARK_BRICKS.get())).displayItems((parameters, tabData) -> {
				tabData.accept(UnseenWorldModBlocks.COLD_DARK_BRICKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DARK_GRASS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get().asItem());
				tabData.accept(UnseenWorldModBlocks.RED_OOZE.get().asItem());
				tabData.accept(UnseenWorldModBlocks.CRYSTALLIZED_DARK_SAND.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_LEAVES.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_WOOD.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_LOG.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_PLANKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_STAIRS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_SLAB.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_TRAPDOOR.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_DOOR.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GRIZZLY_WOOD.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GRIZZLY_LOG.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GRIZZLY_PLANKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GRIZZLY_LEAVES.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GRIZZLY_STAIRS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GRIZZLY_SLAB.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GRIZZLY_TRAPDOOR.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GRIZZLY_DOOR.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GRIZZLY_LIGHT_BLOCK.get().asItem());
				tabData.accept(UnseenWorldModBlocks.AMETHYST_LEAVES.get().asItem());
				tabData.accept(UnseenWorldModBlocks.AMETHYST_WOOD.get().asItem());
				tabData.accept(UnseenWorldModBlocks.AMETHYST_LOG.get().asItem());
				tabData.accept(UnseenWorldModBlocks.AMETHYST_PLANKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.AMETHYST_STAIRS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.AMETHYST_SLAB.get().asItem());
				tabData.accept(UnseenWorldModBlocks.AMETHYST_TRAPDOOR.get().asItem());
				tabData.accept(UnseenWorldModBlocks.AMETHYST_DOOR.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TEALIVY_LEAVES.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TEALIVY_WOOD.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TEALIVY_LOG.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TEALIVY_PLANKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TEALIVY_STAIRS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TEALIVY_SLAB.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TEALIVY_TRAPDOOR.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TEALIVY_DOOR.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_LEAVES.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_WOOD.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_LOG.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_PLANKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_STAIRS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_SLAB.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_TRAPDOOR.get().asItem());
				tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_DOOR.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TANZASHROOM_LIGHT.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TANZANITE_BLOCK.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TANZANITE_BLOCK_BUDDING.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TANZANITE_CLUSTER.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TANZANITE_BRICKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TANZANITE_BRICKS_STAIRS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TANZANITE_BRICKS_SLAB.get().asItem());
				tabData.accept(UnseenWorldModBlocks.TANZANITE_BRICKS_WALL.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DAKNESS_ANCIENT_BRICKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DAKNESS_ANCIENT_BRICKS_STAIRS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DAKNESS_ANCIENT_BRICKS_SLAB.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DAKNESS_ANCIENT_BRICKS_WALL.get().asItem());
				tabData.accept(UnseenWorldModBlocks.REDDEEPSLATEBRICKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.REDDEEPSLATEBRICKSSLAB.get().asItem());
				tabData.accept(UnseenWorldModBlocks.REDDEEPSLATEBRICKSSTAIRS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.REDDEEPSLATEBRICKS_FENCE.get().asItem());
				tabData.accept(UnseenWorldModBlocks.CHLORITE_SLATE_STONE.get().asItem());
				tabData.accept(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS_STAIRS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS_SLAB.get().asItem());
				tabData.accept(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS_WALL.get().asItem());
				tabData.accept(UnseenWorldModBlocks.REINFORCED_POLISHED_BLACKSTONE_BRICKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.REINFORCED_POLISHED_BLACKSTONE_BRICKS_SLAB.get().asItem());
				tabData.accept(UnseenWorldModBlocks.ANCIENT_TRANSIENT_BLOCK_CLOSE.get().asItem());
				tabData.accept(UnseenWorldModBlocks.REINFORCED_RED_ANIENT_BRICKS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.BEACON_RUNE.get().asItem());
				tabData.accept(UnseenWorldModBlocks.BEACON_OF_WEAPONS.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DRIPSTONE_OF_AMETHYST_OVERGROWTH.get().asItem());
				tabData.accept(UnseenWorldModBlocks.DEEP_WATER_ANFELTSIA.get().asItem());
			}).withSearchBar().build());
	public static final RegistryObject<CreativeModeTab> UNSEEN_WORLD_COMBAT = REGISTRY.register("unseen_world_combat",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.unseen_world.unseen_world_combat")).icon(() -> new ItemStack(UnseenWorldModItems.RED_TITANIUM_SWORD.get())).displayItems((parameters, tabData) -> {
				tabData.accept(UnseenWorldModItems.DEEP_GEM_SWORD.get());
				tabData.accept(UnseenWorldModItems.VOID_INGOT_SWORD.get());
				tabData.accept(UnseenWorldModItems.RED_TITANIUM_SWORD.get());
				tabData.accept(UnseenWorldModItems.RED_TITANIUM_POISONOUS_SWORD.get());
				tabData.accept(UnseenWorldModItems.NATURERIUM_SWORD.get());
				tabData.accept(UnseenWorldModItems.UNSEEN_SWORD.get());
				tabData.accept(UnseenWorldModItems.VOID_ENDERMEN_SWORD.get());
				tabData.accept(UnseenWorldModItems.HEAVY_CLAYMORE.get());
				tabData.accept(UnseenWorldModItems.LIGHT_TULVAR.get());
				tabData.accept(UnseenWorldModItems.FIERY_SABER.get());
				tabData.accept(UnseenWorldModItems.VOID_HAMMER.get());
				tabData.accept(UnseenWorldModItems.REDNESS_HAMMER.get());
				tabData.accept(UnseenWorldModItems.AMETHYST_HAMMER.get());
				tabData.accept(UnseenWorldModItems.TEALIVY_VOID_SPEAR.get());
				tabData.accept(UnseenWorldModItems.UNSEEN_TITANIUM_SPEAR.get());
				tabData.accept(UnseenWorldModItems.NETHERIUM_STAFF.get());
				tabData.accept(UnseenWorldModItems.VOID_STAFF.get());
				tabData.accept(UnseenWorldModItems.TEALIVY_FIRE_STAFF.get());
				tabData.accept(UnseenWorldModItems.VOID_BOW.get());
				tabData.accept(UnseenWorldModItems.DEEP_GEM_ARMOR_HELMET.get());
				tabData.accept(UnseenWorldModItems.DEEP_GEM_ARMOR_CHESTPLATE.get());
				tabData.accept(UnseenWorldModItems.DEEP_GEM_ARMOR_LEGGINGS.get());
				tabData.accept(UnseenWorldModItems.DEEP_GEM_ARMOR_BOOTS.get());
				tabData.accept(UnseenWorldModItems.VOIDINGOT_ARMOR_HELMET.get());
				tabData.accept(UnseenWorldModItems.VOIDINGOT_ARMOR_CHESTPLATE.get());
				tabData.accept(UnseenWorldModItems.VOIDINGOT_ARMOR_LEGGINGS.get());
				tabData.accept(UnseenWorldModItems.VOIDINGOT_ARMOR_BOOTS.get());
				tabData.accept(UnseenWorldModItems.UNSEEN_ARMOR_HELMET.get());
				tabData.accept(UnseenWorldModItems.UNSEEN_ARMOR_CHESTPLATE.get());
				tabData.accept(UnseenWorldModItems.UNSEEN_ARMOR_LEGGINGS.get());
				tabData.accept(UnseenWorldModItems.UNSEEN_ARMOR_BOOTS.get());
				tabData.accept(UnseenWorldModItems.RED_TITANIUM_ARMOR_HELMET.get());
				tabData.accept(UnseenWorldModItems.RED_TITANIUM_ARMOR_CHESTPLATE.get());
				tabData.accept(UnseenWorldModItems.RED_TITANIUM_ARMOR_LEGGINGS.get());
				tabData.accept(UnseenWorldModItems.RED_TITANIUM_ARMOR_BOOTS.get());
				tabData.accept(UnseenWorldModItems.NATURERIUM_ARMOR_HELMET.get());
				tabData.accept(UnseenWorldModItems.NATURERIUM_ARMOR_CHESTPLATE.get());
				tabData.accept(UnseenWorldModItems.NATURERIUM_ARMOR_LEGGINGS.get());
				tabData.accept(UnseenWorldModItems.NATURERIUM_ARMOR_BOOTS.get());
				tabData.accept(UnseenWorldModItems.KNIGHT_ARMOR_HELMET.get());
				tabData.accept(UnseenWorldModItems.KNIGHT_ARMOR_CHESTPLATE.get());
				tabData.accept(UnseenWorldModItems.KNIGHT_ARMOR_LEGGINGS.get());
				tabData.accept(UnseenWorldModItems.KNIGHT_ARMOR_BOOTS.get());
				tabData.accept(UnseenWorldModItems.BLAZER_HELMET_HELMET.get());
				tabData.accept(UnseenWorldModItems.BLAZER_SHIELD.get());
				tabData.accept(UnseenWorldModItems.DARKSKELETON_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.AMETHYST_GOLEM_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.DARK_PHANTOM_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.DUSTY_PINK_MAXOR_FISH_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.MOONFISH_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.CAVERN_SCARECROW_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.SAVAGE_SMALL_BLAZE_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.CHIMERIC_PURPLEMARER_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.CHIMERIC_REDMARER_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.NETHERMEN_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.RED_SLYLF_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.RED_BLAZE_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.STREDER_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.GHAST_OF_TEALIVE_VALLEY_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.TANZANITE_GUARDIAN_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.DARKSPIRITWOLF_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.VOID_ENDERMEN_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.TEALIVE_SKELETON_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.RED_RAVENGER_SPAWN_EGG.get());
				tabData.accept(UnseenWorldModItems.DARK_HOGLIN_SPAWN_EGG.get());
			}).withSearchBar().build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
			tabData.accept(UnseenWorldModBlocks.GOLDENCHEST.get().asItem());
			tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_FENCE_GATE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_PRESSURE_PLATE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_BUTTON.get().asItem());
			tabData.accept(UnseenWorldModBlocks.GRIZZLY_FENCE_GATE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.GRIZZLY_PRESSURE_PLATE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.GRIZZLY_BUTTON.get().asItem());
			tabData.accept(UnseenWorldModBlocks.AMETHYST_FENCE_GATE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.AMETHYST_PRESSURE_PLATE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.AMETHYST_BUTTON.get().asItem());
			tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_FENCE_GATE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_PRESSURE_PLATE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_BUTTON.get().asItem());
			tabData.accept(UnseenWorldModBlocks.TEALIVY_FENCE_GATE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.TEALIVY_PRESSURE_PLATE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.TEALIVY_BUTTON.get().asItem());
		}

		if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			tabData.accept(UnseenWorldModItems.DARK_WATER_BUCKET.get());
			tabData.accept(UnseenWorldModItems.LIQUID_OF_CHIMERY_BUCKET.get());
			tabData.accept(UnseenWorldModItems.MOON_FISHIN_BUCKET.get());
			tabData.accept(UnseenWorldModItems.DUSTY_PINK_MAXOR_FISH_BUCKET.get());
		}

		if (tabData.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
			tabData.accept(UnseenWorldModBlocks.SMALL_CRIMSERRY_SOUL_BERRY.get().asItem());
			tabData.accept(UnseenWorldModBlocks.MISTERYFLOWER_SAPLING.get().asItem());
			tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_FENCE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.GRIZZLY_FENCE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.AMETHYST_FENCE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_FENCE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.TEALIVY_FENCE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.NIGHTDEW_OF_CHIMERIC_DARKNESS.get().asItem());
			tabData.accept(UnseenWorldModBlocks.OUT_GROWT_APPLE_BUSH.get().asItem());
			tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_FLOWING_AZALIA.get().asItem());
			tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_AZALEA.get().asItem());
			tabData.accept(UnseenWorldModBlocks.DARK_CRIMSON_VINE_FLOWER.get().asItem());
			tabData.accept(UnseenWorldModBlocks.LUMINOUSAMETHYSTVINE.get().asItem());
			tabData.accept(UnseenWorldModBlocks.DARKCRIMSONSAPLING.get().asItem());
			tabData.accept(UnseenWorldModBlocks.AMETHYST_TREE_SAPLING.get().asItem());
			tabData.accept(UnseenWorldModBlocks.TEALIVY_TREE_SAPLING.get().asItem());
			tabData.accept(UnseenWorldModBlocks.GREENISH_BURLYWOOD_SAPLING.get().asItem());
			tabData.accept(UnseenWorldModBlocks.GROWN_CRIMSERRY_SOUL_BERRY.get().asItem());
			tabData.accept(UnseenWorldModBlocks.AMETHYST_GRASS.get().asItem());
			tabData.accept(UnseenWorldModBlocks.TEALIVY_PLUMERIA.get().asItem());
			tabData.accept(UnseenWorldModBlocks.TEALIVY_JADE_VINE_FLOWER.get().asItem());
			tabData.accept(UnseenWorldModBlocks.TANZASHROOM.get().asItem());
			tabData.accept(UnseenWorldModBlocks.TANZASHROOM_BLOCK.get().asItem());
			tabData.accept(UnseenWorldModBlocks.TANZASHROOM_STEM.get().asItem());
		}

		if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			tabData.accept(UnseenWorldModItems.MUSICDISC_PIANO.get());
			tabData.accept(UnseenWorldModItems.MUSIC_DISC_HAPPY_PLACE.get());
		}

		if (tabData.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
			tabData.accept(UnseenWorldModItems.OUTGROWTHAPPLE.get());
			tabData.accept(UnseenWorldModItems.CHIMERIC_BLUE_PEPPER.get());
			tabData.accept(UnseenWorldModItems.PURPLE_BERRIES.get());
			tabData.accept(UnseenWorldModItems.LUMINOUSPORKCHOP.get());
			tabData.accept(UnseenWorldModItems.LUMINOUSCOOKEDPORKCHOP.get());
			tabData.accept(UnseenWorldModItems.BERRIESFROM_BLOOMING_VINE.get());
			tabData.accept(UnseenWorldModItems.CRIMSERRY_SOUL_BERRY_FOOD.get());
			tabData.accept(UnseenWorldModItems.DISHWITH_BERRIES.get());
			tabData.accept(UnseenWorldModItems.BOWLWITH_BERRIESWITHOUT_EFFECT.get());
			tabData.accept(UnseenWorldModItems.MOON_FISH_FOOD.get());
			tabData.accept(UnseenWorldModItems.COOKED_MOON_FISH.get());
			tabData.accept(UnseenWorldModItems.NIGHTDEW_NECTAR_BOTTLE.get());
			tabData.accept(UnseenWorldModItems.DUSTY_PINK_MAXOR_FISH_FOOD.get());
			tabData.accept(UnseenWorldModItems.COOKED_DUSTY_PINK_MAXOR_FISH.get());
			tabData.accept(UnseenWorldModItems.DISH_VEGETABLE_WITH_PORK.get());
		}
	}
}
