package net.sashakyotoz.common.world;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.world.biomes.DarknessMultiNoiseBiomeSource;
import net.sashakyotoz.common.world.carvers.custom.SpiralCaveCarver;
import net.sashakyotoz.common.world.features.custom.*;

public class ModWorldFeatures {
    public static void register() {
        UnseenWorld.log("Registering Features for modid : " + UnseenWorld.MOD_ID);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.makeID("boulder"), BoulderFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.makeID("ceiling_boulder"), CeilingBoulderFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.makeID("standing_flower_bunch"), StandingFlowerBunchFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.makeID("midnight_lily_pad_patch"), MidnightLilyPadFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.makeID("bush_like_tree_patch"), TreeBushLikeFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.makeID("adaptive_lake"), AdaptiveLakeFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.makeID("umbral_kelp"), UmbralKelpFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.makeID("spiral_spike_feature"), SpiralSpikeFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.makeID("cluster_spike_feature"), ClusterSpikeFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.makeID("gripping_vegetation"), GrippingVegetationFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.makeID("disk_ore"), DiskOreFeature.INSTANCE);

        Registry.register(BuiltInRegistries.CARVER, UnseenWorld.makeID("spiral_cave"), SpiralCaveCarver.INSTANCE);
        Registry.register(BuiltInRegistries.BIOME_SOURCE, UnseenWorld.makeID("darkness_multi_noise"), DarknessMultiNoiseBiomeSource.CODEC);
    }
}