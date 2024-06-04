package net.sashakyotoz.unseenworld.registries;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.item.*;
import net.sashakyotoz.unseenworld.managers.DarkPearlRightClickedProcedure;

public class UnseenWorldModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UnseenWorldMod.MODID);
	public static final RegistryObject<Item> MUSIC_DISC_PIANO = ITEMS.register("musicdisc_piano",()-> new RecordItem(3,()-> UnseenWorldModSounds.CHAOTIC,new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 600));
	public static final RegistryObject<Item> MUSIC_DISC_HAPPY_PLACE = ITEMS.register("music_disc_happy_place", ()-> new RecordItem(5,()->UnseenWorldModSounds.HAPPY_PLACE,new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 800));
	public static final RegistryObject<Item> DARK_WATER_BUCKET = ITEMS.register("dark_water_bucket", () -> new BucketItem(UnseenWorldModFluids.DARK_WATER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> LIQUID_OF_CHIMERY_BUCKET = ITEMS.register("liquid_of_chimery_bucket", () -> new BucketItem(UnseenWorldModFluids.LIQUID_OF_CHIMERY, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MOON_FISHIN_BUCKET = ITEMS.register("moon_fishin_bucket", () -> new MobBucketItem(UnseenWorldModEntities.MOONFISH,() -> Fluids.WATER,()-> SoundEvents.BUCKET_EMPTY_FISH,(new Item.Properties()).stacksTo(1)));
	public static final RegistryObject<Item> DUSTY_PINK_MAXOR_FISH_BUCKET = ITEMS.register("dusty_pink_maxor_fish_bucket", () -> new MobBucketItem(UnseenWorldModEntities.DUSTY_PINK_MAXOR_FISH,() -> Fluids.WATER,()-> SoundEvents.BUCKET_EMPTY_FISH,(new Item.Properties()).stacksTo(1)));
	public static final RegistryObject<Item> DEEP_GEM_SWORD = ITEMS.register("deep_gem_sword", () -> new SwordItem(ModTiers.DEEP_GEM,3, -2.4f, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> VOID_INGOT_SWORD = ITEMS.register("void_ingot_sword", () -> new SwordItem(ModTiers.VOID,3, -2.4f, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> RED_TITANIUM_SWORD = ITEMS.register("red_titanium_sword", () -> new SwordItem(ModTiers.TITANIUM,3, -2.4f, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> RED_TITANIUM_POISONOUS_SWORD = ITEMS.register("red_titanium_poisonous_sword", RedTitaniumPoisonousSwordItem::new);
	public static final RegistryObject<Item> NATURERIUM_SWORD = ITEMS.register("naturerium_sword", () -> new SwordItem(ModTiers.NATURERIUM,3, -2.4f, new Item.Properties()));
	public static final RegistryObject<Item> UNSEEN_SWORD = ITEMS.register("unseen_sword", () -> new SwordItem(ModTiers.UNSEENIUM,3, -2.4f, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> VOID_ENDERMEN_SWORD = ITEMS.register("void_endermen_sword", () -> new SwordItem(ModTiers.VOID,3, -2.4f, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> HEAVY_CLAYMORE = ITEMS.register("heavy_claymore", HeavyClaymoreItem::new);
	public static final RegistryObject<Item> LIGHT_TULVAR = ITEMS.register("light_tulvar", LightTulvarItem::new);
	public static final RegistryObject<Item> BLASTING_LANCER = ITEMS.register("blasting_lancer", BlastingLancer::new);
	public static final RegistryObject<Item> FIERY_SABER = ITEMS.register("fiery_saber", FierySaberItem::new);
	public static final RegistryObject<Item> VOID_HAMMER = ITEMS.register("void_hammer", () -> new SwordItem(ModTiers.VOID_HAMMER, 3, -3.1f, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> REDNESS_HAMMER = ITEMS.register("redness_hammer", () -> new SwordItem(ModTiers.REDNESS_HAMMER, 3, -3.1f, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> AMETHYST_HAMMER = ITEMS.register("amethyst_hammer", () -> new SwordItem(ModTiers.AMETHYST_HAMMER, 3, -3.1f, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> TEALIVY_VOID_SPEAR = ITEMS.register("tealivy_void_spear", TealivyVoidSpearItem::new);
	public static final RegistryObject<Item> UNSEEN_TITANIUM_SPEAR = ITEMS.register("unseen_titanium_spear", UnseenTitaniumSpearItem::new);
	public static final RegistryObject<Item> NETHERIUM_STAFF = ITEMS.register("netherium_staff", NetheriumStaffItem::new);
	public static final RegistryObject<Item> VOID_STAFF = ITEMS.register("void_staff", VoidStaffItem::new);
	public static final RegistryObject<Item> TANZANITE_STAFF = ITEMS.register("tanzanite_staff", TanzaniteStaffItem::new);
	public static final RegistryObject<Item> TEALIVY_FIRE_STAFF = ITEMS.register("tealivy_fire_staff", TealivyFireStaffItem::new);
	public static final RegistryObject<Item> VOID_BOW = ITEMS.register("void_bow", VoidBowItem::new);
	public static final RegistryObject<Item> DEEP_GEM_ARMOR_HELMET = ITEMS.register("deep_gem_armor_helmet", DeepGemArmorItem.Helmet::new);
	public static final RegistryObject<Item> DEEP_GEM_ARMOR_CHESTPLATE = ITEMS.register("deep_gem_armor_chestplate", DeepGemArmorItem.Chestplate::new);
	public static final RegistryObject<Item> DEEP_GEM_ARMOR_LEGGINGS = ITEMS.register("deep_gem_armor_leggings", DeepGemArmorItem.Leggings::new);
	public static final RegistryObject<Item> DEEP_GEM_ARMOR_BOOTS = ITEMS.register("deep_gem_armor_boots", DeepGemArmorItem.Boots::new);
	public static final RegistryObject<Item> VOIDINGOT_ARMOR_HELMET = ITEMS.register("voidingot_armor_helmet", VoidArmorItem.Helmet::new);
	public static final RegistryObject<Item> VOIDINGOT_ARMOR_CHESTPLATE = ITEMS.register("voidingot_armor_chestplate", VoidArmorItem.Chestplate::new);
	public static final RegistryObject<Item> VOIDINGOT_ARMOR_LEGGINGS = ITEMS.register("voidingot_armor_leggings", VoidArmorItem.Leggings::new);
	public static final RegistryObject<Item> VOIDINGOT_ARMOR_BOOTS = ITEMS.register("voidingot_armor_boots", VoidArmorItem.Boots::new);
	public static final RegistryObject<Item> UNSEEN_ARMOR_HELMET = ITEMS.register("unseen_armor_helmet", UnseenArmorItem.Helmet::new);
	public static final RegistryObject<Item> UNSEEN_ARMOR_CHESTPLATE = ITEMS.register("unseen_armor_chestplate", UnseenArmorItem.Chestplate::new);
	public static final RegistryObject<Item> UNSEEN_ARMOR_LEGGINGS = ITEMS.register("unseen_armor_leggings", UnseenArmorItem.Leggings::new);
	public static final RegistryObject<Item> UNSEEN_ARMOR_BOOTS = ITEMS.register("unseen_armor_boots", UnseenArmorItem.Boots::new);
	public static final RegistryObject<Item> RED_TITANIUM_ARMOR_HELMET = ITEMS.register("red_titanium_armor_helmet", RedTitaniumArmorItem.Helmet::new);
	public static final RegistryObject<Item> RED_TITANIUM_ARMOR_CHESTPLATE = ITEMS.register("red_titanium_armor_chestplate", RedTitaniumArmorItem.Chestplate::new);
	public static final RegistryObject<Item> RED_TITANIUM_ARMOR_LEGGINGS = ITEMS.register("red_titanium_armor_leggings", RedTitaniumArmorItem.Leggings::new);
	public static final RegistryObject<Item> RED_TITANIUM_ARMOR_BOOTS = ITEMS.register("red_titanium_armor_boots", RedTitaniumArmorItem.Boots::new);
	public static final RegistryObject<Item> NATURERIUM_ARMOR_HELMET = ITEMS.register("naturerium_armor_helmet", NatureriumArmorItem.Helmet::new);
	public static final RegistryObject<Item> NATURERIUM_ARMOR_CHESTPLATE = ITEMS.register("naturerium_armor_chestplate", NatureriumArmorItem.Chestplate::new);
	public static final RegistryObject<Item> NATURERIUM_ARMOR_LEGGINGS = ITEMS.register("naturerium_armor_leggings", NatureriumArmorItem.Leggings::new);
	public static final RegistryObject<Item> NATURERIUM_ARMOR_BOOTS = ITEMS.register("naturerium_armor_boots", NatureriumArmorItem.Boots::new);
	public static final RegistryObject<Item> KNIGHT_ARMOR_HELMET = ITEMS.register("knight_armor_helmet", KnightArmorItem.Helmet::new);
	public static final RegistryObject<Item> KNIGHT_ARMOR_CHESTPLATE = ITEMS.register("knight_armor_chestplate", KnightArmorItem.Chestplate::new);
	public static final RegistryObject<Item> KNIGHT_ARMOR_LEGGINGS = ITEMS.register("knight_armor_leggings", KnightArmorItem.Leggings::new);
	public static final RegistryObject<Item> KNIGHT_ARMOR_BOOTS = ITEMS.register("knight_armor_boots", KnightArmorItem.Boots::new);
	public static final RegistryObject<Item> BLAZER_HELMET = ITEMS.register("blazer_helmet", BlazerItem.Helmet::new);
	public static final RegistryObject<Item> BLAZER_SHIELD = ITEMS.register("blazer_shield", () -> new ShieldItem(new Item.Properties().durability(1240).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> DEEP_GEM_PICKAXE = ITEMS.register("deep_gem_pickaxe", () -> new PickaxeItem(ModTiers.DEEP_GEM,1, -2.8F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> DEEP_GEM_AXE = ITEMS.register("deep_gem_axe", () -> new AxeItem(ModTiers.DEEP_GEM,5F, -3.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> DEEP_GEM_SHOVEL = ITEMS.register("deep_gem_shovel", () -> new ShovelItem(ModTiers.DEEP_GEM,1.5F, -3.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> DEEP_GEM_HOE = ITEMS.register("deep_gem_hoe", () -> new HoeItem(ModTiers.DEEP_GEM,-3, 0.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NATURERIUM_PICKAXE = ITEMS.register("naturerium_pickaxe", () -> new PickaxeItem(ModTiers.NATURERIUM,2, -2.8F, new Item.Properties()));
	public static final RegistryObject<Item> NATURERIUM_AXE = ITEMS.register("naturerium_axe", () -> new AxeItem(ModTiers.NATURERIUM,6F, -3.0F, new Item.Properties()));
	public static final RegistryObject<Item> NATURERIUM_SHOVEL = ITEMS.register("naturerium_shovel", () -> new ShovelItem(ModTiers.NATURERIUM,2.5F, -3.0F, new Item.Properties()));
	public static final RegistryObject<Item> NATURERIUM_HOE = ITEMS.register("naturerium_hoe", () -> new HoeItem(ModTiers.NATURERIUM,-4, 0.0F, new Item.Properties()));
	public static final RegistryObject<Item> UNSEEN_PICKAXE = ITEMS.register("unseen_pickaxe", () -> new PickaxeItem(ModTiers.UNSEENIUM,2, -2.8F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> UNSEEN_AXE = ITEMS.register("unseen_axe", () -> new AxeItem(ModTiers.UNSEENIUM,6F, -3.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> UNSEEN_SHOVEL = ITEMS.register("unseen_shovel", () -> new ShovelItem(ModTiers.UNSEENIUM,2.5F, -3.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> UNSEEN_HOE = ITEMS.register("unseen_hoe", () -> new HoeItem(ModTiers.UNSEENIUM,-4, 0.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> VOID_INGOT_PICKAXE = ITEMS.register("void_ingot_pickaxe", () -> new PickaxeItem(ModTiers.VOID,2, -2.8F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> VOID_INGOT_AXE = ITEMS.register("void_ingot_axe", () -> new AxeItem(ModTiers.VOID,6F, -3.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> VOID_INGOT_SHOVEL = ITEMS.register("void_ingot_shovel", () -> new ShovelItem(ModTiers.VOID,2.5F, -3.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> VOID_INGOT_HOE = ITEMS.register("void_ingot_hoe", () -> new HoeItem(ModTiers.VOID,-5, 0.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> RED_TITANIUM_PICKAXE = ITEMS.register("red_titanium_pickaxe", () -> new PickaxeItem(ModTiers.TITANIUM,2, -2.8F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> RED_TITANIUM_AXE = ITEMS.register("red_titanium_axe", () -> new AxeItem(ModTiers.TITANIUM,6F, -3.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> RED_TITANIUM_SHOVEL = ITEMS.register("red_titanium_shovel", () -> new ShovelItem(ModTiers.TITANIUM,2.5F, -3.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> RED_TITANIUM_HOE = ITEMS.register("red_titanium_hoe", () -> new HoeItem(ModTiers.TITANIUM,-6, 0.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> VOID_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("void_upgrade_smithing_template", VoidUpgradeSmithingTemplateItem::new);
	public static final RegistryObject<Item> THE_DARKNESS = ITEMS.register("the_darkness", TheDarknessItem::new);
	public static final RegistryObject<Item> DARK_GOLEM_HEART = ITEMS.register("dark_golem_heart", DarkGolemHeartItem::new);
	public static final RegistryObject<Item> DARK_PEARL = ITEMS.register("dark_pearl", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)){
		@Override
		public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
			InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
			DarkPearlRightClickedProcedure.execute(entity);
			return ar;
		}
	});
	public static final RegistryObject<Item> FIRE_PEARL = ITEMS.register("fire_pearl", () -> new Item(new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> DEEP_GEM = ITEMS.register("deep_gem", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> BLUE_VOID = ITEMS.register("blue_void", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> VOID_INGOT = ITEMS.register("void_ingot_ingot", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> RAW_RED_TITANIUM = ITEMS.register("raw_red_titanium", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> RED_TITANIUM_INGOT = ITEMS.register("red_titanium_ingot", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> RAW_UNSEENIUM = ITEMS.register("raw_unseenium", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> UNSEEN_INGOT = ITEMS.register("unseen_ingot", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> NATURERIUM_INGOT = ITEMS.register("naturerium_ingot", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> RED_BLAZE_ROD = ITEMS.register("red_blaze_rod", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BLAZE_SHIELD_SHARD = ITEMS.register("blaze_shield_shard", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TANZANITE_SHARD = ITEMS.register("tanzanite_shard", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> CHLORITE_SLATE_STONE_SHARD = ITEMS.register("chlorite_slate_stone_shard", ()-> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TEALIVE_STONY_SHARD = ITEMS.register("tealive_stony_shard", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> DARK_FREE_SOUL = ITEMS.register("dark_free_soul", DarkFreeSoulItem::new);
	public static final RegistryObject<Item> OUTGROWTH_APPLE = ITEMS.register("outgrowthapple", OutgrowthAppleItem::new);
	public static final RegistryObject<Item> CHIMERIC_BLUE_PEPPER = ITEMS.register("chimeric_blue_pepper", () -> new Item(new Item.Properties().rarity(Rarity.RARE).food((new FoodProperties.Builder()).nutrition(2).fast().saturationMod(2f).build())));
	public static final RegistryObject<Item> PURPLE_BERRIES = ITEMS.register("purple_berries",()->new ModFoodItem(new Item.Properties().rarity(Rarity.RARE).food((new FoodProperties.Builder()).nutrition(3).saturationMod(1f).build()),new MobEffectInstance(MobEffects.HEAL, 60, 1)));
	public static final RegistryObject<Item> LUMINOUS_PORKCHOP = ITEMS.register("luminousporkchop", ()->new ModFoodItem(new Item.Properties().rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.3f).meat().build()),new MobEffectInstance(MobEffects.GLOWING, 600, 1, (false), (false))));
	public static final RegistryObject<Item> LUMINOUS_COOKED_PORKCHOP = ITEMS.register("luminouscookedporkchop", ()->new ModFoodItem(new Item.Properties().rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(7).saturationMod(4f).meat().build()),new MobEffectInstance(MobEffects.GLOWING, 600, 1, (false), (false))));
	public static final RegistryObject<Item> BERRIES_OF_BLOOMING_VINE = ITEMS.register("berriesfrom_blooming_vine", () -> new ItemNameBlockItem(UnseenWorldModBlocks.DARK_CRIMSON_VINE_FLOWER.get(), new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(3).saturationMod(1.5f).build())){
		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 64;
		}
	});
	public static final RegistryObject<Item> CRIMSERRY_SOUL_BERRY = ITEMS.register("crimserry_soul_berry_food", ()-> new ModFoodItem(new Item.Properties().rarity(Rarity.COMMON).food((new FoodProperties.Builder()).nutrition(2).saturationMod(1f).build()),new MobEffectInstance(UnseenWorldModMobEffects.SOUL_OVERGROWTH.get(), 100, 1)));
	public static final RegistryObject<Item> DISHWITH_BERRIES = ITEMS.register("dishwith_berries", DishWithBerriesItem::new);
	public static final RegistryObject<Item> BOWL_WITH_BERRIES_WITHOUT_EFFECT = ITEMS.register("bowlwith_berrieswithout_effect", BowlWithBerriesWithoutEffectItem::new);
	public static final RegistryObject<Item> MOON_FISH_FOOD = ITEMS.register("moon_fish_food", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(2).saturationMod(1f).build())){
		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 24;
		}
	});
	public static final RegistryObject<Item> COOKED_MOON_FISH = ITEMS.register("cooked_moon_fish", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(3).saturationMod(2.5f).build())));
	public static final RegistryObject<Item> NIGHTDEW_NECTAR_BOTTLE = ITEMS.register("nightdew_nectar_bottle", NightdewNectarBottleItem::new);
	public static final RegistryObject<Item> DUSTY_PINK_MAXOR_FISH_FOOD = ITEMS.register("dusty_pink_maxor_fish_food", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.8f).build())){
		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 24;
		}
	});
	public static final RegistryObject<Item> COOKED_DUSTY_PINK_MAXOR_FISH = ITEMS.register("cooked_dusty_pink_maxor_fish", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(3).saturationMod(3f).build())));
	public static final RegistryObject<Item> DISH_VEGETABLE_WITH_PORK = ITEMS.register("dish_vegetable_with_pork", DishVegetableWithPorkItem::new);
	//spawn eggs
	public static final RegistryObject<Item> DARK_SKELETON_SPAWN_EGG = ITEMS.register("dark_skeleton_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.DARK_SKELETON, -14935012, -3355393, new Item.Properties()));
	public static final RegistryObject<Item> AMETHYST_GOLEM_SPAWN_EGG = ITEMS.register("amethyst_golem_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.AMETHYST_GOLEM, -10066330, -26113, new Item.Properties()));
	public static final RegistryObject<Item> DARK_PHANTOM_SPAWN_EGG = ITEMS.register("dark_phantom_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.DARK_PHANTOM, -16777216, -1, new Item.Properties()));
	public static final RegistryObject<Item> DUSTY_PINK_MAXOR_FISH_SPAWN_EGG = ITEMS.register("dusty_pink_maxor_fish_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.DUSTY_PINK_MAXOR_FISH, -26113, -13421773, new Item.Properties()));
	public static final RegistryObject<Item> MOONFISH_SPAWN_EGG = ITEMS.register("moonfish_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.MOONFISH, -11770241, -3355393, new Item.Properties()));
	public static final RegistryObject<Item> CAVERN_SCARECROW_SPAWN_EGG = ITEMS.register("cavern_scarecrow_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.CAVERN_SCARECROW, -8355712, -27648, new Item.Properties()));
	public static final RegistryObject<Item> SAVAGE_SMALL_BLAZE_SPAWN_EGG = ITEMS.register("savage_small_blaze_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.SAVAGE_SMALL_BLAZE, -39424, -13434880, new Item.Properties()));
	public static final RegistryObject<Item> CHIMERIC_PURPLEMARER_SPAWN_EGG = ITEMS.register("chimeric_purplemarer_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.CHIMERIC_PURPLEMARER, -13434829, -6750055, new Item.Properties()));
	public static final RegistryObject<Item> CHIMERIC_REDMARER_SPAWN_EGG = ITEMS.register("chimeric_redmarer_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.CHIMERIC_REDMARER, -6750208, -6737152, new Item.Properties()));
	public static final RegistryObject<Item> NETHERMEN_SPAWN_EGG = ITEMS.register("nethermen_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.NETHERMAN, -33024, -41153, new Item.Properties()));
	public static final RegistryObject<Item> RED_SLYLF_SPAWN_EGG = ITEMS.register("red_slylf_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.RED_SLYLF, -6737152, -39373, new Item.Properties()));
	public static final RegistryObject<Item> RED_BLAZE_SPAWN_EGG = ITEMS.register("red_blaze_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.RED_BLAZE, -11010048, -29184, new Item.Properties()));
	public static final RegistryObject<Item> STREDER_SPAWN_EGG = ITEMS.register("streder_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.STREDER, -9671553, -13421773, new Item.Properties()));
	public static final RegistryObject<Item> GHAST_OF_TEALIVE_VALLEY_SPAWN_EGG = ITEMS.register("ghast_of_tealive_valley_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.GHAST_OF_TEALIVE_VALLEY, -16724839, -16724788, new Item.Properties()));
	public static final RegistryObject<Item> TANZANITE_GUARDIAN_SPAWN_EGG = ITEMS.register("tanzanite_guardian_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.TANZANITE_GUARDIAN, -10092442, -39220, new Item.Properties()));
	public static final RegistryObject<Item> DARK_SPIRIT_WOLF_SPAWN_EGG = ITEMS.register("dark_spirit_wolf_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.DARK_SPIRIT_WOLF, -13421773, -16724737, new Item.Properties()));
	public static final RegistryObject<Item> VOID_ENDERMEN_SPAWN_EGG = ITEMS.register("void_endermen_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.VOID_ENDERMEN, -13877415, -9846858, new Item.Properties()));
	public static final RegistryObject<Item> TEALIVE_SKELETON_SPAWN_EGG = ITEMS.register("tealive_skeleton_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.TEALIVE_SKELETON, -16764109, -16737946, new Item.Properties()));
	public static final RegistryObject<Item> RED_RAVENGER_SPAWN_EGG = ITEMS.register("red_ravenger_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.RED_RAVENGER, -9297374, -49408, new Item.Properties()));
	public static final RegistryObject<Item> DARK_HOGLIN_SPAWN_EGG = ITEMS.register("dark_hoglin_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.DARK_HOGLIN, -3355393, -16777216, new Item.Properties()));
	public static final RegistryObject<Item> SNOWDRIFTER_SPAWN_EGG = ITEMS.register("snowdrifter_spawn_egg", () -> new ForgeSpawnEggItem(UnseenWorldModEntities.SNOWDRIFTER, 8496292, 14283506, new Item.Properties()));
}