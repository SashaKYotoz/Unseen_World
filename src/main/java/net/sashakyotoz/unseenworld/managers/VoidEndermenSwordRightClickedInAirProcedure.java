package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.BlockPos;

public class VoidEndermenSwordRightClickedInAirProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double speed = 0.75;
		double Yaw;
		Yaw = entity.getYRot();
		entity.setDeltaMovement(new Vec3((speed * Math.cos((Yaw + 90) * (Math.PI / 180))), (entity.getXRot() * (-0.025)), (speed * Math.sin((Yaw + 90) * (Math.PI / 180)))));
		if (world.getBlockState(BlockPos.containing(x, y - 2, z)).canOcclude() || world.getBlockState(BlockPos.containing(x, y - 3, z)).canOcclude() || world.getBlockState(BlockPos.containing(x, y - 4, z)).canOcclude()) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 60, 0));
		}
	}
}
