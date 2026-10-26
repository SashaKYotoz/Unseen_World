package net.sashakyotoz.common.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;

import java.util.function.BiConsumer;

public class ModChestLootTableProvider extends SimpleFabricLootTableProvider {
    public static final ResourceLocation WARRIOR_RUINS = chest("warrior_ruins");
    public static final ResourceLocation WARRIOR_FORTRESS = chest("warrior_fortress");
    public static final ResourceLocation SENTINEL_RUINS = chest("sentinel_ruins");
    public static final ResourceLocation SENTINEL_STRONGHOLD = chest("sentinel_stronghold");
    public static final ResourceLocation PATRIARCH_RUINS = chest("patriarch_ruins");
    public static final ResourceLocation PATRIARCH_CITADEL = chest("patriarch_citadel");
    public static final ResourceLocation VIOLEGER_CANTONMENT = chest("violeger_cantonment");
    public static final ResourceLocation VIOLEGER_CAMP_REMAINS = chest("violeger_camp_remains");
    public static final ResourceLocation DARKNESS_MINESHAFT_MAP = chest("darkness_mineshaft_map");
    public static final ResourceLocation DARKNESS_MINESHAFT_ORES = chest("darkness_mineshaft_ores");
    public static final ResourceLocation FROST_BROKEN_PORTAL_LOOT = chest("frost_broken_portal_loot");
    public static final ResourceLocation VIOLEGER_HOLLOWHOLD_LARDER = chest("violeger_hollowhold_larder");
    public static final ResourceLocation VIOLEGER_HOLLOWHOLD_ARMORY = chest("violeger_hollowhold_armory");
    public static final ResourceLocation VIOLEGER_HOLLOWHOLD_VAULT = chest("violeger_hollowhold_vault");

    public ModChestLootTableProvider(FabricDataOutput output) {
        super(output, LootContextParamSets.CHEST);
    }

    @Override
    public String getName() {
        return "Unseen World Structure Chest Loot";
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> exporter) {
        exporter.accept(WARRIOR_RUINS, warriorRuins());
        exporter.accept(WARRIOR_FORTRESS, warriorFortress());
        exporter.accept(SENTINEL_RUINS, sentinelRuins());
        exporter.accept(SENTINEL_STRONGHOLD, sentinelStronghold());
        exporter.accept(PATRIARCH_RUINS, patriarchRuins());
        exporter.accept(PATRIARCH_CITADEL, patriarchCitadel());
        exporter.accept(VIOLEGER_CANTONMENT, violegerCantonment());
        exporter.accept(VIOLEGER_CAMP_REMAINS, violegerCampRemains());
        exporter.accept(DARKNESS_MINESHAFT_MAP, darknessMineshaftMap());
        exporter.accept(DARKNESS_MINESHAFT_ORES, darknessMineshaftOres());
        exporter.accept(FROST_BROKEN_PORTAL_LOOT, frostBrokenPortalLoot());
        exporter.accept(VIOLEGER_HOLLOWHOLD_LARDER, violegerHollowholdLarder());
        exporter.accept(VIOLEGER_HOLLOWHOLD_ARMORY, violegerHollowholdArmory());
        exporter.accept(VIOLEGER_HOLLOWHOLD_VAULT, violegerHollowholdVault());
    }

    private static LootTable.Builder warriorRuins() {
        return LootTable.lootTable()
                .withPool(pool(2, 4)
                        .add(stack(ModItems.COOKED_RIMEHORN_MEAT, 10, 1, 3))
                        .add(stack(ModItems.RIMEHORN_MEAT, 8, 1, 4))
                        .add(stack(ModItems.NIGHTBERRY, 8, 2, 5))
                        .add(stack(ModItems.GLOW_APPLE, 4, 1, 2))
                        .add(stack(Items.BONE, 6, 1, 4))
                        .add(stack(Items.IRON_NUGGET, 8, 3, 9))
                        .add(stack(Items.ARROW, 5, 4, 10)))
                .withPool(pool(1, 2)
                        .add(stack(ModItems.GRANULATED_GRIPCRYSTAL, 10, 2, 5))
                        .add(stack(ModItems.GRIPCRYSTAL, 5, 1, 2))
                        .add(stack(ModItems.RAW_ABYSSAL_ORE, 6, 1, 3))
                        .add(stack(ModItems.THICK_STRING, 6, 1, 3))
                        .add(stack(Items.IRON_INGOT, 8, 1, 3))
                        .add(stack(ModItems.ABYSSAL_INGOT, 2, 1, 1)))
                .withPool(pool(1, 1)
                        .add(empty(24))
                        .add(damaged(ModItems.ABYSSAL_SWORD, 2, 0.15F, 0.45F))
                        .add(damaged(ModItems.ABYSSAL_AXE, 2, 0.15F, 0.45F))
                        .add(damaged(ModItems.ABYSSAL_HELMET, 2, 0.15F, 0.45F))
                        .add(single(ModItems.GRIPCRYSTAL_KEY, 2))
                        .add(single(ModItems.ECLIPSE_KEYSTONE, 1)));
    }

