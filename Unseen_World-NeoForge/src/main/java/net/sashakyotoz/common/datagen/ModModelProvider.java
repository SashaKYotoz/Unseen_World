package net.sashakyotoz.common.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.data.block.BlockModel;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
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
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModWoodFamily;
import net.sashakyotoz.common.blocks.custom.EngravedBricksBlock;
import net.sashakyotoz.common.blocks.custom.OrdealSpawnerBlock;
import net.sashakyotoz.common.blocks.custom.plants.DarknessFernBlock;
import net.sashakyotoz.common.blocks.custom.plants.HangingFruitBlock;
import net.sashakyotoz.common.blocks.custom.plants.LeafDroppingLeaveBlock;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ModModelProvider extends VanillaModelProvider {
    private static final String SNOWY_SUFFIX = "_snow";
    private static final List<Block> GRASS_BLOCKS = List.of(
            ModBlocks.NIGHTDARK_GRASS_BLOCK,
            ModBlocks.PENUMBRAL_GRASS_BLOCK,
            ModBlocks.TEALIVY_GRASS_BLOCK,
            ModBlocks.AMETHYST_GRASS_BLOCK,
            ModBlocks.GRIMWOOD_GRASS_BLOCK
    );

    private static final Map<Block, Block> CORAL_FANS = Map.of(
            ModBlocks.UMBRAL_CORAL_FAN, ModBlocks.UMBRAL_CORAL_WALL_FAN,
            ModBlocks.DEAD_UMBRAL_CORAL_FAN, ModBlocks.DEAD_UMBRAL_CORAL_WALL_FAN
    );

    public ModModelProvider(PackOutput output) {
        super(output, UnseenWorld.MOD_ID);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        this.generateHolderBlockModels(generator);

        ModWoodFamily.ALL.forEach(family -> registerWoodFamily(generator, family));
        GRASS_BLOCKS.forEach(block -> registerTopSoil(generator, block));
        CORAL_FANS.forEach((fan, wallFan) -> registerCoralFan(generator, fan, wallFan));

        registerCubeToggle(generator, ModBlocks.GLACIEMITE_BULB, BlockStateProperties.LIT, "_litted");
        registerCubeToggle(generator, ModBlocks.BURLYWOOD_LEAVES, LeafDroppingLeaveBlock.OVERGROWN, "_overgrown");
        registerCubeToggle(generator, ModBlocks.AMETHYST_LEAVES, LeafDroppingLeaveBlock.OVERGROWN, "_overgrown");
        registerCrossWithItemTexture(generator, ModBlocks.ASHREED);

        generator.delegateItemModel(ModBlocks.GLOW_APPLE_BUSH.asItem(), UnseenWorld.MOD.makeID("block/glow_apple_bush/glow_apple_bush_without_fruit"));
        generator.createSimpleFlatItemModel(ModBlocks.HANGING_BURLYWOOD_LEAVES);

        ResourceLocation hoarfrost = TexturedModel.CARPET.create(ModBlocks.HOARFROST, generator.modelOutput);
        generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(ModBlocks.HOARFROST, hoarfrost));

        registerOrientableLump(generator, ModBlocks.GRIPPING_DARK_CURRANTSLATE, "gripping_lump", "dark_currantslate");
        registerOrientableLump(generator, ModBlocks.GRIPPING_GLACIEMITE, "gripping_lump", "glaciemite");
        registerOrientableLump(generator, ModBlocks.GRIPPING_CORONA_STONE, "gripping_lump", "corona_stone");
        registerOrientableLump(generator, ModBlocks.GRIPTONITED_DARK_CURRANTSLATE, "griptonited_lump", "dark_currantslate");
        registerOrientableLump(generator, ModBlocks.GRIPTONITED_GLACIEMITE, "griptonited_lump", "glaciemite");
        registerOrientableLump(generator, ModBlocks.GRIPTONITED_CORONA_STONE, "griptonited_lump", "corona_stone");
        generator.createBrushableBlock(ModBlocks.SUSPICIOUS_ASHEN_OOZE);
        generator.createBrushableBlock(ModBlocks.SUSPICIOUS_GLIMMERGRAIN_SAND);

        List.of(ModBlocks.CRIMSONVEIL_VINE, ModBlocks.CRIMSONVEIL_VINE_PLANT, ModBlocks.GLOWROOT_VINE, ModBlocks.GLOWROOT_VINE_PLANT)
                .forEach(vine -> registerBerryCross(generator, vine));
        registerBurlywoodFruit(generator, ModBlocks.HANGING_BURLYWOOD_LEAVES);

        generator.createFlowerBed(ModBlocks.AMETHYST_PETALS);
        generator.createGrowingPlant(ModBlocks.UMBRAL_KELP, ModBlocks.UMBRAL_KELP_PLANT, BlockModelGenerators.TintState.NOT_TINTED);
        registerTintableLitCross(generator, ModBlocks.GLOOMWEED, BlockModelGenerators.TintState.TINTED);
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
        this.generateHolderItemModels(generator);

        registerPredicatedItem(generator, ModItems.GRIPPING_BUNDLE, "filled", 0.000001);
        registerPredicatedItem(generator, ModItems.GRIPPING_GAUNTLET, "grip", 1.0);

        SolRegistries.MOD.iterate(ItemHolder.class, holder -> {
            if (holder.getID().getNamespace().equals(UnseenWorld.MOD_ID) && holder.get() instanceof ArmorItem armor) {
                generator.generateArmorTrims(armor);
            }
        });
    }

    private void registerWoodFamily(BlockModelGenerators generator, ModWoodFamily family) {
        generator.woodProvider(family.log()).logWithHorizontal(family.log());
        generator.woodProvider(family.strippedLog()).logWithHorizontal(family.strippedLog());
        generator.createOrientableTrapdoor(family.trapdoor());

        ResourceLocation sign = ModelTemplates.PARTICLE_ONLY.create(family.sign(), TextureMapping.particle(family.planks()), generator.modelOutput);
        generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(family.sign(), sign));
        generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(family.wallSign(), sign));
        generator.createSimpleFlatItemModel(family.signItem());

        generator.createHangingSign(family.strippedLog(), family.hangingSign(), family.wallHangingSign());
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

    private void registerBurlywoodFruit(BlockModelGenerators generator, Block block) {
        ResourceLocation plain = ModelTemplates.CROSS.create(block, TextureMapping.cross(block), generator.modelOutput);
        ResourceLocation fruit = UnseenWorld.MOD.makeID("block/glow_apple_fruit");
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createBooleanModelDispatch(HangingFruitBlock.HAS_FRUIT, fruit, plain)));
    }

    private void registerTintableLitCross(BlockModelGenerators generator, Block block, BlockModelGenerators.TintState tintType) {
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
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, SNOWY_SUFFIX + "_top"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, SNOWY_SUFFIX + "_side"));
        Variant snowy = variant(ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, SNOWY_SUFFIX, snowyTextures, generator.modelOutput));
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

    private static boolean isOwn(net.lcc.sollib.api.common.registry.SHolder<?> holder) {
        return holder.getID().getNamespace().equals(UnseenWorld.MOD_ID);
    }

    private void generateHolderBlockModels(BlockModelGenerators generator) {
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            if (!isOwn(holder)) {
                return;
            }
            if (holder.getBlockSet().isEmpty() && holder.hasModel()) {
                switch (holder.getModel()) {
                    case CUBE -> generator.createTrivialCube(holder.get());
                    case PILLAR -> generator.woodProvider(holder.get()).log(holder.get());
                    case ROTATABLE -> generator.createRotatedVariantBlock(holder.get());
                    case CROSS -> generator.createCrossBlockWithDefaultItem(holder.get(), BlockModelGenerators.TintState.NOT_TINTED);
                    case CROSS_POTTED -> pottedCrossBlock(generator, holder.get());
                    case CROSS_DIRECTIONAL -> directionalCrossBlock(generator, holder.get());
                    case DOOR -> generator.createDoor(holder.get());
                    case TRAPDOOR -> generator.createTrapdoor(holder.get());
                    case FLUID -> generator.createNonTemplateModelBlock(holder.get());
                    default -> {
                    }
                }
            } else if (!holder.getBlockSet().isEmpty()) {
                BlockModelGenerators.BlockFamilyProvider familyProvider = generator.family(holder.get());
                for (Map.Entry<BlockModel, BlockHolder> entry : holder.getBlockSet().entrySet()) {
                    switch (entry.getKey()) {
                        case STAIRS -> familyProvider.stairs(entry.getValue().get());
                        case SLAB -> familyProvider.slab(entry.getValue().get());
                        case WALL -> familyProvider.wall(entry.getValue().get());
                        case PRESSURE_PLATE -> familyProvider.pressurePlate(entry.getValue().get());
                        case BUTTON -> familyProvider.button(entry.getValue().get());
                        case FENCE -> familyProvider.fence(entry.getValue().get());
                        case FENCE_GATE -> familyProvider.fenceGate(entry.getValue().get());
                        default -> {
                        }
                    }
                }
            }
        });
    }

    private void generateHolderItemModels(ItemModelGenerators generator) {
        SolRegistries.MOD.iterate(ItemHolder.class, holder -> {
            if (isOwn(holder) && holder.hasModel()) {
                generator.generateFlatItem(holder.get(), holder.getModel());
            }
        });
    }

    private static void pottedCrossBlock(BlockModelGenerators generator, Block block) {
        Block plant = ((FlowerPotBlock) block).getPotted();
        ResourceLocation model = ModelTemplates.FLOWER_POT_CROSS.create(block, TextureMapping.plant(plant), generator.modelOutput);
        generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    }

    private static void directionalCrossBlock(BlockModelGenerators generator, Block block) {
        ResourceLocation model = ModelTemplates.CROSS.create(block, TextureMapping.cross(block), generator.modelOutput);
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.FACING)
                .select(Direction.UP, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R0))
                .select(Direction.DOWN, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .select(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
                .select(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))));
    }
}
