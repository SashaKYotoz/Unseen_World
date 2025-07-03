
package net.sashakyotoz.unseenworld.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.UseOnContext;
import net.sashakyotoz.unseenworld.managers.LivingEntityIsHitWithTreasureWeaponProcedure;
import net.sashakyotoz.unseenworld.managers.TreasureWeaponOnBeaconClick;

import java.util.UUID;

public class LightTulvarItem extends SwordItem {
	public static final UUID KNOCKBACK = UUID.fromString("27f95289-09cd-40bf-9f1e-2eb9ac2bb130");
	public LightTulvarItem() {
		super(ModTiers.LIGHT_TULVAR, 3, -2, new Item.Properties().fireResistant());
	}
	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		if (equipmentSlot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
			builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(KNOCKBACK, "Tool modifier", 1f, AttributeModifier.Operation.ADDITION));
			return builder.build();
		}
		return super.getDefaultAttributeModifiers(equipmentSlot);
	}
	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		LivingEntityIsHitWithTreasureWeaponProcedure.onHit(entity, itemstack);
		return super.hurtEnemy(itemstack, entity, sourceentity);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		super.useOn(context);
		TreasureWeaponOnBeaconClick.onClick(context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getItemInHand());
		return InteractionResult.SUCCESS;
	}
}