    private static LootTable.Builder warriorFortress() {
        return LootTable.lootTable()
                .withPool(pool(2, 3)
                        .add(stack(ModItems.COOKED_RIMEHORN_MEAT, 10, 2, 5))
                        .add(stack(ModItems.GLOW_APPLE, 6, 1, 3))
                        .add(stack(ModItems.CRYSTIE_APPLE, 4, 1, 2))
                        .add(potion(Potions.STRONG_HEALING, 4))
                        .add(potion(Potions.LONG_NIGHT_VISION, 4))
                        .add(potion(Potions.STRONG_STRENGTH, 2)))
                .withPool(pool(2, 4)
                        .add(stack(ModItems.RAW_ABYSSAL_ORE, 10, 2, 5))
                        .add(stack(ModItems.ABYSSAL_INGOT, 8, 1, 3))
                        .add(stack(ModItems.GRIPCRYSTAL, 8, 1, 3))
                        .add(stack(ModItems.GRANULATED_GRIPTONITE, 6, 1, 3))
                        .add(stack(ModItems.THICK_STRING, 5, 2, 4))
                        .add(stack(Items.DIAMOND, 3, 1, 2))
                        .add(stack(Items.EXPERIENCE_BOTTLE, 5, 2, 5)))
                .withPool(pool(1, 1)
                        .add(empty(6))
                        .add(enchanted(ModItems.ABYSSAL_HELMET, 3, 10, 30))
                        .add(enchanted(ModItems.ABYSSAL_CHESTPLATE, 2, 10, 30))
                        .add(enchanted(ModItems.ABYSSAL_LEGGINGS, 2, 10, 30))
                        .add(enchanted(ModItems.ABYSSAL_BOOTS, 3, 10, 30))
                        .add(enchanted(ModItems.ABYSSAL_SWORD, 3, 10, 30))
                        .add(enchanted(ModItems.ABYSSAL_PICKAXE, 3, 10, 30))
                        .add(book(4))
                        .add(single(ModItems.GRIPCRYSTAL_KEY, 4)));
    }

    private static LootTable.Builder sentinelRuins() {
        return LootTable.lootTable()
                .withPool(pool(2, 4)
                        .add(stack(ModItems.BEARFRUIT_BRAMBLE, 10, 2, 4))
                        .add(stack(ModItems.CRYSTIE_APPLE, 6, 1, 2))
                        .add(stack(ModItems.GLOW_APPLE, 6, 1, 2))
                        .add(stack(ModItems.WARPEDVEIL_VINE_FRUIT, 6, 1, 3))
                        .add(stack(Items.BONE, 6, 1, 4))
                        .add(stack(Items.IRON_NUGGET, 8, 3, 9))
                        .add(stack(Items.PAPER, 4, 2, 6)))
                .withPool(pool(1, 2)
                        .add(stack(ModItems.RAW_UNSEENIUM_ORE, 10, 1, 3))
                        .add(stack(ModItems.UNSEENIUM_INGOT, 4, 1, 2))
                        .add(stack(ModItems.RAW_ABYSSAL_ORE, 4, 1, 2))
                        .add(stack(ModItems.GRANULATED_GRIPTONITE, 6, 1, 3))
                        .add(stack(ModItems.GRIPPING_MEMBRANE, 3, 1, 2))
                        .add(stack(Items.IRON_INGOT, 8, 1, 3)))
                .withPool(pool(1, 1)
                        .add(empty(24))
                        .add(damaged(ModItems.UNSEENIUM_SWORD, 4, 0.15F, 0.5F))
                        .add(damaged(ModItems.UNSEENIUM_CHESTPLATE, 3, 0.15F, 0.5F))
                        .add(damaged(ModItems.ABYSSAL_SWORD, 1, 0.3F, 0.6F))
                        .add(single(ModItems.ABYSSAL_KEY, 2))
                        .add(single(ModItems.ECLIPSE_KEYSTONE, 1)));
    }

