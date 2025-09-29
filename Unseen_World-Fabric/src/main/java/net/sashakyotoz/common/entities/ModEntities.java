package net.sashakyotoz.common.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.bosses.EclipseSentinelEntity;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;
import net.sashakyotoz.common.entities.custom.*;
import net.sashakyotoz.common.entities.custom.projectiles.GrippingCrystalProjectileEntity;

public class ModEntities {
    public static void register() {
        UnseenWorld.log("Registering Entities for modid : " + UnseenWorld.MOD_ID);
        FabricDefaultAttributeRegistry.register(GLEAMCARVER, GleamcarverEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HARMONY_WATCHER, HarmonyWatcherEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(VIOLEGER, ViolegerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarkness.createAttributes());
        FabricDefaultAttributeRegistry.register(ECLIPSE_SENTINEL, EclipseSentinelEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GLOOMWHALE, GloomwhaleEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SABERPARD, SaberpardEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ESPYER, EspyerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ELDRITCH_WATCHER, EldritchWatcherEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TUSKHOG, TuskhogEntity.createAttributes());
    }

    public static final EntityType<GleamcarverEntity> GLEAMCARVER = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("gleamcarver"), FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, GleamcarverEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 0.6f)).build());
    public static final EntityType<HarmonyWatcherEntity> HARMONY_WATCHER = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("harmony_watcher"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, HarmonyWatcherEntity::new)
                    .dimensions(EntityDimensions.fixed(1.4f, 1.8f)).build());
    public static final EntityType<ViolegerEntity> VIOLEGER = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("violeger"), FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, ViolegerEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 1.9f)).build());
    public static final EntityType<GloomwhaleEntity> GLOOMWHALE = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("gloomwhale"), FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, GloomwhaleEntity::new)
                    .dimensions(EntityDimensions.fixed(3f, 2.5f)).build());
    public static final EntityType<SaberpardEntity> SABERPARD = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("saberpard"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SaberpardEntity::new)
                    .dimensions(EntityDimensions.fixed(0.65F, 0.75F)).build());
    public static final EntityType<EspyerEntity> ESPYER = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("espyer"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EspyerEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6F, 1.6F)).build());
    public static final EntityType<EldritchWatcherEntity> ELDRITCH_WATCHER = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("eldritch_watcher"), FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, EldritchWatcherEntity::new)
                    .dimensions(EntityDimensions.fixed(1.3F, 3.8F)).build());
    public static final EntityType<TuskhogEntity> TUSKHOG = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("tuskhog"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TuskhogEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9F, 0.9F)).build());

    public static final EntityType<WarriorOfChimericDarkness> WARRIOR_OF_CHIMERIC_DARKNESS = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("warrior_of_chimeric_darkness"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, WarriorOfChimericDarkness::new)
                    .dimensions(EntityDimensions.fixed(1.9f, 2.95f)).build());
    public static final EntityType<EclipseSentinelEntity> ECLIPSE_SENTINEL = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("eclipse_sentinel"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EclipseSentinelEntity::new)
                    .dimensions(EntityDimensions.fixed(1.55f, 3f)).build());

    public static final EntityType<GrippingCrystalProjectileEntity> GRIPPING_CRYSTAL_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("gripping_crystal_projectile"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, GrippingCrystalProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeBlocks(4).trackedUpdateRate(20).build());
}