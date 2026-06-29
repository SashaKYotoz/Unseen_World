package net.sashakyotoz.client.renderers.layers;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.client.models.EspyerModel;
import net.sashakyotoz.common.entities.custom.EspyerEntity;

public class EspyerChargeFeatureRenderer extends EnergySwirlLayer<EspyerEntity, EspyerModel> {
    private static final ResourceLocation SKIN = ResourceLocation.tryBuild("minecraft","textures/entity/creeper/creeper_armor.png");
    private final EspyerModel model;

    public EspyerChargeFeatureRenderer(RenderLayerParent<EspyerEntity, EspyerModel> context, EntityModelSet loader) {
        super(context);
        this.model = new EspyerModel(loader.bakeLayer(EspyerModel.ESPYER));
    }

    @Override
    protected float xOffset(float partialAge) {
        return partialAge * 0.01F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return SKIN;
    }

    @Override
    protected EntityModel<EspyerEntity> model() {
        return this.model;
    }
}