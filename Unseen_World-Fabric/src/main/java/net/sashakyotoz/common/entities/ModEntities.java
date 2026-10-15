package net.sashakyotoz.common.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.bosses.AuricPatriarch;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;
import net.sashakyotoz.common.entities.custom.*;
import net.sashakyotoz.common.entities.custom.projectiles.GrippingCrystalProjectileEntity;
import net.sashakyotoz.common.entities.custom.projectiles.ParticleProjectileEntity;
import net.sashakyotoz.common.entities.custom.projectiles.ThrownAuricArc;

public class ModEntities {
    public static void register() {
        UnseenWorld.log().debug("Registering Entities for modid : " + UnseenWorld.MOD_ID);
        FabricDefaultAttributeRegistry.register(GLEAMCARVER, GleamcarverEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HARMONY_WATCHER, HarmonyWatcherEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(VIOLEGER, ViolegerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(VENOMER, VenomerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarkness.createAttributes());
        FabricDefaultAttributeRegistry.register(ECLIPSE_SENTINEL, EclipseSentinel.createAttributes());
        FabricDefaultAttributeRegistry.register(AURIC_PATRIARCH, AuricPatriarch.createAttributes());
        FabricDefaultAttributeRegistry.register(GLOOMWHALE, GloomwhaleEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GRIPPING_GLOOMWHALE, GrippingGloomwhaleEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SHIMMER, ShimmerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SABERPARD, SaberpardEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ESPYER, EspyerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ELDRITCH_WATCHER, EldritchWatcherEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TUSKHOG, TuskhogEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(DREADFLAP, DreadflapEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(REAVER, ReaverEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(VEXAL_BEETLE, VexalBeetleEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AUREATE_VEX, AureateVexEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(RIMEHORN, RimehornEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ECLIPSED_ACOLYTE, EclipsedAcolyteEntity.createAttributes());
    }

    public static final EntityType<GleamcarverEntity> GLEAMCARVER = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("gleamcarver"), FabricEntityTypeBuilder.create(MobCategory.AMBIENT, GleamcarverEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 0.6f)).build());
    public static final EntityType<HarmonyWatcherEntity> HARMONY_WATCHER = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("harmony_watcher"), FabricEntityTypeBuilder.create(MobCategory.CREATURE, HarmonyWatcherEntity::new)
                    .dimensions(EntityDimensions.fixed(1.4f, 1.8f)).build());
    public static final EntityType<ViolegerEntity> VIOLEGER = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("violeger"), FabricEntityTypeBuilder.create(MobCategory.CREATURE, ViolegerEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 1.9f)).build());
    public static final EntityType<VenomerEntity> VENOMER = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("venomer"), FabricEntityTypeBuilder.create(MobCategory.MONSTER, VenomerEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 1.9f)).build());
    public static final EntityType<GloomwhaleEntity> GLOOMWHALE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("gloomwhale"), FabricEntityTypeBuilder.create(MobCategory.WATER_CREATURE, GloomwhaleEntity::new)
                    .dimensions(EntityDimensions.fixed(3.5f, 2.5f)).build());
    public static final EntityType<GrippingGloomwhaleEntity> GRIPPING_GLOOMWHALE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("gripping_gloomwhale"), FabricEntityTypeBuilder.create(MobCategory.WATER_CREATURE, GrippingGloomwhaleEntity::new)
                    .dimensions(EntityDimensions.fixed(3.5f, 2.5f)).build());
    public static final EntityType<ShimmerEntity> SHIMMER = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("shimmer"), FabricEntityTypeBuilder.create(MobCategory.WATER_CREATURE, ShimmerEntity::new)
                    .dimensions(EntityDimensions.fixed(2f, 2.5f)).build());
    public static final EntityType<SaberpardEntity> SABERPARD = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("saberpard"), FabricEntityTypeBuilder.create(MobCategory.CREATURE, SaberpardEntity::new)
                    .dimensions(EntityDimensions.fixed(0.65F, 0.75F)).build());
    public static final EntityType<EspyerEntity> ESPYER = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("espyer"), FabricEntityTypeBuilder.create(MobCategory.MONSTER, EspyerEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6F, 1.6F)).build());
    public static final EntityType<EldritchWatcherEntity> ELDRITCH_WATCHER = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("eldritch_watcher"), FabricEntityTypeBuilder.create(MobCategory.AMBIENT, EldritchWatcherEntity::new)
                    .dimensions(EntityDimensions.fixed(1.3F, 3.8F)).build());
    public static final EntityType<ReaverEntity> REAVER = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("reaver"), FabricEntityTypeBuilder.create(MobCategory.MONSTER, ReaverEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9F, 2.1F)).build());
    public static final EntityType<TuskhogEntity> TUSKHOG = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("tuskhog"), FabricEntityTypeBuilder.create(MobCategory.CREATURE, TuskhogEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9F, 0.9F)).build());
    public static final EntityType<DreadflapEntity> DREADFLAP = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("dreadflap"), FabricEntityTypeBuilder.create(MobCategory.AMBIENT, DreadflapEntity::new)
                    .dimensions(EntityDimensions.fixed(0.7F, 1.1F)).build());
    public static final EntityType<VexalBeetleEntity> VEXAL_BEETLE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("vexal_beetle"), FabricEntityTypeBuilder.create(MobCategory.CREATURE, VexalBeetleEntity::new)
                    .dimensions(EntityDimensions.fixed(1.3F, 1F)).build());
    public static final EntityType<RimehornEntity> RIMEHORN = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("rimehorn"), FabricEntityTypeBuilder.create(MobCategory.CREATURE, RimehornEntity::new)
                    .dimensions(EntityDimensions.scalable(0.95F, 1.5F)).trackRangeChunks(10).build());
    public static final EntityType<EclipsedAcolyteEntity> ECLIPSED_ACOLYTE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("eclipsed_acolyte"), FabricEntityTypeBuilder.create(MobCategory.MONSTER, EclipsedAcolyteEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8F, 2.3F)).trackRangeChunks(8).build());
    public static final EntityType<AureateVexEntity> AUREATE_VEX = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("aureate_vex"), FabricEntityTypeBuilder.create(MobCategory.MONSTER, AureateVexEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.9F)).fireImmune().trackRangeChunks(8).build());

    public static final EntityType<WarriorOfChimericDarkness> WARRIOR_OF_CHIMERIC_DARKNESS = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("warrior_of_chimeric_darkness"), FabricEntityTypeBuilder.create(MobCategory.MONSTER, WarriorOfChimericDarkness::new)
                    .dimensions(EntityDimensions.fixed(2.75f, 3.5f)).trackRangeChunks(10).trackedUpdateRate(2).build());
    public static final EntityType<EclipseSentinel> ECLIPSE_SENTINEL = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("eclipse_sentinel"), FabricEntityTypeBuilder.create(MobCategory.MONSTER, EclipseSentinel::new)
                    .dimensions(EntityDimensions.fixed(1.6f, 3.5f)).trackRangeChunks(10).trackedUpdateRate(2).build());
    public static final EntityType<AuricPatriarch> AURIC_PATRIARCH = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("auric_patriarch"), FabricEntityTypeBuilder.create(MobCategory.MONSTER, AuricPatriarch::new)
                    .dimensions(EntityDimensions.fixed(1.6f, 3.75f)).build());

    public static final EntityType<GrippingCrystalProjectileEntity> GRIPPING_CRYSTAL_PROJECTILE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("gripping_crystal_projectile"), FabricEntityTypeBuilder.create(MobCategory.MISC, GrippingCrystalProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<ParticleProjectileEntity> PARTICLE_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("particle_projectile"), FabricEntityTypeBuilder.<ParticleProjectileEntity>create(MobCategory.MISC, ParticleProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeChunks(4).trackedUpdateRate(20).build()
    );
    public static final EntityType<ThrownAuricArc> THROWN_AURIC_ARC = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            UnseenWorld.MOD.makeID("thrown_auric_arc"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, ThrownAuricArc::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeChunks(4)
                    .trackedUpdateRate(20)
                    .build());
}