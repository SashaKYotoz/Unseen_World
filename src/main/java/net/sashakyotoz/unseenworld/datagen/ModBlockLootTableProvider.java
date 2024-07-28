package net.sashakyotoz.unseenworld.datagen;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    private static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.085F, 0.1F};
    public ModBlockLootTableProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
//        this.add(UnseenWorldModBlocks.LUMINOUS_AMETHYST_VINE.get(), (block) -> createSilkTouchOrShearsDispatchTable(block, LootItem.lootTableItem(Items.AMETHYST_SHARD)));
//        this.dropOther(UnseenWorldModBlocks.DARK_CRIMSON_VINE_FLOWER.get(), UnseenWorldModItems.BERRIES_OF_BLOOMING_VINE.get());
//        this.dropOther(UnseenWorldModBlocks.DARK_CRIMSON_BLOOMING_VINE.get(), UnseenWorldModItems.BERRIES_OF_BLOOMING_VINE.get());
//        this.dropOther(UnseenWorldModBlocks.ANCIENT_TRANSIENT_BLOCK_OPEN.get(), UnseenWorldModBlocks.ANCIENT_TRANSIENT_BLOCK_CLOSE.get());
//        this.dropWhenSilkTouch(UnseenWorldModBlocks.TANZANITE_BLOCK_BUDDING.get());
//        this.add(UnseenWorldModBlocks.TANZANITE_CLUSTER.get(), (block) -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(UnseenWorldModItems.TANZANITE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(block, LootItem.lootTableItem(UnseenWorldModItems.TANZANITE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
//        this.dropSelf(UnseenWorldModBlocks.TANZANITE_BLOCK.get());
//        this.dropSelf(UnseenWorldModBlocks.ANCIENT_TRANSIENT_BLOCK_CLOSE.get());
//        this.dropSelf(UnseenWorldModBlocks.BEACON_OF_WEAPONS.get());
//        this.dropSelf(UnseenWorldModBlocks.DRIPSTONE_OF_AMETHYST_OVERGROWTH.get());
//        this.dropSelf(UnseenWorldModBlocks.GOLDEN_CHEST.get());
//        this.dropSelf(UnseenWorldModBlocks.BLAZER_SUMMON_BLOCK.get());
//        this.dropSelf(UnseenWorldModBlocks.THE_WITHER_KNIGHT_BLOCK.get());
//        this.dropSelf(UnseenWorldModBlocks.UNDEAD_WARRIOR_OF_THE_CHIMERIC_DARKNESS.get());
//        this.dropSelf(UnseenWorldModBlocks.TOTEM_OF_GUDDY_BLAZE.get());
//        this.dropSelf(UnseenWorldModBlocks.BEACON_RUNE.get());
//        //leaves
//        this.add(UnseenWorldModBlocks.DARK_CRIMSON_LEAVES.get(), (block) -> this.createLeavesDrops(block, UnseenWorldModBlocks.DARK_CRIMSON_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
//        this.add(UnseenWorldModBlocks.AMETHYST_LEAVES.get(), (block) -> this.createLeavesDrops(block, UnseenWorldModBlocks.AMETHYST_TREE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
//        this.add(UnseenWorldModBlocks.GRIZZLY_LEAVES.get(), (block) -> this.createLeavesDrops(block, UnseenWorldModBlocks.GRIZZLY_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
//        this.add(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_LEAVES.get(), (block) -> this.createLeavesDrops(block, UnseenWorldModBlocks.GREENISH_BURLYWOOD_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
//        this.add(UnseenWorldModBlocks.TEALIVY_LEAVES.get(), (block) -> this.createLeavesDrops(block, UnseenWorldModBlocks.TEALIVY_TREE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
//        this.add(UnseenWorldModBlocks.TANZASHROOM_BLOCK.get(), (block) -> this.createSingleItemTableWithSilkTouch(block, UnseenWorldModBlocks.TANZASHROOM.get()));
        //ores
