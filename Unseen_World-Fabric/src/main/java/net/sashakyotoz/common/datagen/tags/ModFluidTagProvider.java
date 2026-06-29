package net.sashakyotoz.common.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.sashakyotoz.common.blocks.ModFluids;

import java.util.concurrent.CompletableFuture;

public class ModFluidTagProvider extends FluidTagsProvider {
    public ModFluidTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void addTags(HolderLookup.Provider arg) {
        this.tag(FluidTags.WATER).add(
                ModFluids.DARK_WATER,
                ModFluids.DARK_FLOWING_WATER
        );
    }
}