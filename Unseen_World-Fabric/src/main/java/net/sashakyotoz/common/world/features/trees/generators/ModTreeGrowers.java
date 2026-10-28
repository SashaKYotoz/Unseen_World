package net.sashakyotoz.common.world.features.trees.generators;

import net.minecraft.world.level.block.grower.TreeGrower;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;

import java.util.Optional;

public final class ModTreeGrowers {
    public static final TreeGrower AMETHYST = new TreeGrower("unseen_world:amethyst",
            Optional.empty(), Optional.of(ModConfiguredFeatures.AMETHYST_TREE), Optional.empty());
    public static final TreeGrower GRIZZLY = new TreeGrower("unseen_world:grizzly",
            Optional.empty(), Optional.of(ModConfiguredFeatures.GRIZZLY_TREE), Optional.empty());
    public static final TreeGrower TEALIVE = new TreeGrower("unseen_world:tealive",
            Optional.empty(), Optional.of(ModConfiguredFeatures.TEALIVE_TREE), Optional.empty());
    public static final TreeGrower BURLYWOOD = new TreeGrower("unseen_world:burlywood",
            Optional.of(ModConfiguredFeatures.BURLYWOOD_TREE), Optional.empty(), Optional.empty());
    public static final TreeGrower CRIMSONVEIL = new TreeGrower("unseen_world:crimsonveil",
            Optional.empty(), Optional.of(ModConfiguredFeatures.CRIMSONVEIL_TREE), Optional.empty());

    private ModTreeGrowers() {
    }
}