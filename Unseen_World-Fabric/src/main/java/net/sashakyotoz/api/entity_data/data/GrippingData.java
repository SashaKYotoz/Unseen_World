package net.sashakyotoz.api.entity_data.data;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.common.config.ModMainConfig;
import net.sashakyotoz.common.tags.ModTags;

import java.util.Collection;

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

    public static int applyGripping(CommandSourceStack source, Collection<? extends Entity> targets, int amount) {
        int affected = 0;
        for (Entity entity : targets) {
            if (entity instanceof IGrippingEntity grippingEntity) {
                GrippingData.addGrippingSeconds(grippingEntity, amount);
                affected++;
            }
        }
        if (affected == 0) {
            source.sendFailure(Component.literal("None of the selected entities can receive Gripping"));
            return 0;
        }
        return affected;
    }

    public static void removeGripping(IGrippingEntity entity) {
        entity.setGrippingData(0);
    }
}