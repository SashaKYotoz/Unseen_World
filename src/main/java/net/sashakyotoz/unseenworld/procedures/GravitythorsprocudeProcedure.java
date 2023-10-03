package net.sashakyotoz.unseenworld.procedures;

import net.sashakyotoz.unseenworld.init.UnseenWorldModEnchantments;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class GravitythorsprocudeProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		if (event != null && event.getEntity() != null) {
			execute(event, event.getEntity().getX(), event.getEntity().getZ(), event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(double x, double z, Entity entity, Entity sourceentity) {
		execute(null, x, z, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, double x, double z, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		double enchantLevel = 0;
		if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getEnchantmentLevel(UnseenWorldModEnchantments.GRAVITY_SPIKE.get()) > 0) {
			enchantLevel = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getEnchantmentLevel(UnseenWorldModEnchantments.GRAVITY_SPIKE.get());
			sourceentity.setDeltaMovement(new Vec3(x, (enchantLevel / 2), z));
		}
	}
}
