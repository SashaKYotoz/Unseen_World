package net.sashakyotoz.common.world.carvers;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.carvers.custom.SpiralCaveCarver;
import net.sashakyotoz.common.world.carvers.custom.configs.SpiralCaveCarverConfig;

public class ModCarvers {
    public static final ResourceKey<ConfiguredWorldCarver<?>> SPIRAL_CAVE = create("spiral_cave");

    public static void boostrap(BootstapContext<ConfiguredWorldCarver<?>> context) {
        HolderGetter<Block> registryEntryLookup = context.lookup(Registries.BLOCK);
        register(context, SPIRAL_CAVE, SpiralCaveCarver.INSTANCE, new SpiralCaveCarverConfig(
                0.35F,
                UniformHeight.of(VerticalAnchor.absolute(-48),VerticalAnchor.absolute(32)),
                ConstantFloat.of(1.0F),
                VerticalAnchor.absolute(10),
                CarverDebugSettings.DEFAULT,
                registryEntryLookup.getOrThrow(ModTags.Blocks.CHIMERIC_DARKNESS_STONES),
                6.5f,
                384,
                (float) (Math.PI * 0.04),
                1.5f,
                2
        ));
    }

    private static ResourceKey<ConfiguredWorldCarver<?>> create(String name) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, UnseenWorld.makeID(name));
    }

    private static <FC extends CarverConfiguration, F extends WorldCarver<FC>> void register(BootstapContext<ConfiguredWorldCarver<?>> context,
                                                                                 ResourceKey<ConfiguredWorldCarver<?>> key, F feature, FC config) {
        context.register(key, new ConfiguredWorldCarver<>(feature, config));
    }
}