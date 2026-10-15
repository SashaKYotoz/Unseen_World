package net.sashakyotoz.common.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.particles.custom.effects.LeafParticleEffect;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.custom.*;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;
import net.sashakyotoz.common.blocks.custom.corals.*;
import net.sashakyotoz.common.blocks.custom.plants.*;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.features.trees.generators.*;

import java.util.Map;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static void register() {
        UnseenWorld.log().debug("Registering Blocks for modid : " + UnseenWorld.MOD_ID);
        ModRegistry.BlockBuilder.registerBricksSet(DARK_CURRANTSLATE, DARK_CURRANTSLATE_STAIRS, DARK_CURRANTSLATE_SLAB, DARK_CURRANTSLATE_WALL);
        ModRegistry.BlockBuilder.registerBricksSet(DARK_CURRANTSLATE_BRICKS, DARK_CURRANTSLATE_BRICKS_STAIRS, DARK_CURRANTSLATE_BRICKS_SLAB, DARK_CURRANTSLATE_BRICKS_WALL);
        ModRegistry.BlockBuilder.registerBricksSet(GLACIEMITE, GLACIEMITE_STAIRS, GLACIEMITE_SLAB, GLACIEMITE_WALL);
        ModRegistry.BlockBuilder.registerBricksSet(GLACIEMITE_BRICKS, GLACIEMITE_BRICKS_STAIRS, GLACIEMITE_BRICKS_SLAB, GLACIEMITE_BRICKS_WALL);
        ModRegistry.BlockBuilder.registerBricksSet(CORONA_STONE, CORONA_STONE_STAIRS, CORONA_STONE_SLAB, CORONA_STONE_WALL);
        ModRegistry.BlockBuilder.registerBricksSet(CORONA_STONE_BRICKS, CORONA_STONE_BRICKS_STAIRS, CORONA_STONE_BRICKS_SLAB, CORONA_STONE_BRICKS_WALL);
        ModRegistry.BlockBuilder.registerPolishedSet(POLISHED_TANZANITE, POLISHED_TANZANITE_STAIRS, POLISHED_TANZANITE_SLAB);
        ModRegistry.addDrop(ModItems.AMETHYST_SIGN, ModItems.AMETHYST_SIGN);
        ModRegistry.addDrop(ModItems.AMETHYST_WALL_SIGN, ModItems.AMETHYST_SIGN);
        ModRegistry.addDrop(ModItems.AMETHYST_HANGING_SIGN, ModItems.AMETHYST_HANGING_SIGN);
        ModRegistry.addDrop(ModItems.AMETHYST_WALL_HANGING_SIGN, ModItems.AMETHYST_HANGING_SIGN);

        ModRegistry.addDrop(ModItems.GRIZZLY_SIGN, ModItems.GRIZZLY_SIGN);
        ModRegistry.addDrop(ModItems.GRIZZLY_WALL_SIGN, ModItems.GRIZZLY_SIGN);
        ModRegistry.addDrop(ModItems.GRIZZLY_HANGING_SIGN, ModItems.GRIZZLY_HANGING_SIGN);
        ModRegistry.addDrop(ModItems.GRIZZLY_WALL_HANGING_SIGN, ModItems.GRIZZLY_HANGING_SIGN);

        ModRegistry.addDrop(ModItems.TEALIVE_SIGN, ModItems.TEALIVE_SIGN);
        ModRegistry.addDrop(ModItems.TEALIVE_WALL_SIGN, ModItems.TEALIVE_SIGN);
        ModRegistry.addDrop(ModItems.TEALIVE_HANGING_SIGN, ModItems.TEALIVE_HANGING_SIGN);
        ModRegistry.addDrop(ModItems.TEALIVE_WALL_HANGING_SIGN, ModItems.TEALIVE_HANGING_SIGN);
        ModRegistry.registerSet(AMETHYST_PLANKS, Map.of(
                ModRegistry.Models.STAIRS, AMETHYST_STAIRS,
                ModRegistry.Models.SLAB, AMETHYST_SLAB,
                ModRegistry.Models.BUTTON, AMETHYST_BUTTON,
                ModRegistry.Models.PRESSURE_PLATE, AMETHYST_PRESSURE_PLATE,
                ModRegistry.Models.FENCE, AMETHYST_FENCE,
                ModRegistry.Models.FENCE_GATE, AMETHYST_FENCE_GATE,
                ModRegistry.Models.SIGN, ModItems.AMETHYST_SIGN,
                ModRegistry.Models.WALL_SIGN, ModItems.AMETHYST_WALL_SIGN,
                ModRegistry.Models.HANGING_SIGN, ModItems.AMETHYST_HANGING_SIGN,
                ModRegistry.Models.WALL_HANGING_SIGN, ModItems.AMETHYST_WALL_HANGING_SIGN
        ));
        ModRegistry.registerSet(GRIZZLY_PLANKS, Map.of(
                ModRegistry.Models.STAIRS, GRIZZLY_STAIRS,
                ModRegistry.Models.SLAB, GRIZZLY_SLAB,
                ModRegistry.Models.BUTTON, GRIZZLY_BUTTON,
                ModRegistry.Models.PRESSURE_PLATE, GRIZZLY_PRESSURE_PLATE,
                ModRegistry.Models.FENCE, GRIZZLY_FENCE,
                ModRegistry.Models.FENCE_GATE, GRIZZLY_FENCE_GATE,
                ModRegistry.Models.SIGN, ModItems.GRIZZLY_SIGN,
                ModRegistry.Models.WALL_SIGN, ModItems.GRIZZLY_WALL_SIGN,
                ModRegistry.Models.HANGING_SIGN, ModItems.GRIZZLY_HANGING_SIGN,
                ModRegistry.Models.WALL_HANGING_SIGN, ModItems.GRIZZLY_WALL_HANGING_SIGN
        ));
        ModRegistry.registerSet(TEALIVE_PLANKS, Map.of(
                ModRegistry.Models.STAIRS, TEALIVE_STAIRS,
                ModRegistry.Models.SLAB, TEALIVE_SLAB,
                ModRegistry.Models.BUTTON, TEALIVE_BUTTON,
                ModRegistry.Models.PRESSURE_PLATE, TEALIVE_PRESSURE_PLATE,
                ModRegistry.Models.FENCE, TEALIVE_FENCE,
                ModRegistry.Models.FENCE_GATE, TEALIVE_FENCE_GATE,
                ModRegistry.Models.SIGN, ModItems.TEALIVE_SIGN,
                ModRegistry.Models.WALL_SIGN, ModItems.TEALIVE_WALL_SIGN,
                ModRegistry.Models.HANGING_SIGN, ModItems.TEALIVE_HANGING_SIGN,
                ModRegistry.Models.WALL_HANGING_SIGN, ModItems.TEALIVE_WALL_HANGING_SIGN
        ));
        ModRegistry.registerSet(BURLYWOOD_PLANKS, Map.of(
                ModRegistry.Models.STAIRS, BURLYWOOD_STAIRS,
                ModRegistry.Models.SLAB, BURLYWOOD_SLAB,
                ModRegistry.Models.BUTTON, BURLYWOOD_BUTTON,
                ModRegistry.Models.PRESSURE_PLATE, BURLYWOOD_PRESSURE_PLATE,
                ModRegistry.Models.FENCE, BURLYWOOD_FENCE,
                ModRegistry.Models.FENCE_GATE, BURLYWOOD_FENCE_GATE,
                ModRegistry.Models.SIGN, ModItems.BURLYWOOD_SIGN,
                ModRegistry.Models.WALL_SIGN, ModItems.BURLYWOOD_WALL_SIGN,
                ModRegistry.Models.HANGING_SIGN, ModItems.BURLYWOOD_HANGING_SIGN,
                ModRegistry.Models.WALL_HANGING_SIGN, ModItems.BURLYWOOD_WALL_HANGING_SIGN
        ));
        ModRegistry.registerSet(CRIMSONVEIL_PLANKS, Map.of(
                ModRegistry.Models.STAIRS, CRIMSONVEIL_STAIRS,
                ModRegistry.Models.SLAB, CRIMSONVEIL_SLAB,
                ModRegistry.Models.BUTTON, CRIMSONVEIL_BUTTON,
                ModRegistry.Models.PRESSURE_PLATE, CRIMSONVEIL_PRESSURE_PLATE,
                ModRegistry.Models.FENCE, CRIMSONVEIL_FENCE,
                ModRegistry.Models.FENCE_GATE, CRIMSONVEIL_FENCE_GATE,
                ModRegistry.Models.SIGN, ModItems.CRIMSONVEIL_SIGN,
                ModRegistry.Models.WALL_SIGN, ModItems.CRIMSONVEIL_WALL_SIGN,
                ModRegistry.Models.HANGING_SIGN, ModItems.CRIMSONVEIL_HANGING_SIGN,
                ModRegistry.Models.WALL_HANGING_SIGN, ModItems.CRIMSONVEIL_WALL_HANGING_SIGN
        ));
    }

    private static FabricBlockSettings copy(BlockBehaviour.Properties original) {
        return FabricBlockSettings.copyOf(original);
    }

    private static FabricBlockSettings copy(Block original) {
        return FabricBlockSettings.copyOf(original);
    }

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

    private static final BlockBehaviour.Properties DarkCurrantslateMaterial =
            FabricBlockSettings.create().destroyTime(3).instrument(NoteBlockInstrument.BIT).mapColor(MapColor.CRIMSON_HYPHAE).sound(SoundType.GILDED_BLACKSTONE);
    private static final BlockBehaviour.Properties GlaciemiteMaterial =
            FabricBlockSettings.create().destroyTime(2.5f).instrument(NoteBlockInstrument.SNARE).mapColor(MapColor.WARPED_STEM).sound(SoundType.DEEPSLATE);
    private static final BlockBehaviour.Properties CoronaStoneMaterial =
            copy(Blocks.CALCITE).sound(SoundType.BONE_BLOCK);
    private static final BlockBehaviour.Properties TanzaniteMaterial =
            FabricBlockSettings.create().destroyTime(2f).lightLevel((blockState) -> 5).instrument(NoteBlockInstrument.BELL).mapColor(MapColor.TERRACOTTA_PURPLE).sound(SoundType.AMETHYST);

    private static final BlockBehaviour.Properties PassableTreeMaterial = copy(Blocks.OAK_SIGN);
    private static final BlockBehaviour.Properties AmethystTreeMaterial =
            FabricBlockSettings.create().destroyTime(2.5f).lightLevel((blockState) -> 3).mapColor(MapColor.COLOR_PURPLE).sound(SoundType.AMETHYST);
    private static final BlockBehaviour.Properties AmethystLeafMaterial = FabricBlockSettings.create().sound(SoundType.AZALEA_LEAVES).noOcclusion().instabreak().randomTicks();
    private static final BlockBehaviour.Properties BurlywoodTreeMaterial =
            FabricBlockSettings.create().destroyTime(3f).lightLevel((blockState) -> 3).mapColor(MapColor.PLANT).sound(SoundType.BAMBOO_WOOD);
    private static final BlockBehaviour.Properties BurlywoodLeafMaterial = FabricBlockSettings.create().luminance((blockState) -> 3).mapColor(MapColor.PLANT).sound(SoundType.BIG_DRIPLEAF).noOcclusion().instabreak().randomTicks();
    private static final BlockBehaviour.Properties TealiveTreeMaterial =
            FabricBlockSettings.create().destroyTime(1.5f).lightLevel((blockState) -> 5).mapColor(MapColor.COLOR_CYAN).sound(SoundType.NETHER_WOOD);
    private static final BlockBehaviour.Properties TealiveLeafMaterial = FabricBlockSettings.create().mapColor(MapColor.COLOR_CYAN).sound(SoundType.AZALEA_LEAVES).noOcclusion().instabreak().randomTicks();
    private static final BlockBehaviour.Properties CrimsonveilTreeMaterial =
            FabricBlockSettings.create().destroyTime(2f).lightLevel((blockState) -> 2).mapColor(MapColor.TERRACOTTA_ORANGE).sound(SoundType.WOOD);
    private static final BlockBehaviour.Properties CrimsonveilLeafMaterial = FabricBlockSettings.create().destroyTime(2f).lightLevel((blockState) -> 2).mapColor(MapColor.TERRACOTTA_ORANGE).sound(SoundType.CHERRY_LEAVES).noOcclusion().instabreak().randomTicks();
    private static final BlockBehaviour.Properties GrizzlyTreeMaterial =
            FabricBlockSettings.create().destroyTime(3.5f).mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHER_WOOD);
    private static final BlockBehaviour.Properties GrizzlyLeafMaterial = FabricBlockSettings.create().destroyTime(3.5f).mapColor(MapColor.COLOR_GRAY).sound(SoundType.CHERRY_LEAVES).noOcclusion().instabreak();

    public static final WoodType GRIZZLY = new WoodTypeBuilder().register(UnseenWorld.MOD.makeID("grizzly"), BlockSetType.DARK_OAK);
    public static final WoodType AMETHYST = new WoodTypeBuilder().register(UnseenWorld.MOD.makeID("amethyst"), BlockSetType.BIRCH);
    public static final WoodType BURLYWOOD = new WoodTypeBuilder().register(UnseenWorld.MOD.makeID("burlywood"), BlockSetType.MANGROVE);
    public static final WoodType TEALIVE = new WoodTypeBuilder().register(UnseenWorld.MOD.makeID("tealive"), BlockSetType.JUNGLE);
    public static final WoodType CRIMSONVEIL = new WoodTypeBuilder().register(UnseenWorld.MOD.makeID("crimsonveil"), BlockSetType.CRIMSON);
    public static final Block DARK_FROST_BRICKS = ModRegistry.ofBlock("dark_frost_bricks",
                    new Block(copy(Blocks.POLISHED_BLACKSTONE_BRICKS)))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().model().build();
    public static final Block ORDEAL_SPAWNER = ModRegistry.ofBlock("ordeal_spawner",
                    new OrdealSpawnerBlock(copy(Blocks.SPAWNER).strength(50.0F, 1200.0F).noOcclusion().noLootTable()
                            .pushReaction(PushReaction.BLOCK)
                            .lightLevel(state -> state.getValue(OrdealSpawnerBlock.STATE).luminance)))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe").cutout().build();
    public static final Block EFFIGY = ModRegistry.ofBlock("effigy",
                    new EffigyBlock(effigySettings()
                            .lightLevel(state -> state.getValue(EffigyBlock.PHASE).luminance)))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).build();
    public static final Block EFFIGY_PART = ModRegistry.ofBlock("effigy_part",
                    new EffigyPartBlock(effigySettings().noParticlesOnBreak()), false)
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).build();
    public static final Block GRIPTONITE_LANTERN = ModRegistry.ofBlock("griptonite_lantern",
                    new ModLanternBlock(FabricBlockSettings.create().forceSolidOn().sound(SoundType.LANTERN).randomTicks().requiresCorrectToolForDrops().strength(3.5F).lightLevel((blockState) -> 14)))
            .tool("stone_pickaxe").cutout().drop().build();
    public static final Block DARK_CURRANTSLATE = dungeonStone("dark_currantslate", new Block(DarkCurrantslateMaterial));
    public static final Block GILDED_CURRANTSLATE = dungeonStoneWithModel("gilded_currantslate", new Block(DarkCurrantslateMaterial));
    public static final Block DARK_CURRANTSLATE_STAIRS = dungeonStairs("dark_currantslate_stairs", DARK_CURRANTSLATE, DarkCurrantslateMaterial);
    public static final Block DARK_CURRANTSLATE_SLAB = dungeonSlab("dark_currantslate_slab", DarkCurrantslateMaterial);
    public static final Block DARK_CURRANTSLATE_WALL = dungeonWall("dark_currantslate_wall", DarkCurrantslateMaterial);
    public static final Block DARK_CURRANTSLATE_BRICKS = dungeonStone("dark_currantslate_bricks", new Block(DarkCurrantslateMaterial));
    public static final Block DARK_CURRANTSLATE_BRICKS_STAIRS = dungeonStairs("dark_currantslate_bricks_stairs", DARK_CURRANTSLATE_BRICKS, DarkCurrantslateMaterial);
    public static final Block DARK_CURRANTSLATE_BRICKS_SLAB = dungeonSlab("dark_currantslate_bricks_slab", DarkCurrantslateMaterial);
    public static final Block DARK_CURRANTSLATE_BRICKS_WALL = dungeonWall("dark_currantslate_bricks_wall", DarkCurrantslateMaterial);
    public static final Block DARK_CURRANTSLATE_CHEST = chest("dark_currantslate_chest", 5, 5, SoundType.POLISHED_DEEPSLATE, ModChestBlock.ChestTypes.DARK_CURRANTSLATE);
    public static final Block GLACIEMITE = dungeonStone("glaciemite", new Block(GlaciemiteMaterial));
    public static final Block GLACIEMITE_STAIRS = dungeonStairs("glaciemite_stairs", GLACIEMITE, GlaciemiteMaterial);
    public static final Block GLACIEMITE_SLAB = dungeonSlab("glaciemite_slab", GlaciemiteMaterial);
    public static final Block GLACIEMITE_WALL = dungeonWall("glaciemite_wall", GlaciemiteMaterial);
    public static final Block GLACIEMITE_BRICKS = dungeonStone("glaciemite_bricks", new Block(GlaciemiteMaterial));
    public static final Block CRACKED_GLACIEMITE_BRICKS = dungeonStoneWithModel("cracked_glaciemite_bricks", new Block(copy(GlaciemiteMaterial).destroyTime(2f)));
    public static final Block CHISELED_GLACIEMITE_BRICKS = ModRegistry.ofBlock("chiseled_glaciemite_bricks",
                    new Block(GlaciemiteMaterial))
            .tool("stone_pickaxe")
            .drop().model().build();
    public static final Block ENGRAVED_GLACIEMITE_BRICKS = ModRegistry.ofBlock("engraved_glaciemite_bricks",
                    new EngravedBricksBlock(GlaciemiteMaterial))
            .tool("stone_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE_BRICKS_STAIRS = dungeonStairs("glaciemite_bricks_stairs", GLACIEMITE_BRICKS, GlaciemiteMaterial);
    public static final Block GLACIEMITE_BRICKS_SLAB = dungeonSlab("glaciemite_bricks_slab", GlaciemiteMaterial);
    public static final Block GLACIEMITE_BRICKS_WALL = dungeonWall("glaciemite_bricks_wall", GlaciemiteMaterial);
    public static final Block GLACIEMITE_CHEST = chest("glaciemite_chest", 4, 5, SoundType.POLISHED_DEEPSLATE, ModChestBlock.ChestTypes.GLACIEMITE);
    public static final Block GLACIEMITE_BULB = ModRegistry.ofBlock("glaciemite_bulb",
                    new BulbLikeBlock(copy(GlaciemiteMaterial).luminance(Blocks.litBlockEmission(14))))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().model(ModRegistry.Models.BULB).build();
    public static final Block CORONA_STONE = dungeonStone("corona_stone", new Block(CoronaStoneMaterial));
    public static final Block CORONA_STONE_STAIRS = dungeonStairs("corona_stone_stairs", CORONA_STONE, CoronaStoneMaterial);
    public static final Block CORONA_STONE_SLAB = dungeonSlab("corona_stone_slab", CoronaStoneMaterial);
    public static final Block CORONA_STONE_WALL = dungeonWall("corona_stone_wall", CoronaStoneMaterial);
    public static final Block CORONA_STONE_BRICKS = dungeonStone("corona_stone_bricks", new Block(CoronaStoneMaterial));
    public static final Block CRACKED_CORONA_STONE_BRICKS = dungeonStoneWithModel("cracked_corona_stone_bricks", new Block(copy(CoronaStoneMaterial).destroyTime(0.5f)));
    public static final Block ENGRAVED_CORONA_STONE_BRICKS = ModRegistry.ofBlock("engraved_corona_stone_bricks",
                    new EngravedBricksBlock(CoronaStoneMaterial))
            .tool("stone_pickaxe")
            .drop().build();
    public static final Block CORONA_STONE_BRICKS_STAIRS = dungeonStairs("corona_stone_bricks_stairs", CORONA_STONE_BRICKS, CoronaStoneMaterial);
    public static final Block CORONA_STONE_BRICKS_SLAB = dungeonSlab("corona_stone_bricks_slab", CoronaStoneMaterial);
    public static final Block CORONA_STONE_BRICKS_WALL = dungeonWall("corona_stone_bricks_wall", CoronaStoneMaterial);
    public static final Block CORONA_STONE_CHEST = chest("corona_stone_chest", 2, 2, SoundType.BONE_BLOCK, ModChestBlock.ChestTypes.CORONA_STONE);
    public static final Block TANZANITE_BLOCK = ModRegistry.ofBlock("tanzanite_block",
                    new Block(TanzaniteMaterial)).tool("stone_pickaxe")
            .drop().model().build();
    public static final Block POLISHED_TANZANITE = ModRegistry.ofBlock("polished_tanzanite",
                    new Block(TanzaniteMaterial)).tool("stone_pickaxe")
            .drop().build();
    public static final Block POLISHED_TANZANITE_STAIRS = ModRegistry.ofBlock("polished_tanzanite_stairs",
                    new StairBlock(POLISHED_TANZANITE.defaultBlockState(), TanzaniteMaterial)).tool("stone_pickaxe")
            .drop().build();
    public static final Block POLISHED_TANZANITE_SLAB = ModRegistry.ofBlock("polished_tanzanite_slab",
                    new SlabBlock(TanzaniteMaterial)).tool("stone_pickaxe")
            .drop().build();
    public static final Block ABYSSAL_ORE_IN_DARK_CURRANTSLATE = ore("abyssal_ore_in_dark_currantslate", 5, 4, 1, 5, "diamond_pickaxe");
    public static final Block ABYSSAL_ORE_IN_GLACIEMITE = ore("abyssal_ore_in_glaciemite", 5, 4, 1, 5, "diamond_pickaxe");
    public static final Block ABYSSAL_ORE_IN_CORONA_STONE = ore("abyssal_ore_in_corona_stone", 4, 4, 1, 5, "diamond_pickaxe");
    public static final Block ABYSSAL_INGOT_BLOCK = storageBlock("abyssal_ingot_block", 5);
    public static final Block RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE = ore("red_titanium_ore_in_dark_currantslate", 10, 3, 1, 6, "diamond_pickaxe");
    public static final Block RED_TITANIUM_ORE_IN_GLACIEMITE = ore("red_titanium_ore_in_glaciemite", 10, 3, 1, 6, "diamond_pickaxe");
    public static final Block RED_TITANIUM_ORE_IN_CORONA_STONE = ore("red_titanium_ore_in_corona_stone", 8, 3, 1, 6, "diamond_pickaxe");
    public static final Block RED_TITANIUM_INGOT_BLOCK = storageBlock("red_titanium_ingot_block", 6);
    public static final Block UNSEENIUM_ORE_IN_DARK_CURRANTSLATE = ore("unseenium_ore_in_dark_currantslate", 4, 5, 1, 4, "diamond_pickaxe");
    public static final Block UNSEENIUM_ORE_IN_GLACIEMITE = ore("unseenium_ore_in_glaciemite", 4, 5, 1, 4, "diamond_pickaxe");
    public static final Block UNSEENIUM_ORE_IN_CORONA_STONE = ore("unseenium_ore_in_corona_stone", 3, 5, 1, 4, "diamond_pickaxe");
    public static final Block UNSEENIUM_INGOT_BLOCK = storageBlock("unseenium_ingot_block", 4);
    public static final Block IRON_ORE_IN_DARK_CURRANTSLATE = ore("iron_ore_in_dark_currantslate", 3, 0, 1, 3, "iron_pickaxe");
    public static final Block IRON_ORE_IN_GLACIEMITE = ore("iron_ore_in_glaciemite", 3, 0, 1, 3, "iron_pickaxe");
    public static final Block IRON_ORE_IN_CORONA_STONE = ore("iron_ore_in_corona_stone", 3, 0, 1, 3, "iron_pickaxe");
    public static final Block ANCIENT_DEBRIS_IN_DARK_CURRANTSLATE = ore("ancient_debris_in_dark_currantslate", 15, 0, 1, 2, "diamond_pickaxe");
    public static final Block ANCIENT_DEBRIS_IN_GLACIEMITE = ore("ancient_debris_in_glaciemite", 15, 0, 1, 2, "diamond_pickaxe");
    public static final Block ANCIENT_DEBRIS_IN_CORONA_STONE = ore("ancient_debris_in_corona_stone", 15, 0, 1, 2, "diamond_pickaxe");
    public static final Block NIGHTDARK_GRASS_BLOCK = grass("nightdark_grass_block", MapColor.COLOR_BLACK, SoundType.ROOTED_DIRT);
    public static final Block PENUMBRAL_GRASS_BLOCK = grass("penumbral_grass_block", MapColor.COLOR_BLACK, SoundType.ROOTED_DIRT);
    public static final Block TEALIVY_GRASS_BLOCK = grass("tealivy_grass_block", MapColor.COLOR_CYAN, SoundType.ROOTED_DIRT);
    public static final Block AMETHYST_GRASS_BLOCK = grass("amethyst_grass_block", MapColor.COLOR_PURPLE, SoundType.AMETHYST);
    public static final Block GRIMWOOD_GRASS_BLOCK = grass("grimwood_grass_block", MapColor.COLOR_RED, SoundType.GILDED_BLACKSTONE);
    public static final Block NIGHTDARK_DIRT = ModRegistry.ofBlock("nightdark_dirt", new Block(FabricBlockSettings.create().destroyTime(1).mapColor(MapColor.COLOR_GRAY).sound(SoundType.ROOTED_DIRT))).tool("_shovel").model().drop().tag(BlockTags.DIRT).build();
    public static final Block TEALIVY_DIRT = ModRegistry.ofBlock("tealivy_dirt", new Block(FabricBlockSettings.create().destroyTime(1).mapColor(MapColor.COLOR_CYAN).sound(SoundType.ROOTED_DIRT))).tool("_shovel").model().drop().tag(BlockTags.DIRT).build();
    public static final Block ASHEN_OOZE = ModRegistry.ofBlock("ashen_ooze", new Block(FabricBlockSettings.create().sound(SoundType.ROOTED_DIRT).destroyTime(1).mapColor(MapColor.TERRACOTTA_ORANGE))).tool("_shovel").drop().model().build();
    public static final Block SUSPICIOUS_ASHEN_OOZE = ModRegistry.ofBlock("suspicious_ashen_ooze", new SusBlock(
            ASHEN_OOZE,
            FabricBlockSettings.create().destroyTime(0.25f).mapColor(MapColor.TERRACOTTA_ORANGE),
            SoundEvents.BRUSH_GRAVEL,
            SoundEvents.BRUSH_SAND_COMPLETED
    )).tool("_shovel").build();
    public static final Block GLIMMERGRAIN_SAND = ModRegistry.ofBlock("glimmergrain_sand", new FallingBlock(FabricBlockSettings.create().sound(SoundType.SOUL_SAND)
            .destroyTime(1).mapColor(MapColor.COLOR_GRAY))).tool("_shovel").drop().tag(BlockTags.SAND, BlockTags.SMELTS_TO_GLASS).model().build();
    public static final Block SUSPICIOUS_GLIMMERGRAIN_SAND = ModRegistry.ofBlock("suspicious_glimmergrain_sand", new SusBlock(
            GLIMMERGRAIN_SAND,
            FabricBlockSettings.create().sound(SoundType.SUSPICIOUS_SAND).destroyTime(0.25f).mapColor(MapColor.SAND),
            SoundEvents.BRUSH_SAND,
            SoundEvents.BRUSH_GRAVEL_COMPLETED
    )).tool("_shovel").build();
    public static final Block GLIMMERGRAIN_SANDSTONE = ModRegistry.ofBlock("glimmergrain_sandstone", new Block(FabricBlockSettings.create().sound(SoundType.BONE_BLOCK).destroyTime(1.8f).mapColor(MapColor.COLOR_GRAY))).tool("_pickaxe").drop().build();
    public static final Block STRIPPED_AMETHYST_LOG = strippedLog("stripped_amethyst_log", AmethystTreeMaterial, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Items.AMETHYST_LOGS);
    public static final Block AMETHYST_LOG = log("amethyst_log", AmethystTreeMaterial, STRIPPED_AMETHYST_LOG, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Items.AMETHYST_LOGS);
    public static final Block STRIPPED_AMETHYST_WOOD = strippedWood("stripped_amethyst_wood", AmethystTreeMaterial, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Items.AMETHYST_LOGS);
    public static final Block AMETHYST_WOOD = wood("amethyst_wood", AmethystTreeMaterial, STRIPPED_AMETHYST_WOOD, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Items.AMETHYST_LOGS);
    public static final Block AMETHYST_LEAVES = ModRegistry.ofBlock("amethyst_leaves",
                    new LeafDroppingLeaveBlock(AmethystLeafMaterial, new LeafParticleEffect(0.6f, 0.2f, 0.7f)))
            .tag(BlockTags.LEAVES, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .cutout().build();
    public static final Block HANGING_AMETHYST_LEAVES = ModRegistry.ofBlock("hanging_amethyst_leaves",
                    new HangingFruitBlock(copy(AmethystLeafMaterial).luminance(lightLevelFromBlockStateR(4, HangingFruitBlock.HAS_FRUIT)).noCollission().instabreak(),
                            () -> ModItems.CRYSTIE_APPLE,
                            ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON,
                            Block.box(0, 0, 0, 16, 16, 16)))
            .tag(BlockTags.LEAVES, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .cutout().build();
    public static final Block AMETHYST_PLANKS = planks("amethyst", AmethystTreeMaterial, ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_STAIRS = woodenStairs("amethyst", AMETHYST_PLANKS, AmethystTreeMaterial, ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_SLAB = woodenSlab("amethyst", AmethystTreeMaterial, ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_PRESSURE_PLATE = woodenPressurePlate("amethyst", ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_BUTTON = woodenButton("amethyst", ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_FENCE = woodenFence("amethyst", AmethystTreeMaterial, ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_FENCE_GATE = woodenFenceGate("amethyst", AmethystTreeMaterial, AMETHYST, ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_DOOR = woodenDoor("amethyst", ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_TRAPDOOR = woodenTrapdoor("amethyst", ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block AMETHYST_SAPLING = sapling("amethyst", new AmethystSaplingGenerator(), ModTags.Blocks.AMETHYST_BLOCKS);
    public static final Block STRIPPED_GRIZZLY_LOG = strippedLog("stripped_grizzly_log", GrizzlyTreeMaterial, ModTags.Blocks.GRIZZLY_BLOCKS, ModTags.Items.GRIZZLY_LOGS);
    public static final Block GRIZZLY_LOG = log("grizzly_log", GrizzlyTreeMaterial, STRIPPED_GRIZZLY_LOG, ModTags.Blocks.GRIZZLY_BLOCKS, ModTags.Items.GRIZZLY_LOGS);
    public static final Block STRIPPED_GRIZZLY_WOOD = strippedWood("stripped_grizzly_wood", GrizzlyTreeMaterial, ModTags.Blocks.GRIZZLY_BLOCKS, ModTags.Items.GRIZZLY_LOGS);
    public static final Block GRIZZLY_WOOD = wood("grizzly_wood", GrizzlyTreeMaterial, STRIPPED_GRIZZLY_WOOD, ModTags.Blocks.GRIZZLY_BLOCKS, ModTags.Items.GRIZZLY_LOGS);
    public static final Block GRIZZLY_LEAVES = ModRegistry.ofBlock("grizzly_leaves",
                    new LeavesBlock(GrizzlyLeafMaterial))
            .tag(BlockTags.LEAVES, ModTags.Blocks.GRIZZLY_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model().cutout().build();
    public static final Block GRIZZLY_PLANKS = planks("grizzly", GrizzlyTreeMaterial, ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_STAIRS = woodenStairs("grizzly", GRIZZLY_PLANKS, GrizzlyTreeMaterial, ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_SLAB = woodenSlab("grizzly", GrizzlyTreeMaterial, ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_PRESSURE_PLATE = woodenPressurePlate("grizzly", ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_BUTTON = woodenButton("grizzly", ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_FENCE = woodenFence("grizzly", GrizzlyTreeMaterial, ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_FENCE_GATE = woodenFenceGate("grizzly", GrizzlyTreeMaterial, GRIZZLY, ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_DOOR = woodenDoor("grizzly", ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_TRAPDOOR = woodenTrapdoor("grizzly", ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block GRIZZLY_SAPLING = sapling("grizzly", new GrizzlySaplingGenerator(), ModTags.Blocks.GRIZZLY_BLOCKS);
    public static final Block STRIPPED_TEALIVE_LOG = strippedLog("stripped_tealive_log", TealiveTreeMaterial, ModTags.Blocks.TEALIVE_BLOCKS, ModTags.Items.TEALIVE_LOGS);
    public static final Block TEALIVE_LOG = log("tealive_log", TealiveTreeMaterial, STRIPPED_TEALIVE_LOG, ModTags.Blocks.TEALIVE_BLOCKS, ModTags.Items.TEALIVE_LOGS);
    public static final Block STRIPPED_TEALIVE_WOOD = strippedWood("stripped_tealive_wood", TealiveTreeMaterial, ModTags.Blocks.TEALIVE_BLOCKS, ModTags.Items.TEALIVE_LOGS);
    public static final Block TEALIVE_WOOD = wood("tealive_wood", TealiveTreeMaterial, STRIPPED_TEALIVE_WOOD, ModTags.Blocks.TEALIVE_BLOCKS, ModTags.Items.TEALIVE_LOGS);
    public static final Block TEALIVE_LEAVES = ModRegistry.ofBlock("tealive_leaves",
                    new LeavesBlock(TealiveLeafMaterial))
            .tag(BlockTags.LEAVES, ModTags.Blocks.TEALIVE_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model().cutout().build();
    public static final Block TEALIVE_PLANKS = planks("tealive", TealiveTreeMaterial, ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_STAIRS = woodenStairs("tealive", TEALIVE_PLANKS, TealiveTreeMaterial, ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_SLAB = woodenSlab("tealive", TealiveTreeMaterial, ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_PRESSURE_PLATE = woodenPressurePlate("tealive", ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_BUTTON = woodenButton("tealive", ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_FENCE = woodenFence("tealive", TealiveTreeMaterial, ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_FENCE_GATE = woodenFenceGate("tealive", TealiveTreeMaterial, TEALIVE, ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_DOOR = woodenDoor("tealive", ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_TRAPDOOR = woodenTrapdoor("tealive", ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block TEALIVE_SAPLING = sapling("tealive", new TealiveSaplingGenerator(), ModTags.Blocks.TEALIVE_BLOCKS);
    public static final Block STRIPPED_BURLYWOOD_LOG = strippedLog("stripped_burlywood_log", BurlywoodTreeMaterial, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Items.BURLYWOOD_LOGS);
    public static final Block BURLYWOOD_LOG = log("burlywood_log", BurlywoodTreeMaterial, STRIPPED_BURLYWOOD_LOG, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Items.BURLYWOOD_LOGS);
    public static final Block STRIPPED_BURLYWOOD_WOOD = strippedWood("stripped_burlywood_wood", BurlywoodTreeMaterial, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Items.BURLYWOOD_LOGS);
    public static final Block BURLYWOOD_WOOD = wood("burlywood_wood", BurlywoodTreeMaterial, STRIPPED_BURLYWOOD_WOOD, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Items.BURLYWOOD_LOGS);
    public static final Block BURLYWOOD_HEART = ModRegistry.ofBlock("burlywood_heart",
                    new BurlywoodHeartBlock(copy(BurlywoodTreeMaterial).randomTicks().lightLevel((blockState) -> 12).emissiveRendering((state, blockView, pos) ->
                            !state.getValue(BurlywoodHeartBlock.PERSISTENT)
                                    && (blockView.getBlockState(pos.north()).isAir()
                                    || blockView.getBlockState(pos.south()).isAir()
                                    || blockView.getBlockState(pos.west()).isAir()
                                    || blockView.getBlockState(pos.east()).isAir()))))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.BURLYWOOD_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.BURLYWOOD_LOGS)
            .flammable(20, 1).fuel(600).model().build();
    public static final Block BURLYWOOD_LEAVES = ModRegistry.ofBlock("burlywood_leaves",
                    new LeafDroppingLeaveBlock(BurlywoodLeafMaterial, new LeafParticleEffect(0.5f, 0.5f, 0.1f)))
            .tag(BlockTags.LEAVES, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Blocks.HANGING_BURLYWOOD_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .cutout().build();
    public static final Block HANGING_BURLYWOOD_LEAVES = ModRegistry.ofBlock("hanging_burlywood_leaves",
                    new HangingFruitBlock(copy(BurlywoodLeafMaterial).noCollission().lightLevel(lightLevelFromBlockStateR(8, HangingFruitBlock.HAS_FRUIT)).instabreak(),
                            () -> ModItems.GLOW_APPLE,
                            ModTags.Blocks.HANGING_BURLYWOOD_LEAVES_GROWABLE_ON,
                            Block.box(4, 0, 4, 12, 16, 12)))
            .tag(BlockTags.LEAVES, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Blocks.HANGING_BURLYWOOD_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .cutout().build();
    public static final Block GLOWROOT_VINE = ModRegistry.ofBlock("glowroot_vine",
                    new FruitingVinesHeadBlock(vineSettings(MapColor.TERRACOTTA_GREEN, SoundType.CAVE_VINES),
                            () -> ModBlocks.GLOWROOT_VINE_PLANT, () -> ModItems.GLOWROOT_BULB, ModTags.Blocks.GLOWROOT_VINES_GROWABLE_ON), false)
            .tag(BlockTags.CAVE_VINES, ModTags.Blocks.GLOWROOT_VINES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .cutout().build();
    public static final Block GLOWROOT_VINE_PLANT = ModRegistry.ofBlock("glowroot_vine_plant",
                    new FruitingVinesBodyBlock(vineSettings(MapColor.TERRACOTTA_GREEN, SoundType.CAVE_VINES),
                            () -> ModBlocks.GLOWROOT_VINE, () -> ModItems.GLOWROOT_BULB, ModTags.Blocks.GLOWROOT_VINES_GROWABLE_ON), false)
            .tag(BlockTags.CAVE_VINES, ModTags.Blocks.GLOWROOT_VINES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .cutout().build();
    public static final Block BURLYWOOD_PLANKS = planks("burlywood", BurlywoodTreeMaterial, ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_STAIRS = woodenStairs("burlywood", BURLYWOOD_PLANKS, BurlywoodTreeMaterial, ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_SLAB = woodenSlab("burlywood", BurlywoodTreeMaterial, ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_PRESSURE_PLATE = woodenPressurePlate("burlywood", ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_BUTTON = woodenButton("burlywood", ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_FENCE = woodenFence("burlywood", BurlywoodTreeMaterial, ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_FENCE_GATE = woodenFenceGate("burlywood", BurlywoodTreeMaterial, BURLYWOOD, ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_DOOR = woodenDoor("burlywood", ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_TRAPDOOR = woodenTrapdoor("burlywood", ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block BURLYWOOD_SAPLING = sapling("burlywood", new BurlywoodSaplingGenerator(), ModTags.Blocks.BURLYWOOD_BLOCKS);
    public static final Block STRIPPED_CRIMSONVEIL_LOG = strippedLog("stripped_crimsonveil_log", CrimsonveilTreeMaterial, ModTags.Blocks.CRIMSONVEIL_BLOCKS, ModTags.Items.CRIMSONVEIL_LOGS);
    public static final Block CRIMSONVEIL_LOG = log("crimsonveil_log", CrimsonveilTreeMaterial, STRIPPED_CRIMSONVEIL_LOG, ModTags.Blocks.CRIMSONVEIL_BLOCKS, ModTags.Items.CRIMSONVEIL_LOGS);
    public static final Block STRIPPED_CRIMSONVEIL_WOOD = strippedWood("stripped_crimsonveil_wood", CrimsonveilTreeMaterial, ModTags.Blocks.CRIMSONVEIL_BLOCKS, ModTags.Items.CRIMSONVEIL_LOGS);
    public static final Block CRIMSONVEIL_WOOD = wood("crimsonveil_wood", CrimsonveilTreeMaterial, STRIPPED_CRIMSONVEIL_WOOD, ModTags.Blocks.CRIMSONVEIL_BLOCKS, ModTags.Items.CRIMSONVEIL_LOGS);
    public static final Block CRIMSONVEIL_LEAVES = ModRegistry.ofBlock("crimsonveil_leaves",
                    new LeavesBlock(CrimsonveilLeafMaterial))
            .tag(BlockTags.LEAVES, ModTags.Blocks.CRIMSONVEIL_BLOCKS, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model().cutout().build();
    public static final Block CRIMSONVEIL_VINE = ModRegistry.ofBlock("crimsonveil_vine",
                    new FruitingVinesHeadBlock(vineSettings(MapColor.CRIMSON_HYPHAE, SoundType.FLOWERING_AZALEA),
                            () -> ModBlocks.CRIMSONVEIL_VINE_PLANT, () -> ModItems.WARPEDVEIL_VINE_FRUIT, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON), false)
            .tag(BlockTags.LEAVES, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .cutout().build();
    public static final Block CRIMSONVEIL_VINE_PLANT = ModRegistry.ofBlock("crimsonveil_vine_plant",
                    new FruitingVinesBodyBlock(vineSettings(MapColor.CRIMSON_HYPHAE, SoundType.FLOWERING_AZALEA),
                            () -> ModBlocks.CRIMSONVEIL_VINE, () -> ModItems.WARPEDVEIL_VINE_FRUIT, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON), false)
            .tag(BlockTags.LEAVES, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .cutout().build();
    public static final Block CRIMSONVEIL_PLANKS = planks("crimsonveil", CrimsonveilTreeMaterial, ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_STAIRS = woodenStairs("crimsonveil", CRIMSONVEIL_PLANKS, CrimsonveilTreeMaterial, ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_SLAB = woodenSlab("crimsonveil", CrimsonveilTreeMaterial, ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_PRESSURE_PLATE = woodenPressurePlate("crimsonveil", ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_BUTTON = woodenButton("crimsonveil", ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_FENCE = woodenFence("crimsonveil", CrimsonveilTreeMaterial, ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_FENCE_GATE = woodenFenceGate("crimsonveil", CrimsonveilTreeMaterial, CRIMSONVEIL, ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_DOOR = woodenDoor("crimsonveil", ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_TRAPDOOR = woodenTrapdoor("crimsonveil", ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block CRIMSONVEIL_SAPLING = sapling("crimsonveil", new CrimsonveilSaplingGenerator(), ModTags.Blocks.CRIMSONVEIL_BLOCKS);
    public static final Block BEARFRUIT_BRAMBLE = ModRegistry.ofBlock("bearfruit_bramble_stem",
                    new StandingFruitBlock(copy(Blocks.OAK_SAPLING).mapColor(MapColor.COLOR_PURPLE).instabreak()
                            .lightLevel(StandingFruitBlock.STATE_TO_LUMINANCE),
                            () -> ModItems.BEARFRUIT_BRAMBLE, ModTags.Blocks.BEARFRUIT_BRAMBLE_GROWABLE_ON, Block.box(
                            6, 0, 6, 10, 16, 10
                    )))
            .cutout().tag(ModTags.Blocks.BEARFRUIT_BRAMBLE_GROWABLE_ON, BlockTags.CLIMBABLE).tagitem(ItemTags.FLOWERS).drop_shears(() -> ModItems.BEARFRUIT_BRAMBLE).build();
    public static final Block RIME_ICE = ModRegistry.ofBlock("rime_ice",
                    new Block(copy(Blocks.BLUE_ICE).noOcclusion())).tool("_pickaxe")
            .drop_silk(Items.SNOWBALL).translucent().model().build();
    public static final Block HOARFROST = ModRegistry.ofBlock("hoarfrost",
                    new HoarfrostBlock(FabricBlockSettings.create().mapColor(MapColor.SNOW).replaceable().strength(0.1F)
                            .speedFactor(0.6F).sound(SoundType.POWDER_SNOW).randomTicks().noOcclusion().pushReaction(PushReaction.DESTROY)))
            .tool("_shovel").drop_silk(Items.SNOWBALL).tag(BlockTags.DAMPENS_VIBRATIONS).cutout().build();
    public static final Block MIDNIGHT_LILY_PAD = ModRegistry.ofBlock("midnight_lily",
            new MidnightLilyPadBlock(FabricBlockSettings.create().mapColor(MapColor.DIAMOND).instabreak().sound(SoundType.LILY_PAD).noOcclusion().pushReaction(PushReaction.DESTROY).lightLevel((blockState) -> 8))).drop_shears().cutout().build();
    public static final Block AMETHYST_PETALS = ModRegistry.ofBlock("amethyst_petals",
            new PinkPetalsBlock(FabricBlockSettings.create().mapColor(MapColor.ICE).instabreak().noCollission().sound(SoundType.SMALL_AMETHYST_BUD)
                    .pushReaction(PushReaction.DESTROY).lightLevel(lightLevelFromBlockState(8, BlockStateProperties.FLOWER_AMOUNT, 2)))).cutout().build();
    public static final Block GLOW_APPLE_BUSH = ModRegistry.ofBlock("glow_apple_bush",
            new TreeBushLikeBlock(FabricBlockSettings.create().mapColor(MapColor.GRASS).destroyTime(0.25f).sound(SoundType.SMALL_DRIPLEAF)
                    .pushReaction(PushReaction.DESTROY).lightLevel(lightLevelFromBlockStateR(10, TreeBushLikeBlock.GROWN)).randomTicks())).cutout().build();
    public static final Block BURLYWOOD_VIOLET = ModRegistry.ofBlock("burlywood_violet",
            new FlowerBlock(MobEffects.GLOWING, 100, FabricBlockSettings.create().mapColor(MapColor.ICE).instabreak().sound(SoundType.FLOWERING_AZALEA)
                    .lightLevel((blockState) -> 10).noCollission())).drop().cutout().model(ModRegistry.Models.CROSS).build();
    public static final Block FROSTBLOOM = ModRegistry.ofBlock("frostbloom",
            new FrostbloomBlock(MobEffects.WEAKNESS, 100, FabricBlockSettings.create().mapColor(MapColor.ICE).instabreak().sound(SoundType.GLASS)
                    .lightLevel((blockState) -> 6).noCollission())).drop().cutout().model(ModRegistry.Models.CROSS).build();
    public static final Block CORONA_BLOOM = ModRegistry.ofBlock("corona_bloom",
            new FlowerBlock(MobEffects.FIRE_RESISTANCE, 200, FabricBlockSettings.create().mapColor(MapColor.FIRE).instabreak().sound(SoundType.NETHER_WART)
                    .lightLevel((blockState) -> 12).emissiveRendering((blockState, blockGetter, blockPos) -> blockGetter.getBlockState(blockPos.above()).isAir()).noCollission())).drop().cutout().model(ModRegistry.Models.CROSS).build();
    public static final Block ASHREED = ModRegistry.ofBlock("ashreed",
            new ReedBlock(copy(Blocks.SUGAR_CANE))).drop().cutout().build();
    public static final Block UMBRAL_KELP = ModRegistry.ofBlock("umbral_kelp",
            new UmbralKelpBlock(FabricBlockSettings.create().mapColor(MapColor.COLOR_PURPLE).instabreak().sound(SoundType.WET_GRASS)
                    .pushReaction(PushReaction.DESTROY).noCollission().lightLevel((blockState) -> 8))).drop().cutout().build();
    public static final Block UMBRAL_KELP_PLANT = ModRegistry.ofBlock("umbral_kelp_plant",
            new UmbralKelpPlantBlock(FabricBlockSettings.create().mapColor(MapColor.COLOR_PURPLE).instabreak().sound(SoundType.WET_GRASS)
                    .pushReaction(PushReaction.DESTROY).noCollission()), false).drop(UMBRAL_KELP).cutout().build();
    public static final Block DEAD_UMBRAL_CORAL_BLOCK = ModRegistry.ofBlock("dead_umbral_coral_block",
                    new Block(copy(Blocks.DEAD_BRAIN_CORAL_BLOCK)))
            .tool("_pickaxe").model().drop().build();
    public static final Block DEAD_UMBRAL_CORAL = ModRegistry.ofBlock("dead_umbral_coral",
                    new ModBaseCoralPlantBlock(copy(Blocks.DEAD_BRAIN_CORAL)))
            .tool("_pickaxe").model(ModRegistry.Models.CROSS).cutout().build();
    public static final Block DEAD_UMBRAL_CORAL_FAN = ModRegistry.ofBlock("dead_umbral_coral_fan",
                    new ModBaseCoralFanBlock(copy(Blocks.DEAD_BRAIN_CORAL_FAN)), false)
            .tool("_pickaxe").cutout().build();
    public static final Block DEAD_UMBRAL_CORAL_WALL_FAN = ModRegistry.ofBlock("dead_umbral_coral_wall_fan",
                    new ModBaseCoralWallFanBlock(copy(Blocks.DEAD_BRAIN_CORAL_WALL_FAN).dropsLike(DEAD_UMBRAL_CORAL_FAN)), false)
            .tool("_pickaxe").cutout().build();
    public static final Block UMBRAL_CORAL_BLOCK = ModRegistry.ofBlock("umbral_coral_block",
                    new ModCoralBlock(DEAD_UMBRAL_CORAL_BLOCK, copy(Blocks.BRAIN_CORAL_BLOCK).mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> 6)))
            .tool("_pickaxe").model().build();
    public static final Block UMBRAL_CORAL = ModRegistry.ofBlock("umbral_coral",
                    new ModCoralPlantBlock(DEAD_UMBRAL_CORAL, copy(Blocks.BRAIN_CORAL).mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> 6)))
            .model(ModRegistry.Models.CROSS).drop_silk(DEAD_UMBRAL_CORAL_BLOCK).cutout().build();
    public static final Block UMBRAL_CORAL_FAN = ModRegistry.ofBlock("umbral_coral_fan",
                    new ModCoralFanBlock(DEAD_UMBRAL_CORAL_FAN, copy(Blocks.BRAIN_CORAL_FAN).mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> 6)), false)
            .cutout().build();
    public static final Block UMBRAL_CORAL_WALL_FAN = ModRegistry.ofBlock("umbral_coral_wall_fan",
                    new ModCoralWallFanBlock(DEAD_UMBRAL_CORAL_WALL_FAN, copy(Blocks.BRAIN_CORAL_WALL_FAN).mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> 6).dropsLike(UMBRAL_CORAL_FAN)), false)
            .cutout().build();
    public static final Block GLOOMWEED = ModRegistry.ofBlock("gloomweed",
            new DarknessFernBlock(FabricBlockSettings.create().replaceable().instabreak().sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY).noCollission(), false)).drop_shears().tool("_hoe").cutout().build();
    public static final Block TALL_GLOOMWEED = ModRegistry.ofBlock("tall_gloomweed",
            new DoublePlantBlock(FabricBlockSettings.create().replaceable().instabreak().sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY).noCollission())).tool("_hoe").drop_tall_shears(GLOOMWEED).cutout().build();
    public static final Block MURKTUFT = ModRegistry.ofBlock("murktuft",
            new DarknessFernBlock(FabricBlockSettings.create().replaceable().instabreak().randomTicks().sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY).noCollission().lightLevel(lightLevelFromBlockStateR(8, DarknessFernBlock.LIT))
                    .emissiveRendering((state, world, pos) -> state.getValue(DarknessFernBlock.LIT)), true)).drop_shears().tool("_hoe").model(ModRegistry.Models.CROSS).cutout().build();
    public static final Block GRIPPING_DARK_CURRANTSLATE = ModRegistry.ofBlock("gripping_dark_currantslate",
                    new GrippingStoneBlock(FabricBlockSettings.create().destroyTime(3).dropsLike(DARK_CURRANTSLATE)))
            .tag(BlockTags.DAMPENS_VIBRATIONS).tool("stone_pickaxe").build();
    public static final Block GRIPPING_GLACIEMITE = ModRegistry.ofBlock("gripping_glaciemite",
                    new GrippingStoneBlock(FabricBlockSettings.create().destroyTime(2.5f).dropsLike(GLACIEMITE)))
            .tag(BlockTags.DAMPENS_VIBRATIONS).tool("stone_pickaxe").build();
    public static final Block GRIPPING_CORONA_STONE = ModRegistry.ofBlock("gripping_corona_stone",
                    new GrippingStoneBlock(FabricBlockSettings.create().destroyTime(2f).dropsLike(CORONA_STONE)))
            .tag(BlockTags.DAMPENS_VIBRATIONS).tool("stone_pickaxe").build();
    public static final Block GRIPPING_SPIGELIA = ModRegistry.ofBlock("gripping_spigelia",
                    new GrippingSpigeliaBlock(copy(Blocks.WARPED_FUNGUS).randomTicks()
                            .lightLevel(lightLevelFromBlockState(2, BlockStateProperties.AGE_2, 2)).emissiveRendering((state, world, pos) ->
                                    world.getBlockState(pos.below()).isAir() || world.getBlockState(pos.above()).isAir()).jumpFactor(0.85f)))
            .tag(BlockTags.DAMPENS_VIBRATIONS).tool("stone_pickaxe").cutout().drop().build();
    public static final Block GRIPCRYSTAL_WART = ModRegistry.ofBlock("gripcrystal_wart",
            new GripcrystalWartBlock(FabricBlockSettings.create().replaceable().instabreak().lightLevel((blockState) -> 10).sound(SoundType.LARGE_AMETHYST_BUD)
                    .pushReaction(PushReaction.DESTROY).randomTicks().noCollission())).tool("stone_pickaxe").drop_silk(() -> ModItems.GRANULATED_GRIPCRYSTAL).cutout().build();
    public static final Block GRIPTONITED_DARK_CURRANTSLATE = ModRegistry.ofBlock("griptonited_dark_currantslate",
                    new GrippingStoneBlock(FabricBlockSettings.create().destroyTime(3).emissiveRendering(IS_NEXT_TO_GRIPTONITE_CLUSTER).dropsLike(DARK_CURRANTSLATE)))
            .tag(BlockTags.DAMPENS_VIBRATIONS).tool("stone_pickaxe").build();
    public static final Block GRIPTONITED_GLACIEMITE = ModRegistry.ofBlock("griptonited_glaciemite",
                    new GrippingStoneBlock(FabricBlockSettings.create().destroyTime(2.5f).emissiveRendering(IS_NEXT_TO_GRIPTONITE_CLUSTER).dropsLike(GLACIEMITE)))
            .tag(BlockTags.DAMPENS_VIBRATIONS).tool("stone_pickaxe").build();
    public static final Block GRIPTONITED_CORONA_STONE = ModRegistry.ofBlock("griptonited_corona_stone",
                    new GrippingStoneBlock(FabricBlockSettings.create().destroyTime(2f).emissiveRendering(IS_NEXT_TO_GRIPTONITE_CLUSTER).dropsLike(CORONA_STONE)))
            .tag(BlockTags.DAMPENS_VIBRATIONS).tool("stone_pickaxe").build();
    public static final Block GRIPTONITE_CLUSTER = ModRegistry.ofBlock("griptonite_cluster",
            new AmethystClusterBlock(10, 4, FabricBlockSettings.create().replaceable().destroyTime(2).lightLevel((blockState) -> 4).sound(SoundType.MEDIUM_AMETHYST_BUD)
                    .pushReaction(PushReaction.DESTROY).emissiveRendering(BlockBehaviour.BlockStateBase::hasPostProcess).requiresCorrectToolForDrops())).tool("iron_pickaxe").drop_silk(() -> ModItems.GRANULATED_GRIPTONITE).cutout().build();
    public static final Block DARK_WATER = ModRegistry.ofBlock("dark_water",
                    new DarkWaterBlock(ModFluids.DARK_WATER,
                            FabricBlockSettings.create()
                                    .mapColor(MapColor.COLOR_BLACK)
                                    .replaceable()
                                    .noCollission()
                                    .strength(150.0F)
                                    .pushReaction(PushReaction.DESTROY)
                                    .noLootTable()
                                    .liquid()
                                    .sound(SoundType.EMPTY)))
            .model(ModRegistry.Models.FLUID).build();

    private static Block dungeonStone(String id, Block block) {
        return ModRegistry.ofBlock(id, block)
                .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
                .drop().build();
    }

    private static Block dungeonStoneWithModel(String id, Block block) {
        return ModRegistry.ofBlock(id, block)
                .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
                .drop().model().build();
    }

    private static Block dungeonStairs(String id, Block base, BlockBehaviour.Properties material) {
        return dungeonStone(id, new StairBlock(base.defaultBlockState(), material));
    }

    private static Block dungeonSlab(String id, BlockBehaviour.Properties material) {
        return dungeonStone(id, new SlabBlock(material));
    }

    private static Block dungeonWall(String id, BlockBehaviour.Properties material) {
        return ModRegistry.ofBlock(id, new WallBlock(material))
                .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.WALLS).tool("stone_pickaxe")
                .drop().build();
    }

    private static Block chest(String id, float hardness, float resistance, SoundType sound, ModChestBlock.ChestTypes type) {
        return ModRegistry.ofBlock(id, new ModChestBlock(copy(Blocks.CHEST).strength(hardness, resistance).sound(sound), type))
                .tag(ConventionalBlockTags.CHESTS).drop().build();
    }

    private static Block ore(String id, float hardness, int light, int minXp, int maxXp, String tool) {
        return ModRegistry.ofBlock(id, new DropExperienceBlock(FabricBlockSettings.create().destroyTime(hardness)
                        .requiresCorrectToolForDrops().lightLevel(state -> light), UniformInt.of(minXp, maxXp)))
                .model().tool(tool).build();
    }

    private static Block storageBlock(String id, float hardness) {
        return ModRegistry.ofBlock(id, new Block(FabricBlockSettings.create().destroyTime(hardness).requiresCorrectToolForDrops()))
                .tag(BlockTags.BEACON_BASE_BLOCKS).model().drop().tool("diamond_pickaxe").build();
    }

    private static Block grass(String id, MapColor color, SoundType sound) {
        return ModRegistry.ofBlock(id, new DarknessGrassBlock(FabricBlockSettings.create().destroyTime(1).mapColor(color).sound(sound)))
                .tool("_shovel").model(ModRegistry.Models.GRASS).tag(BlockTags.DIRT, BlockTags.VALID_SPAWN).build();
    }

    private static Block strippedLog(String id, BlockBehaviour.Properties material, TagKey<Block> family, TagKey<Item> logs) {
        return ModRegistry.ofBlock(id, new RotatedPillarBlock(material))
                .tool("_axe")
                .tag(BlockTags.LOGS_THAT_BURN, family).tagitem(ItemTags.LOGS_THAT_BURN, logs)
                .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();
    }

    private static Block log(String id, BlockBehaviour.Properties material, Block stripped, TagKey<Block> family, TagKey<Item> logs) {
        return ModRegistry.ofBlock(id, new RotatedPillarBlock(material))
                .tool("_axe").strip(stripped)
                .tag(BlockTags.LOGS_THAT_BURN, family).tagitem(ItemTags.LOGS_THAT_BURN, logs)
                .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();
    }

    private static Block strippedWood(String id, BlockBehaviour.Properties material, TagKey<Block> family, TagKey<Item> logs) {
        return ModRegistry.ofBlock(id, new RotatedPillarBlock(material))
                .tool("_axe")
                .tag(BlockTags.LOGS_THAT_BURN, family).tagitem(ItemTags.LOGS_THAT_BURN, logs)
                .flammable(5, 5).fuel(300).drop().model().build();
    }

    private static Block wood(String id, BlockBehaviour.Properties material, Block stripped, TagKey<Block> family, TagKey<Item> logs) {
        return ModRegistry.ofBlock(id, new RotatedPillarBlock(material))
                .tool("_axe").strip(stripped)
                .tag(BlockTags.LOGS_THAT_BURN, family).tagitem(ItemTags.LOGS_THAT_BURN, logs)
                .flammable(5, 5).fuel(300).drop().model().build();
    }

    private static Block planks(String wood, BlockBehaviour.Properties material, TagKey<Block> family) {
        return ModRegistry.ofBlock(wood + "_planks", new Block(material))
                .tool("_axe")
                .tag(BlockTags.PLANKS, family).tagitem(ItemTags.PLANKS)
                .flammable(5, 20).fuel(300).drop().build();
    }

    private static Block woodenStairs(String wood, Block planks, BlockBehaviour.Properties material, TagKey<Block> family) {
        return ModRegistry.ofBlock(wood + "_stairs", new StairBlock(planks.defaultBlockState(), material))
                .tool("_axe")
                .tag(BlockTags.WOODEN_STAIRS, family).tagitem(ItemTags.WOODEN_STAIRS)
                .flammable(5, 20).fuel(300).drop().build();
    }

    private static Block woodenSlab(String wood, BlockBehaviour.Properties material, TagKey<Block> family) {
        return ModRegistry.ofBlock(wood + "_slab", new SlabBlock(material))
                .tool("_axe")
                .tag(BlockTags.WOODEN_SLABS, family).tagitem(ItemTags.WOODEN_SLABS)
                .flammable(5, 20).fuel(300).drop().build();
    }

    private static Block woodenPressurePlate(String wood, TagKey<Block> family) {
        return ModRegistry.ofBlock(wood + "_pressure_plate", new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, PassableTreeMaterial, BlockSetType.OAK))
                .tool("_axe")
                .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.WALL_POST_OVERRIDE, family)
                .tagitem(ItemTags.WOODEN_PRESSURE_PLATES)
                .fuel(300).drop().build();
    }

    private static Block woodenButton(String wood, TagKey<Block> family) {
        return ModRegistry.ofBlock(wood + "_button", new ButtonBlock(PassableTreeMaterial, BlockSetType.OAK, 10, true))
                .tool("_axe")
                .tag(BlockTags.WOODEN_BUTTONS, family).tagitem(ItemTags.WOODEN_BUTTONS)
                .fuel(100).drop().build();
    }

    private static Block woodenFence(String wood, BlockBehaviour.Properties material, TagKey<Block> family) {
        return ModRegistry.ofBlock(wood + "_fence", new FenceBlock(material))
                .tool("_axe")
                .tag(BlockTags.WOODEN_FENCES, family).tagitem(ItemTags.WOODEN_FENCES)
                .flammable(5, 5).fuel(300).drop().build();
    }

    private static Block woodenFenceGate(String wood, BlockBehaviour.Properties material, WoodType type, TagKey<Block> family) {
        return ModRegistry.ofBlock(wood + "_fence_gate", new FenceGateBlock(material, type))
                .tool("_axe")
                .tag(BlockTags.WOODEN_FENCES, BlockTags.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, family)
                .tagitem(ItemTags.WOODEN_FENCES, ItemTags.FENCE_GATES)
                .flammable(5, 5).fuel(300).drop().build();
    }

    private static Block woodenDoor(String wood, TagKey<Block> family) {
        return ModRegistry.ofBlock(wood + "_door", new DoorBlock(copy(Blocks.OAK_DOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
                .tool("_axe")
                .tag(BlockTags.WOODEN_DOORS, family).tagitem(ItemTags.WOODEN_DOORS)
                .model(ModRegistry.Models.DOOR).cutout()
                .drop().build();
    }

    private static Block woodenTrapdoor(String wood, TagKey<Block> family) {
        return ModRegistry.ofBlock(wood + "_trapdoor", new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
                .tool("_axe")
                .tag(BlockTags.WOODEN_TRAPDOORS, family).tagitem(ItemTags.WOODEN_TRAPDOORS)
                .model(ModRegistry.Models.TRAPDOOR).cutout()
                .drop().build();
    }

    private static Block sapling(String wood, AbstractTreeGrower grower, TagKey<Block> family) {
        return ModRegistry.ofBlock(wood + "_sapling", new SaplingBlock(grower, copy(Blocks.OAK_SAPLING).mapColor(MapColor.COLOR_PURPLE).instabreak()))
                .model(ModRegistry.Models.CROSS).cutout()
                .tag(BlockTags.SAPLINGS, family).tagitem(ItemTags.SAPLINGS)
                .fuel(100).drop().build();
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
        return FabricBlockSettings.create().mapColor(color)
                .randomTicks()
                .noCollission()
                .lightLevel(ModPlantVines.getLuminanceSupplier(12))
                .instabreak()
                .sound(sound)
                .pushReaction(PushReaction.DESTROY);
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