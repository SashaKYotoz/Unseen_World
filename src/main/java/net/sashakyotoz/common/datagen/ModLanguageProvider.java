package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.item.BlockItem;
import net.sashakyotoz.common.ModRegistry;

public class ModLanguageProvider extends FabricLanguageProvider {
    public ModLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("itemgroup.unseen_world.items", "Unseen World: Items");
        translationBuilder.add("itemgroup.unseen_world.blocks", "Unseen World: Blocks");

        translationBuilder.add("entity.unseen_world.warrior_of_chimeric_darkness", "Warrior of Chimeric Darkness");

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

        ModRegistry.BLOCKS.forEach(block -> {
            if (!block.getTranslationKey().contains("sign"))
                translationBuilder.add(block, convertToName(block.getTranslationKey(), true));
        });
        ModRegistry.ITEMS.forEach(item -> {
            if (!(item instanceof BlockItem))
                translationBuilder.add(item, convertToName(item.getTranslationKey(), false));
        });
    }

    private String convertToName(String key, boolean isBlock) {
        String[] parts = key.split("\\.");
        String[] words = parts[parts.length - 1].split("_");

        StringBuilder nameBuilder = new StringBuilder();

        for (String word : words) {
            nameBuilder.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        String name = nameBuilder.toString().trim();
        if (isBlock) {
            return name;
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}