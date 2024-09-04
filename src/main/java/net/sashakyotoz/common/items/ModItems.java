package net.sashakyotoz.common.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
<<<<<<< Updated upstream
=======
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
>>>>>>> Stashed changes
import net.minecraft.data.client.Models;
import net.minecraft.item.BucketItem;
import net.minecraft.item.HangingSignItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
<<<<<<< Updated upstream
=======
import net.minecraft.registry.tag.BlockTags;
>>>>>>> Stashed changes
import net.minecraft.util.Rarity;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
<<<<<<< Updated upstream
=======
import net.sashakyotoz.common.tags.ModTags;
>>>>>>> Stashed changes

public class ModItems {
    public static void register() {
        UnseenWorld.log("Registering Items for modid : " + UnseenWorld.MOD_ID);
    }
    public static final Item ECLIPSE_KEYSTONE = ModRegistry.ofItem("eclipse_keystone",
            new Item(new FabricItemSettings().rarity(Rarity.EPIC))).model(Models.GENERATED).build();
    //ore
    public static final Item RAW_ABYSSAL_ORE = ModRegistry.ofItem("raw_abyssal",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).model(Models.GENERATED).build();
    public static final Item RAW_RED_TITANIUM_ORE = ModRegistry.ofItem("raw_red_titanium",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).model(Models.GENERATED).build();
    public static final Item RAW_UNSEENIUM_ORE = ModRegistry.ofItem("raw_unseenium",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).model(Models.GENERATED).build();
    public static final Item ABYSSAL_INGOT = ModRegistry.ofItem("abyssal_ingot",
            new Item(new FabricItemSettings().rarity(Rarity.RARE))).model(Models.GENERATED).build();
    public static final Item RED_TITANIUM_INGOT = ModRegistry.ofItem("red_titanium_ingot",
            new Item(new FabricItemSettings().rarity(Rarity.RARE))).model(Models.GENERATED).build();
    public static final Item UNSEENIUM_INGOT = ModRegistry.ofItem("unseenium_ingot",
            new Item(new FabricItemSettings().rarity(Rarity.RARE))).model(Models.GENERATED).build();
    //food
    public static final Item CRYSTIE_APPLE = ModRegistry.ofItem("crystie_apple",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).food(ModRegistry.Foods.CRYSTIE_APPLE))).model(Models.GENERATED).build();
<<<<<<< Updated upstream
    //wood items
    public static final Item AMETHYST_SIGN = ModRegistry.ofItem("amethyst_sign",
                    new SignItem(new FabricItemSettings(), ModBlocks.AMETHYST_SIGN, ModBlocks.AMETHYST_WALL_SIGN))
            .fuel(200).build();
    public static final Item AMETHYST_HANGING_SIGN = ModRegistry.ofItem("amethyst_hanging_sign",
                    new HangingSignItem(ModBlocks.AMETHYST_HANGING_SIGN, ModBlocks.AMETHYST_WALL_HANGING_SIGN, new FabricItemSettings()))
