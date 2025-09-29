package net.sashakyotoz.unseenworld.registries;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.client.gui.GoldenChestGUIMenu;

public class UnseenWorldMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, UnseenWorldMod.MODID);
	public static final RegistryObject<MenuType<GoldenChestGUIMenu>> GOLDEN_CHEST_GUI = REGISTRY.register("golden_chest_gui", () -> IForgeMenuType.create(GoldenChestGUIMenu::new));
}
