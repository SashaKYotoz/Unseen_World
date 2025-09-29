package net.sashakyotoz.api.entity_data.data;

import net.minecraft.entity.LivingEntity;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.common.config.ConfigEntries;
import net.sashakyotoz.common.tags.ModTags;

public class GrippingData {

    public static void addGrippingSeconds(IGrippingEntity entity, int amount) {
        if (entity instanceof LivingEntity entity1 && !entity1.getType().isIn(ModTags.Entities.GRIPPING_IMMUNE_ENTITY_TYPES)
                && entity.getGrippingData() + amount < 100) {
            int seconds = entity.getGrippingData();
            seconds += amount;
            seconds = Math.max(seconds, 0);
            entity.setGrippingData(seconds);
        }
    }

    public static void removeGrippingPerTick(IGrippingEntity entity) {
        if (ConfigEntries.removeGrippingNaturally) {
            int gripping = entity.getGrippingData();
            gripping = gripping - 1;
            entity.setGrippingData(gripping);
        }
    }

    public static void removeGripping(IGrippingEntity entity) {
        entity.setGrippingData(0);
    }
}