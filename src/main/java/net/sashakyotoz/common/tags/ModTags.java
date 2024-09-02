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
        public static final TagKey<Block> HANGING_CRIMSONVEIL_LEAVES_GROWABLE_ON = create("hanging_crimsonveil_leaves_growable_on");
        public static final TagKey<Block> HANGING_BURLYWOOD_LEAVES_GROWABLE_ON = create("hanging_burlywood_leaves_growable_on");
        private static TagKey<Block> create(String name) {
            return TagKey.of(RegistryKeys.BLOCK, UnseenWorld.makeID(name));
        }
    }
    public static class Biomes {
        public static final TagKey<Biome> GREENISH_LIKE_BIOMES = create("greenish_like_biomes");
        public static final TagKey<Biome> AMETHYST_LIKE_BIOMES = create("amethyst_like_biomes");
        public static final TagKey<Biome> TEALIVE_LIKE_BIOMES = create("tealive_like_biomes");
        public static final TagKey<Biome> DARK_WATERFUL_BIOMES = create("dark_waterful_biomes");
        public static final TagKey<Biome> GLACIEMITE_MAIN_STONE_BIOMES = create("glaciemite_main_stone_biomes");
        public static final TagKey<Biome> NIGHTDARK_GRASS_COVERED_BIOMES = create("nightdark_grass_covered_biomes");
        private static TagKey<Biome> create(String name) {
            return TagKey.of(RegistryKeys.BIOME, UnseenWorld.makeID(name));
        }
    }
}