package net.sashakyotoz.unseenworld.registries;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.effects.*;

public class UnseenWorldMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, UnseenWorldMod.MODID);
	public static final RegistryObject<MobEffect> DARK_VOID = REGISTRY.register("dark_void", DarkVoidMobEffect::new);
	public static final RegistryObject<MobEffect> UNSEENIUM = REGISTRY.register("unseenium", UnseeniumMobEffect::new);
	public static final RegistryObject<MobEffect> SOUL_OVERGROWTH = REGISTRY.register("soul_overgrowth", SoulOvergrowthMobEffect::new);
	public static final RegistryObject<MobEffect> DARK_IMMUNITE = REGISTRY.register("dark_immunite", DarkImmuniteMobEffect::new);
	public static final RegistryObject<MobEffect> METEORITESTROPHY = REGISTRY.register("meteoritestrophy", MeteoritestrophyMobEffect::new);
	public static final RegistryObject<MobEffect> REDUCED_OF_GRAVITY = REGISTRY.register("reduced_of_gravity", ReducedOfGravityMobEffect::new);
}
