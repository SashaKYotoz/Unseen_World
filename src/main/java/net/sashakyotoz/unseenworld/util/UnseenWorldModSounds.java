
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.util;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

public class UnseenWorldModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, UnseenWorldMod.MODID);
	public static final SoundEvent BIOME_AMETHYST_FOREST = create("biome.amethyst_forest");
	public static final SoundEvent BIOME_TEALIVE_VALLEY = create("biome.tealivy_valley");
	public static final SoundEvent BIOME_GRIZZLY_FOREST = create("biome.grizzly_forest");
	public static final SoundEvent BIOME_DARK_CHIMERIC = create("biome.dark_chimeric");
	public static final SoundEvent ITEM_STAFF_SHOT = create("item.staff_shot");
	public static final RegistryObject<SoundEvent> CHIMERIC_SOUND = REGISTRY.register("chimeric_sound", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("unseen_world", "chimeric_sound")));
	public static final RegistryObject<SoundEvent> HAPPY_PLACE = REGISTRY.register("happy_place", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("unseen_world", "happy_place")));
	public static final RegistryObject<SoundEvent> CHAOTIC = REGISTRY.register("chaotic", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("unseen_world", "chaotic")));
	private static SoundEvent create(String name) {
		ResourceLocation location = new ResourceLocation(UnseenWorldMod.MODID,name);
		SoundEvent sound = SoundEvent.createVariableRangeEvent(location);
		ForgeRegistries.SOUND_EVENTS.register(location, sound);
		return sound;
	}
}