    private static LootTable.Builder sentinelStronghold() {
        return LootTable.lootTable()
                .withPool(pool(2, 3)
                        .add(stack(ModItems.BEARFRUIT_BRAMBLE, 8, 2, 5))
                        .add(stack(ModItems.CRYSTIE_APPLE, 6, 1, 3))
                        .add(stack(ModItems.GLOW_APPLE, 6, 1, 3))
                        .add(potion(Potions.STRONG_HEALING, 4))
                        .add(potion(Potions.LONG_SWIFTNESS, 4))
                        .add(potion(Potions.LONG_NIGHT_VISION, 3)))
                .withPool(pool(2, 4)
                        .add(stack(ModItems.UNSEENIUM_INGOT, 10, 1, 3))
                        .add(stack(ModItems.ABYSSAL_INGOT, 5, 1, 2))
                        .add(stack(ModItems.GRANULATED_GRIPTONITE, 10, 2, 6))
                        .add(single(ModItems.GRIPTONITE, 3))
                        .add(stack(ModItems.GRIPPING_MEMBRANE, 4, 1, 3))
                        .add(stack(Items.DIAMOND, 3, 1, 2))
                        .add(stack(Items.EXPERIENCE_BOTTLE, 5, 2, 5)))
                .withPool(pool(1, 1)
                        .add(empty(6))
                        .add(enchanted(ModItems.UNSEENIUM_SWORD, 4, 15, 30))
                        .add(enchanted(ModItems.UNSEENIUM_HELMET, 3, 15, 30))
                        .add(enchanted(ModItems.UNSEENIUM_CHESTPLATE, 3, 15, 30))
                        .add(enchanted(ModItems.UNSEENIUM_LEGGINGS, 3, 15, 30))
                        .add(enchanted(ModItems.UNSEENIUM_BOOTS, 3, 15, 30))
                        .add(enchanted(ModItems.ABYSSAL_SWORD, 2, 20, 30))
                        .add(book(4))
                        .add(single(ModItems.ABYSSAL_KEY, 4))
                        .add(single(ModItems.ECLIPSE_KEYSTONE, 2)));
    }

    private static LootTable.Builder patriarchRuins() {
        return LootTable.lootTable()
                .withPool(pool(2, 4)
                        .add(stack(ModItems.GLOW_APPLE, 8, 1, 3))
                        .add(stack(ModItems.WARPEDVEIL_VINE_FRUIT, 8, 1, 3))
                        .add(stack(Items.GOLDEN_CARROT, 5, 1, 3))
                        .add(stack(Items.GOLD_NUGGET, 10, 3, 12))
                        .add(stack(Items.SPECTRAL_ARROW, 4, 2, 6))
                        .add(stack(Items.BONE, 4, 1, 3)))
                .withPool(pool(1, 2)
                        .add(stack(ModItems.RAW_RED_TITANIUM_ORE, 10, 1, 3))
                        .add(stack(Items.GOLD_INGOT, 8, 1, 3))
                        .add(stack(ModItems.RED_TITANIUM_INGOT, 2, 1, 1))
                        .add(stack(ModItems.GRANULATED_GRIPTONITE, 4, 1, 2))
                        .add(stack(ModItems.THICK_STRING, 4, 1, 3)))
                .withPool(pool(1, 1)
                        .add(empty(24))
                        .add(damaged(ModItems.RED_TITANIUM_SWORD, 2, 0.2F, 0.5F))
                        .add(damaged(ModItems.RED_TITANIUM_HELMET, 2, 0.2F, 0.5F))
                        .add(damaged(ModItems.RED_TITANIUM_BOOTS, 2, 0.2F, 0.5F))
                        .add(single(Items.GOLDEN_APPLE, 3))
                        .add(single(ModItems.AURIC_KEY, 2))
                        .add(single(ModItems.ECLIPSE_KEYSTONE, 1)));
    }

