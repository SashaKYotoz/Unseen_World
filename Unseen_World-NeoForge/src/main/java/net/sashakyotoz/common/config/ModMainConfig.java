package net.sashakyotoz.common.config;

import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.builder.IJsonBuilder;
import net.sashakyotoz.compat.iris.ShaderCompatMode;

public class ModMainConfig {
    public static void build(IJsonBuilder builder) {
        builder.addObject("mobs", mobs -> mobs
                .comment("Should parasite crystal effects be visible on entities")
                .add("render_crystals_on_mobs", true)
                .bind(renderCrystalsOnMobs)
                .comment("Should darkening effect be visible on entities")
                .add("render_darkening_on_mobs", true)
                .bind(renderDarkeningOnMobs)
                .comment("Should parasite crystal effects be cured naturally over time")
                .add("remove_parasites_naturally", true)
                .bind(removeParasitesNaturally)
                .comment("Should abyssal armor set save player from falling into void")
                .add("abyssal_armor_save_from_void", true)
                .bind(doAbyssalArmorSaveFromVoid)
        );
        builder.addObject("rendering", rendering -> rendering
                .comment("How Iris shaderpacks render the Chimeric Darkness dimension (client only)")
                .comment("vanilla - shaderpack is turned off inside the dimension, mod sky and fog render as designed")
                .comment("nether_profile - shaderpack renders the dimension with its Nether settings")
                .comment("pack_default - shaderpack decides on its own, usually with its Overworld settings")
                .comment("Applied on the next dimension change or shader reload")
                .add("shader_compat_mode", ShaderCompatMode.VANILLA.configName())
                .bind(shaderCompatMode)
        );
    }

    public static ConfigEntry<Boolean> renderCrystalsOnMobs = new ConfigEntry<>(true);
    public static ConfigEntry<Boolean> renderDarkeningOnMobs = new ConfigEntry<>(true);
    public static ConfigEntry<Boolean> removeParasitesNaturally = new ConfigEntry<>(true);
    public static ConfigEntry<Boolean> doAbyssalArmorSaveFromVoid = new ConfigEntry<>(true);
    public static ConfigEntry<ShaderCompatMode> shaderCompatMode = new ConfigEntry<ShaderCompatMode>(ShaderCompatMode.VANILLA)
            .withProcessor(ShaderCompatMode::fromJson);
}