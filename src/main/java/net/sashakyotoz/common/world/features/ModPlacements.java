package net.sashakyotoz.common.world.features;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;

import java.util.List;

public class ModPlacements {
    public static final RegistryKey<PlacedFeature> AMETHYST_TREE = create("amethyst_tree");
    public static void boostrap(Registerable<PlacedFeature> context) {
        var configLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        register(context, AMETHYST_TREE, configLookup.getOrThrow(ModConfiguredFeatures.AMETHYST_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 2),
                        ModBlocks.AMETHYST_SAPLING));
    }
    public static RegistryKey<PlacedFeature> create(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, UnseenWorld.makeID(name));
    }

    private static void register(Registerable<PlacedFeature> context,
                                 RegistryKey<PlacedFeature> key,
                                 RegistryEntry<ConfiguredFeature<?, ?>> config,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }

    private static void register(Registerable<PlacedFeature> context,
                                 RegistryKey<PlacedFeature> key,
                                 RegistryEntry<ConfiguredFeature<?, ?>> config,
                                 PlacementModifier... modifiers) {
        register(context, key, config, List.of(modifiers));
    }
}