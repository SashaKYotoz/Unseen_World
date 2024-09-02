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
        translationBuilder.add("itemgroup.unseen_world.equipment", "Unseen World: Equipment");
        translationBuilder.add("itemgroup.unseen_world.blocks", "Unseen World: Blocks");

        ModRegistry.BLOCKS.forEach(block ->
                translationBuilder.add(block, convertToName(block.getTranslationKey(), true))
        );
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