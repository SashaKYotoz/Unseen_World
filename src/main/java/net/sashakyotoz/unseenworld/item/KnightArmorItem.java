
package net.sashakyotoz.unseenworld.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.sashakyotoz.unseenworld.client.model.ModelThe_Wither_Knight_Armor;
import net.sashakyotoz.unseenworld.procedures.KnightArmorBodyTickEventProcedure;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class KnightArmorItem extends ArmorItem {
	private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.GOLD;
	private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
	public KnightArmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForType(ArmorItem.Type type) {
				return new int[]{13, 15, 16, 12}[type.getSlot().getIndex()] * 60;
			}

			@Override
			public int getDefenseForType(ArmorItem.Type type) {
				return new int[]{9, 12, 14, 9}[type.getSlot().getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 30;
			}

			@Override
			public SoundEvent getEquipSound() {
				return SoundEvents.ARMOR_EQUIP_NETHERITE;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.NETHERITE_INGOT));
			}

			@Override
			public String getName() {
				return "knight_armor";
			}

			@Override
			public float getToughness() {
				return 4f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0.1f;
			}
		}, type, properties);
	}

	public static class Helmet extends KnightArmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties().fireResistant());
		}

		@Override
		public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
			return true;
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("head", new ModelThe_Wither_Knight_Armor(Minecraft.getInstance().getEntityModels().bakeLayer(ModelThe_Wither_Knight_Armor.LAYER_LOCATION)).Head, "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"body", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_arm",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.knight_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/entities/the_wither_knight_armor.png";
		}
	}

	public static class Chestplate extends KnightArmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant());
		}
		@Override
		public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
			return true;
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("body", new ModelThe_Wither_Knight_Armor(Minecraft.getInstance().getEntityModels().bakeLayer(ModelThe_Wither_Knight_Armor.LAYER_LOCATION)).Body, "left_arm",
									new ModelThe_Wither_Knight_Armor(Minecraft.getInstance().getEntityModels().bakeLayer(ModelThe_Wither_Knight_Armor.LAYER_LOCATION)).LeftArm, "right_arm",
									new ModelThe_Wither_Knight_Armor(Minecraft.getInstance().getEntityModels().bakeLayer(ModelThe_Wither_Knight_Armor.LAYER_LOCATION)).RightArm, "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.knight_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/entities/the_wither_knight_armor.png";
		}
		@Override
		public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean p_41408_) {
			KnightArmorBodyTickEventProcedure.execute(entity);
		}
	}

	public static class Leggings extends KnightArmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant());
		}
		@Override
		public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
			return true;
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("left_leg", new ModelThe_Wither_Knight_Armor(Minecraft.getInstance().getEntityModels().bakeLayer(ModelThe_Wither_Knight_Armor.LAYER_LOCATION)).LeftLeg, "right_leg",
									new ModelThe_Wither_Knight_Armor(Minecraft.getInstance().getEntityModels().bakeLayer(ModelThe_Wither_Knight_Armor.LAYER_LOCATION)).RightLeg, "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_arm",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.knight_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/entities/the_wither_knight.png";
		}
	}

	public static class Boots extends KnightArmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties().fireResistant());
		}
		@Override
		public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
			return true;
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("left_leg", new ModelThe_Wither_Knight_Armor(Minecraft.getInstance().getEntityModels().bakeLayer(ModelThe_Wither_Knight_Armor.LAYER_LOCATION)).LeftBoot, "right_leg",
									new ModelThe_Wither_Knight_Armor(Minecraft.getInstance().getEntityModels().bakeLayer(ModelThe_Wither_Knight_Armor.LAYER_LOCATION)).RightBoot, "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_arm",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, world, list, flag);
			list.add(Component.translatable("item.unseen_world.armor_trips").withStyle(TITLE_FORMAT));
			list.add(CommonComponents.EMPTY);
			list.add(Component.translatable("item.unseen_world.knight_armor_trips").withStyle(DESCRIPTION_FORMAT));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "unseen_world:textures/entities/the_wither_knight.png";
		}
	}
}
