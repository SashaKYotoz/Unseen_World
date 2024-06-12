
package net.sashakyotoz.unseenworld.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.GameRules;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModParticleTypes;
import net.sashakyotoz.unseenworld.world.teleporter.TheDarknessPortalShape;
import net.sashakyotoz.unseenworld.world.teleporter.TheDarknessTeleporter;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.Optional;

public class DarknessPortalBlock extends NetherPortalBlock {
	public static ResourceKey<Level> CHIMERIC_DARKNESS = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(UnseenWorldMod.MODID,"the_darkness"));
	public DarknessPortalBlock() {
		super(BlockBehaviour.Properties.of().noCollission().randomTicks().pushReaction(PushReaction.BLOCK).strength(-1.0F).sound(SoundType.GLASS).lightLevel(s -> 8).noLootTable());
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level.dimensionType().natural() && level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && random.nextInt(2000) < level.getDifficulty().getId()) {
			while(level.getBlockState(pos).is(this)) {
				pos = pos.below();
			}

			if (level.getBlockState(pos).isValidSpawn(level, pos, UnseenWorldModEntities.DARK_SKELETON.get())) {
				Entity entity =UnseenWorldModEntities.DARK_SKELETON.get().spawn(level, pos.above(), MobSpawnType.STRUCTURE);
				if (entity != null)
					entity.setPortalCooldown();
			}
		}
	}

	public static void portalSpawn(Level world, BlockPos pos) {
		Optional<TheDarknessPortalShape> optional = TheDarknessPortalShape.findEmptyPortalShape(world, pos, Direction.Axis.X);
		optional.ifPresent(TheDarknessPortalShape::createPortalBlocks);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor accessor, BlockPos pos, BlockPos p_54933_) {
		Direction.Axis direction$axis = direction.getAxis();
		Direction.Axis direction$axis1 = state.getValue(AXIS);
		boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
		return !flag && !blockState.is(this) && !(new TheDarknessPortalShape(accessor, pos, direction$axis1)).isComplete() ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, blockState, accessor, pos, p_54933_);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		for (int i = 0; i < 4; i++) {
			double px = pos.getX() + random.nextFloat();
			double py = pos.getY() + random.nextFloat();
			double pz = pos.getZ() + random.nextFloat();
			double vx = (random.nextFloat() - 0.5) / 2.;
			double vy = (random.nextFloat() - 0.5) / 2.;
			double vz = (random.nextFloat() - 0.5) / 2.;
			int j = random.nextInt(4) - 1;
			if (level.getBlockState(pos.west()).getBlock() != this && level.getBlockState(pos.east()).getBlock() != this) {
				px = pos.getX() + 0.5 + 0.25 * j;
				vx = random.nextFloat() * 2 * j;
			} else {
				pz = pos.getZ() + 0.5 + 0.25 * j;
				vz = random.nextFloat() * 2 * j;
			}
			level.addParticle(UnseenWorldModParticleTypes.BLUE_VOID_PARTICLE.get(), px, py, pz, vx, vy, vz);
		}
		if (random.nextInt(110) == 0)
			level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5f, random.nextFloat() * 0.4f + 0.8f);
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity.canChangeDimensions() && !entity.level().isClientSide()) {
			if (entity.isOnPortalCooldown()) {
				entity.setPortalCooldown();
			} else if (entity.level().dimension() != CHIMERIC_DARKNESS) {
				entity.setPortalCooldown();
				teleportToDimension(entity, pos, CHIMERIC_DARKNESS);
			} else {
				entity.setPortalCooldown();
				teleportToDimension(entity, pos, Level.OVERWORLD);
			}
		}
	}

	private void teleportToDimension(Entity entity, BlockPos pos, ResourceKey<Level> destinationType) {
		entity.changeDimension(entity.getServer().getLevel(destinationType), new TheDarknessTeleporter(entity.getServer().getLevel(destinationType), pos));
	}
}
