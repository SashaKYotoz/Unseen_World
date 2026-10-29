package net.sashakyotoz.common.datagen.recipes;

import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.datagen.SRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModWoodFamily;
import net.sashakyotoz.common.items.ModItems;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends SRecipeProvider {
    private static final Map<Block, Block> STONECUTTER_CHAINS = Map.of(
            ModBlocks.DARK_CURRANTSLATE, ModBlocks.DARK_CURRANTSLATE_BRICKS,
            ModBlocks.GLACIEMITE, ModBlocks.GLACIEMITE_BRICKS,
            ModBlocks.CORONA_STONE, ModBlocks.CORONA_STONE_BRICKS,
            ModBlocks.TANZANITE_BLOCK, ModBlocks.POLISHED_TANZANITE
    );

    private static final List<Gear> GEAR = List.of(
            new Gear(ModItems.UNSEENIUM_INGOT, ModItems.UNSEENIUM_HELMET, ModItems.UNSEENIUM_CHESTPLATE, ModItems.UNSEENIUM_LEGGINGS, ModItems.UNSEENIUM_BOOTS,
                    ModItems.UNSEENIUM_SWORD, ModItems.UNSEENIUM_PICKAXE, ModItems.UNSEENIUM_AXE, ModItems.UNSEENIUM_SHOVEL, ModItems.UNSEENIUM_HOE),
            new Gear(ModItems.ABYSSAL_INGOT, ModItems.ABYSSAL_HELMET, ModItems.ABYSSAL_CHESTPLATE, ModItems.ABYSSAL_LEGGINGS, ModItems.ABYSSAL_BOOTS,
                    ModItems.ABYSSAL_SWORD, ModItems.ABYSSAL_PICKAXE, ModItems.ABYSSAL_AXE, ModItems.ABYSSAL_SHOVEL, ModItems.ABYSSAL_HOE),
            new Gear(ModItems.RED_TITANIUM_INGOT, ModItems.RED_TITANIUM_HELMET, ModItems.RED_TITANIUM_CHESTPLATE, ModItems.RED_TITANIUM_LEGGINGS, ModItems.RED_TITANIUM_BOOTS,
                    ModItems.RED_TITANIUM_SWORD, ModItems.RED_TITANIUM_PICKAXE, ModItems.RED_TITANIUM_AXE, ModItems.RED_TITANIUM_SHOVEL, ModItems.RED_TITANIUM_HOE)
    );

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        super.buildRecipes(exporter);

        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            if (holder.getID().getNamespace().equals(UnseenWorld.MOD_ID)) {
                setMembers(exporter, holder);
            }
        });
        STONECUTTER_CHAINS.forEach((source, target) -> {
            stonecutting(exporter, source, holderOf(target));
            twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, target, source);
        });
        ModWoodFamily.ALL.forEach(family -> woodFamily(exporter, family));
        GEAR.forEach(gear -> gear(exporter, gear));

        SpecialRecipeBuilder.special(WarriorShieldDecorationRecipe::new).save(exporter, "unseen_world:crafting_warrior_shield_decoration");

        oreBlasting(exporter, List.of(ModBlocks.ABYSSAL_ORE_IN_DARK_CURRANTSLATE, ModBlocks.ABYSSAL_ORE_IN_GLACIEMITE, ModBlocks.ABYSSAL_ORE_IN_CORONA_STONE),
                RecipeCategory.TOOLS, ModItems.RAW_ABYSSAL_ORE, 10, 400, "abyssal_ore");
        oreBlasting(exporter, List.of(ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE, ModBlocks.UNSEENIUM_ORE_IN_GLACIEMITE, ModBlocks.UNSEENIUM_ORE_IN_CORONA_STONE),
                RecipeCategory.TOOLS, ModItems.RAW_UNSEENIUM_ORE, 5, 300, "unseenium_ore");
        oreBlasting(exporter, List.of(ModBlocks.RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE, ModBlocks.RED_TITANIUM_ORE_IN_GLACIEMITE, ModBlocks.RED_TITANIUM_ORE_IN_CORONA_STONE),
                RecipeCategory.TOOLS, ModItems.RAW_RED_TITANIUM_ORE, 10, 500, "titanium_ore");
        oreBlasting(exporter, List.of(ModBlocks.IRON_ORE_IN_DARK_CURRANTSLATE, ModBlocks.IRON_ORE_IN_GLACIEMITE, ModBlocks.IRON_ORE_IN_CORONA_STONE),
                RecipeCategory.TOOLS, Items.IRON_NUGGET, 5, 300, "iron_ore");
        oreBlasting(exporter, List.of(ModBlocks.ANCIENT_DEBRIS_IN_DARK_CURRANTSLATE, ModBlocks.ANCIENT_DEBRIS_IN_GLACIEMITE, ModBlocks.ANCIENT_DEBRIS_IN_CORONA_STONE),
                RecipeCategory.TOOLS, Items.NETHERITE_SCRAP, 10, 500, "ancient_debris");
        oreBlasting(exporter, List.of(ModBlocks.GLIMMERGRAIN_SAND), RecipeCategory.BUILDING_BLOCKS, Blocks.BLACK_STAINED_GLASS, 5, 200, "black_stained_glass");

        ingot(exporter, ModItems.UNSEENIUM_INGOT, ModItems.RAW_UNSEENIUM_ORE);
        ingot(exporter, ModItems.ABYSSAL_INGOT, ModItems.RAW_ABYSSAL_ORE);
        ingot(exporter, ModItems.RED_TITANIUM_INGOT, ModItems.RAW_RED_TITANIUM_ORE);

        nineBlockStorageRecipesWithCustomPacking(exporter, RecipeCategory.MISC, ModItems.GRANULATED_GRIPCRYSTAL,
                RecipeCategory.MISC, ModItems.GRIPCRYSTAL, "gripcrystal_from_granulate", "gripcrystal");
        nineBlockStorageRecipesWithCustomPacking(exporter, RecipeCategory.MISC, ModItems.GRANULATED_GRIPTONITE,
                RecipeCategory.MISC, ModItems.GRIPTONITE, "griptonite_from_granulate", "griptonite");
        twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLIMMERGRAIN_SANDSTONE, ModBlocks.GLIMMERGRAIN_SAND);

        oneToOneConversionRecipe(exporter, Items.PURPLE_DYE, ModBlocks.BURLYWOOD_VIOLET, "purple_dye");
        oneToOneConversionRecipe(exporter, Items.LIGHT_BLUE_DYE, ModBlocks.FROSTBLOOM, "light_blue_dye");
        oneToOneConversionRecipe(exporter, Items.ORANGE_DYE, ModBlocks.CORONA_BLOOM, "orange_dye");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.RIMEHORN_MEAT), RecipeCategory.FOOD, ModItems.COOKED_RIMEHORN_MEAT, 0.5F, 300)
                .unlockedBy(getHasName(ModItems.RIMEHORN_MEAT), has(ModItems.RIMEHORN_MEAT)).save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.PAPER, 3)
                .define('#', ModBlocks.ASHREED)
                .pattern("###")
                .unlockedBy(getHasName(ModBlocks.ASHREED), has(ModBlocks.ASHREED))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, Items.BONE_MEAL, 4)
                .define('D', ModBlocks.GLOOMWEED)
                .define('G', ModBlocks.UMBRAL_KELP)
                .pattern("DG")
                .pattern("GD")
                .unlockedBy(getHasName(ModBlocks.UMBRAL_KELP), has(ModBlocks.UMBRAL_KELP))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.THICK_STRING, 1)
                .define('D', ModBlocks.GLOOMWEED)
                .define('G', ModBlocks.UMBRAL_KELP)
                .pattern("GDG")
                .unlockedBy(getHasName(ModBlocks.UMBRAL_KELP), has(ModBlocks.UMBRAL_KELP))
                .save(exporter);
        threeByThreePacker(exporter, RecipeCategory.BUILDING_BLOCKS, Blocks.PURPLE_WOOL, ModItems.THICK_STRING, getHasName(ModItems.THICK_STRING));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GRIPPING_ABYSSAL_BOW)
                .define('A', ModItems.ABYSSAL_INGOT)
                .define('G', ModItems.GRIPCRYSTAL)
                .define('X', ModItems.THICK_STRING)
                .pattern(" GX")
                .pattern("A X")
                .pattern(" GX")
                .unlockedBy(getHasName(ModItems.THICK_STRING), has(ModItems.THICK_STRING))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, Items.BOW)
                .requires(ModItems.GRIPPING_ABYSSAL_BOW)
                .requires(ModItems.GRIPTONITE)
                .unlockedBy(getHasName(ModItems.GRIPTONITE), has(ModItems.GRIPTONITE))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.GRIPTONITE_LANTERN)
                .define('#', ModItems.GRIPTONITE)
                .define('X', Items.IRON_NUGGET)
                .pattern("XXX")
                .pattern("X#X")
                .pattern("XXX")
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                .unlockedBy(getHasName(ModItems.GRIPTONITE), has(ModItems.GRIPTONITE))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.GRIPPING_BUNDLE)
                .define('t', ModItems.THICK_STRING)
                .define('m', ModItems.GRIPPING_MEMBRANE)
                .pattern("t")
                .pattern("m")
                .unlockedBy("has_string", has(ModItems.THICK_STRING))
                .save(exporter);
    }

    private static BlockHolder holderOf(Block block) {
        BlockHolder holder = SolRegistries.MOD.find(BlockHolder.class, candidate -> candidate.get() == block);
        if (holder == null) {
            throw new IllegalStateException("No SolLib holder for " + block);
        }
        return holder;
    }

    private void setMembers(RecipeOutput exporter, BlockHolder holder) {
        Block base = holder.get();
        if (holder.getFence() != null) {
            fenceBuilder(holder.getFence().get(), Ingredient.of(base))
                    .unlockedBy(getHasName(base), has(base))
                    .save(exporter);
        }
        if (holder.getFenceGate() != null) {
            fenceGateBuilder(holder.getFenceGate().get(), Ingredient.of(base))
                    .unlockedBy(getHasName(base), has(base))
                    .save(exporter);
        }
        if (holder.getWall() != null) {
            wall(exporter, RecipeCategory.DECORATIONS, holder.getWall().get(), base);
        }
        if (holder.getStairs() != null && holder.getFence() == null) {
            stonecutting(exporter, base, holder);
        }
    }

    private static void stonecutting(RecipeOutput exporter, ItemLike source, BlockHolder target) {
        if (target.get().asItem() != source.asItem()) {
            stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, target.get(), source);
        }
        if (target.getStairs() != null) {
            stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, target.getStairs().get(), source);
        }
        if (target.getSlab() != null) {
            stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, target.getSlab().get(), source, 2);
        }
        if (target.getWall() != null) {
            stonecutterResultFromBase(exporter, RecipeCategory.DECORATIONS, target.getWall().get(), source);
        }
    }

    private void woodFamily(RecipeOutput exporter, ModWoodFamily family) {
        planksFromLogs(exporter, family.planks(), family.logs(), 4);
        woodFromLogs(exporter, family.wood(), family.log());
        woodFromLogs(exporter, family.strippedWood(), family.strippedLog());
        doorBuilder(family.door(), Ingredient.of(family.planks()))
                .unlockedBy(getHasName(family.planks()), has(family.planks()))
                .save(exporter);
        trapdoorBuilder(family.trapdoor(), Ingredient.of(family.planks()))
                .unlockedBy(getHasName(family.planks()), has(family.planks()))
                .save(exporter);
        signBuilder(family.signItem(), Ingredient.of(family.planks()))
                .unlockedBy(getHasName(family.planks()), has(family.planks()))
                .save(exporter);
        hangingSign(exporter, family.hangingSignItem(), family.strippedLog());
    }

    private void ingot(RecipeOutput exporter, Item ingot, Item raw) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot)
                .requires(raw, 4)
                .requires(Items.NETHERITE_SCRAP, 4)
                .group(getItemName(ingot))
                .unlockedBy(getHasName(Items.NETHERITE_SCRAP), has(Items.NETHERITE_SCRAP))
                .save(exporter);
    }

    private void gear(RecipeOutput exporter, Gear gear) {
        shaped(exporter, RecipeCategory.COMBAT, gear.helmet(), gear.ingot(), "XXX", "X X");
        shaped(exporter, RecipeCategory.COMBAT, gear.chestplate(), gear.ingot(), "X X", "XXX", "XXX");
        shaped(exporter, RecipeCategory.COMBAT, gear.leggings(), gear.ingot(), "XXX", "X X", "X X");
        shaped(exporter, RecipeCategory.COMBAT, gear.boots(), gear.ingot(), "X X", "X X");
        shaped(exporter, RecipeCategory.COMBAT, gear.sword(), gear.ingot(), "X", "X", "#");
        shaped(exporter, RecipeCategory.TOOLS, gear.pickaxe(), gear.ingot(), "XXX", " # ", " # ");
        shaped(exporter, RecipeCategory.TOOLS, gear.axe(), gear.ingot(), "XX", "X#", " #");
        shaped(exporter, RecipeCategory.TOOLS, gear.shovel(), gear.ingot(), "X", "#", "#");
        shaped(exporter, RecipeCategory.TOOLS, gear.hoe(), gear.ingot(), "XX", " #", " #");
    }

    private void shaped(RecipeOutput exporter, RecipeCategory category, Item result, Item ingot, String... pattern) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(category, result).define('X', ingot);
        boolean usesStick = false;
        for (String row : pattern) {
            builder.pattern(row);
            usesStick |= row.indexOf('#') >= 0;
        }
        if (usesStick) {
            builder.define('#', Items.STICK);
        }
        builder.unlockedBy(getHasName(ingot), has(ingot)).save(exporter);
    }

    private record Gear(Item ingot, Item helmet, Item chestplate, Item leggings, Item boots,
                        Item sword, Item pickaxe, Item axe, Item shovel, Item hoe) {
    }
}