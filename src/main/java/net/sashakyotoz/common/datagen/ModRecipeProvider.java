package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
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

                if (entry.getKey() == ModRegistry.Models.SIGN)
                    createSignRecipe(entry.getValue(), Ingredient.ofItems(parent))
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.PANE)
                    ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, entry.getValue(), 16)
                            .input('#', parent)
                            .pattern("###").pattern("###")
                            .group(Registries.ITEM.getId(entry.getValue().asItem()).getPath())
                            .criterion(hasItem(parent),conditionsFromItem(parent))
                            .offerTo(exporter);
            }
        }
        offerBlasting(exporter, List.of(ModBlocks.ABYSSAL_ORE_IN_DARK_CURRANTSLATE,ModBlocks.ABYSSAL_ORE_IN_GLACIEMITE, ModItems.RAW_ABYSSAL_ORE),
                RecipeCategory.TOOLS, ModItems.ABYSSAL_INGOT,5,400,"abyssal_ore");
        offerBlasting(exporter, List.of(ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE,ModBlocks.UNSEENIUM_ORE_IN_GLACIEMITE, ModItems.RAW_UNSEENIUM_ORE),
                RecipeCategory.TOOLS,ModItems.UNSEENIUM_INGOT,5,300,"unseenium_ore");
        offerBlasting(exporter, List.of(ModBlocks.RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE,ModBlocks.RED_TITANIUM_IN_GLACIEMITE, ModItems.RAW_RED_TITANIUM_ORE),
                RecipeCategory.TOOLS,ModItems.RED_TITANIUM_INGOT,10,500,"titanium_ore");
        offer2x2CompactingRecipe(exporter,RecipeCategory.BUILDING_BLOCKS,ModBlocks.POLISHED_TANZANITE,ModBlocks.TANZANITE_BLOCK);
        offer2x2CompactingRecipe(exporter,RecipeCategory.BUILDING_BLOCKS,ModBlocks.DARK_CURRANTSLATE_BRICKS,ModBlocks.DARK_CURRANTSLATE);
        offer2x2CompactingRecipe(exporter,RecipeCategory.BUILDING_BLOCKS,ModBlocks.GLACIEMITE_BRICKS,ModBlocks.GLACIEMITE);

        generateHelmetRecipe(exporter,ModItems.UNSEENIUM_INGOT,ModItems.UNSEENIUM_HELMET);
        generateChestplateRecipe(exporter,ModItems.UNSEENIUM_INGOT,ModItems.UNSEENIUM_CHESTPLATE);
        generateLeggingsRecipe(exporter,ModItems.UNSEENIUM_INGOT,ModItems.UNSEENIUM_LEGGINGS);
        generateBootsRecipe(exporter,ModItems.UNSEENIUM_INGOT,ModItems.UNSEENIUM_BOOTS);
        generateHelmetRecipe(exporter,ModItems.ABYSSAL_INGOT,ModItems.ABYSSAL_HELMET);
        generateChestplateRecipe(exporter,ModItems.ABYSSAL_INGOT,ModItems.ABYSSAL_CHESTPLATE);
        generateLeggingsRecipe(exporter,ModItems.ABYSSAL_INGOT,ModItems.ABYSSAL_LEGGINGS);
        generateBootsRecipe(exporter,ModItems.ABYSSAL_INGOT,ModItems.ABYSSAL_BOOTS);
        generateHelmetRecipe(exporter,ModItems.RED_TITANIUM_INGOT,ModItems.RED_TITANIUM_HELMET);
        generateChestplateRecipe(exporter,ModItems.RED_TITANIUM_INGOT,ModItems.RED_TITANIUM_CHESTPLATE);
        generateLeggingsRecipe(exporter,ModItems.RED_TITANIUM_INGOT,ModItems.RED_TITANIUM_LEGGINGS);
        generateBootsRecipe(exporter,ModItems.RED_TITANIUM_INGOT,ModItems.RED_TITANIUM_BOOTS);
    }
    private void generateHelmetRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient,Item result){
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('X', ingredient)
                .pattern("XXX")
                .pattern("X X")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }
    private void generateChestplateRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient,Item result){
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('X', ingredient)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }
    private void generateLeggingsRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient,Item result){
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('X', ingredient)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }
    private void generateBootsRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient,Item result){
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('X', ingredient)
                .pattern("X X")
                .pattern("X X")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }
}