package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.tags.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        for (TagKey<Block> tag : ModRegistry.BLOCK_TAGS.keySet()) {
            FabricTagProvider<Block>.FabricTagBuilder builder = getOrCreateTagBuilder(tag);
            for (Block block : ModRegistry.BLOCK_TAGS.get(tag)) {
                builder.add(block);
            }
        }
        this.getOrCreateTagBuilder(ModTags.Blocks.BEARFRUIT_BRAMBLE_GROWABLE_ON).add(
                Blocks.DIRT,
                Blocks.GRASS_BLOCK,
                Blocks.MYCELIUM,
                ModBlocks.AMETHYST_GRASS_BLOCK,
                ModBlocks.GRIMWOOD_GRASS_BLOCK,
                ModBlocks.NIGHTDARK_GRASS_BLOCK,
                ModBlocks.TEALIVY_GRASS_BLOCK,
                ModBlocks.BEARFRUIT_BRAMBLE
        );
    }
}
