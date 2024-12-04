package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;

public class ModLanguageProvider extends FabricLanguageProvider {
    public ModLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("itemgroup.unseen_world.items", "Unseen World: Items");
        translationBuilder.add("itemgroup.unseen_world.blocks", "Unseen World: Blocks");

//        translationBuilder.add("entity.unseen_world.warrior_of_chimeric_darkness", "Warrior of Chimeric Darkness");
//        translationBuilder.add("entity.unseen_world.eclipse_sentinel", "Eclipse Sentinel");

        translationBuilder.add("item.unseen_world.warpedveil_vine_fruit", "Warpedveil vine fruit");

        translationBuilder.add("item.unseen_world.armor_tips", "When the full set worn:");
        translationBuilder.add("item.unseen_world.abyssal_armor_tooltip", "Immunise you to Darkness, Falling into void");
        translationBuilder.add("item.unseen_world.titanium_armor_tooltip", "Immunise you to Fire");
        translationBuilder.add("item.unseen_world.unseenium_armor_tooltip", "Immunise you to Glowing");
        translationBuilder.add("item.unseen_world.unseenium_armor_tooltip1", "Slowly recover itself, if noone near");

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

        translationBuilder.add("item.minecraft.potion.effect.glowing", "Potion of Glowing");
        translationBuilder.add("item.minecraft.splash_potion.effect.glowing", "Splash Potion of Glowing");
        translationBuilder.add("item.minecraft.lingering_potion.effect.glowing", "Lingering Potion of Glowing");

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

        ModRegistry.BLOCKS.forEach(block -> {
            if (!block.getTranslationKey().contains("sign"))
                translationBuilder.add(block, convertToName(block.getTranslationKey(), true));
        });
        ModRegistry.ITEMS.forEach(item -> {
            if (!(item instanceof BlockItem))
                translationBuilder.add(item, convertToName(item.getTranslationKey(), false));
        });
        Registries.ENTITY_TYPE.forEach(entityType -> {
            if (entityType.getTranslationKey().contains(UnseenWorld.MOD_ID))
                translationBuilder.add(entityType, convertToName(entityType.getTranslationKey(), true));
        });
    }

    private String convertToName(String key, boolean toLeaveUppercase) {
        String[] parts = key.split("\\.");
        String[] words = parts[parts.length - 1].split("_");

        StringBuilder nameBuilder = new StringBuilder();

        for (String word : words) {
            nameBuilder.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        String name = nameBuilder.toString().trim();
        if (toLeaveUppercase) {
            return name;
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}