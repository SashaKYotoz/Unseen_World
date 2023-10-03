package net.sashakyotoz.unseenworld.procedures;

import net.sashakyotoz.unseenworld.entity.DustyPinkMaxorFishEntity;
import net.sashakyotoz.unseenworld.entity.MoonfishEntity;
import net.sashakyotoz.unseenworld.entity.StrederEntity;
import net.sashakyotoz.unseenworld.init.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.init.UnseenWorldModMobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

public class DarkWaterMobplayerCollidesBlockProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
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
}
