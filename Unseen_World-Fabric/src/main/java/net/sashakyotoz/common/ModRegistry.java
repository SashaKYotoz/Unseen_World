package net.sashakyotoz.common;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.datagen.advancements.CuredGripcrystalEntityCriterion;
import net.sashakyotoz.common.datagen.recipes.WarriorShieldDecorationRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModRegistry {
    //constants
    public static final CuredGripcrystalEntityCriterion CURED_GRIPCRYSTAL_ENTITY_CRITERION = CriteriaTriggers.register(new CuredGripcrystalEntityCriterion());

    public static final ResourceKey<Structure> WARRIOR_OF_DARKNESS_TOWER = ResourceKey.create(Registries.STRUCTURE, UnseenWorld.makeID("warrior_of_darkness_tower"));
    public static final ResourceKey<Structure> ECLIPSE_CORE = ResourceKey.create(Registries.STRUCTURE, UnseenWorld.makeID("eclipse_core"));

    public static RecipeSerializer<WarriorShieldDecorationRecipe> WARRIOR_SHIELD_DECORATION = register(
            "crafting_warrior_shield_decoration", new SimpleCraftingRecipeSerializer<>(WarriorShieldDecorationRecipe::new)
    );

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, String.format("%s:%s", UnseenWorld.MOD_ID, id), serializer);
    }

    public static class BlockBuilder {
        protected BlockBuilder(ResourceLocation id, Block block, boolean item) {
            this.id = id;
            this.block = Registry.register(BuiltInRegistries.BLOCK, id, block);
            if (item)
                ModRegistry.ofItem(id.getPath(), new BlockItem(block, new FabricItemSettings())).build();
        }

        public Block build() {
            BLOCKS.add(this.block);
            return this.block;
        }

        protected ResourceLocation id;
        protected Block block;

        public BlockBuilder drop() {
            return this.drop(this.block);
        }

        public BlockBuilder drop(ItemLike loot) {
            BLOCK_DROPS.putIfAbsent(this.block, loot);
            return this;
        }

        public BlockBuilder drop_silk(ItemLike loot) {
            drop(loot);
            BLOCK_SILK_DROPS.putIfAbsent(this.block, loot);
            return this;
        }

        public BlockBuilder drop_shears(ItemLike loot) {
            drop(loot);
            BLOCK_SHEARS_DROPS.putIfAbsent(this.block, loot);
            return this;
        }

        public BlockBuilder drop_shears() {
            drop();
            BLOCK_SHEARS_DROPS.putIfAbsent(this.block, this.block.asItem());
            return this;
        }

        public BlockBuilder drop_tall_shears(Block grass) {
            drop();
            TALL_BLOCK_SHEARS_DROPS.putIfAbsent(this.block, new Tuple<>(grass, this.block));
            return this;
        }

        public BlockBuilder tag(TagKey<Block> tagname) {
            BLOCK_TAGS.putIfAbsent(tagname, new ArrayList<>());
            BLOCK_TAGS.get(tagname).add(this.block);
            return this;
        }

        @SafeVarargs
        public final BlockBuilder tag(TagKey<Block>... tags) {
            for (TagKey<Block> tagname : tags) {
                this.tag(tagname);
            }
            return this;
        }

        public BlockBuilder tagitem(TagKey<Item> tagname) {
            ITEM_TAGS.putIfAbsent(tagname, new ArrayList<>());
            ITEM_TAGS.get(tagname).add(this.block.asItem());
            return this;
        }

        @SafeVarargs
        public final BlockBuilder tagitem(TagKey<Item>... tags) {
            for (TagKey<Item> tagname : tags) {
                this.tagitem(tagname);
            }
            return this;
        }

        public BlockBuilder tool(String tool_material) {
            String[] needed = tool_material.split("_");

            if (needed[0].equals("stone")) this.tag(BlockTags.NEEDS_STONE_TOOL);
            if (needed[0].equals("iron")) this.tag(BlockTags.NEEDS_IRON_TOOL);
            if (needed[0].equals("diamond")) this.tag(BlockTags.NEEDS_DIAMOND_TOOL);

            if (needed[1].equals("pickaxe")) this.tag(BlockTags.MINEABLE_WITH_PICKAXE);
            if (needed[1].equals("axe")) this.tag(BlockTags.MINEABLE_WITH_AXE);
            if (needed[1].equals("shovel")) this.tag(BlockTags.MINEABLE_WITH_SHOVEL);
            if (needed[1].equals("hoe")) this.tag(BlockTags.MINEABLE_WITH_HOE);
            if (needed[1].equals("sword")) this.tag(BlockTags.SWORD_EFFICIENT);

            return this;
        }

        public BlockBuilder model() {
            return this.model(Models.CUBE);
        }

        public static void registerBricksSet(Block parent, Block stairs, Block slab, Block wall) {
            registerSet(parent, Map.of(
                    Models.STAIRS, stairs,
                    Models.SLAB, slab,
                    Models.WALL, wall
            ));
        }

        public static void registerPolishedSet(Block parent, Block stairs, Block slab) {
            registerSet(parent, Map.of(
                    Models.STAIRS, stairs,
                    Models.SLAB, slab
            ));
        }

        public BlockBuilder model(Models model) {
            BLOCK_MODELS.putIfAbsent(model, new ArrayList<>());
            BLOCK_MODELS.get(model).add(this.block);
            return this;
        }

        public BlockBuilder model(ModelTemplate model) {
            ITEM_MODELS.put(this.block.asItem(), model);
            return this;
        }

        public BlockBuilder cutout() {
            BLOCK_CUTOUT.add(this.block);
            return this;
        }

        public BlockBuilder translucent() {
            BLOCK_TRANSLUCENT.add(this.block);
            return this;
        }

        public BlockBuilder fuel(int duration) {
            ITEM_BURNABLE.put(this.block, duration);
            return this;
        }

        public BlockBuilder flammable(int duration, int spread) {
            BLOCK_FLAMMABLE.put(this.block, new Tuple<>(duration, spread));
            return this;
        }

        public BlockBuilder strip(Block stripped) {
            BLOCK_STRIPPED.putIfAbsent(this.block, stripped);
            return this;
        }
    }

    public static class ItemBuilder {
        protected ItemBuilder(ResourceLocation id, Item item) {
            this.id = id;
            this.item = Registry.register(BuiltInRegistries.ITEM, id, item);
        }

        public Item build() {
            ITEMS.add(this.item);
            return this.item;
        }

        protected ResourceLocation id;
        protected Item item;

        public ItemBuilder tag(TagKey<Item> tagname) {
            ITEM_TAGS.putIfAbsent(tagname, new ArrayList<>());
            ITEM_TAGS.get(tagname).add(this.item);
            return this;
        }

        @SafeVarargs
        public final ItemBuilder tag(TagKey<Item>... tags) {
            for (TagKey<Item> tagname : tags) {
                this.tag(tagname);
            }
            return this;
        }

        public ItemBuilder model(ModelTemplate model) {
            ITEM_MODELS.put(this.item, model);
            return this;
        }

        public ItemBuilder fuel(int duration) {
            ITEM_BURNABLE.put(this.item, duration);
            return this;
        }
    }


    public enum Models {
        CUBE,
        GRASS,
        LOG,
        FLUID,
        WALL,
        CROSS,
        PILLAR,
        WOOD,
        STAIRS,
        SLAB,
        BUTTON,
        PRESSURE_PLATE,
        FENCE,
        FENCE_GATE,
        DOOR,
        TRAPDOOR,
        SIGN,
        WALL_SIGN,
        HANGING_SIGN,
        WALL_HANGING_SIGN,
        BULB,
        PANE
    }

    public static class Foods {
        public static final FoodProperties CRYSTIE_APPLE = new FoodProperties.Builder().alwaysEat().saturationMod(2).nutrition(3).effect(
                        new MobEffectInstance(MobEffects.HARM, 1, 0, true, false),
                        1)
                .effect(
                        new MobEffectInstance(MobEffects.DIG_SPEED, 240, 1, true, false),
                        1).build();
        public static final FoodProperties GLOW_APPLE = new FoodProperties.Builder().alwaysEat().saturationMod(3).nutrition(3)
                .effect(
                        new MobEffectInstance(MobEffects.GLOWING, 300, 1, true, false),
                        1).build();
        public static final FoodProperties BEARFRUIT_BRAMBLE = new FoodProperties.Builder().alwaysEat().saturationMod(1).nutrition(3).effect(
                        new MobEffectInstance(MobEffects.REGENERATION, 100, 1, true, true),
                        1)
                .effect(
                        new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, true, true),
                        1).build();
        public static final FoodProperties NIGHTBERRY = new FoodProperties.Builder().alwaysEat().saturationMod(2).nutrition(2).effect(
                        new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, true, true),
                        1)
                .effect(
                        new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100, 0, true, true),
                        1).build();
        public static final FoodProperties WARPEDVEIL_VINE_FRUIT = new FoodProperties.Builder().alwaysEat().saturationMod(1).nutrition(3).effect(
                        new MobEffectInstance(MobEffects.GLOWING, 300, 0, true, true),
                        1)
                .effect(
                        new MobEffectInstance(MobEffects.SATURATION, 60, 0, true, true),
                        1).build();
    }


    public static BlockBuilder ofBlock(String id, Block block) {
        return ModRegistry.ofBlock(id, block, true);
    }

    public static BlockBuilder ofBlock(String id, Block block, boolean item) {
        return new BlockBuilder(UnseenWorld.makeID(id), block, item);
    }

    public static ItemBuilder ofItem(String id, Item item) {
        return new ItemBuilder(UnseenWorld.makeID(id), item);
    }


    public static List<Block> getModelList(Models key) {
        return BLOCK_MODELS.getOrDefault(key, new ArrayList<>());
    }


    public static void registerSet(Block parent, Map<Models, Block> set) {
        BLOCK_SETS.putIfAbsent(parent, set);
        for (Models model : set.keySet()) {
            BLOCK_MODELS.putIfAbsent(model, new ArrayList<>());
            BLOCK_MODELS.get(model).add(set.get(model));
        }
    }

    public static void addDrop(Block block, ItemLike loot) {
        BLOCK_DROPS.putIfAbsent(block, loot);
    }


    public static List<Block> BLOCKS = new ArrayList<>();
    public static Map<TagKey<Block>, List<Block>> BLOCK_TAGS = new HashMap<>();

    public static Map<Block, ItemLike> BLOCK_DROPS = new HashMap<>();
    public static Map<Block, ItemLike> BLOCK_SILK_DROPS = new HashMap<>();
    public static Map<Block, ItemLike> BLOCK_SHEARS_DROPS = new HashMap<>();
    public static Map<Block, Tuple<Block, Block>> TALL_BLOCK_SHEARS_DROPS = new HashMap<>();

    public static Map<Block, Block> BLOCK_STRIPPED = new HashMap<>();
    public static Map<Block, Map<Models, Block>> BLOCK_SETS = new HashMap<>();

    public static Map<Models, List<Block>> BLOCK_MODELS = new HashMap<>();
    public static List<Block> BLOCK_CUTOUT = new ArrayList<>();
    public static List<Block> BLOCK_TRANSLUCENT = new ArrayList<>();

    public static Map<Block, Tuple<Integer, Integer>> BLOCK_FLAMMABLE = new HashMap<>();


    public static List<Item> ITEMS = new ArrayList<>();
    public static Map<TagKey<Item>, List<Item>> ITEM_TAGS = new HashMap<>();
    public static Map<Item, ModelTemplate> ITEM_MODELS = new HashMap<>();
    public static Map<ItemLike, Integer> ITEM_BURNABLE = new HashMap<>();

    public static final Potion GLOWING = register("glowing", new Potion(new MobEffectInstance(MobEffects.GLOWING, 3600)));

    private static Potion register(String name, Potion potion) {
        return Registry.register(BuiltInRegistries.POTION, name, potion);
    }
}