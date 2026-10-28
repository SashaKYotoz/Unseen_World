package net.sashakyotoz.common.world.features.trees;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.world.features.trees.placers.*;
import net.sashakyotoz.mixin.access.FoliagePlacerTypeInvoker;
import net.sashakyotoz.mixin.access.TreeDecoratorTypeInvoker;
import net.sashakyotoz.mixin.access.TrunkPlacerTypeInvoker;

public class ModTreePlacerTypes {
    public static void register() {
        UnseenWorld.log().debug("Registering Trunk and Foliage Placers for modid : " + UnseenWorld.MOD_ID);
    }

    public static final TrunkPlacerType<?> GRIZZLY_TRUNK_PLACER = TrunkPlacerTypeInvoker.callRegister("grizzly_trunk_placer",
            GrizzlyTrunkPlacer.CODEC);
    public static final TrunkPlacerType<?> BURLYWOOD_TRUNK_PLACER = TrunkPlacerTypeInvoker.callRegister("burlywood_trunk_placer",
            BurlywoodTrunkPlacer.CODEC);
    public static final TrunkPlacerType<CrimsonveilTrunkPlacer> CRIMSONVEIL_TRUNK_PLACER = Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE,
            UnseenWorld.MOD.makeID("crimsonveil_trunk_placer"), new TrunkPlacerType<>(CrimsonveilTrunkPlacer.CODEC));
    public static final TrunkPlacerType<ThornTrunkPlacer> THORN_TRUNK_PLACER = Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE,
            UnseenWorld.MOD.makeID("thorn_trunk_placer"), new TrunkPlacerType<>(ThornTrunkPlacer.CODEC));

    public static final FoliagePlacerType<?> GRIZZLY_FOLIAGE_PLACER = FoliagePlacerTypeInvoker.callRegister("grizzly_foliage_placer",
            GrizzlyFoliagePlacer.CODEC);
    public static final FoliagePlacerType<?> BURLYWOOD_FOLIAGE_PLACER = FoliagePlacerTypeInvoker.callRegister("burlywood_foliage_placer",
            BurlywoodFoliagePlacer.CODEC);
    public static final FoliagePlacerType<CrimsonveilFoliagePlacer> CRIMSONVEIL_FOLIAGE_PLACER = Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE,
            UnseenWorld.MOD.makeID("crimsonveil_foliage_placer"), new FoliagePlacerType<>(CrimsonveilFoliagePlacer.CODEC));
    public static final FoliagePlacerType<ThornFoliagePlacer> THORN_FOLIAGE_PLACER = Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE,
            UnseenWorld.MOD.makeID("thorn_foliage_placer"), new FoliagePlacerType<>(ThornFoliagePlacer.CODEC));
    public static final FoliagePlacerType<?> HANGING_BLOB_FOLIAGE_PLACER = FoliagePlacerTypeInvoker.callRegister("hanging_blob_foliage_placer",
            HangingBlobFoliagePlacer.CODEC);

    public static final TreeDecoratorType<?> VINES_TO_LEAVES_DECORATOR = TreeDecoratorTypeInvoker.callRegister("vines_to_leaves_decorator",
            VinesToLeavesTreeDecorator.CODEC);
}