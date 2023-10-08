package net.sashakyotoz.unseenworld.util;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import net.sashakyotoz.unseenworld.entity.VoidStaffEntity;
import net.sashakyotoz.unseenworld.entity.VoidEndermenEntity;
import net.sashakyotoz.unseenworld.entity.VoidBowEntity;
import net.sashakyotoz.unseenworld.entity.UnseenTitaniumSpearEntity;
import net.sashakyotoz.unseenworld.entity.TheWitherKnightEntity;
import net.sashakyotoz.unseenworld.entity.TheBlazerEntity;
import net.sashakyotoz.unseenworld.entity.TealivyVoidSpearEntity;
import net.sashakyotoz.unseenworld.entity.TealivyFireStaffEntity;
import net.sashakyotoz.unseenworld.entity.TealiveSkeletonEntity;
import net.sashakyotoz.unseenworld.entity.TanzaniteGuardianEntity;
import net.sashakyotoz.unseenworld.entity.StrederEntity;
import net.sashakyotoz.unseenworld.entity.SavageSmallBlazeEntity;
import net.sashakyotoz.unseenworld.entity.RedblazeEntity;
import net.sashakyotoz.unseenworld.entity.RedSlylfEntity;
import net.sashakyotoz.unseenworld.entity.RedRavengerEntity;
import net.sashakyotoz.unseenworld.entity.NethermenEntity;
import net.sashakyotoz.unseenworld.entity.NetheriumStaffEntity;
import net.sashakyotoz.unseenworld.entity.MoonfishEntity;
import net.sashakyotoz.unseenworld.entity.GhastOfTealiveValleyEntity;
import net.sashakyotoz.unseenworld.entity.DustyPinkMaxorFishEntity;
import net.sashakyotoz.unseenworld.entity.DarkspiritwolfEntity;
import net.sashakyotoz.unseenworld.entity.DarkskeletonEntity;
import net.sashakyotoz.unseenworld.entity.DarkPhantomEntity;
import net.sashakyotoz.unseenworld.entity.DarkPearlRangedItemEntity;
import net.sashakyotoz.unseenworld.entity.DarkHoglinEntity;
import net.sashakyotoz.unseenworld.entity.DarkGolemEntity;
import net.sashakyotoz.unseenworld.entity.ChimericRedmarerEntity;
import net.sashakyotoz.unseenworld.entity.ChimericPurplemarerEntity;
import net.sashakyotoz.unseenworld.entity.CavernScarecrowEntity;
import net.sashakyotoz.unseenworld.entity.AmethystGolemEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnseenWorldModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, UnseenWorldMod.MODID);
	public static final RegistryObject<EntityType<TealivyVoidSpearEntity>> TEALIVY_VOID_SPEAR = register("projectile_tealivy_void_spear", EntityType.Builder.<TealivyVoidSpearEntity>of(TealivyVoidSpearEntity::new, MobCategory.MISC)
			.setCustomClientFactory(TealivyVoidSpearEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(4).setUpdateInterval(20).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<UnseenTitaniumSpearEntity>> UNSEEN_TITANIUM_SPEAR = register("projectile_unseen_titanium_spear", EntityType.Builder.<UnseenTitaniumSpearEntity>of(UnseenTitaniumSpearEntity::new, MobCategory.MISC)
			.setCustomClientFactory(UnseenTitaniumSpearEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(4).setUpdateInterval(20).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<NetheriumStaffEntity>> NETHERIUM_STAFF = register("projectile_netherium_staff",
			EntityType.Builder.<NetheriumStaffEntity>of(NetheriumStaffEntity::new, MobCategory.MISC).setCustomClientFactory(NetheriumStaffEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<VoidStaffEntity>> VOID_STAFF = register("projectile_void_staff",
			EntityType.Builder.<VoidStaffEntity>of(VoidStaffEntity::new, MobCategory.MISC).setCustomClientFactory(VoidStaffEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<TealivyFireStaffEntity>> TEALIVY_FIRE_STAFF = register("projectile_tealivy_fire_staff", EntityType.Builder.<TealivyFireStaffEntity>of(TealivyFireStaffEntity::new, MobCategory.MISC)
			.setCustomClientFactory(TealivyFireStaffEntity::new).setShouldReceiveVelocityUpdates(true).clientTrackingRange(16).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<VoidBowEntity>> VOID_BOW = register("projectile_void_bow",
			EntityType.Builder.<VoidBowEntity>of(VoidBowEntity::new, MobCategory.MISC).setCustomClientFactory(VoidBowEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<DarkskeletonEntity>> DARKSKELETON = register("darkskeleton",
			EntityType.Builder.<DarkskeletonEntity>of(DarkskeletonEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).clientTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(DarkskeletonEntity::new)

					.sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<AmethystGolemEntity>> AMETHYST_GOLEM = register("amethyst_golem", EntityType.Builder.<AmethystGolemEntity>of(AmethystGolemEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true)
			.clientTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(AmethystGolemEntity::new).fireImmune().sized(1.2f, 1.6f));
	public static final RegistryObject<EntityType<DarkPhantomEntity>> DARK_PHANTOM = register("dark_phantom", EntityType.Builder.<DarkPhantomEntity>of(DarkPhantomEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true)
			.clientTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(DarkPhantomEntity::new).fireImmune().sized(1f, 1f));
	public static final RegistryObject<EntityType<DustyPinkMaxorFishEntity>> DUSTY_PINK_MAXOR_FISH = register("dusty_pink_maxor_fish",
			EntityType.Builder.<DustyPinkMaxorFishEntity>of(DustyPinkMaxorFishEntity::new, MobCategory.WATER_AMBIENT).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(5)
					.setCustomClientFactory(DustyPinkMaxorFishEntity::new)

					.sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<MoonfishEntity>> MOONFISH = register("moonfish",
			EntityType.Builder.<MoonfishEntity>of(MoonfishEntity::new, MobCategory.WATER_AMBIENT).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(5).setCustomClientFactory(MoonfishEntity::new)

					.sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<CavernScarecrowEntity>> CAVERN_SCARECROW = register("cavern_scarecrow", EntityType.Builder.<CavernScarecrowEntity>of(CavernScarecrowEntity::new, MobCategory.CREATURE)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(3).setCustomClientFactory(CavernScarecrowEntity::new).fireImmune().sized(0.6f, 0.8f));
	public static final RegistryObject<EntityType<SavageSmallBlazeEntity>> SAVAGE_SMALL_BLAZE = register("savage_small_blaze", EntityType.Builder.<SavageSmallBlazeEntity>of(SavageSmallBlazeEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).clientTrackingRange(8).setUpdateInterval(3).setCustomClientFactory(SavageSmallBlazeEntity::new).fireImmune().sized(0.6f, 1.6f));
	public static final RegistryObject<EntityType<ChimericPurplemarerEntity>> CHIMERIC_PURPLEMARER = register("chimeric_purplemarer", EntityType.Builder.<ChimericPurplemarerEntity>of(ChimericPurplemarerEntity::new, MobCategory.CREATURE)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(3).setCustomClientFactory(ChimericPurplemarerEntity::new).fireImmune().sized(0.8f, 1.6f));
	public static final RegistryObject<EntityType<ChimericRedmarerEntity>> CHIMERIC_REDMARER = register("chimeric_redmarer", EntityType.Builder.<ChimericRedmarerEntity>of(ChimericRedmarerEntity::new, MobCategory.CREATURE)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(3).setCustomClientFactory(ChimericRedmarerEntity::new).fireImmune().sized(0.8f, 1.6f));
	public static final RegistryObject<EntityType<NethermenEntity>> NETHERMEN = register("nethermen",
			EntityType.Builder.<NethermenEntity>of(NethermenEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(NethermenEntity::new).fireImmune().sized(0.6f, 3f));
	public static final RegistryObject<EntityType<RedSlylfEntity>> RED_SLYLF = register("red_slylf",
			EntityType.Builder.<RedSlylfEntity>of(RedSlylfEntity::new, MobCategory.AMBIENT).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(RedSlylfEntity::new).fireImmune().sized(0.7f, 0.9f));
	public static final RegistryObject<EntityType<RedblazeEntity>> RED_BLAZE = register("red_blaze",
			EntityType.Builder.<RedblazeEntity>of(RedblazeEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).clientTrackingRange(8).setUpdateInterval(3).setCustomClientFactory(RedblazeEntity::new).fireImmune().sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<StrederEntity>> STREDER = register("streder",
			EntityType.Builder.<StrederEntity>of(StrederEntity::new, MobCategory.AMBIENT).setShouldReceiveVelocityUpdates(true).clientTrackingRange(8).setUpdateInterval(3).setCustomClientFactory(StrederEntity::new).fireImmune().sized(0.4f, 1f));
	public static final RegistryObject<EntityType<GhastOfTealiveValleyEntity>> GHAST_OF_TEALIVE_VALLEY = register("ghast_of_tealive_valley", EntityType.Builder.<GhastOfTealiveValleyEntity>of(GhastOfTealiveValleyEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).clientTrackingRange(8).setUpdateInterval(3).setCustomClientFactory(GhastOfTealiveValleyEntity::new).fireImmune().sized(2f, 2f));
	public static final RegistryObject<EntityType<TanzaniteGuardianEntity>> TANZANITE_GUARDIAN = register("tanzanite_guardian", EntityType.Builder.<TanzaniteGuardianEntity>of(TanzaniteGuardianEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).clientTrackingRange(8).setUpdateInterval(3).setCustomClientFactory(TanzaniteGuardianEntity::new).fireImmune().sized(1.6f, 2.4f));
	public static final RegistryObject<EntityType<DarkspiritwolfEntity>> DARKSPIRITWOLF = register("darkspiritwolf", EntityType.Builder.<DarkspiritwolfEntity>of(DarkspiritwolfEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true)
			.clientTrackingRange(8).setUpdateInterval(3).setCustomClientFactory(DarkspiritwolfEntity::new).fireImmune().sized(0.6f, 0.5f));
	public static final RegistryObject<EntityType<VoidEndermenEntity>> VOID_ENDERMEN = register("void_endermen", EntityType.Builder.<VoidEndermenEntity>of(VoidEndermenEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true)
			.clientTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(VoidEndermenEntity::new).fireImmune().sized(0.6f, 2.5f));
	public static final RegistryObject<EntityType<TealiveSkeletonEntity>> TEALIVE_SKELETON = register("tealive_skeleton", EntityType.Builder.<TealiveSkeletonEntity>of(TealiveSkeletonEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).clientTrackingRange(8).setUpdateInterval(3).setCustomClientFactory(TealiveSkeletonEntity::new).fireImmune().sized(0.7f, 1.8f));
	public static final RegistryObject<EntityType<RedRavengerEntity>> RED_RAVENGER = register("red_ravenger", EntityType.Builder.<RedRavengerEntity>of(RedRavengerEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true)
			.clientTrackingRange(8).setUpdateInterval(3).setCustomClientFactory(RedRavengerEntity::new).fireImmune().sized(1.5f, 1.8f));
	public static final RegistryObject<EntityType<DarkHoglinEntity>> DARK_HOGLIN = register("dark_hoglin", EntityType.Builder.<DarkHoglinEntity>of(DarkHoglinEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).clientTrackingRange(8)
			.setUpdateInterval(3).setCustomClientFactory(DarkHoglinEntity::new).fireImmune().sized(1.2f, 1.5f));
	public static final RegistryObject<EntityType<DarkGolemEntity>> DARK_GOLEM = register("dark_golem",
			EntityType.Builder.<DarkGolemEntity>of(DarkGolemEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(DarkGolemEntity::new).fireImmune().sized(1f, 1.8f));
	public static final RegistryObject<EntityType<TheBlazerEntity>> THE_BLAZER = register("the_blazer", EntityType.Builder.<TheBlazerEntity>of(TheBlazerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(16)
			.setUpdateInterval(3).setCustomClientFactory(TheBlazerEntity::new).fireImmune().sized(0.7f, 2.3f));
	public static final RegistryObject<EntityType<TheWitherKnightEntity>> THE_WITHER_KNIGHT = register("the_wither_knight", EntityType.Builder.<TheWitherKnightEntity>of(TheWitherKnightEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(TheWitherKnightEntity::new).fireImmune().sized(0.6f, 2f));
	public static final RegistryObject<EntityType<DarkPearlRangedItemEntity>> DARK_PEARL_RANGED_ITEM = register("projectile_dark_pearl_ranged_item", EntityType.Builder.<DarkPearlRangedItemEntity>of(DarkPearlRangedItemEntity::new, MobCategory.MISC)
			.setCustomClientFactory(DarkPearlRangedItemEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(20).sized(0.5f, 0.5f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			DarkskeletonEntity.init();
			AmethystGolemEntity.init();
			DarkPhantomEntity.init();
			DustyPinkMaxorFishEntity.init();
			MoonfishEntity.init();
			CavernScarecrowEntity.init();
			SavageSmallBlazeEntity.init();
			ChimericPurplemarerEntity.init();
			ChimericRedmarerEntity.init();
			NethermenEntity.init();
			RedSlylfEntity.init();
			RedblazeEntity.init();
			StrederEntity.init();
			GhastOfTealiveValleyEntity.init();
			TanzaniteGuardianEntity.init();
			DarkspiritwolfEntity.init();
			VoidEndermenEntity.init();
			TealiveSkeletonEntity.init();
			RedRavengerEntity.init();
			DarkHoglinEntity.init();
			DarkGolemEntity.init();
			TheBlazerEntity.init();
			TheWitherKnightEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(DARKSKELETON.get(), DarkskeletonEntity.createAttributes().build());
		event.put(AMETHYST_GOLEM.get(), AmethystGolemEntity.createAttributes().build());
		event.put(DARK_PHANTOM.get(), DarkPhantomEntity.createAttributes().build());
		event.put(DUSTY_PINK_MAXOR_FISH.get(), DustyPinkMaxorFishEntity.createAttributes().build());
		event.put(MOONFISH.get(), MoonfishEntity.createAttributes().build());
		event.put(CAVERN_SCARECROW.get(), CavernScarecrowEntity.createAttributes().build());
		event.put(SAVAGE_SMALL_BLAZE.get(), SavageSmallBlazeEntity.createAttributes().build());
		event.put(CHIMERIC_PURPLEMARER.get(), ChimericPurplemarerEntity.createAttributes().build());
		event.put(CHIMERIC_REDMARER.get(), ChimericRedmarerEntity.createAttributes().build());
		event.put(NETHERMEN.get(), NethermenEntity.createAttributes().build());
		event.put(RED_SLYLF.get(), RedSlylfEntity.createAttributes().build());
		event.put(RED_BLAZE.get(), RedblazeEntity.createAttributes().build());
		event.put(STREDER.get(), StrederEntity.createAttributes().build());
		event.put(GHAST_OF_TEALIVE_VALLEY.get(), GhastOfTealiveValleyEntity.createAttributes().build());
		event.put(TANZANITE_GUARDIAN.get(), TanzaniteGuardianEntity.createAttributes().build());
		event.put(DARKSPIRITWOLF.get(), DarkspiritwolfEntity.createAttributes().build());
		event.put(VOID_ENDERMEN.get(), VoidEndermenEntity.createAttributes().build());
		event.put(TEALIVE_SKELETON.get(), TealiveSkeletonEntity.createAttributes().build());
		event.put(RED_RAVENGER.get(), RedRavengerEntity.createAttributes().build());
		event.put(DARK_HOGLIN.get(), DarkHoglinEntity.createAttributes().build());
		event.put(DARK_GOLEM.get(), DarkGolemEntity.createAttributes().build());
		event.put(THE_BLAZER.get(), TheBlazerEntity.createAttributes().build());
		event.put(THE_WITHER_KNIGHT.get(), TheWitherKnightEntity.createAttributes().build());
	}
}
