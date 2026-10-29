package net.sashakyotoz.common.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;

import java.util.function.BiConsumer;

public class ModArchaeologyLootTableProvider implements LootTableSubProvider {
    public static final ResourceKey<LootTable> GLACIEMITE_RUINS_SAND = archaeology("glaciemite_ruins_sand");
    public static final ResourceKey<LootTable> GLACIEMITE_RUINS_OOZE = archaeology("glaciemite_ruins_ooze");
    public static final ResourceKey<LootTable> GLACIEMITE_RUINS_RARE = archaeology("glaciemite_ruins_rare");
    public static final ResourceKey<LootTable> VIOLEGER_HOLLOWHOLD_RUBBLE = archaeology("violeger_hollowhold_rubble");
    public static final ResourceKey<LootTable> VIOLEGER_HOLLOWHOLD_RELIC = archaeology("violeger_hollowhold_relic");

    public ModArchaeologyLootTableProvider(HolderLookup.Provider registries) {
    }

    public String getName() {
        return "Unseen World Archaeology Loot";
    }

    private static ResourceKey<LootTable> archaeology(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, UnseenWorld.MOD.makeID("archaeology/" + name));
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter) {
        exporter.accept(GLACIEMITE_RUINS_SAND, trailSupplies());
        exporter.accept(GLACIEMITE_RUINS_OOZE, campBelongings());
        exporter.accept(GLACIEMITE_RUINS_RARE, movementRelics());
        exporter.accept(VIOLEGER_HOLLOWHOLD_RUBBLE, hollowholdRubble());
        exporter.accept(VIOLEGER_HOLLOWHOLD_RELIC, hollowholdRelics());
    }

    private static LootTable.Builder trailSupplies() {
        return LootTable.lootTable().withPool(single()
                .add(stack(ModItems.THICK_STRING, 5, 1, 3))
                .add(stack(Items.ARROW, 4, 2, 6))
                .add(stack(ModItems.NIGHTBERRY, 4, 1, 3))
                .add(stack(ModItems.BEARFRUIT_BRAMBLE, 3, 1, 2))
                .add(stack(ModItems.GRANULATED_GRIPCRYSTAL, 3, 1, 3))
                .add(stack(Items.FLINT, 3, 1, 2))
                .add(stack(Items.STICK, 2, 1, 4))
                .add(stack(Items.IRON_NUGGET, 2, 1, 4))
                .add(stack(Items.PURPLE_DYE, 2, 1, 2))
                .add(item(Items.LEATHER, 2))
                .add(item(Items.BOWL, 1))
                .add(item(Items.EMERALD, 1)));
    }

    private static LootTable.Builder campBelongings() {
        return LootTable.lootTable().withPool(single()
                .add(stack(Items.IRON_NUGGET, 4, 2, 5))
                .add(stack(Items.GOLD_NUGGET, 3, 1, 4))
                .add(stack(ModItems.RAW_UNSEENIUM_ORE, 3, 1, 2))
                .add(stack(ModItems.GRANULATED_GRIPTONITE, 3, 1, 2))
                .add(stack(ModItems.THICK_STRING, 2, 1, 3))
                .add(stack(Items.COAL, 2, 1, 3))
                .add(item(Items.PURPLE_CANDLE, 3))
                .add(item(Items.BOWL, 2))
                .add(item(Items.LEAD, 1))
                .add(item(Items.PURPLE_BANNER, 1))
                .add(item(Items.EMERALD, 1))
                .add(LootItem.lootTableItem(Items.CROSSBOW).setWeight(1)
                        .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.05F, 0.35F)))));
    }

    private static LootTable.Builder movementRelics() {
        return LootTable.lootTable().withPool(single()
                .add(LootItem.lootTableItem(Items.MAP).setWeight(3)
                        .apply(ExplorationMapFunction.makeExplorationMap()
                                .setDestination(ModTags.Structures.RUIN_STRUCTURES_MAP_CAN_LOCATE)
                                .setMapDecoration(MapDecorationTypes.RED_X)
                                .setZoom((byte) 2)
                                .setSkipKnownStructures(false))
                        .apply(SetNameFunction.setName(Component.translatable("filled_map.unseen_world.ruined_structures"), SetNameFunction.Target.ITEM_NAME)))
                .add(item(ModItems.GRIPCRYSTAL, 3))
                .add(item(ModItems.GRIPTONITE, 2))
                .add(stack(ModItems.UNSEENIUM_INGOT, 2, 1, 2))
                .add(item(ModItems.ECLIPSE_KEYSTONE, 1))
                .add(LootItem.lootTableItem(ModItems.GRIPPING_ABYSSAL_BOW).setWeight(1)
                        .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.2F, 0.6F))))
                .add(item(ModItems.GRIPPING_BUNDLE, 1))
                .add(item(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, 1))
                .add(item(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, 1))
                .add(item(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, 1))
                .add(item(Items.DANGER_POTTERY_SHERD, 1))
                .add(item(Items.HOWL_POTTERY_SHERD, 1))
                .add(item(Items.PRIZE_POTTERY_SHERD, 1))
                .add(item(Items.HEARTBREAK_POTTERY_SHERD, 1)));
    }

    private static LootTable.Builder hollowholdRubble() {
        return LootTable.lootTable().withPool(single()
                .add(stack(Items.IRON_NUGGET, 5, 2, 6))
                .add(stack(ModItems.THICK_STRING, 4, 1, 3))
                .add(stack(Items.ARROW, 4, 2, 5))
                .add(stack(Items.COAL, 4, 1, 3))
                .add(stack(Items.GOLD_NUGGET, 3, 1, 4))
                .add(stack(ModItems.RAW_UNSEENIUM_ORE, 3, 1, 2))
                .add(stack(ModItems.GRANULATED_GRIPCRYSTAL, 3, 1, 3))
                .add(stack(ModItems.GLOWROOT_BULB, 3, 1, 2))
                .add(stack(ModItems.GRANULATED_GRIPTONITE, 2, 1, 2))
                .add(stack(Items.BONE, 2, 1, 3))
                .add(item(Items.FLINT, 2))
                .add(item(Items.PURPLE_CANDLE, 2))
                .add(item(Items.BOWL, 1)));
    }

    private static LootTable.Builder hollowholdRelics() {
        return LootTable.lootTable().withPool(single()
                .add(LootItem.lootTableItem(Items.MAP).setWeight(2)
                        .apply(ExplorationMapFunction.makeExplorationMap()
                                .setDestination(ModTags.Structures.STRUCTURES_MAP_CAN_LOCATE)
                                .setMapDecoration(MapDecorationTypes.RED_X)
                                .setZoom((byte) 2)
                                .setSkipKnownStructures(false))
                        .apply(SetNameFunction.setName(Component.translatable("filled_map.unseen_world.structures"), SetNameFunction.Target.ITEM_NAME)))
                .add(item(ModItems.GRIPCRYSTAL, 3))
                .add(item(ModItems.GRIPTONITE, 2))
                .add(stack(ModItems.UNSEENIUM_INGOT, 2, 1, 2))
                .add(stack(ModItems.RAW_ABYSSAL_ORE, 2, 1, 3))
                .add(item(Items.DIAMOND, 1))
                .add(item(ModItems.ECLIPSE_KEYSTONE, 1))
                .add(LootItem.lootTableItem(ModItems.GRIPPING_ABYSSAL_BOW).setWeight(1)
                        .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.2F, 0.6F))))
                .add(item(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, 1))
                .add(item(Items.MINER_POTTERY_SHERD, 1))
                .add(item(Items.PLENTY_POTTERY_SHERD, 1))
                .add(item(Items.ARCHER_POTTERY_SHERD, 1))
                .add(item(Items.BURN_POTTERY_SHERD, 1)));
    }

    private static LootPool.Builder single() {
        return LootPool.lootPool().setRolls(ConstantValue.exactly(1));
    }

    private static LootPoolSingletonContainer.Builder<?> item(ItemLike item, int weight) {
        return LootItem.lootTableItem(item).setWeight(weight);
    }

    private static LootPoolSingletonContainer.Builder<?> stack(ItemLike item, int weight, int min, int max) {
        return LootItem.lootTableItem(item).setWeight(weight)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }
}