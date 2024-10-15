package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.BulbLikeBlock;
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
            generator.registerTrapdoor(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.FLUID))
            generator.registerSimpleState(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.BULB))
            registerBulbBlock(generator,(BulbLikeBlock) block);
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
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        for (Map.Entry<Item, Model> entry : ModRegistry.ITEM_MODELS.entrySet())
            generator.register(entry.getKey(), entry.getValue());
        generator.register(ModBlocks.HANGING_AMETHYST_LEAVES.asItem(), Models.GENERATED);
        generator.register(ModBlocks.BEARFRUIT_BRAMBLE.asItem(), Models.GENERATED);
        generator.register(ModBlocks.MIDNIGHT_LILY_PAD.asItem(), Models.GENERATED);
        for (Item item : ModRegistry.ITEMS) {
            if (item instanceof ArmorItem armorItem)
                generator.registerArmor(armorItem);
            if (item.getTranslationKey().contains("spawn_egg"))
                generator.register(item, new Model(Optional.of(Identifier.of("minecraft", "item/template_spawn_egg")),
                        Optional.empty()));
        }
    }
    private void registerBulbBlock(BlockStateModelGenerator generator, BulbLikeBlock block) {
        Identifier identifier = TexturedModel.CUBE_ALL.upload(block, generator.modelCollector);
        Identifier identifier2 = generator.createSubModel(block, "_litted", Models.CUBE_ALL, TextureMap::all);
        generator.blockStateCollector
                .accept(VariantsBlockStateSupplier.create(block).coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.LIT, identifier2, identifier)));
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
}