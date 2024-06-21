
package net.sashakyotoz.unseenworld.item;

import net.minecraft.sounds.SoundEvents;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.managers.ArmorAbilitiesProcedure;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public abstract class NatureriumArmorItem extends ArmorItem {
	private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.DARK_GREEN;
	private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
	public NatureriumArmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForType(ArmorItem.Type type) {
				return new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 30;
			}

			@Override
			public int getDefenseForType(ArmorItem.Type type) {
				return new int[]{4, 7, 9, 4}[type.getSlot().getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 18;
			}

			@Override
			public SoundEvent getEquipSound() {
				return SoundEvents.ARMOR_EQUIP_IRON;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get()));
			}

			@Override
			public String getName() {
				return "naturerium_armor";
			}

			@Override
			public float getToughness() {
				return 0f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0f;
			}
		}, type, properties);
	}

	public static class Helmet extends NatureriumArmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties());
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.naturerium_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/models/armor/naturerium_layer_1.png";
		}
	}

	public static class Chestplate extends NatureriumArmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties());
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.naturerium_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/models/armor/naturerium_layer_1.png";
		}

		@Override
		public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean p_41408_) {
			ArmorAbilitiesProcedure.execute(entity);
		}
	}

	public static class Leggings extends NatureriumArmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties());
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.naturerium_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/models/armor/naturerium_layer_2.png";
		}
	}

	public static class Boots extends NatureriumArmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties());
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.naturerium_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/models/armor/naturerium_layer_1.png";
		}
	}
}
