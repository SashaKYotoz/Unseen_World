package net.sashakyotoz.common.datagen;

import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class VanillaModelProvider implements DataProvider {
    private final PackOutput.PathProvider blockStatePathProvider;
    private final PackOutput.PathProvider modelPathProvider;
    private final String namespace;

    protected VanillaModelProvider(PackOutput output, String namespace) {
        this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
        this.namespace = namespace;
    }

    public abstract void generateBlockStateModels(BlockModelGenerators generator);

    public abstract void generateItemModels(ItemModelGenerators generator);

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Map<Block, BlockStateGenerator> blockStates = new HashMap<>();
        Consumer<BlockStateGenerator> blockStateConsumer = generator -> {
            BlockStateGenerator previous = blockStates.put(generator.getBlock(), generator);
            if (previous != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + generator.getBlock());
            }
        };
        Map<ResourceLocation, Supplier<JsonElement>> models = new HashMap<>();
        BiConsumer<ResourceLocation, Supplier<JsonElement>> modelConsumer = (id, json) -> {
            Supplier<JsonElement> previous = models.put(id, json);
            if (previous != null) {
                throw new IllegalStateException("Duplicate model definition for " + id);
            }
        };
        Set<Item> skippedAutoModels = new HashSet<>();
        this.generateBlockStateModels(new BlockModelGenerators(blockStateConsumer, modelConsumer, skippedAutoModels::add));
        this.generateItemModels(new ItemModelGenerators(modelConsumer));

        for (Block block : blockStates.keySet()) {
            Item item = Item.BY_BLOCK.get(block);
            if (item == null || skippedAutoModels.contains(item) || !(item instanceof BlockItem)) {
                continue;
            }
            ResourceLocation itemModel = ModelLocationUtils.getModelLocation(item);
            if (!models.containsKey(itemModel)) {
                models.put(itemModel, new DelegatedModel(ModelLocationUtils.getModelLocation(block)));
            }
        }

        List<CompletableFuture<?>> futures = new ArrayList<>();
        blockStates.forEach((block, generator) -> {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            if (this.namespace.equals(id.getNamespace())) {
                futures.add(DataProvider.saveStable(output, generator.get(), this.blockStatePathProvider.json(id)));
            }
        });
        models.forEach((id, json) -> {
            if (this.namespace.equals(id.getNamespace())) {
                futures.add(DataProvider.saveStable(output, json.get(), this.modelPathProvider.json(id)));
            }
        });
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Block State Definitions and Models: " + this.namespace;
    }
}
