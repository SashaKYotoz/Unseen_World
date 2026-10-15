package net.sashakyotoz.api.entity_data.data;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.common.config.ModMainConfig;
import net.sashakyotoz.common.tags.ModTags;

public class GrippingData {
    public static final int MAX_GRIPPING = 99;

    public static boolean canBeGripped(IGrippingEntity entity) {
        return entity instanceof LivingEntity living && !living.getType().is(ModTags.Entities.GRIPPING_IMMUNE_ENTITY_TYPES);
    }

    public static void addGrippingSeconds(IGrippingEntity entity, int amount) {
        if (canBeGripped(entity)) {
            entity.setGrippingData(Mth.clamp(entity.getGrippingData() + amount, 0, MAX_GRIPPING));
        }
    }

    public static void removeGrippingPerTick(IGrippingEntity entity) {
        if (ModMainConfig.removeParasitesNaturally.get() && entity.getGrippingData() > 0) {
            entity.setGrippingData(entity.getGrippingData() - 1);
        }
    }

    public static boolean consumeGripping(IGrippingEntity entity, int threshold) {
        if (entity.getGrippingData() >= threshold) {
            removeGripping(entity);
            return true;
        }
        return false;
    }

    public static void removeGripping(IGrippingEntity entity) {
        entity.setGrippingData(0);
    }
}