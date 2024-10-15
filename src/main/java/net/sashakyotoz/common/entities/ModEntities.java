package net.sashakyotoz.common.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;

public class ModEntities {
    public static void register() {
        UnseenWorld.log("Registering Entities for modid : " + UnseenWorld.MOD_ID);
        FabricDefaultAttributeRegistry.register(GLEAMCARVER, GleamcarverEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HARMONY_WATCHER, HarmonyWatcherEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(DARK_GUARDIAN, DarkGuardianEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarkness.createAttributes());
    }
    public static final EntityType<GleamcarverEntity> GLEAMCARVER = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("gleamcarver"), FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, GleamcarverEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 0.6f)).build());
    public static final EntityType<HarmonyWatcherEntity> HARMONY_WATCHER = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("harmony_watcher"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, HarmonyWatcherEntity::new)
                    .dimensions(EntityDimensions.fixed(1.4f, 1.8f)).build());
    public static final EntityType<DarkGuardianEntity> DARK_GUARDIAN = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("dark_guardian"), FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, DarkGuardianEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 1.9f)).build());

    public static final EntityType<WarriorOfChimericDarkness> WARRIOR_OF_CHIMERIC_DARKNESS = Registry.register(Registries.ENTITY_TYPE,
            UnseenWorld.makeID("warrior_of_chimeric_darkness"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, WarriorOfChimericDarkness::new)
                    .dimensions(EntityDimensions.fixed(1.75f, 2.85f)).build());
}