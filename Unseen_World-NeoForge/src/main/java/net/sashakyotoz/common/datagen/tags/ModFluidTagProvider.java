package net.sashakyotoz.common.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModFluids;

import java.util.concurrent.CompletableFuture;

public class ModFluidTagProvider extends FluidTagsProvider {
    public ModFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, ExistingFileHelper existingFileHelper) {
        super(output, registriesFuture, UnseenWorld.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider arg) {
        this.tag(FluidTags.WATER).add(
                ModFluids.DARK_WATER,
                ModFluids.DARK_FLOWING_WATER
        );
    }
}