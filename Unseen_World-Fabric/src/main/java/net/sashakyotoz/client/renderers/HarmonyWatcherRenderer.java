package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.HarmonyWatcherModel;
import net.sashakyotoz.common.entities.custom.HarmonyWatcherEntity;

public class HarmonyWatcherRenderer extends MobRenderer<HarmonyWatcherEntity, HarmonyWatcherModel> {

    public HarmonyWatcherRenderer(EntityRendererProvider.Context context) {
        super(context, new HarmonyWatcherModel(context.bakeLayer(HarmonyWatcherModel.HARMONY_WATCHER)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this,context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(HarmonyWatcherEntity entity) {
        return entity.watcherType == null ? UnseenWorld.makeID("textures/entity/harmony_watcher/harmony_watcher_glaciemite.png")
                : UnseenWorld.makeID("textures/entity/harmony_watcher/harmony_watcher_" + entity.watcherType.typeName + ".png");
    }
}