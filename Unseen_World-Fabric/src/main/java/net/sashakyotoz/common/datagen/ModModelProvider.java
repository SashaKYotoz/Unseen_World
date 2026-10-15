package net.sashakyotoz.common.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
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
import net.sashakyotoz.common.blocks.custom.EngravedBricksBlock;
import net.sashakyotoz.common.blocks.custom.OrdealSpawnerBlock;
import net.sashakyotoz.common.blocks.custom.plants.DarknessFernBlock;
import net.sashakyotoz.common.blocks.custom.plants.HangingFruitBlock;
import net.sashakyotoz.common.blocks.custom.plants.LeafDroppingLeaveBlock;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ModModelProvider extends FabricModelProvider {
    private static final Set<Block> ITEM_TEXTURE_CROSS = Set.of(ModBlocks.ASHREED);

    private static final Map<Block, Block> CORAL_FANS = Map.of(
            ModBlocks.UMBRAL_CORAL_FAN, ModBlocks.UMBRAL_CORAL_WALL_FAN,
            ModBlocks.DEAD_UMBRAL_CORAL_FAN, ModBlocks.DEAD_UMBRAL_CORAL_WALL_FAN
    );

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        ModRegistry.getModelList(ModRegistry.Models.CUBE).forEach(generator::createTrivialCube);
        ModRegistry.getModelList(ModRegistry.Models.GRASS).forEach(block -> registerTopSoil(generator, block));
        ModRegistry.getModelList(ModRegistry.Models.PILLAR).forEach(block -> generator.woodProvider(block).logWithHorizontal(block));
        ModRegistry.getModelList(ModRegistry.Models.CROSS).stream()
                .filter(block -> !ITEM_TEXTURE_CROSS.contains(block))
                .forEach(block -> generator.createCrossBlockWithDefaultItem(block, BlockModelGenerators.TintState.NOT_TINTED));
        ModRegistry.getModelList(ModRegistry.Models.DOOR).forEach(generator::createDoor);
        ModRegistry.getModelList(ModRegistry.Models.TRAPDOOR).forEach(generator::createOrientableTrapdoor);
        ModRegistry.getModelList(ModRegistry.Models.FLUID).forEach(generator::createNonTemplateModelBlock);
        ModRegistry.getModelList(ModRegistry.Models.BULB).forEach(block -> registerCubeToggle(generator, block, BlockStateProperties.LIT, "_litted"));
        ModRegistry.BLOCK_SETS.forEach((parent, variants) -> registerBlockSet(generator, parent, variants));

        ITEM_TEXTURE_CROSS.forEach(block -> registerCrossWithItemTexture(generator, block));

        CORAL_FANS.forEach((fan, wallFan) -> registerCoralFan(generator, fan, wallFan));

        generator.delegateItemModel(ModBlocks.GLOW_APPLE_BUSH.asItem(), UnseenWorld.MOD.makeID("block/glow_apple_bush/glow_apple_bush_without_fruit"));
        generator.createSimpleFlatItemModel(ModBlocks.HANGING_BURLYWOOD_LEAVES);

        ResourceLocation hoarfrost = TexturedModel.CARPET.create(ModBlocks.HOARFROST, generator.modelOutput);
        generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(ModBlocks.HOARFROST, hoarfrost));

        generator.createHangingSign(ModBlocks.STRIPPED_AMETHYST_LOG, ModItems.AMETHYST_HANGING_SIGN, ModItems.AMETHYST_WALL_HANGING_SIGN);
        generator.createHangingSign(ModBlocks.STRIPPED_GRIZZLY_LOG, ModItems.GRIZZLY_HANGING_SIGN, ModItems.GRIZZLY_WALL_HANGING_SIGN);
        generator.createHangingSign(ModBlocks.STRIPPED_TEALIVE_LOG, ModItems.TEALIVE_HANGING_SIGN, ModItems.TEALIVE_WALL_HANGING_SIGN);
        generator.createHangingSign(ModBlocks.STRIPPED_BURLYWOOD_LOG, ModItems.BURLYWOOD_HANGING_SIGN, ModItems.BURLYWOOD_WALL_HANGING_SIGN);
        generator.createHangingSign(ModBlocks.STRIPPED_CRIMSONVEIL_LOG, ModItems.CRIMSONVEIL_HANGING_SIGN, ModItems.CRIMSONVEIL_WALL_HANGING_SIGN);

        registerOrientableLump(generator, ModBlocks.GRIPPING_DARK_CURRANTSLATE, "gripping_lump", "dark_currantslate");
        registerOrientableLump(generator, ModBlocks.GRIPPING_GLACIEMITE, "gripping_lump", "glaciemite");
        registerOrientableLump(generator, ModBlocks.GRIPPING_CORONA_STONE, "gripping_lump", "corona_stone");
        registerOrientableLump(generator, ModBlocks.GRIPTONITED_DARK_CURRANTSLATE, "griptonited_lump", "dark_currantslate");
        registerOrientableLump(generator, ModBlocks.GRIPTONITED_GLACIEMITE, "griptonited_lump", "glaciemite");
        registerOrientableLump(generator, ModBlocks.GRIPTONITED_CORONA_STONE, "griptonited_lump", "corona_stone");
        generator.createBrushableBlock(ModBlocks.SUSPICIOUS_ASHEN_OOZE);
        generator.createBrushableBlock(ModBlocks.SUSPICIOUS_GLIMMERGRAIN_SAND);

        registerCubeToggle(generator, ModBlocks.BURLYWOOD_LEAVES, LeafDroppingLeaveBlock.OVERGROWN, "_overgrown");
        registerCubeToggle(generator, ModBlocks.AMETHYST_LEAVES, LeafDroppingLeaveBlock.OVERGROWN, "_overgrown");

        List.of(ModBlocks.CRIMSONVEIL_VINE, ModBlocks.CRIMSONVEIL_VINE_PLANT, ModBlocks.GLOWROOT_VINE, ModBlocks.GLOWROOT_VINE_PLANT)
                .forEach(vine -> registerBerryCross(generator, vine));
        registerBurlywoodFruit(generator, (HangingFruitBlock) ModBlocks.HANGING_BURLYWOOD_LEAVES);

        generator.createFlowerBed(ModBlocks.AMETHYST_PETALS);
        generator.createGrowingPlant(ModBlocks.UMBRAL_KELP, ModBlocks.UMBRAL_KELP_PLANT, BlockModelGenerators.TintState.NOT_TINTED);
        registerTintableLitCross(generator, (DarknessFernBlock) ModBlocks.GLOOMWEED, BlockModelGenerators.TintState.TINTED);
        generator.createDoublePlant(ModBlocks.TALL_GLOOMWEED, BlockModelGenerators.TintState.TINTED);
        registerVerticalPlant(generator, ModBlocks.GRIPPING_SPIGELIA);
        registerCrystalLike(generator, ModBlocks.GRIPCRYSTAL_WART);
        registerCrystalLike(generator, ModBlocks.GRIPTONITE_CLUSTER);

        registerCubeBottomTop(generator, ModBlocks.ENGRAVED_GLACIEMITE_BRICKS, EngravedBricksBlock.GRIPPED, "_charged", false);
        registerCubeBottomTop(generator, ModBlocks.ENGRAVED_CORONA_STONE_BRICKS, EngravedBricksBlock.GRIPPED, "_charged", false);
        registerCubeBottomTop(generator, ModBlocks.GLIMMERGRAIN_SANDSTONE, null, "", true);
        registerCubeBottomTop(generator, ModBlocks.ORDEAL_SPAWNER, OrdealSpawnerBlock.GRIPPING, "_gripped", true);

        generator.createLantern(ModBlocks.GRIPTONITE_LANTERN);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        ModRegistry.ITEM_MODELS.forEach((item, template) -> {
            if (!isItemTextureCross(item)) {
                generator.generateFlatItem(item, template);
            }
        });
        List.of(ModBlocks.HANGING_AMETHYST_LEAVES, ModBlocks.BEARFRUIT_BRAMBLE, ModBlocks.MIDNIGHT_LILY_PAD, ModBlocks.UMBRAL_KELP)
                .forEach(block -> generator.generateFlatItem(block.asItem(), ModelTemplates.FLAT_ITEM));

        registerPredicatedItem(generator, ModItems.GRIPPING_BUNDLE, "filled", 0.000001);
        registerPredicatedItem(generator, ModItems.GRIPPING_GAUNTLET, "grip", 1.0);

        ModelTemplate spawnEgg = new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/template_spawn_egg")), Optional.empty());
        for (Item item : ModRegistry.ITEMS) {
            if (item instanceof ArmorItem armorItem) {
                generator.generateArmorTrims(armorItem);
            }
            if (item.getDescriptionId().contains("spawn_egg")) {
                generator.generateFlatItem(item, spawnEgg);
            }
        }
    }

    private static boolean isItemTextureCross(Item item) {
        for (Block block : ITEM_TEXTURE_CROSS) {
            if (block.asItem() == item) {
                return true;
            }
        }
        return false;
    }

    private void registerCoralFan(BlockModelGenerators generator, Block fan, Block wallFan) {
        TextureMapping mapping = TextureMapping.fan(fan);
        ResourceLocation fanModel = ModelTemplates.CORAL_FAN.create(fan, mapping, generator.modelOutput);
        generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(fan, fanModel));
        ResourceLocation wallModel = ModelTemplates.CORAL_WALL_FAN.create(wallFan, mapping, generator.modelOutput);
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(wallFan, variant(wallModel))
                .with(BlockModelGenerators.createHorizontalFacingDispatch()));
        generator.createSimpleFlatItemModel(fan);
    }

    private static ResourceLocation blockTexture(String name) {
        return UnseenWorld.MOD.makeID("block/" + name);
    }

    private static Variant variant(ResourceLocation model) {
        return Variant.variant().with(VariantProperties.MODEL, model);
    }

    private static VariantProperties.Rotation yRotation(Direction direction) {
        return switch (direction) {
            case EAST -> VariantProperties.Rotation.R90;
            case SOUTH -> VariantProperties.Rotation.R180;
            case WEST -> VariantProperties.Rotation.R270;
            default -> VariantProperties.Rotation.R0;
        };
    }

    private static Variant horizontalVariant(ResourceLocation model, Direction direction) {
        return variant(model).with(VariantProperties.Y_ROT, yRotation(direction));
    }

    private static Variant facingVariant(ResourceLocation model, Direction direction) {
        return switch (direction) {
            case DOWN -> variant(model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180);
            case UP -> variant(model);
            default -> horizontalVariant(model, direction).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90);
        };
    }

    private void registerBlockSet(BlockModelGenerators generator, Block parent, Map<ModRegistry.Models, Block> variants) {
        if (variants.containsKey(ModRegistry.Models.WOOD)) {
            BlockModelGenerators.WoodProvider logs = generator.woodProvider(parent);
            logs.logWithHorizontal(parent);
            logs.wood(variants.get(ModRegistry.Models.WOOD));
        } else if (variants.containsKey(ModRegistry.Models.PANE)) {
            generator.createGlassBlocks(parent, variants.get(ModRegistry.Models.PANE));
        } else {
            BlockModelGenerators.BlockFamilyProvider family = generator.family(parent);
            variants.forEach((type, variant) -> registerFamilyMember(family, parent, variants, type, variant));
        }
    }

    private void registerFamilyMember(BlockModelGenerators.BlockFamilyProvider family, Block parent, Map<ModRegistry.Models, Block> variants, ModRegistry.Models type, Block variant) {
        switch (type) {
            case STAIRS -> family.stairs(variant);
            case SLAB -> family.slab(variant);
            case BUTTON -> family.button(variant);
            case PRESSURE_PLATE -> family.pressurePlate(variant);
            case FENCE -> family.fence(variant);
            case WALL -> family.wall(variant);
            case FENCE_GATE -> family.fenceGate(variant);
            case SIGN -> family.generateFor(new BlockFamily.Builder(parent)
                    .sign(variant, variants.get(ModRegistry.Models.WALL_SIGN))
                    .getFamily());
            default -> {
            }
        }
    }

    private void registerCrossWithItemTexture(BlockModelGenerators generator, Block block) {
        ResourceLocation model = ModelTemplates.CROSS.create(block, TextureMapping.cross(block), generator.modelOutput);
        generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
        generator.createSimpleFlatItemModel(block.asItem());
    }

    private void registerCubeToggle(BlockModelGenerators generator, Block block, BooleanProperty property, String suffix) {
        ResourceLocation off = TexturedModel.CUBE.create(block, generator.modelOutput);
        ResourceLocation on = generator.createSuffixedVariant(block, suffix, ModelTemplates.CUBE_ALL, TextureMapping::cube);
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createBooleanModelDispatch(property, on, off)));
    }

    private void registerBerryCross(BlockModelGenerators generator, Block block) {
        ResourceLocation plain = generator.createSuffixedVariant(block, "", ModelTemplates.CROSS, TextureMapping::cross);
        ResourceLocation berry = generator.createSuffixedVariant(block, "_has_berry", ModelTemplates.CROSS, TextureMapping::cross);
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.BERRIES, berry, plain)));
    }

    private void registerBurlywoodFruit(BlockModelGenerators generator, HangingFruitBlock block) {
        ResourceLocation plain = ModelTemplates.CROSS.create(block, TextureMapping.cross(block), generator.modelOutput);
        ResourceLocation fruit = UnseenWorld.MOD.makeID("block/glow_apple_fruit");
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createBooleanModelDispatch(HangingFruitBlock.HAS_FRUIT, fruit, plain)));
    }

    private void registerTintableLitCross(BlockModelGenerators generator, DarknessFernBlock block, BlockModelGenerators.TintState tintType) {
        generator.createSimpleFlatItemModel(block);
        ResourceLocation unlit = tintType.getCross().create(block, TextureMapping.cross(block), generator.modelOutput);
        ResourceLocation lit = generator.createSuffixedVariant(block, "_lit", ModelTemplates.TINTED_CROSS, TextureMapping::cross);
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createBooleanModelDispatch(DarknessFernBlock.LIT, lit, unlit)));
    }

    private void registerOrientableLump(BlockModelGenerators generator, Block block, String lumpTexture, String baseTexture) {
        TextureMapping textures = new TextureMapping()
                .put(TextureSlot.TOP, blockTexture(lumpTexture))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block))
                .put(TextureSlot.BOTTOM, blockTexture(baseTexture))
                .copyForced(TextureSlot.BOTTOM, TextureSlot.PARTICLE);
        ResourceLocation model = ModelTemplates.CUBE_BOTTOM_TOP.create(block, textures, generator.modelOutput);
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(BlockStateProperties.FACING)
                        .generate(direction -> facingVariant(model, direction))));
    }

    private ResourceLocation createCubeBottomTop(BlockModelGenerators generator, Block block, String suffix, ResourceLocation bottom) {
        TextureMapping textures = new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side" + suffix))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top" + suffix))
                .put(TextureSlot.BOTTOM, bottom);
        return suffix.isEmpty()
                ? ModelTemplates.CUBE_BOTTOM_TOP.create(block, textures, generator.modelOutput)
                : ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, suffix, textures, generator.modelOutput);
    }

    private void registerCubeBottomTop(BlockModelGenerators generator, Block block, @Nullable BooleanProperty property, String suffix, boolean separateBottom) {
        ResourceLocation bottom = TextureMapping.getBlockTexture(block, separateBottom ? "_bottom" : "_top");
        ResourceLocation active = createCubeBottomTop(generator, block, "", bottom);
        boolean rotates = block.defaultBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING);
        if (property == null) {
            if (rotates) {
                generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                        .with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                                .generate(direction -> horizontalVariant(active, direction))));
            } else {
                generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, active));
            }
            return;
        }
        ResourceLocation alternate = createCubeBottomTop(generator, block, suffix, bottom);
        if (rotates) {
            PropertyDispatch.C2<Boolean, Direction> dispatch = PropertyDispatch.properties(property, BlockStateProperties.HORIZONTAL_FACING);
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                dispatch.select(false, direction, horizontalVariant(active, direction));
                dispatch.select(true, direction, horizontalVariant(alternate, direction));
            }
            generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(dispatch));
        } else {
            generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                    .with(PropertyDispatch.property(property)
                            .select(false, variant(active))
                            .select(true, variant(alternate))));
        }
    }

    private void registerTopSoil(BlockModelGenerators generator, Block block) {
        ResourceLocation bottom = TextureMapping.getBlockTexture(block);
        TextureMapping snowyTextures = new TextureMapping()
                .put(TextureSlot.BOTTOM, bottom)
                .copyForced(TextureSlot.BOTTOM, TextureSlot.PARTICLE)
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"));
        Variant snowy = variant(ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_snow", snowyTextures, generator.modelOutput));
        ResourceLocation model = TexturedModel.CUBE_TOP_BOTTOM
                .get(block)
                .updateTextures(textures -> textures.put(TextureSlot.BOTTOM, bottom))
                .create(block, generator.modelOutput);
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(BlockStateProperties.SNOWY)
                        .select(true, snowy)
                        .select(false, List.of(BlockModelGenerators.createRotatedVariants(model)))));
    }

    private void registerVerticalPlant(BlockModelGenerators generator, Block block) {
        generator.createSimpleFlatItemModel(block.asItem());
        ResourceLocation[] stages = new ResourceLocation[3];
        for (int age = 0; age < stages.length; age++) {
            String suffix = "_stage" + age;
            stages[age] = ModelTemplates.CROSS.createWithSuffix(block, suffix, TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix)), generator.modelOutput);
        }
        PropertyDispatch.C2<Direction, Integer> dispatch = PropertyDispatch.properties(BlockStateProperties.VERTICAL_DIRECTION, BlockStateProperties.AGE_2);
        for (int age = 0; age < stages.length; age++) {
            dispatch.select(Direction.UP, age, variant(stages[age]));
            dispatch.select(Direction.DOWN, age, variant(stages[age]).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180));
        }
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(dispatch));
    }

    private void registerCrystalLike(BlockModelGenerators generator, Block block) {
        generator.createSimpleFlatItemModel(block.asItem());
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(
                        block, variant(ModelTemplates.CROSS.create(block, TextureMapping.cross(block), generator.modelOutput)))
                .with(generator.createColumnWithFacing()));
    }

    private void registerPredicatedItem(ItemModelGenerators generator, Item item, String predicate, double threshold) {
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(TextureMapping.getItemTexture(item)), generator.output,
                (id, textures) -> createPredicatedJson(id, textures, predicate, threshold));
        generator.generateFlatItem(item, "_" + predicate, ModelTemplates.FLAT_ITEM);
    }

    private JsonObject createPredicatedJson(ResourceLocation id, Map<TextureSlot, ResourceLocation> textures, String predicateName, double threshold) {
        JsonObject model = ModelTemplates.FLAT_ITEM.createBaseTemplate(id, textures);
        JsonObject predicate = new JsonObject();
        predicate.addProperty(predicateName, threshold);
        JsonObject override = new JsonObject();
        override.add("predicate", predicate);
        override.addProperty("model", id.withSuffix("_" + predicateName).toString());
        JsonArray overrides = new JsonArray();
        overrides.add(override);
        model.add("overrides", overrides);
        return model;
    }
}