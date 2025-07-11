
package net.sashakyotoz.unseenworld.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.unseenworld.entity.TealivyFireStaffEntity;

public class TealivyFireStaffItem extends Item {
	public TealivyFireStaffItem() {
		super(new Item.Properties().durability(658));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		entity.startUsingItem(hand);
		return new InteractionResultHolder(InteractionResult.SUCCESS, entity.getItemInHand(hand));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.CROSSBOW;
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 72000;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getDefaultAttributeModifiers(slot));
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Ranged item modifier", 4, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Ranged item modifier", -2.4, AttributeModifier.Operation.ADDITION));
			return builder.build();
		}
		return super.getDefaultAttributeModifiers(slot);
	}
	@Override
	public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		super.onUseTick(level, user, stack, remainingUseTicks);
		float sin = (float) Math.sin(remainingUseTicks * Math.PI / 10);
		float cos = (float) Math.cos(remainingUseTicks * Math.PI / 10);
		level.addParticle(new ColorableParticleOption("cube",0.15f,0.5f,1f), user.getX() + sin, user.getEyeY() - 1, user.getZ() + cos, 0, 0, 0);
	}

	@Override
	public void releaseUsing(ItemStack itemstack, Level level, LivingEntity entityLiving, int timeLeft) {
		if (!level.isClientSide() && entityLiving instanceof ServerPlayer player) {
			TealivyFireStaffEntity entityarrow = TealivyFireStaffEntity.shoot(level, player, level.getRandom(), 2.5f, 3.5, 2);
			itemstack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));
			entityarrow.pickup = AbstractArrow.Pickup.DISALLOWED;
			player.getCooldowns().addCooldown(itemstack.getItem(),20);
		}
	}
}
