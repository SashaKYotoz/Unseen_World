package net.sashakyotoz.common.world.features.trees;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.registry.ModRegistrar;
import net.sashakyotoz.common.world.features.trees.placers.*;

public class ModTreePlacerTypes {
    public static void register() {
        UnseenWorld.log().debug("Registering Trunk and Foliage Placers for modid : " + UnseenWorld.MOD_ID);
    }

    public static final TrunkPlacerType<?> GRIZZLY_TRUNK_PLACER = ModRegistrar.register(Registries.TRUNK_PLACER_TYPE, ResourceLocation.withDefaultNamespace("grizzly_trunk_placer"), new TrunkPlacerType<>(GrizzlyTrunkPlacer.CODEC));
    public static final TrunkPlacerType<?> BURLYWOOD_TRUNK_PLACER = ModRegistrar.register(Registries.TRUNK_PLACER_TYPE, ResourceLocation.withDefaultNamespace("burlywood_trunk_placer"), new TrunkPlacerType<>(BurlywoodTrunkPlacer.CODEC));
    public static final TrunkPlacerType<CrimsonveilTrunkPlacer> CRIMSONVEIL_TRUNK_PLACER = ModRegistrar.register(Registries.TRUNK_PLACER_TYPE,
            UnseenWorld.MOD.makeID("crimsonveil_trunk_placer"), new TrunkPlacerType<>(CrimsonveilTrunkPlacer.CODEC));
    public static final TrunkPlacerType<ThornTrunkPlacer> THORN_TRUNK_PLACER = ModRegistrar.register(Registries.TRUNK_PLACER_TYPE,
            UnseenWorld.MOD.makeID("thorn_trunk_placer"), new TrunkPlacerType<>(ThornTrunkPlacer.CODEC));

    public static final FoliagePlacerType<?> GRIZZLY_FOLIAGE_PLACER = ModRegistrar.register(Registries.FOLIAGE_PLACER_TYPE, ResourceLocation.withDefaultNamespace("grizzly_foliage_placer"), new FoliagePlacerType<>(GrizzlyFoliagePlacer.CODEC));
    public static final FoliagePlacerType<?> BURLYWOOD_FOLIAGE_PLACER = ModRegistrar.register(Registries.FOLIAGE_PLACER_TYPE, ResourceLocation.withDefaultNamespace("burlywood_foliage_placer"), new FoliagePlacerType<>(BurlywoodFoliagePlacer.CODEC));
    public static final FoliagePlacerType<CrimsonveilFoliagePlacer> CRIMSONVEIL_FOLIAGE_PLACER = ModRegistrar.register(Registries.FOLIAGE_PLACER_TYPE,
            UnseenWorld.MOD.makeID("crimsonveil_foliage_placer"), new FoliagePlacerType<>(CrimsonveilFoliagePlacer.CODEC));
    public static final FoliagePlacerType<ThornFoliagePlacer> THORN_FOLIAGE_PLACER = ModRegistrar.register(Registries.FOLIAGE_PLACER_TYPE,
            UnseenWorld.MOD.makeID("thorn_foliage_placer"), new FoliagePlacerType<>(ThornFoliagePlacer.CODEC));
    public static final FoliagePlacerType<?> HANGING_BLOB_FOLIAGE_PLACER = ModRegistrar.register(Registries.FOLIAGE_PLACER_TYPE, ResourceLocation.withDefaultNamespace("hanging_blob_foliage_placer"), new FoliagePlacerType<>(HangingBlobFoliagePlacer.CODEC));

    public static final TreeDecoratorType<?> VINES_TO_LEAVES_DECORATOR = ModRegistrar.register(Registries.TREE_DECORATOR_TYPE, ResourceLocation.withDefaultNamespace("vines_to_leaves_decorator"), new TreeDecoratorType<>(VinesToLeavesTreeDecorator.CODEC));
}