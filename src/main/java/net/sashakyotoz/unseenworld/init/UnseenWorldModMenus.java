
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.world.inventory.GoldenChestGUIMenu;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

public class UnseenWorldModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, UnseenWorldMod.MODID);
	public static final RegistryObject<MenuType<GoldenChestGUIMenu>> GOLDEN_CHEST_GUI = REGISTRY.register("golden_chest_gui", () -> IForgeMenuType.create(GoldenChestGUIMenu::new));
}
