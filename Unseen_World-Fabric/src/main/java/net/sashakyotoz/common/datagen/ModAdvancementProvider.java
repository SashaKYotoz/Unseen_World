package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.datagen.advancements.CuredGripcrystalEntityCriterion;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.items.custom.GrippingGauntletItem;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.common.world.biomes.ModBiomes;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    public static final String BASE = "advancement.unseen_world.";
    public static final String DESC = ".desc";
    private static final int DEFAULT_MIN_Y = 0;
    private static final int DEFAULT_MAX_Y = 256;
    private static final int SURFACE_MIN_Y = 0;
    private static final int HIGHLANDS_MAX_Y = 320;

    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer) {
        HolderGetter<Biome> biomes = registries.lookupOrThrow(Registries.BIOME);

        AdvancementHolder intoTheChimericDarkness = Advancement.Builder.advancement()
                .display(ModItems.ECLIPSE_KEYSTONE,
                        Component.translatable(BASE + "into_the_chimeric_darkness"),
                        Component.translatable(BASE + "into_the_chimeric_darkness" + DESC),
                        UnseenWorld.MOD.makeID("textures/environment/advancements_frame.png"),
                        AdvancementType.TASK,
                        true, true, false)
                .addCriterion("entered_the_chimeric_darkness", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:into_the_chimeric_darkness");
        AdvancementHolder intoTheHeartOfDarkness = Advancement.Builder.advancement()
                .display(ModBlocks.GRIPPING_SPIGELIA,
                        Component.translatable(BASE + "into_the_heart_of_darkness"),
                        Component.translatable(BASE + "into_the_heart_of_darkness" + DESC),
                        null,
                        AdvancementType.TASK,
                        true, true, false)
                .parent(intoTheChimericDarkness)
                .addCriterion("entered_the_darkness", inBiome(biomes, ModBiomes.THE_DARKNESS, DEFAULT_MIN_Y, DEFAULT_MAX_Y))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:into_the_heart_of_darkness");
        requireListedBiomesVisited(Advancement.Builder.advancement(), biomes)
                .display(ModItems.RED_TITANIUM_BOOTS,
                        Component.translatable(BASE + "explore_chimeric_darkness"),
                        Component.translatable(BASE + "explore_chimeric_darkness" + DESC),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false)
                .parent(intoTheChimericDarkness)
                .rewards(AdvancementRewards.Builder.experience(500)).save(consumer, "unseen_world:explore_chimeric_darkness");
        AdvancementHolder cureGripcrystalEntity = Advancement.Builder.advancement()
                .display(ModItems.GRIPTONITE,
                        Component.translatable(BASE + "cure_gripcrystal_entity"),
                        Component.translatable(BASE + "cure_gripcrystal_entity" + DESC),
                        null,
                        AdvancementType.GOAL,
                        true, true, false)
                .parent(intoTheChimericDarkness)
                .addCriterion("cure_gripcrystal_entity", CuredGripcrystalEntityCriterion.Conditions.any())
                .rewards(AdvancementRewards.Builder.experience(250)).save(consumer, "unseen_world:cure_gripcrystal_entity");
        Advancement.Builder.advancement()
                .display(ModItems.GRIPPING_GAUNTLET,
                        Component.translatable(BASE + "steal"),
                        Component.translatable(BASE + "steal" + DESC),
                        null,
                        AdvancementType.TASK,
                        true, true, true)
                .parent(cureGripcrystalEntity)
                .addCriterion(GrippingGauntletItem.STEAL_CRITERION, CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .rewards(AdvancementRewards.Builder.experience(50)).save(consumer, GrippingGauntletItem.STEAL_ADVANCEMENT.toString());
        AdvancementHolder fortressInTheDarkness = handwritten("fortress_in_the_darkness");
        AdvancementHolder intoThePenumbralSteppe = Advancement.Builder.advancement()
                .display(ModBlocks.CORONA_BLOOM,
                        Component.translatable(BASE + "into_the_penumbral_steppe"),
                        Component.translatable(BASE + "into_the_penumbral_steppe" + DESC),
                        null,
                        AdvancementType.TASK,
                        true, true, false)
                .parent(intoTheChimericDarkness)
                .addCriterion("entered_the_penumbral_steppe", inBiome(biomes, ModBiomes.PENUMBRAL_STEPPE, SURFACE_MIN_Y, HIGHLANDS_MAX_Y))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:into_the_penumbral_steppe");
        AdvancementHolder ringOfTheCorona = handwritten("ring_of_the_corona");
        AdvancementHolder intoTheAuricHeights = Advancement.Builder.advancement()
                .display(ModBlocks.GILDED_CURRANTSLATE,
                        Component.translatable(BASE + "into_the_auric_heights"),
                        Component.translatable(BASE + "into_the_auric_heights" + DESC),
                        null,
                        AdvancementType.TASK,
                        true, true, false)
                .parent(intoTheChimericDarkness)
                .addCriterion("entered_the_auric_heights", inBiome(biomes, ModBiomes.AURIC_HEIGHTS, SURFACE_MIN_Y, HIGHLANDS_MAX_Y))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:into_the_auric_heights");
        AdvancementHolder citadelAboveThePeaks = handwritten("citadel_above_the_peaks");
        Advancement.Builder.advancement()
                .display(ModItems.CHIMERIC_ROCKBREAKER_HAMMER,
                        Component.translatable(BASE + "whispers_of_the_light"),
                        Component.translatable(BASE + "whispers_of_the_light" + DESC),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false)
                .parent(fortressInTheDarkness)
                .addCriterion("whispers_of_the_light", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS)))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:whispers_of_the_light");
        Advancement.Builder.advancement()
                .display(ModItems.ECLIPSEBANE,
                        Component.translatable(BASE + "quenched_sun"),
                        Component.translatable(BASE + "quenched_sun" + DESC),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false)
                .parent(ringOfTheCorona)
                .addCriterion("quenched_sun", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.ECLIPSE_SENTINEL)))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:quenched_sun");
        Advancement.Builder.advancement()
                .display(ModItems.AURIC_ARC,
                        Component.translatable(BASE + "fallen_crown"),
                        Component.translatable(BASE + "fallen_crown" + DESC),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false)
                .parent(citadelAboveThePeaks)
                .addCriterion("fallen_crown", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.AURIC_PATRIARCH)))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:fallen_crown");
    }

    private static Criterion<PlayerTrigger.TriggerInstance> inBiome(HolderGetter<Biome> biomes, ResourceKey<Biome> biome, int minY, int maxY) {
        return PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.location()
                .setBiomes(HolderSet.direct(biomes.getOrThrow(biome)))
                .setY(MinMaxBounds.Doubles.between(minY, maxY)));
    }

    private static AdvancementHolder handwritten(String path) {
        return new AdvancementHolder(UnseenWorld.MOD.makeID(path), null);
    }

    private static Advancement.Builder requireListedBiomesVisited(Advancement.Builder builder, HolderGetter<Biome> biomes) {
        for (ResourceKey<Biome> biome : ModBiomes.ALL) {
            builder.addCriterion(biome.location().toString(), PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inBiome(biomes.getOrThrow(biome))));
        }
        return builder;
    }
}