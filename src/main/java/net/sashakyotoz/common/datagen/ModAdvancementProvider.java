package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.ChangedDimensionCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.data.server.advancement.vanilla.VanillaAdventureTabAdvancementGenerator;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
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

    public static class BiomeCriterion extends AbstractCriterionConditions {
        public RegistryKey<Biome> biome;

        private BiomeCriterion(RegistryKey<Biome> biome, int min, int max) {
            super(Identifier.of("minecraft", "location"),
                    LootContextPredicate.create(LocationCheckLootCondition.builder(
                            LocationPredicate.Builder.create().biome(biome).y(NumberRange.FloatRange.between(min, max))).build()));
        }

        public static BiomeCriterion of(RegistryKey<Biome> biome) {
            return BiomeCriterion.of(biome, 0, 256);
        }

        public static BiomeCriterion of(RegistryKey<Biome> biome, int min, int max) {
            return new BiomeCriterion(biome, min, max);
        }
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement INTO_THE_CHIMERIC_DARKNESS = Advancement.Builder.create()
                .display(ModItems.ECLIPSE_KEYSTONE,
                        Text.translatable(BASE + "into_the_chimeric_darkness"),
                        Text.translatable(BASE + "into_the_chimeric_darkness" + DESC),
                        UnseenWorld.makeID("textures/environment/advancements_frame.png"),
                        AdvancementFrame.TASK,
                        true, true, false)
                .criterion("entered_the_chimeric_darkness", ChangedDimensionCriterion.Conditions.to(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY))
                .rewards(AdvancementRewards.NONE).build(consumer, "unseen_world:into_the_chimeric_darkness");
        Advancement INTO_THE_HEART_OF_DARKNESS = Advancement.Builder.create()
                .display(ModBlocks.KEY_HANDLER_STONE,
                        Text.translatable(BASE + "into_the_heart_of_darkness"),
                        Text.translatable(BASE + "into_the_heart_of_darkness" + DESC),
                        null,
                        AdvancementFrame.GOAL,
                        true, true, false)
                .parent(INTO_THE_CHIMERIC_DARKNESS)
                .criterion("entered_the_darkness", BiomeCriterion.of(ModBiomes.THE_DARKNESS))
                .rewards(AdvancementRewards.NONE).build(consumer, "unseen_world:into_the_heart_of_darkness");
        Advancement EXPLORE_CHIMERIC_DARKNESS = requireListedBiomesVisited(
                Advancement.Builder.create(),List.of(
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
                        Text.translatable(BASE + "explore_chimeric_darkness"),
                        Text.translatable(BASE + "explore_chimeric_darkness" + DESC),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true, true, false)
                .parent(INTO_THE_HEART_OF_DARKNESS)
                .rewards(AdvancementRewards.Builder.experience(500)).build(consumer, "unseen_world:explore_chimeric_darkness");
    }

    private static Advancement.Builder requireListedBiomesVisited(Advancement.Builder builder, List<RegistryKey<Biome>> biomes) {
        for (RegistryKey<Biome> registryKey : biomes) {
            builder.criterion(registryKey.getValue().toString(), TickCriterion.Conditions.createLocation(LocationPredicate.biome(registryKey)));
        }

        return builder;
    }
}