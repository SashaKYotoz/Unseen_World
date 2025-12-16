package net.sashakyotoz.common.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.BulbLikeBlock;
import net.sashakyotoz.common.blocks.custom.plants.DarknessFernBlock;
import net.sashakyotoz.common.blocks.custom.plants.HangingFruitBlock;
import net.sashakyotoz.common.blocks.custom.plants.LeafDroppingLeaveBlock;
import net.sashakyotoz.common.items.ModItems;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        BlockStateModelGenerator.BlockTexturePool pool;
        BlockStateModelGenerator.LogTexturePool poollog;

        for (Block block : ModRegistry.getModelList(ModRegistry.Models.CUBE))
            generator.registerSimpleCubeAll(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.GRASS))
            registerDynamicTopSoils(TextureMap.getId(block), generator, block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.PILLAR))
            generator.registerLog(block).log(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.CROSS))
            generator.registerTintableCross(block, BlockStateModelGenerator.TintType.NOT_TINTED);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.DOOR))
            generator.registerDoor(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.TRAPDOOR))
            generator.registerOrientableTrapdoor(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.FLUID))
            generator.registerSimpleState(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.BULB))
            registerBulbBlock(generator, (BulbLikeBlock) block);
        for (Block block : ModRegistry.BLOCK_SETS.keySet()) {
            if (ModRegistry.BLOCK_SETS.get(block).containsKey(ModRegistry.Models.WOOD)) {
                poollog = generator.registerLog(block);
                poollog.log(block);
                for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(block).entrySet()) {
                    if (entry.getKey() == ModRegistry.Models.WOOD)
                        poollog.wood(entry.getValue());
                }
            } else if (ModRegistry.BLOCK_SETS.get(block).containsKey(ModRegistry.Models.PANE)) {
                for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(block).entrySet()) {
                    if (entry.getKey() == ModRegistry.Models.PANE)
                        generator.registerGlassPane(block, entry.getValue());
                }
            } else {
                pool = generator.registerCubeAllModelTexturePool(block);
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
                        pool.family(new BlockFamily.Builder(block)
                                .sign(entry.getValue(), ModRegistry.BLOCK_SETS.get(block).get(ModRegistry.Models.WALL_SIGN))
                                .build());
                }
            }
        }
        generator.registerParentedItemModel(ModBlocks.KEY_HANDLER_STONE.asItem(), UnseenWorld.makeID("block/key_handler_stone"));
        generator.registerParentedItemModel(ModBlocks.GLACIEMITE_TRANSLOCATONE.asItem(), UnseenWorld.makeID("block/glaciemite_translocatone"));
        generator.registerParentedItemModel(ModBlocks.GLOW_APPLE_BUSH.asItem(), UnseenWorld.makeID("block/glow_apple_bush/glow_apple_bush_without_fruit"));
        generator.registerItemModel(ModBlocks.HANGING_BURLYWOOD_LEAVES);

        generator.registerHangingSign(ModBlocks.STRIPPED_AMETHYST_LOG,
                ModItems.AMETHYST_HANGING_SIGN, ModItems.AMETHYST_WALL_HANGING_SIGN);
        generator.registerHangingSign(ModBlocks.STRIPPED_GRIZZLY_LOG,
                ModItems.GRIZZLY_HANGING_SIGN, ModItems.GRIZZLY_WALL_HANGING_SIGN);
        generator.registerHangingSign(ModBlocks.STRIPPED_TEALIVE_LOG,
                ModItems.TEALIVE_HANGING_SIGN, ModItems.TEALIVE_WALL_HANGING_SIGN);
        generator.registerHangingSign(ModBlocks.STRIPPED_BURLYWOOD_LOG,
                ModItems.BURLYWOOD_HANGING_SIGN, ModItems.BURLYWOOD_WALL_HANGING_SIGN);
        generator.registerHangingSign(ModBlocks.STRIPPED_CRIMSONVEIL_LOG,
                ModItems.CRIMSONVEIL_HANGING_SIGN, ModItems.CRIMSONVEIL_WALL_HANGING_SIGN);

        registerGrippingOrientable(generator, ModBlocks.GRIPPING_DARK_CURRANTSLATE);
        registerGrippingOrientable(generator, ModBlocks.GRIPPING_GLACIEMITE);
        generator.registerBrushableBlock(ModBlocks.SUSPICIOUS_ASHEN_OOZE);
        generator.registerBrushableBlock(ModBlocks.SUSPICIOUS_GLIMMERGRAIN_SAND);

        registerOvergrownLeaves(generator, (LeafDroppingLeaveBlock) ModBlocks.BURLYWOOD_LEAVES);
        registerOvergrownLeaves(generator, (LeafDroppingLeaveBlock) ModBlocks.AMETHYST_LEAVES);

        registerCrimsonveilVines(generator);
        registerBurlywoodFruit(generator, (HangingFruitBlock) ModBlocks.HANGING_BURLYWOOD_LEAVES);

        generator.registerFlowerbed(ModBlocks.AMETHYST_PETALS);
        generator.registerPlantPart(ModBlocks.UMBRAL_KELP, ModBlocks.UMBRAL_KELP_PLANT, BlockStateModelGenerator.TintType.NOT_TINTED);
        registerTintableLitCross(generator, (DarknessFernBlock) ModBlocks.GLOOMWEED, BlockStateModelGenerator.TintType.TINTED);
        generator.registerDoubleBlock(ModBlocks.TALL_GLOOMWEED, BlockStateModelGenerator.TintType.TINTED);
        registerVerticalPlant(generator, ModBlocks.GRIPPING_SPIGELIA);
        registerCrystalLike(generator, ModBlocks.GRIPCRYSTAL_WART);
        generator.registerLantern(ModBlocks.GRIPTONITE_LANTERN);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        for (Map.Entry<Item, Model> entry : ModRegistry.ITEM_MODELS.entrySet())
            generator.register(entry.getKey(), entry.getValue());
        generator.register(ModBlocks.HANGING_AMETHYST_LEAVES.asItem(), Models.GENERATED);
        generator.register(ModBlocks.BEARFRUIT_BRAMBLE.asItem(), Models.GENERATED);
        generator.register(ModBlocks.MIDNIGHT_LILY_PAD.asItem(), Models.GENERATED);
        generator.register(ModBlocks.UMBRAL_KELP.asItem(), Models.GENERATED);

        registerBundle(generator, ModItems.GRIPPING_BUNDLE);
        registerGauntlet(generator, ModItems.GRIPPING_GAUNTLET);
        for (Item item : ModRegistry.ITEMS) {
            if (item instanceof ArmorItem armorItem)
                generator.registerArmor(armorItem);
            if (item.getTranslationKey().contains("spawn_egg"))
                generator.register(item, new Model(Optional.of(Identifier.of("minecraft", "item/template_spawn_egg")),
                        Optional.empty()));
        }
    }

    private JsonObject createPredicatedJson(Identifier id, Map<TextureKey, Identifier> textures, String s, double d) {
        JsonObject jsonObject = Models.GENERATED.createJson(id, textures);
        JsonArray overrides = new JsonArray();
        JsonObject override = new JsonObject();
        JsonObject predicate = new JsonObject();
        predicate.addProperty(s, d);
        override.add("predicate", predicate);
        override.addProperty("model", id.withSuffixedPath("_%s".formatted(s)).toString());
        overrides.add(override);
        jsonObject.add("overrides", overrides);
        return jsonObject;
    }

    private void registerBundle(ItemModelGenerator generator, Item item) {
        Models.GENERATED.upload(ModelIds.getItemModelId(item), TextureMap.layer0(TextureMap.getId(item)), generator.writer,
                (id, textures) -> createPredicatedJson(id, textures, "filled", 0.000001));
        generator.register(item, "_filled", Models.GENERATED);
    }

    private void registerGauntlet(ItemModelGenerator generator, Item item) {
        Models.GENERATED.upload(ModelIds.getItemModelId(item), TextureMap.layer0(TextureMap.getId(item)), generator.writer,
                (id, textures) -> createPredicatedJson(id, textures, "grip", 1.0));
        generator.register(item, "_grip", Models.GENERATED);
    }

    private void registerTintableLitCross(BlockStateModelGenerator generator, DarknessFernBlock block, BlockStateModelGenerator.TintType tintType) {
        generator.registerItemModel(block);
        TextureMap crossTexture = TextureMap.cross(block);
        Identifier identifier = tintType.getCrossModel().upload(block, crossTexture, generator.modelCollector);
        Identifier identifier2 = generator.createSubModel(block, "_lit", Models.TINTED_CROSS, TextureMap::cross);
        generator.blockStateCollector
                .accept(VariantsBlockStateSupplier.create(block).coordinate(BlockStateModelGenerator.createBooleanModelMap(DarknessFernBlock.LIT, identifier2, identifier)));
    }

    private void registerOvergrownLeaves(BlockStateModelGenerator generator, LeafDroppingLeaveBlock block) {
        Identifier identifier = TexturedModel.CUBE_ALL.upload(block, generator.modelCollector);
        Identifier identifier2 = generator.createSubModel(block, "_overgrown", Models.CUBE_ALL, TextureMap::all);
        generator.blockStateCollector
                .accept(VariantsBlockStateSupplier.create(block).coordinate(BlockStateModelGenerator.createBooleanModelMap(LeafDroppingLeaveBlock.OVERGROWN, identifier2, identifier)));
    }

    private void registerBurlywoodFruit(BlockStateModelGenerator generator, HangingFruitBlock block) {
        Identifier identifier = Models.CROSS.upload(block, TextureMap.cross(block), generator.modelCollector);
        Identifier identifier2 = UnseenWorld.makeID("block/glow_apple_fruit");
        generator.blockStateCollector
                .accept(VariantsBlockStateSupplier.create(block).coordinate(BlockStateModelGenerator.createBooleanModelMap(HangingFruitBlock.HAS_FRUIT, identifier2, identifier)));
    }

    private void registerCrimsonveilVines(BlockStateModelGenerator generator) {
        Identifier identifier = generator.createSubModel(ModBlocks.CRIMSONVEIL_VINE, "", Models.CROSS, TextureMap::cross);
        Identifier identifier2 = generator.createSubModel(ModBlocks.CRIMSONVEIL_VINE, "_has_berry", Models.CROSS, TextureMap::cross);
        generator.blockStateCollector
                .accept(VariantsBlockStateSupplier.create(ModBlocks.CRIMSONVEIL_VINE).coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.BERRIES, identifier2, identifier)));
        Identifier identifier3 = generator.createSubModel(ModBlocks.CRIMSONVEIL_VINE_PLANT, "", Models.CROSS, TextureMap::cross);
        Identifier identifier4 = generator.createSubModel(ModBlocks.CRIMSONVEIL_VINE_PLANT, "_has_berry", Models.CROSS, TextureMap::cross);
        generator.blockStateCollector
                .accept(VariantsBlockStateSupplier.create(ModBlocks.CRIMSONVEIL_VINE_PLANT).coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.BERRIES, identifier4, identifier3)));
    }

    private void registerBulbBlock(BlockStateModelGenerator generator, BulbLikeBlock block) {
        Identifier identifier = TexturedModel.CUBE_ALL.upload(block, generator.modelCollector);
        Identifier identifier2 = generator.createSubModel(block, "_litted", Models.CUBE_ALL, TextureMap::all);
        generator.blockStateCollector
                .accept(VariantsBlockStateSupplier.create(block).coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.LIT, identifier2, identifier)));
    }

    public final void registerGrippingOrientable(BlockStateModelGenerator generator, Block block) {
        String s = block.equals(ModBlocks.GRIPPING_GLACIEMITE) ? "glaciemite" : "dark_currantslate";
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TOP, Registries.BLOCK.getId(block).withPath(path -> "block/" + "gripping_lump"))
                .put(TextureKey.SIDE, TextureMap.getSubId(block, ""))
                .put(TextureKey.BOTTOM, Registries.BLOCK.getId(block).withPath(path -> "block/" + s))
                .inherit(TextureKey.BOTTOM, TextureKey.PARTICLE);
        Identifier identifier = Models.CUBE_BOTTOM_TOP.upload(block, textureMap, generator.modelCollector);
        generator.blockStateCollector
                .accept(
                        VariantsBlockStateSupplier.create(block)
                                .coordinate(
                                        BlockStateVariantMap.create(Properties.FACING)
                                                .register(Direction.DOWN, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.X, VariantSettings.Rotation.R180))
                                                .register(Direction.UP, BlockStateVariant.create().put(VariantSettings.MODEL, identifier))
                                                .register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R0).put(VariantSettings.X, VariantSettings.Rotation.R90))
                                                .register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R90).put(VariantSettings.X, VariantSettings.Rotation.R90))
                                                .register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R180).put(VariantSettings.X, VariantSettings.Rotation.R90))
                                                .register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R270).put(VariantSettings.X, VariantSettings.Rotation.R90))
                                )
                );
    }

    private void registerDynamicTopSoils(Identifier bottomTexture, BlockStateModelGenerator generator, Block... topSoilBlocks) {
        for (Block topSoil : topSoilBlocks) {
            TextureMap textureMap = new TextureMap()
                    .put(TextureKey.BOTTOM, bottomTexture)
                    .inherit(TextureKey.BOTTOM, TextureKey.PARTICLE)
                    .put(TextureKey.TOP, TextureMap.getSubId(topSoil, "_top"))
                    .put(TextureKey.SIDE, TextureMap.getSubId(topSoil, "_side"));

            BlockStateVariant blockStateVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, Models.CUBE_BOTTOM_TOP.upload(topSoil, "_snow", textureMap, generator.modelCollector));

            Identifier modelId = TexturedModel.CUBE_BOTTOM_TOP
                    .get(topSoil)
                    .textures(textures -> textures.put(TextureKey.BOTTOM, bottomTexture))
                    .upload(topSoil, generator.modelCollector);

            List<BlockStateVariant> list = Arrays.asList(BlockStateModelGenerator.createModelVariantWithRandomHorizontalRotations(modelId));

            generator.blockStateCollector.accept(
                    VariantsBlockStateSupplier.create(topSoil)
                            .coordinate(BlockStateVariantMap.create(Properties.SNOWY)
                                    .register(true, blockStateVariant)
                                    .register(false, list))
            );
        }
    }

    private void registerVerticalPlant(BlockStateModelGenerator generator, Block block) {
        generator.registerItemModel(block.asItem());
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block)
                        .coordinate(
                                BlockStateVariantMap.create(Properties.VERTICAL_DIRECTION, Properties.AGE_2)
                                        .register(Direction.UP, 0, BlockStateVariant.create().put(VariantSettings.MODEL, Models.CROSS.upload(block, "_stage0", TextureMap.cross(TextureMap.getSubId(block, "_stage0")), generator.modelCollector)))
                                        .register(Direction.UP, 1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.CROSS.upload(block, "_stage1", TextureMap.cross(TextureMap.getSubId(block, "_stage1")), generator.modelCollector)))
                                        .register(Direction.UP, 2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.CROSS.upload(block, "_stage2", TextureMap.cross(TextureMap.getSubId(block, "_stage2")), generator.modelCollector)))
                                        .register(Direction.DOWN, 0, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(block, "_stage0")).put(VariantSettings.X, VariantSettings.Rotation.R180))
                                        .register(Direction.DOWN, 1, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(block, "_stage1")).put(VariantSettings.X, VariantSettings.Rotation.R180))
                                        .register(Direction.DOWN, 2, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(block, "_stage2")).put(VariantSettings.X, VariantSettings.Rotation.R180))
                        )
        );
    }


    private void registerCrystalLike(BlockStateModelGenerator generator, Block block) {
        generator.registerItemModel(block.asItem());
        generator.blockStateCollector
                .accept(
                        VariantsBlockStateSupplier.create(
                                        block, BlockStateVariant.create().put(VariantSettings.MODEL, Models.CROSS.upload(block, TextureMap.cross(block), generator.modelCollector))
                                )
                                .coordinate(generator.createUpDefaultFacingVariantMap())
                );
    }
}