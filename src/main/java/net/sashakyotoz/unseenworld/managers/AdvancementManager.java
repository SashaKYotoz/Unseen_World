package net.sashakyotoz.unseenworld.managers;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.block.DarknessPortalBlock;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldMobEffects;
import net.sashakyotoz.unseenworld.registries.UnseenWorldTags;

import java.util.HashMap;

public class AdvancementManager {
	private static final HashMap<ResourceLocation,ResourceLocation> ADVANCEMENT_MAP = new HashMap<>();
	public static final ResourceLocation WELCOME_TO_UNSEEN_WORLD_ADV = new ResourceLocation(UnseenWorldMod.MODID,"welcometo_unseen_world");
	public static final ResourceLocation UNSEEN_WORLD_ADV = new ResourceLocation(UnseenWorldMod.MODID,"goto_darkness");
	public static final ResourceLocation UNDER_THE_CHIMERIC_DARKNESS_ADV = new ResourceLocation(UnseenWorldMod.MODID,"under_the_chimeric_darkness");
	public static final ResourceLocation DARK_WARRIOR_ADV = new ResourceLocation(UnseenWorldMod.MODID,"biguncle");
	public static final ResourceLocation THE_BLAZER_ADV = new ResourceLocation(UnseenWorldMod.MODID,"the_blazer_advancement");
	public static final ResourceLocation THE_WITHER_KNIGHT_ADV = new ResourceLocation(UnseenWorldMod.MODID,"golden_knight");
	public static final ResourceLocation FOUND_TREASURE_ADV = new ResourceLocation(UnseenWorldMod.MODID,"found_treasure");
	public static final ResourceLocation RED_TITANIUM_INGOT_ADV = new ResourceLocation(UnseenWorldMod.MODID,"hotmetal");
	public static final ResourceLocation VOID_INGOT_ADV = new ResourceLocation(UnseenWorldMod.MODID,"isitingotfromvoid");
	public static final ResourceLocation NATURERIUM_INGOT_ADV = new ResourceLocation(UnseenWorldMod.MODID,"uncommoningot");
	public static final ResourceLocation FLIGHT_OF_FANTASIES_ADV = new ResourceLocation(UnseenWorldMod.MODID,"flight_of_fantasies");
	public static final ResourceLocation METEORITESTROPHY_ADV = new ResourceLocation(UnseenWorldMod.MODID,"survive_per_the_meteoritestrophy");
	public static final ResourceLocation STAFF_OF_VOID_ADV = new ResourceLocation(UnseenWorldMod.MODID,"staff_of_void");
	public static final ResourceLocation STAFF_OF_TANZANITE_ADV = new ResourceLocation(UnseenWorldMod.MODID,"tanzanite_staff_advancement");
	public static final ResourceLocation STAFF_OF_FIRE_ADV = new ResourceLocation(UnseenWorldMod.MODID,"staffof_fire");
	public static final ResourceLocation STAFF_OF_TEALIVY_FIRE_ADV = new ResourceLocation(UnseenWorldMod.MODID,"tealivy_fire_staff_advancement");
	public static final ResourceLocation UPGRADE_YOUR_SET_ADV = new ResourceLocation(UnseenWorldMod.MODID,"upgrade_your_set");
	public static final ResourceLocation PETIFICATE_THE_WAYWARD = new ResourceLocation(UnseenWorldMod.MODID,"petificate_the_wayward");
	static {
		ADVANCEMENT_MAP.put(WELCOME_TO_UNSEEN_WORLD_ADV,WELCOME_TO_UNSEEN_WORLD_ADV);
		ADVANCEMENT_MAP.put(UNSEEN_WORLD_ADV,UNSEEN_WORLD_ADV);
		ADVANCEMENT_MAP.put(UNDER_THE_CHIMERIC_DARKNESS_ADV,UNDER_THE_CHIMERIC_DARKNESS_ADV);
		ADVANCEMENT_MAP.put(DARK_WARRIOR_ADV,DARK_WARRIOR_ADV);
		ADVANCEMENT_MAP.put(THE_BLAZER_ADV,THE_BLAZER_ADV);
		ADVANCEMENT_MAP.put(THE_WITHER_KNIGHT_ADV,THE_WITHER_KNIGHT_ADV);
		ADVANCEMENT_MAP.put(FOUND_TREASURE_ADV,FOUND_TREASURE_ADV);
		ADVANCEMENT_MAP.put(RED_TITANIUM_INGOT_ADV,RED_TITANIUM_INGOT_ADV);
		ADVANCEMENT_MAP.put(VOID_INGOT_ADV,VOID_INGOT_ADV);
		ADVANCEMENT_MAP.put(NATURERIUM_INGOT_ADV,NATURERIUM_INGOT_ADV);
		ADVANCEMENT_MAP.put(FLIGHT_OF_FANTASIES_ADV,FLIGHT_OF_FANTASIES_ADV);
		ADVANCEMENT_MAP.put(METEORITESTROPHY_ADV,METEORITESTROPHY_ADV);
		ADVANCEMENT_MAP.put(STAFF_OF_VOID_ADV,STAFF_OF_VOID_ADV);
		ADVANCEMENT_MAP.put(STAFF_OF_TANZANITE_ADV,STAFF_OF_TANZANITE_ADV);
		ADVANCEMENT_MAP.put(STAFF_OF_FIRE_ADV,STAFF_OF_FIRE_ADV);
		ADVANCEMENT_MAP.put(STAFF_OF_TEALIVY_FIRE_ADV,STAFF_OF_TEALIVY_FIRE_ADV);
		ADVANCEMENT_MAP.put(UPGRADE_YOUR_SET_ADV,UPGRADE_YOUR_SET_ADV);
		ADVANCEMENT_MAP.put(PETIFICATE_THE_WAYWARD,PETIFICATE_THE_WAYWARD);
	}
	public static void addAdvancement(Player player, ResourceLocation location) {
		ResourceLocation tmpLocation = ADVANCEMENT_MAP.get(location);
		if (tmpLocation != null && player instanceof ServerPlayer serverPlayer) {
			awardAdvancement(serverPlayer, tmpLocation);
		}
	}
	private static void awardAdvancement(ServerPlayer player, ResourceLocation advancementLocation) {
		Advancement advancement = player.server.getAdvancements().getAdvancement(advancementLocation);
		AdvancementProgress ap = player.getAdvancements().getOrStartProgress(advancement);
		if (!ap.isDone()) {
			for (String criteria : ap.getRemainingCriteria()) {
				player.getAdvancements().award(advancement, criteria);
			}
		}
	}

