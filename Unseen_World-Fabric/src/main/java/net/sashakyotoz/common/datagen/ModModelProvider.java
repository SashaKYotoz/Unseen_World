package net.sashakyotoz.common.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.BulbLikeBlock;
import net.sashakyotoz.common.blocks.custom.EngravedGlaciemiteBricksBlock;
import net.sashakyotoz.common.blocks.custom.KeyHandlerStoneBlock;
import net.sashakyotoz.common.blocks.custom.OrdealSpawnerBlock;
import net.sashakyotoz.common.blocks.custom.plants.DarknessFernBlock;
import net.sashakyotoz.common.blocks.custom.plants.HangingFruitBlock;
import net.sashakyotoz.common.blocks.custom.plants.LeafDroppingLeaveBlock;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        BlockModelGenerators.BlockFamilyProvider pool;
        BlockModelGenerators.WoodProvider poollog;

        for (Block block : ModRegistry.getModelList(ModRegistry.Models.CUBE))
            generator.createTrivialCube(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.GRASS))
            registerDynamicTopSoils(TextureMapping.getBlockTexture(block), generator, block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.PILLAR))
            generator.woodProvider(block).logWithHorizontal(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.CROSS))
            generator.createCrossBlockWithDefaultItem(block, BlockModelGenerators.TintState.NOT_TINTED);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.DOOR))
            generator.createDoor(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.TRAPDOOR))
            generator.createOrientableTrapdoor(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.FLUID))
            generator.createNonTemplateModelBlock(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.BULB))
            registerBulbBlock(generator, (BulbLikeBlock) block);
        for (Block block : ModRegistry.BLOCK_SETS.keySet()) {
            if (ModRegistry.BLOCK_SETS.get(block).containsKey(ModRegistry.Models.WOOD)) {
                poollog = generator.woodProvider(block);
                poollog.logWithHorizontal(block);
                for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(block).entrySet()) {
                    if (entry.getKey() == ModRegistry.Models.WOOD)
                        poollog.wood(entry.getValue());
                }
            } else if (ModRegistry.BLOCK_SETS.get(block).containsKey(ModRegistry.Models.PANE)) {
                for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(block).entrySet()) {
                    if (entry.getKey() == ModRegistry.Models.PANE)
                        generator.createGlassBlocks(block, entry.getValue());
                }
            } else {
                pool = generator.family(block);
                for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(block).entrySet()) {
                    if (entry.getKey() == ModRegistry.Models.STAIRS)
                        pool.stairs(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.SLAB)
                        pool.slab(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.BUTTON)
                        pool.button(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.PRESSURE_PLATE)
                        pool.pressurePlate(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.FENCE)
                        pool.fence(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.WALL)
                        pool.wall(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.FENCE_GATE)
                        pool.fenceGate(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.SIGN)
                        pool.generateFor(new BlockFamily.Builder(block)
                                .sign(entry.getValue(), ModRegistry.BLOCK_SETS.get(block).get(ModRegistry.Models.WALL_SIGN))
                                .getFamily());
                }
            }
        }
        generator.delegateItemModel(ModBlocks.KEY_HANDLER_STONE.asItem(), UnseenWorld.makeID("block/key_handler_stone_glaciemite"));
        generator.delegateItemModel(ModBlocks.GLACIEMITE_TRANSLOCATONE.asItem(), UnseenWorld.makeID("block/glaciemite_translocatone"));
        generator.delegateItemModel(ModBlocks.GLOW_APPLE_BUSH.asItem(), UnseenWorld.makeID("block/glow_apple_bush/glow_apple_bush_without_fruit"));
        generator.createSimpleFlatItemModel(ModBlocks.HANGING_BURLYWOOD_LEAVES);

        generator.createHangingSign(ModBlocks.STRIPPED_AMETHYST_LOG,
                ModItems.AMETHYST_HANGING_SIGN, ModItems.AMETHYST_WALL_HANGING_SIGN);
        generator.createHangingSign(ModBlocks.STRIPPED_GRIZZLY_LOG,
                ModItems.GRIZZLY_HANGING_SIGN, ModItems.GRIZZLY_WALL_HANGING_SIGN);
        generator.createHangingSign(ModBlocks.STRIPPED_TEALIVE_LOG,
                ModItems.TEALIVE_HANGING_SIGN, ModItems.TEALIVE_WALL_HANGING_SIGN);
        generator.createHangingSign(ModBlocks.STRIPPED_BURLYWOOD_LOG,
                ModItems.BURLYWOOD_HANGING_SIGN, ModItems.BURLYWOOD_WALL_HANGING_SIGN);
        generator.createHangingSign(ModBlocks.STRIPPED_CRIMSONVEIL_LOG,
                ModItems.CRIMSONVEIL_HANGING_SIGN, ModItems.CRIMSONVEIL_WALL_HANGING_SIGN);

        registerGrippingOrientable(generator, ModBlocks.GRIPPING_DARK_CURRANTSLATE);
        registerGrippingOrientable(generator, ModBlocks.GRIPPING_GLACIEMITE);
        generator.createBrushableBlock(ModBlocks.SUSPICIOUS_ASHEN_OOZE);
        generator.createBrushableBlock(ModBlocks.SUSPICIOUS_GLIMMERGRAIN_SAND);

        registerOvergrownLeaves(generator, (LeafDroppingLeaveBlock) ModBlocks.BURLYWOOD_LEAVES);
        registerOvergrownLeaves(generator, (LeafDroppingLeaveBlock) ModBlocks.AMETHYST_LEAVES);

        registerCrimsonveilVines(generator);
        registerBurlywoodFruit(generator, (HangingFruitBlock) ModBlocks.HANGING_BURLYWOOD_LEAVES);

        generator.createFlowerBed(ModBlocks.AMETHYST_PETALS);
        generator.createGrowingPlant(ModBlocks.UMBRAL_KELP, ModBlocks.UMBRAL_KELP_PLANT, BlockModelGenerators.TintState.NOT_TINTED);
        registerTintableLitCross(generator, (DarknessFernBlock) ModBlocks.GLOOMWEED, BlockModelGenerators.TintState.TINTED);
        generator.createDoublePlant(ModBlocks.TALL_GLOOMWEED, BlockModelGenerators.TintState.TINTED);
        registerVerticalPlant(generator, ModBlocks.GRIPPING_SPIGELIA);
        registerCrystalLike(generator, ModBlocks.GRIPCRYSTAL_WART);
        registerCrystalLike(generator, ModBlocks.GRIPTONITE_CLUSTER);

        registerCubeBottomTop(generator, ModBlocks.ENGRAVED_GLACIEMITE_BRICKS, EngravedGlaciemiteBricksBlock.GRIPPED, "_charged", false, false);
        registerCubeBottomTop(generator, ModBlocks.GLIMMERGRAIN_SANDSTONE, null, "", true, false);
        registerCubeBottomTop(generator, ModBlocks.ORDEAL_SPAWNER, OrdealSpawnerBlock.GRIPPING, "_gripped", true, false);

        registerKeyHandler(generator, ModBlocks.KEY_HANDLER_STONE);

        generator.createLantern(ModBlocks.GRIPTONITE_LANTERN);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        for (Map.Entry<Item, ModelTemplate> entry : ModRegistry.ITEM_MODELS.entrySet())
            generator.generateFlatItem(entry.getKey(), entry.getValue());
        generator.generateFlatItem(ModBlocks.HANGING_AMETHYST_LEAVES.asItem(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModBlocks.BEARFRUIT_BRAMBLE.asItem(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModBlocks.MIDNIGHT_LILY_PAD.asItem(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModBlocks.UMBRAL_KELP.asItem(), ModelTemplates.FLAT_ITEM);

        registerBundle(generator, ModItems.GRIPPING_BUNDLE);
        registerGauntlet(generator, ModItems.GRIPPING_GAUNTLET);
        for (Item item : ModRegistry.ITEMS) {
            if (item instanceof ArmorItem armorItem)
                generator.generateArmorTrims(armorItem);
            if (item.getDescriptionId().contains("spawn_egg"))
                generator.generateFlatItem(item, new ModelTemplate(Optional.of(ResourceLocation.tryBuild("minecraft", "item/template_spawn_egg")),
                        Optional.empty()));
        }
    }

    private JsonObject createPredicatedJson(ResourceLocation id, Map<TextureSlot, ResourceLocation> textures, String s, double d) {
        JsonObject jsonObject = ModelTemplates.FLAT_ITEM.createBaseTemplate(id, textures);
        JsonArray overrides = new JsonArray();
        JsonObject override = new JsonObject();
        JsonObject predicate = new JsonObject();
        predicate.addProperty(s, d);
        override.add("predicate", predicate);
        override.addProperty("model", id.withSuffix("_%s".formatted(s)).toString());
        overrides.add(override);
        jsonObject.add("overrides", overrides);
        return jsonObject;
    }

    private void registerBundle(ItemModelGenerators generator, Item item) {
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(TextureMapping.getItemTexture(item)), generator.output,
                (id, textures) -> createPredicatedJson(id, textures, "filled", 0.000001));
        generator.generateFlatItem(item, "_filled", ModelTemplates.FLAT_ITEM);
    }

    private void registerGauntlet(ItemModelGenerators generator, Item item) {
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(TextureMapping.getItemTexture(item)), generator.output,
                (id, textures) -> createPredicatedJson(id, textures, "grip", 1.0));
        generator.generateFlatItem(item, "_grip", ModelTemplates.FLAT_ITEM);
    }

    private void registerTintableLitCross(BlockModelGenerators generator, DarknessFernBlock block, BlockModelGenerators.TintState tintType) {
        generator.createSimpleFlatItemModel(block);
        TextureMapping crossTexture = TextureMapping.cross(block);
        ResourceLocation identifier = tintType.getCross().create(block, crossTexture, generator.modelOutput);
        ResourceLocation identifier2 = generator.createSuffixedVariant(block, "_lit", ModelTemplates.TINTED_CROSS, TextureMapping::cross);
        generator.blockStateOutput
                .accept(MultiVariantGenerator.multiVariant(block).with(BlockModelGenerators.createBooleanModelDispatch(DarknessFernBlock.LIT, identifier2, identifier)));
    }

    private void registerCubeBottomTop(BlockModelGenerators generator, Block block, @Nullable BooleanProperty property, String suffix, boolean bottomDiffer, boolean hasSingleTexture) {
        ResourceLocation sideId = TextureMapping.getBlockTexture(block, "_side");
        ResourceLocation sideSuffixedId = TextureMapping.getBlockTexture(block, "_side" + suffix);
        ResourceLocation topId = TextureMapping.getBlockTexture(block, "_top");
        ResourceLocation topSuffixedId = TextureMapping.getBlockTexture(block, "_top" + suffix);
        ResourceLocation bottomId = bottomDiffer ? TextureMapping.getBlockTexture(block, "_bottom") : topId;
        TextureMapping textureMap = new TextureMapping()
                .put(TextureSlot.SIDE, sideId)
                .put(TextureSlot.TOP, topId)
                .put(TextureSlot.BOTTOM, bottomId);
        TextureMapping textureSuffixedMap = new TextureMapping()
                .put(TextureSlot.SIDE, sideSuffixedId)
                .put(TextureSlot.TOP, topSuffixedId)
                .put(TextureSlot.BOTTOM, bottomId);
        ResourceLocation activeModelId = hasSingleTexture ? TextureMapping.getBlockTexture(block) : ModelTemplates.CUBE_BOTTOM_TOP.create(block, textureMap, generator.modelOutput);
        ResourceLocation suffixedModelId = hasSingleTexture || suffix.isEmpty() ? TextureMapping.getBlockTexture(block, suffix) : ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, suffix, textureSuffixedMap, generator.modelOutput);
        if (property != null) {
            if (block.defaultBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                        .with(PropertyDispatch.properties(property, BlockStateProperties.HORIZONTAL_FACING)
                                .select(false, Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, activeModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
                                .select(true, Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, suffixedModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
                                .select(false, Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, activeModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                                .select(true, Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, suffixedModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                                .select(false, Direction.EAST, Variant.variant().with(VariantProperties.MODEL, activeModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                                .select(true, Direction.EAST, Variant.variant().with(VariantProperties.MODEL, suffixedModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                                .select(false, Direction.WEST, Variant.variant().with(VariantProperties.MODEL, activeModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                                .select(true, Direction.WEST, Variant.variant().with(VariantProperties.MODEL, suffixedModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        )
                );
            } else {
                generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                        .with(PropertyDispatch.property(property)
                                .select(false, Variant.variant().with(VariantProperties.MODEL, activeModelId))
                                .select(true, Variant.variant().with(VariantProperties.MODEL, suffixedModelId))
                        )
                );
            }
        } else {
            if (block.defaultBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                        .with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                                .select(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, activeModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
                                .select(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, activeModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                                .select(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, activeModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                                .select(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, activeModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        )
                );
            } else
                generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, activeModelId));
        }
    }

    private void registerKeyHandler(BlockModelGenerators generator, Block block) {
        TextureSlot layer0 = TextureSlot.create("0");
        ModelTemplate customModel = new ModelTemplate(
                Optional.of(new ResourceLocation("unseen_world", "block/key_handler_stone")),
                Optional.empty(),
                layer0,
                TextureSlot.PARTICLE
        );

        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(KeyHandlerStoneBlock.HANDLER_TYPE)
                        .generate(type -> {
                            String name = type.getSerializedName();
                            ResourceLocation textureId = new ResourceLocation("unseen_world", "block/" + name + "_key_handler");

                            TextureMapping textures = new TextureMapping()
                                    .put(layer0, textureId)
                                    .put(TextureSlot.PARTICLE, textureId);

                            ResourceLocation modelId = customModel.createWithSuffix(block, "_" + name, textures, generator.modelOutput);
                            return Variant.variant().with(VariantProperties.MODEL, modelId);
                        })));
    }

    private void registerOvergrownLeaves(BlockModelGenerators generator, LeafDroppingLeaveBlock block) {
        ResourceLocation identifier = TexturedModel.CUBE.create(block, generator.modelOutput);
        ResourceLocation identifier2 = generator.createSuffixedVariant(block, "_overgrown", ModelTemplates.CUBE_ALL, TextureMapping::cube);
        generator.blockStateOutput
                .accept(MultiVariantGenerator.multiVariant(block).with(BlockModelGenerators.createBooleanModelDispatch(LeafDroppingLeaveBlock.OVERGROWN, identifier2, identifier)));
    }

    private void registerBurlywoodFruit(BlockModelGenerators generator, HangingFruitBlock block) {
        ResourceLocation identifier = ModelTemplates.CROSS.create(block, TextureMapping.cross(block), generator.modelOutput);
        ResourceLocation identifier2 = UnseenWorld.makeID("block/glow_apple_fruit");
        generator.blockStateOutput
                .accept(MultiVariantGenerator.multiVariant(block).with(BlockModelGenerators.createBooleanModelDispatch(HangingFruitBlock.HAS_FRUIT, identifier2, identifier)));
    }

    private void registerCrimsonveilVines(BlockModelGenerators generator) {
        ResourceLocation identifier = generator.createSuffixedVariant(ModBlocks.CRIMSONVEIL_VINE, "", ModelTemplates.CROSS, TextureMapping::cross);
        ResourceLocation identifier2 = generator.createSuffixedVariant(ModBlocks.CRIMSONVEIL_VINE, "_has_berry", ModelTemplates.CROSS, TextureMapping::cross);
        generator.blockStateOutput
                .accept(MultiVariantGenerator.multiVariant(ModBlocks.CRIMSONVEIL_VINE).with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.BERRIES, identifier2, identifier)));
        ResourceLocation identifier3 = generator.createSuffixedVariant(ModBlocks.CRIMSONVEIL_VINE_PLANT, "", ModelTemplates.CROSS, TextureMapping::cross);
        ResourceLocation identifier4 = generator.createSuffixedVariant(ModBlocks.CRIMSONVEIL_VINE_PLANT, "_has_berry", ModelTemplates.CROSS, TextureMapping::cross);
        generator.blockStateOutput
                .accept(MultiVariantGenerator.multiVariant(ModBlocks.CRIMSONVEIL_VINE_PLANT).with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.BERRIES, identifier4, identifier3)));
    }

    private void registerBulbBlock(BlockModelGenerators generator, BulbLikeBlock block) {
        ResourceLocation identifier = TexturedModel.CUBE.create(block, generator.modelOutput);
        ResourceLocation identifier2 = generator.createSuffixedVariant(block, "_litted", ModelTemplates.CUBE_ALL, TextureMapping::cube);
        generator.blockStateOutput
                .accept(MultiVariantGenerator.multiVariant(block).with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, identifier2, identifier)));
    }

    public final void registerGrippingOrientable(BlockModelGenerators generator, Block block) {
        String s = block.equals(ModBlocks.GRIPPING_GLACIEMITE) ? "glaciemite" : "dark_currantslate";
        TextureMapping textureMap = new TextureMapping()
                .put(TextureSlot.TOP, BuiltInRegistries.BLOCK.getKey(block).withPath(path -> "block/" + "gripping_lump"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, ""))
                .put(TextureSlot.BOTTOM, BuiltInRegistries.BLOCK.getKey(block).withPath(path -> "block/" + s))
                .copyForced(TextureSlot.BOTTOM, TextureSlot.PARTICLE);
        ResourceLocation identifier = ModelTemplates.CUBE_BOTTOM_TOP.create(block, textureMap, generator.modelOutput);
        generator.blockStateOutput
                .accept(
                        MultiVariantGenerator.multiVariant(block)
                                .with(
                                        PropertyDispatch.property(BlockStateProperties.FACING)
                                                .select(Direction.DOWN, Variant.variant().with(VariantProperties.MODEL, identifier).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                                                .select(Direction.UP, Variant.variant().with(VariantProperties.MODEL, identifier))
                                                .select(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, identifier).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                                                .select(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, identifier).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                                                .select(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, identifier).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                                                .select(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, identifier).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                                )
                );
    }

    private void registerDynamicTopSoils(ResourceLocation bottomTexture, BlockModelGenerators generator, Block... topSoilBlocks) {
        for (Block topSoil : topSoilBlocks) {
            TextureMapping textureMap = new TextureMapping()
                    .put(TextureSlot.BOTTOM, bottomTexture)
                    .copyForced(TextureSlot.BOTTOM, TextureSlot.PARTICLE)
                    .put(TextureSlot.TOP, TextureMapping.getBlockTexture(topSoil, "_top"))
                    .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(topSoil, "_side"));

            Variant blockStateVariant = Variant.variant()
                    .with(VariantProperties.MODEL, ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(topSoil, "_snow", textureMap, generator.modelOutput));

            ResourceLocation modelId = TexturedModel.CUBE_TOP_BOTTOM
                    .get(topSoil)
                    .updateTextures(textures -> textures.put(TextureSlot.BOTTOM, bottomTexture))
                    .create(topSoil, generator.modelOutput);

            List<Variant> list = Arrays.asList(BlockModelGenerators.createRotatedVariants(modelId));

            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(topSoil)
                            .with(PropertyDispatch.property(BlockStateProperties.SNOWY)
                                    .select(true, blockStateVariant)
                                    .select(false, list))
            );
        }
    }

    private void registerVerticalPlant(BlockModelGenerators generator, Block block) {
        generator.createSimpleFlatItemModel(block.asItem());
        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(
                                PropertyDispatch.properties(BlockStateProperties.VERTICAL_DIRECTION, BlockStateProperties.AGE_2)
                                        .select(Direction.UP, 0, Variant.variant().with(VariantProperties.MODEL, ModelTemplates.CROSS.createWithSuffix(block, "_stage0", TextureMapping.cross(TextureMapping.getBlockTexture(block, "_stage0")), generator.modelOutput)))
                                        .select(Direction.UP, 1, Variant.variant().with(VariantProperties.MODEL, ModelTemplates.CROSS.createWithSuffix(block, "_stage1", TextureMapping.cross(TextureMapping.getBlockTexture(block, "_stage1")), generator.modelOutput)))
                                        .select(Direction.UP, 2, Variant.variant().with(VariantProperties.MODEL, ModelTemplates.CROSS.createWithSuffix(block, "_stage2", TextureMapping.cross(TextureMapping.getBlockTexture(block, "_stage2")), generator.modelOutput)))
                                        .select(Direction.DOWN, 0, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(block, "_stage0")).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                                        .select(Direction.DOWN, 1, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(block, "_stage1")).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                                        .select(Direction.DOWN, 2, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(block, "_stage2")).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                        )
        );
    }


    private void registerCrystalLike(BlockModelGenerators generator, Block block) {
        generator.createSimpleFlatItemModel(block.asItem());
        generator.blockStateOutput
                .accept(
                        MultiVariantGenerator.multiVariant(
                                        block, Variant.variant().with(VariantProperties.MODEL, ModelTemplates.CROSS.create(block, TextureMapping.cross(block), generator.modelOutput))
                                )
                                .with(generator.createColumnWithFacing())
                );
    }
}