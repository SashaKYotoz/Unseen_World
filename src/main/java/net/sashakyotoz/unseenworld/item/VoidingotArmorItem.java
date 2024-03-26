
package net.sashakyotoz.unseenworld.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.managers.ArmorAbilitiesProcedure;

import java.util.List;

public abstract class VoidingotArmorItem extends ArmorItem {
	private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.DARK_AQUA;
	private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
	public VoidingotArmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForType(ArmorItem.Type type) {
				return new int[]{13, 15, 16, 12}[type.getSlot().getIndex()] * 40;
			}

			@Override
			public int getDefenseForType(ArmorItem.Type type) {
				return new int[]{6, 10, 12, 6}[type.getSlot().getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 20;
			}

			@Override
			public SoundEvent getEquipSound() {
				return SoundEvents.ARMOR_EQUIP_NETHERITE;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.NETHERITE_INGOT), new ItemStack(UnseenWorldModItems.VOID_INGOT.get()));
			}

			@Override
			public String getName() {
				return "voidingot_armor";
			}

			@Override
			public float getToughness() {
				return 2f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0.1f;
			}
		}, type, properties);
	}

	public static class Helmet extends VoidingotArmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties().fireResistant());
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.void_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/models/armor/void_ingot_layer_1.png";
		}
	}

	public static class Chestplate extends VoidingotArmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant());
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.void_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/models/armor/void_ingot_layer_1.png";
		}

		@Override
		public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean p_41408_) {
			ArmorAbilitiesProcedure.execute(entity);
		}
	}

	public static class Leggings extends VoidingotArmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant());
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.void_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/models/armor/void_ingot_layer_2.png";
		}
	}

	public static class Boots extends VoidingotArmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties().fireResistant());
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.void_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/models/armor/void_ingot_layer_1.png";
		}
	}
}
