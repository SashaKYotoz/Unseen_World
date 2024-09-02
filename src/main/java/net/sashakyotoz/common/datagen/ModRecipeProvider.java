package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
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
//            offerPlanksRecipe(exporter, ModBlocks.AMETHYST_PLANKS, ModTags.Items.AMETHYST_LOGS, 4);
//            offerBlasting(exporter, List.of(ModBlocks.ABYSSAL_ORE_IN_DARK_CURRANTSLATE,ModBlocks.ABYSSAL_ORE_IN_GLACIEMITE, ModItems.RAW_ABYSSAL_ORE),
//                    RecipeCategory.TOOLS, ModItems.ABYSSAL_INGOT,5,400,"abyssal_ore");
//            offerBlasting(exporter, List.of(ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE,ModBlocks.UNSEENIUM_ORE_IN_GLACIEMITE, ModItems.RAW_UNSEENIUM_ORE),
//                    RecipeCategory.TOOLS,ModItems.UNSEENIUM_INGOT,5,300,"unseenium_ore");
//            offerBlasting(exporter, List.of(ModBlocks.RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE,ModBlocks.RED_TITANIUM_IN_GLACIEMITE, ModItems.RAW_RED_TITANIUM_ORE),
//                    RecipeCategory.TOOLS,ModItems.RED_TITANIUM_INGOT,10,500,"titanium_ore");
//            offer2x2CompactingRecipe(exporter,RecipeCategory.BUILDING_BLOCKS,ModBlocks.POLISHED_TANZANITE,ModBlocks.TANZANITE_BLOCK);
//            offer2x2CompactingRecipe(exporter,RecipeCategory.BUILDING_BLOCKS,ModBlocks.DARK_CURRANTSLATE_BRICKS,ModBlocks.DARK_CURRANTSLATE);
//            offer2x2CompactingRecipe(exporter,RecipeCategory.BUILDING_BLOCKS,ModBlocks.GLACIEMITE_BRICKS,ModBlocks.GLACIEMITE);
        }
    }
}