	public static void everyTickCheckingAdvancements(Level level, Player entity) {
		if (entity == null)
			return;
		if (entity.getInventory().contains(new ItemStack(UnseenWorldItems.NATURERIUM_INGOT.get()))) {
			if (entity instanceof ServerPlayer player)
				addAdvancement(player,NATURERIUM_INGOT_ADV);
		}
		if (entity.getInventory().contains(new ItemStack(UnseenWorldItems.VOID_INGOT.get()))) {
			if (entity instanceof ServerPlayer player)
				addAdvancement(player,VOID_INGOT_ADV);
		}
		if (entity.getInventory().contains(new ItemStack(UnseenWorldItems.RED_TITANIUM_INGOT.get()))) {
			if (entity instanceof ServerPlayer player)
				addAdvancement(player,RED_TITANIUM_INGOT_ADV);
		}
		if (entity.getInventory().contains(new ItemStack(UnseenWorldItems.NETHERIUM_STAFF.get()))) {
			if (entity instanceof ServerPlayer player)
				addAdvancement(player,STAFF_OF_FIRE_ADV);
		}
		if (entity.getY() <= 0 && level.dimension().equals(DarknessPortalBlock.CHIMERIC_DARKNESS)) {
			if (entity instanceof ServerPlayer player)
				addAdvancement(player,UNDER_THE_CHIMERIC_DARKNESS_ADV);
		}
		if (level.dimension().equals(DarknessPortalBlock.CHIMERIC_DARKNESS)) {
			if (entity instanceof ServerPlayer player)
				addAdvancement(player,UNSEEN_WORLD_ADV);
		}
		if (entity.getInventory().contains(UnseenWorldTags.Items.TREASURE_WEAPONS)) {
			if (entity instanceof ServerPlayer player)
				addAdvancement(player,FOUND_TREASURE_ADV);
		}
		if (entity.getMainHandItem().is(UnseenWorldTags.Items.TREASURE_WEAPONS)
				&& entity.getMainHandItem().getOrCreateTag().getDouble("CustomModelData") > 0) {
			if (entity instanceof ServerPlayer player)
				addAdvancement(player,UPGRADE_YOUR_SET_ADV);
		}
		if (entity.hasEffect(UnseenWorldMobEffects.METEORITESTROPHY.get())) {
			if (entity instanceof ServerPlayer player)
				addAdvancement(player,METEORITESTROPHY_ADV);
		}
		if (entity.hasEffect(UnseenWorldMobEffects.REDUCED_OF_GRAVITY.get())) {
			if (entity instanceof ServerPlayer player)
				addAdvancement(player,FLIGHT_OF_FANTASIES_ADV);
		}
	}
}