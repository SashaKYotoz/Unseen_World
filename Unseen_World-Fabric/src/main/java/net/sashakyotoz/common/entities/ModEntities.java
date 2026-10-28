package net.sashakyotoz.common.entities;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.lcc.sollib.api.common.registry.holder.EntityHolder;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.flag.FeatureFlags;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.bosses.AuricPatriarch;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;
import net.sashakyotoz.common.entities.custom.*;
import net.sashakyotoz.common.entities.custom.projectiles.GrippingCrystalProjectileEntity;
import net.sashakyotoz.common.entities.custom.projectiles.ParticleProjectileEntity;
import net.sashakyotoz.common.entities.custom.projectiles.ThrownAuricArc;
import net.sashakyotoz.common.tags.ModTags;

import java.util.function.Supplier;

public class ModEntities {
    public static void register() {
        UnseenWorld.log().debug("Registering Entities for modid : " + UnseenWorld.MOD_ID);
    }

    private static final TagKey<EntityType<?>> GRIPPING_IMMUNE = ModTags.Entities.GRIPPING_IMMUNE_ENTITY_TYPES;
    private static final Spec BOSS = Spec.fixed(1.0F, 1.0F).fireImmune().tracking(10, 2);
    private static final Spec PROJECTILE = Spec.fixed(0.5F, 0.5F).tracking(4, 20);

    public static final EntityType<GleamcarverEntity> GLEAMCARVER = mob("gleamcarver", GleamcarverEntity::new, MobCategory.AMBIENT,
            Spec.fixed(0.6F, 0.6F), GleamcarverEntity::createAttributes, GRIPPING_IMMUNE);
    public static final EntityType<HarmonyWatcherEntity> HARMONY_WATCHER = mob("harmony_watcher", HarmonyWatcherEntity::new, MobCategory.CREATURE,
            Spec.fixed(1.4F, 1.8F), HarmonyWatcherEntity::createAttributes, GRIPPING_IMMUNE);
    public static final EntityType<ViolegerEntity> VIOLEGER = mob("violeger", ViolegerEntity::new, MobCategory.CREATURE,
            Spec.fixed(0.8F, 1.9F), ViolegerEntity::createAttributes, GRIPPING_IMMUNE);
    public static final EntityType<VenomerEntity> VENOMER = mob("venomer", VenomerEntity::new, MobCategory.MONSTER,
            Spec.fixed(0.8F, 1.9F), VenomerEntity::createAttributes, GRIPPING_IMMUNE);
    public static final EntityType<GloomwhaleEntity> GLOOMWHALE = mob("gloomwhale", GloomwhaleEntity::new, MobCategory.WATER_CREATURE,
            Spec.fixed(3.5F, 2.5F), GloomwhaleEntity::createAttributes);
    public static final EntityType<GrippingGloomwhaleEntity> GRIPPING_GLOOMWHALE = mob("gripping_gloomwhale", GrippingGloomwhaleEntity::new, MobCategory.WATER_CREATURE,
            Spec.fixed(3.5F, 2.5F), GrippingGloomwhaleEntity::createAttributes, GRIPPING_IMMUNE);
    public static final EntityType<ShimmerEntity> SHIMMER = mob("shimmer", ShimmerEntity::new, MobCategory.WATER_CREATURE,
            Spec.fixed(2.0F, 2.5F).eyeHeight(1.5F), ShimmerEntity::createAttributes);
    public static final EntityType<SaberpardEntity> SABERPARD = mob("saberpard", SaberpardEntity::new, MobCategory.CREATURE,
            Spec.fixed(0.65F, 0.75F), SaberpardEntity::createAttributes, GRIPPING_IMMUNE);
    public static final EntityType<EspyerEntity> ESPYER = mob("espyer", EspyerEntity::new, MobCategory.MONSTER,
            Spec.fixed(0.6F, 1.6F), EspyerEntity::createAttributes, GRIPPING_IMMUNE);
    public static final EntityType<EldritchWatcherEntity> ELDRITCH_WATCHER = mob("eldritch_watcher", EldritchWatcherEntity::new, MobCategory.AMBIENT,
            Spec.fixed(1.3F, 3.8F), EldritchWatcherEntity::createAttributes, GRIPPING_IMMUNE);
    public static final EntityType<ReaverEntity> REAVER = mob("reaver", ReaverEntity::new, MobCategory.MONSTER,
            Spec.fixed(0.9F, 2.1F), ReaverEntity::createAttributes, GRIPPING_IMMUNE);
    public static final EntityType<TuskhogEntity> TUSKHOG = mob("tuskhog", TuskhogEntity::new, MobCategory.CREATURE,
            Spec.fixed(0.9F, 0.9F), TuskhogEntity::createAttributes, GRIPPING_IMMUNE);
    public static final EntityType<DreadflapEntity> DREADFLAP = mob("dreadflap", DreadflapEntity::new, MobCategory.AMBIENT,
            Spec.fixed(0.7F, 1.1F).eyeHeight(0.88F), DreadflapEntity::createAttributes);
    public static final EntityType<VexalBeetleEntity> VEXAL_BEETLE = mob("vexal_beetle", VexalBeetleEntity::new, MobCategory.CREATURE,
            Spec.fixed(1.3F, 1.0F), VexalBeetleEntity::createAttributes);
    public static final EntityType<RimehornEntity> RIMEHORN = mob("rimehorn", RimehornEntity::new, MobCategory.CREATURE,
            Spec.scalable(0.95F, 1.5F).tracking(10, 3), RimehornEntity::createAttributes);
    public static final EntityType<UmbralSerpentEntity> UMBRAL_SERPENT = mob("umbral_serpent", UmbralSerpentEntity::new, MobCategory.CREATURE,
            Spec.fixed(1.2F, 0.9F).eyeHeight(0.54F).tracking(10, 3), UmbralSerpentEntity::createAttributes, EntityTypeTags.CAN_BREATHE_UNDER_WATER);
    public static final EntityType<EclipsedAcolyteEntity> ECLIPSED_ACOLYTE = mob("eclipsed_acolyte", EclipsedAcolyteEntity::new, MobCategory.MONSTER,
            Spec.fixed(0.8F, 2.3F).tracking(8, 3), EclipsedAcolyteEntity::createAttributes, GRIPPING_IMMUNE);
    public static final EntityType<AureateVexEntity> AUREATE_VEX = mob("aureate_vex", AureateVexEntity::new, MobCategory.MONSTER,
            Spec.fixed(0.5F, 0.9F).tracking(8, 3).fireImmune(), AureateVexEntity::createAttributes);

