package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.ComplexRecipeJsonBuilder;
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
        //trapdoors
        createTrapdoorRecipe(ModBlocks.AMETHYST_TRAPDOOR, Ingredient.ofItems(ModBlocks.AMETHYST_PLANKS))
                .criterion(hasItem(ModBlocks.AMETHYST_PLANKS), conditionsFromItem(ModBlocks.AMETHYST_PLANKS))
                .offerTo(exporter);
        createTrapdoorRecipe(ModBlocks.TEALIVE_TRAPDOOR, Ingredient.ofItems(ModBlocks.TEALIVE_PLANKS))
                .criterion(hasItem(ModBlocks.TEALIVE_PLANKS), conditionsFromItem(ModBlocks.TEALIVE_PLANKS))
                .offerTo(exporter);
        createTrapdoorRecipe(ModBlocks.BURLYWOOD_TRAPDOOR, Ingredient.ofItems(ModBlocks.BURLYWOOD_PLANKS))
                .criterion(hasItem(ModBlocks.BURLYWOOD_PLANKS), conditionsFromItem(ModBlocks.BURLYWOOD_PLANKS))
                .offerTo(exporter);
        createTrapdoorRecipe(ModBlocks.CRIMSONVEIL_TRAPDOOR, Ingredient.ofItems(ModBlocks.CRIMSONVEIL_PLANKS))
                .criterion(hasItem(ModBlocks.CRIMSONVEIL_PLANKS), conditionsFromItem(ModBlocks.CRIMSONVEIL_PLANKS))
                .offerTo(exporter);
        createTrapdoorRecipe(ModBlocks.GRIZZLY_TRAPDOOR, Ingredient.ofItems(ModBlocks.GRIZZLY_PLANKS))
                .criterion(hasItem(ModBlocks.GRIZZLY_PLANKS), conditionsFromItem(ModBlocks.GRIZZLY_PLANKS))
                .offerTo(exporter);
        //doors
        createDoorRecipe(ModBlocks.AMETHYST_DOOR, Ingredient.ofItems(ModBlocks.AMETHYST_PLANKS))
                .criterion(hasItem(ModBlocks.AMETHYST_PLANKS), conditionsFromItem(ModBlocks.AMETHYST_PLANKS))
                .offerTo(exporter);
        createDoorRecipe(ModBlocks.TEALIVE_DOOR, Ingredient.ofItems(ModBlocks.TEALIVE_PLANKS))
                .criterion(hasItem(ModBlocks.TEALIVE_PLANKS), conditionsFromItem(ModBlocks.TEALIVE_PLANKS))
                .offerTo(exporter);
        createDoorRecipe(ModBlocks.BURLYWOOD_DOOR, Ingredient.ofItems(ModBlocks.BURLYWOOD_PLANKS))
                .criterion(hasItem(ModBlocks.BURLYWOOD_PLANKS), conditionsFromItem(ModBlocks.BURLYWOOD_PLANKS))
                .offerTo(exporter);
        createDoorRecipe(ModBlocks.CRIMSONVEIL_DOOR, Ingredient.ofItems(ModBlocks.CRIMSONVEIL_PLANKS))
                .criterion(hasItem(ModBlocks.CRIMSONVEIL_PLANKS), conditionsFromItem(ModBlocks.CRIMSONVEIL_PLANKS))
                .offerTo(exporter);
        createDoorRecipe(ModBlocks.GRIZZLY_DOOR, Ingredient.ofItems(ModBlocks.GRIZZLY_PLANKS))
                .criterion(hasItem(ModBlocks.GRIZZLY_PLANKS), conditionsFromItem(ModBlocks.GRIZZLY_PLANKS))
                .offerTo(exporter);
        //shields
        ComplexRecipeJsonBuilder.create(ModRegistry.WARRIOR_SHIELD_DECORATION).offerTo(exporter, "unseen_world:crafting_warrior_shield_decoration");
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
        offerBlasting(exporter, List.of(ModBlocks.IRON_ORE_IN_DARK_CURRANTSLATE, ModBlocks.IRON_ORE_IN_GLACIEMITE),
                RecipeCategory.TOOLS, Items.IRON_NUGGET, 5, 300, "iron_ore");
        offerBlasting(exporter, List.of(ModBlocks.ANCIENT_DEBRIS_IN_DARK_CURRANTSLATE, ModBlocks.ANCIENT_DEBRIS_IN_GLACIEMITE),
                RecipeCategory.TOOLS, Items.NETHERITE_SCRAP, 10, 500, "ancient_debris");

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
        //crystals recipes
        offerReversibleCompactingRecipesWithCompactingRecipeGroup(
                exporter, RecipeCategory.MISC, ModItems.GRANULATED_GRIPCRYSTAL, RecipeCategory.MISC, ModItems.GRIPCRYSTAL, "gripcrystal_from_granulate", "gripcrystal"
        );
        offerReversibleCompactingRecipesWithCompactingRecipeGroup(
                exporter, RecipeCategory.MISC, ModItems.GRANULATED_GRIPTONITE, RecipeCategory.MISC, ModItems.GRIPTONITE, "griptonite_from_granulate", "griptonite"
        );

        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_TANZANITE, ModBlocks.TANZANITE_BLOCK);
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CURRANTSLATE_BRICKS, ModBlocks.DARK_CURRANTSLATE);
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLACIEMITE_BRICKS, ModBlocks.GLACIEMITE);

        offerSingleOutputShapelessRecipe(exporter, Items.PURPLE_DYE, ModBlocks.BURLYWOOD_VIOLET, "purple_dye");

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

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, Items.BOW)
                .input(ModItems.GRIPPING_ABYSSAL_BOW)
                .input(ModItems.GRIPTONITE)
                .criterion("has_griptonite", conditionsFromItem(ModItems.GRIPTONITE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.GRIPTONITE_LANTERN)
                .input('#', ModItems.GRIPTONITE).input('X', Items.IRON_NUGGET)
                .pattern("XXX").pattern("X#X").pattern("XXX")
                .criterion("has_iron_nugget", conditionsFromItem(Items.IRON_NUGGET))
                .criterion("has_griptonite", conditionsFromItem(ModItems.GRIPTONITE)).offerTo(exporter);

        offerCompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, Blocks.PURPLE_WOOL, ModItems.THICK_STRING, "has_string");
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

    private void generateSwordRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Item result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('#', Items.STICK)
                .input('X', ingredient)
                .pattern("X")
                .pattern("X")
                .pattern("#")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }

    private void generatePickaxeRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Item result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('#', Items.STICK)
                .input('X', ingredient)
                .pattern("XXX")
                .pattern(" # ")
                .pattern(" # ")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }

    private void generateAxeRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Item result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('#', Items.STICK)
                .input('X', ingredient)
                .pattern("XXX")
                .pattern(" # ")
                .pattern(" # ")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }

    private void generateShovelRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Item result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('#', Items.STICK)
                .input('X', ingredient)
                .pattern("X")
                .pattern("#")
                .pattern("#")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }

    private void generateHoeRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Item result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input('#', Items.STICK)
                .input('X', ingredient)
                .pattern("XX")
                .pattern(" #")
                .pattern(" #")
                .criterion("has_ingredient", conditionsFromItem(ingredient))
                .offerTo(exporter);
    }
}