    private static LootTable.Builder patriarchCitadel() {
        return LootTable.lootTable()
                .withPool(pool(2, 3)
                        .add(stack(ModItems.GLOW_APPLE, 6, 2, 4))
                        .add(stack(Items.GOLDEN_CARROT, 6, 2, 5))
                        .add(single(Items.GOLDEN_APPLE, 4))
                        .add(potion(Potions.STRONG_HEALING, 4))
                        .add(potion(Potions.LONG_FIRE_RESISTANCE, 4))
                        .add(potion(Potions.LONG_SLOW_FALLING, 3)))
                .withPool(pool(2, 4)
                        .add(stack(ModItems.RED_TITANIUM_INGOT, 8, 1, 3))
                        .add(stack(ModItems.RAW_RED_TITANIUM_ORE, 8, 2, 5))
                        .add(stack(Items.GOLD_INGOT, 10, 2, 6))
                        .add(single(Items.GOLD_BLOCK, 2))
                        .add(stack(Items.SPECTRAL_ARROW, 5, 4, 10))
                        .add(stack(Items.DIAMOND, 3, 1, 2))
                        .add(stack(Items.EXPERIENCE_BOTTLE, 5, 3, 6)))
                .withPool(pool(1, 1)
                        .add(empty(6))
                        .add(enchanted(ModItems.RED_TITANIUM_HELMET, 3, 10, 30))
                        .add(enchanted(ModItems.RED_TITANIUM_CHESTPLATE, 2, 10, 30))
                        .add(enchanted(ModItems.RED_TITANIUM_LEGGINGS, 2, 10, 30))
                        .add(enchanted(ModItems.RED_TITANIUM_BOOTS, 3, 10, 30))
                        .add(enchanted(ModItems.RED_TITANIUM_SWORD, 3, 10, 30))
                        .add(enchanted(ModItems.RED_TITANIUM_PICKAXE, 3, 10, 30))
                        .add(single(Items.ENCHANTED_GOLDEN_APPLE, 1))
                        .add(book(4))
                        .add(single(ModItems.AURIC_KEY, 4)));
    }

    private static LootTable.Builder violegerCantonment() {
        return LootTable.lootTable()
                .withPool(pool(2, 4)
                        .add(stack(ModItems.COOKED_RIMEHORN_MEAT, 10, 2, 4))
                        .add(stack(ModItems.NIGHTBERRY, 8, 2, 6))
                        .add(stack(ModItems.BEARFRUIT_BRAMBLE, 6, 1, 3))
                        .add(stack(Items.BREAD, 4, 1, 3))
                        .add(stack(Items.ARROW, 8, 6, 16))
                        .add(stack(Items.LEATHER, 5, 1, 3)))
                .withPool(pool(1, 3)
                        .add(stack(ModItems.THICK_STRING, 10, 2, 5))
                        .add(stack(ModItems.GRANULATED_GRIPCRYSTAL, 10, 2, 5))
                        .add(stack(ModItems.GRIPCRYSTAL, 5, 1, 2))
                        .add(stack(ModItems.GRIPPING_MEMBRANE, 8, 1, 3))
                        .add(stack(ModItems.GRANULATED_GRIPTONITE, 6, 1, 4))
                        .add(stack(ModItems.RAW_UNSEENIUM_ORE, 6, 1, 3))
                        .add(stack(ModItems.UNSEENIUM_INGOT, 3, 1, 2))
                        .add(stack(Items.IRON_INGOT, 8, 1, 4))
                        .add(stack(Items.EXPERIENCE_BOTTLE, 3, 1, 3)))
                .withPool(pool(1, 1)
                        .add(empty(12))
                        .add(enchanted(ModItems.UNSEENIUM_SWORD, 4, 5, 20))
                        .add(enchanted(ModItems.UNSEENIUM_AXE, 4, 5, 20))
                        .add(enchanted(Items.CROSSBOW, 3, 5, 20))
                        .add(single(ModItems.GRIPPING_BUNDLE, 3))
                        .add(single(ModItems.GRIPPING_GAUNTLET, 2))
                        .add(single(Items.SADDLE, 2))
                        .add(book(2))
                        .add(single(ModItems.ECLIPSE_KEYSTONE, 2)))
                .withPool(pool(1, 1)
                        .add(empty(3))
                        .add(structureMap(1)));
    }

