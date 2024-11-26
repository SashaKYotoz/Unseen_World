package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
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
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        for (Block parent : ModRegistry.BLOCK_SETS.keySet()) {
            for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(parent).entrySet()) {
                if (entry.getKey() == ModRegistry.Models.STAIRS)
                    createStairsRecipe(entry.getValue(), Ingredient.ofItems(parent))
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.SLAB)
                    offerSlabRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, entry.getValue(), parent);

                if (entry.getKey() == ModRegistry.Models.PRESSURE_PLATE)
                    offerPressurePlateRecipe(exporter, entry.getValue(), parent);

                if (entry.getKey() == ModRegistry.Models.BUTTON)
                    offerSingleOutputShapelessRecipe(exporter, entry.getValue(), parent, "mod_buttons");

                if (entry.getKey() == ModRegistry.Models.FENCE)
                    createFenceRecipe(entry.getValue(), Ingredient.ofItems(parent))
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.FENCE_GATE)
                    createFenceGateRecipe(entry.getValue(), Ingredient.ofItems(parent))
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.TRAPDOOR)
                    createTrapdoorRecipe(entry.getValue(), Ingredient.ofItems(parent))
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.DOOR)
                    createDoorRecipe(entry.getValue(), Ingredient.ofItems(parent))
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.SIGN)
                    createSignRecipe(entry.getValue(), Ingredient.ofItems(parent))
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.PANE)
                    ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, entry.getValue(), 16)
                            .input('#', parent)
                            .pattern("###").pattern("###")
                            .group(Registries.ITEM.getId(entry.getValue().asItem()).getPath())
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);
            }
        }
        //planks
        offerPlanksRecipe(exporter, ModBlocks.AMETHYST_PLANKS, ModTags.Items.AMETHYST_LOGS, 4);
        offerPlanksRecipe(exporter, ModBlocks.TEALIVE_PLANKS, ModTags.Items.TEALIVE_LOGS, 4);
        offerPlanksRecipe(exporter, ModBlocks.BURLYWOOD_PLANKS, ModTags.Items.BURLYWOOD_LOGS, 4);
        offerPlanksRecipe(exporter, ModBlocks.CRIMSONVEIL_PLANKS, ModTags.Items.CRIMSONVEIL_LOGS, 4);
        offerPlanksRecipe(exporter, ModBlocks.GRIZZLY_PLANKS, ModTags.Items.GRIZZLY_LOGS, 4);
        //woods
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.AMETHYST_WOOD,
                Ingredient.ofItems(ModBlocks.AMETHYST_LOG))
                .criterion(hasItem(ModBlocks.AMETHYST_LOG), conditionsFromItem(ModBlocks.AMETHYST_LOG))
                .group("amethyst_wood").offerTo(exporter, UnseenWorld.makeID("amethyst_wood"));
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_AMETHYST_WOOD,
                Ingredient.ofItems(ModBlocks.STRIPPED_AMETHYST_LOG))
                .criterion(hasItem(ModBlocks.STRIPPED_AMETHYST_LOG), conditionsFromItem(ModBlocks.STRIPPED_AMETHYST_LOG))
                .group("stripped_amethyst_wood").offerTo(exporter, UnseenWorld.makeID("stripped_amethyst_wood"));
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRIZZLY_WOOD,
                Ingredient.ofItems(ModBlocks.GRIZZLY_LOG))
                .criterion(hasItem(ModBlocks.GRIZZLY_LOG), conditionsFromItem(ModBlocks.AMETHYST_LOG))
                .group("grizzly_wood").offerTo(exporter, UnseenWorld.makeID("grizzly_wood"));
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_GRIZZLY_WOOD,
                Ingredient.ofItems(ModBlocks.STRIPPED_GRIZZLY_LOG))
                .criterion(hasItem(ModBlocks.STRIPPED_GRIZZLY_LOG), conditionsFromItem(ModBlocks.STRIPPED_GRIZZLY_LOG))
                .group("stripped_grizzly_wood").offerTo(exporter, UnseenWorld.makeID("stripped_grizzly_wood"));
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TEALIVE_WOOD,
                Ingredient.ofItems(ModBlocks.TEALIVE_LOG))
                .criterion(hasItem(ModBlocks.TEALIVE_LOG), conditionsFromItem(ModBlocks.TEALIVE_LOG))
                .group("tealive_wood").offerTo(exporter, UnseenWorld.makeID("tealive_wood"));
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_TEALIVE_WOOD,
                Ingredient.ofItems(ModBlocks.STRIPPED_TEALIVE_LOG))
                .criterion(hasItem(ModBlocks.STRIPPED_TEALIVE_LOG), conditionsFromItem(ModBlocks.STRIPPED_TEALIVE_LOG))
                .group("stripped_tealive_wood").offerTo(exporter, UnseenWorld.makeID("stripped_tealive_wood"));
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRIMSONVEIL_WOOD,
                Ingredient.ofItems(ModBlocks.CRIMSONVEIL_LOG))
                .criterion(hasItem(ModBlocks.CRIMSONVEIL_LOG), conditionsFromItem(ModBlocks.CRIMSONVEIL_LOG))
                .group("crimsonveil_wood").offerTo(exporter, UnseenWorld.makeID("crimsonveil_wood"));
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_CRIMSONVEIL_WOOD,
                Ingredient.ofItems(ModBlocks.STRIPPED_CRIMSONVEIL_LOG))
                .criterion(hasItem(ModBlocks.STRIPPED_CRIMSONVEIL_LOG), conditionsFromItem(ModBlocks.STRIPPED_CRIMSONVEIL_LOG))
                .group("stripped_crimsonveil_wood").offerTo(exporter, UnseenWorld.makeID("stripped_crimsonveil_wood"));
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BURLYWOOD_WOOD,
                Ingredient.ofItems(ModBlocks.BURLYWOOD_LOG))
                .criterion(hasItem(ModBlocks.BURLYWOOD_LOG), conditionsFromItem(ModBlocks.BURLYWOOD_LOG))
                .group("burlywood_wood").offerTo(exporter, UnseenWorld.makeID("burlywood_wood"));
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_BURLYWOOD_WOOD,
                Ingredient.ofItems(ModBlocks.STRIPPED_BURLYWOOD_LOG))
                .criterion(hasItem(ModBlocks.STRIPPED_BURLYWOOD_LOG), conditionsFromItem(ModBlocks.STRIPPED_BURLYWOOD_LOG))
                .group("stripped_burlywood_wood").offerTo(exporter, UnseenWorld.makeID("stripped_burlywood_wood"));

        //ingots recipes
        offerBlasting(exporter, List.of(ModBlocks.ABYSSAL_ORE_IN_DARK_CURRANTSLATE, ModBlocks.ABYSSAL_ORE_IN_GLACIEMITE),
                RecipeCategory.TOOLS, ModItems.RAW_ABYSSAL_ORE, 10, 400, "abyssal_ore");
        offerBlasting(exporter, List.of(ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE, ModBlocks.UNSEENIUM_ORE_IN_GLACIEMITE),
                RecipeCategory.TOOLS, ModItems.RAW_UNSEENIUM_ORE, 5, 300, "unseenium_ore");
        offerBlasting(exporter, List.of(ModBlocks.RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE, ModBlocks.RED_TITANIUM_IN_GLACIEMITE),
                RecipeCategory.TOOLS, ModItems.RAW_RED_TITANIUM_ORE, 10, 500, "titanium_ore");
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.UNSEENIUM_INGOT)
                .input(ModItems.RAW_UNSEENIUM_ORE, 4)
                .input(Items.NETHERITE_SCRAP, 4)
                .group("unseenium_ingot")
                .criterion("has_netherite_scrap", conditionsFromItem(Items.NETHERITE_SCRAP))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ABYSSAL_INGOT)
                .input(ModItems.RAW_ABYSSAL_ORE, 4)
                .input(Items.NETHERITE_SCRAP, 4)
                .group("abyssal_ingot")
                .criterion("has_netherite_scrap", conditionsFromItem(Items.NETHERITE_SCRAP))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RED_TITANIUM_INGOT)
                .input(ModItems.RAW_RED_TITANIUM_ORE, 4)
                .input(Items.NETHERITE_SCRAP, 4)
                .group("red_titanium_ingot")
                .criterion("has_netherite_scrap", conditionsFromItem(Items.NETHERITE_SCRAP))
                .offerTo(exporter);

        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_TANZANITE, ModBlocks.TANZANITE_BLOCK);
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CURRANTSLATE_BRICKS, ModBlocks.DARK_CURRANTSLATE);
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLACIEMITE_BRICKS, ModBlocks.GLACIEMITE);

        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, Items.BONE_MEAL, 4)
                .input('D', ModBlocks.GLOOMWEED)
                .input('G', ModBlocks.UMBRAL_KELP)
                .pattern("DG")
                .pattern("GD")
                .criterion("has_kelp", conditionsFromItem(ModBlocks.UMBRAL_KELP))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.THICK_STRING, 1)
                .input('D', ModBlocks.GLOOMWEED)
                .input('G', ModBlocks.UMBRAL_KELP)
                .pattern("GDG")
                .criterion("has_kelp", conditionsFromItem(ModBlocks.UMBRAL_KELP))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.GRIPPING_ABYSSAL_BOW)
                .input('A', ModItems.ABYSSAL_INGOT)
                .input('G', ModItems.GRIPCRYSTAL)
                .input('X', ModItems.THICK_STRING)
                .pattern(" GX")
                .pattern("A X")
                .pattern(" GX")
                .criterion("has_string", conditionsFromItem(ModItems.THICK_STRING))
                .offerTo(exporter);
        //hanging sign
        offerHangingSignRecipe(exporter, ModItems.AMETHYST_HANGING_SIGN, ModBlocks.STRIPPED_AMETHYST_LOG);
        offerHangingSignRecipe(exporter, ModItems.GRIZZLY_HANGING_SIGN, ModBlocks.STRIPPED_GRIZZLY_LOG);
        offerHangingSignRecipe(exporter, ModItems.TEALIVE_HANGING_SIGN, ModBlocks.STRIPPED_TEALIVE_LOG);
        offerHangingSignRecipe(exporter, ModItems.CRIMSONVEIL_HANGING_SIGN, ModBlocks.STRIPPED_CRIMSONVEIL_LOG);
        offerHangingSignRecipe(exporter, ModItems.BURLYWOOD_HANGING_SIGN, ModBlocks.STRIPPED_BURLYWOOD_LOG);

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
    }

    private void generateHelmetRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Item result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('X', ingredient)
                .pattern("XXX")
                .pattern("X X")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }

    private void generateChestplateRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Item result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('X', ingredient)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }

    private void generateLeggingsRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Item result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('X', ingredient)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }

    private void generateBootsRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Item result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('X', ingredient)
                .pattern("X X")
                .pattern("X X")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }
}