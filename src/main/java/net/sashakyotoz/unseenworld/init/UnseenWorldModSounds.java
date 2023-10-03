
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

public class UnseenWorldModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, UnseenWorldMod.MODID);
	public static final RegistryObject<SoundEvent> AMETHYST_AMBIENT = REGISTRY.register("amethyst_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("unseen_world", "amethyst_ambient")));
	public static final RegistryObject<SoundEvent> TEALIVE_VALLEY_AMBIENT = REGISTRY.register("tealive_valley_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("unseen_world", "tealive_valley_ambient")));
	public static final RegistryObject<SoundEvent> CHIMERIC_SOUND = REGISTRY.register("chimeric_sound", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("unseen_world", "chimeric_sound")));
	public static final RegistryObject<SoundEvent> DARK_CHIMERIC_SOUND = REGISTRY.register("dark_chimeric_sound", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("unseen_world", "dark_chimeric_sound")));
	public static final RegistryObject<SoundEvent> HAPPY_PLACE = REGISTRY.register("happy_place", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("unseen_world", "happy_place")));
	public static final RegistryObject<SoundEvent> CHAOTIC = REGISTRY.register("chaotic", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("unseen_world", "chaotic")));
	public static final RegistryObject<SoundEvent> GRIZZLY_FOREST_AMBIENT = REGISTRY.register("grizzly_forest_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("unseen_world", "grizzly_forest_ambient")));
	public static final RegistryObject<SoundEvent> FIRE_STAFF_SHOT = REGISTRY.register("fire_staff_shot", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("unseen_world", "fire_staff_shot")));
}