    private static LootTable.Builder darknessMineshaftMap() {
        return LootTable.lootTable()
                .withPool(pool(1, 1)
                        .add(ruinStructureMap(2)))
                .withPool(pool(2, 2)
                        .add(single(ModItems.RAW_UNSEENIUM_ORE, 3))
                        .add(single(Items.MAP, 1))
                        .add(single(Items.RECOVERY_COMPASS, 1))
                        .add(stack(ModItems.THICK_STRING, 15, 1, 10))
                        .add(single(ModItems.ABYSSAL_KEY, 3))
                        .add(stack(ModItems.GRANULATED_GRIPCRYSTAL, 5, 1, 5)))
                .withPool(pool(1, 1)
                        .add(empty(1))
                        .add(stack(ModItems.GRIPTONITE, 1, 2, 2)));
    }

    private static LootTable.Builder darknessMineshaftOres() {
        return LootTable.lootTable()
                .withPool(pool(1, 1)
                        .add(single(ModItems.GLOW_APPLE, 20))
                        .add(single(ModItems.CRYSTIE_APPLE, 1))
                        .add(single(Items.NAME_TAG, 25))
                        .add(bookWithLevels(5, 50))
                        .add(single(ModItems.UNSEENIUM_PICKAXE, 5))
                        .add(empty(5)))
                .withPool(pool(2, 4)
                        .add(stack(ModItems.RAW_UNSEENIUM_ORE, 10, 1, 5))
                        .add(stack(ModItems.RAW_ABYSSAL_ORE, 5, 1, 3))
                        .add(stack(ModItems.GRANULATED_GRIPCRYSTAL, 5, 2, 6))
                        .add(stack(Items.NETHERITE_SCRAP, 3, 1, 2))
                        .add(stack(ModItems.RAW_RED_TITANIUM_ORE, 5, 1, 2))
                        .add(stack(ModBlocks.GLACIEMITE_BULB, 10, 3, 6)))
                .withPool(pool(1, 1)
                        .add(stack(ModBlocks.GRIPTONITE_LANTERN, 2, 1, 3)));
    }

    private static LootTable.Builder frostBrokenPortalLoot() {
        return LootTable.lootTable()
                .withPool(pool(1, 3)
                        .setBonusRolls(ConstantValue.exactly(1))
                        .add(single(ModItems.ECLIPSE_KEYSTONE, 16))
                        .add(stack(ModBlocks.DARK_FROST_BRICKS, 6, 1, 4))
                        .add(stack(Items.NETHERITE_SCRAP, 5, 1, 2))
                        .add(stack(Items.DIAMOND, 5, 1, 3))
                        .add(stack(Items.BREAD, 15, 1, 5))
                        .add(stack(ModItems.CRYSTIE_APPLE, 10, 1, 4))
                        .add(single(Items.IRON_PICKAXE, 5))
                        .add(single(Items.DIAMOND_SWORD, 5))
                        .add(single(Items.IRON_CHESTPLATE, 5))
                        .add(single(Items.IRON_HELMET, 5))
                        .add(single(Items.IRON_LEGGINGS, 5))
                        .add(single(Items.IRON_BOOTS, 5))
                        .add(single(Items.GOLDEN_APPLE, 1))
                        .add(single(Items.MUSIC_DISC_WAIT, 1))
                        .add(bookWithLevels(1, 30)))
                .withPool(pool(1, 1)
                        .add(empty(9))
                        .add(single(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, 1)));
    }

