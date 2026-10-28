package net.sashakyotoz.common.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.lcc.sollib.api.common.registry.data.block.BlockModel;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.particles.custom.effects.LeafParticleEffect;
import net.sashakyotoz.common.blocks.custom.*;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;
import net.sashakyotoz.common.blocks.custom.plants.*;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.features.trees.generators.*;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static void register() {
        UnseenWorld.log().debug("Registering Blocks for modid : " + UnseenWorld.MOD_ID);
    }

    private static final TagKey<Block>[] PICKAXE = tags(BlockTags.MINEABLE_WITH_PICKAXE);
    private static final TagKey<Block>[] STONE_TOOL = tags(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_STONE_TOOL);
    private static final TagKey<Block>[] IRON_TOOL = tags(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL);
    private static final TagKey<Block>[] DIAMOND_TOOL = tags(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL);
    private static final TagKey<Block>[] DUNGEON = plus(STONE_TOOL, BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE);

    public static final BlockBehaviour.StatePredicate IS_NEXT_TO_GRIPTONITE_CLUSTER =
            (state, level, pos) -> {
                BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
                for (Direction direction : Direction.values()) {
                    mutablePos.setWithOffset(pos, direction);
                    if (level.getBlockState(mutablePos).is(ModBlocks.GRIPTONITE_CLUSTER)) {
                        return true;
                    }
                }
                return false;
            };

    public static final WoodType GRIZZLY = new WoodTypeBuilder().register(UnseenWorld.MOD.makeID("grizzly"), BlockSetType.DARK_OAK);
    public static final WoodType AMETHYST = new WoodTypeBuilder().register(UnseenWorld.MOD.makeID("amethyst"), BlockSetType.BIRCH);
    public static final WoodType BURLYWOOD = new WoodTypeBuilder().register(UnseenWorld.MOD.makeID("burlywood"), BlockSetType.MANGROVE);
    public static final WoodType TEALIVE = new WoodTypeBuilder().register(UnseenWorld.MOD.makeID("tealive"), BlockSetType.JUNGLE);
    public static final WoodType CRIMSONVEIL = new WoodTypeBuilder().register(UnseenWorld.MOD.makeID("crimsonveil"), BlockSetType.CRIMSON);

    public static final Block DARK_FROST_BRICKS = block("dark_frost_bricks", () -> new Block(copy(Blocks.POLISHED_BLACKSTONE_BRICKS)))
            .withTags(DUNGEON).dropsSelf().withModel(BlockModel.CUBE).get();
    public static final Block ORDEAL_SPAWNER = block("ordeal_spawner", () -> new OrdealSpawnerBlock(copy(Blocks.SPAWNER)
            .strength(50.0F, 1200.0F).noOcclusion().noLootTable().pushReaction(PushReaction.BLOCK)
            .lightLevel(state -> state.getValue(OrdealSpawnerBlock.STATE).luminance)))
            .withTags(DUNGEON).cutout().get();
    public static final Block EFFIGY = block("effigy", () -> new EffigyBlock(effigySettings()
            .lightLevel(state -> state.getValue(EffigyBlock.PHASE).luminance)))
            .withTags(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).get();
    public static final Block EFFIGY_PART = holder("effigy_part", () -> new EffigyPartBlock(effigySettings().noTerrainParticles()))
            .withTags(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).get();
    public static final Block GRIPTONITE_LANTERN = block("griptonite_lantern", () -> new ModLanternBlock(props().forceSolidOn()
            .sound(SoundType.LANTERN).randomTicks().requiresCorrectToolForDrops().strength(3.5F).lightLevel(state -> 14)))
            .withTags(STONE_TOOL).cutout().dropsSelf().get();

    private static final BlockHolder DARK_CURRANTSLATE_SET = stoneSet("dark_currantslate", () -> new Block(darkCurrantslate()), DUNGEON);
    public static final Block DARK_CURRANTSLATE = DARK_CURRANTSLATE_SET.get();
    public static final Block DARK_CURRANTSLATE_STAIRS = DARK_CURRANTSLATE_SET.getStairs().get();
    public static final Block DARK_CURRANTSLATE_SLAB = DARK_CURRANTSLATE_SET.getSlab().get();
    public static final Block DARK_CURRANTSLATE_WALL = DARK_CURRANTSLATE_SET.getWall().get();
    public static final Block GILDED_CURRANTSLATE = cubeStone("gilded_currantslate", () -> new Block(darkCurrantslate()), plus(DUNGEON, BlockTags.GUARDED_BY_PIGLINS));
    private static final BlockHolder DARK_CURRANTSLATE_BRICKS_SET = stoneSet("dark_currantslate_bricks", () -> new Block(darkCurrantslate()), DUNGEON);
    public static final Block DARK_CURRANTSLATE_BRICKS = DARK_CURRANTSLATE_BRICKS_SET.get();
    public static final Block DARK_CURRANTSLATE_BRICKS_STAIRS = DARK_CURRANTSLATE_BRICKS_SET.getStairs().get();
    public static final Block DARK_CURRANTSLATE_BRICKS_SLAB = DARK_CURRANTSLATE_BRICKS_SET.getSlab().get();
    public static final Block DARK_CURRANTSLATE_BRICKS_WALL = DARK_CURRANTSLATE_BRICKS_SET.getWall().get();
    public static final Block DARK_CURRANTSLATE_CHEST = chest("dark_currantslate_chest", 5, 5, SoundType.POLISHED_DEEPSLATE, ModChestBlock.ChestTypes.DARK_CURRANTSLATE);

    private static final BlockHolder GLACIEMITE_SET = stoneSet("glaciemite", () -> new Block(glaciemite()), DUNGEON);
    public static final Block GLACIEMITE = GLACIEMITE_SET.get();
    public static final Block GLACIEMITE_STAIRS = GLACIEMITE_SET.getStairs().get();
    public static final Block GLACIEMITE_SLAB = GLACIEMITE_SET.getSlab().get();
    public static final Block GLACIEMITE_WALL = GLACIEMITE_SET.getWall().get();
    private static final BlockHolder GLACIEMITE_BRICKS_SET = stoneSet("glaciemite_bricks", () -> new Block(glaciemite()), DUNGEON);
    public static final Block GLACIEMITE_BRICKS = GLACIEMITE_BRICKS_SET.get();
    public static final Block GLACIEMITE_BRICKS_STAIRS = GLACIEMITE_BRICKS_SET.getStairs().get();
    public static final Block GLACIEMITE_BRICKS_SLAB = GLACIEMITE_BRICKS_SET.getSlab().get();
    public static final Block GLACIEMITE_BRICKS_WALL = GLACIEMITE_BRICKS_SET.getWall().get();
    public static final Block CRACKED_GLACIEMITE_BRICKS = cubeStone("cracked_glaciemite_bricks", () -> new Block(glaciemite().destroyTime(2.0F)), DUNGEON);
    public static final Block CHISELED_GLACIEMITE_BRICKS = cubeStone("chiseled_glaciemite_bricks", () -> new Block(glaciemite()), STONE_TOOL);
    public static final Block ENGRAVED_GLACIEMITE_BRICKS = block("engraved_glaciemite_bricks", () -> new EngravedBricksBlock(glaciemite()))
            .withTags(STONE_TOOL).dropsSelf().get();
    public static final Block GLACIEMITE_CHEST = chest("glaciemite_chest", 4, 5, SoundType.POLISHED_DEEPSLATE, ModChestBlock.ChestTypes.GLACIEMITE);
    public static final Block GLACIEMITE_BULB = block("glaciemite_bulb", () -> new BulbLikeBlock(glaciemite().lightLevel(Blocks.litBlockEmission(14))))
            .withTags(DUNGEON).dropsSelf().get();

    private static final BlockHolder CORONA_STONE_SET = stoneSet("corona_stone", () -> new Block(coronaStone()), DUNGEON);
    public static final Block CORONA_STONE = CORONA_STONE_SET.get();
    public static final Block CORONA_STONE_STAIRS = CORONA_STONE_SET.getStairs().get();
    public static final Block CORONA_STONE_SLAB = CORONA_STONE_SET.getSlab().get();
    public static final Block CORONA_STONE_WALL = CORONA_STONE_SET.getWall().get();
    private static final BlockHolder CORONA_STONE_BRICKS_SET = stoneSet("corona_stone_bricks", () -> new Block(coronaStone()), DUNGEON);
    public static final Block CORONA_STONE_BRICKS = CORONA_STONE_BRICKS_SET.get();
    public static final Block CORONA_STONE_BRICKS_STAIRS = CORONA_STONE_BRICKS_SET.getStairs().get();
    public static final Block CORONA_STONE_BRICKS_SLAB = CORONA_STONE_BRICKS_SET.getSlab().get();
    public static final Block CORONA_STONE_BRICKS_WALL = CORONA_STONE_BRICKS_SET.getWall().get();
    public static final Block CRACKED_CORONA_STONE_BRICKS = cubeStone("cracked_corona_stone_bricks", () -> new Block(coronaStone().destroyTime(0.5F)), DUNGEON);
    public static final Block ENGRAVED_CORONA_STONE_BRICKS = block("engraved_corona_stone_bricks", () -> new EngravedBricksBlock(coronaStone()))
            .withTags(STONE_TOOL).dropsSelf().get();
    public static final Block CORONA_STONE_CHEST = chest("corona_stone_chest", 2, 2, SoundType.BONE_BLOCK, ModChestBlock.ChestTypes.CORONA_STONE);

    public static final Block TANZANITE_BLOCK = cubeStone("tanzanite_block", () -> new Block(tanzanite()), STONE_TOOL);
    private static final BlockHolder POLISHED_TANZANITE_SET = polishedSet("polished_tanzanite", () -> new Block(tanzanite()), STONE_TOOL);
    public static final Block POLISHED_TANZANITE = POLISHED_TANZANITE_SET.get();
    public static final Block POLISHED_TANZANITE_STAIRS = POLISHED_TANZANITE_SET.getStairs().get();
    public static final Block POLISHED_TANZANITE_SLAB = POLISHED_TANZANITE_SET.getSlab().get();

    public static final Block ABYSSAL_ORE_IN_DARK_CURRANTSLATE = ore("abyssal_ore_in_dark_currantslate", 5, 4, 1, 5, DIAMOND_TOOL);
    public static final Block ABYSSAL_ORE_IN_GLACIEMITE = ore("abyssal_ore_in_glaciemite", 5, 4, 1, 5, DIAMOND_TOOL);
    public static final Block ABYSSAL_ORE_IN_CORONA_STONE = ore("abyssal_ore_in_corona_stone", 4, 4, 1, 5, DIAMOND_TOOL);
    public static final Block ABYSSAL_INGOT_BLOCK = storageBlock("abyssal_ingot_block", 5);
    public static final Block RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE = ore("red_titanium_ore_in_dark_currantslate", 10, 3, 1, 6, DIAMOND_TOOL);
    public static final Block RED_TITANIUM_ORE_IN_GLACIEMITE = ore("red_titanium_ore_in_glaciemite", 10, 3, 1, 6, DIAMOND_TOOL);
    public static final Block RED_TITANIUM_ORE_IN_CORONA_STONE = ore("red_titanium_ore_in_corona_stone", 8, 3, 1, 6, DIAMOND_TOOL);
    public static final Block RED_TITANIUM_INGOT_BLOCK = storageBlock("red_titanium_ingot_block", 6);
    public static final Block UNSEENIUM_ORE_IN_DARK_CURRANTSLATE = ore("unseenium_ore_in_dark_currantslate", 4, 5, 1, 4, DIAMOND_TOOL);
    public static final Block UNSEENIUM_ORE_IN_GLACIEMITE = ore("unseenium_ore_in_glaciemite", 4, 5, 1, 4, DIAMOND_TOOL);
    public static final Block UNSEENIUM_ORE_IN_CORONA_STONE = ore("unseenium_ore_in_corona_stone", 3, 5, 1, 4, DIAMOND_TOOL);
    public static final Block UNSEENIUM_INGOT_BLOCK = storageBlock("unseenium_ingot_block", 4);
    public static final Block IRON_ORE_IN_DARK_CURRANTSLATE = ore("iron_ore_in_dark_currantslate", 3, 0, 1, 3, IRON_TOOL);
    public static final Block IRON_ORE_IN_GLACIEMITE = ore("iron_ore_in_glaciemite", 3, 0, 1, 3, IRON_TOOL);
    public static final Block IRON_ORE_IN_CORONA_STONE = ore("iron_ore_in_corona_stone", 3, 0, 1, 3, IRON_TOOL);
    public static final Block ANCIENT_DEBRIS_IN_DARK_CURRANTSLATE = ore("ancient_debris_in_dark_currantslate", 15, 0, 1, 2, DIAMOND_TOOL);
    public static final Block ANCIENT_DEBRIS_IN_GLACIEMITE = ore("ancient_debris_in_glaciemite", 15, 0, 1, 2, DIAMOND_TOOL);
    public static final Block ANCIENT_DEBRIS_IN_CORONA_STONE = ore("ancient_debris_in_corona_stone", 15, 0, 1, 2, DIAMOND_TOOL);

    public static final Block NIGHTDARK_GRASS_BLOCK = grass("nightdark_grass_block", MapColor.COLOR_BLACK, SoundType.ROOTED_DIRT);
    public static final Block PENUMBRAL_GRASS_BLOCK = grass("penumbral_grass_block", MapColor.COLOR_BLACK, SoundType.ROOTED_DIRT);
    public static final Block TEALIVY_GRASS_BLOCK = grass("tealivy_grass_block", MapColor.COLOR_CYAN, SoundType.ROOTED_DIRT);
    public static final Block AMETHYST_GRASS_BLOCK = grass("amethyst_grass_block", MapColor.COLOR_PURPLE, SoundType.AMETHYST);
    public static final Block GRIMWOOD_GRASS_BLOCK = grass("grimwood_grass_block", MapColor.COLOR_RED, SoundType.GILDED_BLACKSTONE);
    public static final Block NIGHTDARK_DIRT = dirt("nightdark_dirt", MapColor.COLOR_GRAY);
    public static final Block TEALIVY_DIRT = dirt("tealivy_dirt", MapColor.COLOR_CYAN);
    public static final Block ASHEN_OOZE = block("ashen_ooze", () -> new Block(props().sound(SoundType.ROOTED_DIRT).destroyTime(1).mapColor(MapColor.TERRACOTTA_ORANGE)))
            .withTags(BlockTags.MINEABLE_WITH_SHOVEL).dropsSelf().withModel(BlockModel.CUBE).get();
    public static final Block SUSPICIOUS_ASHEN_OOZE = block("suspicious_ashen_ooze", () -> new SusBlock(ModBlocks.ASHEN_OOZE,
            props().destroyTime(0.25F).mapColor(MapColor.TERRACOTTA_ORANGE), SoundEvents.BRUSH_GRAVEL, SoundEvents.BRUSH_SAND_COMPLETED))
            .withTags(BlockTags.MINEABLE_WITH_SHOVEL).get();
    public static final Block GLIMMERGRAIN_SAND = block("glimmergrain_sand", () -> new ColoredFallingBlock(new ColorRGBA(0xFF000000), props().sound(SoundType.SOUL_SAND).destroyTime(1).mapColor(MapColor.COLOR_GRAY)))
            .withTags(BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.SAND, BlockTags.SMELTS_TO_GLASS).dropsSelf().withModel(BlockModel.CUBE).get();
    public static final Block SUSPICIOUS_GLIMMERGRAIN_SAND = block("suspicious_glimmergrain_sand", () -> new SusBlock(ModBlocks.GLIMMERGRAIN_SAND,
            props().sound(SoundType.SUSPICIOUS_SAND).destroyTime(0.25F).mapColor(MapColor.SAND), SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_GRAVEL_COMPLETED))
            .withTags(BlockTags.MINEABLE_WITH_SHOVEL).get();
    public static final Block GLIMMERGRAIN_SANDSTONE = block("glimmergrain_sandstone", () -> new Block(props().sound(SoundType.BONE_BLOCK).destroyTime(1.8F).mapColor(MapColor.COLOR_GRAY)))
            .withTags(PICKAXE).dropsSelf().get();

    public static final Block STRIPPED_AMETHYST_LOG = strippedLog("stripped_amethyst_log", ModBlocks::amethystWood, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Items.AMETHYST_LOGS);
    public static final Block AMETHYST_LOG = log("amethyst_log", ModBlocks::amethystWood, () -> ModBlocks.STRIPPED_AMETHYST_LOG, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Items.AMETHYST_LOGS);
    public static final Block STRIPPED_AMETHYST_WOOD = strippedWood("stripped_amethyst_wood", ModBlocks::amethystWood, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Items.AMETHYST_LOGS);
    public static final Block AMETHYST_WOOD = wood("amethyst_wood", ModBlocks::amethystWood, () -> ModBlocks.STRIPPED_AMETHYST_WOOD, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Items.AMETHYST_LOGS);
    public static final Block AMETHYST_LEAVES = block("amethyst_leaves", () -> new LeafDroppingLeaveBlock(amethystLeaves(), new LeafParticleEffect(0.6F, 0.2F, 0.7F)))
            .withTags(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .withFlammability(5, 30).cutout().get();
    public static final Block HANGING_AMETHYST_LEAVES = block("hanging_amethyst_leaves", () -> new HangingFruitBlock(amethystLeaves()
            .lightLevel(lightLevelFromBlockStateR(4, HangingFruitBlock.HAS_FRUIT)).noCollission().instabreak(),
            () -> ModItems.CRYSTIE_APPLE, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON, Block.box(0, 0, 0, 16, 16, 16)))
            .withTags(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .withItem(item -> item.withModel(ModelTemplates.FLAT_ITEM)).withFlammability(5, 30).cutout().get();
    private static final BlockHolder AMETHYST_PLANKS_SET = planks("amethyst", ModBlocks::amethystWood, AMETHYST, ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_PLANKS = AMETHYST_PLANKS_SET.get();
    public static final Block AMETHYST_STAIRS = AMETHYST_PLANKS_SET.getStairs().get();
    public static final Block AMETHYST_SLAB = AMETHYST_PLANKS_SET.getSlab().get();
    public static final Block AMETHYST_PRESSURE_PLATE = AMETHYST_PLANKS_SET.getPressurePlate().get();
    public static final Block AMETHYST_BUTTON = AMETHYST_PLANKS_SET.getButton().get();
    public static final Block AMETHYST_FENCE = AMETHYST_PLANKS_SET.getFence().get();
    public static final Block AMETHYST_FENCE_GATE = AMETHYST_PLANKS_SET.getFenceGate().get();
    public static final Block AMETHYST_DOOR = door("amethyst", AMETHYST, ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_TRAPDOOR = trapdoor("amethyst", AMETHYST, ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_SAPLING = sapling("amethyst", ModTreeGrowers.AMETHYST, ModTags.Blocks.AMETHYST_BLOCKS);

    public static final Block STRIPPED_GRIZZLY_LOG = strippedLog("stripped_grizzly_log", ModBlocks::grizzlyWood, ModTags.Blocks.GRIZZLY_BLOCKS, ModTags.Items.GRIZZLY_LOGS);
    public static final Block GRIZZLY_LOG = log("grizzly_log", ModBlocks::grizzlyWood, () -> ModBlocks.STRIPPED_GRIZZLY_LOG, ModTags.Blocks.GRIZZLY_BLOCKS, ModTags.Items.GRIZZLY_LOGS);
    public static final Block STRIPPED_GRIZZLY_WOOD = strippedWood("stripped_grizzly_wood", ModBlocks::grizzlyWood, ModTags.Blocks.GRIZZLY_BLOCKS, ModTags.Items.GRIZZLY_LOGS);
    public static final Block GRIZZLY_WOOD = wood("grizzly_wood", ModBlocks::grizzlyWood, () -> ModBlocks.STRIPPED_GRIZZLY_WOOD, ModTags.Blocks.GRIZZLY_BLOCKS, ModTags.Items.GRIZZLY_LOGS);
    public static final Block GRIZZLY_LEAVES = block("grizzly_leaves", () -> new LeavesBlock(props().strength(0.2F).mapColor(MapColor.COLOR_GRAY)
            .sound(SoundType.CHERRY_LEAVES).noOcclusion()))
            .withTags(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, ModTags.Blocks.GRIZZLY_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .withFlammability(5, 30).withModel(BlockModel.CUBE).cutout().get();
    private static final BlockHolder GRIZZLY_PLANKS_SET = planks("grizzly", ModBlocks::grizzlyWood, GRIZZLY, ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_PLANKS = GRIZZLY_PLANKS_SET.get();
    public static final Block GRIZZLY_STAIRS = GRIZZLY_PLANKS_SET.getStairs().get();
    public static final Block GRIZZLY_SLAB = GRIZZLY_PLANKS_SET.getSlab().get();
    public static final Block GRIZZLY_PRESSURE_PLATE = GRIZZLY_PLANKS_SET.getPressurePlate().get();
    public static final Block GRIZZLY_BUTTON = GRIZZLY_PLANKS_SET.getButton().get();
    public static final Block GRIZZLY_FENCE = GRIZZLY_PLANKS_SET.getFence().get();
    public static final Block GRIZZLY_FENCE_GATE = GRIZZLY_PLANKS_SET.getFenceGate().get();
    public static final Block GRIZZLY_DOOR = door("grizzly", GRIZZLY, ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_TRAPDOOR = trapdoor("grizzly", GRIZZLY, ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_SAPLING = sapling("grizzly", ModTreeGrowers.GRIZZLY, ModTags.Blocks.GRIZZLY_BLOCKS);

    public static final Block STRIPPED_TEALIVE_LOG = strippedLog("stripped_tealive_log", ModBlocks::tealiveWood, ModTags.Blocks.TEALIVE_BLOCKS, ModTags.Items.TEALIVE_LOGS);
    public static final Block TEALIVE_LOG = log("tealive_log", ModBlocks::tealiveWood, () -> ModBlocks.STRIPPED_TEALIVE_LOG, ModTags.Blocks.TEALIVE_BLOCKS, ModTags.Items.TEALIVE_LOGS);
    public static final Block STRIPPED_TEALIVE_WOOD = strippedWood("stripped_tealive_wood", ModBlocks::tealiveWood, ModTags.Blocks.TEALIVE_BLOCKS, ModTags.Items.TEALIVE_LOGS);
    public static final Block TEALIVE_WOOD = wood("tealive_wood", ModBlocks::tealiveWood, () -> ModBlocks.STRIPPED_TEALIVE_WOOD, ModTags.Blocks.TEALIVE_BLOCKS, ModTags.Items.TEALIVE_LOGS);
    public static final Block TEALIVE_LEAVES = block("tealive_leaves", () -> new LeavesBlock(props().mapColor(MapColor.COLOR_CYAN)
            .sound(SoundType.AZALEA_LEAVES).noOcclusion().strength(0.2F).randomTicks()))
            .withTags(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, ModTags.Blocks.TEALIVE_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .withFlammability(5, 30).withModel(BlockModel.CUBE).cutout().get();
    private static final BlockHolder TEALIVE_PLANKS_SET = planks("tealive", ModBlocks::tealiveWood, TEALIVE, ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_PLANKS = TEALIVE_PLANKS_SET.get();
    public static final Block TEALIVE_STAIRS = TEALIVE_PLANKS_SET.getStairs().get();
    public static final Block TEALIVE_SLAB = TEALIVE_PLANKS_SET.getSlab().get();
    public static final Block TEALIVE_PRESSURE_PLATE = TEALIVE_PLANKS_SET.getPressurePlate().get();
    public static final Block TEALIVE_BUTTON = TEALIVE_PLANKS_SET.getButton().get();
    public static final Block TEALIVE_FENCE = TEALIVE_PLANKS_SET.getFence().get();
    public static final Block TEALIVE_FENCE_GATE = TEALIVE_PLANKS_SET.getFenceGate().get();
    public static final Block TEALIVE_DOOR = door("tealive", TEALIVE, ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_TRAPDOOR = trapdoor("tealive", TEALIVE, ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_SAPLING = sapling("tealive", ModTreeGrowers.TEALIVE, ModTags.Blocks.TEALIVE_BLOCKS);

    public static final Block STRIPPED_BURLYWOOD_LOG = strippedLog("stripped_burlywood_log", ModBlocks::burlywood, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Items.BURLYWOOD_LOGS);
    public static final Block BURLYWOOD_LOG = log("burlywood_log", ModBlocks::burlywood, () -> ModBlocks.STRIPPED_BURLYWOOD_LOG, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Items.BURLYWOOD_LOGS);
    public static final Block STRIPPED_BURLYWOOD_WOOD = strippedWood("stripped_burlywood_wood", ModBlocks::burlywood, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Items.BURLYWOOD_LOGS);
    public static final Block BURLYWOOD_WOOD = wood("burlywood_wood", ModBlocks::burlywood, () -> ModBlocks.STRIPPED_BURLYWOOD_WOOD, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Items.BURLYWOOD_LOGS);
    public static final Block BURLYWOOD_HEART = block("burlywood_heart", () -> new BurlywoodHeartBlock(burlywood().randomTicks().lightLevel(state -> 12)
            .emissiveRendering((state, level, pos) -> !state.getValue(BurlywoodHeartBlock.PERSISTENT)
                    && (level.getBlockState(pos.north()).isAir()
                    || level.getBlockState(pos.south()).isAir()
                    || level.getBlockState(pos.west()).isAir()
                    || level.getBlockState(pos.east()).isAir()))))
            .withTags(BlockTags.LOGS_THAT_BURN, BlockTags.MINEABLE_WITH_AXE, ModTags.Blocks.BURLYWOOD_BLOCKS)
            .withItem(item -> item.withTags(ItemTags.LOGS_THAT_BURN, ModTags.Items.BURLYWOOD_LOGS).withFuel(600))
            .withFlammability(20, 1).withModel(BlockModel.CUBE).get();
    public static final Block BURLYWOOD_LEAVES = block("burlywood_leaves", () -> new LeafDroppingLeaveBlock(burlywoodLeaves(), new LeafParticleEffect(0.5F, 0.5F, 0.1F)))
            .withTags(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Blocks.HANGING_BURLYWOOD_LEAVES_GROWABLE_ON)
            .withFlammability(5, 30).cutout().get();
    public static final Block HANGING_BURLYWOOD_LEAVES = block("hanging_burlywood_leaves", () -> new HangingFruitBlock(burlywoodLeaves().noCollission()
            .lightLevel(lightLevelFromBlockStateR(8, HangingFruitBlock.HAS_FRUIT)).instabreak(),
            () -> ModItems.GLOW_APPLE, ModTags.Blocks.HANGING_BURLYWOOD_LEAVES_GROWABLE_ON, Block.box(4, 0, 4, 12, 16, 12)))
            .withTags(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Blocks.HANGING_BURLYWOOD_LEAVES_GROWABLE_ON)
            .withFlammability(5, 30).cutout().get();
    public static final Block GLOWROOT_VINE = holder("glowroot_vine", () -> new FruitingVinesHeadBlock(vineSettings(MapColor.TERRACOTTA_GREEN, SoundType.CAVE_VINES),
            () -> ModBlocks.GLOWROOT_VINE_PLANT, () -> ModItems.GLOWROOT_BULB, ModTags.Blocks.GLOWROOT_VINES_GROWABLE_ON))
            .withTags(BlockTags.CAVE_VINES, BlockTags.MINEABLE_WITH_HOE, ModTags.Blocks.GLOWROOT_VINES_GROWABLE_ON)
            .withFlammability(5, 30).cutout().get();
    public static final Block GLOWROOT_VINE_PLANT = holder("glowroot_vine_plant", () -> new FruitingVinesBodyBlock(vineSettings(MapColor.TERRACOTTA_GREEN, SoundType.CAVE_VINES),
            () -> ModBlocks.GLOWROOT_VINE, () -> ModItems.GLOWROOT_BULB, ModTags.Blocks.GLOWROOT_VINES_GROWABLE_ON))
            .withTags(BlockTags.CAVE_VINES, BlockTags.MINEABLE_WITH_HOE, ModTags.Blocks.GLOWROOT_VINES_GROWABLE_ON)
            .withFlammability(5, 30).cutout().get();
    private static final BlockHolder BURLYWOOD_PLANKS_SET = planks("burlywood", ModBlocks::burlywood, BURLYWOOD, ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_PLANKS = BURLYWOOD_PLANKS_SET.get();
    public static final Block BURLYWOOD_STAIRS = BURLYWOOD_PLANKS_SET.getStairs().get();
    public static final Block BURLYWOOD_SLAB = BURLYWOOD_PLANKS_SET.getSlab().get();
    public static final Block BURLYWOOD_PRESSURE_PLATE = BURLYWOOD_PLANKS_SET.getPressurePlate().get();
    public static final Block BURLYWOOD_BUTTON = BURLYWOOD_PLANKS_SET.getButton().get();
    public static final Block BURLYWOOD_FENCE = BURLYWOOD_PLANKS_SET.getFence().get();
    public static final Block BURLYWOOD_FENCE_GATE = BURLYWOOD_PLANKS_SET.getFenceGate().get();
    public static final Block BURLYWOOD_DOOR = door("burlywood", BURLYWOOD, ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_TRAPDOOR = trapdoor("burlywood", BURLYWOOD, ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_SAPLING = sapling("burlywood", ModTreeGrowers.BURLYWOOD, ModTags.Blocks.BURLYWOOD_BLOCKS);

    public static final Block STRIPPED_CRIMSONVEIL_LOG = strippedLog("stripped_crimsonveil_log", ModBlocks::crimsonveil, ModTags.Blocks.CRIMSONVEIL_BLOCKS, ModTags.Items.CRIMSONVEIL_LOGS);
    public static final Block CRIMSONVEIL_LOG = log("crimsonveil_log", ModBlocks::crimsonveil, () -> ModBlocks.STRIPPED_CRIMSONVEIL_LOG, ModTags.Blocks.CRIMSONVEIL_BLOCKS, ModTags.Items.CRIMSONVEIL_LOGS);
    public static final Block STRIPPED_CRIMSONVEIL_WOOD = strippedWood("stripped_crimsonveil_wood", ModBlocks::crimsonveil, ModTags.Blocks.CRIMSONVEIL_BLOCKS, ModTags.Items.CRIMSONVEIL_LOGS);
    public static final Block CRIMSONVEIL_WOOD = wood("crimsonveil_wood", ModBlocks::crimsonveil, () -> ModBlocks.STRIPPED_CRIMSONVEIL_WOOD, ModTags.Blocks.CRIMSONVEIL_BLOCKS, ModTags.Items.CRIMSONVEIL_LOGS);
    public static final Block CRIMSONVEIL_LEAVES = block("crimsonveil_leaves", () -> new LeavesBlock(props().strength(0.2F).lightLevel(state -> 2)
            .mapColor(MapColor.TERRACOTTA_ORANGE).sound(SoundType.CHERRY_LEAVES).noOcclusion().strength(0.2F).randomTicks()))
            .withTags(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, ModTags.Blocks.CRIMSONVEIL_BLOCKS, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON)
            .withFlammability(5, 30).withModel(BlockModel.CUBE).cutout().get();
    public static final Block CRIMSONVEIL_VINE = holder("crimsonveil_vine", () -> new FruitingVinesHeadBlock(vineSettings(MapColor.CRIMSON_HYPHAE, SoundType.FLOWERING_AZALEA),
            () -> ModBlocks.CRIMSONVEIL_VINE_PLANT, () -> ModItems.WARPEDVEIL_VINE_FRUIT, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON))
            .withTags(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON)
            .withFlammability(5, 30).cutout().get();
    public static final Block CRIMSONVEIL_VINE_PLANT = holder("crimsonveil_vine_plant", () -> new FruitingVinesBodyBlock(vineSettings(MapColor.CRIMSON_HYPHAE, SoundType.FLOWERING_AZALEA),
            () -> ModBlocks.CRIMSONVEIL_VINE, () -> ModItems.WARPEDVEIL_VINE_FRUIT, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON))
            .withTags(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON)
            .withFlammability(5, 30).cutout().get();
    private static final BlockHolder CRIMSONVEIL_PLANKS_SET = planks("crimsonveil", ModBlocks::crimsonveil, CRIMSONVEIL, ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_PLANKS = CRIMSONVEIL_PLANKS_SET.get();
    public static final Block CRIMSONVEIL_STAIRS = CRIMSONVEIL_PLANKS_SET.getStairs().get();
    public static final Block CRIMSONVEIL_SLAB = CRIMSONVEIL_PLANKS_SET.getSlab().get();
    public static final Block CRIMSONVEIL_PRESSURE_PLATE = CRIMSONVEIL_PLANKS_SET.getPressurePlate().get();
    public static final Block CRIMSONVEIL_BUTTON = CRIMSONVEIL_PLANKS_SET.getButton().get();
    public static final Block CRIMSONVEIL_FENCE = CRIMSONVEIL_PLANKS_SET.getFence().get();
    public static final Block CRIMSONVEIL_FENCE_GATE = CRIMSONVEIL_PLANKS_SET.getFenceGate().get();
    public static final Block CRIMSONVEIL_DOOR = door("crimsonveil", CRIMSONVEIL, ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_TRAPDOOR = trapdoor("crimsonveil", CRIMSONVEIL, ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_SAPLING = sapling("crimsonveil", ModTreeGrowers.CRIMSONVEIL, ModTags.Blocks.CRIMSONVEIL_BLOCKS);

    public static final Block BEARFRUIT_BRAMBLE = block("bearfruit_bramble_stem", () -> new StandingFruitBlock(copy(Blocks.OAK_SAPLING)
            .mapColor(MapColor.COLOR_PURPLE).instabreak().lightLevel(StandingFruitBlock.STATE_TO_LUMINANCE),
            () -> ModItems.BEARFRUIT_BRAMBLE, ModTags.Blocks.BEARFRUIT_BRAMBLE_GROWABLE_ON, Block.box(6, 0, 6, 10, 16, 10)))
            .withTags(ModTags.Blocks.BEARFRUIT_BRAMBLE_GROWABLE_ON, BlockTags.CLIMBABLE)
            .withItem(item -> item.withTags(ItemTags.FLOWERS).withModel(ModelTemplates.FLAT_ITEM)).cutout().get();
    public static final Block RIME_ICE = block("rime_ice", () -> new Block(copy(Blocks.BLUE_ICE).noOcclusion()))
            .withTags(PICKAXE).withModel(BlockModel.CUBE).get();
    public static final Block HOARFROST = block("hoarfrost", () -> new HoarfrostBlock(props().mapColor(MapColor.SNOW).replaceable().strength(0.1F)
            .speedFactor(0.6F).sound(SoundType.POWDER_SNOW).randomTicks().noOcclusion().pushReaction(PushReaction.DESTROY)))
            .withTags(BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.DAMPENS_VIBRATIONS).cutout().get();
    public static final Block MIDNIGHT_LILY_PAD = block("midnight_lily", () -> new MidnightLilyPadBlock(props().mapColor(MapColor.DIAMOND).instabreak()
            .sound(SoundType.LILY_PAD).noOcclusion().pushReaction(PushReaction.DESTROY).lightLevel(state -> 8)))
            .withItem(item -> item.withModel(ModelTemplates.FLAT_ITEM)).cutout().get();
    public static final Block AMETHYST_PETALS = block("amethyst_petals", () -> new PinkPetalsBlock(props().mapColor(MapColor.ICE).instabreak().noCollission()
            .sound(SoundType.SMALL_AMETHYST_BUD).pushReaction(PushReaction.DESTROY)
            .lightLevel(lightLevelFromBlockState(8, BlockStateProperties.FLOWER_AMOUNT, 2))) {
            })
            .cutout().get();
    public static final Block GLOW_APPLE_BUSH = block("glow_apple_bush", () -> new TreeBushLikeBlock(props().mapColor(MapColor.GRASS).destroyTime(0.25F)
            .sound(SoundType.SMALL_DRIPLEAF).pushReaction(PushReaction.DESTROY)
            .lightLevel(lightLevelFromBlockStateR(10, TreeBushLikeBlock.GROWN)).randomTicks()))
            .cutout().get();
    public static final Block BURLYWOOD_VIOLET = flower("burlywood_violet", () -> new FlowerBlock(MobEffects.GLOWING, 100.0F, props().mapColor(MapColor.ICE)
            .instabreak().sound(SoundType.FLOWERING_AZALEA).lightLevel(state -> 10).noCollission()));
    public static final Block FROSTBLOOM = flower("frostbloom", () -> new FrostbloomBlock(MobEffects.WEAKNESS, 100.0F, props().mapColor(MapColor.ICE)
            .instabreak().sound(SoundType.GLASS).lightLevel(state -> 6).noCollission()));
    public static final Block CORONA_BLOOM = flower("corona_bloom", () -> new FlowerBlock(MobEffects.FIRE_RESISTANCE, 200.0F, props().mapColor(MapColor.FIRE)
            .instabreak().sound(SoundType.NETHER_WART).lightLevel(state -> 12)
            .emissiveRendering((state, level, pos) -> level.getBlockState(pos.above()).isAir()).noCollission()));
    public static final Block ASHREED = block("ashreed", () -> new ReedBlock(copy(Blocks.SUGAR_CANE)))
            .dropsSelf().cutout().get();
    public static final Block UMBRAL_KELP = block("umbral_kelp", () -> new UmbralKelpBlock(props().mapColor(MapColor.COLOR_PURPLE).instabreak()
            .sound(SoundType.WET_GRASS).pushReaction(PushReaction.DESTROY).noCollission().lightLevel(state -> 8)))
            .withItem(item -> item.withModel(ModelTemplates.FLAT_ITEM)).dropsSelf().cutout().get();
    public static final Block UMBRAL_KELP_PLANT = holder("umbral_kelp_plant", () -> new UmbralKelpPlantBlock(props().mapColor(MapColor.COLOR_PURPLE).instabreak()
            .sound(SoundType.WET_GRASS).pushReaction(PushReaction.DESTROY).noCollission()))
            .dropsOther(() -> ModBlocks.UMBRAL_KELP).cutout().get();

    public static final Block DEAD_UMBRAL_CORAL_BLOCK = block("dead_umbral_coral_block", () -> new Block(copy(Blocks.DEAD_BRAIN_CORAL_BLOCK)))
            .withTags(PICKAXE).withModel(BlockModel.CUBE).dropsSelf().get();
    public static final Block DEAD_UMBRAL_CORAL = block("dead_umbral_coral", () -> new BaseCoralPlantBlock(copy(Blocks.DEAD_BRAIN_CORAL)) {})
            .withTags(PICKAXE).withModel(BlockModel.CROSS).cutout().get();
    public static final Block DEAD_UMBRAL_CORAL_FAN = holder("dead_umbral_coral_fan", () -> new BaseCoralFanBlock(copy(Blocks.DEAD_BRAIN_CORAL_FAN)) {})
            .withTags(PICKAXE).cutout().get();
    public static final Block DEAD_UMBRAL_CORAL_WALL_FAN = holder("dead_umbral_coral_wall_fan", () -> new BaseCoralWallFanBlock(copy(Blocks.DEAD_BRAIN_CORAL_WALL_FAN)
            .dropsLike(ModBlocks.DEAD_UMBRAL_CORAL_FAN)) {})
            .withTags(PICKAXE).cutout().get();
    public static final Block UMBRAL_CORAL_BLOCK = block("umbral_coral_block", () -> new CoralBlock(ModBlocks.DEAD_UMBRAL_CORAL_BLOCK,
            copy(Blocks.BRAIN_CORAL_BLOCK).mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> 6)) {})
            .withTags(PICKAXE).withModel(BlockModel.CUBE).get();
    public static final Block UMBRAL_CORAL = block("umbral_coral", () -> new CoralPlantBlock(ModBlocks.DEAD_UMBRAL_CORAL,
            copy(Blocks.BRAIN_CORAL).mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> 6)) {})
            .withModel(BlockModel.CROSS).cutout().get();
    public static final Block UMBRAL_CORAL_FAN = holder("umbral_coral_fan", () -> new CoralFanBlock(ModBlocks.DEAD_UMBRAL_CORAL_FAN,
            copy(Blocks.BRAIN_CORAL_FAN).mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> 6)) {})
            .cutout().get();
    public static final Block UMBRAL_CORAL_WALL_FAN = holder("umbral_coral_wall_fan", () -> new CoralWallFanBlock(ModBlocks.DEAD_UMBRAL_CORAL_WALL_FAN,
            copy(Blocks.BRAIN_CORAL_WALL_FAN).mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> 6).dropsLike(ModBlocks.UMBRAL_CORAL_FAN)) {})
            .cutout().get();

    public static final Block GLOOMWEED = block("gloomweed", () -> new DarknessFernBlock(props().replaceable().instabreak().sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY).noCollission(), false))
            .withTags(BlockTags.MINEABLE_WITH_HOE).cutout().get();
    public static final Block TALL_GLOOMWEED = block("tall_gloomweed", () -> new DoublePlantBlock(props().replaceable().instabreak().sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY).noCollission()))
            .withTags(BlockTags.MINEABLE_WITH_HOE).cutout().get();
    public static final Block MURKTUFT = block("murktuft", () -> new DarknessFernBlock(props().replaceable().instabreak().randomTicks().sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY).noCollission().lightLevel(lightLevelFromBlockStateR(8, DarknessFernBlock.LIT))
            .emissiveRendering((state, level, pos) -> state.getValue(DarknessFernBlock.LIT)), true))
            .withTags(BlockTags.MINEABLE_WITH_HOE).withModel(BlockModel.CROSS).cutout().get();

    public static final Block GRIPPING_DARK_CURRANTSLATE = grippingStone("gripping_dark_currantslate", 3.0F, null, DARK_CURRANTSLATE);
    public static final Block GRIPPING_GLACIEMITE = grippingStone("gripping_glaciemite", 2.5F, null, GLACIEMITE);
    public static final Block GRIPPING_CORONA_STONE = grippingStone("gripping_corona_stone", 2.0F, null, CORONA_STONE);
    public static final Block GRIPPING_SPIGELIA = block("gripping_spigelia", () -> new GrippingSpigeliaBlock(copy(Blocks.WARPED_FUNGUS).randomTicks()
            .lightLevel(lightLevelFromBlockState(2, BlockStateProperties.AGE_2, 2))
            .emissiveRendering((state, level, pos) -> level.getBlockState(pos.below()).isAir() || level.getBlockState(pos.above()).isAir())
            .jumpFactor(0.85F)))
            .withTags(plus(STONE_TOOL, BlockTags.DAMPENS_VIBRATIONS)).dropsSelf().cutout().get();
    public static final Block GRIPCRYSTAL_WART = block("gripcrystal_wart", () -> new GripcrystalWartBlock(props().replaceable().instabreak()
            .lightLevel(state -> 10).sound(SoundType.LARGE_AMETHYST_BUD).pushReaction(PushReaction.DESTROY).randomTicks().noCollission()))
            .withTags(STONE_TOOL).cutout().get();
    public static final Block GRIPTONITED_DARK_CURRANTSLATE = grippingStone("griptonited_dark_currantslate", 3.0F, IS_NEXT_TO_GRIPTONITE_CLUSTER, DARK_CURRANTSLATE);
    public static final Block GRIPTONITED_GLACIEMITE = grippingStone("griptonited_glaciemite", 2.5F, IS_NEXT_TO_GRIPTONITE_CLUSTER, GLACIEMITE);
    public static final Block GRIPTONITED_CORONA_STONE = grippingStone("griptonited_corona_stone", 2.0F, IS_NEXT_TO_GRIPTONITE_CLUSTER, CORONA_STONE);
    public static final Block GRIPTONITE_CLUSTER = block("griptonite_cluster", () -> new AmethystClusterBlock(10, 4, props().replaceable().destroyTime(2)
            .lightLevel(state -> 4).sound(SoundType.MEDIUM_AMETHYST_BUD).pushReaction(PushReaction.DESTROY)
            .emissiveRendering(BlockBehaviour.BlockStateBase::hasPostProcess).requiresCorrectToolForDrops()))
            .withTags(IRON_TOOL).cutout().get();
    public static final Block DARK_WATER = holder("dark_water", () -> new DarkWaterBlock(ModFluids.DARK_WATER, props().mapColor(MapColor.COLOR_BLACK)
            .replaceable().noCollission().strength(150.0F).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY)))
            .withModel(BlockModel.FLUID).get();

    private static BlockHolder holder(String id, Supplier<Block> block) {
        return UnseenWorld.MOD.register(BlockHolder.class, id, block);
    }

    private static BlockHolder block(String id, Supplier<Block> block) {
        return holder(id, block).withItem();
    }

    private static BlockHolder stoneSet(String id, Supplier<Block> block, TagKey<Block>[] tags) {
        BlockHolder base = block(id, block).withTags(tags).dropsSelf().withStairs().withSlab().withWall();
        base.getStairs().withTags(plus(tags, BlockTags.STAIRS));
        base.getSlab().withTags(plus(tags, BlockTags.SLABS));
        base.getWall().withTags(plus(tags, BlockTags.WALLS));
        return base;
    }

    private static BlockHolder polishedSet(String id, Supplier<Block> block, TagKey<Block>[] tags) {
        BlockHolder base = block(id, block).withTags(tags).dropsSelf().withStairs().withSlab();
        base.getStairs().withTags(plus(tags, BlockTags.STAIRS));
        base.getSlab().withTags(plus(tags, BlockTags.SLABS));
        return base;
    }

    private static Block cubeStone(String id, Supplier<Block> block, TagKey<Block>[] tags) {
        return block(id, block).withTags(tags).dropsSelf().withModel(BlockModel.CUBE).get();
    }

    private static Block grippingStone(String id, float hardness, BlockBehaviour.StatePredicate emissive, Block dropsLike) {
        return block(id, () -> {
            BlockBehaviour.Properties properties = props().destroyTime(hardness).dropsLike(dropsLike);
            return new GrippingStoneBlock(emissive == null ? properties : properties.emissiveRendering(emissive));
        }).withTags(plus(STONE_TOOL, BlockTags.DAMPENS_VIBRATIONS)).get();
    }

    private static Block chest(String id, float hardness, float resistance, SoundType sound, ModChestBlock.ChestTypes type) {
        return block(id, () -> new ModChestBlock(copy(Blocks.CHEST).strength(hardness, resistance).sound(sound), type))
                .withTags(ConventionalBlockTags.CHESTS).dropsSelf().get();
    }

    private static Block ore(String id, float hardness, int light, int minXp, int maxXp, TagKey<Block>[] tool) {
        return block(id, () -> new DropExperienceBlock(UniformInt.of(minXp, maxXp),
                props().destroyTime(hardness).requiresCorrectToolForDrops().lightLevel(state -> light)))
                .withTags(tool).withModel(BlockModel.CUBE).get();
    }

    private static Block storageBlock(String id, float hardness) {
        return block(id, () -> new Block(props().destroyTime(hardness).requiresCorrectToolForDrops()))
                .withTags(plus(DIAMOND_TOOL, BlockTags.BEACON_BASE_BLOCKS)).withModel(BlockModel.CUBE).dropsSelf().get();
    }

    private static Block grass(String id, MapColor color, SoundType sound) {
        return block(id, () -> new DarknessGrassBlock(props().destroyTime(1).mapColor(color).sound(sound)))
                .withTags(BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.DIRT, BlockTags.VALID_SPAWN, BlockTags.ANIMALS_SPAWNABLE_ON).get();
    }

    private static Block dirt(String id, MapColor color) {
        return block(id, () -> new Block(props().destroyTime(1).mapColor(color).sound(SoundType.ROOTED_DIRT)))
                .withTags(BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.DIRT).withModel(BlockModel.CUBE).dropsSelf().get();
    }

    private static Block flower(String id, Supplier<Block> block) {
        return block(id, block).withModel(BlockModel.CROSS).dropsSelf().cutout().get();
    }

    private static BlockHolder logLike(String id, Supplier<BlockBehaviour.Properties> material, TagKey<Block> family, TagKey<Item> logs) {
        return block(id, () -> new RotatedPillarBlock(material.get()))
                .withTags(BlockTags.LOGS_THAT_BURN, BlockTags.MINEABLE_WITH_AXE, family)
                .withItem(item -> item.withTags(ItemTags.LOGS_THAT_BURN, logs))
                .withFlammability(5, 5).dropsSelf();
    }

    private static Block strippedLog(String id, Supplier<BlockBehaviour.Properties> material, TagKey<Block> family, TagKey<Item> logs) {
        return logLike(id, material, family, logs).get();
    }

    private static Block log(String id, Supplier<BlockBehaviour.Properties> material, Supplier<Block> stripped, TagKey<Block> family, TagKey<Item> logs) {
        return logLike(id, material, family, logs).withStripResult(stripped).get();
    }

    private static Block strippedWood(String id, Supplier<BlockBehaviour.Properties> material, TagKey<Block> family, TagKey<Item> logs) {
        return logLike(id, material, family, logs).withModel(BlockModel.CUBE).get();
    }

    private static Block wood(String id, Supplier<BlockBehaviour.Properties> material, Supplier<Block> stripped, TagKey<Block> family, TagKey<Item> logs) {
        return logLike(id, material, family, logs).withStripResult(stripped).withModel(BlockModel.CUBE).get();
    }

    private static BlockHolder planks(String wood, Supplier<BlockBehaviour.Properties> material, WoodType type, TagKey<Block> family) {
        BlockHolder planks = block(wood + "_planks", () -> new Block(material.get()))
                .withTags(BlockTags.PLANKS, BlockTags.MINEABLE_WITH_AXE, family)
                .withItem(item -> item.withTags(ItemTags.PLANKS))
                .withFlammability(5, 20).dropsSelf()
                .withSetPrefix(wood)
                .withStairs().withSlab().withFence().withFenceGate(type)
                .withButton(type.setType(), 30)
                .withPressurePlate(type.setType());
        planks.getStairs().withTags(BlockTags.WOODEN_STAIRS, BlockTags.MINEABLE_WITH_AXE, family)
                .withItem(item -> item.withTags(ItemTags.WOODEN_STAIRS)).withFlammability(5, 20);
        planks.getSlab().withTags(BlockTags.WOODEN_SLABS, BlockTags.MINEABLE_WITH_AXE, family)
                .withItem(item -> item.withTags(ItemTags.WOODEN_SLABS)).withFlammability(5, 20);
        planks.getFence().withTags(BlockTags.WOODEN_FENCES, BlockTags.MINEABLE_WITH_AXE, family)
                .withItem(item -> item.withTags(ItemTags.WOODEN_FENCES)).withFlammability(5, 5);
        planks.getFenceGate().withTags(BlockTags.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, BlockTags.MINEABLE_WITH_AXE, family)
                .withItem(item -> item.withTags(ItemTags.FENCE_GATES)).withFlammability(5, 5);
        planks.getButton().withTags(BlockTags.WOODEN_BUTTONS, BlockTags.MINEABLE_WITH_AXE, family)
                .withItem(item -> item.withTags(ItemTags.WOODEN_BUTTONS));
        planks.getPressurePlate().withTags(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.WALL_POST_OVERRIDE, BlockTags.MINEABLE_WITH_AXE, family)
                .withItem(item -> item.withTags(ItemTags.WOODEN_PRESSURE_PLATES));
        return planks;
    }

    private static Block door(String wood, WoodType type, TagKey<Block> family) {
        return block(wood + "_door", () -> new DoorBlock(type.setType(), copy(Blocks.OAK_DOOR).mapColor(MapColor.TERRACOTTA_YELLOW)))
                .withTags(BlockTags.WOODEN_DOORS, BlockTags.MINEABLE_WITH_AXE, family)
                .withItem(item -> item.withTags(ItemTags.WOODEN_DOORS))
                .withModel(BlockModel.DOOR).cutout().get();
    }

    private static Block trapdoor(String wood, WoodType type, TagKey<Block> family) {
        return block(wood + "_trapdoor", () -> new TrapDoorBlock(type.setType(), copy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.TERRACOTTA_YELLOW)))
                .withTags(BlockTags.WOODEN_TRAPDOORS, BlockTags.MINEABLE_WITH_AXE, family)
                .withItem(item -> item.withTags(ItemTags.WOODEN_TRAPDOORS))
                .dropsSelf().cutout().get();
    }

    private static Block sapling(String wood, TreeGrower grower, TagKey<Block> family) {
        return block(wood + "_sapling", () -> new SaplingBlock(grower, copy(Blocks.OAK_SAPLING).mapColor(MapColor.COLOR_PURPLE).instabreak()))
                .withTags(BlockTags.SAPLINGS, family)
                .withItem(item -> item.withTags(ItemTags.SAPLINGS))
                .withModel(BlockModel.CROSS).dropsSelf().cutout().get();
    }

    private static BlockBehaviour.Properties props() {
        return BlockBehaviour.Properties.of();
    }

    private static BlockBehaviour.Properties copy(Block block) {
        return BlockBehaviour.Properties.ofFullCopy(block);
    }

    private static BlockBehaviour.Properties darkCurrantslate() {
        return props().destroyTime(3).instrument(NoteBlockInstrument.BIT).mapColor(MapColor.CRIMSON_HYPHAE).sound(SoundType.GILDED_BLACKSTONE);
    }

    private static BlockBehaviour.Properties glaciemite() {
        return props().destroyTime(2.5F).instrument(NoteBlockInstrument.SNARE).mapColor(MapColor.WARPED_STEM).sound(SoundType.DEEPSLATE);
    }

    private static BlockBehaviour.Properties coronaStone() {
        return copy(Blocks.CALCITE).sound(SoundType.BONE_BLOCK);
    }

    private static BlockBehaviour.Properties tanzanite() {
        return props().destroyTime(2).lightLevel(state -> 5).instrument(NoteBlockInstrument.BELL).mapColor(MapColor.TERRACOTTA_PURPLE).sound(SoundType.AMETHYST);
    }

    private static BlockBehaviour.Properties amethystWood() {
        return props().destroyTime(2.5F).lightLevel(state -> 3).mapColor(MapColor.COLOR_PURPLE).sound(SoundType.AMETHYST);
    }

    private static BlockBehaviour.Properties amethystLeaves() {
        return props().sound(SoundType.AZALEA_LEAVES).noOcclusion().strength(0.2F).randomTicks();
    }

    private static BlockBehaviour.Properties burlywood() {
        return props().destroyTime(3).lightLevel(state -> 3).mapColor(MapColor.PLANT).sound(SoundType.BAMBOO_WOOD);
    }

    private static BlockBehaviour.Properties burlywoodLeaves() {
        return props().lightLevel(state -> 3).mapColor(MapColor.PLANT).sound(SoundType.BIG_DRIPLEAF).noOcclusion().strength(0.2F).randomTicks();
    }

    private static BlockBehaviour.Properties tealiveWood() {
        return props().destroyTime(1.5F).lightLevel(state -> 5).mapColor(MapColor.COLOR_CYAN).sound(SoundType.NETHER_WOOD);
    }

    private static BlockBehaviour.Properties crimsonveil() {
        return props().destroyTime(2).lightLevel(state -> 2).mapColor(MapColor.TERRACOTTA_ORANGE).sound(SoundType.WOOD);
    }

    private static BlockBehaviour.Properties grizzlyWood() {
        return props().destroyTime(3.5F).mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHER_WOOD);
    }

    private static BlockBehaviour.Properties effigySettings() {
        return copy(Blocks.POLISHED_BLACKSTONE_BRICKS)
                .strength(-1.0F, 3600000.0F)
                .noOcclusion()
                .noLootTable()
                .dynamicShape()
                .pushReaction(PushReaction.BLOCK)
                .isSuffocating((state, level, pos) -> false)
                .isViewBlocking((state, level, pos) -> false)
                .isValidSpawn((state, level, pos, type) -> false);
    }

    private static BlockBehaviour.Properties vineSettings(MapColor color, SoundType sound) {
        return props().mapColor(color)
                .randomTicks()
                .noCollission()
                .lightLevel(ModPlantVines.getLuminanceSupplier(12))
                .instabreak()
                .sound(sound)
                .pushReaction(PushReaction.DESTROY);
    }

    @SafeVarargs
    private static TagKey<Block>[] tags(TagKey<Block>... tags) {
        return tags;
    }

    @SafeVarargs
    private static TagKey<Block>[] plus(TagKey<Block>[] base, TagKey<Block>... extra) {
        TagKey<Block>[] result = Arrays.copyOf(base, base.length + extra.length);
        System.arraycopy(extra, 0, result, base.length, extra.length);
        return result;
    }

    public static ToIntFunction<BlockState> lightLevelFromBlockState(int litLevel, IntegerProperty property, int modifierToLight) {
        return state -> Math.min(state.getValue(property) * modifierToLight, litLevel);
    }

    public static ToIntFunction<BlockState> lightLevelFromBlockState(int litLevel, BooleanProperty property) {
        return state -> state.getValue(property) ? 0 : litLevel;
    }

    public static ToIntFunction<BlockState> lightLevelFromBlockStateR(int litLevel, BooleanProperty property) {
        return state -> state.getValue(property) ? litLevel : 0;
    }
}