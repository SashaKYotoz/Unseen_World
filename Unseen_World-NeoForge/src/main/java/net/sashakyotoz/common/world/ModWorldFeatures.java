package net.sashakyotoz.common.world;

import net.minecraft.core.registries.Registries;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.registry.ModRegistrar;
import net.sashakyotoz.common.world.carvers.custom.SpiralCaveCarver;
import net.sashakyotoz.common.world.features.custom.*;

public class ModWorldFeatures {
    public static void register() {
        UnseenWorld.log().debug("Registering Features for modid : " + UnseenWorld.MOD_ID);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("boulder"), BoulderFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("ceiling_boulder"), CeilingBoulderFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("standing_flower_bunch"), StandingFlowerBunchFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("midnight_lily_pad_patch"), MidnightLilyPadFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("bush_like_tree_patch"), TreeBushLikeFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("umbral_kelp"), UmbralKelpFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("spiral_spike_feature"), SpiralSpikeFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("cluster_spike_feature"), ClusterSpikeFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("gripping_vegetation"), GrippingVegetationFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("disk_ore"), DiskOreFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("chimeric_freeze"), ChimericFreezeFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("coral_reef"), CoralReefFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("stone_ring"), StoneRingFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("taproot_tree"), TaprootTreeFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("surface_coating"), SurfaceCoatingFeature.INSTANCE);
        ModRegistrar.register(Registries.FEATURE, UnseenWorld.MOD.makeID("nest"), NestFeature.INSTANCE);

        ModRegistrar.register(Registries.CARVER, UnseenWorld.MOD.makeID("spiral_cave"), SpiralCaveCarver.INSTANCE);
    }
}