    private static LootTable.Builder violegerCampRemains() {
        return LootTable.lootTable()
                .withPool(pool(2, 4)
                        .add(stack(ModItems.RIMEHORN_MEAT, 6, 1, 2))
                        .add(stack(ModItems.NIGHTBERRY, 6, 1, 4))
                        .add(stack(Items.BONE, 8, 1, 4))
                        .add(stack(Items.STICK, 6, 2, 6))
                        .add(stack(Items.CHARCOAL, 6, 1, 4))
                        .add(stack(Items.ARROW, 4, 2, 6)))
                .withPool(pool(1, 2)
                        .add(stack(ModItems.THICK_STRING, 8, 1, 3))
                        .add(stack(ModItems.GRIPPING_MEMBRANE, 3, 1, 1))
                        .add(stack(Items.IRON_NUGGET, 8, 2, 7))
                        .add(stack(Items.LEATHER, 5, 1, 2))
                        .add(stack(ModItems.RAW_UNSEENIUM_ORE, 3, 1, 2)))
                .withPool(pool(1, 1)
                        .add(empty(16))
                        .add(damaged(ModItems.UNSEENIUM_SWORD, 3, 0.5F, 0.85F))
                        .add(damaged(ModItems.UNSEENIUM_SHOVEL, 3, 0.5F, 0.85F))
                        .add(damaged(Items.CROSSBOW, 3, 0.5F, 0.85F))
                        .add(single(ModItems.ECLIPSE_KEYSTONE, 1)));
    }

    private static LootTable.Builder violegerHollowholdLarder() {
        return LootTable.lootTable()
                .withPool(pool(2, 4)
                        .add(stack(ModItems.COOKED_RIMEHORN_MEAT, 6, 1, 4))
                        .add(stack(ModItems.RIMEHORN_MEAT, 10, 1, 6))
                        .add(stack(ModItems.GLOW_APPLE, 8, 1, 3))
                        .add(stack(ModItems.NIGHTBERRY, 8, 1, 4))
                        .add(stack(ModItems.GLOWROOT_BULB, 8, 1, 5))
                        .add(stack(ModItems.BEARFRUIT_BRAMBLE, 6, 1, 4))
                        .add(stack(ModItems.WARPEDVEIL_VINE_FRUIT, 5, 1, 3))
                        .add(single(Items.MUSHROOM_STEW, 3))
                        .add(single(ModItems.CRYSTIE_APPLE, 2)))
                .withPool(pool(1, 2)
                        .add(stack(Items.RED_MUSHROOM, 5, 1, 3))
                        .add(stack(Items.BROWN_MUSHROOM, 5, 1, 3))
                        .add(stack(Items.BONE_MEAL, 4, 2, 6))
                        .add(stack(Items.BOWL, 3, 1, 3))
                        .add(stack(Items.SALMON, 2, 1, 3))
                        .add(stack(ModBlocks.GRIPTONITE_LANTERN, 2, 1, 2)));
    }

    private static LootTable.Builder violegerHollowholdArmory() {
        return LootTable.lootTable()
                .withPool(pool(2, 4)
                        .add(stack(Items.ARROW, 10, 6, 16))
                        .add(stack(ModItems.THICK_STRING, 8, 2, 5))
                        .add(stack(Items.IRON_INGOT, 6, 1, 3))
                        .add(stack(ModItems.GRANULATED_GRIPCRYSTAL, 6, 2, 4))
                        .add(stack(ModItems.GRANULATED_GRIPTONITE, 5, 1, 3))
                        .add(stack(ModItems.COOKED_RIMEHORN_MEAT, 4, 1, 2))
                        .add(stack(Items.EXPERIENCE_BOTTLE, 3, 1, 3)))
                .withPool(pool(1, 1)
                        .add(empty(8))
                        .add(enchanted(Items.CROSSBOW, 4, 5, 20))
                        .add(single(Items.SHIELD, 2))
                        .add(damaged(ModItems.UNSEENIUM_SWORD, 3, 0.2F, 0.5F))
                        .add(damaged(ModItems.UNSEENIUM_HELMET, 2, 0.2F, 0.5F))
                        .add(enchanted(ModItems.UNSEENIUM_PICKAXE, 3, 5, 20))
                        .add(damaged(ModItems.GRIPPING_ABYSSAL_BOW, 1, 0.3F, 0.6F)));
    }

