package net.sashakyotoz.common.config;

import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.builder.IJsonBuilder;

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
    }

    public static ConfigEntry<Boolean> renderCrystalsOnMobs = new ConfigEntry<>(true);
    public static ConfigEntry<Boolean> renderDarkeningOnMobs = new ConfigEntry<>(true);
    public static ConfigEntry<Boolean> removeParasitesNaturally = new ConfigEntry<>(true);
    public static ConfigEntry<Boolean> doAbyssalArmorSaveFromVoid = new ConfigEntry<>(true);

    public static int keyHandlerBlockCooldown = 999;
}