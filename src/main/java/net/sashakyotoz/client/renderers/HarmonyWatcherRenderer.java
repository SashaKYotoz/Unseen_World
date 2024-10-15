package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.HarmonyWatcherModel;
import net.sashakyotoz.common.entities.HarmonyWatcherEntity;

public class HarmonyWatcherRenderer extends DeathFixedMobRenderer<HarmonyWatcherEntity, HarmonyWatcherModel> {

    public HarmonyWatcherRenderer(EntityRendererFactory.Context context) {
        super(context, new HarmonyWatcherModel(context.getPart(HarmonyWatcherModel.HARMONY_WATCHER)), 0.5f);
        this.addFeature(new HeldItemFeatureRenderer<>(this,context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(HarmonyWatcherEntity entity) {
        return entity.watcherType == null ? UnseenWorld.makeID("textures/entity/harmony_watcher/harmony_watcher_glaciemite.png")
                : UnseenWorld.makeID("textures/entity/harmony_watcher/harmony_watcher_" + entity.watcherType.typeName + ".png");
    }
}