    private static LootTable.Builder violegerHollowholdVault() {
        return LootTable.lootTable()
                .withPool(pool(2, 4)
                        .add(stack(ModItems.RAW_UNSEENIUM_ORE, 10, 1, 4))
                        .add(stack(ModItems.RAW_ABYSSAL_ORE, 6, 1, 3))
                        .add(stack(ModItems.RAW_RED_TITANIUM_ORE, 5, 1, 3))
                        .add(stack(ModItems.UNSEENIUM_INGOT, 4, 1, 2))
                        .add(single(ModItems.ABYSSAL_INGOT, 2))
                        .add(single(ModItems.RED_TITANIUM_INGOT, 2))
                        .add(stack(ModItems.GRIPCRYSTAL, 6, 1, 3))
                        .add(stack(Items.GOLD_INGOT, 5, 2, 4))
                        .add(stack(Items.EMERALD, 4, 2, 5))
                        .add(stack(Items.DIAMOND, 5, 1, 2)))
                .withPool(pool(1, 1)
                        .add(empty(3))
                        .add(structureMap(3)))
                .withPool(pool(1, 1)
                        .add(empty(10))
                        .add(single(ModItems.GRIPTONITE, 3))
                        .add(enchanted(ModItems.UNSEENIUM_PICKAXE, 2, 15, 30))
                        .add(book(2))
                        .add(single(ModItems.ECLIPSE_KEYSTONE, 2))
                        .add(single(ModItems.GRIPPING_BUNDLE, 2))
                        .add(single(ModItems.GRIPPING_GAUNTLET, 1)));
    }

    private static ResourceLocation chest(String name) {
        return UnseenWorld.MOD.makeID("chests/" + name);
    }

    private static LootPool.Builder pool(int minRolls, int maxRolls) {
        return LootPool.lootPool().setRolls(minRolls == maxRolls
                ? ConstantValue.exactly(minRolls)
                : UniformGenerator.between(minRolls, maxRolls));
    }

    private static LootPoolEntryContainer.Builder<?> empty(int weight) {
        return EmptyLootItem.emptyItem().setWeight(weight);
    }

    private static LootPoolSingletonContainer.Builder<?> single(ItemLike item, int weight) {
        return LootItem.lootTableItem(item).setWeight(weight);
    }

    private static LootPoolSingletonContainer.Builder<?> stack(ItemLike item, int weight, int min, int max) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(min == max ? ConstantValue.exactly(min) : UniformGenerator.between(min, max)));
    }

    private static LootPoolSingletonContainer.Builder<?> damaged(ItemLike item, int weight, float minWear, float maxWear) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(1.0F - maxWear, 1.0F - minWear)));
    }

    private static LootPoolSingletonContainer.Builder<?> enchanted(ItemLike item, int weight, int minLevels, int maxLevels) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(minLevels, maxLevels)));
    }

    private static LootPoolSingletonContainer.Builder<?> book(int weight) {
        return LootItem.lootTableItem(Items.BOOK)
                .setWeight(weight)
                .apply(EnchantRandomlyFunction.randomApplicableEnchantment());
    }

    private static LootPoolSingletonContainer.Builder<?> bookWithLevels(int weight, int levels) {
        return LootItem.lootTableItem(Items.BOOK)
                .setWeight(weight)
                .apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(levels)));
    }

    private static LootPoolSingletonContainer.Builder<?> structureMap(int weight) {
        return LootItem.lootTableItem(Items.MAP)
                .setWeight(weight)
                .apply(ExplorationMapFunction.makeExplorationMap()
                        .setDestination(ModTags.Structures.STRUCTURES_MAP_CAN_LOCATE)
                        .setMapDecoration(MapDecoration.Type.RED_X)
                        .setZoom((byte) 2)
                        .setSkipKnownStructures(true))
                .apply(SetNameFunction.setName(Component.translatable("filled_map.unseen_world.structures")));
    }
    private static LootPoolSingletonContainer.Builder<?> ruinStructureMap(int weight) {
        return LootItem.lootTableItem(Items.MAP)
                .setWeight(weight)
                .apply(ExplorationMapFunction.makeExplorationMap()
                        .setDestination(ModTags.Structures.RUIN_STRUCTURES_MAP_CAN_LOCATE)
                        .setMapDecoration(MapDecoration.Type.RED_X)
                        .setZoom((byte) 2)
                        .setSkipKnownStructures(false))
                .apply(SetNameFunction.setName(Component.translatable("filled_map.unseen_world.ruined_structures")));
    }

    private static LootPoolSingletonContainer.Builder<?> potion(Potion potion, int weight) {
        return LootItem.lootTableItem(Items.POTION)
                .setWeight(weight)
                .apply(SetPotionFunction.setPotion(potion));
    }
}