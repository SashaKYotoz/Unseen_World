package net.sashakyotoz.common.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.lcc.sollib.datagen.SBlockTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.tags.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends SBlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        super.addTags(lookup);
        this.getOrCreateTagBuilder(ModTags.Blocks.BEARFRUIT_BRAMBLE_GROWABLE_ON).add(
                Blocks.DIRT,
                Blocks.GRASS_BLOCK,
                Blocks.MYCELIUM,
                ModBlocks.AMETHYST_GRASS_BLOCK,
                ModBlocks.GRIMWOOD_GRASS_BLOCK,
                ModBlocks.NIGHTDARK_GRASS_BLOCK,
                ModBlocks.PENUMBRAL_GRASS_BLOCK,
                ModBlocks.NIGHTDARK_DIRT,
                ModBlocks.TEALIVY_GRASS_BLOCK,
                ModBlocks.TEALIVY_DIRT
        );
        this.getOrCreateTagBuilder(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH).add(
                ModBlocks.DARK_WATER
        );
        this.getOrCreateTagBuilder(ModTags.Blocks.GRIPPING_STONE_CAN_REPLACE).add(
                ModBlocks.DARK_CURRANTSLATE,
                ModBlocks.GLACIEMITE
        );
        this.getOrCreateTagBuilder(ModTags.Blocks.CHIMERIC_DARKNESS_STONES).add(
                ModBlocks.DARK_CURRANTSLATE,
                ModBlocks.TANZANITE_BLOCK,
                ModBlocks.CORONA_STONE,
                ModBlocks.GLACIEMITE,
                Blocks.CALCITE
        );
    }
}