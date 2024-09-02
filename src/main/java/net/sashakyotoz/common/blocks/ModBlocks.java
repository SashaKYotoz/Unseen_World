package net.sashakyotoz.common.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.custom.DarkWaterBlock;
import net.sashakyotoz.common.blocks.custom.HangingFruitBlock;
import net.sashakyotoz.common.blocks.custom.fluids.DarkWaterFluid;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.features.trees.AmethystSaplingGenerator;

import java.util.Map;

public class ModBlocks {
    public static void register() {
        UnseenWorld.log("Registering Blocks for modid : " + UnseenWorld.MOD_ID);

        ModRegistry.addDrop(AMETHYST_SIGN, ModItems.AMETHYST_SIGN);
        ModRegistry.addDrop(AMETHYST_WALL_SIGN, ModItems.AMETHYST_SIGN);
        ModRegistry.addDrop(AMETHYST_HANGING_SIGN, ModItems.AMETHYST_HANGING_SIGN);
        ModRegistry.addDrop(AMETHYST_WALL_HANGING_SIGN, ModItems.AMETHYST_HANGING_SIGN);

        ModRegistry.BlockBuilder.registerBricksSet(DARK_CURRANTSLATE,DARK_CURRANTSLATE_STAIRS,DARK_CURRANTSLATE_SLAB,DARK_CURRANTSLATE_WALL);
        ModRegistry.BlockBuilder.registerBricksSet(DARK_CURRANTSLATE_BRICKS,DARK_CURRANTSLATE_BRICKS_STAIRS,DARK_CURRANTSLATE_BRICKS_SLAB,DARK_CURRANTSLATE_BRICKS_WALL);
        ModRegistry.BlockBuilder.registerBricksSet(GLACIEMITE,GLACIEMITE_STAIRS,GLACIEMITE_SLAB,GLACIEMITE_WALL);
        ModRegistry.BlockBuilder.registerBricksSet(GLACIEMITE_BRICKS,GLACIEMITE_BRICKS_STAIRS,GLACIEMITE_BRICKS_SLAB,GLACIEMITE_BRICKS_WALL);
        ModRegistry.BlockBuilder.registerPolishedSet(POLISHED_TANZANITE,POLISHED_TANZANITE_STAIRS,POLISHED_TANZANITE_SLAB);
        ModRegistry.registerSet(AMETHYST_PLANKS, Map.of(
                ModRegistry.Models.STAIRS, AMETHYST_STAIRS,
                ModRegistry.Models.SLAB, AMETHYST_SLAB,
                ModRegistry.Models.BUTTON, AMETHYST_BUTTON,
                ModRegistry.Models.PRESSURE_PLATE, AMETHYST_PRESSURE_PLATE,
                ModRegistry.Models.FENCE, AMETHYST_FENCE,
                ModRegistry.Models.FENCE_GATE, AMETHYST_FENCE_GATE,
                ModRegistry.Models.SIGN, AMETHYST_SIGN,
                ModRegistry.Models.WALL_SIGN, AMETHYST_WALL_SIGN,
                ModRegistry.Models.HANGING_SIGN, AMETHYST_HANGING_SIGN,
                ModRegistry.Models.WALL_HANGING_SIGN, AMETHYST_WALL_HANGING_SIGN
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

    private static final FabricBlockSettings AmethystTreeMaterial =
            FabricBlockSettings.create().hardness(2.5f).luminance(3).mapColor(MapColor.PURPLE).sounds(BlockSoundGroup.AMETHYST_BLOCK);
    private static final FabricBlockSettings AmethystPassableTreeMaterial = copy(Blocks.OAK_SIGN);
    private static final FabricBlockSettings AmethystLeafMaterial = copy(AmethystTreeMaterial.breakInstantly());
    private static final FabricBlockSettings BurlywoodTreeMaterial =
            FabricBlockSettings.create().hardness(3f).luminance(3).mapColor(MapColor.DARK_GREEN).sounds(BlockSoundGroup.BAMBOO_WOOD);
    private static final FabricBlockSettings TealiveTreeMaterial =
            FabricBlockSettings.create().hardness(1.5f).luminance(5).mapColor(MapColor.CYAN).sounds(BlockSoundGroup.NETHER_WOOD);
    private static final FabricBlockSettings CrimsonveilTreeMaterial =
            FabricBlockSettings.create().hardness(2f).luminance(2).mapColor(MapColor.TERRACOTTA_ORANGE).sounds(BlockSoundGroup.WOOD);
    private static final FabricBlockSettings GrizzlyTreeMaterial =
            FabricBlockSettings.create().hardness(3.5f).mapColor(MapColor.GRAY).sounds(BlockSoundGroup.NETHER_WOOD);


    public static final WoodType GRIZZLY = new WoodTypeBuilder().register(UnseenWorld.makeID("grizzly"), BlockSetType.DARK_OAK);
    public static final WoodType AMETHYST = new WoodTypeBuilder().register(UnseenWorld.makeID("amethyst"), BlockSetType.BIRCH);
    public static final WoodType BURLYWOOD = new WoodTypeBuilder().register(UnseenWorld.makeID("burlywood"), BlockSetType.MANGROVE);
    public static final WoodType TEALIVY = new WoodTypeBuilder().register(UnseenWorld.makeID("tealivy"), BlockSetType.JUNGLE);
    //stones
    public static final Block DARK_FROST_BRICKS = ModRegistry.ofBlock("dark_frost_bricks",
                    new Block(copy(Blocks.POLISHED_BLACKSTONE_BRICKS)))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().model().build();
    public static final Block DARK_CURRANTSLATE = ModRegistry.ofBlock("dark_currantslate",
                    new Block(DarkCurrantslateMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block DARK_CURRANTSLATE_SLAB = ModRegistry.ofBlock("dark_currantslate_slab",
                    new SlabBlock(DarkCurrantslateMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block DARK_CURRANTSLATE_STAIRS = ModRegistry.ofBlock("dark_currantslate_stairs",
                    new StairsBlock(DARK_CURRANTSLATE.getDefaultState(),DarkCurrantslateMaterial))
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
                    new StairsBlock(DARK_CURRANTSLATE_BRICKS.getDefaultState(),DarkCurrantslateMaterial))
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
                    new StairsBlock(DARK_CURRANTSLATE.getDefaultState(),GlaciemiteMaterial))
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
                    new StairsBlock(DARK_CURRANTSLATE_BRICKS.getDefaultState(),GlaciemiteMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
    public static final Block GLACIEMITE_BRICKS_WALL = ModRegistry.ofBlock("glaciemite_bricks_wall",
                    new WallBlock(GlaciemiteMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().build();
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
                    new StairsBlock(DARK_CURRANTSLATE_BRICKS.getDefaultState(),TanzaniteMaterial)).tool("stone_pickaxe")
            .drop().build();
    //ores
    public static final Block ABYSSAL_ORE_IN_DARK_CURRANTSLATE = ModRegistry.ofBlock("abyssal_ore_in_dark_currantslate",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(5).requiresTool().luminance(4), UniformIntProvider.create(1,5))).model().drop_silk(ModItems.RAW_ABYSSAL_ORE).tool("diamond_pickaxe").build();
    public static final Block ABYSSAL_ORE_IN_GLACIEMITE = ModRegistry.ofBlock("abyssal_ore_in_glaciemite",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(5).requiresTool().luminance(4), UniformIntProvider.create(1,5))).model().drop_silk(ModItems.RAW_ABYSSAL_ORE).tool("diamond_pickaxe").build();
    public static final Block ABYSSAL_INGOT_BLOCK = ModRegistry.ofBlock("abyssal_ingot_block",
            new Block(FabricBlockSettings.create().hardness(5).requiresTool())).model().drop().tool("diamond_pickaxe").build();
    public static final Block RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE = ModRegistry.ofBlock("red_titanium_ore_in_dark_currantslate",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(6).requiresTool().luminance(3), UniformIntProvider.create(1,6))).model().drop_silk(ModItems.RAW_RED_TITANIUM_ORE).tool("diamond_pickaxe").build();
    public static final Block RED_TITANIUM_IN_GLACIEMITE = ModRegistry.ofBlock("red_titanium_ore_in_glaciemite",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(6).requiresTool().luminance(3), UniformIntProvider.create(1,6))).model().drop_silk(ModItems.RAW_RED_TITANIUM_ORE).tool("diamond_pickaxe").build();
    public static final Block RED_TITANIUM_INGOT_BLOCK = ModRegistry.ofBlock("red_titanium_ingot_block",
            new Block(FabricBlockSettings.create().hardness(6).requiresTool())).model().drop().tool("diamond_pickaxe").build();
    public static final Block UNSEENIUM_ORE_IN_DARK_CURRANTSLATE = ModRegistry.ofBlock("unseenium_ore_in_dark_currantslate",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(4).requiresTool().luminance(5), UniformIntProvider.create(1,4))).model().drop_silk(ModItems.RAW_UNSEENIUM_ORE).tool("diamond_pickaxe").build();
    public static final Block UNSEENIUM_ORE_IN_GLACIEMITE = ModRegistry.ofBlock("unseenium_ore_in_glaciemite",
            new ExperienceDroppingBlock(FabricBlockSettings.create().hardness(4).requiresTool().luminance(5), UniformIntProvider.create(1,4))).model().drop_silk(ModItems.RAW_UNSEENIUM_ORE).tool("diamond_pickaxe").build();
    public static final Block UNSEENIUM_INGOT_BLOCK = ModRegistry.ofBlock("unseenium_ingot_block",
            new Block(FabricBlockSettings.create().hardness(4).requiresTool())).model().drop().tool("diamond_pickaxe").build();
    //grass
    public static final Block NIGHTDARK_GRASS_BLOCK = ModRegistry.ofBlock("nightdark_grass_block",new GrassBlock(FabricBlockSettings.create().hardness(1).mapColor(MapColor.BLACK))).tool("_hoe").model(ModRegistry.Models.GRASS).build();
    public static final Block TEALIVY_GRASS_BLOCK = ModRegistry.ofBlock("tealivy_grass_block",new GrassBlock(FabricBlockSettings.create().hardness(1).mapColor(MapColor.CYAN))).tool("_hoe").model(ModRegistry.Models.GRASS).build();
    public static final Block AMETHYST_GRASS_BLOCK = ModRegistry.ofBlock("amethyst_grass_block",new GrassBlock(FabricBlockSettings.create().hardness(1).mapColor(MapColor.PURPLE))).tool("_hoe").model(ModRegistry.Models.GRASS).build();
    public static final Block GRIMWOOD_GRASS_BLOCK = ModRegistry.ofBlock("grimwood_grass_block",new GrassBlock(FabricBlockSettings.create().hardness(1).mapColor(MapColor.RED))).tool("_hoe").model(ModRegistry.Models.GRASS).build();
    public static final Block ASHEN_OOZE = ModRegistry.ofBlock("ashen_ooze",new Block(FabricBlockSettings.create().hardness(1).mapColor(MapColor.PALE_YELLOW))).tool("_shovel").model().build();
    public static final Block GLIMMERGRAIN_SAND = ModRegistry.ofBlock("glimmergrain_sand",new FallingBlock(FabricBlockSettings.create().hardness(1).mapColor(MapColor.GRAY))).tool("_shovel").model().build();
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
                    new LeavesBlock(AmethystLeafMaterial))
            .tag(BlockTags.LEAVES, ModTags.Blocks.AMETHYST_BLOCKS, ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model().cutout().build();
    public static final Block HANGING_AMETHYST_LEAVES = ModRegistry.ofBlock("hanging_amethyst_leaves",
                    new HangingFruitBlock(copy(AmethystLeafMaterial).collidable(false).breakInstantly(),
                            () -> ModItems.CRYSTIE_APPLE,
                            ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON,
                            Block.createCuboidShape(0, 8, 0, 16, 16, 16)))
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
                    new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, AmethystPassableTreeMaterial, BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.AMETHYST_BLOCKS)
            .tagitem(ItemTags.WOODEN_PRESSURE_PLATES)
            .fuel(300).drop().build();
    public static final Block AMETHYST_BUTTON = ModRegistry.ofBlock("amethyst_button",
                    new ButtonBlock(AmethystPassableTreeMaterial, BlockSetType.OAK, 10, true))
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

    public static final Block AMETHYST_SIGN = ModRegistry.ofBlock("amethyst_sign",
                    new SignBlock(AmethystPassableTreeMaterial, AMETHYST), false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.AMETHYST_BLOCKS)
            .tool("_axe").drop(ModItems.AMETHYST_SIGN).build();
    public static final Block AMETHYST_WALL_SIGN = ModRegistry.ofBlock("amethyst_wall_sign",
                    new WallSignBlock(AmethystPassableTreeMaterial, AMETHYST), false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").drop(ModItems.AMETHYST_SIGN).build();

    public static final Block AMETHYST_HANGING_SIGN = ModRegistry.ofBlock("amethyst_hanging_sign",
                    new HangingSignBlock(AmethystPassableTreeMaterial, AMETHYST), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.AMETHYST_BLOCKS)
            .tool("_axe").build();
    public static final Block AMETHYST_WALL_HANGING_SIGN = ModRegistry.ofBlock("amethyst_wall_hanging_sign",
                    new WallHangingSignBlock(copy(AmethystPassableTreeMaterial), AMETHYST), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, ModTags.Blocks.AMETHYST_BLOCKS)
            .tool("_axe").build();

    public static final Block AMETHYST_SAPLING = ModRegistry.ofBlock("amethyst_sapling",
                    new SaplingBlock(new AmethystSaplingGenerator(), copy(Blocks.OAK_SAPLING).mapColor(MapColor.PURPLE).hardness(0)))
            .model(ModRegistry.Models.CROSS).cutout()
            .tag(BlockTags.SAPLINGS, ModTags.Blocks.AMETHYST_BLOCKS).tagitem(ItemTags.SAPLINGS)
            .fuel(100).drop().build();
    //fluids
    public static final FlowableFluid DARK_FLOWING_WATER = register("dark_flowing_water", new DarkWaterFluid.Flowing());
    public static final FlowableFluid DARK_WATER = register("dark_water", new DarkWaterFluid.Still());

    private static <T extends Fluid> T register(String id, T value) {
        return Registry.register(Registries.FLUID, id, value);
    }
    public static final Block DARK_WATER_BLOCK = ModRegistry.ofBlock("dark_water",
                    new DarkWaterBlock(DARK_WATER,
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
}