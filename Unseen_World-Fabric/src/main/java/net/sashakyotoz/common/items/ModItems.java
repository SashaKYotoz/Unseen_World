package net.sashakyotoz.common.items;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.core.Direction;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.combat.WeaponEvents;
import net.sashakyotoz.common.items.custom.*;
import net.sashakyotoz.common.tags.ModTags;

import java.util.Optional;
import java.util.function.Supplier;

public class ModItems {
    public static void register() {
        UnseenWorld.log().debug("Registering Items for modid : " + UnseenWorld.MOD_ID);

        ComposterBlock.COMPOSTABLES.put(ModBlocks.UMBRAL_KELP.asItem(), 0.15F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.GLOOMWEED.asItem(), 0.1F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.TALL_GLOOMWEED.asItem(), 0.15F);
        ComposterBlock.COMPOSTABLES.put(CRYSTIE_APPLE, 0.05F);
        ComposterBlock.COMPOSTABLES.put(GLOW_APPLE, 0.05F);
        ComposterBlock.COMPOSTABLES.put(BEARFRUIT_BRAMBLE, 0.05F);
        ComposterBlock.COMPOSTABLES.put(NIGHTBERRY, 0.05F);

        WeaponEvents.register();
    }

    private static final ModelTemplate SPAWN_EGG = new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/template_spawn_egg")), Optional.empty());

    public static final Item ECLIPSE_KEYSTONE = flat("eclipse_keystone", Rarity.EPIC);

    public static final Item RAW_UNSEENIUM_ORE = flat("raw_unseenium", Rarity.UNCOMMON);
    public static final Item RAW_ABYSSAL_ORE = flat("raw_abyssal", Rarity.UNCOMMON);
    public static final Item RAW_RED_TITANIUM_ORE = flat("raw_red_titanium", Rarity.UNCOMMON);
    public static final Item UNSEENIUM_INGOT = ingot("unseenium_ingot");
    public static final Item ABYSSAL_INGOT = ingot("abyssal_ingot");
    public static final Item RED_TITANIUM_INGOT = ingot("red_titanium_ingot");

    public static final Item UNSEENIUM_SHOVEL = handheld("unseenium_shovel", () -> new ShovelItem(ModTiers.UNSEENIUM, 2.5F, -3.0F, gear(false)), ItemTags.SHOVELS);
    public static final Item UNSEENIUM_PICKAXE = handheld("unseenium_pickaxe", () -> new PickaxeItem(ModTiers.UNSEENIUM, 2, -2.8F, gear(false)), ItemTags.PICKAXES);
    public static final Item UNSEENIUM_AXE = handheld("unseenium_axe", () -> new AxeItem(ModTiers.UNSEENIUM, 6, -3.0F, gear(false)), ItemTags.AXES);
    public static final Item UNSEENIUM_HOE = handheld("unseenium_hoe", () -> new HoeItem(ModTiers.UNSEENIUM, -5, 0, gear(false)), ItemTags.HOES);
    public static final Item UNSEENIUM_SWORD = handheld("unseenium_sword", () -> new SwordItem(ModTiers.UNSEENIUM, 4, -2.4F, gear(false)), ItemTags.SWORDS);
    public static final Item UNSEENIUM_HELMET = armor("unseenium_helmet", ModArmorMaterials.UNSEENIUM, ArmorItem.Type.HELMET, false);
    public static final Item UNSEENIUM_CHESTPLATE = armor("unseenium_chestplate", ModArmorMaterials.UNSEENIUM, ArmorItem.Type.CHESTPLATE, false);
    public static final Item UNSEENIUM_LEGGINGS = armor("unseenium_leggings", ModArmorMaterials.UNSEENIUM, ArmorItem.Type.LEGGINGS, false);
    public static final Item UNSEENIUM_BOOTS = armor("unseenium_boots", ModArmorMaterials.UNSEENIUM, ArmorItem.Type.BOOTS, false);

