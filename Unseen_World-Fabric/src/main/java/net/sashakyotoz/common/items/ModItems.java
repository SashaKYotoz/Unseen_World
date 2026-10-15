package net.sashakyotoz.common.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.core.Direction;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.combat.WeaponEvents;
import net.sashakyotoz.common.items.custom.*;
import net.sashakyotoz.common.tags.ModTags;

public class ModItems {
    public static void register() {
        UnseenWorld.log().debug("Registering Items for modid : " + UnseenWorld.MOD_ID);

        ComposterBlock.COMPOSTABLES.put(ModBlocks.UMBRAL_KELP.asItem(), 0.15f);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.GLOOMWEED.asItem(), 0.1f);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.TALL_GLOOMWEED.asItem(), 0.15f);
        ComposterBlock.COMPOSTABLES.put(CRYSTIE_APPLE, 0.05f);
        ComposterBlock.COMPOSTABLES.put(GLOW_APPLE, 0.05f);
        ComposterBlock.COMPOSTABLES.put(BEARFRUIT_BRAMBLE, 0.05f);
        ComposterBlock.COMPOSTABLES.put(NIGHTBERRY, 0.05f);

        WeaponEvents.register();
    }

    public static final Item ECLIPSE_KEYSTONE = flat("eclipse_keystone", Rarity.EPIC);

    public static final Item RAW_UNSEENIUM_ORE = flat("raw_unseenium", Rarity.UNCOMMON);
    public static final Item RAW_ABYSSAL_ORE = flat("raw_abyssal", Rarity.UNCOMMON);
    public static final Item RAW_RED_TITANIUM_ORE = flat("raw_red_titanium", Rarity.UNCOMMON);
    public static final Item UNSEENIUM_INGOT = ingot("unseenium_ingot");
    public static final Item ABYSSAL_INGOT = ingot("abyssal_ingot");
    public static final Item RED_TITANIUM_INGOT = ingot("red_titanium_ingot");

    public static final Item UNSEENIUM_SHOVEL = handheld("unseenium_shovel", new ShovelItem(ModTiers.UNSEENIUM, 2.5f, -3f, gear(false)), ItemTags.SHOVELS);
    public static final Item UNSEENIUM_PICKAXE = handheld("unseenium_pickaxe", new PickaxeItem(ModTiers.UNSEENIUM, 2, -2.8f, gear(false)), ItemTags.PICKAXES);
    public static final Item UNSEENIUM_AXE = handheld("unseenium_axe", new AxeItem(ModTiers.UNSEENIUM, 6, -3f, gear(false)), ItemTags.AXES);
    public static final Item UNSEENIUM_HOE = handheld("unseenium_hoe", new HoeItem(ModTiers.UNSEENIUM, -5, 0, gear(false)), ItemTags.HOES);
    public static final Item UNSEENIUM_SWORD = handheld("unseenium_sword", new SwordItem(ModTiers.UNSEENIUM, 4, -2.4f, gear(false)), ItemTags.SWORDS);
    public static final Item UNSEENIUM_HELMET = armor("unseenium_helmet", ModArmorMaterials.UNSEENIUM, ArmorItem.Type.HELMET, false);
    public static final Item UNSEENIUM_CHESTPLATE = armor("unseenium_chestplate", ModArmorMaterials.UNSEENIUM, ArmorItem.Type.CHESTPLATE, false);
    public static final Item UNSEENIUM_LEGGINGS = armor("unseenium_leggings", ModArmorMaterials.UNSEENIUM, ArmorItem.Type.LEGGINGS, false);
    public static final Item UNSEENIUM_BOOTS = armor("unseenium_boots", ModArmorMaterials.UNSEENIUM, ArmorItem.Type.BOOTS, false);

    public static final Item ABYSSAL_SHOVEL = handheld("abyssal_shovel", new ShovelItem(ModTiers.ABYSSAL, 2.5f, -3f, gear(true)), ItemTags.SHOVELS);
    public static final Item ABYSSAL_PICKAXE = handheld("abyssal_pickaxe", new PickaxeItem(ModTiers.ABYSSAL, 2, -2.8f, gear(true)), ItemTags.PICKAXES);
    public static final Item ABYSSAL_AXE = handheld("abyssal_axe", new AxeItem(ModTiers.ABYSSAL, 6, -3f, gear(true)), ItemTags.AXES);
    public static final Item ABYSSAL_HOE = handheld("abyssal_hoe", new HoeItem(ModTiers.ABYSSAL, -6, 0, gear(true)), ItemTags.HOES);
    public static final Item ABYSSAL_SWORD = handheld("abyssal_sword", new SwordItem(ModTiers.ABYSSAL, 4, -2.4f, gear(true)), ItemTags.SWORDS);
    public static final Item ABYSSAL_HELMET = armor("abyssal_helmet", ModArmorMaterials.ABYSSAL, ArmorItem.Type.HELMET, true);
    public static final Item ABYSSAL_CHESTPLATE = armor("abyssal_chestplate", ModArmorMaterials.ABYSSAL, ArmorItem.Type.CHESTPLATE, true);
    public static final Item ABYSSAL_LEGGINGS = armor("abyssal_leggings", ModArmorMaterials.ABYSSAL, ArmorItem.Type.LEGGINGS, true);
    public static final Item ABYSSAL_BOOTS = armor("abyssal_boots", ModArmorMaterials.ABYSSAL, ArmorItem.Type.BOOTS, true);

    public static final Item RED_TITANIUM_SHOVEL = handheld("red_titanium_shovel", new ShovelItem(ModTiers.TITANIUM, 2.5f, -3f, gear(true)), ItemTags.SHOVELS);
    public static final Item RED_TITANIUM_PICKAXE = handheld("red_titanium_pickaxe", new PickaxeItem(ModTiers.TITANIUM, 2, -2.8f, gear(true)), ItemTags.PICKAXES);
    public static final Item RED_TITANIUM_AXE = handheld("red_titanium_axe", new AxeItem(ModTiers.TITANIUM, 6, -3f, gear(true)), ItemTags.AXES);
    public static final Item RED_TITANIUM_HOE = handheld("red_titanium_hoe", new HoeItem(ModTiers.TITANIUM, -6, 0, gear(true)), ItemTags.HOES);
    public static final Item RED_TITANIUM_SWORD = handheld("red_titanium_sword", new SwordItem(ModTiers.TITANIUM, 4, -2.4f, gear(true)), ItemTags.SWORDS);
    public static final Item RED_TITANIUM_HELMET = armor("red_titanium_helmet", ModArmorMaterials.TITANIUM, ArmorItem.Type.HELMET, true);
    public static final Item RED_TITANIUM_CHESTPLATE = armor("red_titanium_chestplate", ModArmorMaterials.TITANIUM, ArmorItem.Type.CHESTPLATE, true);
    public static final Item RED_TITANIUM_LEGGINGS = armor("red_titanium_leggings", ModArmorMaterials.TITANIUM, ArmorItem.Type.LEGGINGS, true);
    public static final Item RED_TITANIUM_BOOTS = armor("red_titanium_boots", ModArmorMaterials.TITANIUM, ArmorItem.Type.BOOTS, true);

    public static final Item AURIC_ARC = ModRegistry.ofItem("auric_arc",
            new AuricArcItem(9.0D, -2.8D, new FabricItemSettings().rarity(Rarity.EPIC).durability(ModTiers.AURIC_ARC.getUses()))).build();
    public static final Item CHIMERIC_ROCKBREAKER_HAMMER = ModRegistry.ofItem("chimeric_rockbreaker_hammer",
            new ChimericRockbreakerHammerItem(4, -3.1f, new FabricItemSettings().rarity(Rarity.EPIC))).tag(ItemTags.PICKAXES).build();
    public static final Item SHIELD_OF_CHIMERIC_WARRIOR = ModRegistry.ofItem("shield_of_chimeric_warrior",
            new ShieldOfChimericWarriorItem(new FabricItemSettings().rarity(Rarity.EPIC).stacksTo(1).durability(893))).tag(ConventionalItemTags.SHIELDS).build();
    public static final Item ECLIPSEBANE = ModRegistry.ofItem("eclipsebane",
            new EclipsebaneItem(4, -2.6f, new FabricItemSettings().rarity(Rarity.EPIC))).tag(ItemTags.SWORDS).build();
    public static final Item GRIPPING_ABYSSAL_BOW = ModRegistry.ofItem("gripping_abyssal_bow",
            new GrippingAbyssalBowItem(new FabricItemSettings().rarity(Rarity.RARE).durability(984))).tag(ConventionalItemTags.BOWS).build();

    public static final Item GRIPPING_BUNDLE = ModRegistry.ofItem("gripping_bundle",
            new GrippingBundleItem(new FabricItemSettings().rarity(Rarity.EPIC).stacksTo(1))).build();
    public static final Item GRIPPING_GAUNTLET = ModRegistry.ofItem("gripping_gauntlet",
            new GrippingGauntletItem(new FabricItemSettings().rarity(Rarity.EPIC).stacksTo(1))).build();

    public static final Item CRYSTIE_APPLE = food("crystie_apple", ModRegistry.Foods.CRYSTIE_APPLE);
    public static final Item GLOW_APPLE = food("glow_apple", ModRegistry.Foods.GLOW_APPLE);
    public static final Item BEARFRUIT_BRAMBLE = food("bearfruit_bramble", ModRegistry.Foods.BEARFRUIT_BRAMBLE);
    public static final Item NIGHTBERRY = food("nightberry", ModRegistry.Foods.NIGHTBERRY);
    public static final Item WARPEDVEIL_VINE_FRUIT = ModRegistry.ofItem("warpedveil_vine_fruit",
            new ItemNameBlockItem(ModBlocks.CRIMSONVEIL_VINE, new FabricItemSettings().rarity(Rarity.UNCOMMON).food(ModRegistry.Foods.WARPEDVEIL_VINE_FRUIT))).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item GLOWROOT_BULB = ModRegistry.ofItem("glowroot_bulb",
            new ItemNameBlockItem(ModBlocks.GLOWROOT_VINE, new FabricItemSettings().rarity(Rarity.UNCOMMON).food(ModRegistry.Foods.GLOWROOT_BULB))).tag(ModTags.Items.RIMEHORN_TAMING_ITEMS).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item RIMEHORN_MEAT = food("rimehorn_meat", ModRegistry.Foods.RIMEHORN_MEAT);
    public static final Item COOKED_RIMEHORN_MEAT = food("cooked_rimehorn_meat", ModRegistry.Foods.COOKED_RIMEHORN_MEAT);

    public static final Item GRIPCRYSTAL = crystal("gripcrystal");
    public static final Item GRANULATED_GRIPCRYSTAL = crystal("granulated_gripcrystal");
    public static final Item GRIPTONITE = ModRegistry.ofItem("griptonite",
            new GriptoniteItem(new FabricItemSettings().stacksTo(1).durability(4).rarity(Rarity.UNCOMMON))).tag(ModTags.Items.GRIPPING_BUNDLE_CAN_HANDLE).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item GRANULATED_GRIPTONITE = crystal("granulated_griptonite");

    public static final Item THICK_STRING = flat("thick_string", Rarity.UNCOMMON);
    public static final Item GRIPPING_MEMBRANE = ModRegistry.ofItem("gripping_membrane",
            new Item(new FabricItemSettings().rarity(Rarity.RARE))).tag(ModTags.Items.VEXAL_BETTLE_TAMING_ITEMS).model(ModelTemplates.FLAT_ITEM).build();

    public static final Item GRIPCRYSTAL_KEY = key("gripcrystal_key");
    public static final Item ABYSSAL_KEY = key("abyssal_key");
    public static final Item AURIC_KEY = key("auric_key");

    public static final Block AMETHYST_SIGN = standingSign("amethyst_sign", ModBlocks.AMETHYST, ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_WALL_SIGN = wallSign("amethyst_wall_sign", ModBlocks.AMETHYST);
    public static final Block AMETHYST_HANGING_SIGN = hangingSign("amethyst_hanging_sign", ModBlocks.AMETHYST, ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_WALL_HANGING_SIGN = wallHangingSign("amethyst_wall_hanging_sign", ModBlocks.AMETHYST, ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Item AMETHYST_SIGN_ITEM = signItem("amethyst_sign", AMETHYST_SIGN, AMETHYST_WALL_SIGN);
    public static final Item AMETHYST_HANGING_SIGN_ITEM = hangingSignItem("amethyst_hanging_sign", AMETHYST_HANGING_SIGN, AMETHYST_WALL_HANGING_SIGN);

    public static final Block GRIZZLY_SIGN = standingSign("grizzly_sign", ModBlocks.GRIZZLY, ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_WALL_SIGN = wallSign("grizzly_wall_sign", ModBlocks.GRIZZLY);
    public static final Block GRIZZLY_HANGING_SIGN = hangingSign("grizzly_hanging_sign", ModBlocks.GRIZZLY, ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_WALL_HANGING_SIGN = wallHangingSign("grizzly_wall_hanging_sign", ModBlocks.GRIZZLY, ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Item GRIZZLY_SIGN_ITEM = signItem("grizzly_sign", GRIZZLY_SIGN, GRIZZLY_WALL_SIGN);
    public static final Item GRIZZLY_HANGING_SIGN_ITEM = hangingSignItem("grizzly_hanging_sign", GRIZZLY_HANGING_SIGN, GRIZZLY_WALL_HANGING_SIGN);

    public static final Block TEALIVE_SIGN = standingSign("tealive_sign", ModBlocks.TEALIVE, ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_WALL_SIGN = wallSign("tealive_wall_sign", ModBlocks.TEALIVE);
    public static final Block TEALIVE_HANGING_SIGN = hangingSign("tealive_hanging_sign", ModBlocks.TEALIVE, ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_WALL_HANGING_SIGN = wallHangingSign("tealive_wall_hanging_sign", ModBlocks.TEALIVE, ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Item TEALIVE_SIGN_ITEM = signItem("tealive_sign", TEALIVE_SIGN, TEALIVE_WALL_SIGN);
    public static final Item TEALIVE_HANGING_SIGN_ITEM = hangingSignItem("tealive_hanging_sign", TEALIVE_HANGING_SIGN, TEALIVE_WALL_HANGING_SIGN);

    public static final Block BURLYWOOD_SIGN = standingSign("burlywood_sign", ModBlocks.BURLYWOOD, ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_WALL_SIGN = wallSign("burlywood_wall_sign", ModBlocks.BURLYWOOD);
    public static final Block BURLYWOOD_HANGING_SIGN = hangingSign("burlywood_hanging_sign", ModBlocks.BURLYWOOD, ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_WALL_HANGING_SIGN = wallHangingSign("burlywood_wall_hanging_sign", ModBlocks.BURLYWOOD, ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Item BURLYWOOD_SIGN_ITEM = signItem("burlywood_sign", BURLYWOOD_SIGN, BURLYWOOD_WALL_SIGN);
    public static final Item BURLYWOOD_HANGING_SIGN_ITEM = hangingSignItem("burlywood_hanging_sign", BURLYWOOD_HANGING_SIGN, BURLYWOOD_WALL_HANGING_SIGN);

    public static final Block CRIMSONVEIL_SIGN = standingSign("crimsonveil_sign", ModBlocks.CRIMSONVEIL, ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_WALL_SIGN = wallSign("crimsonveil_wall_sign", ModBlocks.CRIMSONVEIL);
    public static final Block CRIMSONVEIL_HANGING_SIGN = hangingSign("crimsonveil_hanging_sign", ModBlocks.CRIMSONVEIL, ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_WALL_HANGING_SIGN = wallHangingSign("crimsonveil_wall_hanging_sign", ModBlocks.CRIMSONVEIL, ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Item CRIMSONVEIL_SIGN_ITEM = signItem("crimsonveil_sign", CRIMSONVEIL_SIGN, CRIMSONVEIL_WALL_SIGN);
    public static final Item CRIMSONVEIL_HANGING_SIGN_ITEM = hangingSignItem("crimsonveil_hanging_sign", CRIMSONVEIL_HANGING_SIGN, CRIMSONVEIL_WALL_HANGING_SIGN);

    public static final Item DARK_WATER_BUCKET = ModRegistry.ofItem("dark_water_bucket",
            new DarkWaterBucketItem(ModFluids.DARK_WATER, new FabricItemSettings().craftRemainder(Items.BUCKET).stacksTo(1))).model(ModelTemplates.FLAT_ITEM).build();

    public static final Item DEAD_UMBRAL_CORAL_FAN_ITEM = ModRegistry.ofItem("dead_umbral_coral_fan",
            new StandingAndWallBlockItem(ModBlocks.DEAD_UMBRAL_CORAL_FAN, ModBlocks.DEAD_UMBRAL_CORAL_WALL_FAN, new FabricItemSettings(), Direction.DOWN)).build();
    public static final Item UMBRAL_CORAL_FAN_ITEM = ModRegistry.ofItem("umbral_coral_fan",
            new StandingAndWallBlockItem(ModBlocks.UMBRAL_CORAL_FAN, ModBlocks.UMBRAL_CORAL_WALL_FAN, new FabricItemSettings(), Direction.DOWN)).build();

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
    public static final Item VEXAL_BEETLE_SPAWN_EGG = spawnEgg("vexal_beetle_spawn_egg", ModEntities.VEXAL_BEETLE, 2430480, 8339378);
    public static final Item RIMEHORN_SPAWN_EGG = spawnEgg("rimehorn_spawn_egg", ModEntities.RIMEHORN, 1474182, 1711153);

    private static Item flat(String name, Rarity rarity) {
        return ModRegistry.ofItem(name, new Item(new FabricItemSettings().rarity(rarity))).model(ModelTemplates.FLAT_ITEM).build();
    }

    private static Item ingot(String name) {
        return ModRegistry.ofItem(name, new Item(new FabricItemSettings().rarity(Rarity.RARE))).tag(ConventionalItemTags.INGOTS).model(ModelTemplates.FLAT_ITEM).build();
    }

    private static Item crystal(String name) {
        return ModRegistry.ofItem(name, new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).tag(ModTags.Items.GRIPPING_BUNDLE_CAN_HANDLE).model(ModelTemplates.FLAT_ITEM).build();
    }

    private static Item key(String name) {
        return ModRegistry.ofItem(name, new Item(new FabricItemSettings().rarity(Rarity.RARE).stacksTo(1))).model(ModelTemplates.FLAT_ITEM).build();
    }

    private static Item food(String name, FoodProperties food) {
        return ModRegistry.ofItem(name, new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).food(food))).model(ModelTemplates.FLAT_ITEM).build();
    }

    private static Item.Properties gear(boolean fireResistant) {
        FabricItemSettings settings = new FabricItemSettings();
        return fireResistant ? settings.fireResistant() : settings;
    }

    private static Item handheld(String name, Item item, TagKey<Item> toolTag) {
        return ModRegistry.ofItem(name, item).model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(toolTag).build();
    }

    private static Item armor(String name, ArmorMaterial material, ArmorItem.Type type, boolean fireResistant) {
        FabricItemSettings settings = new FabricItemSettings().rarity(Rarity.RARE);
        return ModRegistry.ofItem(name, new ModArmorItem(material, type, fireResistant ? settings.fireResistant() : settings)).build();
    }

    private static Item spawnEgg(String name, EntityType<? extends Mob> type, int primaryColor, int secondaryColor) {
        return ModRegistry.ofItem(name, new SpawnEggItem(type, primaryColor, secondaryColor, new FabricItemSettings())).build();
    }

    private static FabricBlockSettings signSettings() {
        return FabricBlockSettings.copyOf(Blocks.OAK_SIGN);
    }

    private static Block standingSign(String name, WoodType wood, TagKey<Block> woodTag) {
        return ModRegistry.ofBlock(name, new StandingSignBlock(signSettings(), wood), false)
                .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, woodTag)
                .tool("_axe").build();
    }

    private static Block wallSign(String name, WoodType wood) {
        return ModRegistry.ofBlock(name, new WallSignBlock(signSettings(), wood), false)
                .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
                .tool("_axe").build();
    }

    private static Block hangingSign(String name, WoodType wood, TagKey<Block> woodTag) {
        return ModRegistry.ofBlock(name, new CeilingHangingSignBlock(signSettings(), wood), false)
                .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, woodTag)
                .tool("_axe").build();
    }

    private static Block wallHangingSign(String name, WoodType wood, TagKey<Block> woodTag) {
        return ModRegistry.ofBlock(name, new WallHangingSignBlock(signSettings(), wood), false)
                .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, woodTag)
                .tool("_axe").build();
    }

    private static Item signItem(String name, Block standing, Block wall) {
        return ModRegistry.ofItem(name, new SignItem(new FabricItemSettings(), standing, wall)).fuel(200).build();
    }

    private static Item hangingSignItem(String name, Block ceiling, Block wall) {
        return ModRegistry.ofItem(name, new HangingSignItem(ceiling, wall, new FabricItemSettings())).fuel(200).build();
    }
}