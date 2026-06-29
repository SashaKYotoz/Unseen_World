package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
        for (Block parent : ModRegistry.BLOCK_SETS.keySet()) {
            for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(parent).entrySet()) {
                if (entry.getKey() == ModRegistry.Models.STAIRS)
                    stairBuilder(entry.getValue(), Ingredient.of(parent))
                            .unlockedBy(getHasName(parent), has(parent))
                            .save(exporter);

                if (entry.getKey() == ModRegistry.Models.SLAB)
                    slab(exporter, RecipeCategory.BUILDING_BLOCKS, entry.getValue(), parent);

                if (entry.getKey() == ModRegistry.Models.PRESSURE_PLATE)
                    pressurePlate(exporter, entry.getValue(), parent);

                if (entry.getKey() == ModRegistry.Models.BUTTON)
                    oneToOneConversionRecipe(exporter, entry.getValue(), parent, "mod_buttons");

                if (entry.getKey() == ModRegistry.Models.FENCE)
                    fenceBuilder(entry.getValue(), Ingredient.of(parent))
                            .unlockedBy(getHasName(parent), has(parent))
                            .save(exporter);

                if (entry.getKey() == ModRegistry.Models.FENCE_GATE)
                    fenceGateBuilder(entry.getValue(), Ingredient.of(parent))
                            .unlockedBy(getHasName(parent), has(parent))
                            .save(exporter);

                if (entry.getKey() == ModRegistry.Models.TRAPDOOR)
                    trapdoorBuilder(entry.getValue(), Ingredient.of(parent))
                            .unlockedBy(getHasName(parent), has(parent))
                            .save(exporter);

                if (entry.getKey() == ModRegistry.Models.DOOR)
                    doorBuilder(entry.getValue(), Ingredient.of(parent))
                            .unlockedBy(getHasName(parent), has(parent))
                            .save(exporter);

                if (entry.getKey() == ModRegistry.Models.SIGN)
                    signBuilder(entry.getValue(), Ingredient.of(parent))
                            .unlockedBy(getHasName(parent), has(parent))
                            .save(exporter);

                if (entry.getKey() == ModRegistry.Models.PANE)
                    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, entry.getValue(), 16)
                            .define('#', parent)
                            .pattern("###").pattern("###")
                            .group(BuiltInRegistries.ITEM.getKey(entry.getValue().asItem()).getPath())
                            .unlockedBy(getHasName(parent), has(parent))
                            .save(exporter);
            }
        }
        //trapdoors
        trapdoorBuilder(ModBlocks.AMETHYST_TRAPDOOR, Ingredient.of(ModBlocks.AMETHYST_PLANKS))
                .unlockedBy(getHasName(ModBlocks.AMETHYST_PLANKS), has(ModBlocks.AMETHYST_PLANKS))
                .save(exporter);
        trapdoorBuilder(ModBlocks.TEALIVE_TRAPDOOR, Ingredient.of(ModBlocks.TEALIVE_PLANKS))
                .unlockedBy(getHasName(ModBlocks.TEALIVE_PLANKS), has(ModBlocks.TEALIVE_PLANKS))
                .save(exporter);
        trapdoorBuilder(ModBlocks.BURLYWOOD_TRAPDOOR, Ingredient.of(ModBlocks.BURLYWOOD_PLANKS))
                .unlockedBy(getHasName(ModBlocks.BURLYWOOD_PLANKS), has(ModBlocks.BURLYWOOD_PLANKS))
                .save(exporter);
        trapdoorBuilder(ModBlocks.CRIMSONVEIL_TRAPDOOR, Ingredient.of(ModBlocks.CRIMSONVEIL_PLANKS))
                .unlockedBy(getHasName(ModBlocks.CRIMSONVEIL_PLANKS), has(ModBlocks.CRIMSONVEIL_PLANKS))
                .save(exporter);
        trapdoorBuilder(ModBlocks.GRIZZLY_TRAPDOOR, Ingredient.of(ModBlocks.GRIZZLY_PLANKS))
                .unlockedBy(getHasName(ModBlocks.GRIZZLY_PLANKS), has(ModBlocks.GRIZZLY_PLANKS))
                .save(exporter);
        //doors
        doorBuilder(ModBlocks.AMETHYST_DOOR, Ingredient.of(ModBlocks.AMETHYST_PLANKS))
                .unlockedBy(getHasName(ModBlocks.AMETHYST_PLANKS), has(ModBlocks.AMETHYST_PLANKS))
                .save(exporter);
        doorBuilder(ModBlocks.TEALIVE_DOOR, Ingredient.of(ModBlocks.TEALIVE_PLANKS))
                .unlockedBy(getHasName(ModBlocks.TEALIVE_PLANKS), has(ModBlocks.TEALIVE_PLANKS))
                .save(exporter);
        doorBuilder(ModBlocks.BURLYWOOD_DOOR, Ingredient.of(ModBlocks.BURLYWOOD_PLANKS))
                .unlockedBy(getHasName(ModBlocks.BURLYWOOD_PLANKS), has(ModBlocks.BURLYWOOD_PLANKS))
                .save(exporter);
        doorBuilder(ModBlocks.CRIMSONVEIL_DOOR, Ingredient.of(ModBlocks.CRIMSONVEIL_PLANKS))
                .unlockedBy(getHasName(ModBlocks.CRIMSONVEIL_PLANKS), has(ModBlocks.CRIMSONVEIL_PLANKS))
                .save(exporter);
        doorBuilder(ModBlocks.GRIZZLY_DOOR, Ingredient.of(ModBlocks.GRIZZLY_PLANKS))
                .unlockedBy(getHasName(ModBlocks.GRIZZLY_PLANKS), has(ModBlocks.GRIZZLY_PLANKS))
                .save(exporter);
        //shields
        SpecialRecipeBuilder.special(ModRegistry.WARRIOR_SHIELD_DECORATION).save(exporter, "unseen_world:crafting_warrior_shield_decoration");
        //planks
        planksFromLogs(exporter, ModBlocks.AMETHYST_PLANKS, ModTags.Items.AMETHYST_LOGS, 4);
        planksFromLogs(exporter, ModBlocks.TEALIVE_PLANKS, ModTags.Items.TEALIVE_LOGS, 4);
        planksFromLogs(exporter, ModBlocks.BURLYWOOD_PLANKS, ModTags.Items.BURLYWOOD_LOGS, 4);
        planksFromLogs(exporter, ModBlocks.CRIMSONVEIL_PLANKS, ModTags.Items.CRIMSONVEIL_LOGS, 4);
        planksFromLogs(exporter, ModBlocks.GRIZZLY_PLANKS, ModTags.Items.GRIZZLY_LOGS, 4);
        //woods
        polishedBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.AMETHYST_WOOD,
                Ingredient.of(ModBlocks.AMETHYST_LOG))
                .unlockedBy(getHasName(ModBlocks.AMETHYST_LOG), has(ModBlocks.AMETHYST_LOG))
                .group("amethyst_wood").save(exporter, UnseenWorld.makeID("amethyst_wood"));
        polishedBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_AMETHYST_WOOD,
                Ingredient.of(ModBlocks.STRIPPED_AMETHYST_LOG))
                .unlockedBy(getHasName(ModBlocks.STRIPPED_AMETHYST_LOG), has(ModBlocks.STRIPPED_AMETHYST_LOG))
                .group("stripped_amethyst_wood").save(exporter, UnseenWorld.makeID("stripped_amethyst_wood"));
        polishedBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRIZZLY_WOOD,
                Ingredient.of(ModBlocks.GRIZZLY_LOG))
                .unlockedBy(getHasName(ModBlocks.GRIZZLY_LOG), has(ModBlocks.AMETHYST_LOG))
                .group("grizzly_wood").save(exporter, UnseenWorld.makeID("grizzly_wood"));
        polishedBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_GRIZZLY_WOOD,
                Ingredient.of(ModBlocks.STRIPPED_GRIZZLY_LOG))
                .unlockedBy(getHasName(ModBlocks.STRIPPED_GRIZZLY_LOG), has(ModBlocks.STRIPPED_GRIZZLY_LOG))
                .group("stripped_grizzly_wood").save(exporter, UnseenWorld.makeID("stripped_grizzly_wood"));
        polishedBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TEALIVE_WOOD,
                Ingredient.of(ModBlocks.TEALIVE_LOG))
                .unlockedBy(getHasName(ModBlocks.TEALIVE_LOG), has(ModBlocks.TEALIVE_LOG))
                .group("tealive_wood").save(exporter, UnseenWorld.makeID("tealive_wood"));
        polishedBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_TEALIVE_WOOD,
                Ingredient.of(ModBlocks.STRIPPED_TEALIVE_LOG))
                .unlockedBy(getHasName(ModBlocks.STRIPPED_TEALIVE_LOG), has(ModBlocks.STRIPPED_TEALIVE_LOG))
                .group("stripped_tealive_wood").save(exporter, UnseenWorld.makeID("stripped_tealive_wood"));
        polishedBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRIMSONVEIL_WOOD,
                Ingredient.of(ModBlocks.CRIMSONVEIL_LOG))
                .unlockedBy(getHasName(ModBlocks.CRIMSONVEIL_LOG), has(ModBlocks.CRIMSONVEIL_LOG))
                .group("crimsonveil_wood").save(exporter, UnseenWorld.makeID("crimsonveil_wood"));
        polishedBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_CRIMSONVEIL_WOOD,
                Ingredient.of(ModBlocks.STRIPPED_CRIMSONVEIL_LOG))
                .unlockedBy(getHasName(ModBlocks.STRIPPED_CRIMSONVEIL_LOG), has(ModBlocks.STRIPPED_CRIMSONVEIL_LOG))
                .group("stripped_crimsonveil_wood").save(exporter, UnseenWorld.makeID("stripped_crimsonveil_wood"));
        polishedBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BURLYWOOD_WOOD,
                Ingredient.of(ModBlocks.BURLYWOOD_LOG))
                .unlockedBy(getHasName(ModBlocks.BURLYWOOD_LOG), has(ModBlocks.BURLYWOOD_LOG))
                .group("burlywood_wood").save(exporter, UnseenWorld.makeID("burlywood_wood"));
        polishedBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_BURLYWOOD_WOOD,
                Ingredient.of(ModBlocks.STRIPPED_BURLYWOOD_LOG))
                .unlockedBy(getHasName(ModBlocks.STRIPPED_BURLYWOOD_LOG), has(ModBlocks.STRIPPED_BURLYWOOD_LOG))
                .group("stripped_burlywood_wood").save(exporter, UnseenWorld.makeID("stripped_burlywood_wood"));

        //ingots recipes
        oreBlasting(exporter, List.of(ModBlocks.ABYSSAL_ORE_IN_DARK_CURRANTSLATE, ModBlocks.ABYSSAL_ORE_IN_GLACIEMITE),
                RecipeCategory.TOOLS, ModItems.RAW_ABYSSAL_ORE, 10, 400, "abyssal_ore");
        oreBlasting(exporter, List.of(ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE, ModBlocks.UNSEENIUM_ORE_IN_GLACIEMITE),
                RecipeCategory.TOOLS, ModItems.RAW_UNSEENIUM_ORE, 5, 300, "unseenium_ore");
        oreBlasting(exporter, List.of(ModBlocks.RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE, ModBlocks.RED_TITANIUM_IN_GLACIEMITE),
                RecipeCategory.TOOLS, ModItems.RAW_RED_TITANIUM_ORE, 10, 500, "titanium_ore");
        oreBlasting(exporter, List.of(ModBlocks.IRON_ORE_IN_DARK_CURRANTSLATE, ModBlocks.IRON_ORE_IN_GLACIEMITE),
                RecipeCategory.TOOLS, Items.IRON_NUGGET, 5, 300, "iron_ore");
        oreBlasting(exporter, List.of(ModBlocks.ANCIENT_DEBRIS_IN_DARK_CURRANTSLATE, ModBlocks.ANCIENT_DEBRIS_IN_GLACIEMITE),
                RecipeCategory.TOOLS, Items.NETHERITE_SCRAP, 10, 500, "ancient_debris");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.UNSEENIUM_INGOT)
                .requires(ModItems.RAW_UNSEENIUM_ORE, 4)
                .requires(Items.NETHERITE_SCRAP, 4)
                .group("unseenium_ingot")
                .unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ABYSSAL_INGOT)
                .requires(ModItems.RAW_ABYSSAL_ORE, 4)
                .requires(Items.NETHERITE_SCRAP, 4)
                .group("abyssal_ingot")
                .unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RED_TITANIUM_INGOT)
                .requires(ModItems.RAW_RED_TITANIUM_ORE, 4)
                .requires(Items.NETHERITE_SCRAP, 4)
                .group("red_titanium_ingot")
                .unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP))
                .save(exporter);
        //crystals recipes
        nineBlockStorageRecipesWithCustomPacking(
                exporter, RecipeCategory.MISC, ModItems.GRANULATED_GRIPCRYSTAL, RecipeCategory.MISC, ModItems.GRIPCRYSTAL, "gripcrystal_from_granulate", "gripcrystal"
        );
        nineBlockStorageRecipesWithCustomPacking(
                exporter, RecipeCategory.MISC, ModItems.GRANULATED_GRIPTONITE, RecipeCategory.MISC, ModItems.GRIPTONITE, "griptonite_from_granulate", "griptonite"
        );

        twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_TANZANITE, ModBlocks.TANZANITE_BLOCK);
        twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CURRANTSLATE_BRICKS, ModBlocks.DARK_CURRANTSLATE);
        twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLACIEMITE_BRICKS, ModBlocks.GLACIEMITE);
        twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLIMMERGRAIN_SANDSTONE, ModBlocks.GLIMMERGRAIN_SAND);

        oneToOneConversionRecipe(exporter, Items.PURPLE_DYE, ModBlocks.BURLYWOOD_VIOLET, "purple_dye");

        oreBlasting(exporter, List.of(ModBlocks.GLIMMERGRAIN_SAND), RecipeCategory.BUILDING_BLOCKS, Blocks.BLACK_STAINED_GLASS, 5, 200, "black_stained_glass");

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, Items.BONE_MEAL, 4)
                .define('D', ModBlocks.GLOOMWEED)
                .define('G', ModBlocks.UMBRAL_KELP)
                .pattern("DG")
                .pattern("GD")
                .unlockedBy("has_kelp", has(ModBlocks.UMBRAL_KELP))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.THICK_STRING, 1)
                .define('D', ModBlocks.GLOOMWEED)
                .define('G', ModBlocks.UMBRAL_KELP)
                .pattern("GDG")
                .unlockedBy("has_kelp", has(ModBlocks.UMBRAL_KELP))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GRIPPING_ABYSSAL_BOW)
                .define('A', ModItems.ABYSSAL_INGOT)
                .define('G', ModItems.GRIPCRYSTAL)
                .define('X', ModItems.THICK_STRING)
                .pattern(" GX")
                .pattern("A X")
                .pattern(" GX")
                .unlockedBy("has_string", has(ModItems.THICK_STRING))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, Items.BOW)
                .requires(ModItems.GRIPPING_ABYSSAL_BOW)
                .requires(ModItems.GRIPTONITE)
                .unlockedBy("has_griptonite", has(ModItems.GRIPTONITE))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.GRIPTONITE_LANTERN)
                .define('#', ModItems.GRIPTONITE).define('X', Items.IRON_NUGGET)
                .pattern("XXX").pattern("X#X").pattern("XXX")
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .unlockedBy("has_griptonite", has(ModItems.GRIPTONITE)).save(exporter);

        threeByThreePacker(exporter, RecipeCategory.BUILDING_BLOCKS, Blocks.PURPLE_WOOL, ModItems.THICK_STRING, "has_string");
        //hanging sign
        hangingSign(exporter, ModItems.AMETHYST_HANGING_SIGN, ModBlocks.STRIPPED_AMETHYST_LOG);
        hangingSign(exporter, ModItems.GRIZZLY_HANGING_SIGN, ModBlocks.STRIPPED_GRIZZLY_LOG);
        hangingSign(exporter, ModItems.TEALIVE_HANGING_SIGN, ModBlocks.STRIPPED_TEALIVE_LOG);
        hangingSign(exporter, ModItems.CRIMSONVEIL_HANGING_SIGN, ModBlocks.STRIPPED_CRIMSONVEIL_LOG);
        hangingSign(exporter, ModItems.BURLYWOOD_HANGING_SIGN, ModBlocks.STRIPPED_BURLYWOOD_LOG);

        generateHelmetRecipe(exporter, ModItems.UNSEENIUM_INGOT, ModItems.UNSEENIUM_HELMET);
        generateChestplateRecipe(exporter, ModItems.UNSEENIUM_INGOT, ModItems.UNSEENIUM_CHESTPLATE);
        generateLeggingsRecipe(exporter, ModItems.UNSEENIUM_INGOT, ModItems.UNSEENIUM_LEGGINGS);
        generateBootsRecipe(exporter, ModItems.UNSEENIUM_INGOT, ModItems.UNSEENIUM_BOOTS);
        generateHelmetRecipe(exporter, ModItems.ABYSSAL_INGOT, ModItems.ABYSSAL_HELMET);
        generateChestplateRecipe(exporter, ModItems.ABYSSAL_INGOT, ModItems.ABYSSAL_CHESTPLATE);
        generateLeggingsRecipe(exporter, ModItems.ABYSSAL_INGOT, ModItems.ABYSSAL_LEGGINGS);
        generateBootsRecipe(exporter, ModItems.ABYSSAL_INGOT, ModItems.ABYSSAL_BOOTS);
        generateHelmetRecipe(exporter, ModItems.RED_TITANIUM_INGOT, ModItems.RED_TITANIUM_HELMET);
        generateChestplateRecipe(exporter, ModItems.RED_TITANIUM_INGOT, ModItems.RED_TITANIUM_CHESTPLATE);
        generateLeggingsRecipe(exporter, ModItems.RED_TITANIUM_INGOT, ModItems.RED_TITANIUM_LEGGINGS);
        generateBootsRecipe(exporter, ModItems.RED_TITANIUM_INGOT, ModItems.RED_TITANIUM_BOOTS);

        generateSwordRecipe(exporter, ModItems.UNSEENIUM_INGOT, ModItems.UNSEENIUM_SWORD);
        generatePickaxeRecipe(exporter, ModItems.UNSEENIUM_INGOT, ModItems.UNSEENIUM_PICKAXE);
        generateAxeRecipe(exporter, ModItems.UNSEENIUM_INGOT, ModItems.UNSEENIUM_AXE);
        generateHoeRecipe(exporter, ModItems.UNSEENIUM_INGOT, ModItems.UNSEENIUM_HOE);
        generateShovelRecipe(exporter, ModItems.UNSEENIUM_INGOT, ModItems.UNSEENIUM_SHOVEL);
        generateSwordRecipe(exporter, ModItems.ABYSSAL_INGOT, ModItems.ABYSSAL_SWORD);
        generatePickaxeRecipe(exporter, ModItems.ABYSSAL_INGOT, ModItems.ABYSSAL_PICKAXE);
        generateAxeRecipe(exporter, ModItems.ABYSSAL_INGOT, ModItems.ABYSSAL_AXE);
        generateHoeRecipe(exporter, ModItems.ABYSSAL_INGOT, ModItems.ABYSSAL_HOE);
        generateShovelRecipe(exporter, ModItems.ABYSSAL_INGOT, ModItems.ABYSSAL_SHOVEL);
        generateSwordRecipe(exporter, ModItems.RED_TITANIUM_INGOT, ModItems.RED_TITANIUM_SWORD);
        generatePickaxeRecipe(exporter, ModItems.RED_TITANIUM_INGOT, ModItems.RED_TITANIUM_PICKAXE);
        generateAxeRecipe(exporter, ModItems.RED_TITANIUM_INGOT, ModItems.RED_TITANIUM_AXE);
        generateHoeRecipe(exporter, ModItems.RED_TITANIUM_INGOT, ModItems.RED_TITANIUM_HOE);
        generateShovelRecipe(exporter, ModItems.RED_TITANIUM_INGOT, ModItems.RED_TITANIUM_SHOVEL);
    }

    private void generateHelmetRecipe(Consumer<FinishedRecipe> exporter, Item ingredient, Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('X', ingredient)
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy("has_ingredient", has(ingredient))
                .save(exporter);
    }

    private void generateChestplateRecipe(Consumer<FinishedRecipe> exporter, Item ingredient, Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('X', ingredient)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_ingredient", has(ingredient))
                .save(exporter);
    }

    private void generateLeggingsRecipe(Consumer<FinishedRecipe> exporter, Item ingredient, Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('X', ingredient)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_ingredient", has(ingredient))
                .save(exporter);
    }

    private void generateBootsRecipe(Consumer<FinishedRecipe> exporter, Item ingredient, Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('X', ingredient)
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_ingredient", has(ingredient))
                .save(exporter);
    }

    private void generateSwordRecipe(Consumer<FinishedRecipe> exporter, Item ingredient, Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('#', Items.STICK)
                .define('X', ingredient)
                .pattern("X")
                .pattern("X")
                .pattern("#")
                .unlockedBy("has_ingredient", has(ingredient))
                .save(exporter);
    }

    private void generatePickaxeRecipe(Consumer<FinishedRecipe> exporter, Item ingredient, Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('#', Items.STICK)
                .define('X', ingredient)
                .pattern("XXX")
                .pattern(" # ")
                .pattern(" # ")
                .unlockedBy("has_ingredient", has(ingredient))
                .save(exporter);
    }

    private void generateAxeRecipe(Consumer<FinishedRecipe> exporter, Item ingredient, Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('#', Items.STICK)
                .define('X', ingredient)
                .pattern("XXX")
                .pattern(" # ")
                .pattern(" # ")
                .unlockedBy("has_ingredient", has(ingredient))
                .save(exporter);
    }

    private void generateShovelRecipe(Consumer<FinishedRecipe> exporter, Item ingredient, Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('#', Items.STICK)
                .define('X', ingredient)
                .pattern("X")
                .pattern("#")
                .pattern("#")
                .unlockedBy("has_ingredient", has(ingredient))
                .save(exporter);
    }

    private void generateHoeRecipe(Consumer<FinishedRecipe> exporter, Item ingredient, Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('#', Items.STICK)
                .define('X', ingredient)
                .pattern("XX")
                .pattern(" #")
                .pattern(" #")
                .unlockedBy("has_ingredient", has(ingredient))
                .save(exporter);
    }
}