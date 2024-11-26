package net.sashakyotoz.common.tags;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import net.sashakyotoz.UnseenWorld;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> AMETHYST_LOGS = create("amethyst_logs");
        public static final TagKey<Item> BURLYWOOD_LOGS = create("burlywood_logs");
        public static final TagKey<Item> TEALIVE_LOGS = create("tealive_logs");
        public static final TagKey<Item> GRIZZLY_LOGS = create("grizzly_logs");
        public static final TagKey<Item> CRIMSONVEIL_LOGS = create("crimsonveil_logs");

        public static final TagKey<Item> CAN_BE_CHARGED_BY_GRIPCRYSTALS = create("can_be_charged_by_gripcrystals");

        private static TagKey<Item> create(String name) {
            return TagKey.of(RegistryKeys.ITEM, UnseenWorld.makeID(name));
        }
    }
    public static class Blocks {
        public static final TagKey<Block> AMETHYST_BLOCKS = create("amethyst_blocks");
        public static final TagKey<Block> BURLYWOOD_BLOCKS = create("burlywood_blocks");
        public static final TagKey<Block> GRIZZLY_BLOCKS = create("grizzly_blocks");
        public static final TagKey<Block> TEALIVE_BLOCKS = create("tealive_blocks");
        public static final TagKey<Block> CRIMSONVEIL_BLOCKS = create("crimsonveil_blocks");
        public static final TagKey<Block> HANGING_AMETHYST_LEAVES_GROWABLE_ON = create("hanging_amethyst_leaves_growable_on");
        public static final TagKey<Block> CRIMSONVEIL_VINES_GROWABLE_ON = create("hanging_crimsonveil_leaves_growable_on");
        public static final TagKey<Block> HANGING_BURLYWOOD_LEAVES_GROWABLE_ON = create("hanging_burlywood_leaves_growable_on");
        public static final TagKey<Block> BEARFRUIT_BRAMBLE_GROWABLE_ON = create("bearfruit_bramble_growable_on");
        private static TagKey<Block> create(String name) {
            return TagKey.of(RegistryKeys.BLOCK, UnseenWorld.makeID(name));
        }
    }
    public static class Biomes {
        public static final TagKey<Biome> ABYSSAL_SPAWNS_IN = create("abyssal_spawns_in");
        public static final TagKey<Biome> UNSEENIUM_SPAWNS_IN = create("unseenium_spawns_in");
        public static final TagKey<Biome> TITANIUM_SPAWNS_IN = create("titanium_spawns_in");

        public static final TagKey<Biome> GRASS_COVERED = create("grass_covered");
        public static final TagKey<Biome> FLOWERS_COVERED = create("flowers_covered");

        public static final TagKey<Biome> GLACIEMITE_BOULDER_SPAWNS_ON = create("glaciemite_boulder_spawns_on");
        public static final TagKey<Biome> CURRANTSLATE_BOULDER_SPAWNS_ON = create("currantslate_boulder_spawns_on");

        public static final TagKey<Biome> WATER_LAKE_SPAWNS_ON = create("water_lake_spawns_on");
        public static final TagKey<Biome> DARK_WATER_LAKE_SPAWNS_ON = create("dark_water_lake_spawns_on");

        public static final TagKey<Biome> HAS_BURLYWOOD_TREE = create("has_burlywood_tree");
        public static final TagKey<Biome> HAS_BURLYWOOD_BUSH_TREE = create("has_burlywood_bush_tree");
        public static final TagKey<Biome> HAS_AMETHYST_TREE = create("has_amethyst_tree");
        public static final TagKey<Biome> HAS_CRIMSONVEIL_TREE = create("has_crimsonveil_tree");

        public static final TagKey<Biome> HAS_FROST_PORTAL = create("has_frost_portal");

        public static final TagKey<Biome> HAS_GLEAMCARVER = create("has_gleamcarver");
        public static final TagKey<Biome> HAS_HARMONY_WATCHER = create("has_harmony_watcher");
        public static final TagKey<Biome> HAS_SABERPARD = create("has_saberpard");

        public static final TagKey<Biome> SPAWNS_STEPPE_SABERPARD = create("spawns_steppe_saberpard");

        private static TagKey<Biome> create(String name) {
            return TagKey.of(RegistryKeys.BIOME, UnseenWorld.makeID(name));
        }
    }
}