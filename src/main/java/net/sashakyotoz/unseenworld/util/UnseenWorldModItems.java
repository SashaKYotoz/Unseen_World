package net.sashakyotoz.unseenworld.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.item.*;
import net.sashakyotoz.unseenworld.managers.DarkPearlRightClickedProcedure;

public class UnseenWorldModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, UnseenWorldMod.MODID);
	public static final RegistryObject<Item> MUSIC_DISC_PIANO = REGISTRY.register("musicdisc_piano", MusicdiscPianoItem::new);
	public static final RegistryObject<Item> MUSIC_DISC_HAPPY_PLACE = REGISTRY.register("music_disc_happy_place", MusicDiscHappyPlaceItem::new);
	public static final RegistryObject<Item> DARK_WATER_BUCKET = REGISTRY.register("dark_water_bucket", () -> new BucketItem(UnseenWorldModFluids.DARK_WATER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> LIQUID_OF_CHIMERY_BUCKET = REGISTRY.register("liquid_of_chimery_bucket", () -> new BucketItem(UnseenWorldModFluids.LIQUID_OF_CHIMERY, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MOON_FISHIN_BUCKET = REGISTRY.register("moon_fishin_bucket", () -> new MobBucketItem(UnseenWorldModEntities.MOONFISH,() -> Fluids.WATER,()-> SoundEvents.BUCKET_EMPTY_FISH,(new Item.Properties()).stacksTo(1)));
	public static final RegistryObject<Item> DUSTY_PINK_MAXOR_FISH_BUCKET = REGISTRY.register("dusty_pink_maxor_fish_bucket", () -> new MobBucketItem(UnseenWorldModEntities.DUSTY_PINK_MAXOR_FISH,() -> Fluids.WATER,()-> SoundEvents.BUCKET_EMPTY_FISH,(new Item.Properties()).stacksTo(1)));
	public static final RegistryObject<Item> DEEP_GEM_SWORD = REGISTRY.register("deep_gem_sword", () -> new DeepGemSwordItem());
	public static final RegistryObject<Item> VOID_INGOT_SWORD = REGISTRY.register("void_ingot_sword", () -> new VoidIngotSwordItem());
	public static final RegistryObject<Item> RED_TITANIUM_SWORD = REGISTRY.register("red_titanium_sword", () -> new RedTitaniumSwordItem());
	public static final RegistryObject<Item> RED_TITANIUM_POISONOUS_SWORD = REGISTRY.register("red_titanium_poisonous_sword", () -> new RedTitaniumPoisonousSwordItem());
	public static final RegistryObject<Item> NATURERIUM_SWORD = REGISTRY.register("naturerium_sword", () -> new NatureriumSwordItem());
	public static final RegistryObject<Item> UNSEEN_SWORD = REGISTRY.register("unseen_sword", () -> new UnseenSwordItem());
	public static final RegistryObject<Item> VOID_ENDERMEN_SWORD = REGISTRY.register("void_endermen_sword", () -> new VoidEndermenSwordItem());
	public static final RegistryObject<Item> HEAVY_CLAYMORE = REGISTRY.register("heavy_claymore", HeavyClaymoreItem::new);
	public static final RegistryObject<Item> LIGHT_TULVAR = REGISTRY.register("light_tulvar", LightTulvarItem::new);
	public static final RegistryObject<Item> BLASTING_LANCER = REGISTRY.register("blasting_lancer", BlastingLancer::new);
	public static final RegistryObject<Item> FIERY_SABER = REGISTRY.register("fiery_saber", FierySaberItem::new);
	public static final RegistryObject<Item> VOID_HAMMER = REGISTRY.register("void_hammer", () -> new SwordItem(new Tier() {
		public int getUses() {
			return 2500;
		}

		public float getSpeed() {
			return 10f;
		}

		public float getAttackDamageBonus() {
			return 12f;
		}

		public int getLevel() {
			return 5;
		}

		public int getEnchantmentValue() {
			return 18;
		}

		public Ingredient getRepairIngredient() {
			return Ingredient.of(new ItemStack(UnseenWorldModItems.VOID_INGOT.get()));
		}
	}, 3, -3.1f, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> REDNESS_HAMMER = REGISTRY.register("redness_hammer", () -> new SwordItem(new Tier() {
		public int getUses() {
			return 2750;
		}

		public float getSpeed() {
			return 8f;
		}

		public float getAttackDamageBonus() {
			return 13f;
		}

		public int getLevel() {
			return 5;
		}

		public int getEnchantmentValue() {
			return 20;
		}

		public Ingredient getRepairIngredient() {
			return Ingredient.of(new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get()));
		}
	}, 3, -3.1f, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> AMETHYST_HAMMER = REGISTRY.register("amethyst_hammer", () -> new SwordItem(new Tier() {
		public int getUses() {
			return 1750;
		}

		public float getSpeed() {
			return 10f;
		}

		public float getAttackDamageBonus() {
			return 11f;
		}

		public int getLevel() {
			return 5;
		}

		public int getEnchantmentValue() {
			return 18;
		}

		public Ingredient getRepairIngredient() {
			return Ingredient.of(new ItemStack(Items.AMETHYST_SHARD), new ItemStack(UnseenWorldModItems.TANZANITE_SHARD.get()));
		}
	}, 3, -3.1f, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> TEALIVY_VOID_SPEAR = REGISTRY.register("tealivy_void_spear", TealivyVoidSpearItem::new);
	public static final RegistryObject<Item> UNSEEN_TITANIUM_SPEAR = REGISTRY.register("unseen_titanium_spear", UnseenTitaniumSpearItem::new);
	public static final RegistryObject<Item> NETHERIUM_STAFF = REGISTRY.register("netherium_staff", NetheriumStaffItem::new);
	public static final RegistryObject<Item> VOID_STAFF = REGISTRY.register("void_staff", VoidStaffItem::new);
	public static final RegistryObject<Item> TANZANITE_STAFF = REGISTRY.register("tanzanite_staff", TanzaniteStaffItem::new);
	public static final RegistryObject<Item> TEALIVY_FIRE_STAFF = REGISTRY.register("tealivy_fire_staff", TealivyFireStaffItem::new);
	public static final RegistryObject<Item> VOID_BOW = REGISTRY.register("void_bow", VoidBowItem::new);
	public static final RegistryObject<Item> DEEP_GEM_ARMOR_HELMET = REGISTRY.register("deep_gem_armor_helmet", DeepGemArmorItem.Helmet::new);
	public static final RegistryObject<Item> DEEP_GEM_ARMOR_CHESTPLATE = REGISTRY.register("deep_gem_armor_chestplate", DeepGemArmorItem.Chestplate::new);
	public static final RegistryObject<Item> DEEP_GEM_ARMOR_LEGGINGS = REGISTRY.register("deep_gem_armor_leggings", DeepGemArmorItem.Leggings::new);
	public static final RegistryObject<Item> DEEP_GEM_ARMOR_BOOTS = REGISTRY.register("deep_gem_armor_boots", DeepGemArmorItem.Boots::new);
	public static final RegistryObject<Item> VOIDINGOT_ARMOR_HELMET = REGISTRY.register("voidingot_armor_helmet", VoidingotArmorItem.Helmet::new);
	public static final RegistryObject<Item> VOIDINGOT_ARMOR_CHESTPLATE = REGISTRY.register("voidingot_armor_chestplate", VoidingotArmorItem.Chestplate::new);
	public static final RegistryObject<Item> VOIDINGOT_ARMOR_LEGGINGS = REGISTRY.register("voidingot_armor_leggings", VoidingotArmorItem.Leggings::new);
	public static final RegistryObject<Item> VOIDINGOT_ARMOR_BOOTS = REGISTRY.register("voidingot_armor_boots", VoidingotArmorItem.Boots::new);
	public static final RegistryObject<Item> UNSEEN_ARMOR_HELMET = REGISTRY.register("unseen_armor_helmet", UnseenArmorItem.Helmet::new);
	public static final RegistryObject<Item> UNSEEN_ARMOR_CHESTPLATE = REGISTRY.register("unseen_armor_chestplate", UnseenArmorItem.Chestplate::new);
	public static final RegistryObject<Item> UNSEEN_ARMOR_LEGGINGS = REGISTRY.register("unseen_armor_leggings", UnseenArmorItem.Leggings::new);
	public static final RegistryObject<Item> UNSEEN_ARMOR_BOOTS = REGISTRY.register("unseen_armor_boots", UnseenArmorItem.Boots::new);
	public static final RegistryObject<Item> RED_TITANIUM_ARMOR_HELMET = REGISTRY.register("red_titanium_armor_helmet", RedTitaniumArmorItem.Helmet::new);
	public static final RegistryObject<Item> RED_TITANIUM_ARMOR_CHESTPLATE = REGISTRY.register("red_titanium_armor_chestplate", RedTitaniumArmorItem.Chestplate::new);
	public static final RegistryObject<Item> RED_TITANIUM_ARMOR_LEGGINGS = REGISTRY.register("red_titanium_armor_leggings", RedTitaniumArmorItem.Leggings::new);
	public static final RegistryObject<Item> RED_TITANIUM_ARMOR_BOOTS = REGISTRY.register("red_titanium_armor_boots", RedTitaniumArmorItem.Boots::new);
	public static final RegistryObject<Item> NATURERIUM_ARMOR_HELMET = REGISTRY.register("naturerium_armor_helmet", NatureriumArmorItem.Helmet::new);
	public static final RegistryObject<Item> NATURERIUM_ARMOR_CHESTPLATE = REGISTRY.register("naturerium_armor_chestplate", NatureriumArmorItem.Chestplate::new);
	public static final RegistryObject<Item> NATURERIUM_ARMOR_LEGGINGS = REGISTRY.register("naturerium_armor_leggings", NatureriumArmorItem.Leggings::new);
	public static final RegistryObject<Item> NATURERIUM_ARMOR_BOOTS = REGISTRY.register("naturerium_armor_boots", NatureriumArmorItem.Boots::new);
	public static final RegistryObject<Item> KNIGHT_ARMOR_HELMET = REGISTRY.register("knight_armor_helmet", KnightArmorItem.Helmet::new);
	public static final RegistryObject<Item> KNIGHT_ARMOR_CHESTPLATE = REGISTRY.register("knight_armor_chestplate", KnightArmorItem.Chestplate::new);
	public static final RegistryObject<Item> KNIGHT_ARMOR_LEGGINGS = REGISTRY.register("knight_armor_leggings", KnightArmorItem.Leggings::new);
	public static final RegistryObject<Item> KNIGHT_ARMOR_BOOTS = REGISTRY.register("knight_armor_boots", KnightArmorItem.Boots::new);
	public static final RegistryObject<Item> BLAZER_HELMET = REGISTRY.register("blazer_helmet", BlazerItem.Helmet::new);
	public static final RegistryObject<Item> BLAZER_SHIELD = REGISTRY.register("blazer_shield", () -> new ShieldItem(new Item.Properties().durability(1240).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> DARK_SKELETON_SPAWN_EGG = REGISTRY.register("dark_skeleton_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.DARK_SKELETON, -14935012, -3355393, new Item.Properties()));
	public static final RegistryObject<Item> AMETHYST_GOLEM_SPAWN_EGG = REGISTRY.register("amethyst_golem_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.AMETHYST_GOLEM, -10066330, -26113, new Item.Properties()));
	public static final RegistryObject<Item> DARK_PHANTOM_SPAWN_EGG = REGISTRY.register("dark_phantom_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.DARK_PHANTOM, -16777216, -1, new Item.Properties()));
	public static final RegistryObject<Item> DUSTY_PINK_MAXOR_FISH_SPAWN_EGG = REGISTRY.register("dusty_pink_maxor_fish_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.DUSTY_PINK_MAXOR_FISH, -26113, -13421773, new Item.Properties()));
	public static final RegistryObject<Item> MOONFISH_SPAWN_EGG = REGISTRY.register("moonfish_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.MOONFISH, -11770241, -3355393, new Item.Properties()));
	public static final RegistryObject<Item> CAVERN_SCARECROW_SPAWN_EGG = REGISTRY.register("cavern_scarecrow_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.CAVERN_SCARECROW, -8355712, -27648, new Item.Properties()));
	public static final RegistryObject<Item> SAVAGE_SMALL_BLAZE_SPAWN_EGG = REGISTRY.register("savage_small_blaze_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.SAVAGE_SMALL_BLAZE, -39424, -13434880, new Item.Properties()));
	public static final RegistryObject<Item> CHIMERIC_PURPLEMARER_SPAWN_EGG = REGISTRY.register("chimeric_purplemarer_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.CHIMERIC_PURPLEMARER, -13434829, -6750055, new Item.Properties()));
	public static final RegistryObject<Item> CHIMERIC_REDMARER_SPAWN_EGG = REGISTRY.register("chimeric_redmarer_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.CHIMERIC_REDMARER, -6750208, -6737152, new Item.Properties()));
	public static final RegistryObject<Item> NETHERMEN_SPAWN_EGG = REGISTRY.register("nethermen_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.NETHERMEN, -33024, -41153, new Item.Properties()));
	public static final RegistryObject<Item> RED_SLYLF_SPAWN_EGG = REGISTRY.register("red_slylf_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.RED_SLYLF, -6737152, -39373, new Item.Properties()));
	public static final RegistryObject<Item> RED_BLAZE_SPAWN_EGG = REGISTRY.register("red_blaze_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.RED_BLAZE, -11010048, -29184, new Item.Properties()));
	public static final RegistryObject<Item> STREDER_SPAWN_EGG = REGISTRY.register("streder_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.STREDER, -9671553, -13421773, new Item.Properties()));
	public static final RegistryObject<Item> GHAST_OF_TEALIVE_VALLEY_SPAWN_EGG = REGISTRY.register("ghast_of_tealive_valley_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.GHAST_OF_TEALIVE_VALLEY, -16724839, -16724788, new Item.Properties()));
	public static final RegistryObject<Item> TANZANITE_GUARDIAN_SPAWN_EGG = REGISTRY.register("tanzanite_guardian_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.TANZANITE_GUARDIAN, -10092442, -39220, new Item.Properties()));
	public static final RegistryObject<Item> DARK_SPIRIT_WOLF_SPAWN_EGG = REGISTRY.register("dark_spirit_wolf_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.DARKSPIRITWOLF, -13421773, -16724737, new Item.Properties()));
	public static final RegistryObject<Item> VOID_ENDERMEN_SPAWN_EGG = REGISTRY.register("void_endermen_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.VOID_ENDERMEN, -13877415, -9846858, new Item.Properties()));
	public static final RegistryObject<Item> TEALIVE_SKELETON_SPAWN_EGG = REGISTRY.register("tealive_skeleton_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.TEALIVE_SKELETON, -16764109, -16737946, new Item.Properties()));
	public static final RegistryObject<Item> RED_RAVENGER_SPAWN_EGG = REGISTRY.register("red_ravenger_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.RED_RAVENGER, -9297374, -49408, new Item.Properties()));
	public static final RegistryObject<Item> DARK_HOGLIN_SPAWN_EGG = REGISTRY.register("dark_hoglin_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.DARK_HOGLIN, -3355393, -16777216, new Item.Properties()));
	public static final RegistryObject<Item> SNOWDRIFTER_SPAWN_EGG = REGISTRY.register("snowdrifter_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.SNOWDRIFTER, 8496292, 14283506, new Item.Properties()));
	public static final RegistryObject<Item> DEEP_GEM_PICKAXE = REGISTRY.register("deep_gem_pickaxe", () -> new DeepGemPickaxeItem());
	public static final RegistryObject<Item> DEEP_GEM_AXE = REGISTRY.register("deep_gem_axe", () -> new DeepGemAxeItem());
	public static final RegistryObject<Item> DEEP_GEM_SHOVEL = REGISTRY.register("deep_gem_shovel", () -> new DeepGemShovelItem());
	public static final RegistryObject<Item> DEEP_GEM_HOE = REGISTRY.register("deep_gem_hoe", () -> new DeepGemHoeItem());
	public static final RegistryObject<Item> VOID_INGOT_PICKAXE = REGISTRY.register("void_ingot_pickaxe", () -> new VoidIngotPickaxeItem());
	public static final RegistryObject<Item> VOID_INGOT_AXE = REGISTRY.register("void_ingot_axe", () -> new VoidIngotAxeItem());
	public static final RegistryObject<Item> VOID_INGOT_SHOVEL = REGISTRY.register("void_ingot_shovel", () -> new VoidIngotShovelItem());
	public static final RegistryObject<Item> VOID_INGOT_HOE = REGISTRY.register("void_ingot_hoe", () -> new VoidIngotHoeItem());
	public static final RegistryObject<Item> RED_TITANIUM_PICKAXE = REGISTRY.register("red_titanium_pickaxe", () -> new RedTitaniumPickaxeItem());
	public static final RegistryObject<Item> RED_TITANIUM_AXE = REGISTRY.register("red_titanium_axe", () -> new RedTitaniumAxeItem());
	public static final RegistryObject<Item> RED_TITANIUM_SHOVEL = REGISTRY.register("red_titanium_shovel", () -> new RedTitaniumShovelItem());
	public static final RegistryObject<Item> RED_TITANIUM_HOE = REGISTRY.register("red_titanium_hoe", () -> new RedTitaniumHoeItem());
	public static final RegistryObject<Item> NATURERIUM_PICKAXE = REGISTRY.register("naturerium_pickaxe", () -> new NatureriumPickaxeItem());
	public static final RegistryObject<Item> NATURERIUM_AXE = REGISTRY.register("naturerium_axe", () -> new NatureriumAxeItem());
	public static final RegistryObject<Item> NATURERIUM_SHOVEL = REGISTRY.register("naturerium_shovel", () -> new NatureriumShovelItem());
	public static final RegistryObject<Item> NATURERIUM_HOE = REGISTRY.register("naturerium_hoe", () -> new NatureriumHoeItem());
	public static final RegistryObject<Item> UNSEEN_PICKAXE = REGISTRY.register("unseen_pickaxe", () -> new UnseenPickaxeItem());
	public static final RegistryObject<Item> UNSEEN_AXE = REGISTRY.register("unseen_axe", () -> new UnseenAxeItem());
	public static final RegistryObject<Item> UNSEEN_SHOVEL = REGISTRY.register("unseen_shovel", () -> new UnseenShovelItem());
	public static final RegistryObject<Item> UNSEEN_HOE = REGISTRY.register("unseen_hoe", () -> new UnseenHoeItem());
	public static final RegistryObject<Item> VOID_UPGRADE_SMITHING_TEMPLATE = REGISTRY.register("void_upgrade_smithing_template", VoidUpgradeSmithingTemplateItem::new);
	public static final RegistryObject<Item> THE_DARKNESS = REGISTRY.register("the_darkness", TheDarknessItem::new);
	public static final RegistryObject<Item> DARK_GOLEM_HEART = REGISTRY.register("dark_golem_heart", () -> new DarkGolemHeartItem());
	public static final RegistryObject<Item> DARKPEARL = REGISTRY.register("darkpearl", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)){
		@Override
		public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
			InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
			DarkPearlRightClickedProcedure.execute(entity);
			return ar;
		}
	});
	public static final RegistryObject<Item> FIRE_PEARL = REGISTRY.register("fire_pearl", () -> new Item(new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> DEEP_GEM = REGISTRY.register("deep_gem", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> SMALL_CRIMSERRY_SOUL_BERRY = block(UnseenWorldModBlocks.SMALL_CRIMSERRY_SOUL_BERRY);
	public static final RegistryObject<Item> MISTERYFLOWER_SAPLING = block(UnseenWorldModBlocks.MISTERYFLOWER_SAPLING);
	public static final RegistryObject<Item> GLOWORCHID = block(UnseenWorldModBlocks.GLOWORCHID);
	public static final RegistryObject<Item> DARK_CRIMSON_FENCE = block(UnseenWorldModBlocks.DARK_CRIMSON_FENCE);
	public static final RegistryObject<Item> GRIZZLY_FENCE = block(UnseenWorldModBlocks.GRIZZLY_FENCE);
	public static final RegistryObject<Item> AMETHYST_FENCE = block(UnseenWorldModBlocks.AMETHYST_FENCE);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_FENCE = block(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_FENCE);
	public static final RegistryObject<Item> TEALIVY_FENCE = block(UnseenWorldModBlocks.TEALIVY_FENCE);
	public static final RegistryObject<Item> NIGHTDEW_OF_CHIMERIC_DARKNESS = block(UnseenWorldModBlocks.NIGHTDEW_OF_CHIMERIC_DARKNESS);
	public static final RegistryObject<Item> OUT_GROWT_APPLE_BUSH = block(UnseenWorldModBlocks.OUT_GROWT_APPLE_BUSH);
	public static final RegistryObject<Item> DARK_CRIMSON_FLOWING_AZALIA = block(UnseenWorldModBlocks.DARK_CRIMSON_FLOWING_AZALIA);
	public static final RegistryObject<Item> DARK_CRIMSON_AZALEA = block(UnseenWorldModBlocks.DARK_CRIMSON_AZALEA);
	public static final RegistryObject<Item> DARK_CRIMSON_VINE_FLOWER = block(UnseenWorldModBlocks.DARK_CRIMSON_VINE_FLOWER);
	public static final RegistryObject<Item> LUMINOUSAMETHYSTVINE = block(UnseenWorldModBlocks.LUMINOUSAMETHYSTVINE);
	public static final RegistryObject<Item> DARKCRIMSONSAPLING = block(UnseenWorldModBlocks.DARKCRIMSONSAPLING);
	public static final RegistryObject<Item> AMETHYST_TREE_SAPLING = block(UnseenWorldModBlocks.AMETHYST_TREE_SAPLING);
	public static final RegistryObject<Item> TEALIVY_TREE_SAPLING = block(UnseenWorldModBlocks.TEALIVY_TREE_SAPLING);
	public static final RegistryObject<Item> GREENISH_BURLYWOOD_SAPLING = block(UnseenWorldModBlocks.GREENISH_BURLYWOOD_SAPLING);
	public static final RegistryObject<Item> GRIZZLY_SAPLING = block(UnseenWorldModBlocks.GRIZZLY_SAPLING);
	public static final RegistryObject<Item> GROWN_CRIMSERRY_SOUL_BERRY = block(UnseenWorldModBlocks.GROWN_CRIMSERRY_SOUL_BERRY);
	public static final RegistryObject<Item> AMETHYST_GRASS = block(UnseenWorldModBlocks.AMETHYST_GRASS);
	public static final RegistryObject<Item> TEALIVY_PLUMERIA = block(UnseenWorldModBlocks.TEALIVY_PLUMERIA);
	public static final RegistryObject<Item> TEALIVY_JADE_VINE_FLOWER = doubleBlock(UnseenWorldModBlocks.TEALIVY_JADE_VINE_FLOWER);
	public static final RegistryObject<Item> TANZASHROOM = block(UnseenWorldModBlocks.TANZASHROOM);
	public static final RegistryObject<Item> TANZASHROOM_BLOCK = block(UnseenWorldModBlocks.TANZASHROOM_BLOCK);
	public static final RegistryObject<Item> TANZASHROOM_STEM = block(UnseenWorldModBlocks.TANZASHROOM_STEM);
	public static final RegistryObject<Item> DARK_CURRANTSLATE = block(UnseenWorldModBlocks.DARK_CURRANTSLATE);
	public static final RegistryObject<Item> DARK_CURRANTSLATE_STAIRS = block(UnseenWorldModBlocks.DARK_CURRANTSLATE_STAIRS);
	public static final RegistryObject<Item> DARK_CURRANTSLATE_SLAB = block(UnseenWorldModBlocks.DARK_CURRANTSLATE_SLAB);
	public static final RegistryObject<Item> DARK_CURRANTSLATE_WALL = block(UnseenWorldModBlocks.DARK_CURRANTSLATE_WALL);
	public static final RegistryObject<Item> DEEP_GEM_ORE = block(UnseenWorldModBlocks.DEEP_GEM_ORE);
	public static final RegistryObject<Item> DEEP_GEM_BLOCK = block(UnseenWorldModBlocks.DEEP_GEM_BLOCK);
	public static final RegistryObject<Item> BLUE_VOID = REGISTRY.register("blue_void", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> VOID_INGOT = REGISTRY.register("void_ingot_ingot", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> VOID_INGOT_ORE = block(UnseenWorldModBlocks.VOID_INGOT_ORE);
	public static final RegistryObject<Item> VOID_INGOT_BLOCK = block(UnseenWorldModBlocks.VOID_INGOT_BLOCK);
	public static final RegistryObject<Item> RAW_RED_TITANIUM = REGISTRY.register("raw_red_titanium", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> RED_TITANIUM_INGOT = REGISTRY.register("red_titanium_ingot", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> RED_TITANIUM_ORE = block(UnseenWorldModBlocks.RED_TITANIUM_ORE);
	public static final RegistryObject<Item> RED_TITANIUM_BLOCK = block(UnseenWorldModBlocks.RED_TITANIUM_BLOCK);
	public static final RegistryObject<Item> RAW_UNSEENIUM = REGISTRY.register("raw_unseenium", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> UNSEEN_INGOT = REGISTRY.register("unseen_ingot", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> UNSEEN_ORE = block(UnseenWorldModBlocks.UNSEEN_ORE);
	public static final RegistryObject<Item> UNSEEN_BLOCK = block(UnseenWorldModBlocks.UNSEEN_BLOCK);
	public static final RegistryObject<Item> NATURERIUM_INGOT = REGISTRY.register("naturerium_ingot", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> NATURERIUM_BLOCK = block(UnseenWorldModBlocks.NATURERIUM_BLOCK);
	public static final RegistryObject<Item> RED_BLAZE_ROD = REGISTRY.register("red_blaze_rod", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BLAZE_SHIELD_SHARD = REGISTRY.register("blaze_shield_shard", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TANZANITE_SHARD = REGISTRY.register("tanzanite_shard", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> CHLORITE_SLATE_STONE_SHARD = REGISTRY.register("chlorite_slate_stone_shard", ()-> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TEALIVE_STONY_SHARD = REGISTRY.register("tealive_stony_shard", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> DARK_FREE_SOUL = REGISTRY.register("dark_free_soul", DarkFreeSoulItem::new);
	public static final RegistryObject<Item> COLD_DARK_BRICKS = block(UnseenWorldModBlocks.COLD_DARK_BRICKS);
	public static final RegistryObject<Item> GRASS_BLOCK_OF_SHINY_REDLIGHT = block(UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT);
	public static final RegistryObject<Item> DARK_GRASS = block(UnseenWorldModBlocks.DARK_GRASS);
	public static final RegistryObject<Item> AMETHYST_GRASS_BLOCK = block(UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK);
	public static final RegistryObject<Item> RED_OOZE = block(UnseenWorldModBlocks.RED_OOZE);
	public static final RegistryObject<Item> CRYSTALLIZED_DARK_SAND = block(UnseenWorldModBlocks.CRYSTALLIZED_DARK_SAND);
	public static final RegistryObject<Item> TEALIVE_LUMINOUS_GRASS_BLOCK = block(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK);
	public static final RegistryObject<Item> DARK_CRIMSON_LEAVES = block(UnseenWorldModBlocks.DARK_CRIMSON_LEAVES);
	public static final RegistryObject<Item> DARK_CRIMSON_WOOD = block(UnseenWorldModBlocks.DARK_CRIMSON_WOOD);
	public static final RegistryObject<Item> DARK_CRIMSON_LOG = block(UnseenWorldModBlocks.DARK_CRIMSON_LOG);
	public static final RegistryObject<Item> DARK_CRIMSON_PLANKS = block(UnseenWorldModBlocks.DARK_CRIMSON_PLANKS);
	public static final RegistryObject<Item> DARK_CRIMSON_STAIRS = block(UnseenWorldModBlocks.DARK_CRIMSON_STAIRS);
	public static final RegistryObject<Item> DARK_CRIMSON_SLAB = block(UnseenWorldModBlocks.DARK_CRIMSON_SLAB);
	public static final RegistryObject<Item> DARK_CRIMSON_TRAPDOOR = block(UnseenWorldModBlocks.DARK_CRIMSON_TRAPDOOR);
	public static final RegistryObject<Item> DARK_CRIMSON_DOOR = doubleBlock(UnseenWorldModBlocks.DARK_CRIMSON_DOOR);
	public static final RegistryObject<Item> GRIZZLY_WOOD = block(UnseenWorldModBlocks.GRIZZLY_WOOD);
	public static final RegistryObject<Item> GRIZZLY_LOG = block(UnseenWorldModBlocks.GRIZZLY_LOG);
	public static final RegistryObject<Item> GRIZZLY_PLANKS = block(UnseenWorldModBlocks.GRIZZLY_PLANKS);
	public static final RegistryObject<Item> GRIZZLY_LEAVES = block(UnseenWorldModBlocks.GRIZZLY_LEAVES);
	public static final RegistryObject<Item> GRIZZLY_STAIRS = block(UnseenWorldModBlocks.GRIZZLY_STAIRS);
	public static final RegistryObject<Item> GRIZZLY_SLAB = block(UnseenWorldModBlocks.GRIZZLY_SLAB);
	public static final RegistryObject<Item> GRIZZLY_TRAPDOOR = block(UnseenWorldModBlocks.GRIZZLY_TRAPDOOR);
	public static final RegistryObject<Item> GRIZZLY_DOOR = doubleBlock(UnseenWorldModBlocks.GRIZZLY_DOOR);
	public static final RegistryObject<Item> GRIZZLY_LIGHT_BLOCK = block(UnseenWorldModBlocks.GRIZZLY_LIGHT_BLOCK);
	public static final RegistryObject<Item> AMETHYST_LEAVES = block(UnseenWorldModBlocks.AMETHYST_LEAVES);
	public static final RegistryObject<Item> AMETHYST_WOOD = block(UnseenWorldModBlocks.AMETHYST_WOOD);
	public static final RegistryObject<Item> AMETHYST_LOG = block(UnseenWorldModBlocks.AMETHYST_LOG);
	public static final RegistryObject<Item> AMETHYST_PLANKS = block(UnseenWorldModBlocks.AMETHYST_PLANKS);
	public static final RegistryObject<Item> AMETHYST_STAIRS = block(UnseenWorldModBlocks.AMETHYST_STAIRS);
	public static final RegistryObject<Item> AMETHYST_SLAB = block(UnseenWorldModBlocks.AMETHYST_SLAB);
	public static final RegistryObject<Item> AMETHYST_TRAPDOOR = block(UnseenWorldModBlocks.AMETHYST_TRAPDOOR);
	public static final RegistryObject<Item> AMETHYST_DOOR = doubleBlock(UnseenWorldModBlocks.AMETHYST_DOOR);
	public static final RegistryObject<Item> TEALIVY_LEAVES = block(UnseenWorldModBlocks.TEALIVY_LEAVES);
	public static final RegistryObject<Item> TEALIVY_WOOD = block(UnseenWorldModBlocks.TEALIVY_WOOD);
	public static final RegistryObject<Item> TEALIVY_LOG = block(UnseenWorldModBlocks.TEALIVY_LOG);
	public static final RegistryObject<Item> TEALIVY_PLANKS = block(UnseenWorldModBlocks.TEALIVY_PLANKS);
	public static final RegistryObject<Item> TEALIVY_STAIRS = block(UnseenWorldModBlocks.TEALIVY_STAIRS);
	public static final RegistryObject<Item> TEALIVY_SLAB = block(UnseenWorldModBlocks.TEALIVY_SLAB);
	public static final RegistryObject<Item> TEALIVY_TRAPDOOR = block(UnseenWorldModBlocks.TEALIVY_TRAPDOOR);
	public static final RegistryObject<Item> TEALIVY_DOOR = doubleBlock(UnseenWorldModBlocks.TEALIVY_DOOR);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_LEAVES = block(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_LEAVES);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_WOOD = block(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_WOOD);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_LOG = block(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_LOG);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_PLANKS = block(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_PLANKS);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_STAIRS = block(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_STAIRS);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_SLAB = block(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_SLAB);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_TRAPDOOR = block(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_TRAPDOOR);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_DOOR = doubleBlock(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_DOOR);
	public static final RegistryObject<Item> TANZASHROOM_LIGHT = block(UnseenWorldModBlocks.TANZASHROOM_LIGHT);
	public static final RegistryObject<Item> TANZANITE_BLOCK = block(UnseenWorldModBlocks.TANZANITE_BLOCK);
	public static final RegistryObject<Item> TANZANITE_BLOCK_BUDDING = block(UnseenWorldModBlocks.TANZANITE_BLOCK_BUDDING);
	public static final RegistryObject<Item> TANZANITE_CLUSTER = block(UnseenWorldModBlocks.TANZANITE_CLUSTER);
	public static final RegistryObject<Item> TANZANITE_BRICKS = block(UnseenWorldModBlocks.TANZANITE_BRICKS);
	public static final RegistryObject<Item> TANZANITE_BRICKS_STAIRS = block(UnseenWorldModBlocks.TANZANITE_BRICKS_STAIRS);
	public static final RegistryObject<Item> TANZANITE_BRICKS_SLAB = block(UnseenWorldModBlocks.TANZANITE_BRICKS_SLAB);
	public static final RegistryObject<Item> TANZANITE_BRICKS_WALL = block(UnseenWorldModBlocks.TANZANITE_BRICKS_WALL);
	public static final RegistryObject<Item> DARKNESS_ANCIENT_BRICKS = block(UnseenWorldModBlocks.DARKNESS_ANCIENT_BRICKS);
	public static final RegistryObject<Item> DARKNESS_ANCIENT_BRICKS_STAIRS = block(UnseenWorldModBlocks.DARKNESS_ANCIENT_BRICKS_STAIRS);
	public static final RegistryObject<Item> DARKNESS_ANCIENT_BRICKS_SLAB = block(UnseenWorldModBlocks.DARKNESS_ANCIENT_BRICKS_SLAB);
	public static final RegistryObject<Item> DARKNESS_ANCIENT_BRICKS_WALL = block(UnseenWorldModBlocks.DARKNESS_ANCIENT_BRICKS_WALL);
	public static final RegistryObject<Item> REDDEEPSLATEBRICKS = block(UnseenWorldModBlocks.REDDEEPSLATEBRICKS);
	public static final RegistryObject<Item> REDDEEPSLATEBRICKSSLAB = block(UnseenWorldModBlocks.REDDEEPSLATEBRICKSSLAB);
	public static final RegistryObject<Item> REDDEEPSLATEBRICKSSTAIRS = block(UnseenWorldModBlocks.REDDEEPSLATEBRICKSSTAIRS);
	public static final RegistryObject<Item> REDDEEPSLATEBRICKS_FENCE = block(UnseenWorldModBlocks.REDDEEPSLATEBRICKS_FENCE);
	public static final RegistryObject<Item> CHLORITE_SLATE_STONE = block(UnseenWorldModBlocks.CHLORITE_SLATE_STONE);
	public static final RegistryObject<Item> CHLORITE_SLATE_STONE_BRICKS = block(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS);
	public static final RegistryObject<Item> CHLORITE_SLATE_STONE_BRICKS_STAIRS = block(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS_STAIRS);
	public static final RegistryObject<Item> CHLORITE_SLATE_STONE_BRICKS_SLAB = block(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS_SLAB);
	public static final RegistryObject<Item> CHLORITE_SLATE_STONE_BRICKS_WALL = block(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS_WALL);
	public static final RegistryObject<Item> REINFORCED_POLISHED_BLACKSTONE_BRICKS = block(UnseenWorldModBlocks.REINFORCED_POLISHED_BLACKSTONE_BRICKS);
	public static final RegistryObject<Item> REINFORCED_POLISHED_BLACKSTONE_BRICKS_SLAB = block(UnseenWorldModBlocks.REINFORCED_POLISHED_BLACKSTONE_BRICKS_SLAB);
	public static final RegistryObject<Item> ANCIENT_TRANSIENT_BLOCK_CLOSE = block(UnseenWorldModBlocks.ANCIENT_TRANSIENT_BLOCK_CLOSE);
	public static final RegistryObject<Item> REINFORCED_RED_ANIENT_BRICKS = block(UnseenWorldModBlocks.REINFORCED_RED_ANIENT_BRICKS);
	public static final RegistryObject<Item> BEACON_RUNE = block(UnseenWorldModBlocks.BEACON_RUNE);
	public static final RegistryObject<Item> BEACON_OF_WEAPONS = block(UnseenWorldModBlocks.BEACON_OF_WEAPONS);
	public static final RegistryObject<Item> DRIPSTONE_OF_AMETHYST_OVERGROWTH = block(UnseenWorldModBlocks.DRIPSTONE_OF_AMETHYST_OVERGROWTH);
	public static final RegistryObject<Item> DEEP_WATER_ANFELTSIA = block(UnseenWorldModBlocks.DEEP_WATER_ANFELTSIA);
	public static final RegistryObject<Item> GOLDENCHEST = block(UnseenWorldModBlocks.GOLDENCHEST);
	public static final RegistryObject<Item> DARK_CRIMSON_FENCE_GATE = block(UnseenWorldModBlocks.DARK_CRIMSON_FENCE_GATE);
	public static final RegistryObject<Item> DARK_CRIMSON_PRESSURE_PLATE = block(UnseenWorldModBlocks.DARK_CRIMSON_PRESSURE_PLATE);
	public static final RegistryObject<Item> DARK_CRIMSON_BUTTON = block(UnseenWorldModBlocks.DARK_CRIMSON_BUTTON);
	public static final RegistryObject<Item> GRIZZLY_FENCE_GATE = block(UnseenWorldModBlocks.GRIZZLY_FENCE_GATE);
	public static final RegistryObject<Item> GRIZZLY_PRESSURE_PLATE = block(UnseenWorldModBlocks.GRIZZLY_PRESSURE_PLATE);
	public static final RegistryObject<Item> GRIZZLY_BUTTON = block(UnseenWorldModBlocks.GRIZZLY_BUTTON);
	public static final RegistryObject<Item> AMETHYST_FENCE_GATE = block(UnseenWorldModBlocks.AMETHYST_FENCE_GATE);
	public static final RegistryObject<Item> AMETHYST_PRESSURE_PLATE = block(UnseenWorldModBlocks.AMETHYST_PRESSURE_PLATE);
	public static final RegistryObject<Item> AMETHYST_BUTTON = block(UnseenWorldModBlocks.AMETHYST_BUTTON);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_FENCE_GATE = block(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_FENCE_GATE);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_PRESSURE_PLATE = block(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_PRESSURE_PLATE);
	public static final RegistryObject<Item> GREENISH_BURLY_WOOD_BUTTON = block(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_BUTTON);
	public static final RegistryObject<Item> TEALIVY_FENCE_GATE = block(UnseenWorldModBlocks.TEALIVY_FENCE_GATE);
	public static final RegistryObject<Item> TEALIVY_PRESSURE_PLATE = block(UnseenWorldModBlocks.TEALIVY_PRESSURE_PLATE);
	public static final RegistryObject<Item> TEALIVY_BUTTON = block(UnseenWorldModBlocks.TEALIVY_BUTTON);
	public static final RegistryObject<Item> OUTGROWTHAPPLE = REGISTRY.register("outgrowthapple", () -> new OutgrowthappleItem());
	public static final RegistryObject<Item> CHIMERIC_BLUE_PEPPER = REGISTRY.register("chimeric_blue_pepper", () -> new ChimericBluePepperItem());
	public static final RegistryObject<Item> PURPLE_BERRIES = REGISTRY.register("purple_berries", () -> new PurpleBerriesItem());
	public static final RegistryObject<Item> LUMINOUS_PORKCHOP = REGISTRY.register("luminousporkchop", () -> new LuminousporkchopItem());
	public static final RegistryObject<Item> LUMINOUS_COOKED_PORKCHOP = REGISTRY.register("luminouscookedporkchop", () -> new LuminouscookedporkchopItem());
	public static final RegistryObject<Item> BERRIESFROM_BLOOMING_VINE = REGISTRY.register("berriesfrom_blooming_vine", () -> new ItemNameBlockItem(UnseenWorldModBlocks.DARK_CRIMSON_VINE_FLOWER.get(), new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(3).saturationMod(1.5f).build())){
		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 64;
		}
	});
	public static final RegistryObject<Item> CRIMSERRY_SOUL_BERRY_FOOD = REGISTRY.register("crimserry_soul_berry_food", () -> new CrimserrySoulBerryFoodItem());
	public static final RegistryObject<Item> DISHWITH_BERRIES = REGISTRY.register("dishwith_berries", DishwithBerriesItem::new);
	public static final RegistryObject<Item> BOWLWITH_BERRIESWITHOUT_EFFECT = REGISTRY.register("bowlwith_berrieswithout_effect", () -> new BowlWithBerriesWithoutEffectItem());
	public static final RegistryObject<Item> MOON_FISH_FOOD = REGISTRY.register("moon_fish_food", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(2).saturationMod(1f).build())){
		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 24;
		}
	});
	public static final RegistryObject<Item> COOKED_MOON_FISH = REGISTRY.register("cooked_moon_fish", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(3).saturationMod(2.5f).build())));
	public static final RegistryObject<Item> NIGHTDEW_NECTAR_BOTTLE = REGISTRY.register("nightdew_nectar_bottle", () -> new NightdewNectarBottleItem());
	public static final RegistryObject<Item> DUSTY_PINK_MAXOR_FISH_FOOD = REGISTRY.register("dusty_pink_maxor_fish_food", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.8f).build())){
		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 24;
		}
	});
	public static final RegistryObject<Item> COOKED_DUSTY_PINK_MAXOR_FISH = REGISTRY.register("cooked_dusty_pink_maxor_fish", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(3).saturationMod(3f).build())));
	public static final RegistryObject<Item> DISH_VEGETABLE_WITH_PORK = REGISTRY.register("dish_vegetable_with_pork", () -> new DishVegetableWithPorkItem());
	public static final RegistryObject<Item> BLAZER_SUMMON_BLOCK = block(UnseenWorldModBlocks.BLAZER_SUMMON_BLOCK);
	public static final RegistryObject<Item> DARK_CRIMSON_BLOOMING_VINE = block(UnseenWorldModBlocks.DARK_CRIMSON_BLOOMING_VINE);
	public static final RegistryObject<Item> THE_WITHER_KNIGHT_BLOCK = block(UnseenWorldModBlocks.THE_WITHER_KNIGHT_BLOCK);
	public static final RegistryObject<Item> MISTERYFLOWER_WITH_FEW_BERRIES = block(UnseenWorldModBlocks.MISTERYFLOWER_WITH_FEW_BERRIES);
	public static final RegistryObject<Item> MISTERYFLOWER_BERRIES = block(UnseenWorldModBlocks.MISTERYFLOWER_BERRIES);
	public static final RegistryObject<Item> ANCIENT_TRANSIENT_BLOCK_OPEN = block(UnseenWorldModBlocks.ANCIENT_TRANSIENT_BLOCK_OPEN);
	public static final RegistryObject<Item> UNDEAD_WARRIOR_OF_THE_CHIMERIC_DARKNESS = block(UnseenWorldModBlocks.UNDEAD_WARRIOR_OF_THE_CHIMERIC_DARKNESS);
	public static final RegistryObject<Item> TOTEMOF_GUDDY_BLAZE = block(UnseenWorldModBlocks.TOTEM_OF_GUDDY_BLAZE);

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}

	private static RegistryObject<Item> doubleBlock(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new DoubleHighBlockItem(block.get(), new Item.Properties()));
	}
}
