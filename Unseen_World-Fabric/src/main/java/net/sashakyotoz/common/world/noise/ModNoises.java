package net.sashakyotoz.common.world.noise;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.sashakyotoz.UnseenWorld;

public final class ModNoises {
    public static final ResourceKey<NormalNoise.NoiseParameters> CAVE_CHEESE = key("darkness_cave_cheese");
    public static final ResourceKey<NormalNoise.NoiseParameters> VALLEY_PILLAR = key("chimeric_darkness/valley_pillar");
    public static final ResourceKey<NormalNoise.NoiseParameters> VALLEY_PILLAR_HEIGHT = key("chimeric_darkness/valley_pillar_height");
    public static final ResourceKey<NormalNoise.NoiseParameters> SEA_SPIRE = key("chimeric_darkness/sea_spire");
    public static final ResourceKey<NormalNoise.NoiseParameters> SEA_SPIRE_HEIGHT = key("chimeric_darkness/sea_spire_height");
    public static final ResourceKey<NormalNoise.NoiseParameters> PATCHES = key("chimeric_darkness/patches");

    private ModNoises() {
    }

    public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
        context.register(CAVE_CHEESE, new NormalNoise.NoiseParameters(-8, 0.5D, 1.0D, 2.0D, 1.0D, 2.0D, 1.0D, 0.0D, 2.0D, 0.0D));
        context.register(VALLEY_PILLAR, new NormalNoise.NoiseParameters(-4, 1.0D, 1.0D));
        context.register(VALLEY_PILLAR_HEIGHT, new NormalNoise.NoiseParameters(-7, 1.0D, 1.0D, 0.5D));
        context.register(SEA_SPIRE, new NormalNoise.NoiseParameters(-4, 1.0D, 0.5D));
        context.register(SEA_SPIRE_HEIGHT, new NormalNoise.NoiseParameters(-6, 1.0D, 1.0D));
        context.register(PATCHES, new NormalNoise.NoiseParameters(-5, 1.0D, 1.0D, 0.5D));
    }

    private static ResourceKey<NormalNoise.NoiseParameters> key(String name) {
        return ResourceKey.create(Registries.NOISE, UnseenWorld.MOD.makeID(name));
    }
}
