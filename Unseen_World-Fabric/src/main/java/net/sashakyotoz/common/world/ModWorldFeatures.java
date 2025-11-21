package net.sashakyotoz.common.world;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.world.biomes.DarknessMultiNoiseBiomeSource;
import net.sashakyotoz.common.world.carvers.custom.SpiralCaveCarver;
import net.sashakyotoz.common.world.features.custom.*;

public class ModWorldFeatures {
    public static void register() {
        UnseenWorld.log("Registering Features for modid : " + UnseenWorld.MOD_ID);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("boulder"), BoulderFeature.INSTANCE);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("ceiling_boulder"), CeilingBoulderFeature.INSTANCE);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("standing_flower_bunch"), StandingFlowerBunchFeature.INSTANCE);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("midnight_lily_pad_patch"), MidnightLilyPadFeature.INSTANCE);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("bush_like_tree_patch"), TreeBushLikeFeature.INSTANCE);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("adaptive_lake"), AdaptiveLakeFeature.INSTANCE);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("umbral_kelp"), UmbralKelpFeature.INSTANCE);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("spiral_spikes_feature"), SpiralSpikesFeature.INSTANCE);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("gripping_vegetation"), GrippingVegetationFeature.INSTANCE);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("disk_ore"), DiskOreFeature.INSTANCE);

        Registry.register(Registries.CARVER, UnseenWorld.makeID("spiral_cave"), SpiralCaveCarver.INSTANCE);
        Registry.register(Registries.BIOME_SOURCE, UnseenWorld.makeID("darkness_multi_noise"), DarknessMultiNoiseBiomeSource.CODEC);
    }
}