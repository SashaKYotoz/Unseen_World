package net.sashakyotoz.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.EspyerModel;
import net.sashakyotoz.client.renderers.layers.EspyerChargeFeatureRenderer;
import net.sashakyotoz.client.renderers.layers.GlowingPartsFeatureRenderer;
import net.sashakyotoz.common.entities.custom.EspyerEntity;

import java.util.Calendar;

public class EspyerRenderer extends MobRenderer<EspyerEntity, EspyerModel> {
    private final ResourceLocation XMAS_TEXTURE = UnseenWorld.makeID("textures/entity/espyer/espyer_xmas.png");
    private final boolean xmasTexture;

    public EspyerRenderer(EntityRendererProvider.Context context) {
        super(context, new EspyerModel(context.bakeLayer(EspyerModel.ESPYER)), 0.5F);
        this.addLayer(new EspyerChargeFeatureRenderer(this, context.getModelSet()));
        this.addLayer(new GlowingPartsFeatureRenderer<>(this, UnseenWorld.makeID("textures/entity/espyer/espyer_glowing_parts.png")));
        Calendar calendar = Calendar.getInstance();
        this.xmasTexture = calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 20 && calendar.get(Calendar.DATE) <= 26;
    }

    protected void scale(EspyerEntity espyer, PoseStack matrixStack, float f) {
        float g = espyer.getClientFuseTime(f);
        float h = 1.0F + Mth.sin(g * 100.0F) * g * 0.01F;
        g = Mth.clamp(g, 0.0F, 1.0F);
        g *= g;
        g *= g;
        float i = (1.0F + g * 0.4F) * h;
        float j = (1.0F + g * 0.1F) / h;
        matrixStack.scale(i, j, i);
    }

    protected float getAnimationCounter(EspyerEntity espyer, float f) {
        float g = espyer.getClientFuseTime(f);
        return (int) (g * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(g, 0.5F, 1.0F);
    }

    public ResourceLocation getTextureLocation(EspyerEntity espyer) {
        return xmasTexture ? XMAS_TEXTURE : UnseenWorld.makeID("textures/entity/espyer/espyer.png");
    }
}