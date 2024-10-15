package net.sashakyotoz.common.world.features.placements;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;

import java.util.List;

public class ModPlacements {
    public static final RegistryKey<PlacedFeature> AMETHYST_TREE = create("amethyst_tree");
    public static final RegistryKey<PlacedFeature> SMALL_AMETHYST_TREE = create("small_amethyst_tree");
    public static final RegistryKey<PlacedFeature> GRIZZLY_TREE = create("grizzly_tree");
    public static final RegistryKey<PlacedFeature> TALL_GRIZZLY_TREE = create("tall_grizzly_tree");
    public static final RegistryKey<PlacedFeature> TEALIVE_TREE = create("tealive_tree");
    public static final RegistryKey<PlacedFeature> CRIMSONVEIL_TREE = create("crimsonveil_tree");
    public static final RegistryKey<PlacedFeature> TALL_CRIMSONVEIL_TREE = create("tall_crimsonveil_tree");
    public static final RegistryKey<PlacedFeature> BURLYWOOD_TREE = create("burlywood_tree");

    public static final RegistryKey<PlacedFeature> UNSEENIUM_ORE = create("unseenium_ore");
    public static final RegistryKey<PlacedFeature> RED_TITANIUM_ORE = create("red_titanium_ore");
    public static final RegistryKey<PlacedFeature> ABYSSAL_ORE = create("abyssal_ore");

    public static final RegistryKey<PlacedFeature> GLACIEMITE_BOULDER = create("glaciemite_boulder");
    public static final RegistryKey<PlacedFeature> DARK_CURRANTSLATE_BOULDER = create("dark_currantslate_boulder");

    public static final RegistryKey<PlacedFeature> WATER_LAKE = create("water_lake");
    public static final RegistryKey<PlacedFeature> DARK_WATER_LAKE = create("dark_water_lake");

    public static final RegistryKey<PlacedFeature> BEARFRUIT_BRAMBLE_PATCH = create("bearfruit_bramble_patch");
    public static final RegistryKey<PlacedFeature> MIDNIGHT_LILY_PATCH = create("midnight_lily_patch");

    public static void boostrap(Registerable<PlacedFeature> context) {
        var configLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        register(context, AMETHYST_TREE, configLookup.getOrThrow(ModConfiguredFeatures.AMETHYST_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 2),
                        ModBlocks.AMETHYST_SAPLING));
        register(context, SMALL_AMETHYST_TREE, configLookup.getOrThrow(ModConfiguredFeatures.AMETHYST_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 1),
                        ModBlocks.AMETHYST_SAPLING));
        register(context, GRIZZLY_TREE, configLookup.getOrThrow(ModConfiguredFeatures.GRIZZLY_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 2),
                        ModBlocks.GRIZZLY_SAPLING));
        register(context, TALL_GRIZZLY_TREE, configLookup.getOrThrow(ModConfiguredFeatures.TALL_GRIZZLY_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 1),
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

        register(context, UNSEENIUM_ORE, configLookup.getOrThrow(ModConfiguredFeatures.UNSEENIUM_ORE),
                ModOrePlacement.modifiersWithCount(4,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(48))));
        register(context, RED_TITANIUM_ORE, configLookup.getOrThrow(ModConfiguredFeatures.RED_TITANIUM_ORE),
                ModOrePlacement.modifiersWithCount(2,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(32))));
        register(context, ABYSSAL_ORE, configLookup.getOrThrow(ModConfiguredFeatures.ABYSSAL_ORE),
                ModOrePlacement.modifiersWithCount(3,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(32))));

        register(context, GLACIEMITE_BOULDER, configLookup.getOrThrow(ModConfiguredFeatures.GLACIEMITE_BOULDER),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of(),
                RarityFilterPlacementModifier.of(3));
        register(context, DARK_CURRANTSLATE_BOULDER, configLookup.getOrThrow(ModConfiguredFeatures.DARK_CURRANTSLATE_BOULDER),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of(),
                RarityFilterPlacementModifier.of(4));

        register(context, WATER_LAKE, configLookup.getOrThrow(ModConfiguredFeatures.WATER_LAKE),
                RarityFilterPlacementModifier.of(6),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of());
        register(context, DARK_WATER_LAKE, configLookup.getOrThrow(ModConfiguredFeatures.DARK_WATER_LAKE),
                RarityFilterPlacementModifier.of(24),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of());

        register(context, BEARFRUIT_BRAMBLE_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.BEARFRUIT_BRAMBLE_PATCH),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of(),
                RarityFilterPlacementModifier.of(4));
        register(context, MIDNIGHT_LILY_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.MIDNIGHT_LILY_PATCH),
                RarityFilterPlacementModifier.of(4),
                HeightRangePlacementModifier.uniform(YOffset.fixed(56),YOffset.fixed(58)),
                SquarePlacementModifier.of(),
                BiomePlacementModifier.of());
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