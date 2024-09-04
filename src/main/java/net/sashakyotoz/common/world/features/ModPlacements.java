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
<<<<<<< Updated upstream
=======
    public static final RegistryKey<PlacedFeature> SMALL_AMETHYST_TREE = create("small_amethyst_tree");
    public static final RegistryKey<PlacedFeature> GRIZZLY_TREE = create("grizzly_tree");
    public static final RegistryKey<PlacedFeature> TEALIVE_TREE = create("tealive_tree");
    public static final RegistryKey<PlacedFeature> CRIMSONVEIL_TREE = create("crimsonveil_tree");
    public static final RegistryKey<PlacedFeature> TALL_CRIMSONVEIL_TREE = create("tall_crimsonveil_tree");
    public static final RegistryKey<PlacedFeature> BURLYWOOD_TREE = create("burlywood_tree");
>>>>>>> Stashed changes
    public static void boostrap(Registerable<PlacedFeature> context) {
        var configLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        register(context, AMETHYST_TREE, configLookup.getOrThrow(ModConfiguredFeatures.AMETHYST_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 2),
                        ModBlocks.AMETHYST_SAPLING));
<<<<<<< Updated upstream
=======
        register(context, SMALL_AMETHYST_TREE, configLookup.getOrThrow(ModConfiguredFeatures.AMETHYST_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 1),
                        ModBlocks.AMETHYST_SAPLING));
        register(context, GRIZZLY_TREE, configLookup.getOrThrow(ModConfiguredFeatures.GRIZZLY_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 2),
                        ModBlocks.GRIZZLY_SAPLING));
        register(context, TEALIVE_TREE, configLookup.getOrThrow(ModConfiguredFeatures.TEALIVE_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 2),
                        ModBlocks.TEALIVE_SAPLING));
        register(context, CRIMSONVEIL_TREE, configLookup.getOrThrow(ModConfiguredFeatures.CRIMSONVEIL_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 2),
                        ModBlocks.CRIMSONVEIL_SAPLING));
        register(context, TALL_CRIMSONVEIL_TREE, configLookup.getOrThrow(ModConfiguredFeatures.TALL_CRIMSONVEIL_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 3),
                        ModBlocks.CRIMSONVEIL_SAPLING));
        register(context, BURLYWOOD_TREE, configLookup.getOrThrow(ModConfiguredFeatures.BURLYWOOD_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.25f, 2),
                        ModBlocks.BURLYWOOD_SAPLING));
>>>>>>> Stashed changes
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