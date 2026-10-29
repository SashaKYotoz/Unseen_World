package net.sashakyotoz.common.datagen;

import net.lcc.sollib.datagen.SEntityTagProvider;
import net.lcc.sollib.datagen.SItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.datagen.loot.ModArchaeologyLootTableProvider;
import net.sashakyotoz.common.datagen.loot.ModBlockLootTableProvider;
import net.sashakyotoz.common.datagen.loot.ModChestLootTableProvider;
import net.sashakyotoz.common.datagen.loot.OrdealLootTableProvider;
import net.sashakyotoz.common.datagen.recipes.ModRecipeProvider;
import net.sashakyotoz.common.datagen.tags.ModBiomeTagProvider;
import net.sashakyotoz.common.datagen.tags.ModBlockTagProvider;
import net.sashakyotoz.common.datagen.tags.ModFluidTagProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class UnseenWorldDataGenerator {
    private UnseenWorldDataGenerator() {
    }

    public static void gatherData(GatherDataEvent event) {
        if (!event.getModContainer().getModId().equals(UnseenWorld.MOD_ID)) {
            return;
        }
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> vanillaLookup = event.getLookupProvider();

        ModWorldGenerator worldGenerator = generator.addProvider(event.includeServer(), new ModWorldGenerator(output, vanillaLookup));
        CompletableFuture<HolderLookup.Provider> lookup = worldGenerator.getRegistryProvider();

        generator.addProvider(event.includeServer(), new AdvancementProvider(output, lookup, helper, List.of(new ModAdvancementProvider())));

        ModBlockTagProvider blockTags = generator.addProvider(event.includeServer(), new ModBlockTagProvider(output, lookup, helper));
        generator.addProvider(event.includeServer(), new ModFluidTagProvider(output, lookup, helper));
        generator.addProvider(event.includeServer(), new ModBiomeTagProvider(output, lookup, helper));
        generator.addProvider(event.includeServer(), new SItemTagProvider(output, lookup, blockTags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new SEntityTagProvider(output, lookup, helper));
        generator.addProvider(event.includeServer(), new ModGameEventTagProvider(output, lookup, helper));

        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(OrdealLootTableProvider::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(ModChestLootTableProvider::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(ModArchaeologyLootTableProvider::new, LootContextParamSets.ARCHAEOLOGY)
        ), lookup));

        generator.addProvider(event.includeClient(), new ModModelProvider(output));
        generator.addProvider(event.includeServer(), new ModDimensionProvider(output, lookup));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookup));
        generator.addProvider(event.includeServer(), new ModDataMapProvider(output, lookup));
    }
}