    public static final EntityType<WarriorOfChimericDarkness> WARRIOR_OF_CHIMERIC_DARKNESS = mob("warrior_of_chimeric_darkness", WarriorOfChimericDarkness::new, MobCategory.MONSTER,
            BOSS.size(2.75F, 3.5F), WarriorOfChimericDarkness::createAttributes, GRIPPING_IMMUNE, ConventionalEntityTypeTags.BOSSES);
    public static final EntityType<EclipseSentinel> ECLIPSE_SENTINEL = mob("eclipse_sentinel", EclipseSentinel::new, MobCategory.MONSTER,
            BOSS.size(1.6F, 3.5F), EclipseSentinel::createAttributes, GRIPPING_IMMUNE, ConventionalEntityTypeTags.BOSSES);
    public static final EntityType<AuricPatriarch> AURIC_PATRIARCH = mob("auric_patriarch", AuricPatriarch::new, MobCategory.MONSTER,
            BOSS.size(1.6F, 3.75F), AuricPatriarch::createAttributes, GRIPPING_IMMUNE, ConventionalEntityTypeTags.BOSSES);

    public static final EntityType<GrippingCrystalProjectileEntity> GRIPPING_CRYSTAL_PROJECTILE = entity("gripping_crystal_projectile",
            GrippingCrystalProjectileEntity::new, MobCategory.MISC, PROJECTILE);
    public static final EntityType<ParticleProjectileEntity> PARTICLE_PROJECTILE = entity("particle_projectile",
            ParticleProjectileEntity::new, MobCategory.MISC, PROJECTILE);
    public static final EntityType<ThrownAuricArc> THROWN_AURIC_ARC = entity("thrown_auric_arc",
            ThrownAuricArc::new, MobCategory.MISC, PROJECTILE);

    private static <T extends Entity> EntityType<T> entity(String name, EntityType.EntityFactory<T> factory, MobCategory category, Spec spec) {
        return cast(UnseenWorld.MOD.register(EntityHolder.class, name, () -> spec.build(factory, category)));
    }

    @SafeVarargs
    private static <T extends LivingEntity> EntityType<T> mob(String name, EntityType.EntityFactory<T> factory, MobCategory category, Spec spec,
                                                              Supplier<AttributeSupplier.Builder> attributes, TagKey<EntityType<?>>... tags) {
        return cast(UnseenWorld.MOD.register(EntityHolder.class, name, () -> spec.build(factory, category))
                .withAttributes(attributes)
                .withTags(tags));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> cast(EntityHolder holder) {
        return (EntityType<T>) holder.get();
    }

    private record Spec(EntityDimensions dimensions, int trackRange, int updateInterval, boolean fireproof) {
        static Spec fixed(float width, float height) {
            return new Spec(EntityDimensions.fixed(width, height), 5, 3, false);
        }

        static Spec scalable(float width, float height) {
            return new Spec(EntityDimensions.scalable(width, height), 5, 3, false);
        }

        Spec size(float width, float height) {
            return new Spec(EntityDimensions.fixed(width, height), this.trackRange, this.updateInterval, this.fireproof);
        }

        Spec tracking(int trackRange, int updateInterval) {
            return new Spec(this.dimensions, trackRange, updateInterval, this.fireproof);
        }

        Spec eyeHeight(float eyeHeight) {
            return new Spec(this.dimensions.withEyeHeight(eyeHeight), this.trackRange, this.updateInterval, this.fireproof);
        }

        Spec fireImmune() {
            return new Spec(this.dimensions, this.trackRange, this.updateInterval, true);
        }

        <T extends Entity> EntityType<T> build(EntityType.EntityFactory<T> factory, MobCategory category) {
            return new EntityType<>(factory, category, true, true, this.fireproof,
                    category == MobCategory.CREATURE || category == MobCategory.MISC, ImmutableSet.of(),
                    this.dimensions, 1.0F, this.trackRange, this.updateInterval, FeatureFlags.VANILLA_SET);
        }
    }
}