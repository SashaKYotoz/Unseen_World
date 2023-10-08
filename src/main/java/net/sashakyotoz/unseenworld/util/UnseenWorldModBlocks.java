
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
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
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.block.*;
import net.sashakyotoz.unseenworld.block.entity.BeaconOfWeaponsBlockEntity;
import net.sashakyotoz.unseenworld.entity.DustyPinkMaxorFishEntity;
import net.sashakyotoz.unseenworld.entity.MoonfishEntity;
import net.sashakyotoz.unseenworld.entity.StrederEntity;
import net.sashakyotoz.unseenworld.world.treegrowers.*;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class UnseenWorldModBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, UnseenWorldMod.MODID);
    public static final RegistryObject<Block> SMALL_CRIMSERRY_SOUL_BERRY = REGISTRY.register("small_crimserry_soul_berry", SmallCrimserrySoulBerryBlock::new);
    public static final RegistryObject<Block> MISTERYFLOWER_SAPLING = REGISTRY.register("misteryflower_sapling", MisteryflowerSaplingBlock::new);
    public static final RegistryObject<Block> DARK_CRIMSON_FENCE = REGISTRY.register("dark_crimson_fence", () -> new FenceBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(4f, 6f).requiresCorrectToolForDrops().dynamicShape()){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> GRIZZLY_FENCE = REGISTRY.register("grizzly_fence", () -> new FenceBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.NETHER_WOOD).strength(6f, 9f).dynamicShape()){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> AMETHYST_FENCE = REGISTRY.register("amethyst_fence", () -> new FenceBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(3.2f, 4.8f).dynamicShape()){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_FENCE = REGISTRY.register("greenish_burly_wood_fence", () -> new FenceBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(10f, 15f).requiresCorrectToolForDrops().dynamicShape()){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> TEALIVY_FENCE = REGISTRY.register("tealivy_fence", () -> new FenceBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(3f, 4.5f).dynamicShape()){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> NIGHTDEW_OF_CHIMERIC_DARKNESS = REGISTRY.register("nightdew_of_chimeric_darkness", () -> new NightdewOfChimericDarknessBlock());
    public static final RegistryObject<Block> OUT_GROWT_APPLE_BUSH = REGISTRY.register("out_growt_apple_bush", OutGrowtAppleBushBlock::new);
    public static final RegistryObject<Block> DARK_CRIMSON_FLOWING_AZALIA = REGISTRY.register("dark_crimson_flowing_azalia", () -> new DarkCrimsonFlowingAzaliaBlock());
    public static final RegistryObject<Block> DARK_CRIMSON_AZALEA = REGISTRY.register("dark_crimson_azalea", () -> new DarkCrimsonAzaleaBlock());
    public static final RegistryObject<Block> DARK_CRIMSON_VINE_FLOWER = REGISTRY.register("dark_crimson_vine_flower", () -> new DarkCrimsonVineFlowerBlock());
    public static final RegistryObject<Block> LUMINOUSAMETHYSTVINE = REGISTRY.register("luminousamethystvine", LuminousamethystvineBlock::new);
    public static final RegistryObject<Block> DARKCRIMSONSAPLING = REGISTRY.register("darkcrimsonsapling", () -> new SaplingBlock(new DarkcrimsonTreeGrower(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)){
        @Override
        public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
            return groundState.is(UnseenWorldModBlocks.DARK_GRASS.get()) || groundState.is(UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get()) || groundState.is(Blocks.GRASS_BLOCK) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.PODZOL)
                    || groundState.is(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()) || groundState.is(Blocks.MOSS_BLOCK);
        }
    });
    public static final RegistryObject<Block> AMETHYST_TREE_SAPLING = REGISTRY.register("amethyst_tree_sapling", () -> new SaplingBlock(new AmethystTreeGrower(),BlockBehaviour.Properties.copy(DARKCRIMSONSAPLING.get())){
        @Override
        public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
            return groundState.is(UnseenWorldModBlocks.DARK_GRASS.get()) || groundState.is(UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get()) || groundState.is(Blocks.GRASS_BLOCK) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.PODZOL)
                    || groundState.is(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()) || groundState.is(Blocks.MOSS_BLOCK);
        }
    });
    public static final RegistryObject<Block> TEALIVY_TREE_SAPLING = REGISTRY.register("tealivy_tree_sapling", () -> new SaplingBlock(new TealivyTreeGrower(),BlockBehaviour.Properties.copy(AMETHYST_TREE_SAPLING.get())){
        @Override
        public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
            return groundState.is(UnseenWorldModBlocks.DARK_GRASS.get()) || groundState.is(UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get()) || groundState.is(Blocks.GRASS_BLOCK) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.PODZOL)
                    || groundState.is(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()) || groundState.is(Blocks.MOSS_BLOCK);
        }
    });
    public static final RegistryObject<Block> GREENISH_BURLYWOOD_SAPLING = REGISTRY.register("greenish_burlywood_sapling", () -> new SaplingBlock(new GreenishBurlywoodTreeGrower(),BlockBehaviour.Properties.copy(AMETHYST_TREE_SAPLING.get())){
        @Override
        public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
            return groundState.is(UnseenWorldModBlocks.DARK_GRASS.get()) || groundState.is(UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get()) || groundState.is(Blocks.GRASS_BLOCK) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.PODZOL)
                    || groundState.is(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()) || groundState.is(Blocks.MOSS_BLOCK);
        }
    });
    public static final RegistryObject<Block> GRIZZLY_SAPLING = REGISTRY.register("grizzly_sapling", () -> new SaplingBlock(new GrizzlyTreeGrower(),BlockBehaviour.Properties.copy(GREENISH_BURLYWOOD_SAPLING.get())){
        @Override
        public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
            return groundState.is(UnseenWorldModBlocks.DARK_GRASS.get()) || groundState.is(UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get()) || groundState.is(Blocks.GRASS_BLOCK) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.PODZOL)
                    || groundState.is(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()) || groundState.is(Blocks.MOSS_BLOCK);
        }
    });
    public static final RegistryObject<Block> GROWN_CRIMSERRY_SOUL_BERRY = REGISTRY.register("grown_crimserry_soul_berry", GrownCrimserrySoulBerryBlock::new);
    public static final RegistryObject<Block> AMETHYST_GRASS = REGISTRY.register("amethyst_grass", AmethystGrassBlock::new);
    public static final RegistryObject<Block> TEALIVY_PLUMERIA = REGISTRY.register("tealivy_plumeria", TealivyPlumeriaBlock::new);
    public static final RegistryObject<Block> TEALIVY_JADE_VINE_FLOWER = REGISTRY.register("tealivy_jade_vine_flower", TealivyJadeVineFlowerBlock::new);
    public static final RegistryObject<Block> TANZASHROOM = REGISTRY.register("tanzashroom", TanzashroomBlock::new);
    public static final RegistryObject<Block> TANZASHROOM_BLOCK = REGISTRY.register("tanzashroom_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.HARD_CROP).strength(2f, 4f).lightLevel(s -> 12)){
        @Override
        public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
            return 0.5f;
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> TANZASHROOM_STEM = REGISTRY.register("tanzashroom_stem", () -> new TanzashroomStemBlock());
    public static final RegistryObject<Block> DARK_WATER = REGISTRY.register("dark_water", () -> new LiquidBlock(UnseenWorldModFluids.DARK_WATER,
			BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(100f).lightLevel(s -> 8).noCollission().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable()){
		@Override
		public void entityInside(BlockState p_60495_, Level p_60496_, BlockPos p_60497_, Entity entity) {
			if (!(entity instanceof MoonfishEntity || entity instanceof DustyPinkMaxorFishEntity || entity instanceof StrederEntity
					|| (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == UnseenWorldModItems.NATURERIUM_ARMOR_HELMET.get()
					|| (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == UnseenWorldModItems.DEEP_GEM_ARMOR_HELMET.get()
					|| (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == UnseenWorldModItems.RED_TITANIUM_ARMOR_HELMET.get()
					|| (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == UnseenWorldModItems.VOIDINGOT_ARMOR_HELMET.get()
					|| entity instanceof LivingEntity _livEnt11 && _livEnt11.hasEffect(UnseenWorldModMobEffects.DARK_IMMUNITE.get()))) {
				entity.setDeltaMovement(new Vec3(0, 0.05, 0));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 1));
			}
		}
	});
    public static final RegistryObject<Block> LIQUID_OF_CHIMERY = REGISTRY.register("liquid_of_chimery", () -> new LiquidBlock(UnseenWorldModFluids.LIQUID_OF_CHIMERY, BlockBehaviour.Properties.of().mapColor(MapColor.WATER).strength(100f).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true).lightLevel(s -> 4).noCollission()
			.noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable()));
    public static final RegistryObject<Block> THE_DARKNESS_PORTAL = REGISTRY.register("the_darkness_portal", TheDarknessPortalBlock::new);
    public static final RegistryObject<Block> DEEP_GEM_ORE = REGISTRY.register("deep_gem_ore", () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(5f, 12f).requiresCorrectToolForDrops()){
        @Override
        public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
            if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
                return tieredItem.getTier().getLevel() >= 3;
            return false;
        }
    });
    public static final RegistryObject<Block> DEEP_GEM_BLOCK = REGISTRY.register("deep_gem_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 10f).requiresCorrectToolForDrops()){
		@Override
		public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
			if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
				return tieredItem.getTier().getLevel() >= 3;
			return false;
		}
		@Override
		public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
			List<ItemStack> dropsOriginal = super.getDrops(state, builder);
			if (!dropsOriginal.isEmpty())
				return dropsOriginal;
			return Collections.singletonList(new ItemStack(this, 1));
		}
	});
    public static final RegistryObject<Block> VOID_INGOT_ORE = REGISTRY.register("void_ingot_ore", Void_ingotOreBlock::new);
    public static final RegistryObject<Block> VOID_INGOT_BLOCK = REGISTRY.register("void_ingot_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 10f).requiresCorrectToolForDrops()) {
        @Override
        public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
            if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
                return tieredItem.getTier().getLevel() >= 2;
            return false;
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> RED_TITANIUM_ORE = REGISTRY.register("red_titanium_ore", Red_TitaniumOreBlock::new);
    public static final RegistryObject<Block> RED_TITANIUM_BLOCK = REGISTRY.register("red_titanium_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 10f).requiresCorrectToolForDrops()) {
        @Override
        public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
            if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
                return tieredItem.getTier().getLevel() >= 4;
            return false;
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> UNSEEN_ORE = REGISTRY.register("unseen_ore", UnseenOreBlock::new);
    public static final RegistryObject<Block> UNSEEN_BLOCK = REGISTRY.register("unseen_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 10f).requiresCorrectToolForDrops()) {
        @Override
        public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
            if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
                return tieredItem.getTier().getLevel() >= 3;
            return false;
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> NATURERIUM_BLOCK = REGISTRY.register("naturerium_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(6.5f, 10f).requiresCorrectToolForDrops()) {
        @Override
        public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction direction, IPlantable plantable) {
            return true;
        }

        @Override
        public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
            if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
                return tieredItem.getTier().getLevel() >= 3;
            return false;
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> COLD_DARK_BRICKS = REGISTRY.register("cold_dark_bricks", ColdDarkBricksBlock::new);
    public static final RegistryObject<Block> GRASS_BLOCK_OF_SHINY_REDLIGHT = REGISTRY.register("grass_block_of_shiny_redlight", GrassBlockOfShinyRedlightBlock::new);
    public static final RegistryObject<Block> DARK_GRASS = REGISTRY.register("dark_grass", DarkGrassBlock::new);
    public static final RegistryObject<Block> AMETHYST_GRASS_BLOCK = REGISTRY.register("amethyst_grass_block", AmethystGrassBlockBlock::new);
    public static final RegistryObject<Block> RED_OOZE = REGISTRY.register("red_ooze", RedOozeBlock::new);
    public static final RegistryObject<Block> CRYSTALLIZED_DARK_SAND = REGISTRY.register("crystallized_dark_sand", CrystallizedDarkSandBlock::new);
    public static final RegistryObject<Block> TEALIVE_LUMINOUS_GRASS_BLOCK = REGISTRY.register("tealive_luminous_grass_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
            .sound(new ForgeSoundType(1.0f, 1.0f, () -> SoundEvents.NYLIUM_BREAK, () -> SoundEvents.FUNGUS_STEP,
                    () -> SoundEvents.NYLIUM_PLACE, () -> SoundEvents.NYLIUM_HIT,
                    () -> SoundEvents.ROOTED_DIRT_FALL))
            .strength(1.5f, 6f).lightLevel(s -> 3).jumpFactor(1.2f)){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }

        @Override
        public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
            return true;
        }
    });
    public static final RegistryObject<Block> DARK_CRIMSON_LEAVES = REGISTRY.register("dark_crimson_leaves", Dark_crimsonLeavesBlock::new);
    public static final RegistryObject<Block> DARK_CRIMSON_WOOD = REGISTRY.register("dark_crimson_wood", () -> new Dark_crimsonWoodBlock());
    public static final RegistryObject<Block> DARK_CRIMSON_LOG = REGISTRY.register("dark_crimson_log", () -> new Dark_crimsonLogBlock());
    public static final RegistryObject<Block> DARK_CRIMSON_PLANKS = REGISTRY.register("dark_crimson_planks", () -> new Dark_crimsonPlanksBlock());
    public static final RegistryObject<Block> DARK_CRIMSON_STAIRS = REGISTRY.register("dark_crimson_stairs", () -> new Dark_crimsonStairsBlock());
    public static final RegistryObject<Block> DARK_CRIMSON_SLAB = REGISTRY.register("dark_crimson_slab", () -> new Dark_crimsonSlabBlock());
    public static final RegistryObject<Block> DARK_CRIMSON_TRAPDOOR = REGISTRY.register("dark_crimson_trapdoor", () -> new DarkCrimsonTrapdoorBlock());
    public static final RegistryObject<Block> DARK_CRIMSON_DOOR = REGISTRY.register("dark_crimson_door", () -> new DarkCrimsonDoorBlock());
    public static final RegistryObject<Block> GRIZZLY_WOOD = REGISTRY.register("grizzly_wood", () -> new GrizzlyWoodBlock());
    public static final RegistryObject<Block> GRIZZLY_LOG = REGISTRY.register("grizzly_log", () -> new GrizzlyLogBlock());
    public static final RegistryObject<Block> GRIZZLY_PLANKS = REGISTRY.register("grizzly_planks", () -> new GrizzlyPlanksBlock());
    public static final RegistryObject<Block> GRIZZLY_LEAVES = REGISTRY.register("grizzly_leaves", GrizzlyLeavesBlock::new);
    public static final RegistryObject<Block> GRIZZLY_STAIRS = REGISTRY.register("grizzly_stairs", () -> new GrizzlyStairsBlock());
    public static final RegistryObject<Block> GRIZZLY_SLAB = REGISTRY.register("grizzly_slab", () -> new GrizzlySlabBlock());
    public static final RegistryObject<Block> GRIZZLY_TRAPDOOR = REGISTRY.register("grizzly_trapdoor", () -> new GrizzlyTrapdoorBlock());
    public static final RegistryObject<Block> GRIZZLY_DOOR = REGISTRY.register("grizzly_door", () -> new GrizzlyDoorBlock());
    public static final RegistryObject<Block> GRIZZLY_LIGHT_BLOCK = REGISTRY.register("grizzly_light_block", () -> new GrizzlyLightBlockBlock());
    public static final RegistryObject<Block> AMETHYST_LEAVES = REGISTRY.register("amethyst_leaves", () -> new AmethystLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).mapColor(MapColor.COLOR_MAGENTA).sound(SoundType.GRASS).strength(0.1f, 0.2f).requiresCorrectToolForDrops().noOcclusion().hasPostProcess((bs, br, bp) -> true)
            .emissiveRendering((bs, br, bp) -> true)));
    public static final RegistryObject<Block> AMETHYST_WOOD = REGISTRY.register("amethyst_wood", () -> new AmethystWoodBlock());
    public static final RegistryObject<Block> AMETHYST_LOG = REGISTRY.register("amethyst_log", () -> new AmethystLogBlock());
    public static final RegistryObject<Block> AMETHYST_PLANKS = REGISTRY.register("amethyst_planks", () -> new AmethystPlanksBlock());
    public static final RegistryObject<Block> AMETHYST_STAIRS = REGISTRY.register("amethyst_stairs", () -> new AmethystStairsBlock());
    public static final RegistryObject<Block> AMETHYST_SLAB = REGISTRY.register("amethyst_slab", () -> new AmethystSlabBlock());
    public static final RegistryObject<Block> AMETHYST_TRAPDOOR = REGISTRY.register("amethyst_trapdoor", () -> new AmethystTrapdoorBlock());
    public static final RegistryObject<Block> AMETHYST_DOOR = REGISTRY.register("amethyst_door", () -> new AmethystDoorBlock());
    public static final RegistryObject<Block> TEALIVY_LEAVES = REGISTRY.register("tealivy_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_LEAVES)));
    public static final RegistryObject<Block> TEALIVY_WOOD = REGISTRY.register("tealivy_wood", () -> new TealivyWoodBlock());
    public static final RegistryObject<Block> TEALIVY_LOG = REGISTRY.register("tealivy_log", () -> new TealivyLogBlock());
    public static final RegistryObject<Block> TEALIVY_PLANKS = REGISTRY.register("tealivy_planks", () -> new TealivyPlanksBlock());
    public static final RegistryObject<Block> TEALIVY_STAIRS = REGISTRY.register("tealivy_stairs", () -> new TealivyStairsBlock());
    public static final RegistryObject<Block> TEALIVY_SLAB = REGISTRY.register("tealivy_slab", () -> new TealivySlabBlock());
    public static final RegistryObject<Block> TEALIVY_TRAPDOOR = REGISTRY.register("tealivy_trapdoor", () -> new TealivyTrapdoorBlock());
    public static final RegistryObject<Block> TEALIVY_DOOR = REGISTRY.register("tealivy_door", () -> new TealivyDoorBlock());
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_LEAVES = REGISTRY.register("greenish_burly_wood_leaves", () -> new Greenish_BurlyWoodLeavesBlock());
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_WOOD = REGISTRY.register("greenish_burly_wood_wood", () -> new Greenish_BurlyWoodWoodBlock());
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_LOG = REGISTRY.register("greenish_burly_wood_log", () -> new Greenish_BurlyWoodLogBlock());
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_PLANKS = REGISTRY.register("greenish_burly_wood_planks", () -> new Greenish_BurlyWoodPlanksBlock());
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_STAIRS = REGISTRY.register("greenish_burly_wood_stairs", () -> new Greenish_BurlyWoodStairsBlock());
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_SLAB = REGISTRY.register("greenish_burly_wood_slab", () -> new Greenish_BurlyWoodSlabBlock());
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_TRAPDOOR = REGISTRY.register("greenish_burly_wood_trapdoor", () -> new GreenishBurlyWoodTrapdoorBlock());
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_DOOR = REGISTRY.register("greenish_burly_wood_door", () -> new GreenishBurlyWoodDoorBlock());
    public static final RegistryObject<Block> TANZASHROOM_LIGHT = REGISTRY.register("tanzashroom_light", TanzashroomLightBlock::new);
    public static final RegistryObject<Block> TANZANITE_BLOCK = REGISTRY.register("tanzanite_block", () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM)
            .sound(new ForgeSoundType(1.0f, 1.0f, () -> SoundEvents.LARGE_AMETHYST_BUD_BREAK, () -> SoundEvents.AMETHYST_BLOCK_PLACE,
                    () -> SoundEvents.LARGE_AMETHYST_BUD_PLACE, () -> SoundEvents.AMETHYST_BLOCK_HIT,
                    () -> SoundEvents.AMETHYST_CLUSTER_FALL)).strength(5f, 10f).lightLevel(s -> 3).requiresCorrectToolForDrops()) {
        @Override
        public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
            if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
                return tieredItem.getTier().getLevel() >= 1;
            return false;
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> TANZANITE_BLOCK_BUDDING = REGISTRY.register("tanzanite_block_budding", TanzaniteBlockBuddingBlock::new);
    public static final RegistryObject<Block> TANZANITE_CLUSTER = REGISTRY.register("tanzanite_cluster", TanzaniteClusterBlock::new);
    public static final RegistryObject<Block> TANZANITE_BRICKS = REGISTRY.register("tanzanite_bricks", () -> new TanzaniteBricksBlock());
    public static final RegistryObject<Block> TANZANITE_BRICKS_STAIRS = REGISTRY.register("tanzanite_bricks_stairs", () -> new TanzaniteBricksStairsBlock());
    public static final RegistryObject<Block> TANZANITE_BRICKS_SLAB = REGISTRY.register("tanzanite_bricks_slab", () -> new TanzaniteBricksSlabBlock());
    public static final RegistryObject<Block> TANZANITE_BRICKS_WALL = REGISTRY.register("tanzanite_bricks_wall", () -> new TanzaniteBricksWallBlock());
    public static final RegistryObject<Block> DARKNESS_ANCIENT_BRICKS = REGISTRY.register("dakness_ancient_bricks", () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(10f).lightLevel(s -> 5).requiresCorrectToolForDrops()){
		@Override
		public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
			if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
				return tieredItem.getTier().getLevel() >= 2;
			return false;
		}

		@Override
		public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
			List<ItemStack> dropsOriginal = super.getDrops(state, builder);
			if (!dropsOriginal.isEmpty())
				return dropsOriginal;
			return Collections.singletonList(new ItemStack(this, 1));
		}
	});
    public static final RegistryObject<Block> DARKNESS_ANCIENT_BRICKS_STAIRS = REGISTRY.register("dakness_ancient_bricks_stairs", () -> new StairBlock(DARKNESS_ANCIENT_BRICKS.get().defaultBlockState(),BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(10f).lightLevel(s -> 5).requiresCorrectToolForDrops()){
		@Override
		public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
			if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
				return tieredItem.getTier().getLevel() >= 2;
			return false;
		}
		@Override
		public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
			List<ItemStack> dropsOriginal = super.getDrops(state, builder);
			if (!dropsOriginal.isEmpty())
				return dropsOriginal;
			return Collections.singletonList(new ItemStack(this, 1));
		}
	});
    public static final RegistryObject<Block> DARKNESS_ANCIENT_BRICKS_SLAB = REGISTRY.register("dakness_ancient_bricks_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(10f).lightLevel(s -> 5).requiresCorrectToolForDrops().dynamicShape()){
		@Override
		public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
			if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
				return tieredItem.getTier().getLevel() >= 2;
			return false;
		}

		@Override
		public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
			List<ItemStack> dropsOriginal = super.getDrops(state, builder);
			if (!dropsOriginal.isEmpty())
				return dropsOriginal;
			return Collections.singletonList(new ItemStack(this, 1));
		}
	});
    public static final RegistryObject<Block> DARKNESS_ANCIENT_BRICKS_WALL = REGISTRY.register("dakness_ancient_bricks_wall", () -> new WallBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(10f).lightLevel(s -> 5).requiresCorrectToolForDrops().noOcclusion().isRedstoneConductor((bs, br, bp) -> false).dynamicShape()){
		public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
			if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
				return tieredItem.getTier().getLevel() >= 2;
			return false;
		}
		@Override
		public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
			List<ItemStack> dropsOriginal = super.getDrops(state, builder);
			if (!dropsOriginal.isEmpty())
				return dropsOriginal;
			return Collections.singletonList(new ItemStack(this, 1));
		}
	});
    public static final RegistryObject<Block> REDDEEPSLATEBRICKS = REGISTRY.register("reddeepslatebricks", () -> new ReddeepslatebricksBlock());
    public static final RegistryObject<Block> REDDEEPSLATEBRICKSSLAB = REGISTRY.register("reddeepslatebricksslab", () -> new ReddeepslatebricksslabBlock());
    public static final RegistryObject<Block> REDDEEPSLATEBRICKSSTAIRS = REGISTRY.register("reddeepslatebricksstairs", () -> new ReddeepslatebricksstairsBlock());
    public static final RegistryObject<Block> REDDEEPSLATEBRICKS_FENCE = REGISTRY.register("reddeepslatebricks_fence", () -> new ReddeepslatebricksFenceBlock());
    public static final RegistryObject<Block> CHLORITE_SLATE_STONE = REGISTRY.register("chlorite_slate_stone", () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(10f, 5f).lightLevel(s -> 3).requiresCorrectToolForDrops()) {
        @Override
        public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
            return new ItemStack(UnseenWorldModItems.CHLORITE_SLATE_STONE_SHARD.get());
        }

        @Override
        public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
            if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
                return tieredItem.getTier().getLevel() >= 2;
            return false;
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> CHLORITE_SLATE_STONE_BRICKS = REGISTRY.register("chlorite_slate_stone_bricks", () -> new ChloriteSlateStoneBricksBlock());
    public static final RegistryObject<Block> CHLORITE_SLATE_STONE_BRICKS_STAIRS = REGISTRY.register("chlorite_slate_stone_bricks_stairs", () -> new ChloriteSlateStoneBricksStairsBlock());
    public static final RegistryObject<Block> CHLORITE_SLATE_STONE_BRICKS_SLAB = REGISTRY.register("chlorite_slate_stone_bricks_slab", () -> new ChloriteSlateStoneBricksSlabBlock());
    public static final RegistryObject<Block> CHLORITE_SLATE_STONE_BRICKS_WALL = REGISTRY.register("chlorite_slate_stone_bricks_wall", () -> new ChloriteSlateStoneBricksWallBlock());
    public static final RegistryObject<Block> REINFORCED_POLISHED_BLACKSTONE_BRICKS = REGISTRY.register("reinforced_polished_blackstone_bricks", () -> new ReinforcedPolishedBlackstoneBricksBlock());
    public static final RegistryObject<Block> REINFORCED_POLISHED_BLACKSTONE_BRICKS_SLAB = REGISTRY.register("reinforced_polished_blackstone_bricks_slab", () -> new ReinforcedPolishedBlackstoneBricksSlabBlock());
    public static final RegistryObject<Block> ANCIENT_TRANSIENT_BLOCK_CLOSE = REGISTRY.register("ancient_transient_block_close", () -> new AncientTransientBlockCloseBlock());
    public static final RegistryObject<Block> REINFORCED_RED_ANIENT_BRICKS = REGISTRY.register("reinforced_red_anient_bricks", () -> new ReinforcedRedAnientBricksBlock());
    public static final RegistryObject<Block> BEACON_RUNE = REGISTRY.register("beacon_rune", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_MAGENTA).sound(SoundType.LODESTONE).strength(25f, 10f).requiresCorrectToolForDrops().hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true)) {
        @Override
        public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
            if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
                return tieredItem.getTier().getLevel() >= 2;
            return false;
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> BEACON_OF_WEAPONS = REGISTRY.register("beacon_of_weapons", () -> new BaseEntityBlock(BlockBehaviour.Properties.of()
            .sound(new ForgeSoundType(1.0f, 1.0f, () -> SoundEvents.LODESTONE_BREAK, () -> SoundEvents.SCULK_BLOCK_SPREAD,
                    () -> SoundEvents.LODESTONE_HIT, () -> SoundEvents.NETHER_BRICKS_HIT,
                    () -> SoundEvents.SCULK_CATALYST_FALL))
            .strength(15f, 5f).lightLevel(s -> 5).requiresCorrectToolForDrops().speedFactor(0.5f).jumpFactor(0.5f).noOcclusion().hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true)) {
        public static final List<BlockPos> BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2).filter((p_207914_) -> Math.abs(p_207914_.getX()) == 2 || Math.abs(p_207914_.getZ()) == 2).map(BlockPos::immutable).toList();
        public static boolean isValidRune(Level p_207910_, BlockPos p_207911_, BlockPos p_207912_) {
            return p_207910_.getBlockState(p_207911_.offset(p_207912_)).is(BEACON_RUNE.get()) && p_207910_.getBlockState(p_207911_.offset(p_207912_.getX() / 2, p_207912_.getY(), p_207912_.getZ() / 2)).is(BlockTags.REPLACEABLE);
        }
        public void animateTick(BlockState p_221092_, Level p_221093_, BlockPos p_221094_, RandomSource p_221095_) {
            super.animateTick(p_221092_, p_221093_, p_221094_, p_221095_);
            for(BlockPos blockpos : BOOKSHELF_OFFSETS) {
                if (p_221095_.nextInt(16) == 0 && isValidRune(p_221093_, p_221094_, blockpos)) {
                    p_221093_.addParticle(UnseenWorldModParticleTypes.REDNESS.get(), (double)p_221094_.getX() + 0.5D, (double)p_221094_.getY() + 2.0D, (double)p_221094_.getZ() + 0.5D, (double)((float)blockpos.getX() + p_221095_.nextFloat()) - 0.5D, (double)((float)blockpos.getY() - p_221095_.nextFloat() - 1.0F), (double)((float)blockpos.getZ() + p_221095_.nextFloat()) - 0.5D);
                }
            }
        }

        @Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
            return new BeaconOfWeaponsBlockEntity(p_153215_,p_153216_);
        }
        public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof BeaconOfWeaponsBlockEntity) {
                Containers.dropContents(worldIn, pos, (BeaconOfWeaponsBlockEntity) tileentity);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
        @Override
        public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            return Shapes.empty();
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            return box(0, 0, 0, 16, 14, 16);
        }

        @Override
        public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
            if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
                return tieredItem.getTier().getLevel() >= 2;
            return false;
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> DRIPSTONE_OF_AMETHYST_OVERGROWTH = REGISTRY.register("dripstone_of_amethyst_overgrowth", DripstoneOfAmethystOvergrowthBlock::new);
    public static final RegistryObject<Block> DEEP_WATER_ANFELTSIA = REGISTRY.register("deep_water_anfeltsia", DeepWaterAnfeltsiaBlock::new);
    public static final RegistryObject<Block> GOLDENCHEST = REGISTRY.register("goldenchest", GoldenchestBlock::new);
    public static final RegistryObject<Block> DARK_CRIMSON_FENCE_GATE = REGISTRY.register("dark_crimson_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(4f, 6f).requiresCorrectToolForDrops().dynamicShape(), WoodType.OAK){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> DARK_CRIMSON_PRESSURE_PLATE = REGISTRY.register("dark_crimson_pressure_plate", () -> new Dark_crimsonPressurePlateBlock());
    public static final RegistryObject<Block> DARK_CRIMSON_BUTTON = REGISTRY.register("dark_crimson_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(4f, 6f).requiresCorrectToolForDrops().dynamicShape(), BlockSetType.WARPED, 30, true){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> GRIZZLY_FENCE_GATE = REGISTRY.register("grizzly_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.NETHER_WOOD).strength(6f, 9f).dynamicShape(), WoodType.WARPED){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> GRIZZLY_PRESSURE_PLATE = REGISTRY.register("grizzly_pressure_plate", () -> new GrizzlyPressurePlateBlock());
    public static final RegistryObject<Block> GRIZZLY_BUTTON = REGISTRY.register("grizzly_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.NETHER_WOOD).strength(6f, 9f).dynamicShape(), BlockSetType.WARPED, 30, true){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> AMETHYST_FENCE_GATE = REGISTRY.register("amethyst_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(3.2f, 4.8f).dynamicShape(), WoodType.OAK){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> AMETHYST_PRESSURE_PLATE = REGISTRY.register("amethyst_pressure_plate", () -> new AmethystPressurePlateBlock());
    public static final RegistryObject<Block> AMETHYST_BUTTON = REGISTRY.register("amethyst_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(3.2f, 4.8f).dynamicShape(), BlockSetType.OAK, 30, true){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_FENCE_GATE = REGISTRY.register("greenish_burly_wood_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(10f, 15f).dynamicShape(), WoodType.OAK){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_PRESSURE_PLATE = REGISTRY.register("greenish_burly_wood_pressure_plate", () -> new Greenish_BurlyWoodPressurePlateBlock());
    public static final RegistryObject<Block> GREENISH_BURLY_WOOD_BUTTON = REGISTRY.register("greenish_burly_wood_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(8f, 15f).requiresCorrectToolForDrops().dynamicShape(), BlockSetType.OAK, 30, true){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> TEALIVY_FENCE_GATE = REGISTRY.register("tealivy_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(3f, 4.5f).dynamicShape(), WoodType.OAK){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> TEALIVY_PRESSURE_PLATE = REGISTRY.register("tealivy_pressure_plate", () -> new TealivyPressurePlateBlock());
    public static final RegistryObject<Block> TEALIVY_BUTTON = REGISTRY.register("tealivy_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(3f, 4.5f).dynamicShape(), BlockSetType.OAK, 30, true){
        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }
    });
    public static final RegistryObject<Block> BLAZER_SUMMON_BLOCK = REGISTRY.register("blazer_summon_block", () -> new BlazerSummonBlockBlock());
    public static final RegistryObject<Block> DARK_CRIMSON_BLOOMING_VINE = REGISTRY.register("dark_crimson_blooming_vine", DarkCrimsonBloomingVineBlock::new);
    public static final RegistryObject<Block> THE_WITHER_KNIGHT_BLOCK = REGISTRY.register("the_wither_knight_block", () -> new TheWitherKnightBlockBlock());
    public static final RegistryObject<Block> MISTERYFLOWER_WITH_FEW_BERRIES = REGISTRY.register("misteryflower_with_few_berries", () -> new MisteryflowerWithFewBerriesBlock());
    public static final RegistryObject<Block> MISTERYFLOWER_BERRIES = REGISTRY.register("misteryflower_berries", () -> new MisteryflowerBerriesBlock());
    public static final RegistryObject<Block> ANCIENT_TRANSIENT_BLOCK_OPEN = REGISTRY.register("ancient_transient_block_open", () -> new AncientTransientBlockOpenBlock());
    public static final RegistryObject<Block> UNDEAD_WARRIOR_OF_THE_CHIMERIC_DARKNESS = REGISTRY.register("undead_warrior_of_the_chimeric_darkness", UndeadWarriorOfTheChimericDarknessBlock::new);
    public static final RegistryObject<Block> TOTEMOF_GUDDY_BLAZE = REGISTRY.register("totemof_guddy_blaze", TotemofGuddyBlazeBlock::new);

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSideHandler {
        @SubscribeEvent
        public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
            AmethystGrassBlock.blockColorLoad(event);
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
