package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.EffectHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.biome.Biome;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.world.biomes.ModBiomes;

import java.util.Map;

public class ModLanguageProvider extends FabricLanguageProvider {
    private static final Map<ResourceKey<Biome>, String> BIOME_NAME_OVERRIDES = Map.of(
            ModBiomes.DEEP_GLACIEMITE_CAVES, "Deep Glaciemite Caverns");

    public ModLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("itemgroup.unseen_world.items", "Unseen World: Items");
        translationBuilder.add("itemgroup.unseen_world.blocks", "Unseen World: Blocks");

        translationBuilder.add("commands.unseen_world.weather.set.grippfall", "Set the weather to grippfall");

        translationBuilder.add("item.unseen_world.warpedveil_vine_fruit", "Warpedveil vine fruit");
        translationBuilder.add("item.unseen_world.glowroot_bulb", "Glowroot bulb");

        translationBuilder.add("item.unseen_world.armor_tips", "When the full set worn:");
        translationBuilder.add("item.unseen_world.abyssal_armor_tooltip", "Immunise you to Darkness, Falling into void");
        translationBuilder.add("item.unseen_world.titanium_armor_tooltip", "Immunise you to Fire");
        translationBuilder.add("item.unseen_world.unseenium_armor_tooltip", "Immunise you to Glowing");
        translationBuilder.add("item.unseen_world.unseenium_armor_tooltip1", "Slowly recover itself, if noone near");

        translationBuilder.add("item.unseen_world.griptonite_lantern_tooltip", "Removes gripping sickness of near players");

        translationBuilder.add("item.unseen_world.chimeric_rockbreaker_hammer.ability.0", "Release on hold use: Charged Strike");
        translationBuilder.add("item.unseen_world.chimeric_rockbreaker_hammer.ability.1", "Use sneaking: Fissure");
        translationBuilder.add("item.unseen_world.chimeric_rockbreaker_hammer.ability.2", "Hits break raised shields");
        translationBuilder.add("item.unseen_world.chimeric_rockbreaker_hammer.lore", "Still warm from the Warrior's last swing");

        translationBuilder.add("item.unseen_world.eclipsebane.ability.0", "Sweeps reach 3 blocks in a wide arc");
        translationBuilder.add("item.unseen_world.eclipsebane.ability.1", "Use sneaking: Dash");
        translationBuilder.add("item.unseen_world.eclipsebane.ability.2", "Release on hold use: Eclipse Pulse, fed by Granulated Griptonite");
        translationBuilder.add("item.unseen_world.eclipsebane.lore", "The Sentinel's eclipse, folded into a blade");

        translationBuilder.add("item.unseen_world.auric_arc.ability.0", "Critical hits strike a golden line");
        translationBuilder.add("item.unseen_world.auric_arc.ability.1", "Release on hold use: Sunburst");
        translationBuilder.add("item.unseen_world.auric_arc.ability.2", "A Sunburst throw leaves a mark of Weakness");
        translationBuilder.add("item.unseen_world.auric_arc.lore", "A sliver of the Patriarch's sun");

        translationBuilder.add("item.unseen_world.gripping_abyssal_bow.ability.0", "Full draw: arrows inflict Gripping");
        translationBuilder.add("item.unseen_world.gripping_abyssal_bow.ability.1", "Release on hold use: Abyssal Shot pierces and pulls fliers down");
        translationBuilder.add("item.unseen_world.gripping_abyssal_bow.lore", "Strung with a thread of the Abyss");

        translationBuilder.add("item.unseen_world.shield_of_chimeric_warrior.ability.0", "Block just as you raise it: Parry");
        translationBuilder.add("item.unseen_world.shield_of_chimeric_warrior.ability.1", "A parry with the Rockbreaker in hand readies its strike");
        translationBuilder.add("item.unseen_world.shield_of_chimeric_warrior.lore", "It held back the Warrior. Now it holds back the dark");

        translationBuilder.add("item.unseen_world.gripping_bundle.ability.0", "Holds gripcrystal and griptonite. Eclipsebane draws its fuel from it");
        translationBuilder.add("item.unseen_world.gripping_bundle.lore", "It never lets go of what it holds");

        translationBuilder.add("item.unseen_world.gripping_gauntlet.ability.0", "Release on hold use:: tear the held item from a creature");
        translationBuilder.add("item.unseen_world.gripping_gauntlet.lore", "Grip what is not yours");

        translationBuilder.add("block.unseen_world.effigy.status", "Keys %s/%s · Seals %s/%s");
        translationBuilder.add("block.unseen_world.effigy.wrong_key", "This effigy answers only to the %s");
        translationBuilder.add("block.unseen_world.effigy.awake", "The effigy is already stirring");
        translationBuilder.add("block.unseen_world.effigy.full", "Every socket is already filled");

        translationBuilder.add("block.unseen_world.ordeal_spawner.primary", "First wave: %s");
        translationBuilder.add("block.unseen_world.ordeal_spawner.secondary", "Second wave: %s");

