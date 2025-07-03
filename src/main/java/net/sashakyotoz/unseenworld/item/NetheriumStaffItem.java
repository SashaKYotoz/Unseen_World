
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
import net.sashakyotoz.unseenworld.entity.NetheriumStaffEntity;

public class NetheriumStaffItem extends Item {
	public NetheriumStaffItem() {
		super(new Item.Properties().durability(500));
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
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Ranged item modifier", 3, AttributeModifier.Operation.ADDITION));
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
		level.addParticle(new ColorableParticleOption("sparkle",1f,0.5f,0.1f), user.getX() + sin, user.getEyeY() - 0.5, user.getZ() + cos, 0, 0, 0);
	}
	@Override
	public void releaseUsing(ItemStack itemstack, Level world, LivingEntity entityLiving, int timeLeft) {
		if (!world.isClientSide() && entityLiving instanceof ServerPlayer player) {
			NetheriumStaffEntity entity = NetheriumStaffEntity.shoot(world, player, world.getRandom(), 3f, 2, 2);
			itemstack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));
			entity.pickup = AbstractArrow.Pickup.DISALLOWED;
			player.getCooldowns().addCooldown(itemstack.getItem(),20);
		}
	}
}