=======
    //signs
    private static FabricBlockSettings copy(FabricBlockSettings original) {
        return FabricBlockSettings.copyOf(original);
    }
    //amethyst
    private static final FabricBlockSettings PassableTreeMaterial = FabricBlockSettings.copyOf(Blocks.OAK_SIGN);
    public static final Block AMETHYST_SIGN = ModRegistry.ofBlock("amethyst_sign",
                    new SignBlock(PassableTreeMaterial, ModBlocks.AMETHYST),false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.AMETHYST_BLOCKS)
            .tool("_axe").build();
    public static final Block AMETHYST_WALL_SIGN = ModRegistry.ofBlock("amethyst_wall_sign",
                    new WallSignBlock(PassableTreeMaterial, ModBlocks.AMETHYST),false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").build();
    public static final Block AMETHYST_HANGING_SIGN = ModRegistry.ofBlock("amethyst_hanging_sign",
                    new HangingSignBlock(PassableTreeMaterial, ModBlocks.AMETHYST),false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.AMETHYST_BLOCKS)
            .tool("_axe").build();
    public static final Block AMETHYST_WALL_HANGING_SIGN = ModRegistry.ofBlock("amethyst_wall_hanging_sign",
                    new WallHangingSignBlock(copy(PassableTreeMaterial), ModBlocks.AMETHYST),false)
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
                    new SignBlock(PassableTreeMaterial, ModBlocks.GRIZZLY),false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.GRIZZLY_BLOCKS)
            .tool("_axe").build();
    public static final Block GRIZZLY_WALL_SIGN = ModRegistry.ofBlock("grizzly_wall_sign",
                    new WallSignBlock(PassableTreeMaterial, ModBlocks.GRIZZLY),false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").build();
    public static final Block GRIZZLY_HANGING_SIGN = ModRegistry.ofBlock("grizzly_hanging_sign",
                    new HangingSignBlock(PassableTreeMaterial, ModBlocks.GRIZZLY),false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.GRIZZLY_BLOCKS)
            .tool("_axe").build();
    public static final Block GRIZZLY_WALL_HANGING_SIGN = ModRegistry.ofBlock("grizzly_wall_hanging_sign",
                    new WallHangingSignBlock(copy(PassableTreeMaterial), ModBlocks.GRIZZLY),false)
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
                    new SignBlock(PassableTreeMaterial, ModBlocks.TEALIVE),false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.TEALIVE_BLOCKS)
            .tool("_axe").build();
    public static final Block TEALIVE_WALL_SIGN = ModRegistry.ofBlock("tealive_wall_sign",
                    new WallSignBlock(PassableTreeMaterial, ModBlocks.TEALIVE),false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").build();
    public static final Block TEALIVE_HANGING_SIGN = ModRegistry.ofBlock("tealive_hanging_sign",
                    new HangingSignBlock(PassableTreeMaterial, ModBlocks.TEALIVE),false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.TEALIVE_BLOCKS)
            .tool("_axe").build();
    public static final Block TEALIVE_WALL_HANGING_SIGN = ModRegistry.ofBlock("tealive_wall_hanging_sign",
                    new WallHangingSignBlock(copy(PassableTreeMaterial), ModBlocks.TEALIVE),false)
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
                    new SignBlock(PassableTreeMaterial, ModBlocks.BURLYWOOD),false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.BURLYWOOD_BLOCKS)
            .tool("_axe").build();
    public static final Block BURLYWOOD_WALL_SIGN = ModRegistry.ofBlock("burlywood_wall_sign",
                    new WallSignBlock(PassableTreeMaterial, ModBlocks.BURLYWOOD),false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").build();
    public static final Block BURLYWOOD_HANGING_SIGN = ModRegistry.ofBlock("burlywood_hanging_sign",
                    new HangingSignBlock(PassableTreeMaterial, ModBlocks.BURLYWOOD),false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.BURLYWOOD_BLOCKS)
            .tool("_axe").build();
    public static final Block BURLYWOOD_WALL_HANGING_SIGN = ModRegistry.ofBlock("burlywood_wall_hanging_sign",
                    new WallHangingSignBlock(copy(PassableTreeMaterial), ModBlocks.BURLYWOOD),false)
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
                    new SignBlock(PassableTreeMaterial, ModBlocks.CRIMSONVEIL),false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.CRIMSONVEIL_BLOCKS)
            .tool("_axe").build();
    public static final Block CRIMSONVEIL_WALL_SIGN = ModRegistry.ofBlock("crimsonveil_wall_sign",
                    new WallSignBlock(PassableTreeMaterial, ModBlocks.CRIMSONVEIL),false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").build();
    public static final Block CRIMSONVEIL_HANGING_SIGN = ModRegistry.ofBlock("crimsonveil_hanging_sign",
                    new HangingSignBlock(PassableTreeMaterial, ModBlocks.CRIMSONVEIL),false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.CRIMSONVEIL_BLOCKS)
            .tool("_axe").build();
    public static final Block CRIMSONVEIL_WALL_HANGING_SIGN = ModRegistry.ofBlock("crimsonveil_wall_hanging_sign",
                    new WallHangingSignBlock(copy(PassableTreeMaterial), ModBlocks.CRIMSONVEIL),false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, ModTags.Blocks.CRIMSONVEIL_BLOCKS)
            .tool("_axe").build();
    public static final Item CRIMSONVEIL_SIGN_ITEM = ModRegistry.ofItem("crimsonveil_sign",
                    new SignItem(new FabricItemSettings(), CRIMSONVEIL_SIGN, CRIMSONVEIL_WALL_SIGN))
            .fuel(200).build();
    public static final Item CRIMSONVEIL_HANGING_SIGN_ITEM = ModRegistry.ofItem("crimsonveil_hanging_sign",
                    new HangingSignItem(CRIMSONVEIL_HANGING_SIGN, CRIMSONVEIL_WALL_HANGING_SIGN, new FabricItemSettings()))
>>>>>>> Stashed changes
            .fuel(200).build();
    //buckets
    public static final Item DARK_WATER_BUCKET = ModRegistry.ofItem("dark_water_bucket",
            new BucketItem(ModBlocks.DARK_WATER,new FabricItemSettings())).model(Models.GENERATED).build();
}