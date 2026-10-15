package net.sashakyotoz.common.world.noise;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;

public final class ModNoiseSettings {
    public static final ResourceKey<NoiseGeneratorSettings> CHIMERIC_DARKNESS = ResourceKey.create(Registries.NOISE_SETTINGS,
            UnseenWorld.MOD.makeID("chimeric_darkness_settings"));

    private static final double AQUIFER_BARRIER_Y_SCALE = 0.5D;
    private static final double AQUIFER_FLOODEDNESS_Y_SCALE = 0.67D;
    private static final double AQUIFER_SPREAD_Y_SCALE = 0.7142857142857143D;

    private ModNoiseSettings() {
    }

    public static void bootstrap(BootstapContext<NoiseGeneratorSettings> context) {
        HolderGetter<DensityFunction> functions = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);
        DensityFunction zero = DensityFunctions.zero();
        NoiseRouter router = new NoiseRouter(
                DensityFunctions.noise(noises.getOrThrow(Noises.AQUIFER_BARRIER), AQUIFER_BARRIER_Y_SCALE),
                DensityFunctions.noise(noises.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), AQUIFER_FLOODEDNESS_Y_SCALE),
                DensityFunctions.noise(noises.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), AQUIFER_SPREAD_Y_SCALE),
                zero,
                mod(functions, ModDensityFunctions.TEMPERATURE),
                mod(functions, ModDensityFunctions.VEGETATION),
                vanilla(functions, "overworld/continents"),
                vanilla(functions, "overworld/erosion"),
                vanilla(functions, "overworld/depth"),
                vanilla(functions, "overworld/ridges"),
                mod(functions, ModDensityFunctions.INITIAL_DENSITY),
                mod(functions, ModDensityFunctions.FINAL_DENSITY),
                zero,
                zero,
                zero);
        context.register(CHIMERIC_DARKNESS, new NoiseGeneratorSettings(
                NoiseSettings.create(ModDensityFunctions.MIN_Y, ModDensityFunctions.HEIGHT, 1, 2),
                ChimericDarknessSurfaceRules.modBlock("dark_currantslate"),
                ModBlocks.DARK_WATER.defaultBlockState(),
                router,
                ChimericDarknessSurfaceRules.build(),
                new OverworldBiomeBuilder().spawnTarget(),
                ChimericDarknessSurfaceRules.SEA_LEVEL,
                true,
                true,
                false,
                false));
    }

    private static DensityFunction mod(HolderGetter<DensityFunction> functions, ResourceKey<DensityFunction> key) {
        return new DensityFunctions.HolderHolder(functions.getOrThrow(key));
    }

    private static DensityFunction vanilla(HolderGetter<DensityFunction> functions, String path) {
        return new DensityFunctions.HolderHolder(functions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation("minecraft", path))));
    }
}