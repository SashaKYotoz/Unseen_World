package net.sashakyotoz.common.config;

public class ConfigEntries {
    public static void reload() {
        doAdvancedDeathForMobs = new ConfigEntry<>("mobs.animate_death_for_mobs", true).get();
        renderGrippingOnMobs = new ConfigEntry<>("mobs.render_gripping_on_mobs", true).get();
        spawnParticlesOfGripping = new ConfigEntry<>("mobs.spawn_gripping_particles", true).get();
        removeGrippingNaturally = new ConfigEntry<>("mobs.remove_gripping_naturally", true).get();
        violegerPatrolSpawnsIn = new ConfigEntry<>("mobs.violeger_patrol_spawns_in", 9000).get();
        doAbyssalArmorSaveFromVoid = new ConfigEntry<>("gameplay.do_abyssal_armor_save_from_void", true).get();

        keyHandlerBlockCooldown = new ConfigEntry<>("gameplay.key_handler_block_cooldown", 24000).get();

        doLoggingOnlyInDev = new ConfigEntry<>("technic.do_logging_only_in_dev", true).get();
    }

    public static boolean doAdvancedDeathForMobs;
    public static boolean renderGrippingOnMobs;
    public static boolean spawnParticlesOfGripping;
    public static boolean removeGrippingNaturally;
    public static boolean doAbyssalArmorSaveFromVoid;
    public static boolean doLoggingOnlyInDev;

    public static int violegerPatrolSpawnsIn;
    public static int keyHandlerBlockCooldown;
}