//        this.add(UnseenWorldModBlocks.DEEP_GEM_ORE.get(), (block) -> createFortureAppliedOreDrops(block, UnseenWorldModItems.DEEP_GEM.get(), 1, 7));
//        this.add(UnseenWorldModBlocks.VOID_INGOT_ORE.get(), (block) -> createFortureAppliedOreDrops(block, UnseenWorldModItems.BLUE_VOID.get(), 1, 4));
//        this.add(UnseenWorldModBlocks.UNSEEN_ORE.get(), (block) -> createFortureAppliedOreDrops(block, UnseenWorldModItems.RAW_UNSEENIUM.get(), 1, 4));
//        this.add(UnseenWorldModBlocks.RED_TITANIUM_ORE.get(), (block) -> createFortureAppliedOreDrops(block, UnseenWorldModItems.RAW_RED_TITANIUM.get(), 1, 3));
//        //ore blocks
//        this.dropSelf(UnseenWorldModBlocks.UNSEEN_BLOCK.get());
//        this.dropSelf(UnseenWorldModBlocks.VOID_INGOT_BLOCK.get());
//        this.dropSelf(UnseenWorldModBlocks.RED_TITANIUM_BLOCK.get());
//        this.dropSelf(UnseenWorldModBlocks.NATURERIUM_BLOCK.get());
//        this.dropSelf(UnseenWorldModBlocks.DEEP_GEM_BLOCK.get());
//        //sand-like
//        this.dropSelf(UnseenWorldModBlocks.CRYSTALLIZED_DARK_SAND.get());
//        this.dropSelf(UnseenWorldModBlocks.RED_OOZE.get());
//        //light blocks
//        this.dropSelf(UnseenWorldModBlocks.GRIZZLY_LIGHT_BLOCK.get());
//        this.dropSelf(UnseenWorldModBlocks.TANZASHROOM_LIGHT.get());
//        //flowers like
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_AZALEA.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_FLOWING_AZALEA.get());
//        this.dropSelf(UnseenWorldModBlocks.AMETHYST_GRASS.get());
//        this.dropSelf(UnseenWorldModBlocks.TEALIVY_PLUMERIA.get());
//        this.dropSelf(UnseenWorldModBlocks.NIGHTDEW_OF_CHIMERIC_DARKNESS.get());
//        this.dropSelf(UnseenWorldModBlocks.GLOWORCHID.get());
//        this.dropSelf(UnseenWorldModBlocks.DEEP_WATER_ANFELTSIA.get());
//        this.dropOther(UnseenWorldModBlocks.WILD_CRIMSERRY_SOUL_FLOWER.get(), UnseenWorldModItems.CRIMSERRY_SOUL_BERRY.get());
//        this.dropOther(UnseenWorldModBlocks.WILD_MISTERY_FLOWER_BERRIES.get(), UnseenWorldModItems.PURPLE_BERRIES.get());
//        this.dropOther(UnseenWorldModBlocks.CRIMSERRY_SOUL_CROP.get(), UnseenWorldModItems.CRIMSERRY_SOUL_BERRY.get());
//        this.dropOther(UnseenWorldModBlocks.MISTERY_CROP_FLOWER.get(), UnseenWorldModItems.PURPLE_BERRIES.get());
//        this.dropOther(UnseenWorldModBlocks.OUT_GROWT_APPLE_BUSH.get(), UnseenWorldModItems.OUTGROWTH_APPLE.get());
//        this.add(UnseenWorldModBlocks.TEALIVY_JADE_VINE_FLOWER.get(), BlockLootSubProvider::createDoublePlantShearsDrop);
//        //tanzashroom
//        this.dropSelf(UnseenWorldModBlocks.TANZASHROOM.get());
//        this.dropSelf(UnseenWorldModBlocks.TANZASHROOM_LIGHT.get());
//        this.dropSelf(UnseenWorldModBlocks.TANZASHROOM_STEM.get());
////        bricks
//        this.dropSelf(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS.get());
//        this.dropSelf(UnseenWorldModBlocks.COLD_DARK_BRICKS.get());
//        this.dropSelf(UnseenWorldModBlocks.DARKNESS_ANCIENT_BRICKS.get());
//        this.dropSelf(UnseenWorldModBlocks.RED_DEEPSLATE_BRICKS.get());
//        this.dropSelf(UnseenWorldModBlocks.REINFORCED_POLISHED_BLACKSTONE_BRICKS.get());
//        this.dropSelf(UnseenWorldModBlocks.REINFORCED_RED_ANCIENT_BRICKS.get());
//        this.dropSelf(UnseenWorldModBlocks.TANZANITE_BRICKS.get());
////        stones
//        this.dropSelf(UnseenWorldModBlocks.CHLORITE_SLATE_STONE.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CURRANTSLATE.get());
////        planks
//        this.dropSelf(UnseenWorldModBlocks.AMETHYST_PLANKS.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_PLANKS.get());
//        this.dropSelf(UnseenWorldModBlocks.GRIZZLY_PLANKS.get());
//        this.dropSelf(UnseenWorldModBlocks.TEALIVY_PLANKS.get());
//        this.dropSelf(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_PLANKS.get());
////        woods
//        this.dropSelf(UnseenWorldModBlocks.AMETHYST_WOOD.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_WOOD.get());
//        this.dropSelf(UnseenWorldModBlocks.GRIZZLY_WOOD.get());
//        this.dropSelf(UnseenWorldModBlocks.TEALIVY_WOOD.get());
//        this.dropSelf(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_BLOCK.get());
////        doors
//        this.add(UnseenWorldModBlocks.AMETHYST_DOOR.get(), this::createDoorTable);
//        this.add(UnseenWorldModBlocks.DARK_CRIMSON_DOOR.get(), this::createDoorTable);
//        this.add(UnseenWorldModBlocks.GRIZZLY_DOOR.get(), this::createDoorTable);
//        this.add(UnseenWorldModBlocks.TEALIVY_DOOR.get(), this::createDoorTable);
//        this.add(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_DOOR.get(), this::createDoorTable);
////        buttons
//        this.dropSelf(UnseenWorldModBlocks.AMETHYST_BUTTON.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_BUTTON.get());
//        this.dropSelf(UnseenWorldModBlocks.GRIZZLY_BUTTON.get());
//        this.dropSelf(UnseenWorldModBlocks.TEALIVY_BUTTON.get());
//        this.dropSelf(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_BUTTON.get());
////        slabs
//        this.add(UnseenWorldModBlocks.AMETHYST_SLAB.get(), this::createSlabItemTable);
//        this.add(UnseenWorldModBlocks.DARK_CRIMSON_SLAB.get(), this::createSlabItemTable);
//        this.add(UnseenWorldModBlocks.GRIZZLY_SLAB.get(), this::createSlabItemTable);
//        this.add(UnseenWorldModBlocks.TEALIVY_SLAB.get(), this::createSlabItemTable);
//        this.add(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_SLAB.get(), this::createSlabItemTable);
////        planks unlike slabs
//        this.add(UnseenWorldModBlocks.DARK_CURRANTSLATE_SLAB.get(), this::createSlabItemTable);
//        this.add(UnseenWorldModBlocks.TANZANITE_BRICKS_SLAB.get(), this::createSlabItemTable);
//        this.add(UnseenWorldModBlocks.REINFORCED_POLISHED_BLACKSTONE_BRICKS_SLAB.get(), this::createSlabItemTable);
//        this.add(UnseenWorldModBlocks.DARKNESS_ANCIENT_BRICKS_SLAB.get(), this::createSlabItemTable);
//        this.add(UnseenWorldModBlocks.RED_DEEPSLATE_BRICKS_SLAB.get(), this::createSlabItemTable);
//        this.add(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS_SLAB.get(), this::createSlabItemTable);
////        stairs
//        this.dropSelf(UnseenWorldModBlocks.AMETHYST_STAIRS.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_STAIRS.get());
//        this.dropSelf(UnseenWorldModBlocks.GRIZZLY_STAIRS.get());
//        this.dropSelf(UnseenWorldModBlocks.TEALIVY_STAIRS.get());
//        this.dropSelf(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_STAIRS.get());
////        planks unlike stairs
//        this.dropSelf(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS_STAIRS.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CURRANTSLATE_STAIRS.get());
//        this.dropSelf(UnseenWorldModBlocks.TANZANITE_BRICKS_STAIRS.get());
//        this.dropSelf(UnseenWorldModBlocks.DARKNESS_ANCIENT_BRICKS_STAIRS.get());
//        this.dropSelf(UnseenWorldModBlocks.RED_DEEPSLATE_BRICKS_STAIRS.get());
////        fences
//        this.dropSelf(UnseenWorldModBlocks.AMETHYST_FENCE.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_FENCE.get());
//        this.dropSelf(UnseenWorldModBlocks.GRIZZLY_FENCE.get());
//        this.dropSelf(UnseenWorldModBlocks.TEALIVY_FENCE.get());
//        this.dropSelf(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_FENCE.get());
////        fence gates
//        this.dropSelf(UnseenWorldModBlocks.AMETHYST_FENCE_GATE.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_FENCE_GATE.get());
//        this.dropSelf(UnseenWorldModBlocks.GRIZZLY_FENCE_GATE.get());
//        this.dropSelf(UnseenWorldModBlocks.TEALIVY_FENCE_GATE.get());
//        this.dropSelf(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_FENCE_GATE.get());
////        walls
//        this.dropSelf(UnseenWorldModBlocks.CHLORITE_SLATE_STONE_BRICKS_WALL.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CURRANTSLATE_WALL.get());
//        this.dropSelf(UnseenWorldModBlocks.DARKNESS_ANCIENT_BRICKS_WALL.get());
//        this.dropSelf(UnseenWorldModBlocks.TANZANITE_BRICKS_WALL.get());
//        this.dropSelf(UnseenWorldModBlocks.RED_DEEPSLATE_BRICKS_WALL.get());
////        pressure plates
//        this.dropSelf(UnseenWorldModBlocks.AMETHYST_PRESSURE_PLATE.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_PRESSURE_PLATE.get());
//        this.dropSelf(UnseenWorldModBlocks.GRIZZLY_PRESSURE_PLATE.get());
//        this.dropSelf(UnseenWorldModBlocks.TEALIVY_PRESSURE_PLATE.get());
//        this.dropSelf(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_PRESSURE_PLATE.get());
//        logs
//        this.dropSelf(UnseenWorldModBlocks.AMETHYST_LOG.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_LOG.get());
//        this.dropSelf(UnseenWorldModBlocks.GRIZZLY_LOG.get());
//        this.dropSelf(UnseenWorldModBlocks.TEALIVY_LOG.get());
//        this.dropSelf(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_LOG.get());
////        trapdoors
//        this.dropSelf(UnseenWorldModBlocks.AMETHYST_TRAPDOOR.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_TRAPDOOR.get());
//        this.dropSelf(UnseenWorldModBlocks.GRIZZLY_TRAPDOOR.get());
//        this.dropSelf(UnseenWorldModBlocks.TEALIVY_TRAPDOOR.get());
//        this.dropSelf(UnseenWorldModBlocks.GREENISH_BURLY_WOOD_TRAPDOOR.get());
////        saplings
//        this.dropSelf(UnseenWorldModBlocks.AMETHYST_TREE_SAPLING.get());
//        this.dropSelf(UnseenWorldModBlocks.DARK_CRIMSON_SAPLING.get());
//        this.dropSelf(UnseenWorldModBlocks.GRIZZLY_SAPLING.get());
//        this.dropSelf(UnseenWorldModBlocks.TEALIVY_TREE_SAPLING.get());
//        this.dropSelf(UnseenWorldModBlocks.GREENISH_BURLYWOOD_SAPLING.get());
//        grass blocks
//        this.otherWhenSilkTouch(UnseenWorldModBlocks.DARK_GRASS_BLOCK.get(), Blocks.GRASS_BLOCK);
//        this.otherWhenSilkTouch(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get(), Blocks.GRASS_BLOCK);
//        this.otherWhenSilkTouch(UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get(), UnseenWorldModBlocks.DARK_CURRANTSLATE.get());
//        this.otherWhenSilkTouch(UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get(), UnseenWorldModBlocks.DARK_CURRANTSLATE.get());
    }

    private LootTable.Builder createFortureAppliedOreDrops(Block block, Item item, int min, int max) {
        return createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return UnseenWorldBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block.getDescriptionId().contains("misteryflower_sapling"))::iterator;
    }
}
//Block[] allBlocks = UnseenWorldModBlocks.BLOCKS.getEntries().stream()
//                .map(RegistryObject::get)
//                .toArray(Block[]::new);
//        Arrays.stream(allBlocks).sequential()
//                .filter(block -> block.getDescriptionId().contains("stairs"))
//                .forEach(this::dropSelf);