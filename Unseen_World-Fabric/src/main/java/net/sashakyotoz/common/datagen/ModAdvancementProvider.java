package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.datagen.advancements.CuredGripcrystalEntityCriterion;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.common.world.biomes.ModBiomes;

import java.util.List;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    public static final String BASE = "advancement.unseen_world.";
    public static final String DESC = ".desc";

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
                        UnseenWorld.makeID("textures/environment/advancements_frame.png"),
                        FrameType.TASK,
                        true, true, false)
                .addCriterion("entered_the_chimeric_darkness", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:into_the_chimeric_darkness");
        Advancement INTO_THE_HEART_OF_DARKNESS = Advancement.Builder.advancement()
                .display(ModBlocks.KEY_HANDLER_STONE,
                        Component.translatable(BASE + "into_the_heart_of_darkness"),
                        Component.translatable(BASE + "into_the_heart_of_darkness" + DESC),
                        null,
                        FrameType.GOAL,
                        true, true, false)
                .parent(INTO_THE_CHIMERIC_DARKNESS)
                .addCriterion("entered_the_darkness", BiomeCriterion.of(ModBiomes.THE_DARKNESS))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:into_the_heart_of_darkness");
        Advancement EXPLORE_CHIMERIC_DARKNESS = requireListedBiomesVisited(
                Advancement.Builder.advancement(), List.of(
                        ModBiomes.THE_DARKNESS,
                        ModBiomes.AMETHYST_FOREST,
                        ModBiomes.CRIMSONVEIL_WOODS,
                        ModBiomes.GRIZZLY_THICKET,
                        ModBiomes.BURLYWOOD_JUNGLE,
                        ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS,
                        ModBiomes.AMETHYST_CHIMERIES,
                        ModBiomes.GREENISH_VALLEY,
                        ModBiomes.GREENISH_MEADOW,
                        ModBiomes.TEALIVY_VALLEY,
                        ModBiomes.DARK_WASTELAND,
                        ModBiomes.CRIMSONVEIL_PLATEAU,
                        ModBiomes.HARMONY_GROVE,
                        ModBiomes.GRIZZLY_HIGHLANDS,
                        ModBiomes.TEALIVY_HIGHLANDS,
                        ModBiomes.CURRANTSLATE_PEAKS,
                        ModBiomes.SHINY_CAVERNS,
                        ModBiomes.TANZANITE_CAVES,
                        ModBiomes.DEEP_GLACIEMITE_CAVES,
                        ModBiomes.DARK_RIVER,
                        ModBiomes.LUKEWARM_DARK_RIVER,
                        ModBiomes.DARK_OCEAN,
                        ModBiomes.DARK_LIFELESS_OCEAN,
                        ModBiomes.GREYNISH_SHORE,
                        ModBiomes.DARK_BADLANDS
                )
        )
                .display(ModItems.RED_TITANIUM_BOOTS,
                        Component.translatable(BASE + "explore_chimeric_darkness"),
                        Component.translatable(BASE + "explore_chimeric_darkness" + DESC),
                        null,
                        FrameType.CHALLENGE,
                        true, true, false)
                .parent(INTO_THE_HEART_OF_DARKNESS)
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
        Advancement FORTRESS_IN_THE_DARKNESS = Advancement.Builder.advancement()
                .display(ModItems.GRIPCRYSTAL_KEY,
                        Component.translatable(BASE + "fortress_in_the_darkness"),
                        Component.translatable(BASE + "fortress_in_the_darkness" + DESC),
                        null,
                        FrameType.TASK,
                        true, true, false)
                .parent(INTO_THE_CHIMERIC_DARKNESS)
                .addCriterion("fortress_in_the_darkness", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(ModRegistry.WARRIOR_OF_DARKNESS_TOWER)))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:fortress_in_the_darkness");
        Advancement VAULT_OF_ECLIPSE = Advancement.Builder.advancement()
                .display(ModItems.ABYSSAL_KEY,
                        Component.translatable(BASE + "vault_of_eclipse"),
                        Component.translatable(BASE + "vault_of_eclipse" + DESC),
                        null,
                        FrameType.TASK,
                        true, true, false)
                .parent(INTO_THE_CHIMERIC_DARKNESS)
                .addCriterion("vault_of_eclipse", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(ModRegistry.ECLIPSE_CORE)))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:vault_of_eclipse");
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
                .parent(VAULT_OF_ECLIPSE)
                .addCriterion("quenched_sun", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.ECLIPSE_SENTINEL)))
                .rewards(AdvancementRewards.EMPTY).save(consumer, "unseen_world:quenched_sun");
    }

    private static Advancement.Builder requireListedBiomesVisited(Advancement.Builder builder, List<ResourceKey<Biome>> biomes) {
        for (ResourceKey<Biome> registryKey : biomes) {
            builder.addCriterion(registryKey.location().toString(), PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(registryKey)));
        }

        return builder;
    }
}