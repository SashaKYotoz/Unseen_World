package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.SHolder;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.EffectHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.biome.Biome;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.world.biomes.ModBiomes;
import net.sashakyotoz.compat.UWInfoPages;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {
    private static final Map<ResourceKey<Biome>, String> BIOME_NAME_OVERRIDES = Map.of(
            ModBiomes.DEEP_GLACIEMITE_CAVES, "Deep Glaciemite Caverns");

    public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder builder) {
        builder.add("itemgroup.unseen_world.items", "Unseen World: Items");
        builder.add("itemgroup.unseen_world.blocks", "Unseen World: Blocks");

        builder.add("commands.unseen_world.weather.set.grippfall", "Set the weather to grippfall");

        builder.add("item.unseen_world.warpedveil_vine_fruit", "Warpedveil vine fruit");
        builder.add("item.unseen_world.glowroot_bulb", "Glowroot bulb");

        builder.add("item.unseen_world.armor_tips", "When the full set worn:");
        builder.add("item.unseen_world.abyssal_armor_tooltip", "Immunise you to Darkness, Falling into void");
        builder.add("item.unseen_world.titanium_armor_tooltip", "Immunise you to Fire");
        builder.add("item.unseen_world.unseenium_armor_tooltip", "Immunise you to Glowing");
        builder.add("item.unseen_world.unseenium_armor_tooltip1", "Slowly recover itself, if noone near");

        builder.add("item.unseen_world.griptonite_lantern_tooltip", "Removes gripping sickness of near players");

        builder.add("item.unseen_world.chimeric_rockbreaker_hammer.ability.0", "Release on hold use: Charged Strike");
        builder.add("item.unseen_world.chimeric_rockbreaker_hammer.ability.1", "Use sneaking: Fissure");
        builder.add("item.unseen_world.chimeric_rockbreaker_hammer.ability.2", "Hits break raised shields");
        builder.add("item.unseen_world.chimeric_rockbreaker_hammer.lore", "Still warm from the Warrior's last swing");

        builder.add("item.unseen_world.eclipsebane.ability.0", "Sweeps reach 3 blocks in a wide arc");
        builder.add("item.unseen_world.eclipsebane.ability.1", "Use sneaking: Dash");
        builder.add("item.unseen_world.eclipsebane.ability.2", "Release on hold use: Eclipse Pulse, fed by Granulated Griptonite");
        builder.add("item.unseen_world.eclipsebane.lore", "The Sentinel's eclipse, folded into a blade");

        builder.add("item.unseen_world.auric_arc.ability.0", "Critical hits strike a golden line");
        builder.add("item.unseen_world.auric_arc.ability.1", "Release on hold use: Sunburst");
        builder.add("item.unseen_world.auric_arc.ability.2", "A Sunburst throw leaves a mark of Weakness");
        builder.add("item.unseen_world.auric_arc.lore", "A sliver of the Patriarch's sun");

        builder.add("item.unseen_world.gripping_abyssal_bow.ability.0", "Full draw: arrows inflict Gripping");
        builder.add("item.unseen_world.gripping_abyssal_bow.ability.1", "Release on hold use: Abyssal Shot pierces and pulls fliers down");
        builder.add("item.unseen_world.gripping_abyssal_bow.lore", "Strung with a thread of the Abyss");

        builder.add("item.unseen_world.shield_of_chimeric_warrior.ability.0", "Block just as you raise it: Parry");
        builder.add("item.unseen_world.shield_of_chimeric_warrior.ability.1", "A parry with the Rockbreaker in hand readies its strike");
        builder.add("item.unseen_world.shield_of_chimeric_warrior.lore", "It held back the Warrior. Now it holds back the dark");

        builder.add("item.unseen_world.gripping_bundle.ability.0", "Holds gripcrystal and griptonite. Eclipsebane draws its fuel from it");
        builder.add("item.unseen_world.gripping_bundle.lore", "It never lets go of what it holds");

        builder.add("item.unseen_world.gripping_gauntlet.ability.0", "Release on hold use: tear the held item from a creature");
        builder.add("item.unseen_world.gripping_gauntlet.lore", "Grip what is not yours");

        builder.add("block.unseen_world.effigy.status", "Keys %s/%s · Seals %s/%s");
        builder.add("block.unseen_world.effigy.wrong_key", "This effigy answers only to the %s");
        builder.add("block.unseen_world.effigy.awake", "The effigy is already stirring");
        builder.add("block.unseen_world.effigy.full", "Every socket is already filled");

        builder.add("block.unseen_world.ordeal_spawner.primary", "First wave: %s");
        builder.add("block.unseen_world.ordeal_spawner.secondary", "Second wave: %s");

        builder.add("block.unseen_world.amethyst_sign", "Amethyst Sign");
        builder.add("block.unseen_world.amethyst_hanging_sign", "Amethyst Hanging Sign");
        builder.add("block.unseen_world.burlywood_sign", "Burlywood Sign");
        builder.add("block.unseen_world.burlywood_hanging_sign", "Burlywood Hanging Sign");
        builder.add("block.unseen_world.tealive_sign", "Tealive Sign");
        builder.add("block.unseen_world.tealive_hanging_sign", "Tealive Hanging Sign");
        builder.add("block.unseen_world.grizzly_sign", "Grizzly Sign");
        builder.add("block.unseen_world.grizzly_hanging_sign", "Grizzly Hanging Sign");
        builder.add("block.unseen_world.crimsonveil_sign", "Crimsonveil Sign");
        builder.add("block.unseen_world.crimsonveil_hanging_sign", "Crimsonveil Hanging Sign");

        builder.add("sounds.unseen_world.gleamcarver_ambient", "Gleamcarver squeak");

        builder.add("sounds.unseen_world.reaver_ambient", "Reaver dissatisfied");
        builder.add("sounds.unseen_world.reaver_scream", "Reaver screams");
        builder.add("sounds.unseen_world.reaver_grind", "Reaver grinds");
        builder.add("sounds.unseen_world.reaver_hurt", "Reaver hurt");
        builder.add("sounds.unseen_world.reaver_crack", "Reaver cracks");
        builder.add("sounds.unseen_world.reaver_death", "Reaver dies");
        builder.add("sounds.unseen_world.reaver_crumble", "Reaver crumbles");
        builder.add("sounds.unseen_world.reaver_teleport", "Reaver teleports");
        builder.add("sounds.unseen_world.reaver_resonate", "Reaver resonates");
        builder.add("sounds.unseen_world.reaver_attack", "Reaver attacks");
        builder.add("sounds.unseen_world.reaver_step", "Reaver steps");

        builder.add("sounds.unseen_world.vexal_beetle_ambient", "Vexal Beetle buzzes");
        builder.add("sounds.unseen_world.vexal_beetle_angry", "Vexal Beetle hums");
        builder.add("sounds.unseen_world.vexal_beetle_death", "Vexal Beetle dies");
        builder.add("sounds.unseen_world.vexal_beetle_attack", "Vexal Beetle attacks");
        builder.add("sounds.unseen_world.vexal_beetle_exhausted", "Vexal Beetle exhausted");

        builder.add("sounds.unseen_world.shimmer_ambient", "Shimmer emits");

        builder.add("sounds.unseen_world.ordeal_spawner_rewarding", "Ordeal spawner rewarding");
        builder.add("sounds.unseen_world.wind_slide_sound", "Whirlwind rises");

        builder.add("sounds.unseen_world.ambient_sound", "Darkness vibrates");

        builder.add("filled_map.unseen_world.structures", "Darkness Boss Structures map");
        builder.add("filled_map.unseen_world.ruined_structures", "Darkness Ruin Structures map");

        builder.add("item.minecraft.tipped_arrow.effect.gripping_resistance", "Arrow of Gripping Resistance");

        builder.add("item.minecraft.potion.effect.gripping_resistance", "Potion of Gripping Resistance");
        builder.add("item.minecraft.splash_potion.effect.gripping_resistance", "Splash Potion of Gripping Resistance");
        builder.add("item.minecraft.lingering_potion.effect.gripping_resistance", "Lingering Potion of Gripping Resistance");

        builder.add("tag.unseen_world.amethyst_logs","Amethyst Logs");
        builder.add("tag.unseen_world.burlywood_logs","Burlywood Logs");
        builder.add("tag.unseen_world.tealive_logs","Tealive Logs");
        builder.add("tag.unseen_world.grizzly_logs","Grizzly Logs");
        builder.add("tag.unseen_world.crimsonveil_logs","Crimsonveil Logs");

        builder.add("tag.unseen_world.vexal_beetle_taming_items","Vexal Beetle Taming Items");
        builder.add("tag.unseen_world.rimehorn_taming_items","Rimehorn Taming Items");
        builder.add("tag.unseen_world.serpent_taming_items","Serpent Taming Items");
        builder.add("tag.unseen_world.tuskhog_taming_items","Tuskhog Taming Items");

        builder.add("tag.unseen_world.gripping_bundle_can_handle","Gripping Bundle Can Contain");

        builder.add("advancement.unseen_world.into_the_chimeric_darkness", "Into the Chimeric Darkness");
        builder.add("advancement.unseen_world.into_the_chimeric_darkness.desc", "Place, where echoes of the Overworld whisper in void");
        builder.add("advancement.unseen_world.into_the_heart_of_darkness", "Heart of the Darkness");
        builder.add("advancement.unseen_world.into_the_heart_of_darkness.desc", "Brave the shadows, but beware what looks back. (We hope it's friendly...)");
        builder.add("advancement.unseen_world.explore_chimeric_darkness", "Darkness Tourist Destinations");
        builder.add("advancement.unseen_world.explore_chimeric_darkness.desc", "Explore all Chimeric Darkness biomes");
        builder.add("advancement.unseen_world.cure_gripcrystal_entity", "Captive of Gripping on Freedom now");
        builder.add("advancement.unseen_world.cure_gripcrystal_entity.desc", "Grip no more! The illness fades as griptonite works its magic");
        builder.add("advancement.unseen_world.steal", "Steal!");
        builder.add("advancement.unseen_world.steal.desc", "Tear something from another creature's grip with the Gripping Gauntlet. A firm believer in equality, it takes from anyone");
        builder.add("advancement.unseen_world.fortress_in_the_darkness", "Fortress in the Darkness");
        builder.add("advancement.unseen_world.fortress_in_the_darkness.desc", "A shadowy fortress looms... Did its master build it, or was it built for them?");
        builder.add("advancement.unseen_world.into_the_penumbral_steppe", "Steppe of Half-Light");
        builder.add("advancement.unseen_world.into_the_penumbral_steppe.desc", "Walk the ashen steppe, where every day is an eclipse");
        builder.add("advancement.unseen_world.ring_of_the_corona", "Ring of the Corona");
        builder.add("advancement.unseen_world.ring_of_the_corona.desc", "Pale stones in a black field... Something still keeps watch over them");
        builder.add("advancement.unseen_world.into_the_auric_heights", "Gilded Summits");
        builder.add("advancement.unseen_world.into_the_auric_heights.desc", "Climb the peaks that catch a sun no one can see");
        builder.add("advancement.unseen_world.citadel_above_the_peaks", "Citadel Above the Peaks");
        builder.add("advancement.unseen_world.citadel_above_the_peaks.desc", "A golden spire, an open sky... and wings circling above it");
        builder.add("advancement.unseen_world.whispers_of_the_light", "Whispers of the Light");
        builder.add("advancement.unseen_world.whispers_of_the_light.desc", "The one who bounded stars to darkness - defeated");
        builder.add("advancement.unseen_world.quenched_sun", "Quenched Sun");
        builder.add("advancement.unseen_world.quenched_sun.desc", "The one who hided eternal eclipse - defeated");
        builder.add("advancement.unseen_world.fallen_crown", "Fallen Crown");
        builder.add("advancement.unseen_world.fallen_crown.desc", "The one who raised gold from the dead - defeated");

        builder.add("tooltip.unseen_world.effigy.boss", "Boss: %s");
        builder.add("tooltip.unseen_world.effigy.phase.dormant", "Dormant");
        builder.add("tooltip.unseen_world.effigy.phase.awakening", "Awakening");
        builder.add("tooltip.unseen_world.effigy.phase.awakened", "The boss is awake");
        builder.add("tooltip.unseen_world.ordeal_spawner.phase.inactive", "Waiting for challengers");
        builder.add("tooltip.unseen_world.ordeal_spawner.phase.waiting_for_players", "Charging");
        builder.add("tooltip.unseen_world.ordeal_spawner.phase.active", "Ordeal in progress");
        builder.add("tooltip.unseen_world.ordeal_spawner.phase.ejecting_reward", "Ejecting rewards");
        builder.add("tooltip.unseen_world.ordeal_spawner.phase.cooldown", "Cooling down");
        builder.add("tooltip.unseen_world.ordeal_spawner.gripping", "Gripping ordeal");
        builder.add("tooltip.unseen_world.ordeal_spawner.wave", "Wave %s: %s");
        builder.add("tooltip.unseen_world.ordeal_spawner.current_wave", "Wave %s of 2: %s");
        builder.add("tooltip.unseen_world.ordeal_spawner.progress", "Mobs: %s/%s");
        builder.add("tooltip.unseen_world.ordeal_spawner.ready_in", "Ready in %s");
        builder.add("tooltip.unseen_world.fruit.ripe", "Fruit: ripe");
        builder.add("tooltip.unseen_world.fruit.growing", "Fruit: growing");
        builder.add("tooltip.unseen_world.burlywood_heart.natural", "Natural heart");
        builder.add("tooltip.unseen_world.burlywood_heart.persistent", "Placed heart");
        builder.add("tooltip.unseen_world.gripping", "Gripping: %s s");
        builder.add("tooltip.unseen_world.gripping_immune", "Immune to Gripping");
        builder.add("config.jade.plugin_unseen_world.effigy", "Effigy keys and seals");
        builder.add("config.jade.plugin_unseen_world.ordeal_spawner", "Ordeal spawner waves");
        builder.add("config.jade.plugin_unseen_world.fruit", "Fruit ripeness");
        builder.add("config.jade.plugin_unseen_world.burlywood_heart", "Burlywood heart state");
        builder.add("config.jade.plugin_unseen_world.gripping", "Gripping level");

        builder.add("effect.unseen_world.gripping_resistance.desc", "Provides temporary resistance to gripcrystal infection.");

        for (UWInfoPages.Page page : UWInfoPages.PAGES) {
            for (int line = 0; line < page.lines().size(); line++) {
                builder.add(UWInfoPages.lineKey(page, line), page.lines().get(line));
            }
        }

        ModBiomes.ALL.forEach(biome -> {
            String key = "biome." + biome.location().getNamespace() + "." + biome.location().getPath();
            builder.add(key, BIOME_NAME_OVERRIDES.getOrDefault(biome, convertToName(key)));
        });
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            if (isOwn(holder) && !holder.get().getDescriptionId().contains("sign"))
                builder.add(holder.get(), convertToName(holder.get().getDescriptionId()));
        });
        SolRegistries.MOD.iterate(ItemHolder.class, holder -> {
            if (isOwn(holder) && !(holder.get() instanceof BlockItem))
                builder.add(holder.get(), convertToName(holder.get().getDescriptionId()));
        });
        BuiltInRegistries.ENTITY_TYPE.forEach(entityType -> {
            if (entityType.getDescriptionId().contains(UnseenWorld.MOD_ID))
                builder.add(entityType, convertToName(entityType.getDescriptionId()));
        });
        SolRegistries.MOD.iterate(EffectHolder.class, holder -> {
            if (isOwn(holder))
                builder.add(holder.get(), convertToName(holder.get().getDescriptionId()));
        });
    }

    private static boolean isOwn(SHolder<?> holder) {
        return holder.getID().getNamespace().equals(UnseenWorld.MOD_ID);
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
        return nameBuilder.toString().trim();
    }
}