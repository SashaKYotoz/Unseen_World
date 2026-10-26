package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.datagen.advancements.CuredGripcrystalEntityCriterion;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.items.custom.GrippingGauntletItem;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.common.world.biomes.ModBiomes;

import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    public static final String BASE = "advancement.unseen_world.";
    public static final String DESC = ".desc";
    private static final ResourceKey<Structure> WARRIOR_FORTRESS = structure("warrior_fortress");
    private static final ResourceKey<Structure> SENTINEL_STRONGHOLD = structure("sentinel_stronghold");
    private static final ResourceKey<Structure> PATRIARCH_CITADEL = structure("patriarch_citadel");
    private static final int SURFACE_MIN_Y = 0;
    private static final int HIGHLANDS_MAX_Y = 320;

    public ModAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    public static class BiomeCriterion extends AbstractCriterionTriggerInstance {
        public ResourceKey<Biome> biome;

        private BiomeCriterion(ResourceKey<Biome> biome, int min, int max) {
            super(ResourceLocation.tryBuild("minecraft", "location"),
                    ContextAwarePredicate.create(LocationCheck.checkLocation(
                            LocationPredicate.Builder.location().setBiome(biome).setY(MinMaxBounds.Doubles.between(min, max))).build()));
        }

        public static BiomeCriterion of(ResourceKey<Biome> biome) {
            return BiomeCriterion.of(biome, 0, 256);
        }

        public static BiomeCriterion of(ResourceKey<Biome> biome, int min, int max) {
            return new BiomeCriterion(biome, min, max);
        }
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement INTO_THE_CHIMERIC_DARKNESS = Advancement.Builder.advancement()
                .display(ModItems.ECLIPSE_KEYSTONE,
                        Component.translatable(BASE + "into_the_chimeric_darkness"),
                        Component.translatable(BASE + "into_the_chimeric_darkness" + DESC),
                        UnseenWorld.MOD.makeID("textures/environment/advancements_frame.png"),
                        FrameType.TASK,
                        true, true, false)
                .addCriterion("entered_the_chimeric_darkness", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:into_the_chimeric_darkness");
        Advancement INTO_THE_HEART_OF_DARKNESS = Advancement.Builder.advancement()
                .display(ModBlocks.GRIPPING_SPIGELIA,
                        Component.translatable(BASE + "into_the_heart_of_darkness"),
                        Component.translatable(BASE + "into_the_heart_of_darkness" + DESC),
                        null,
                        FrameType.TASK,
                        true, true, false)
                .parent(INTO_THE_CHIMERIC_DARKNESS)
                .addCriterion("entered_the_darkness", BiomeCriterion.of(ModBiomes.THE_DARKNESS))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:into_the_heart_of_darkness");
        Advancement EXPLORE_CHIMERIC_DARKNESS = requireListedBiomesVisited(
                Advancement.Builder.advancement()
        )
                .display(ModItems.RED_TITANIUM_BOOTS,
                        Component.translatable(BASE + "explore_chimeric_darkness"),
                        Component.translatable(BASE + "explore_chimeric_darkness" + DESC),
                        null,
                        FrameType.CHALLENGE,
                        true, true, false)
                .parent(INTO_THE_CHIMERIC_DARKNESS)
                .rewards(AdvancementRewards.Builder.experience(500)).save(consumer, "unseen_world:explore_chimeric_darkness");
        Advancement CURE_GRIPCRYSTAL_ENTITY = Advancement.Builder.advancement()
                .display(ModItems.GRIPTONITE,
                        Component.translatable(BASE + "cure_gripcrystal_entity"),
                        Component.translatable(BASE + "cure_gripcrystal_entity" + DESC),
                        null,
                        FrameType.GOAL,
                        true, true, false)
                .parent(INTO_THE_CHIMERIC_DARKNESS)
                .addCriterion("cure_gripcrystal_entity", CuredGripcrystalEntityCriterion.Conditions.any())
                .rewards(AdvancementRewards.Builder.experience(250)).save(consumer, "unseen_world:cure_gripcrystal_entity");
        Advancement.Builder.advancement()
                .display(ModItems.GRIPPING_GAUNTLET,
                        Component.translatable(BASE + "steal"),
                        Component.translatable(BASE + "steal" + DESC),
                        null,
                        FrameType.TASK,
                        true, true, true)
                .parent(CURE_GRIPCRYSTAL_ENTITY)
                .addCriterion(GrippingGauntletItem.STEAL_CRITERION, new ImpossibleTrigger.TriggerInstance())
                .rewards(AdvancementRewards.Builder.experience(50)).save(consumer, GrippingGauntletItem.STEAL_ADVANCEMENT.toString());
        Advancement FORTRESS_IN_THE_DARKNESS = Advancement.Builder.advancement()
                .display(ModItems.GRIPCRYSTAL_KEY,
                        Component.translatable(BASE + "fortress_in_the_darkness"),
                        Component.translatable(BASE + "fortress_in_the_darkness" + DESC),
                        null,
                        FrameType.GOAL,
                        true, true, false)
                .parent(INTO_THE_HEART_OF_DARKNESS)
                .addCriterion("fortress_in_the_darkness", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(WARRIOR_FORTRESS)))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:fortress_in_the_darkness");
        Advancement INTO_THE_PENUMBRAL_STEPPE = Advancement.Builder.advancement()
                .display(ModBlocks.CORONA_BLOOM,
                        Component.translatable(BASE + "into_the_penumbral_steppe"),
                        Component.translatable(BASE + "into_the_penumbral_steppe" + DESC),
                        null,
                        FrameType.TASK,
                        true, true, false)
                .parent(INTO_THE_CHIMERIC_DARKNESS)
                .addCriterion("entered_the_penumbral_steppe", BiomeCriterion.of(ModBiomes.PENUMBRAL_STEPPE, SURFACE_MIN_Y, HIGHLANDS_MAX_Y))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:into_the_penumbral_steppe");
        Advancement RING_OF_THE_CORONA = Advancement.Builder.advancement()
                .display(ModItems.ABYSSAL_KEY,
                        Component.translatable(BASE + "ring_of_the_corona"),
                        Component.translatable(BASE + "ring_of_the_corona" + DESC),
                        null,
                        FrameType.GOAL,
                        true, true, false)
                .parent(INTO_THE_PENUMBRAL_STEPPE)
                .addCriterion("found_sentinel_stronghold", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(SENTINEL_STRONGHOLD)))
                .rewards(AdvancementRewards.Builder.experience(100)).save(consumer, "unseen_world:ring_of_the_corona");
        Advancement INTO_THE_AURIC_HEIGHTS = Advancement.Builder.advancement()
                .display(ModBlocks.GILDED_CURRANTSLATE,
                        Component.translatable(BASE + "into_the_auric_heights"),
                        Component.translatable(BASE + "into_the_auric_heights" + DESC),
                        null,
                        FrameType.TASK,
                        true, true, false)
                .parent(INTO_THE_CHIMERIC_DARKNESS)
                .addCriterion("entered_the_auric_heights", BiomeCriterion.of(ModBiomes.AURIC_HEIGHTS, SURFACE_MIN_Y, HIGHLANDS_MAX_Y))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:into_the_auric_heights");
        Advancement CITADEL_ABOVE_THE_PEAKS = Advancement.Builder.advancement()
                .display(ModItems.AURIC_KEY,
                        Component.translatable(BASE + "citadel_above_the_peaks"),
                        Component.translatable(BASE + "citadel_above_the_peaks" + DESC),
                        null,
                        FrameType.GOAL,
                        true, true, false)
                .parent(INTO_THE_AURIC_HEIGHTS)
                .addCriterion("found_patriarch_citadel", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(PATRIARCH_CITADEL)))
                .rewards(AdvancementRewards.Builder.experience(100)).save(consumer, "unseen_world:citadel_above_the_peaks");
        Advancement WHISPERS_OF_THE_LIGHT = Advancement.Builder.advancement()
                .display(ModItems.CHIMERIC_ROCKBREAKER_HAMMER,
                        Component.translatable(BASE + "whispers_of_the_light"),
                        Component.translatable(BASE + "whispers_of_the_light" + DESC),
                        null,
                        FrameType.CHALLENGE,
                        true, true, false)
                .parent(FORTRESS_IN_THE_DARKNESS)
                .addCriterion("whispers_of_the_light", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS)))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:whispers_of_the_light");
        Advancement QUENCHED_SUN = Advancement.Builder.advancement()
                .display(ModItems.ECLIPSEBANE,
                        Component.translatable(BASE + "quenched_sun"),
                        Component.translatable(BASE + "quenched_sun" + DESC),
                        null,
                        FrameType.CHALLENGE,
                        true, true, false)
                .parent(RING_OF_THE_CORONA)
                .addCriterion("quenched_sun", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.ECLIPSE_SENTINEL)))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:quenched_sun");
        Advancement FALLEN_CROWN = Advancement.Builder.advancement()
                .display(ModItems.AURIC_ARC,
                        Component.translatable(BASE + "fallen_crown"),
                        Component.translatable(BASE + "fallen_crown" + DESC),
                        null,
                        FrameType.CHALLENGE,
                        true, true, false)
                .parent(CITADEL_ABOVE_THE_PEAKS)
                .addCriterion("fallen_crown", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.AURIC_PATRIARCH)))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:fallen_crown");
    }

    private static ResourceKey<Structure> structure(String path) {
        return ResourceKey.create(Registries.STRUCTURE, UnseenWorld.MOD.makeID(path));
    }

    private static Advancement.Builder requireListedBiomesVisited(Advancement.Builder builder) {
        for (ResourceKey<Biome> registryKey : ModBiomes.ALL) {
            builder.addCriterion(registryKey.location().toString(), PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(registryKey)));
        }

        return builder;
    }
}