        translationBuilder.add("block.unseen_world.amethyst_sign", "Amethyst Sign");
        translationBuilder.add("block.unseen_world.amethyst_hanging_sign", "Amethyst Hanging Sign");
        translationBuilder.add("block.unseen_world.burlywood_sign", "Burlywood Sign");
        translationBuilder.add("block.unseen_world.burlywood_hanging_sign", "Burlywood Hanging Sign");
        translationBuilder.add("block.unseen_world.tealive_sign", "Tealive Sign");
        translationBuilder.add("block.unseen_world.tealive_hanging_sign", "Tealive Hanging Sign");
        translationBuilder.add("block.unseen_world.grizzly_sign", "Grizzly Sign");
        translationBuilder.add("block.unseen_world.grizzly_hanging_sign", "Grizzly Hanging Sign");
        translationBuilder.add("block.unseen_world.crimsonveil_sign", "Crimsonveil Sign");
        translationBuilder.add("block.unseen_world.crimsonveil_hanging_sign", "Crimsonveil Hanging Sign");

        translationBuilder.add("sounds.unseen_world.gleamcarver_ambient_sound", "Gleamcarver squeak");
        translationBuilder.add("sounds.unseen_world.ordeal_spawner_rewarding", "Ordeal spawner rewarding");
        translationBuilder.add("sounds.unseen_world.wind_slide_sound", "Whirlwind rises");
        translationBuilder.add("sounds.unseen_world.ambient_sound", "Darkness vibrates");

        translationBuilder.add("filled_map.unseen_world.structures", "Darkness Boss Structures map");

        translationBuilder.add("item.minecraft.tipped_arrow.effect.gripping_resistance", "Arrow of Gripping Resistance");

        translationBuilder.add("item.minecraft.potion.effect.gripping_resistance", "Potion of Gripping Resistance");
        translationBuilder.add("item.minecraft.splash_potion.effect.gripping_resistance", "Splash Potion of Gripping Resistance");
        translationBuilder.add("item.minecraft.lingering_potion.effect.gripping_resistance", "Lingering Potion of Gripping Resistance");

        translationBuilder.add("key.category.unseen_world.unseen_world", "Unseen World's Key binds");
        translationBuilder.add("key.unseen_world.gripcrystal_weapons_abilities", "Key to change ability");
        translationBuilder.add("key.unseen_world.extra_gripcrystal_weapons_abilities", "Key to use ability with BetterCombat");

        translationBuilder.add("advancement.unseen_world.into_the_chimeric_darkness", "Into the Chimeric Darkness");
        translationBuilder.add("advancement.unseen_world.into_the_chimeric_darkness.desc", "Place, where echoes of the Overworld whisper in void");
        translationBuilder.add("advancement.unseen_world.into_the_heart_of_darkness", "Heart of the Darkness");
        translationBuilder.add("advancement.unseen_world.into_the_heart_of_darkness.desc", "Brave the shadows, but beware what looks back. (We hope it's friendly...)");
        translationBuilder.add("advancement.unseen_world.explore_chimeric_darkness", "Darkness Tourist Destinations");
        translationBuilder.add("advancement.unseen_world.explore_chimeric_darkness.desc", "Explore all Chimeric Darkness biomes");
        translationBuilder.add("advancement.unseen_world.cure_gripcrystal_entity", "Captive of Gripping on Freedom now");
        translationBuilder.add("advancement.unseen_world.cure_gripcrystal_entity.desc", "Grip no more! The illness fades as griptonite works its magic");
        translationBuilder.add("advancement.unseen_world.fortress_in_the_darkness", "Fortress in the Darkness");
        translationBuilder.add("advancement.unseen_world.fortress_in_the_darkness.desc", "A shadowy stronghold looms... Did its master build it, or was it built for them?");
        translationBuilder.add("advancement.unseen_world.vault_of_eclipse", "Vault of Eclipse");
        translationBuilder.add("advancement.unseen_world.vault_of_eclipse.desc", "Deep below, where the light dare not tread... but what eclipsed the Sentinel's glory?");
        translationBuilder.add("advancement.unseen_world.whispers_of_the_light", "Whispers of the Light");
        translationBuilder.add("advancement.unseen_world.whispers_of_the_light.desc", "The one who bounded stars to darkness - defeated");
        translationBuilder.add("advancement.unseen_world.quenched_sun", "Quenched Sun");
        translationBuilder.add("advancement.unseen_world.quenched_sun.desc", "The one who hided eternal eclipse - defeated");

        ModBiomes.ALL.forEach(biome -> {
            String key = "biome." + biome.location().getNamespace() + "." + biome.location().getPath();
            translationBuilder.add(key, BIOME_NAME_OVERRIDES.getOrDefault(biome, convertToName(key)));
        });
        ModRegistry.BLOCKS.forEach(block -> {
            if (!block.getDescriptionId().contains("sign"))
                translationBuilder.add(block, convertToName(block.getDescriptionId()));
        });
        ModRegistry.ITEMS.forEach(item -> {
            if (!(item instanceof BlockItem))
                translationBuilder.add(item, convertToName(item.getDescriptionId()));
        });
        BuiltInRegistries.ENTITY_TYPE.forEach(entityType -> {
            if (entityType.getDescriptionId().contains(UnseenWorld.MOD_ID))
                translationBuilder.add(entityType, convertToName(entityType.getDescriptionId()));
        });
        SolRegistries.MOD.iterate(EffectHolder.class, holder -> {
            if (holder.getID().getNamespace().equals(UnseenWorld.MOD_ID))
                translationBuilder.add(holder.get(), convertToName(holder.get().getDescriptionId()));
        });
    }

    private String convertToName(String key) {
        String[] parts = key.split("\\.");
        String[] words = parts[parts.length - 1].split("_");

        StringBuilder nameBuilder = new StringBuilder();

        for (String word : words) {
            nameBuilder.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        String name = nameBuilder.toString().trim();
        if (true) {
            return name;
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}