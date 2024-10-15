package net.sashakyotoz.common.world.features;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.world.features.custom.BoulderFeature;
import net.sashakyotoz.common.world.features.custom.MidnightLilyPadFeature;
import net.sashakyotoz.common.world.features.custom.StandingFlowerBunchFeature;

public class ModFeatures {
    public static void register() {
        UnseenWorld.log("Registering Features for modid : " + UnseenWorld.MOD_ID);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("boulder"), BoulderFeature.INSTANCE);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("standing_flower_bunch"), StandingFlowerBunchFeature.INSTANCE);
        Registry.register(Registries.FEATURE, UnseenWorld.makeID("midnight_lily_pad_patch"), MidnightLilyPadFeature.INSTANCE);
    }
}