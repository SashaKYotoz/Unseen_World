
package net.sashakyotoz.unseenworld.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;


public class VoidUpgradeSmithingTemplateItem extends SmithingTemplateItem {
	private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
	private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
	private static final Component VOID_UPGRADE = Component.translatable("upgrade.unseen_world.void_upgrade").withStyle(TITLE_FORMAT);
	private static final Component VOID_UPGRADE_APPLIES_TO = Component.translatable("item.unseen_world.smithing_template.void_upgrade.applies_to").withStyle(DESCRIPTION_FORMAT);
	private static final Component VOID_UPGRADE_INGREDIENTS = Component.translatable("item.unseen_world.smithing_template.void_upgrade.ingredients").withStyle(DESCRIPTION_FORMAT);
	private static final Component VOID_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable("item.unseen_world.smithing_template.void_upgrade.base_slot_description");
	private static final Component NETHERITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable("item.unseen_world.smithing_template.void_upgrade.additions_slot_description");
	private static final ResourceLocation EMPTY_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
	private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
	private static final ResourceLocation EMPTY_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
	private static final ResourceLocation EMPTY_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
	private static final ResourceLocation EMPTY_SLOT_HOE = new ResourceLocation("item/empty_slot_hoe");
	private static final ResourceLocation EMPTY_SLOT_AXE = new ResourceLocation("item/empty_slot_axe");
	private static final ResourceLocation EMPTY_SLOT_SWORD = new ResourceLocation("item/empty_slot_sword");
	private static final ResourceLocation EMPTY_SLOT_SHOVEL = new ResourceLocation("item/empty_slot_shovel");
	private static final ResourceLocation EMPTY_SLOT_PICKAXE = new ResourceLocation("item/empty_slot_pickaxe");
	private static final ResourceLocation EMPTY_SLOT_INGOT = new ResourceLocation("item/empty_slot_ingot");
	public VoidUpgradeSmithingTemplateItem() {
		super(VOID_UPGRADE_APPLIES_TO, VOID_UPGRADE_INGREDIENTS, VOID_UPGRADE, VOID_UPGRADE_BASE_SLOT_DESCRIPTION, NETHERITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, createVoidUpgradeIconList(), createVoidUpgradeMaterialList());
	}
	private static List<ResourceLocation> createVoidUpgradeIconList() {
		return List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_SWORD, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_AXE, EMPTY_SLOT_BOOTS, EMPTY_SLOT_HOE, EMPTY_SLOT_SHOVEL);
	}
	private static List<ResourceLocation> createVoidUpgradeMaterialList() {
		return List.of(EMPTY_SLOT_INGOT);
	}
}
