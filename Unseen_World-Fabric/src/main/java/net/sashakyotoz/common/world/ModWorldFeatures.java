package net.sashakyotoz.common.world;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.world.carvers.custom.SpiralCaveCarver;
import net.sashakyotoz.common.world.features.custom.*;

public class ModWorldFeatures {
    public static void register() {
        UnseenWorld.log().debug("Registering Features for modid : " + UnseenWorld.MOD_ID);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("boulder"), BoulderFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("ceiling_boulder"), CeilingBoulderFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("standing_flower_bunch"), StandingFlowerBunchFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("midnight_lily_pad_patch"), MidnightLilyPadFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("bush_like_tree_patch"), TreeBushLikeFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("umbral_kelp"), UmbralKelpFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("spiral_spike_feature"), SpiralSpikeFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("cluster_spike_feature"), ClusterSpikeFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("gripping_vegetation"), GrippingVegetationFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("disk_ore"), DiskOreFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("chimeric_freeze"), ChimericFreezeFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("coral_reef"), CoralReefFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("stone_ring"), StoneRingFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("taproot_tree"), TaprootTreeFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("surface_coating"), SurfaceCoatingFeature.INSTANCE);
        Registry.register(BuiltInRegistries.FEATURE, UnseenWorld.MOD.makeID("nest"), NestFeature.INSTANCE);

        Registry.register(BuiltInRegistries.CARVER, UnseenWorld.MOD.makeID("spiral_cave"), SpiralCaveCarver.INSTANCE);
    }
}