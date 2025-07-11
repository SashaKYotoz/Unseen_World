package net.sashakyotoz.unseenworld.registries;

import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.unseenworld.UnseenWorldMod;

public class UnseenWorldPaintings {
	public static final DeferredRegister<PaintingVariant> REGISTRY = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, UnseenWorldMod.MODID);
	public static final RegistryObject<PaintingVariant> AMETHYST_FOREST_PAINTING = REGISTRY.register("amethyst_forest_painting", () -> new PaintingVariant(32, 16));
	public static final RegistryObject<PaintingVariant> GRIZZLY_FOREST_PAINTING = REGISTRY.register("grizzly_forest_painting", () -> new PaintingVariant(16, 32));
}
