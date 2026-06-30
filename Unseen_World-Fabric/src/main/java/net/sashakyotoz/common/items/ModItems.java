package net.sashakyotoz.common.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.entities.ModEntities;
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
    }

    public static final Item ECLIPSE_KEYSTONE = ModRegistry.ofItem("eclipse_keystone",
            new Item(new FabricItemSettings().rarity(Rarity.EPIC))).model(ModelTemplates.FLAT_ITEM).build();
    //ore
    public static final Item RAW_UNSEENIUM_ORE = ModRegistry.ofItem("raw_unseenium",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item RAW_ABYSSAL_ORE = ModRegistry.ofItem("raw_abyssal",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item RAW_RED_TITANIUM_ORE = ModRegistry.ofItem("raw_red_titanium",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item UNSEENIUM_INGOT = ModRegistry.ofItem("unseenium_ingot",
            new Item(new FabricItemSettings().rarity(Rarity.RARE))).tag(ConventionalItemTags.INGOTS).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item ABYSSAL_INGOT = ModRegistry.ofItem("abyssal_ingot",
            new Item(new FabricItemSettings().rarity(Rarity.RARE))).tag(ConventionalItemTags.INGOTS).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item RED_TITANIUM_INGOT = ModRegistry.ofItem("red_titanium_ingot",
            new Item(new FabricItemSettings().rarity(Rarity.RARE))).tag(ConventionalItemTags.INGOTS).model(ModelTemplates.FLAT_ITEM).build();

    public static final Item UNSEENIUM_SHOVEL = ModRegistry.ofItem("unseenium_shovel",
                    new ShovelItem(ModTiers.UNSEENIUM, 2.5f, -3f, new FabricItemSettings()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.SHOVELS).build();
    public static final Item UNSEENIUM_PICKAXE = ModRegistry.ofItem("unseenium_pickaxe",
                    new PickaxeItem(ModTiers.UNSEENIUM, 2, -2.8f, new FabricItemSettings()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.PICKAXES).build();
    public static final Item UNSEENIUM_AXE = ModRegistry.ofItem("unseenium_axe",
                    new AxeItem(ModTiers.UNSEENIUM, 6, -3f, new FabricItemSettings()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.AXES).build();
    public static final Item UNSEENIUM_HOE = ModRegistry.ofItem("unseenium_hoe",
                    new HoeItem(ModTiers.UNSEENIUM, -5, 0, new FabricItemSettings()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.HOES).build();
    public static final Item UNSEENIUM_SWORD = ModRegistry.ofItem("unseenium_sword",
                    new SwordItem(ModTiers.UNSEENIUM, 4, -2.4f, new FabricItemSettings()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.SWORDS).build();
    public static final Item UNSEENIUM_HELMET = ModRegistry.ofItem("unseenium_helmet",
            new ModArmorItem(ModArmorMaterials.UNSEENIUM, ArmorItem.Type.HELMET,
                    new FabricItemSettings().rarity(Rarity.RARE))).build();
    public static final Item UNSEENIUM_CHESTPLATE = ModRegistry.ofItem("unseenium_chestplate",
            new ModArmorItem(ModArmorMaterials.UNSEENIUM, ArmorItem.Type.CHESTPLATE,
                    new FabricItemSettings().rarity(Rarity.RARE))).build();
    public static final Item UNSEENIUM_LEGGINGS = ModRegistry.ofItem("unseenium_leggings",
            new ModArmorItem(ModArmorMaterials.UNSEENIUM, ArmorItem.Type.LEGGINGS,
                    new FabricItemSettings().rarity(Rarity.RARE))).build();
    public static final Item UNSEENIUM_BOOTS = ModRegistry.ofItem("unseenium_boots",
            new ModArmorItem(ModArmorMaterials.UNSEENIUM, ArmorItem.Type.BOOTS,
                    new FabricItemSettings().rarity(Rarity.RARE))).build();

    public static final Item ABYSSAL_SHOVEL = ModRegistry.ofItem("abyssal_shovel",
                    new ShovelItem(ModTiers.ABYSSAL, 2.5f, -3f, new FabricItemSettings().fireResistant()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.SHOVELS).build();
    public static final Item ABYSSAL_PICKAXE = ModRegistry.ofItem("abyssal_pickaxe",
                    new PickaxeItem(ModTiers.ABYSSAL, 2, -2.8f, new FabricItemSettings().fireResistant()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.PICKAXES).build();
    public static final Item ABYSSAL_AXE = ModRegistry.ofItem("abyssal_axe",
                    new AxeItem(ModTiers.ABYSSAL, 6, -3f, new FabricItemSettings().fireResistant()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.AXES).build();
    public static final Item ABYSSAL_HOE = ModRegistry.ofItem("abyssal_hoe",
                    new HoeItem(ModTiers.ABYSSAL, -6, 0, new FabricItemSettings().fireResistant()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.HOES).build();
    public static final Item ABYSSAL_SWORD = ModRegistry.ofItem("abyssal_sword",
                    new SwordItem(ModTiers.ABYSSAL, 4, -2.4f, new FabricItemSettings().fireResistant()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.SWORDS).build();
    public static final Item ABYSSAL_HELMET = ModRegistry.ofItem("abyssal_helmet",
            new ModArmorItem(ModArmorMaterials.ABYSSAL, ArmorItem.Type.HELMET,
                    new FabricItemSettings().rarity(Rarity.RARE).fireResistant())).build();
    public static final Item ABYSSAL_CHESTPLATE = ModRegistry.ofItem("abyssal_chestplate",
            new ModArmorItem(ModArmorMaterials.ABYSSAL, ArmorItem.Type.CHESTPLATE,
                    new FabricItemSettings().rarity(Rarity.RARE).fireResistant())).build();
    public static final Item ABYSSAL_LEGGINGS = ModRegistry.ofItem("abyssal_leggings",
            new ModArmorItem(ModArmorMaterials.ABYSSAL, ArmorItem.Type.LEGGINGS,
                    new FabricItemSettings().rarity(Rarity.RARE).fireResistant())).build();
    public static final Item ABYSSAL_BOOTS = ModRegistry.ofItem("abyssal_boots",
            new ModArmorItem(ModArmorMaterials.ABYSSAL, ArmorItem.Type.BOOTS,
                    new FabricItemSettings().rarity(Rarity.RARE).fireResistant())).build();

    public static final Item RED_TITANIUM_SHOVEL = ModRegistry.ofItem("red_titanium_shovel",
                    new ShovelItem(ModTiers.TITANIUM, 2.5f, -3f, new FabricItemSettings().fireResistant()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.SHOVELS).build();
    public static final Item RED_TITANIUM_PICKAXE = ModRegistry.ofItem("red_titanium_pickaxe",
                    new PickaxeItem(ModTiers.TITANIUM, 2, -2.8f, new FabricItemSettings().fireResistant()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.PICKAXES).build();
    public static final Item RED_TITANIUM_AXE = ModRegistry.ofItem("red_titanium_axe",
                    new AxeItem(ModTiers.TITANIUM, 6, -3f, new FabricItemSettings().fireResistant()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.AXES).build();
    public static final Item RED_TITANIUM_HOE = ModRegistry.ofItem("red_titanium_hoe",
                    new HoeItem(ModTiers.TITANIUM, -6, 0, new FabricItemSettings().fireResistant()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.HOES).build();
    public static final Item RED_TITANIUM_SWORD = ModRegistry.ofItem("red_titanium_sword",
                    new SwordItem(ModTiers.TITANIUM, 4, -2.4f, new FabricItemSettings().fireResistant()))
            .model(ModelTemplates.FLAT_HANDHELD_ITEM).tag(ItemTags.SWORDS).build();
    public static final Item RED_TITANIUM_HELMET = ModRegistry.ofItem("red_titanium_helmet",
            new ModArmorItem(ModArmorMaterials.TITANIUM, ArmorItem.Type.HELMET,
                    new FabricItemSettings().rarity(Rarity.RARE).fireResistant())).build();
    public static final Item RED_TITANIUM_CHESTPLATE = ModRegistry.ofItem("red_titanium_chestplate",
            new ModArmorItem(ModArmorMaterials.TITANIUM, ArmorItem.Type.CHESTPLATE,
                    new FabricItemSettings().rarity(Rarity.RARE).fireResistant())).build();
    public static final Item RED_TITANIUM_LEGGINGS = ModRegistry.ofItem("red_titanium_leggings",
            new ModArmorItem(ModArmorMaterials.TITANIUM, ArmorItem.Type.LEGGINGS,
                    new FabricItemSettings().rarity(Rarity.RARE).fireResistant())).build();
    public static final Item RED_TITANIUM_BOOTS = ModRegistry.ofItem("red_titanium_boots",
            new ModArmorItem(ModArmorMaterials.TITANIUM, ArmorItem.Type.BOOTS,
                    new FabricItemSettings().rarity(Rarity.RARE).fireResistant())).build();
    //bosses' stuff
    public static final Item CHIMERIC_ROCKBREAKER_HAMMER = ModRegistry.ofItem("chimeric_rockbreaker_hammer",
            new ChimericRockbreakerHammerItem(4, -3.1f, new FabricItemSettings().rarity(Rarity.EPIC))).build();
    public static final Item SHIELD_OF_CHIMERIC_WARRIOR = ModRegistry.ofItem("shield_of_chimeric_warrior",
            new ShieldOfChimericWarriorItem(new FabricItemSettings().rarity(Rarity.EPIC).stacksTo(1).durability(893))).tag(ConventionalItemTags.SHIELDS).build();
    public static final Item ECLIPSEBANE = ModRegistry.ofItem("eclipsebane",
            new EclipsebaneItem(ModTiers.ECLIPSEBANE, 4, -2.5f, new FabricItemSettings().rarity(Rarity.EPIC))).build();
    public static final Item GRIPPING_ABYSSAL_BOW = ModRegistry.ofItem("gripping_abyssal_bow",
            new GrippingAbyssalBowItem(new FabricItemSettings().rarity(Rarity.RARE).durability(984))).tag(ConventionalItemTags.BOWS).build();
    //artifacts
    public static final Item GRIPPING_BUNDLE = ModRegistry.ofItem("gripping_bundle", new GrippingBundleItem(new FabricItemSettings().rarity(Rarity.EPIC).stacksTo(1))).build();
    public static final Item GRIPPING_GAUNTLET = ModRegistry.ofItem("gripping_gauntlet", new GrippingGauntletItem(new FabricItemSettings().rarity(Rarity.EPIC).stacksTo(1))).build();
    //food
    public static final Item CRYSTIE_APPLE = ModRegistry.ofItem("crystie_apple",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).food(ModRegistry.Foods.CRYSTIE_APPLE))).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item GLOW_APPLE = ModRegistry.ofItem("glow_apple",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).food(ModRegistry.Foods.GLOW_APPLE))).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item BEARFRUIT_BRAMBLE = ModRegistry.ofItem("bearfruit_bramble",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).food(ModRegistry.Foods.BEARFRUIT_BRAMBLE))).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item NIGHTBERRY = ModRegistry.ofItem("nightberry",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).food(ModRegistry.Foods.NIGHTBERRY))).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item WARPEDVEIL_VINE_FRUIT = ModRegistry.ofItem("warpedveil_vine_fruit",
            new ItemNameBlockItem(ModBlocks.CRIMSONVEIL_VINE, new FabricItemSettings().rarity(Rarity.UNCOMMON).food(ModRegistry.Foods.WARPEDVEIL_VINE_FRUIT))).model(ModelTemplates.FLAT_ITEM).build();
    //crystals
    public static final Item GRIPCRYSTAL = ModRegistry.ofItem("gripcrystal",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).tag(ModTags.Items.GRIPPING_BUNDLE_CAN_HANDLE).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item GRANULATED_GRIPCRYSTAL = ModRegistry.ofItem("granulated_gripcrystal",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).tag(ModTags.Items.GRIPPING_BUNDLE_CAN_HANDLE).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item GRIPTONITE = ModRegistry.ofItem("griptonite",
            new GriptoniteItem(new FabricItemSettings().stacksTo(1).durability(4).rarity(Rarity.UNCOMMON))).tag(ModTags.Items.GRIPPING_BUNDLE_CAN_HANDLE).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item GRANULATED_GRIPTONITE = ModRegistry.ofItem("granulated_griptonite",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).tag(ModTags.Items.GRIPPING_BUNDLE_CAN_HANDLE).model(ModelTemplates.FLAT_ITEM).build();
    //materials
    public static final Item THICK_STRING = ModRegistry.ofItem("thick_string",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).model(ModelTemplates.FLAT_ITEM).build();
    //keys
    public static final Item GRIPCRYSTAL_KEY = ModRegistry.ofItem("gripcrystal_key",
            new Item(new FabricItemSettings().rarity(Rarity.RARE).stacksTo(1))).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item ABYSSAL_KEY = ModRegistry.ofItem("abyssal_key",
            new Item(new FabricItemSettings().rarity(Rarity.RARE).stacksTo(1))).model(ModelTemplates.FLAT_ITEM).build();
    public static final Item AURIC_KEY = ModRegistry.ofItem("auric_key",
            new Item(new FabricItemSettings().rarity(Rarity.RARE).stacksTo(1))).model(ModelTemplates.FLAT_ITEM).build();
    //signs
    private static FabricBlockSettings copy(FabricBlockSettings original) {
        return FabricBlockSettings.copyOf(original);
    }

    //amethyst
    private static final FabricBlockSettings PassableTreeMaterial = FabricBlockSettings.copyOf(Blocks.OAK_SIGN);
    public static final Block AMETHYST_SIGN = ModRegistry.ofBlock("amethyst_sign",
                    new StandingSignBlock(PassableTreeMaterial, ModBlocks.AMETHYST), false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.AMETHYST_BLOCKS)
            .tool("_axe").build();
    public static final Block AMETHYST_WALL_SIGN = ModRegistry.ofBlock("amethyst_wall_sign",
                    new WallSignBlock(PassableTreeMaterial, ModBlocks.AMETHYST), false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").build();
    public static final Block AMETHYST_HANGING_SIGN = ModRegistry.ofBlock("amethyst_hanging_sign",
                    new CeilingHangingSignBlock(PassableTreeMaterial, ModBlocks.AMETHYST), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.AMETHYST_BLOCKS)
            .tool("_axe").build();
    public static final Block AMETHYST_WALL_HANGING_SIGN = ModRegistry.ofBlock("amethyst_wall_hanging_sign",
                    new WallHangingSignBlock(copy(PassableTreeMaterial), ModBlocks.AMETHYST), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, ModTags.Blocks.AMETHYST_BLOCKS)
            .tool("_axe").build();
    public static final Item AMETHYST_SIGN_ITEM = ModRegistry.ofItem("amethyst_sign",
                    new SignItem(new FabricItemSettings(), AMETHYST_SIGN, AMETHYST_WALL_SIGN))
            .fuel(200).build();
    public static final Item AMETHYST_HANGING_SIGN_ITEM = ModRegistry.ofItem("amethyst_hanging_sign",
                    new HangingSignItem(AMETHYST_HANGING_SIGN, AMETHYST_WALL_HANGING_SIGN, new FabricItemSettings()))
            .fuel(200).build();
    //grizzly
    public static final Block GRIZZLY_SIGN = ModRegistry.ofBlock("grizzly_sign",
                    new StandingSignBlock(PassableTreeMaterial, ModBlocks.GRIZZLY), false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.GRIZZLY_BLOCKS)
            .tool("_axe").build();
    public static final Block GRIZZLY_WALL_SIGN = ModRegistry.ofBlock("grizzly_wall_sign",
                    new WallSignBlock(PassableTreeMaterial, ModBlocks.GRIZZLY), false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").build();
    public static final Block GRIZZLY_HANGING_SIGN = ModRegistry.ofBlock("grizzly_hanging_sign",
                    new CeilingHangingSignBlock(PassableTreeMaterial, ModBlocks.GRIZZLY), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.GRIZZLY_BLOCKS)
            .tool("_axe").build();
    public static final Block GRIZZLY_WALL_HANGING_SIGN = ModRegistry.ofBlock("grizzly_wall_hanging_sign",
                    new WallHangingSignBlock(copy(PassableTreeMaterial), ModBlocks.GRIZZLY), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, ModTags.Blocks.GRIZZLY_BLOCKS)
            .tool("_axe").build();
    public static final Item GRIZZLY_SIGN_ITEM = ModRegistry.ofItem("grizzly_sign",
                    new SignItem(new FabricItemSettings(), GRIZZLY_SIGN, GRIZZLY_WALL_SIGN))
            .fuel(200).build();
    public static final Item GRIZZLY_HANGING_SIGN_ITEM = ModRegistry.ofItem("grizzly_hanging_sign",
                    new HangingSignItem(GRIZZLY_HANGING_SIGN, GRIZZLY_WALL_HANGING_SIGN, new FabricItemSettings()))
            .fuel(200).build();
    //tealive
    public static final Block TEALIVE_SIGN = ModRegistry.ofBlock("tealive_sign",
                    new StandingSignBlock(PassableTreeMaterial, ModBlocks.TEALIVE), false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.TEALIVE_BLOCKS)
            .tool("_axe").build();
    public static final Block TEALIVE_WALL_SIGN = ModRegistry.ofBlock("tealive_wall_sign",
                    new WallSignBlock(PassableTreeMaterial, ModBlocks.TEALIVE), false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").build();
    public static final Block TEALIVE_HANGING_SIGN = ModRegistry.ofBlock("tealive_hanging_sign",
                    new CeilingHangingSignBlock(PassableTreeMaterial, ModBlocks.TEALIVE), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.TEALIVE_BLOCKS)
            .tool("_axe").build();
    public static final Block TEALIVE_WALL_HANGING_SIGN = ModRegistry.ofBlock("tealive_wall_hanging_sign",
                    new WallHangingSignBlock(copy(PassableTreeMaterial), ModBlocks.TEALIVE), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, ModTags.Blocks.TEALIVE_BLOCKS)
            .tool("_axe").build();
    public static final Item TEALIVE_SIGN_ITEM = ModRegistry.ofItem("tealive_sign",
                    new SignItem(new FabricItemSettings(), TEALIVE_SIGN, TEALIVE_WALL_SIGN))
            .fuel(200).build();
    public static final Item TEALIVE_HANGING_SIGN_ITEM = ModRegistry.ofItem("tealive_hanging_sign",
                    new HangingSignItem(TEALIVE_HANGING_SIGN, TEALIVE_WALL_HANGING_SIGN, new FabricItemSettings()))
            .fuel(200).build();
    //burlywood
    public static final Block BURLYWOOD_SIGN = ModRegistry.ofBlock("burlywood_sign",
                    new StandingSignBlock(PassableTreeMaterial, ModBlocks.BURLYWOOD), false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.BURLYWOOD_BLOCKS)
            .tool("_axe").build();
    public static final Block BURLYWOOD_WALL_SIGN = ModRegistry.ofBlock("burlywood_wall_sign",
                    new WallSignBlock(PassableTreeMaterial, ModBlocks.BURLYWOOD), false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").build();
    public static final Block BURLYWOOD_HANGING_SIGN = ModRegistry.ofBlock("burlywood_hanging_sign",
                    new CeilingHangingSignBlock(PassableTreeMaterial, ModBlocks.BURLYWOOD), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.BURLYWOOD_BLOCKS)
            .tool("_axe").build();
    public static final Block BURLYWOOD_WALL_HANGING_SIGN = ModRegistry.ofBlock("burlywood_wall_hanging_sign",
                    new WallHangingSignBlock(copy(PassableTreeMaterial), ModBlocks.BURLYWOOD), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, ModTags.Blocks.BURLYWOOD_BLOCKS)
            .tool("_axe").build();
    public static final Item BURLYWOOD_SIGN_ITEM = ModRegistry.ofItem("burlywood_sign",
                    new SignItem(new FabricItemSettings(), BURLYWOOD_SIGN, BURLYWOOD_WALL_SIGN))
            .fuel(200).build();
    public static final Item BURLYWOOD_HANGING_SIGN_ITEM = ModRegistry.ofItem("burlywood_hanging_sign",
                    new HangingSignItem(BURLYWOOD_HANGING_SIGN, BURLYWOOD_WALL_HANGING_SIGN, new FabricItemSettings()))
            .fuel(200).build();
    //crimsonveil
    public static final Block CRIMSONVEIL_SIGN = ModRegistry.ofBlock("crimsonveil_sign",
                    new StandingSignBlock(PassableTreeMaterial, ModBlocks.CRIMSONVEIL), false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.CRIMSONVEIL_BLOCKS)
            .tool("_axe").build();
    public static final Block CRIMSONVEIL_WALL_SIGN = ModRegistry.ofBlock("crimsonveil_wall_sign",
                    new WallSignBlock(PassableTreeMaterial, ModBlocks.CRIMSONVEIL), false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").build();
    public static final Block CRIMSONVEIL_HANGING_SIGN = ModRegistry.ofBlock("crimsonveil_hanging_sign",
                    new CeilingHangingSignBlock(PassableTreeMaterial, ModBlocks.CRIMSONVEIL), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.CRIMSONVEIL_BLOCKS)
            .tool("_axe").build();
    public static final Block CRIMSONVEIL_WALL_HANGING_SIGN = ModRegistry.ofBlock("crimsonveil_wall_hanging_sign",
                    new WallHangingSignBlock(copy(PassableTreeMaterial), ModBlocks.CRIMSONVEIL), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, ModTags.Blocks.CRIMSONVEIL_BLOCKS)
            .tool("_axe").build();
    public static final Item CRIMSONVEIL_SIGN_ITEM = ModRegistry.ofItem("crimsonveil_sign",
                    new SignItem(new FabricItemSettings(), CRIMSONVEIL_SIGN, CRIMSONVEIL_WALL_SIGN))
            .fuel(200).build();
    public static final Item CRIMSONVEIL_HANGING_SIGN_ITEM = ModRegistry.ofItem("crimsonveil_hanging_sign",
                    new HangingSignItem(CRIMSONVEIL_HANGING_SIGN, CRIMSONVEIL_WALL_HANGING_SIGN, new FabricItemSettings()))
            .fuel(200).build();
    //buckets
    public static final Item DARK_WATER_BUCKET = ModRegistry.ofItem("dark_water_bucket",
            new BucketItem(ModFluids.DARK_WATER, new FabricItemSettings().craftRemainder(Items.BUCKET).stacksTo(1))).model(ModelTemplates.FLAT_ITEM).build();
    //spawn eggs
    public static final Item GLEAMCARVER_SPAWN_EGG = ModRegistry.ofItem("gleamcarver_spawn_egg",
                    new SpawnEggItem(ModEntities.GLEAMCARVER, 9787244, 3746083, new FabricItemSettings()))
            .build();
    public static final Item HARMONY_WATCHER_SPAWN_EGG = ModRegistry.ofItem("harmony_watcher_spawn_egg",
                    new SpawnEggItem(ModEntities.HARMONY_WATCHER, 5013401, 10066329, new FabricItemSettings()))
            .build();
    public static final Item VIOLEGER_SPAWN_EGG = ModRegistry.ofItem("violeger_spawn_egg",
                    new SpawnEggItem(ModEntities.VIOLEGER, 12801229, 6035741, new FabricItemSettings()))
            .build();
    public static final Item VENOMER_SPAWN_EGG = ModRegistry.ofItem("venomer_spawn_egg",
                    new SpawnEggItem(ModEntities.VENOMER, 12801229, 9715553, new FabricItemSettings()))
            .build();
    public static final Item GLOOMWHALE_SPAWN_EGG = ModRegistry.ofItem("gloomwhale_spawn_egg",
                    new SpawnEggItem(ModEntities.GLOOMWHALE, 4030943, 1644825, new FabricItemSettings()))
            .build();
    public static final Item GRIPPING_GLOOMWHALE_SPAWN_EGG = ModRegistry.ofItem("gripping_gloomwhale_spawn_egg",
                    new SpawnEggItem(ModEntities.GRIPPING_GLOOMWHALE, 4030943, 1643895, new FabricItemSettings()))
            .build();
    public static final Item SHIMMER_SPAWN_EGG = ModRegistry.ofItem("shimmer_spawn_egg",
                    new SpawnEggItem(ModEntities.SHIMMER, 9131142, 8278982, new FabricItemSettings()))
            .build();
    public static final Item SABERPARD_SPAWN_EGG = ModRegistry.ofItem("saberpard_spawn_egg",
                    new SpawnEggItem(ModEntities.SABERPARD, 16445005, 1644825, new FabricItemSettings()))
            .build();
    public static final Item ESPYER_SPAWN_EGG = ModRegistry.ofItem("espyer_spawn_egg",
                    new SpawnEggItem(ModEntities.ESPYER, 10066329, 1474182, new FabricItemSettings()))
            .build();
    public static final Item ELDRITCH_WATCHER_SPAWN_EGG = ModRegistry.ofItem("eldritch_watcher_spawn_egg",
                    new SpawnEggItem(ModEntities.ELDRITCH_WATCHER, 5013401, 0, new FabricItemSettings()))
            .build();
    public static final Item TUSKHOG_SPAWN_EGG = ModRegistry.ofItem("tuskhog_spawn_egg",
                    new SpawnEggItem(ModEntities.TUSKHOG, 15771042, 5000268, new FabricItemSettings()))
            .build();
    public static final Item DREADFLAP_SPAWN_EGG = ModRegistry.ofItem("dreadflap_spawn_egg",
                    new SpawnEggItem(ModEntities.DREADFLAP, 1644825, 8476209, new FabricItemSettings()))
            .build();
    public static final Item REAVER_SPAWN_EGG = ModRegistry.ofItem("reaver_spawn_egg",
                    new SpawnEggItem(ModEntities.REAVER, 8278982, 1711153, new FabricItemSettings()))
            .build();
}