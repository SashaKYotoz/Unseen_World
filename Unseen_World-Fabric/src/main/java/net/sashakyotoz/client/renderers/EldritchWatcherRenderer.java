package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.EldritchWatcherModel;
import net.sashakyotoz.client.renderers.layers.EldritchWatcherBlockFeatureRenderer;
import net.sashakyotoz.client.renderers.layers.EldritchWatcherPulsatingLayer;
import net.sashakyotoz.common.entities.custom.EldritchWatcherEntity;

public class EldritchWatcherRenderer extends DeathFixedMobRenderer<EldritchWatcherEntity, EldritchWatcherModel> {
    public EldritchWatcherRenderer(EntityRendererProvider.Context context) {
        super(context, new EldritchWatcherModel(context.bakeLayer(EldritchWatcherModel.ELDRITCH_WATCHER)), 0.5f);
        this.addLayer(new EldritchWatcherBlockFeatureRenderer(this, context.getBlockRenderDispatcher()));
        this.addLayer(new EldritchWatcherPulsatingLayer<>(this));
    }

    @Override
    protected boolean isShaking(EldritchWatcherEntity entity) {
        return super.isShaking(entity) || entity.isConverting();
    }

    @Override
    public ResourceLocation getTextureLocation(EldritchWatcherEntity entity) {
        return UnseenWorld.makeID("textures/entity/eldritch_watcher/eldritch_watcher.png");
    }
}