    public static final Item ABYSSAL_SHOVEL = handheld("abyssal_shovel", () -> new ShovelItem(ModTiers.ABYSSAL, 2.5F, -3.0F, gear(true)), ItemTags.SHOVELS);
    public static final Item ABYSSAL_PICKAXE = handheld("abyssal_pickaxe", () -> new PickaxeItem(ModTiers.ABYSSAL, 2, -2.8F, gear(true)), ItemTags.PICKAXES);
    public static final Item ABYSSAL_AXE = handheld("abyssal_axe", () -> new AxeItem(ModTiers.ABYSSAL, 6, -3.0F, gear(true)), ItemTags.AXES);
    public static final Item ABYSSAL_HOE = handheld("abyssal_hoe", () -> new HoeItem(ModTiers.ABYSSAL, -6, 0, gear(true)), ItemTags.HOES);
    public static final Item ABYSSAL_SWORD = handheld("abyssal_sword", () -> new SwordItem(ModTiers.ABYSSAL, 4, -2.4F, gear(true)), ItemTags.SWORDS);
    public static final Item ABYSSAL_HELMET = armor("abyssal_helmet", ModArmorMaterials.ABYSSAL, ArmorItem.Type.HELMET, true);
    public static final Item ABYSSAL_CHESTPLATE = armor("abyssal_chestplate", ModArmorMaterials.ABYSSAL, ArmorItem.Type.CHESTPLATE, true);
    public static final Item ABYSSAL_LEGGINGS = armor("abyssal_leggings", ModArmorMaterials.ABYSSAL, ArmorItem.Type.LEGGINGS, true);
    public static final Item ABYSSAL_BOOTS = armor("abyssal_boots", ModArmorMaterials.ABYSSAL, ArmorItem.Type.BOOTS, true);

    public static final Item RED_TITANIUM_SHOVEL = handheld("red_titanium_shovel", () -> new ShovelItem(ModTiers.TITANIUM, 2.5F, -3.0F, gear(true)), ItemTags.SHOVELS);
    public static final Item RED_TITANIUM_PICKAXE = handheld("red_titanium_pickaxe", () -> new PickaxeItem(ModTiers.TITANIUM, 2, -2.8F, gear(true)), ItemTags.PICKAXES);
    public static final Item RED_TITANIUM_AXE = handheld("red_titanium_axe", () -> new AxeItem(ModTiers.TITANIUM, 6, -3.0F, gear(true)), ItemTags.AXES);
    public static final Item RED_TITANIUM_HOE = handheld("red_titanium_hoe", () -> new HoeItem(ModTiers.TITANIUM, -6, 0, gear(true)), ItemTags.HOES);
    public static final Item RED_TITANIUM_SWORD = handheld("red_titanium_sword", () -> new SwordItem(ModTiers.TITANIUM, 4, -2.4F, gear(true)), ItemTags.SWORDS);
    public static final Item RED_TITANIUM_HELMET = armor("red_titanium_helmet", ModArmorMaterials.TITANIUM, ArmorItem.Type.HELMET, true);
    public static final Item RED_TITANIUM_CHESTPLATE = armor("red_titanium_chestplate", ModArmorMaterials.TITANIUM, ArmorItem.Type.CHESTPLATE, true);
    public static final Item RED_TITANIUM_LEGGINGS = armor("red_titanium_leggings", ModArmorMaterials.TITANIUM, ArmorItem.Type.LEGGINGS, true);
    public static final Item RED_TITANIUM_BOOTS = armor("red_titanium_boots", ModArmorMaterials.TITANIUM, ArmorItem.Type.BOOTS, true);

