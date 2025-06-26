package net.sashakyotoz.common.world.features.trees;

import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.world.features.trees.placers.*;
import net.sashakyotoz.mixin.access.FoliagePlacerTypeInvoker;
import net.sashakyotoz.mixin.access.TreeDecoratorTypeInvoker;
import net.sashakyotoz.mixin.access.TrunkPlacerTypeInvoker;

public class ModTreePlacerTypes {
    public static void register() {
        UnseenWorld.log("Registering Trunk and Foliage Placers for modid : " + UnseenWorld.MOD_ID);
    }

    public static final TrunkPlacerType<?> GRIZZLY_TRUNK_PLACER = TrunkPlacerTypeInvoker.callRegister("grizzly_trunk_placer",
            GrizzlyTrunkPlacer.CODEC);
    public static final TrunkPlacerType<?> BURLYWOOD_TRUNK_PLACER = TrunkPlacerTypeInvoker.callRegister("burlywood_trunk_placer",
            BurlywoodTrunkPlacer.CODEC);

    public static final FoliagePlacerType<?> GRIZZLY_FOLIAGE_PLACER = FoliagePlacerTypeInvoker.callRegister("grizzly_foliage_placer",
            GrizzlyFoliagePlacer.CODEC);
    public static final FoliagePlacerType<?> BURLYWOOD_FOLIAGE_PLACER = FoliagePlacerTypeInvoker.callRegister("burlywood_foliage_placer",
            BurlywoodFoliagePlacer.CODEC);
    public static final FoliagePlacerType<?> HANGING_BLOB_FOLIAGE_PLACER = FoliagePlacerTypeInvoker.callRegister("hanging_blob_foliage_placer",
            BurlywoodFoliagePlacer.CODEC);

    public static final TreeDecoratorType<?> VINES_TO_LEAVES_DECORATOR = TreeDecoratorTypeInvoker.callRegister("vines_to_leaves_decorator",
            VinesToLeavesTreeDecorator.CODEC);
}
