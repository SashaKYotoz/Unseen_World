package net.sashakyotoz.unseenworld.registries;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
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
	public static final SoundEvent HAPPY_PLACE = create("happy_place");
	public static final SoundEvent CHAOTIC = create("chaotic");
	public static final SoundType TANZANITE = new ForgeSoundType(1.0f, 1.0f, () -> SoundEvents.LARGE_AMETHYST_BUD_BREAK, () -> SoundEvents.AMETHYST_BLOCK_PLACE,
			() -> SoundEvents.LARGE_AMETHYST_BUD_PLACE, () -> SoundEvents.AMETHYST_BLOCK_HIT,
			() -> SoundEvents.AMETHYST_CLUSTER_FALL);
	public static final SoundType BEACON_OF_WEAPONS = new ForgeSoundType(1.0f, 1.0f, () -> SoundEvents.LODESTONE_BREAK, () -> SoundEvents.SCULK_BLOCK_SPREAD,
			() -> SoundEvents.LODESTONE_HIT, () -> SoundEvents.NETHER_BRICKS_HIT,
			() -> SoundEvents.SCULK_CATALYST_FALL);
	private static SoundEvent create(String name) {
		ResourceLocation location = new ResourceLocation(UnseenWorldMod.MODID,name);
		SoundEvent sound = SoundEvent.createVariableRangeEvent(location);
		ForgeRegistries.SOUND_EVENTS.register(location, sound);
		return sound;
	}
}