    public static final Item AURIC_ARC = holder("auric_arc", () -> new AuricArcItem(9.0D, -2.8D,
            new Item.Properties().rarity(Rarity.EPIC).durability(ModTiers.AURIC_ARC.getUses()))).get();
    public static final Item CHIMERIC_ROCKBREAKER_HAMMER = holder("chimeric_rockbreaker_hammer", () -> new ChimericRockbreakerHammerItem(4, -3.1F,
            new Item.Properties().rarity(Rarity.EPIC))).withTags(ItemTags.PICKAXES).get();
    public static final Item SHIELD_OF_CHIMERIC_WARRIOR = holder("shield_of_chimeric_warrior", () -> new ShieldOfChimericWarriorItem(
            new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).durability(893))).withTags(ConventionalItemTags.SHIELDS).get();
    public static final Item ECLIPSEBANE = holder("eclipsebane", () -> new EclipsebaneItem(4, -2.6F,
            new Item.Properties().rarity(Rarity.EPIC))).withTags(ItemTags.SWORDS).get();
    public static final Item GRIPPING_ABYSSAL_BOW = holder("gripping_abyssal_bow", () -> new GrippingAbyssalBowItem(
            new Item.Properties().rarity(Rarity.RARE).durability(984))).withTags(ConventionalItemTags.BOWS).get();

    public static final Item GRIPPING_BUNDLE = holder("gripping_bundle", () -> new GrippingBundleItem(
            new Item.Properties().rarity(Rarity.EPIC).stacksTo(1))).get();
    public static final Item GRIPPING_GAUNTLET = holder("gripping_gauntlet", () -> new GrippingGauntletItem(
            new Item.Properties().rarity(Rarity.EPIC).stacksTo(1))).get();

    public static final Item CRYSTIE_APPLE = food("crystie_apple", ModRegistry.Foods.CRYSTIE_APPLE, ModTags.Items.TUSKHOG_TAMING_ITEMS);
    public static final Item GLOW_APPLE = food("glow_apple", ModRegistry.Foods.GLOW_APPLE, ModTags.Items.TUSKHOG_TAMING_ITEMS);
    public static final Item BEARFRUIT_BRAMBLE = food("bearfruit_bramble", ModRegistry.Foods.BEARFRUIT_BRAMBLE);
    public static final Item NIGHTBERRY = food("nightberry", ModRegistry.Foods.NIGHTBERRY, ModTags.Items.SERPENT_TAMING_ITEMS);
    public static final Item WARPEDVEIL_VINE_FRUIT = holder("warpedveil_vine_fruit", () -> new ItemNameBlockItem(ModBlocks.CRIMSONVEIL_VINE,
            new Item.Properties().rarity(Rarity.UNCOMMON).food(ModRegistry.Foods.WARPEDVEIL_VINE_FRUIT))).withModel(ModelTemplates.FLAT_ITEM).get();
    public static final Item GLOWROOT_BULB = holder("glowroot_bulb", () -> new ItemNameBlockItem(ModBlocks.GLOWROOT_VINE,
            new Item.Properties().rarity(Rarity.UNCOMMON).food(ModRegistry.Foods.GLOWROOT_BULB)))
            .withTags(ModTags.Items.RIMEHORN_TAMING_ITEMS).withModel(ModelTemplates.FLAT_ITEM).get();
    public static final Item RIMEHORN_MEAT = food("rimehorn_meat", ModRegistry.Foods.RIMEHORN_MEAT);
    public static final Item COOKED_RIMEHORN_MEAT = food("cooked_rimehorn_meat", ModRegistry.Foods.COOKED_RIMEHORN_MEAT);

    public static final Item GRIPCRYSTAL = crystal("gripcrystal");
    public static final Item GRANULATED_GRIPCRYSTAL = crystal("granulated_gripcrystal");
    public static final Item GRIPTONITE = holder("griptonite", () -> new GriptoniteItem(new Item.Properties().stacksTo(1).durability(4).rarity(Rarity.UNCOMMON)))
            .withTags(ModTags.Items.GRIPPING_BUNDLE_CAN_HANDLE).withModel(ModelTemplates.FLAT_ITEM).get();
    public static final Item GRANULATED_GRIPTONITE = crystal("granulated_griptonite");

    public static final Item THICK_STRING = flat("thick_string", Rarity.UNCOMMON);
    public static final Item GRIPPING_MEMBRANE = holder("gripping_membrane", () -> new Item(new Item.Properties().rarity(Rarity.RARE)))
            .withTags(ModTags.Items.VEXAL_BETTLE_TAMING_ITEMS).withModel(ModelTemplates.FLAT_ITEM).get();

    public static final Item GRIPCRYSTAL_KEY = key("gripcrystal_key");
    public static final Item ABYSSAL_KEY = key("abyssal_key");
    public static final Item AURIC_KEY = key("auric_key");

    public static final Block AMETHYST_SIGN = standingSign("amethyst_sign", ModBlocks.AMETHYST, ModTags.Blocks.AMETHYST_BLOCKS, () -> ModItems.AMETHYST_SIGN_ITEM);
    public static final Block AMETHYST_WALL_SIGN = wallSign("amethyst_wall_sign", ModBlocks.AMETHYST, () -> ModItems.AMETHYST_SIGN_ITEM);
    public static final Block AMETHYST_HANGING_SIGN = hangingSign("amethyst_hanging_sign", ModBlocks.AMETHYST, ModTags.Blocks.AMETHYST_BLOCKS, () -> ModItems.AMETHYST_HANGING_SIGN_ITEM);
    public static final Block AMETHYST_WALL_HANGING_SIGN = wallHangingSign("amethyst_wall_hanging_sign", ModBlocks.AMETHYST, ModTags.Blocks.AMETHYST_BLOCKS, () -> ModItems.AMETHYST_HANGING_SIGN_ITEM);
    public static final Item AMETHYST_SIGN_ITEM = signItem("amethyst_sign", AMETHYST_SIGN, AMETHYST_WALL_SIGN);
    public static final Item AMETHYST_HANGING_SIGN_ITEM = hangingSignItem("amethyst_hanging_sign", AMETHYST_HANGING_SIGN, AMETHYST_WALL_HANGING_SIGN);

    public static final Block GRIZZLY_SIGN = standingSign("grizzly_sign", ModBlocks.GRIZZLY, ModTags.Blocks.GRIZZLY_BLOCKS, () -> ModItems.GRIZZLY_SIGN_ITEM);
    public static final Block GRIZZLY_WALL_SIGN = wallSign("grizzly_wall_sign", ModBlocks.GRIZZLY, () -> ModItems.GRIZZLY_SIGN_ITEM);
    public static final Block GRIZZLY_HANGING_SIGN = hangingSign("grizzly_hanging_sign", ModBlocks.GRIZZLY, ModTags.Blocks.GRIZZLY_BLOCKS, () -> ModItems.GRIZZLY_HANGING_SIGN_ITEM);
    public static final Block GRIZZLY_WALL_HANGING_SIGN = wallHangingSign("grizzly_wall_hanging_sign", ModBlocks.GRIZZLY, ModTags.Blocks.GRIZZLY_BLOCKS, () -> ModItems.GRIZZLY_HANGING_SIGN_ITEM);
    public static final Item GRIZZLY_SIGN_ITEM = signItem("grizzly_sign", GRIZZLY_SIGN, GRIZZLY_WALL_SIGN);
    public static final Item GRIZZLY_HANGING_SIGN_ITEM = hangingSignItem("grizzly_hanging_sign", GRIZZLY_HANGING_SIGN, GRIZZLY_WALL_HANGING_SIGN);

    public static final Block TEALIVE_SIGN = standingSign("tealive_sign", ModBlocks.TEALIVE, ModTags.Blocks.TEALIVE_BLOCKS, () -> ModItems.TEALIVE_SIGN_ITEM);
    public static final Block TEALIVE_WALL_SIGN = wallSign("tealive_wall_sign", ModBlocks.TEALIVE, () -> ModItems.TEALIVE_SIGN_ITEM);
    public static final Block TEALIVE_HANGING_SIGN = hangingSign("tealive_hanging_sign", ModBlocks.TEALIVE, ModTags.Blocks.TEALIVE_BLOCKS, () -> ModItems.TEALIVE_HANGING_SIGN_ITEM);
    public static final Block TEALIVE_WALL_HANGING_SIGN = wallHangingSign("tealive_wall_hanging_sign", ModBlocks.TEALIVE, ModTags.Blocks.TEALIVE_BLOCKS, () -> ModItems.TEALIVE_HANGING_SIGN_ITEM);
    public static final Item TEALIVE_SIGN_ITEM = signItem("tealive_sign", TEALIVE_SIGN, TEALIVE_WALL_SIGN);
    public static final Item TEALIVE_HANGING_SIGN_ITEM = hangingSignItem("tealive_hanging_sign", TEALIVE_HANGING_SIGN, TEALIVE_WALL_HANGING_SIGN);

    public static final Block BURLYWOOD_SIGN = standingSign("burlywood_sign", ModBlocks.BURLYWOOD, ModTags.Blocks.BURLYWOOD_BLOCKS, () -> ModItems.BURLYWOOD_SIGN_ITEM);
    public static final Block BURLYWOOD_WALL_SIGN = wallSign("burlywood_wall_sign", ModBlocks.BURLYWOOD, () -> ModItems.BURLYWOOD_SIGN_ITEM);
    public static final Block BURLYWOOD_HANGING_SIGN = hangingSign("burlywood_hanging_sign", ModBlocks.BURLYWOOD, ModTags.Blocks.BURLYWOOD_BLOCKS, () -> ModItems.BURLYWOOD_HANGING_SIGN_ITEM);
    public static final Block BURLYWOOD_WALL_HANGING_SIGN = wallHangingSign("burlywood_wall_hanging_sign", ModBlocks.BURLYWOOD, ModTags.Blocks.BURLYWOOD_BLOCKS, () -> ModItems.BURLYWOOD_HANGING_SIGN_ITEM);
    public static final Item BURLYWOOD_SIGN_ITEM = signItem("burlywood_sign", BURLYWOOD_SIGN, BURLYWOOD_WALL_SIGN);
    public static final Item BURLYWOOD_HANGING_SIGN_ITEM = hangingSignItem("burlywood_hanging_sign", BURLYWOOD_HANGING_SIGN, BURLYWOOD_WALL_HANGING_SIGN);

    public static final Block CRIMSONVEIL_SIGN = standingSign("crimsonveil_sign", ModBlocks.CRIMSONVEIL, ModTags.Blocks.CRIMSONVEIL_BLOCKS, () -> ModItems.CRIMSONVEIL_SIGN_ITEM);
    public static final Block CRIMSONVEIL_WALL_SIGN = wallSign("crimsonveil_wall_sign", ModBlocks.CRIMSONVEIL, () -> ModItems.CRIMSONVEIL_SIGN_ITEM);
    public static final Block CRIMSONVEIL_HANGING_SIGN = hangingSign("crimsonveil_hanging_sign", ModBlocks.CRIMSONVEIL, ModTags.Blocks.CRIMSONVEIL_BLOCKS, () -> ModItems.CRIMSONVEIL_HANGING_SIGN_ITEM);
    public static final Block CRIMSONVEIL_WALL_HANGING_SIGN = wallHangingSign("crimsonveil_wall_hanging_sign", ModBlocks.CRIMSONVEIL, ModTags.Blocks.CRIMSONVEIL_BLOCKS, () -> ModItems.CRIMSONVEIL_HANGING_SIGN_ITEM);
    public static final Item CRIMSONVEIL_SIGN_ITEM = signItem("crimsonveil_sign", CRIMSONVEIL_SIGN, CRIMSONVEIL_WALL_SIGN);
    public static final Item CRIMSONVEIL_HANGING_SIGN_ITEM = hangingSignItem("crimsonveil_hanging_sign", CRIMSONVEIL_HANGING_SIGN, CRIMSONVEIL_WALL_HANGING_SIGN);

    public static final Item DARK_WATER_BUCKET = holder("dark_water_bucket", () -> new DarkWaterBucketItem(ModFluids.DARK_WATER,
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1))).withModel(ModelTemplates.FLAT_ITEM).get();

    public static final Item GLEAMCARVER_BUCKET = holder("gleamcarver_bucket", () -> new GleamcarverBucketItem(
            new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET))).withModel(ModelTemplates.FLAT_ITEM).get();

    public static final Item DEAD_UMBRAL_CORAL_FAN_ITEM = holder("dead_umbral_coral_fan", () -> new StandingAndWallBlockItem(
            ModBlocks.DEAD_UMBRAL_CORAL_FAN, ModBlocks.DEAD_UMBRAL_CORAL_WALL_FAN, new Item.Properties(), Direction.DOWN)).get();
    public static final Item UMBRAL_CORAL_FAN_ITEM = holder("umbral_coral_fan", () -> new StandingAndWallBlockItem(
            ModBlocks.UMBRAL_CORAL_FAN, ModBlocks.UMBRAL_CORAL_WALL_FAN, new Item.Properties(), Direction.DOWN)).get();

    public static final Item GLEAMCARVER_SPAWN_EGG = spawnEgg("gleamcarver_spawn_egg", ModEntities.GLEAMCARVER, 9787244, 3746083);
    public static final Item HARMONY_WATCHER_SPAWN_EGG = spawnEgg("harmony_watcher_spawn_egg", ModEntities.HARMONY_WATCHER, 5013401, 10066329);
    public static final Item VIOLEGER_SPAWN_EGG = spawnEgg("violeger_spawn_egg", ModEntities.VIOLEGER, 12801229, 6035741);
    public static final Item VENOMER_SPAWN_EGG = spawnEgg("venomer_spawn_egg", ModEntities.VENOMER, 12801229, 9715553);
    public static final Item GLOOMWHALE_SPAWN_EGG = spawnEgg("gloomwhale_spawn_egg", ModEntities.GLOOMWHALE, 4030943, 1644825);
    public static final Item GRIPPING_GLOOMWHALE_SPAWN_EGG = spawnEgg("gripping_gloomwhale_spawn_egg", ModEntities.GRIPPING_GLOOMWHALE, 4030943, 1643895);
    public static final Item SHIMMER_SPAWN_EGG = spawnEgg("shimmer_spawn_egg", ModEntities.SHIMMER, 9131142, 8278982);
    public static final Item SABERPARD_SPAWN_EGG = spawnEgg("saberpard_spawn_egg", ModEntities.SABERPARD, 16445005, 1644825);
    public static final Item ESPYER_SPAWN_EGG = spawnEgg("espyer_spawn_egg", ModEntities.ESPYER, 10066329, 1474182);
    public static final Item ELDRITCH_WATCHER_SPAWN_EGG = spawnEgg("eldritch_watcher_spawn_egg", ModEntities.ELDRITCH_WATCHER, 5013401, 0);
    public static final Item TUSKHOG_SPAWN_EGG = spawnEgg("tuskhog_spawn_egg", ModEntities.TUSKHOG, 15771042, 5000268);
    public static final Item DREADFLAP_SPAWN_EGG = spawnEgg("dreadflap_spawn_egg", ModEntities.DREADFLAP, 1644825, 8476209);
    public static final Item REAVER_SPAWN_EGG = spawnEgg("reaver_spawn_egg", ModEntities.REAVER, 8278982, 1711153);
    public static final Item ECLIPSED_ACOLYTE_SPAWN_EGG = spawnEgg("eclipsed_acolyte_spawn_egg", ModEntities.ECLIPSED_ACOLYTE, 5000268, 8339378);
    public static final Item AUREATE_VEX_SPAWN_EGG = spawnEgg("aureate_vex_spawn_egg", ModEntities.AUREATE_VEX, 9787244, 16445005);
    public static final Item VEXAL_BEETLE_SPAWN_EGG = spawnEgg("vexal_beetle_spawn_egg", ModEntities.VEXAL_BEETLE, 2430480, 8339378);
    public static final Item RIMEHORN_SPAWN_EGG = spawnEgg("rimehorn_spawn_egg", ModEntities.RIMEHORN, 1474182, 1711153);
    public static final Item UMBRAL_SERPENT_SPAWN_EGG = spawnEgg("umbral_serpent_spawn_egg", ModEntities.UMBRAL_SERPENT, 2236979, 5592456);

    private static ItemHolder holder(String name, Supplier<Item> item) {
        return UnseenWorld.MOD.register(ItemHolder.class, name, item);
    }

    private static Item flat(String name, Rarity rarity) {
        return holder(name, () -> new Item(new Item.Properties().rarity(rarity))).withModel(ModelTemplates.FLAT_ITEM).get();
    }

    private static Item ingot(String name) {
        return holder(name, () -> new Item(new Item.Properties().rarity(Rarity.RARE)))
                .withTags(ConventionalItemTags.INGOTS).withModel(ModelTemplates.FLAT_ITEM).get();
    }

    private static Item crystal(String name) {
        return holder(name, () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)))
                .withTags(ModTags.Items.GRIPPING_BUNDLE_CAN_HANDLE).withModel(ModelTemplates.FLAT_ITEM).get();
    }

    private static Item key(String name) {
        return holder(name, () -> new Item(new Item.Properties().rarity(Rarity.RARE).stacksTo(1))).withModel(ModelTemplates.FLAT_ITEM).get();
    }

    @SafeVarargs
    private static Item food(String name, FoodProperties food, TagKey<Item>... tags) {
        return holder(name, () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).food(food))).withModel(ModelTemplates.FLAT_ITEM).withTags(tags).get();
    }

    private static Item.Properties gear(boolean fireResistant) {
        Item.Properties properties = new Item.Properties();
        return fireResistant ? properties.fireResistant() : properties;
    }

    private static Item handheld(String name, Supplier<Item> item, TagKey<Item> toolTag) {
        return holder(name, item).withTags(toolTag).withModel(ModelTemplates.FLAT_HANDHELD_ITEM).get();
    }

    private static Item armor(String name, ArmorMaterial material, ArmorItem.Type type, boolean fireResistant) {
        return holder(name, () -> {
            Item.Properties properties = new Item.Properties().rarity(Rarity.RARE);
            return new ModArmorItem(material, type, fireResistant ? properties.fireResistant() : properties);
        }).withTags(ItemTags.TRIMMABLE_ARMOR).get();
    }

    private static Item spawnEgg(String name, EntityType<? extends Mob> type, int primaryColor, int secondaryColor) {
        return holder(name, () -> new SpawnEggItem(type, primaryColor, secondaryColor, new Item.Properties())).withModel(SPAWN_EGG).get();
    }

    private static BlockHolder sign(String name, Supplier<Block> block, Supplier<ItemLike> drop) {
        return UnseenWorld.MOD.register(BlockHolder.class, name, block).dropsOther(drop);
    }

    private static BlockBehaviour.Properties signSettings() {
        return BlockBehaviour.Properties.copy(Blocks.OAK_SIGN);
    }

    private static Block standingSign(String name, WoodType wood, TagKey<Block> woodTag, Supplier<ItemLike> drop) {
        return sign(name, () -> new StandingSignBlock(signSettings(), wood), drop)
                .withTags(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, BlockTags.MINEABLE_WITH_AXE, woodTag).get();
    }

    private static Block wallSign(String name, WoodType wood, Supplier<ItemLike> drop) {
        return sign(name, () -> new WallSignBlock(signSettings(), wood), drop)
                .withTags(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE, BlockTags.MINEABLE_WITH_AXE).get();
    }

    private static Block hangingSign(String name, WoodType wood, TagKey<Block> woodTag, Supplier<ItemLike> drop) {
        return sign(name, () -> new CeilingHangingSignBlock(signSettings(), wood), drop)
                .withTags(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, BlockTags.MINEABLE_WITH_AXE, woodTag).get();
    }

    private static Block wallHangingSign(String name, WoodType wood, TagKey<Block> woodTag, Supplier<ItemLike> drop) {
        return sign(name, () -> new WallHangingSignBlock(signSettings(), wood), drop)
                .withTags(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, BlockTags.MINEABLE_WITH_AXE, woodTag).get();
    }

    private static Item signItem(String name, Block standing, Block wall) {
        return holder(name, () -> new SignItem(new Item.Properties().stacksTo(16), standing, wall)).withTags(ItemTags.SIGNS).get();
    }

    private static Item hangingSignItem(String name, Block ceiling, Block wall) {
        return holder(name, () -> new HangingSignItem(ceiling, wall, new Item.Properties().stacksTo(16))).withTags(ItemTags.HANGING_SIGNS).get();
    }
}