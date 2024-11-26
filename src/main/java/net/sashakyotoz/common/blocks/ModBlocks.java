package net.sashakyotoz.common.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.particles.custom.effects.LeafParticleEffect;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.custom.*;
import net.sashakyotoz.common.blocks.custom.plants.*;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.features.trees.*;

import java.util.Map;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static void register() {
        UnseenWorld.log("Registering Blocks for modid : " + UnseenWorld.MOD_ID);
        ModRegistry.BlockBuilder.registerBricksSet(DARK_CURRANTSLATE, DARK_CURRANTSLATE_STAIRS, DARK_CURRANTSLATE_SLAB, DARK_CURRANTSLATE_WALL);
        ModRegistry.BlockBuilder.registerBricksSet(DARK_CURRANTSLATE_BRICKS, DARK_CURRANTSLATE_BRICKS_STAIRS, DARK_CURRANTSLATE_BRICKS_SLAB, DARK_CURRANTSLATE_BRICKS_WALL);
        ModRegistry.BlockBuilder.registerBricksSet(GLACIEMITE, GLACIEMITE_STAIRS, GLACIEMITE_SLAB, GLACIEMITE_WALL);
        ModRegistry.BlockBuilder.registerBricksSet(GLACIEMITE_BRICKS, GLACIEMITE_BRICKS_STAIRS, GLACIEMITE_BRICKS_SLAB, GLACIEMITE_BRICKS_WALL);
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

    private static FabricBlockSettings copy(FabricBlockSettings original) {
        return FabricBlockSettings.copyOf(original);
    }

    private static FabricBlockSettings copy(Block original) {
        return FabricBlockSettings.copyOf(original);
    }

    private static final FabricBlockSettings DarkCurrantslateMaterial =
            FabricBlockSettings.create().hardness(3).luminance(2).instrument(Instrument.BIT).mapColor(MapColor.DARK_CRIMSON).sounds(BlockSoundGroup.GILDED_BLACKSTONE);
    private static final FabricBlockSettings GlaciemiteMaterial =
            FabricBlockSettings.create().hardness(2.5f).luminance(4).instrument(Instrument.SNARE).mapColor(MapColor.DARK_AQUA).sounds(BlockSoundGroup.DEEPSLATE);
    private static final FabricBlockSettings TanzaniteMaterial =
            FabricBlockSettings.create().hardness(2f).luminance(5).instrument(Instrument.BELL).mapColor(MapColor.TERRACOTTA_PURPLE).sounds(BlockSoundGroup.AMETHYST_BLOCK);

    private static final FabricBlockSettings PassableTreeMaterial = copy(Blocks.OAK_SIGN);
    private static final FabricBlockSettings AmethystTreeMaterial =
            FabricBlockSettings.create().hardness(2.5f).luminance(3).mapColor(MapColor.PURPLE).sounds(BlockSoundGroup.AMETHYST_BLOCK);
    private static final FabricBlockSettings AmethystLeafMaterial = FabricBlockSettings.create().sounds(BlockSoundGroup.AZALEA_LEAVES).nonOpaque().breakInstantly().ticksRandomly();
    private static final FabricBlockSettings BurlywoodTreeMaterial =
            FabricBlockSettings.create().hardness(3f).luminance(3).mapColor(MapColor.DARK_GREEN).sounds(BlockSoundGroup.BAMBOO_WOOD);
    private static final FabricBlockSettings BurlywoodLeafMaterial = FabricBlockSettings.create().luminance(3).mapColor(MapColor.DARK_GREEN).sounds(BlockSoundGroup.BAMBOO_WOOD).nonOpaque().breakInstantly().ticksRandomly();
    private static final FabricBlockSettings TealiveTreeMaterial =
            FabricBlockSettings.create().hardness(1.5f).luminance(5).mapColor(MapColor.CYAN).sounds(BlockSoundGroup.NETHER_WOOD);
    private static final FabricBlockSettings TealiveLeafMaterial = FabricBlockSettings.create().mapColor(MapColor.CYAN).sounds(BlockSoundGroup.AZALEA_LEAVES).nonOpaque().breakInstantly().ticksRandomly();
    private static final FabricBlockSettings CrimsonveilTreeMaterial =
            FabricBlockSettings.create().hardness(2f).luminance(2).mapColor(MapColor.TERRACOTTA_ORANGE).sounds(BlockSoundGroup.WOOD);
    private static final FabricBlockSettings CrimsonveilLeafMaterial = FabricBlockSettings.create().hardness(2f).luminance(2).mapColor(MapColor.TERRACOTTA_ORANGE).sounds(BlockSoundGroup.CHERRY_LEAVES).nonOpaque().breakInstantly().ticksRandomly();
    private static final FabricBlockSettings GrizzlyTreeMaterial =
            FabricBlockSettings.create().hardness(3.5f).mapColor(MapColor.GRAY).sounds(BlockSoundGroup.NETHER_WOOD);
    private static final FabricBlockSettings GrizzlyLeafMaterial = FabricBlockSettings.create().hardness(3.5f).mapColor(MapColor.GRAY).sounds(BlockSoundGroup.CHERRY_LEAVES).nonOpaque().breakInstantly();

    public static final WoodType GRIZZLY = new WoodTypeBuilder().register(UnseenWorld.makeID("grizzly"), BlockSetType.DARK_OAK);
    public static final WoodType AMETHYST = new WoodTypeBuilder().register(UnseenWorld.makeID("amethyst"), BlockSetType.BIRCH);
    public static final WoodType BURLYWOOD = new WoodTypeBuilder().register(UnseenWorld.makeID("burlywood"), BlockSetType.MANGROVE);
    public static final WoodType TEALIVE = new WoodTypeBuilder().register(UnseenWorld.makeID("tealive"), BlockSetType.JUNGLE);
    public static final WoodType CRIMSONVEIL = new WoodTypeBuilder().register(UnseenWorld.makeID("crimsonveil"), BlockSetType.CRIMSON);
    //stones
    public static final Block DARK_FROST_BRICKS = ModRegistry.ofBlock("dark_frost_bricks",
                    new Block(copy(Blocks.POLISHED_BLACKSTONE_BRICKS)))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().model().build();
    public static final Block KEY_HANDLER_STONE = ModRegistry.ofBlock("key_handler_stone",
                    new KeyHandlerStoneBlock(copy(Blocks.POLISHED_BLACKSTONE_BRICKS).luminance(createLightLevelFromBlockState(12, Properties.LOCKED)).hardness(25)))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("iron_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE_TRANSLOCATONE = ModRegistry.ofBlock("glaciemite_translocatone",
                    new GlaciemiteTranslocatoneBlock(copy(Blocks.POLISHED_BLACKSTONE_BRICKS).hardness(25)))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("iron_pickaxe")
            .drop().translucent().build();
    public static final Block DARK_CURRANTSLATE = ModRegistry.ofBlock("dark_currantslate",
                    new Block(DarkCurrantslateMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block DARK_CURRANTSLATE_SLAB = ModRegistry.ofBlock("dark_currantslate_slab",
                    new SlabBlock(DarkCurrantslateMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block DARK_CURRANTSLATE_STAIRS = ModRegistry.ofBlock("dark_currantslate_stairs",
                    new StairsBlock(DARK_CURRANTSLATE.getDefaultState(), DarkCurrantslateMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block DARK_CURRANTSLATE_WALL = ModRegistry.ofBlock("dark_currantslate_wall",
                    new WallBlock(DarkCurrantslateMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block DARK_CURRANTSLATE_BRICKS = ModRegistry.ofBlock("dark_currantslate_bricks",
                    new Block(DarkCurrantslateMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block DARK_CURRANTSLATE_BRICKS_SLAB = ModRegistry.ofBlock("dark_currantslate_bricks_slab",
                    new SlabBlock(DarkCurrantslateMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block DARK_CURRANTSLATE_BRICKS_STAIRS = ModRegistry.ofBlock("dark_currantslate_bricks_stairs",
                    new StairsBlock(DARK_CURRANTSLATE_BRICKS.getDefaultState(), DarkCurrantslateMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block DARK_CURRANTSLATE_BRICKS_WALL = ModRegistry.ofBlock("dark_currantslate_bricks_wall",
                    new WallBlock(DarkCurrantslateMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE = ModRegistry.ofBlock("glaciemite",
                    new Block(GlaciemiteMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE_SLAB = ModRegistry.ofBlock("glaciemite_slab",
                    new SlabBlock(GlaciemiteMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE_STAIRS = ModRegistry.ofBlock("glaciemite_stairs",
                    new StairsBlock(DARK_CURRANTSLATE.getDefaultState(), GlaciemiteMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE_WALL = ModRegistry.ofBlock("glaciemite_wall",
                    new WallBlock(GlaciemiteMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE_BRICKS = ModRegistry.ofBlock("glaciemite_bricks",
                    new Block(GlaciemiteMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE_BRICKS_SLAB = ModRegistry.ofBlock("glaciemite_bricks_slab",
                    new SlabBlock(GlaciemiteMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE_BRICKS_STAIRS = ModRegistry.ofBlock("glaciemite_bricks_stairs",
                    new StairsBlock(DARK_CURRANTSLATE_BRICKS.getDefaultState(), GlaciemiteMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE_BRICKS_WALL = ModRegistry.ofBlock("glaciemite_bricks_wall",
                    new WallBlock(GlaciemiteMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE_BULB = ModRegistry.ofBlock("glaciemite_bulb",
                    new BulbLikeBlock(GlaciemiteMaterial.luminance(Blocks.createLightLevelFromLitBlockState(14))))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().model(ModRegistry.Models.BULB).build();
    public static final Block TANZANITE_BLOCK = ModRegistry.ofBlock("tanzanite_block",
                    new Block(TanzaniteMaterial)).tool("stone_pickaxe")
            .drop().model().build();
    public static final Block POLISHED_TANZANITE = ModRegistry.ofBlock("polished_tanzanite",
                    new Block(TanzaniteMaterial)).tool("stone_pickaxe")
            .drop().build();
    public static final Block POLISHED_TANZANITE_SLAB = ModRegistry.ofBlock("polished_tanzanite_slab",
                    new SlabBlock(TanzaniteMaterial)).tool("stone_pickaxe")
            .drop().build();
    public static final Block POLISHED_TANZANITE_STAIRS = ModRegistry.ofBlock("polished_tanzanite_stairs",
                    new StairsBlock(DARK_CURRANTSLATE_BRICKS.getDefaultState(), TanzaniteMaterial)).tool("stone_pickaxe")
            .drop().build();
    //ores
    public static final Block ABYSSAL_ORE_IN_DARK_CURRANTSLATE = ModRegistry.ofBlock("abyssal_ore_in_dark_currantslate",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(5).requiresTool().luminance(4), UniformIntProvider.create(1, 5))).model().tool("diamond_pickaxe").build();
    public static final Block ABYSSAL_ORE_IN_GLACIEMITE = ModRegistry.ofBlock("abyssal_ore_in_glaciemite",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(5).requiresTool().luminance(4), UniformIntProvider.create(1, 5))).model().tool("diamond_pickaxe").build();
    public static final Block ABYSSAL_INGOT_BLOCK = ModRegistry.ofBlock("abyssal_ingot_block",
            new Block(FabricBlockSettings.create().hardness(5).requiresTool())).model().drop().tool("diamond_pickaxe").build();
    public static final Block RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE = ModRegistry.ofBlock("red_titanium_ore_in_dark_currantslate",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(6).requiresTool().luminance(3), UniformIntProvider.create(1, 6))).model().tool("diamond_pickaxe").build();
    public static final Block RED_TITANIUM_IN_GLACIEMITE = ModRegistry.ofBlock("red_titanium_ore_in_glaciemite",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(6).requiresTool().luminance(3), UniformIntProvider.create(1, 6))).model().tool("diamond_pickaxe").build();
    public static final Block RED_TITANIUM_INGOT_BLOCK = ModRegistry.ofBlock("red_titanium_ingot_block",
            new Block(FabricBlockSettings.create().hardness(6).requiresTool())).model().drop().tool("diamond_pickaxe").build();
    public static final Block UNSEENIUM_ORE_IN_DARK_CURRANTSLATE = ModRegistry.ofBlock("unseenium_ore_in_dark_currantslate",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(4).requiresTool().luminance(5), UniformIntProvider.create(1, 4))).model().tool("diamond_pickaxe").build();
    public static final Block UNSEENIUM_ORE_IN_GLACIEMITE = ModRegistry.ofBlock("unseenium_ore_in_glaciemite",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(4).requiresTool().luminance(5), UniformIntProvider.create(1, 4))).model().tool("diamond_pickaxe").build();
    public static final Block UNSEENIUM_INGOT_BLOCK = ModRegistry.ofBlock("unseenium_ingot_block",
            new Block(FabricBlockSettings.create().hardness(4).requiresTool())).model().drop().tool("diamond_pickaxe").build();
    //grass
    public static final Block NIGHTDARK_GRASS_BLOCK = ModRegistry.ofBlock("nightdark_grass_block", new DarknessGrassBlock(FabricBlockSettings.create().hardness(1).mapColor(MapColor.BLACK))).tool("_shovel").model(ModRegistry.Models.GRASS).tag(BlockTags.DIRT).build();
    public static final Block TEALIVY_GRASS_BLOCK = ModRegistry.ofBlock("tealivy_grass_block", new DarknessGrassBlock(FabricBlockSettings.create().hardness(1).mapColor(MapColor.CYAN))).tool("_shovel").model(ModRegistry.Models.GRASS).tag(BlockTags.DIRT).build();
    public static final Block AMETHYST_GRASS_BLOCK = ModRegistry.ofBlock("amethyst_grass_block", new DarknessGrassBlock(FabricBlockSettings.create().hardness(1).mapColor(MapColor.PURPLE))).tool("_shovel").model(ModRegistry.Models.GRASS).tag(BlockTags.DIRT).build();
    public static final Block GRIMWOOD_GRASS_BLOCK = ModRegistry.ofBlock("grimwood_grass_block", new DarknessGrassBlock(FabricBlockSettings.create().hardness(1).mapColor(MapColor.RED))).tool("_shovel").model(ModRegistry.Models.GRASS).tag(BlockTags.DIRT).build();
    public static final Block ASHEN_OOZE = ModRegistry.ofBlock("ashen_ooze", new Block(FabricBlockSettings.create().hardness(1).mapColor(MapColor.PALE_YELLOW))).tool("_shovel").drop().model().build();
    public static final Block GLIMMERGRAIN_SAND = ModRegistry.ofBlock("glimmergrain_sand", new FallingBlock(FabricBlockSettings.create().hardness(1).mapColor(MapColor.GRAY))).tool("_shovel").drop().model().build();
    //woods
    public static final Block STRIPPED_AMETHYST_LOG = ModRegistry.ofBlock("stripped_amethyst_log",
                    new PillarBlock(AmethystTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.AMETHYST_LOGS)
            .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();
    public static final Block AMETHYST_LOG = ModRegistry.ofBlock("amethyst_log",
                    new PillarBlock(AmethystTreeMaterial))
            .tool("_axe").strip(STRIPPED_AMETHYST_LOG)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.AMETHYST_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.AMETHYST_LOGS)
            .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();

    public static final Block STRIPPED_AMETHYST_WOOD = ModRegistry.ofBlock("stripped_amethyst_wood",
                    new PillarBlock(AmethystTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.AMETHYST_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.AMETHYST_LOGS)
            .flammable(5, 5).fuel(300).drop().model().build();
    public static final Block AMETHYST_WOOD = ModRegistry.ofBlock("amethyst_wood",
                    new PillarBlock(AmethystTreeMaterial))
            .tool("_axe").strip(STRIPPED_AMETHYST_WOOD)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.AMETHYST_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.AMETHYST_LOGS)
            .flammable(5, 5).fuel(300).drop().model().build();

    public static final Block AMETHYST_LEAVES = ModRegistry.ofBlock("amethyst_leaves",
                    new LeafDroppingLeaveBlock(AmethystLeafMaterial, new LeafParticleEffect(0.5f, 0.15f, 0.75f)))
            .tag(BlockTags.LEAVES, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model().cutout().build();
    public static final Block HANGING_AMETHYST_LEAVES = ModRegistry.ofBlock("hanging_amethyst_leaves",
                    new HangingFruitBlock(copy(AmethystLeafMaterial).collidable(false).breakInstantly(),
                            () -> ModItems.CRYSTIE_APPLE,
                            ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON,
                            Block.createCuboidShape(0, 0, 0, 16, 16, 16)))
            .tag(BlockTags.LEAVES, ModTags.Blocks.AMETHYST_BLOCKS)
            .tool("_hoe").flammable(5, 30)
            .cutout().build();

    public static final Block AMETHYST_PLANKS = ModRegistry.ofBlock("amethyst_planks",
                    new Block(AmethystTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.PLANKS, ModTags.Blocks.AMETHYST_BLOCKS).tagitem(ItemTags.PLANKS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block AMETHYST_STAIRS = ModRegistry.ofBlock("amethyst_stairs",
                    new StairsBlock(AMETHYST_PLANKS.getDefaultState(), AmethystTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_STAIRS, ModTags.Blocks.AMETHYST_BLOCKS).tagitem(ItemTags.WOODEN_STAIRS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block AMETHYST_SLAB = ModRegistry.ofBlock("amethyst_slab",
                    new SlabBlock(AmethystTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_SLABS, ModTags.Blocks.AMETHYST_BLOCKS).tagitem(ItemTags.WOODEN_SLABS)
            .flammable(5, 20).fuel(300).drop().build();

    public static final Block AMETHYST_PRESSURE_PLATE = ModRegistry.ofBlock("amethyst_pressure_plate",
                    new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, PassableTreeMaterial, BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.AMETHYST_BLOCKS)
            .tagitem(ItemTags.WOODEN_PRESSURE_PLATES)
            .fuel(300).drop().build();
    public static final Block AMETHYST_BUTTON = ModRegistry.ofBlock("amethyst_button",
                    new ButtonBlock(PassableTreeMaterial, BlockSetType.OAK, 10, true))
            .tool("_axe")
            .tag(BlockTags.WOODEN_BUTTONS, ModTags.Blocks.AMETHYST_BLOCKS).tagitem(ItemTags.WOODEN_BUTTONS)
            .fuel(100).drop().build();

    public static final Block AMETHYST_FENCE = ModRegistry.ofBlock("amethyst_fence",
                    new FenceBlock(AmethystTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, ModTags.Blocks.AMETHYST_BLOCKS).tagitem(ItemTags.WOODEN_FENCES)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block AMETHYST_FENCE_GATE = ModRegistry.ofBlock("amethyst_fence_gate",
                    new FenceGateBlock(AmethystTreeMaterial, AMETHYST))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, BlockTags.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, ModTags.Blocks.AMETHYST_BLOCKS)
            .tagitem(ItemTags.WOODEN_FENCES, ItemTags.FENCE_GATES)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block AMETHYST_DOOR = ModRegistry.ofBlock("amethyst_door",
                    new DoorBlock(copy(Blocks.OAK_DOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_DOORS, ModTags.Blocks.AMETHYST_BLOCKS)
            .tagitem(ItemTags.WOODEN_DOORS)
            .model(ModRegistry.Models.DOOR).cutout()
            .drop().build();
    public static final Block AMETHYST_TRAPDOOR = ModRegistry.ofBlock("amethyst_trapdoor",
                    new TrapdoorBlock(copy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_TRAPDOORS, ModTags.Blocks.AMETHYST_BLOCKS)
            .tagitem(ItemTags.WOODEN_TRAPDOORS)
            .model(ModRegistry.Models.TRAPDOOR).cutout()
            .drop().build();
    public static final Block AMETHYST_SAPLING = ModRegistry.ofBlock("amethyst_sapling",
                    new SaplingBlock(new AmethystSaplingGenerator(), copy(Blocks.OAK_SAPLING).mapColor(MapColor.PURPLE).hardness(0)))
            .model(ModRegistry.Models.CROSS).cutout()
            .tag(BlockTags.SAPLINGS, ModTags.Blocks.AMETHYST_BLOCKS).tagitem(ItemTags.SAPLINGS)
            .fuel(100).drop().build();

    public static final Block STRIPPED_GRIZZLY_LOG = ModRegistry.ofBlock("stripped_grizzly_log",
                    new PillarBlock(GrizzlyTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.GRIZZLY_LOGS)
            .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();
    public static final Block GRIZZLY_LOG = ModRegistry.ofBlock("grizzly_log",
                    new PillarBlock(GrizzlyTreeMaterial))
            .tool("_axe").strip(STRIPPED_GRIZZLY_LOG)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.GRIZZLY_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.GRIZZLY_LOGS)
            .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();

    public static final Block STRIPPED_GRIZZLY_WOOD = ModRegistry.ofBlock("stripped_grizzly_wood",
                    new PillarBlock(GrizzlyTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.GRIZZLY_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.GRIZZLY_LOGS)
            .flammable(5, 5).fuel(300).drop().model().build();
    public static final Block GRIZZLY_WOOD = ModRegistry.ofBlock("grizzly_wood",
                    new PillarBlock(GrizzlyTreeMaterial))
            .tool("_axe").strip(STRIPPED_GRIZZLY_WOOD)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.GRIZZLY_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.GRIZZLY_LOGS)
            .flammable(5, 5).fuel(300).drop().model().build();

    public static final Block GRIZZLY_LEAVES = ModRegistry.ofBlock("grizzly_leaves",
                    new LeavesBlock(GrizzlyLeafMaterial))
            .tag(BlockTags.LEAVES, ModTags.Blocks.GRIZZLY_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model().cutout().build();

    public static final Block GRIZZLY_PLANKS = ModRegistry.ofBlock("grizzly_planks",
                    new Block(GrizzlyTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.PLANKS, ModTags.Blocks.GRIZZLY_BLOCKS).tagitem(ItemTags.PLANKS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block GRIZZLY_STAIRS = ModRegistry.ofBlock("grizzly_stairs",
                    new StairsBlock(GRIZZLY_PLANKS.getDefaultState(), GrizzlyTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_STAIRS, ModTags.Blocks.GRIZZLY_BLOCKS).tagitem(ItemTags.WOODEN_STAIRS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block GRIZZLY_SLAB = ModRegistry.ofBlock("grizzly_slab",
                    new SlabBlock(GrizzlyTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_SLABS, ModTags.Blocks.GRIZZLY_BLOCKS).tagitem(ItemTags.WOODEN_SLABS)
            .flammable(5, 20).fuel(300).drop().build();

    public static final Block GRIZZLY_PRESSURE_PLATE = ModRegistry.ofBlock("grizzly_pressure_plate",
                    new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, PassableTreeMaterial, BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.GRIZZLY_BLOCKS)
            .tagitem(ItemTags.WOODEN_PRESSURE_PLATES)
            .fuel(300).drop().build();
    public static final Block GRIZZLY_BUTTON = ModRegistry.ofBlock("grizzly_button",
                    new ButtonBlock(PassableTreeMaterial, BlockSetType.OAK, 10, true))
            .tool("_axe")
            .tag(BlockTags.WOODEN_BUTTONS, ModTags.Blocks.GRIZZLY_BLOCKS).tagitem(ItemTags.WOODEN_BUTTONS)
            .fuel(100).drop().build();

    public static final Block GRIZZLY_FENCE = ModRegistry.ofBlock("grizzly_fence",
                    new FenceBlock(GrizzlyTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, ModTags.Blocks.GRIZZLY_BLOCKS).tagitem(ItemTags.WOODEN_FENCES)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block GRIZZLY_FENCE_GATE = ModRegistry.ofBlock("grizzly_fence_gate",
                    new FenceGateBlock(GrizzlyTreeMaterial, GRIZZLY))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, BlockTags.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, ModTags.Blocks.GRIZZLY_BLOCKS)
            .tagitem(ItemTags.WOODEN_FENCES, ItemTags.FENCE_GATES)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block GRIZZLY_DOOR = ModRegistry.ofBlock("grizzly_door",
                    new DoorBlock(copy(Blocks.OAK_DOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_DOORS, ModTags.Blocks.GRIZZLY_BLOCKS)
            .tagitem(ItemTags.WOODEN_DOORS)
            .model(ModRegistry.Models.DOOR).cutout()
            .drop().build();
    public static final Block GRIZZLY_TRAPDOOR = ModRegistry.ofBlock("grizzly_trapdoor",
                    new TrapdoorBlock(copy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_TRAPDOORS, ModTags.Blocks.GRIZZLY_BLOCKS)
            .tagitem(ItemTags.WOODEN_TRAPDOORS)
            .model(ModRegistry.Models.TRAPDOOR).cutout()
            .drop().build();
    public static final Block GRIZZLY_SAPLING = ModRegistry.ofBlock("grizzly_sapling",
                    new SaplingBlock(new GrizzlySaplingGenerator(), copy(Blocks.OAK_SAPLING).mapColor(MapColor.PURPLE).hardness(0)))
            .model(ModRegistry.Models.CROSS).cutout()
            .tag(BlockTags.SAPLINGS, ModTags.Blocks.GRIZZLY_BLOCKS).tagitem(ItemTags.SAPLINGS)
            .fuel(100).drop().build();

    public static final Block STRIPPED_TEALIVE_LOG = ModRegistry.ofBlock("stripped_tealive_log",
                    new PillarBlock(TealiveTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.TEALIVE_LOGS)
            .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();
    public static final Block TEALIVE_LOG = ModRegistry.ofBlock("tealive_log",
                    new PillarBlock(TealiveTreeMaterial))
            .tool("_axe").strip(STRIPPED_TEALIVE_LOG)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.TEALIVE_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.TEALIVE_LOGS)
            .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();

    public static final Block STRIPPED_TEALIVE_WOOD = ModRegistry.ofBlock("stripped_tealive_wood",
                    new PillarBlock(TealiveTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.TEALIVE_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.TEALIVE_LOGS)
            .flammable(5, 5).fuel(300).drop().model().build();
    public static final Block TEALIVE_WOOD = ModRegistry.ofBlock("tealive_wood",
                    new PillarBlock(TealiveTreeMaterial))
            .tool("_axe").strip(STRIPPED_TEALIVE_WOOD)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.TEALIVE_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.TEALIVE_LOGS)
            .flammable(5, 5).fuel(300).drop().model().build();

    public static final Block TEALIVE_LEAVES = ModRegistry.ofBlock("tealive_leaves",
                    new LeavesBlock(TealiveLeafMaterial))
            .tag(BlockTags.LEAVES, ModTags.Blocks.TEALIVE_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model().cutout().build();

    public static final Block TEALIVE_PLANKS = ModRegistry.ofBlock("tealive_planks",
                    new Block(TealiveTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.PLANKS, ModTags.Blocks.TEALIVE_BLOCKS).tagitem(ItemTags.PLANKS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block TEALIVE_STAIRS = ModRegistry.ofBlock("tealive_stairs",
                    new StairsBlock(TEALIVE_PLANKS.getDefaultState(), TealiveTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_STAIRS, ModTags.Blocks.TEALIVE_BLOCKS).tagitem(ItemTags.WOODEN_STAIRS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block TEALIVE_SLAB = ModRegistry.ofBlock("tealive_slab",
                    new SlabBlock(TealiveTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_SLABS, ModTags.Blocks.TEALIVE_BLOCKS).tagitem(ItemTags.WOODEN_SLABS)
            .flammable(5, 20).fuel(300).drop().build();

    public static final Block TEALIVE_PRESSURE_PLATE = ModRegistry.ofBlock("tealive_pressure_plate",
                    new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, PassableTreeMaterial, BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.TEALIVE_BLOCKS)
            .tagitem(ItemTags.WOODEN_PRESSURE_PLATES)
            .fuel(300).drop().build();
    public static final Block TEALIVE_BUTTON = ModRegistry.ofBlock("tealive_button",
                    new ButtonBlock(PassableTreeMaterial, BlockSetType.OAK, 10, true))
            .tool("_axe")
            .tag(BlockTags.WOODEN_BUTTONS, ModTags.Blocks.TEALIVE_BLOCKS).tagitem(ItemTags.WOODEN_BUTTONS)
            .fuel(100).drop().build();

    public static final Block TEALIVE_FENCE = ModRegistry.ofBlock("tealive_fence",
                    new FenceBlock(TealiveTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, ModTags.Blocks.TEALIVE_BLOCKS).tagitem(ItemTags.WOODEN_FENCES)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block TEALIVE_FENCE_GATE = ModRegistry.ofBlock("tealive_fence_gate",
                    new FenceGateBlock(TealiveTreeMaterial, TEALIVE))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, BlockTags.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, ModTags.Blocks.TEALIVE_BLOCKS)
            .tagitem(ItemTags.WOODEN_FENCES, ItemTags.FENCE_GATES)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block TEALIVE_DOOR = ModRegistry.ofBlock("tealive_door",
                    new DoorBlock(copy(Blocks.OAK_DOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_DOORS, ModTags.Blocks.TEALIVE_BLOCKS)
            .tagitem(ItemTags.WOODEN_DOORS)
            .model(ModRegistry.Models.DOOR).cutout()
            .drop().build();
    public static final Block TEALIVE_TRAPDOOR = ModRegistry.ofBlock("tealive_trapdoor",
                    new TrapdoorBlock(copy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_TRAPDOORS, ModTags.Blocks.TEALIVE_BLOCKS)
            .tagitem(ItemTags.WOODEN_TRAPDOORS)
            .model(ModRegistry.Models.TRAPDOOR).cutout()
            .drop().build();
    public static final Block TEALIVE_SAPLING = ModRegistry.ofBlock("tealive_sapling",
                    new SaplingBlock(new TealiveSaplingGenerator(), copy(Blocks.OAK_SAPLING).mapColor(MapColor.PURPLE).breakInstantly()))
            .model(ModRegistry.Models.CROSS).cutout()
            .tag(BlockTags.SAPLINGS, ModTags.Blocks.TEALIVE_BLOCKS).tagitem(ItemTags.SAPLINGS)
            .fuel(100).drop().build();

    public static final Block STRIPPED_BURLYWOOD_LOG = ModRegistry.ofBlock("stripped_burlywood_log",
                    new PillarBlock(BurlywoodTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.BURLYWOOD_LOGS)
            .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();
    public static final Block BURLYWOOD_LOG = ModRegistry.ofBlock("burlywood_log",
                    new PillarBlock(BurlywoodTreeMaterial))
            .tool("_axe").strip(STRIPPED_BURLYWOOD_LOG)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.BURLYWOOD_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.BURLYWOOD_LOGS)
            .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();

    public static final Block STRIPPED_BURLYWOOD_WOOD = ModRegistry.ofBlock("stripped_burlywood_wood",
                    new PillarBlock(BurlywoodTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.BURLYWOOD_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.BURLYWOOD_LOGS)
            .flammable(5, 5).fuel(300).drop().model().build();
    public static final Block BURLYWOOD_WOOD = ModRegistry.ofBlock("burlywood_wood",
                    new PillarBlock(BurlywoodTreeMaterial))
            .tool("_axe").strip(STRIPPED_BURLYWOOD_WOOD)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.BURLYWOOD_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.BURLYWOOD_LOGS)
            .flammable(5, 5).fuel(300).drop().model().build();

    public static final Block BURLYWOOD_LEAVES = ModRegistry.ofBlock("burlywood_leaves",
                    new LeafDroppingLeaveBlock(BurlywoodLeafMaterial, new LeafParticleEffect(0.5f, 0.5f, 0)))
            .tag(BlockTags.LEAVES, ModTags.Blocks.BURLYWOOD_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model().cutout().build();

    public static final Block BURLYWOOD_PLANKS = ModRegistry.ofBlock("burlywood_planks",
                    new Block(BurlywoodTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.PLANKS, ModTags.Blocks.BURLYWOOD_BLOCKS).tagitem(ItemTags.PLANKS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block BURLYWOOD_STAIRS = ModRegistry.ofBlock("burlywood_stairs",
                    new StairsBlock(BURLYWOOD_PLANKS.getDefaultState(), BurlywoodTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_STAIRS, ModTags.Blocks.BURLYWOOD_BLOCKS).tagitem(ItemTags.WOODEN_STAIRS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block BURLYWOOD_SLAB = ModRegistry.ofBlock("burlywood_slab",
                    new SlabBlock(BurlywoodTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_SLABS, ModTags.Blocks.BURLYWOOD_BLOCKS).tagitem(ItemTags.WOODEN_SLABS)
            .flammable(5, 20).fuel(300).drop().build();

    public static final Block BURLYWOOD_PRESSURE_PLATE = ModRegistry.ofBlock("burlywood_pressure_plate",
                    new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, PassableTreeMaterial, BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.BURLYWOOD_BLOCKS)
            .tagitem(ItemTags.WOODEN_PRESSURE_PLATES)
            .fuel(300).drop().build();
    public static final Block BURLYWOOD_BUTTON = ModRegistry.ofBlock("burlywood_button",
                    new ButtonBlock(PassableTreeMaterial, BlockSetType.OAK, 10, true))
            .tool("_axe")
            .tag(BlockTags.WOODEN_BUTTONS, ModTags.Blocks.BURLYWOOD_BLOCKS).tagitem(ItemTags.WOODEN_BUTTONS)
            .fuel(100).drop().build();

    public static final Block BURLYWOOD_FENCE = ModRegistry.ofBlock("burlywood_fence",
                    new FenceBlock(BurlywoodTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, ModTags.Blocks.BURLYWOOD_BLOCKS).tagitem(ItemTags.WOODEN_FENCES)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block BURLYWOOD_FENCE_GATE = ModRegistry.ofBlock("burlywood_fence_gate",
                    new FenceGateBlock(BurlywoodTreeMaterial, BURLYWOOD))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, BlockTags.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, ModTags.Blocks.BURLYWOOD_BLOCKS)
            .tagitem(ItemTags.WOODEN_FENCES, ItemTags.FENCE_GATES)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block BURLYWOOD_DOOR = ModRegistry.ofBlock("burlywood_door",
                    new DoorBlock(copy(Blocks.OAK_DOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_DOORS, ModTags.Blocks.BURLYWOOD_BLOCKS)
            .tagitem(ItemTags.WOODEN_DOORS)
            .model(ModRegistry.Models.DOOR).cutout()
            .drop().build();
    public static final Block BURLYWOOD_TRAPDOOR = ModRegistry.ofBlock("burlywood_trapdoor",
                    new TrapdoorBlock(copy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_TRAPDOORS, ModTags.Blocks.BURLYWOOD_BLOCKS)
            .tagitem(ItemTags.WOODEN_TRAPDOORS)
            .model(ModRegistry.Models.TRAPDOOR).cutout()
            .drop().build();
    public static final Block BURLYWOOD_SAPLING = ModRegistry.ofBlock("burlywood_sapling",
                    new SaplingBlock(new BurlywoodSaplingGenerator(), copy(Blocks.OAK_SAPLING).mapColor(MapColor.PURPLE).breakInstantly()))
            .model(ModRegistry.Models.CROSS).cutout()
            .tag(BlockTags.SAPLINGS, ModTags.Blocks.BURLYWOOD_BLOCKS).tagitem(ItemTags.SAPLINGS)
            .fuel(100).drop().build();

    public static final Block STRIPPED_CRIMSONVEIL_LOG = ModRegistry.ofBlock("stripped_crimsonveil_log",
                    new PillarBlock(CrimsonveilTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.CRIMSONVEIL_LOGS)
            .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();
    public static final Block CRIMSONVEIL_LOG = ModRegistry.ofBlock("crimsonveil_log",
                    new PillarBlock(CrimsonveilTreeMaterial))
            .tool("_axe").strip(STRIPPED_CRIMSONVEIL_LOG)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.CRIMSONVEIL_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.CRIMSONVEIL_LOGS)
            .flammable(5, 5).fuel(300).drop().model(ModRegistry.Models.PILLAR).build();

    public static final Block STRIPPED_CRIMSONVEIL_WOOD = ModRegistry.ofBlock("stripped_crimsonveil_wood",
                    new PillarBlock(CrimsonveilTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.CRIMSONVEIL_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.CRIMSONVEIL_LOGS)
            .flammable(5, 5).fuel(300).drop().model().build();
    public static final Block CRIMSONVEIL_WOOD = ModRegistry.ofBlock("crimsonveil_wood",
                    new PillarBlock(CrimsonveilTreeMaterial))
            .tool("_axe").strip(STRIPPED_CRIMSONVEIL_WOOD)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.CRIMSONVEIL_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.CRIMSONVEIL_LOGS)
            .flammable(5, 5).fuel(300).drop().model().build();

    public static final Block CRIMSONVEIL_LEAVES = ModRegistry.ofBlock("crimsonveil_leaves",
                    new LeavesBlock(CrimsonveilLeafMaterial))
            .tag(BlockTags.LEAVES, ModTags.Blocks.CRIMSONVEIL_BLOCKS, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model().cutout().build();
    public static final Block CRIMSONVEIL_VINE = ModRegistry.ofBlock("crimsonveil_vine",
                    new CrimsonveilVinesHeadBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_CRIMSON)
                            .ticksRandomly()
                            .noCollision()
                            .luminance(CrimsonvielVines.getLuminanceSupplier(12))
                            .breakInstantly()
                            .sounds(BlockSoundGroup.CAVE_VINES)
                            .pistonBehavior(PistonBehavior.DESTROY)
                    ), false)
            .tag(BlockTags.LEAVES, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model(ModRegistry.Models.CROSS_ITEMLESS).cutout().build();
    public static final Block CRIMSONVEIL_VINE_PLANT = ModRegistry.ofBlock("crimsonveil_vine_plant",
                    new CrimsonveilVinesBodyBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_CRIMSON)
                            .ticksRandomly()
                            .noCollision()
                            .luminance(CrimsonvielVines.getLuminanceSupplier(12))
                            .breakInstantly()
                            .sounds(BlockSoundGroup.CAVE_VINES)
                            .pistonBehavior(PistonBehavior.DESTROY)), false)
            .tag(BlockTags.LEAVES, ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model(ModRegistry.Models.CROSS_ITEMLESS).cutout().build();

    public static final Block CRIMSONVEIL_PLANKS = ModRegistry.ofBlock("crimsonveil_planks",
                    new Block(CrimsonveilTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.PLANKS, ModTags.Blocks.CRIMSONVEIL_BLOCKS).tagitem(ItemTags.PLANKS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block CRIMSONVEIL_STAIRS = ModRegistry.ofBlock("crimsonveil_stairs",
                    new StairsBlock(CRIMSONVEIL_PLANKS.getDefaultState(), CrimsonveilTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_STAIRS, ModTags.Blocks.CRIMSONVEIL_BLOCKS).tagitem(ItemTags.WOODEN_STAIRS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block CRIMSONVEIL_SLAB = ModRegistry.ofBlock("crimsonveil_slab",
                    new SlabBlock(CrimsonveilTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_SLABS, ModTags.Blocks.CRIMSONVEIL_BLOCKS).tagitem(ItemTags.WOODEN_SLABS)
            .flammable(5, 20).fuel(300).drop().build();

    public static final Block CRIMSONVEIL_PRESSURE_PLATE = ModRegistry.ofBlock("crimsonveil_pressure_plate",
                    new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, PassableTreeMaterial, BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.CRIMSONVEIL_BLOCKS)
            .tagitem(ItemTags.WOODEN_PRESSURE_PLATES)
            .fuel(300).drop().build();
    public static final Block CRIMSONVEIL_BUTTON = ModRegistry.ofBlock("crimsonveil_button",
                    new ButtonBlock(PassableTreeMaterial, BlockSetType.OAK, 10, true))
            .tool("_axe")
            .tag(BlockTags.WOODEN_BUTTONS, ModTags.Blocks.CRIMSONVEIL_BLOCKS).tagitem(ItemTags.WOODEN_BUTTONS)
            .fuel(100).drop().build();

    public static final Block CRIMSONVEIL_FENCE = ModRegistry.ofBlock("crimsonveil_fence",
                    new FenceBlock(CrimsonveilTreeMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, ModTags.Blocks.CRIMSONVEIL_BLOCKS).tagitem(ItemTags.WOODEN_FENCES)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block CRIMSONVEIL_FENCE_GATE = ModRegistry.ofBlock("crimsonveil_fence_gate",
                    new FenceGateBlock(CrimsonveilTreeMaterial, CRIMSONVEIL))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, BlockTags.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, ModTags.Blocks.CRIMSONVEIL_BLOCKS)
            .tagitem(ItemTags.WOODEN_FENCES, ItemTags.FENCE_GATES)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block CRIMSONVEIL_DOOR = ModRegistry.ofBlock("crimsonveil_door",
                    new DoorBlock(copy(Blocks.OAK_DOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_DOORS, ModTags.Blocks.CRIMSONVEIL_BLOCKS)
            .tagitem(ItemTags.WOODEN_DOORS)
            .model(ModRegistry.Models.DOOR).cutout()
            .drop().build();
    public static final Block CRIMSONVEIL_TRAPDOOR = ModRegistry.ofBlock("crimsonveil_trapdoor",
                    new TrapdoorBlock(copy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_TRAPDOORS, ModTags.Blocks.CRIMSONVEIL_BLOCKS)
            .tagitem(ItemTags.WOODEN_TRAPDOORS)
            .model(ModRegistry.Models.TRAPDOOR).cutout()
            .drop().build();
    public static final Block CRIMSONVEIL_SAPLING = ModRegistry.ofBlock("crimsonveil_sapling",
                    new SaplingBlock(new CrimsonveilSaplingGenerator(), copy(Blocks.OAK_SAPLING).mapColor(MapColor.PURPLE).breakInstantly()))
            .model(ModRegistry.Models.CROSS).cutout()
            .tag(BlockTags.SAPLINGS, ModTags.Blocks.CRIMSONVEIL_BLOCKS).tagitem(ItemTags.SAPLINGS)
            .fuel(100).drop().build();
    public static final Block BEARFRUIT_BRAMBLE = ModRegistry.ofBlock("bearfruit_bramble_stem",
                    new StandingFruitBlock(copy(Blocks.OAK_SAPLING).mapColor(MapColor.PURPLE).breakInstantly()
                            .luminance(StandingFruitBlock.STATE_TO_LUMINANCE),
                            ModItems.BEARFRUIT_BRAMBLE, ModTags.Blocks.BEARFRUIT_BRAMBLE_GROWABLE_ON, Block.createCuboidShape(
                            6, 0, 6, 10, 16, 10
                    )))
            .cutout().tag(ModTags.Blocks.BEARFRUIT_BRAMBLE_GROWABLE_ON).tagitem(ItemTags.FLOWERS).drop_shears(ModItems.BEARFRUIT_BRAMBLE).build();
    public static final Block MIDNIGHT_LILY_PAD = ModRegistry.ofBlock("midnight_lily",
            new MidnightLilyPadBlock(FabricBlockSettings.create().mapColor(MapColor.DIAMOND_BLUE).breakInstantly().sounds(BlockSoundGroup.LILY_PAD).nonOpaque().pistonBehavior(PistonBehavior.DESTROY).luminance(8))).cutout().build();
    public static final Block AMETHYST_PETALS = ModRegistry.ofBlock("amethyst_petals",
            new FlowerbedBlock(FabricBlockSettings.create().mapColor(MapColor.PALE_PURPLE).breakInstantly().noCollision().sounds(BlockSoundGroup.SMALL_AMETHYST_BUD)
                    .pistonBehavior(PistonBehavior.DESTROY).luminance(createLightLevelFromBlockState(8, Properties.FLOWER_AMOUNT, 2)))).cutout().build();
    public static final Block GLOW_APPLE_BUSH = ModRegistry.ofBlock("glow_apple_bush",
            new TreeBushLikeBlock(FabricBlockSettings.create().mapColor(MapColor.PALE_GREEN).hardness(0.25f).sounds(BlockSoundGroup.SMALL_DRIPLEAF)
                    .pistonBehavior(PistonBehavior.DESTROY).luminance(createLightLevelFromBlockState(4, TreeBushLikeBlock.GROWN)).ticksRandomly())).cutout().build();
    public static final Block UMBRAL_KELP = ModRegistry.ofBlock("umbral_kelp",
            new UmbralKelpBlock(FabricBlockSettings.create().mapColor(MapColor.PURPLE).breakInstantly().sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY).noCollision().luminance(8))).drop().cutout().build();
    public static final Block UMBRAL_KELP_PLANT = ModRegistry.ofBlock("umbral_kelp_plant",
            new UmbralKelpPlantBlock(FabricBlockSettings.create().mapColor(MapColor.PURPLE).breakInstantly().sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY).noCollision()), false).drop(UMBRAL_KELP).cutout().build();
    public static final Block GLOOMWEED = ModRegistry.ofBlock("gloomweed",
            new DarknessFernBlock(FabricBlockSettings.create().replaceable().breakInstantly().sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY).noCollision())).tool("_hoe").cutout().build();
    public static final Block TALL_GLOOMWEED = ModRegistry.ofBlock("tall_gloomweed",
            new TallPlantBlock(FabricBlockSettings.create().replaceable().breakInstantly().sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY).noCollision())).tool("_hoe").cutout().build();

    public static final Block DARK_WATER = ModRegistry.ofBlock("dark_water",
                    new DarkWaterBlock(ModFluids.DARK_WATER,
                            FabricBlockSettings.create()
                                    .mapColor(MapColor.WATER_BLUE)
                                    .replaceable()
                                    .noCollision()
                                    .strength(150.0F)
                                    .pistonBehavior(PistonBehavior.DESTROY)
                                    .dropsNothing()
                                    .liquid()
                                    .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)))
            .model(ModRegistry.Models.FLUID).build();

    public static ToIntFunction<BlockState> createLightLevelFromBlockState(int litLevel, IntProperty property, int modifierToLight) {
        return state -> Math.min(state.get(property) * modifierToLight, litLevel);
    }

    public static ToIntFunction<BlockState> createLightLevelFromBlockState(int litLevel, BooleanProperty property) {
        return state -> state.get(property) ? 0 : litLevel;
    }
}