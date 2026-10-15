package net.sashakyotoz.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.sashakyotoz.UnseenWorld;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> AMETHYST_LOGS = create("amethyst_logs");
        public static final TagKey<Item> BURLYWOOD_LOGS = create("burlywood_logs");
        public static final TagKey<Item> TEALIVE_LOGS = create("tealive_logs");
        public static final TagKey<Item> GRIZZLY_LOGS = create("grizzly_logs");
        public static final TagKey<Item> CRIMSONVEIL_LOGS = create("crimsonveil_logs");

        public static final TagKey<Item> VEXAL_BETTLE_TAMING_ITEMS = create("vexal_beetle_taming_items");
        public static final TagKey<Item> RIMEHORN_TAMING_ITEMS = create("rimehorn_taming_items");

        public static final TagKey<Item> GRIPPING_BUNDLE_CAN_HANDLE = create("gripping_bundle_can_handle");

        private static TagKey<Item> create(String name) {
            return TagKey.create(Registries.ITEM, UnseenWorld.MOD.makeID(name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> AMETHYST_BLOCKS = create("amethyst_blocks");
        public static final TagKey<Block> BURLYWOOD_BLOCKS = create("burlywood_blocks");
        public static final TagKey<Block> GRIZZLY_BLOCKS = create("grizzly_blocks");
        public static final TagKey<Block> TEALIVE_BLOCKS = create("tealive_blocks");
        public static final TagKey<Block> CRIMSONVEIL_BLOCKS = create("crimsonveil_blocks");
        public static final TagKey<Block> HANGING_AMETHYST_LEAVES_GROWABLE_ON = create("hanging_amethyst_leaves_growable_on");
        public static final TagKey<Block> CRIMSONVEIL_VINES_GROWABLE_ON = create("crimsonveil_vines_growable_on");
        public static final TagKey<Block> GLOWROOT_VINES_GROWABLE_ON = create("glowroot_vines_growable_on");
        public static final TagKey<Block> HANGING_BURLYWOOD_LEAVES_GROWABLE_ON = create("hanging_burlywood_leaves_growable_on");
        public static final TagKey<Block> BEARFRUIT_BRAMBLE_GROWABLE_ON = create("bearfruit_bramble_growable_on");
        public static final TagKey<Block> GRIPPING_STONE_CAN_REPLACE = create("gripping_stone_can_replace");
        public static final TagKey<Block> CHIMERIC_DARKNESS_STONES = create("chimeric_darkness_stones");

        private static TagKey<Block> create(String name) {
            return TagKey.create(Registries.BLOCK, UnseenWorld.MOD.makeID(name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> IS_CHIMERIC_DARKNESS = create("is_chimeric_darkness");
        public static final TagKey<Biome> IS_UNDERGROUND_BIOME = create("is_underground_biome");
        public static final TagKey<Biome> IS_COLD = create("is_cold");
        public static final TagKey<Biome> IS_BOSS_DOMAIN = create("is_boss_domain");

        public static final TagKey<Biome> HAS_DENSE_FOG = create("has_dense_fog");
        public static final TagKey<Biome> HAS_CLEAR_FOG = create("has_clear_fog");

        public static final TagKey<Biome> HAS_WARRIOR_ARENA = create("has_structure/warrior_arena");
        public static final TagKey<Biome> HAS_WARRIOR_RUINS = create("has_structure/warrior_ruins");
        public static final TagKey<Biome> HAS_SENTINEL_ARENA = create("has_structure/sentinel_arena");
        public static final TagKey<Biome> HAS_SENTINEL_RUINS = create("has_structure/sentinel_ruins");
        public static final TagKey<Biome> HAS_PATRIARCH_ARENA = create("has_structure/patriarch_arena");
        public static final TagKey<Biome> HAS_PATRIARCH_RUINS = create("has_structure/patriarch_ruins");
        public static final TagKey<Biome> HAS_FROST_PORTAL = create("has_frost_portal");

        public static final TagKey<Biome> ABYSSAL_SPAWNS_IN = create("abyssal_spawns_in");
        public static final TagKey<Biome> UNSEENIUM_SPAWNS_IN = create("unseenium_spawns_in");
        public static final TagKey<Biome> TITANIUM_SPAWNS_IN = create("titanium_spawns_in");

        public static final TagKey<Biome> FLOWERS_COVERED = create("flowers_covered");
        public static final TagKey<Biome> GLACIEMITE_BOULDER_SPAWNS_ON = create("glaciemite_boulder_spawns_on");
        public static final TagKey<Biome> CURRANTSLATE_BOULDER_SPAWNS_ON = create("currantslate_boulder_spawns_on");

        public static final TagKey<Biome> HAS_BURLYWOOD_TREE = create("has_burlywood_tree");
        public static final TagKey<Biome> HAS_AMETHYST_TREE = create("has_amethyst_tree");
        public static final TagKey<Biome> HAS_CRIMSONVEIL_TREE = create("has_crimsonveil_tree");

        public static final TagKey<Biome> HAS_GLEAMCARVER = create("has_gleamcarver");
        public static final TagKey<Biome> HAS_HARMONY_WATCHER = create("has_harmony_watcher");
        public static final TagKey<Biome> HAS_SABERPARD = create("has_saberpard");
        public static final TagKey<Biome> HAS_TUSKHOG = create("has_tuskhog");

        public static final TagKey<Biome> SPAWNS_STEPPE_SABERPARD = create("spawns_steppe_saberpard");
        public static final TagKey<Biome> SPAWNS_WARM_TUSKHOG = create("spawns_warm_tuskhog");

        private static TagKey<Biome> create(String name) {
            return TagKey.create(Registries.BIOME, UnseenWorld.MOD.makeID(name));
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> GRIPPING_IMMUNE_ENTITY_TYPES = create("gripping_immune_entity_types");

        private static TagKey<EntityType<?>> create(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, UnseenWorld.MOD.makeID(name));
        }
    }

    public static class GameEvents {
        public static final TagKey<GameEvent> ELDRITCH_CAN_LISTEN = create("eldritch_can_listen");

        public static final TagKey<GameEvent> ENTITY_SHOOT = create("entity_shoot");
        public static final TagKey<GameEvent> ENTITY_MOVE = create("entity_move");

        private static TagKey<GameEvent> create(String name) {
            return TagKey.create(Registries.GAME_EVENT, UnseenWorld.MOD.makeID(name));
        }
    }

    public static class Structures {
        public static final TagKey<Structure> STRUCTURES_MAP_CAN_LOCATE = create("structures_map_can_locate");

        private static TagKey<Structure> create(String name) {
            return TagKey.create(Registries.STRUCTURE, UnseenWorld.MOD.makeID(name));
        }
    }
}