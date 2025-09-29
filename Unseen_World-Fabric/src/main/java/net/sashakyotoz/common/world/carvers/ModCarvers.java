package net.sashakyotoz.common.world.carvers;

import net.minecraft.block.Block;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.carvers.custom.SpiralCaveCarver;
import net.sashakyotoz.common.world.carvers.custom.configs.SpiralCaveCarverConfig;

public class ModCarvers {
    public static final RegistryKey<ConfiguredCarver<?>> SPIRAL_CAVE = create("spiral_cave");

    public static void boostrap(Registerable<ConfiguredCarver<?>> context) {
        RegistryEntryLookup<Block> registryEntryLookup = context.getRegistryLookup(RegistryKeys.BLOCK);
        register(context, SPIRAL_CAVE, SpiralCaveCarver.INSTANCE, new SpiralCaveCarverConfig(
                0.25F,
                UniformHeightProvider.create(YOffset.fixed(-48),YOffset.fixed(32)),
                ConstantFloatProvider.create(1.0F),
                YOffset.fixed(10),
                CarverDebugConfig.DEFAULT,
                registryEntryLookup.getOrThrow(ModTags.Blocks.CHIMERIC_DARKNESS_STONES),
                6.5f,
                384,
                (float) (Math.PI * 0.04),
                1.5f,
                2
        ));
    }

    private static RegistryKey<ConfiguredCarver<?>> create(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_CARVER, UnseenWorld.makeID(name));
    }

    private static <FC extends CarverConfig, F extends Carver<FC>> void register(Registerable<ConfiguredCarver<?>> context,
                                                                                 RegistryKey<ConfiguredCarver<?>> key, F feature, FC config) {
        context.register(key, new ConfiguredCarver<>(feature, config));
    }
}