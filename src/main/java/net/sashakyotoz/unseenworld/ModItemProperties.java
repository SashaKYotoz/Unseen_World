
package net.sashakyotoz.unseenworld;

import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.item.ItemProperties;

import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;

public class ModItemProperties {
	public static void addCustomItemProperties() {
		makeBow(UnseenWorldModItems.VOID_BOW.get());
		makeShield(UnseenWorldModItems.BLAZER_SHIELD.get());
	}

	private static void makeBow(Item item) {
		ItemProperties.register(item, new ResourceLocation("pull"), (stack, level, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				return livingEntity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
			}
		});
		ItemProperties.register(item, new ResourceLocation("pulling"), (stack, level, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	private static void makeShield(Item item) {
		ItemProperties.register(item, new ResourceLocation("blocking"), (stack, level, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F);
	}
}
