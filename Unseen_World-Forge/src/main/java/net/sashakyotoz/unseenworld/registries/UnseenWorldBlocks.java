package net.sashakyotoz.unseenworld.registries;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.tags.ITag;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.block.*;
import net.sashakyotoz.unseenworld.entity.*;
import net.sashakyotoz.unseenworld.world.treegrowers.*;

import java.util.function.Supplier;

public class UnseenWorldBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, UnseenWorldMod.MODID);
    public static final RegistryObject<Block> CRIMSERRY_SOUL_CROP = registerBlock("small_crimserry_soul_berry", CrimserrySoulCropBlock::new);
    public static final RegistryObject<Block> MISTERY_CROP_FLOWER = registerBlock("misteryflower_sapling", MisteryFlowerBlock::new);
    public static final RegistryObject<Block> NIGHTDEW_OF_CHIMERIC_DARKNESS = registerBlock("nightdew_of_chimeric_darkness", NightdewOfChimericDarknessBlock::new);
    public static final RegistryObject<Block> OUT_GROWT_APPLE_BUSH = registerBlock("out_growt_apple_bush", OutGrowtAppleBushBlock::new);
    public static final RegistryObject<Block> DARK_CRIMSON_FLOWING_AZALEA = registerBlock("dark_crimson_flowing_azalia", DarkCrimsonFlowingAzaleaBlock::new);
    public static final RegistryObject<Block> DARK_CRIMSON_AZALEA = registerBlock("dark_crimson_azalea", DarkCrimsonAzaleaBlock::new);
    public static final RegistryObject<Block> DARK_CRIMSON_VINE_FLOWER = registerBlock("dark_crimson_vine_flower", DarkCrimsonVineFlowerBlock::new);
    public static final RegistryObject<Block> DARK_CRIMSON_BLOOMING_VINE = registerBlock("dark_crimson_blooming_vine", DarkCrimsonBloomingVineBlock::new);
    public static final RegistryObject<Block> LUMINOUS_AMETHYST_VINE = registerBlock("luminousamethystvine", LuminousAmethystVineBlock::new);
    public static final RegistryObject<Block> GLOWORCHID = registerBlock("gloworchid", () -> new FlowerBlock(() -> MobEffects.GLOWING,200,BlockBehaviour.Properties.copy(Blocks.ORANGE_TULIP).emissiveRendering((bs, br, bp) -> true).lightLevel((blockState) -> 12)){
        @Override
        public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
            return groundState.is(Blocks.GRASS_BLOCK) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.PODZOL)
                    || groundState.is(Blocks.MOSS_BLOCK) || groundState.is(UnseenWorldTags.Blocks.DIRT_THE_DARKNESS) || groundState.is(Blocks.MOSS_BLOCK) || groundState.is(Blocks.CLAY);
        }
    });
    public static final RegistryObject<Block> DARK_CRIMSON_SAPLING = registerBlock("darkcrimsonsapling", () -> new ModSaplingBlock(new DarkcrimsonTreeGrower(),BlockBehaviour.Properties.copy(Blocks.CHERRY_SAPLING)));
    public static final RegistryObject<Block> AMETHYST_TREE_SAPLING = registerBlock("amethyst_tree_sapling", () -> new ModSaplingBlock(new AmethystTreeGrower(),BlockBehaviour.Properties.copy(DARK_CRIMSON_SAPLING.get())));
    public static final RegistryObject<Block> TEALIVY_TREE_SAPLING = registerBlock("tealivy_tree_sapling", () -> new ModSaplingBlock(new TealivyTreeGrower(),BlockBehaviour.Properties.copy(AMETHYST_TREE_SAPLING.get())));
    public static final RegistryObject<Block> GREENISH_BURLYWOOD_SAPLING = registerBlock("greenish_burlywood_sapling", () -> new ModSaplingBlock(new GreenishBurlywoodTreeGrower(),BlockBehaviour.Properties.copy(AMETHYST_TREE_SAPLING.get())));
    public static final RegistryObject<Block> GRIZZLY_SAPLING = registerBlock("grizzly_sapling", () -> new ModSaplingBlock(new GrizzlyTreeGrower(),BlockBehaviour.Properties.copy(GREENISH_BURLYWOOD_SAPLING.get())));
    public static final RegistryObject<Block> WILD_CRIMSERRY_SOUL_FLOWER = registerBlock("grown_crimserry_soul_berry", WildCrimserrySoulFlowerBlock::new);
    public static final RegistryObject<Block> WILD_MISTERY_FLOWER_BERRIES = registerBlock("misteryflower_berries", WildMisteryFlowerBerriesBlock::new);
    public static final RegistryObject<Block> AMETHYST_GRASS = registerBlock("amethyst_grass", AmethystGrass::new);
    public static final RegistryObject<Block> TEALIVY_PLUMERIA = registerBlock("tealivy_plumeria", TealivyPlumeriaBlock::new);
    public static final RegistryObject<Block> TEALIVY_JADE_VINE_FLOWER = registerDoubleBlock("tealivy_jade_vine_flower", TealivyJadeVineFlowerBlock::new);
    public static final RegistryObject<Block> TANZASHROOM = registerBlock("tanzashroom", TanzashroomBlock::new);
    public static final RegistryObject<Block> TANZASHROOM_BLOCK = registerBlock("tanzashroom_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.HARD_CROP).strength(3f).lightLevel(s -> 12)){
        @Override
        public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
            return 0.5f;
        }
    });
    public static final RegistryObject<Block> TANZASHROOM_STEM = registerBlock("tanzashroom_stem", TanzashroomStemBlock::new);
    public static final RegistryObject<Block> DARK_WATER = registerBlock("dark_water", () -> new LiquidBlock(UnseenWorldFluids.DARK_WATER,
			BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(100f).lightLevel(s -> 8).noCollission().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable()){
		@Override
		public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
			if (!(entity instanceof MoonFishEntity || entity instanceof DustyPinkMaxorFishEntity || entity instanceof StrederEntity
					|| isHelmetProtected(entity) || entity instanceof ItemEntity
                    || entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(UnseenWorldMobEffects.DARK_IMMUNITE.get())
                    || entity instanceof Projectile)) {
				entity.setDeltaMovement(new Vec3(0, 0.05, 0));
				if (entity instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide())
					livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 1));
			}else if (entity instanceof ItemEntity itemEntity && itemEntity.getItem().is(ItemTags.FLOWERS))
                itemEntity.discard();
		}
        private boolean isHelmetProtected(Entity entity) {
            if (entity instanceof LivingEntity livingEntity) {
                ItemStack headSlotItem = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
                ITag<Item> helmetTag = ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(UnseenWorldTags.Items.DARK_WATER_PROTECTED_HELMETS.location()));
                return helmetTag.contains(headSlotItem.getItem());
            }
            return false;
        }
	});
    public static final RegistryObject<Block> LIQUID_OF_CHIMERY = registerBlock("liquid_of_chimery", () -> new LiquidBlock(UnseenWorldFluids.LIQUID_OF_CHIMERY, BlockBehaviour.Properties.of().mapColor(MapColor.WATER).strength(100f).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true).lightLevel(s -> 6).noCollission()
			.noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable()){
        @Override
        public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
            if ((entity instanceof ChimericPurplemarerEntity || entity instanceof ChimericRedmarerEntity) && entity.tickCount % 5 == 0)
                ((TamableAnimal) entity).heal(2);
        }
    });
    public static final RegistryObject<Block> THE_DARKNESS_PORTAL = registerBlock("the_darkness_portal", DarknessPortalBlock::new);
    public static final RegistryObject<Block> DEEP_GEM_ORE = registerBlock("deep_gem_ore", () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(6f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DEEP_GEM_BLOCK = registerBlock("deep_gem_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 10f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VOID_INGOT_ORE = registerBlock("void_ingot_ore", ()->new ModFacingableBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(7.5f).requiresCorrectToolForDrops()){
        @Override
        public int getExpDrop(BlockState state, LevelReader level, RandomSource randomSource, BlockPos pos, int fortuneLevel, int silkTouchLevel) {
            return silkTouchLevel == 0 ? UniformInt.of(0,5).sample(randomSource) : 0;
        }
    });
    public static final RegistryObject<Block> VOID_INGOT_BLOCK = registerBlock("void_ingot_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(7.5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> RED_TITANIUM_ORE = registerBlock("red_titanium_ore",()-> new ModFacingableBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.IRON_XYLOPHONE).sound(SoundType.NETHER_ORE).strength(10f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> RED_TITANIUM_BLOCK = registerBlock("red_titanium_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 10f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> UNSEEN_ORE = registerBlock("unseen_ore", ()->new ModFacingableBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BIT).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops().strength(5f).lightLevel(s -> 4).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true)));
    public static final RegistryObject<Block> UNSEEN_BLOCK = registerBlock("unseen_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 10f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> NATURERIUM_BLOCK = registerBlock("naturerium_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 10f).requiresCorrectToolForDrops()) {
        @Override
        public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction direction, IPlantable plantable) {
            return true;
        }
    });
    public static final RegistryObject<Block> COLD_DARK_BRICKS = registerBlock("cold_dark_bricks",()-> new ModFacingableBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(5f, 10f).lightLevel(s -> 8)));
    public static final RegistryObject<Block> GRASS_BLOCK_OF_SHINY_REDLIGHT = registerBlock("grass_block_of_shiny_redlight", ()-> new ModGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).sound(SoundType.DRIPSTONE_BLOCK).strength(1.5f, 4f).lightLevel(s -> 12)));
    public static final RegistryObject<Block> DARK_GRASS_BLOCK = registerBlock("dark_grass", DarkGrassBlock::new);
    public static final RegistryObject<Block> AMETHYST_GRASS_BLOCK = registerBlock("amethyst_grass_block",()->new ModGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).sound(SoundType.AMETHYST_CLUSTER).strength(1.5f, 10f).speedFactor(1.2f).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true)));
    public static final RegistryObject<Block> RED_OOZE = registerBlock("red_ooze", ()->new ModFacingableBlock(BlockBehaviour.Properties.of()
            .sound(new ForgeSoundType(1.0f, 1.0f, () -> SoundEvents.ROOTED_DIRT_BREAK, () -> SoundEvents.ROOTED_DIRT_STEP,
                    () -> SoundEvents.SOUL_SOIL_PLACE, () -> SoundEvents.SAND_HIT,
                    () -> SoundEvents.SOUL_SAND_FALL)).strength(4f)));
    public static final RegistryObject<Block> CRYSTALLIZED_DARK_SAND = registerBlock("crystallized_dark_sand", CrystallizedDarkSandBlock::new);
    public static final RegistryObject<Block> TEALIVE_LUMINOUS_GRASS_BLOCK = registerBlock("tealive_luminous_grass_block", () -> new ModGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
            .sound(new ForgeSoundType(1.0f, 1.0f, () -> SoundEvents.NYLIUM_BREAK, () -> SoundEvents.FUNGUS_STEP,
                    () -> SoundEvents.NYLIUM_PLACE, () -> SoundEvents.NYLIUM_HIT,
                    () -> SoundEvents.ROOTED_DIRT_FALL)).strength(2f).lightLevel(s -> 3).jumpFactor(1.2f)));
    private static final BlockBehaviour.Properties DARK_CRIMSON = BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(3.5f);
    public static final RegistryObject<Block> DARK_CRIMSON_LEAVES = registerBlock("dark_crimson_leaves", ()-> new DarkCrimsonLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).mapColor(MapColor.COLOR_BLACK).sound(SoundType.GRASS).strength(0.3f).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> DARK_CRIMSON_WOOD = registerBlock("dark_crimson_wood", () -> new ModRotatedPillarBlock(DARK_CRIMSON));
    public static final RegistryObject<Block> DARK_CRIMSON_LOG = registerBlock("dark_crimson_log", () -> new ModRotatedPillarBlock(DARK_CRIMSON));
    private static final BlockBehaviour.Properties PLANKLIKE_DARK_CRIMSON = BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(3f, 5f);
    public static final RegistryObject<Block> DARK_CRIMSON_PLANKS = registerBlock("dark_crimson_planks", () -> new ModFacingableBlock(PLANKLIKE_DARK_CRIMSON));
    public static final RegistryObject<Block> DARK_CRIMSON_STAIRS = registerBlock("dark_crimson_stairs", () -> new StairBlock(DARK_CRIMSON_PLANKS.get()::defaultBlockState,PLANKLIKE_DARK_CRIMSON));
    public static final RegistryObject<Block> DARK_CRIMSON_SLAB = registerBlock("dark_crimson_slab", () -> new SlabBlock(PLANKLIKE_DARK_CRIMSON));
    public static final RegistryObject<Block> DARK_CRIMSON_TRAPDOOR = registerBlock("dark_crimson_trapdoor", () -> new TrapDoorBlock(PLANKLIKE_DARK_CRIMSON.noOcclusion(),BlockSetType.CRIMSON));
    public static final RegistryObject<Block> DARK_CRIMSON_DOOR = registerDoubleBlock("dark_crimson_door", () -> new DoorBlock(PLANKLIKE_DARK_CRIMSON,BlockSetType.CRIMSON));
    public static final RegistryObject<Block> DARK_CRIMSON_FENCE_GATE = registerBlock("dark_crimson_fence_gate", () -> new FenceGateBlock(PLANKLIKE_DARK_CRIMSON.dynamicShape(), WoodType.CRIMSON));
    public static final RegistryObject<Block> DARK_CRIMSON_PRESSURE_PLATE = registerBlock("dark_crimson_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,PLANKLIKE_DARK_CRIMSON,BlockSetType.CRIMSON));
    public static final RegistryObject<Block> DARK_CRIMSON_BUTTON = registerBlock("dark_crimson_button", () -> new ButtonBlock(PLANKLIKE_DARK_CRIMSON.dynamicShape(), BlockSetType.CRIMSON, 30, true));
    public static final RegistryObject<Block> DARK_CRIMSON_FENCE = registerBlock("dark_crimson_fence", () -> new FenceBlock(PLANKLIKE_DARK_CRIMSON.dynamicShape()));
    private static final BlockBehaviour.Properties GRIZZLY = BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(5f);
    public static final RegistryObject<Block> GRIZZLY_WOOD = registerBlock("grizzly_wood", () -> new ModRotatedPillarBlock(GRIZZLY));
    public static final RegistryObject<Block> GRIZZLY_LOG = registerBlock("grizzly_log", () -> new ModRotatedPillarBlock(GRIZZLY));
    private static final BlockBehaviour.Properties PLANKLIKE_GRIZZLY = BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.NETHER_WOOD).strength(3f, 3f);
    public static final RegistryObject<Block> GRIZZLY_PLANKS = registerBlock("grizzly_planks", () -> new Block(PLANKLIKE_GRIZZLY));
    public static final RegistryObject<Block> GRIZZLY_LEAVES = registerBlock("grizzly_leaves", GrizzlyLeavesBlock::new);
    public static final RegistryObject<Block> GRIZZLY_STAIRS = registerBlock("grizzly_stairs", () -> new StairBlock(GRIZZLY_PLANKS.get()::defaultBlockState,PLANKLIKE_GRIZZLY));
    public static final RegistryObject<Block> GRIZZLY_SLAB = registerBlock("grizzly_slab", () -> new SlabBlock(PLANKLIKE_GRIZZLY));
    public static final RegistryObject<Block> GRIZZLY_TRAPDOOR = registerBlock("grizzly_trapdoor", () -> new TrapDoorBlock(PLANKLIKE_GRIZZLY.noOcclusion(),BlockSetType.JUNGLE));
    public static final RegistryObject<Block> GRIZZLY_DOOR = registerDoubleBlock("grizzly_door", () -> new DoorBlock(PLANKLIKE_GRIZZLY,BlockSetType.JUNGLE));
    public static final RegistryObject<Block> GRIZZLY_FENCE_GATE = registerBlock("grizzly_fence_gate", () -> new FenceGateBlock(PLANKLIKE_GRIZZLY.dynamicShape(), WoodType.JUNGLE));
    public static final RegistryObject<Block> GRIZZLY_PRESSURE_PLATE = registerBlock("grizzly_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,GRIZZLY,BlockSetType.JUNGLE));
    public static final RegistryObject<Block> GRIZZLY_BUTTON = registerBlock("grizzly_button", () -> new ButtonBlock(PLANKLIKE_GRIZZLY.dynamicShape(), BlockSetType.JUNGLE, 30, true));
    public static final RegistryObject<Block> GRIZZLY_FENCE = registerBlock("grizzly_fence", () -> new FenceBlock(PLANKLIKE_GRIZZLY));
    public static final RegistryObject<Block> GRIZZLY_LIGHT_BLOCK = registerBlock("grizzly_light_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.HONEY_BLOCK).strength(4f, 10f).lightLevel(s -> 14).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true)));
    public static final RegistryObject<Block> AMETHYST_LEAVES = registerBlock("amethyst_leaves", () -> new AmethystLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).mapColor(MapColor.COLOR_MAGENTA).sound(SoundType.GRASS).instabreak().noOcclusion().randomTicks()));
    private static final BlockBehaviour.Properties AMETHYST = BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).mapColor(MapColor.COLOR_MAGENTA).strength(3.25f, 3f);
    public static final RegistryObject<Block> AMETHYST_WOOD = registerBlock("amethyst_wood", () -> new ModRotatedPillarBlock(AMETHYST));
    public static final RegistryObject<Block> AMETHYST_LOG = registerBlock("amethyst_log", () -> new ModRotatedPillarBlock(AMETHYST));
    public static final RegistryObject<Block> AMETHYST_PLANKS = registerBlock("amethyst_planks", () -> new Block(AMETHYST));
    public static final RegistryObject<Block> AMETHYST_STAIRS = registerBlock("amethyst_stairs", () -> new StairBlock(AMETHYST_PLANKS.get()::defaultBlockState,AMETHYST));
    public static final RegistryObject<Block> AMETHYST_SLAB = registerBlock("amethyst_slab", () -> new SlabBlock(AMETHYST));
    public static final RegistryObject<Block> AMETHYST_TRAPDOOR = registerBlock("amethyst_trapdoor", () -> new TrapDoorBlock(AMETHYST.noOcclusion(),BlockSetType.ACACIA));
    public static final RegistryObject<Block> AMETHYST_DOOR = registerDoubleBlock("amethyst_door", () -> new DoorBlock(AMETHYST,BlockSetType.ACACIA));
    public static final RegistryObject<Block> AMETHYST_FENCE_GATE = registerBlock("amethyst_fence_gate", () -> new FenceGateBlock(AMETHYST,WoodType.ACACIA));
    public static final RegistryObject<Block> AMETHYST_PRESSURE_PLATE = registerBlock("amethyst_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,AMETHYST,BlockSetType.ACACIA));
    public static final RegistryObject<Block> AMETHYST_BUTTON = registerBlock("amethyst_button", () -> new ButtonBlock(AMETHYST,BlockSetType.ACACIA,60,true));
    public static final RegistryObject<Block> AMETHYST_FENCE = registerBlock("amethyst_fence", () -> new FenceBlock(AMETHYST));
    public static final RegistryObject<Block> TEALIVY_LEAVES = registerBlock("tealivy_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_LEAVES)));
    private static final BlockBehaviour.Properties TEALIVY = BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.NETHER_WOOD).strength(2.5f).lightLevel(s -> 4).mapColor(MapColor.COLOR_CYAN);
    public static final RegistryObject<Block> TEALIVY_WOOD = registerBlock("tealivy_wood", () -> new ModRotatedPillarBlock(TEALIVY));
    public static final RegistryObject<Block> TEALIVY_LOG = registerBlock("tealivy_log", () -> new ModRotatedPillarBlock(TEALIVY));
    private static final BlockBehaviour.Properties PLANKLIKE_TEALIVY = BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(2.5f);
    public static final RegistryObject<Block> TEALIVY_PLANKS = registerBlock("tealivy_planks", () -> new Block(PLANKLIKE_TEALIVY));
    public static final RegistryObject<Block> TEALIVY_STAIRS = registerBlock("tealivy_stairs", () -> new StairBlock(TEALIVY_PLANKS.get()::defaultBlockState, PLANKLIKE_TEALIVY));
    public static final RegistryObject<Block> TEALIVY_SLAB = registerBlock("tealivy_slab", () -> new SlabBlock(PLANKLIKE_TEALIVY));
    public static final RegistryObject<Block> TEALIVY_TRAPDOOR = registerBlock("tealivy_trapdoor", () -> new TrapDoorBlock(PLANKLIKE_TEALIVY.noOcclusion(),BlockSetType.BAMBOO));
    public static final RegistryObject<Block> TEALIVY_DOOR = registerDoubleBlock("tealivy_door", () -> new DoorBlock(PLANKLIKE_TEALIVY,BlockSetType.BAMBOO));
    public static final RegistryObject<Block> TEALIVY_FENCE_GATE = registerBlock("tealivy_fence_gate", () -> new FenceGateBlock(PLANKLIKE_TEALIVY,WoodType.BAMBOO));
    public static final RegistryObject<Block> TEALIVY_PRESSURE_PLATE = registerBlock("tealivy_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, PLANKLIKE_TEALIVY,BlockSetType.BAMBOO));
    public static final RegistryObject<Block> TEALIVY_BUTTON = registerBlock("tealivy_button", () -> new ButtonBlock(PLANKLIKE_TEALIVY,BlockSetType.BAMBOO,80,true));
    public static final RegistryObject<Block> TEALIVY_FENCE = registerBlock("tealivy_fence", () -> new FenceBlock(PLANKLIKE_TEALIVY));
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_LEAVES = registerBlock("greenish_burly_wood_leaves", GreenishBurlyWoodLeavesBlock::new);
    private static final BlockBehaviour.Properties GREENISH_BURLY_WOOD =BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(4.5f).mapColor(MapColor.COLOR_LIGHT_GREEN);
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_BLOCK = registerBlock("greenish_burly_wood_wood", () -> new ModRotatedPillarBlock(GREENISH_BURLY_WOOD));
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_LOG = registerBlock("greenish_burly_wood_log", () -> new ModRotatedPillarBlock(GREENISH_BURLY_WOOD));
    private static final BlockBehaviour.Properties PLANKLIKE_GREENISH_BURLY_WOOD = BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(4.5f).mapColor(MapColor.COLOR_GREEN);
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_PLANKS = registerBlock("greenish_burly_wood_planks", () -> new Block(PLANKLIKE_GREENISH_BURLY_WOOD));
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_STAIRS = registerBlock("greenish_burly_wood_stairs", () -> new StairBlock(GREENISH_BURLY_WOOD_PLANKS.get()::defaultBlockState, PLANKLIKE_GREENISH_BURLY_WOOD));
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_SLAB = registerBlock("greenish_burly_wood_slab", () -> new SlabBlock(PLANKLIKE_GREENISH_BURLY_WOOD));
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_TRAPDOOR = registerBlock("greenish_burly_wood_trapdoor", () -> new TrapDoorBlock(PLANKLIKE_GREENISH_BURLY_WOOD.noOcclusion(),BlockSetType.DARK_OAK));
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_DOOR = registerDoubleBlock("greenish_burly_wood_door", () -> new DoorBlock(PLANKLIKE_GREENISH_BURLY_WOOD,BlockSetType.DARK_OAK));
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_FENCE_GATE = registerBlock("greenish_burly_wood_fence_gate", () -> new FenceGateBlock(PLANKLIKE_GREENISH_BURLY_WOOD,WoodType.DARK_OAK));
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_PRESSURE_PLATE = registerBlock("greenish_burly_wood_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, PLANKLIKE_GREENISH_BURLY_WOOD,BlockSetType.DARK_OAK));
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_BUTTON = registerBlock("greenish_burly_wood_button", () -> new ButtonBlock(PLANKLIKE_GREENISH_BURLY_WOOD,BlockSetType.DARK_OAK,40,true));
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_FENCE = registerBlock("greenish_burly_wood_fence", () -> new FenceBlock(PLANKLIKE_GREENISH_BURLY_WOOD));
    public static final RegistryObject<Block> TANZASHROOM_LIGHT = registerBlock("tanzashroom_light", TanzashroomLightBlock::new);
    private static final BlockBehaviour.Properties TANZANITE = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(UnseenWorldSounds.TANZANITE).requiresCorrectToolForDrops().strength(5f).lightLevel(s -> 3);
    public static final RegistryObject<Block> TANZANITE_BLOCK = registerBlock("tanzanite_block", () -> new Block(TANZANITE));
    public static final RegistryObject<Block> TANZANITE_BLOCK_BUDDING = registerBlock("tanzanite_block_budding", TanzaniteBlockBuddingBlock::new);
    public static final RegistryObject<Block> TANZANITE_CLUSTER = registerBlock("tanzanite_cluster", TanzaniteClusterBlock::new);
    public static final RegistryObject<Block> TANZANITE_BRICKS = registerBlock("tanzanite_bricks", () -> new Block(TANZANITE));
    public static final RegistryObject<Block> TANZANITE_BRICKS_STAIRS = registerBlock("tanzanite_bricks_stairs", () -> new StairBlock(TANZANITE_BRICKS.get()::defaultBlockState,TANZANITE));
    public static final RegistryObject<Block> TANZANITE_BRICKS_SLAB = registerBlock("tanzanite_bricks_slab", () -> new SlabBlock(TANZANITE));
    public static final RegistryObject<Block> TANZANITE_BRICKS_WALL = registerBlock("tanzanite_bricks_wall", () -> new WallBlock(TANZANITE));
    private static final BlockBehaviour.Properties DARK_CURRANTSLATE_LIKE = BlockBehaviour.Properties.of().strength(2.5f,3).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_MAGENTA);
    public static final RegistryObject<Block> DARK_CURRANTSLATE = registerBlock("dark_currantslate",()->new Block(DARK_CURRANTSLATE_LIKE));
    public static final RegistryObject<Block> DARK_CURRANTSLATE_STAIRS = registerBlock("dark_currantslate_stairs",()->new StairBlock(DARK_CURRANTSLATE.get()::defaultBlockState,DARK_CURRANTSLATE_LIKE));
    public static final RegistryObject<Block> DARK_CURRANTSLATE_SLAB = registerBlock("dark_currantslate_slab",()->new SlabBlock(DARK_CURRANTSLATE_LIKE));
    public static final RegistryObject<Block> DARK_CURRANTSLATE_WALL = registerBlock("dark_currantslate_wall",()->new WallBlock(DARK_CURRANTSLATE_LIKE));
    private static final BlockBehaviour.Properties DARKNESS_ANCIENT = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(10f).lightLevel(s -> 6);
    public static final RegistryObject<Block> DARKNESS_ANCIENT_BRICKS = registerBlock("darkness_ancient_bricks", () -> new Block(DARKNESS_ANCIENT));
    public static final RegistryObject<Block> DARKNESS_ANCIENT_BRICKS_STAIRS = registerBlock("darkness_ancient_bricks_stairs", () -> new StairBlock(DARKNESS_ANCIENT_BRICKS.get()::defaultBlockState,DARKNESS_ANCIENT));
    public static final RegistryObject<Block> DARKNESS_ANCIENT_BRICKS_SLAB = registerBlock("darkness_ancient_bricks_slab", () -> new SlabBlock(DARKNESS_ANCIENT));
    public static final RegistryObject<Block> DARKNESS_ANCIENT_BRICKS_WALL = registerBlock("darkness_ancient_bricks_wall", () -> new WallBlock(DARKNESS_ANCIENT));
    private static final BlockBehaviour.Properties RED_DEEPSLATE = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BANJO).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_BRICKS).strength(5f).lightLevel(s -> 4);
    public static final RegistryObject<Block> RED_DEEPSLATE_BRICKS = registerBlock("reddeepslatebricks", () -> new ModFacingableBlock(RED_DEEPSLATE));
    public static final RegistryObject<Block> RED_DEEPSLATE_BRICKS_SLAB = registerBlock("reddeepslatebricksslab", () -> new SlabBlock(RED_DEEPSLATE));
    public static final RegistryObject<Block> RED_DEEPSLATE_BRICKS_STAIRS = registerBlock("reddeepslatebricksstairs", () -> new StairBlock(RED_DEEPSLATE_BRICKS.get()::defaultBlockState, RED_DEEPSLATE));
    public static final RegistryObject<Block> RED_DEEPSLATE_BRICKS_WALL = registerBlock("reddeepslatebricks_wall", () -> new WallBlock(RED_DEEPSLATE));
    private static final BlockBehaviour.Properties CHLORITE = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).requiresCorrectToolForDrops().strength(7.5f).lightLevel(s -> 3);
    public static final RegistryObject<Block> CHLORITE_SLATE_STONE = registerBlock("chlorite_slate_stone", () -> new Block(CHLORITE) {
        @Override
        public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
            return new ItemStack(UnseenWorldItems.CHLORITE_SLATE_STONE_SHARD.get());
        }
    });
    public static final RegistryObject<Block> CHLORITE_SLATE_STONE_BRICKS = registerBlock("chlorite_slate_stone_bricks", () -> new Block(CHLORITE));
    public static final RegistryObject<Block> CHLORITE_SLATE_STONE_BRICKS_STAIRS = registerBlock("chlorite_slate_stone_bricks_stairs", () -> new StairBlock(CHLORITE_SLATE_STONE_BRICKS.get()::defaultBlockState,CHLORITE));
    public static final RegistryObject<Block> CHLORITE_SLATE_STONE_BRICKS_SLAB = registerBlock("chlorite_slate_stone_bricks_slab", () -> new SlabBlock(CHLORITE));
    public static final RegistryObject<Block> CHLORITE_SLATE_STONE_BRICKS_WALL = registerBlock("chlorite_slate_stone_bricks_wall", () -> new WallBlock(CHLORITE.dynamicShape()));
    private static final BlockBehaviour.Properties REINFORCED_POLISHED_BLACKSTONE = BlockBehaviour.Properties.of().sound(SoundType.BASALT).requiresCorrectToolForDrops().strength(20f).lightLevel(s -> 1).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true);
    public static final RegistryObject<Block> REINFORCED_POLISHED_BLACKSTONE_BRICKS = registerBlock("reinforced_polished_blackstone_bricks", () -> new Block(REINFORCED_POLISHED_BLACKSTONE));
    public static final RegistryObject<Block> REINFORCED_POLISHED_BLACKSTONE_BRICKS_SLAB = registerBlock("reinforced_polished_blackstone_bricks_slab", () -> new SlabBlock(REINFORCED_POLISHED_BLACKSTONE));
    public static final RegistryObject<Block> ANCIENT_TRANSIENT_BLOCK_CLOSE = registerBlock("ancient_transient_block_close", AncientTransientBlockCloseBlock::new);
    public static final RegistryObject<Block> REINFORCED_RED_ANCIENT_BRICKS = registerBlock("reinforced_red_anient_bricks", () -> new ModRotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(20f)));
    public static final RegistryObject<Block> DRIPSTONE_OF_AMETHYST_OVERGROWTH = registerBlock("dripstone_of_amethyst_overgrowth", DripstoneOfAmethystOvergrowthBlock::new);
    public static final RegistryObject<Block> DEEP_WATER_ANFELTSIA = registerBlock("deep_water_anfeltsia", DeepWaterAnfeltsiaBlock::new);
    public static final RegistryObject<Block> GOLDEN_CHEST = registerBlock("goldenchest", GoldenChestBlock::new);
    public static final RegistryObject<Block> BLAZER_SUMMON_BLOCK = registerBlock("blazer_summon_block", BlazerSummonBlock::new);
    public static final RegistryObject<Block> THE_WITHER_KNIGHT_BLOCK = registerBlock("the_wither_knight_block", TheWitherKnightBlock::new);
    public static final RegistryObject<Block> ANCIENT_TRANSIENT_BLOCK_OPEN = registerBlock("ancient_transient_block_open", AncientTransientBlockOpenBlock::new);
    public static final RegistryObject<Block> UNDEAD_WARRIOR_OF_THE_CHIMERIC_DARKNESS = registerBlock("undead_warrior_of_the_chimeric_darkness", ()-> new ModFacingableBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.NYLIUM).strength(-1, 3500000).noOcclusion().noLootTable()){
        @Override
        public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            return box(-4, 0, -4, 20, 32, 20);
        }
    });
    public static final RegistryObject<Block> TOTEM_OF_GUDDY_BLAZE = registerBlock("totemof_guddy_blaze", TotemOfGuddyBlazeBlock::new);
    public static final RegistryObject<Block> BEACON_RUNE = registerBlock("beacon_rune", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).sound(SoundType.LODESTONE).strength(10f).requiresCorrectToolForDrops().hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true)));
    public static final RegistryObject<Block> BEACON_OF_WEAPONS = registerBlock("beacon_of_weapons", () -> new BeaconOfWeaponsBlock(BlockBehaviour.Properties.of().sound(UnseenWorldSounds.BEACON_OF_WEAPONS).strength(15f).lightLevel(s -> 5).speedFactor(0.5f).jumpFactor(0.5f).noOcclusion().hasPostProcess((bs, br, bp) -> true).requiresCorrectToolForDrops().emissiveRendering((bs, br, bp) -> true)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> RegistryObject<T> registerDoubleBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerDoubleBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return UnseenWorldItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    private static <T extends Block> RegistryObject<Item> registerDoubleBlockItem(String name, RegistryObject<T> block) {
        return UnseenWorldItems.ITEMS.register(name, () -> new DoubleHighBlockItem(block.get(), new Item.Properties()));
    }
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSideHandler {
        @SubscribeEvent
        public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
            AmethystGrass.blockColorLoad(event);
            TanzashroomBlock.blockColorLoad(event);
            TanzashroomStemBlock.blockColorLoad(event);
            CrystallizedDarkSandBlock.blockColorLoad(event);
            DeepWaterAnfeltsiaBlock.blockColorLoad(event);
        }

        @SubscribeEvent
        public static void itemColorLoad(RegisterColorHandlersEvent.Item event) {
            TanzashroomBlock.itemColorLoad(event);
            TanzashroomStemBlock.itemColorLoad(event);
        }
    }
}