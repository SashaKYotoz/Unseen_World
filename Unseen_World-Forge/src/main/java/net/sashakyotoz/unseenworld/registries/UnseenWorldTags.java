package net.sashakyotoz.unseenworld.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.sashakyotoz.unseenworld.UnseenWorldMod;

public class UnseenWorldTags {
    public static class Blocks {
        public static final TagKey<Block> STONE_THE_DARKNESS = tag("stone_the_darkness");
        public static final TagKey<Block> DIRT_THE_DARKNESS = tag("dirt_the_darkness");
        public static final TagKey<Block> DRIPSTONE_OF_AMETHYST_CAN_GROW_ON = tag("dripstone_of_amethyst_can_grow_on");
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(UnseenWorldMod.MODID, name));
        }
    }
    public static class Entities{
        public static final TagKey<EntityType<?>> ON_DARK_GRASS_SPAWN_WHITELIST = create("on_dark_grass_spawn_whitelist");
        private static TagKey<EntityType<?>> create(String s) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(UnseenWorldMod.MODID,s));
        }
    }
    public static class Items {
        public static final TagKey<Item> DARK_WATER_PROTECTED_HELMETS = tag("dark_water_protected_helmets");
        public static final TagKey<Item> TREASURE_WEAPONS = tag("treasure_weapons");
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(UnseenWorldMod.MODID, name));
        }
    }
    public static class Biomes{
        public static final TagKey<Biome> ICEBERGS_BLACKLIST_BIOMES = create("icebergs_blacklist_biomes");
        private static TagKey<Biome> create(String s) {
            return TagKey.create(Registries.BIOME, new ResourceLocation(UnseenWorldMod.MODID,s));
        }
    }
}
