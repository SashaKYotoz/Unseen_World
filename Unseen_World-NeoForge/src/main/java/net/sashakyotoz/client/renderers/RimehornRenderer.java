package net.sashakyotoz.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.RimehornModel;
import net.sashakyotoz.common.entities.custom.RimehornEntity;

public class RimehornRenderer extends MobRenderer<RimehornEntity, RimehornModel<RimehornEntity>> {
    private static final ResourceLocation TEXTURE = UnseenWorld.MOD.makeID("textures/entity/rimehorn/rimehorn.png");
    private static final float BABY_SCALE = 0.5F;

    public RimehornRenderer(EntityRendererProvider.Context context) {
        super(context, new RimehornModel<>(context.bakeLayer(RimehornModel.RIMEHORN)), 0.6F);
    }

    @Override
    public void render(RimehornEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.shadowRadius = entity.isBaby() ? 0.3F : 0.6F;
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void scale(RimehornEntity entity, PoseStack poseStack, float partialTickTime) {
        if (entity.isBaby()) {
            poseStack.scale(BABY_SCALE, BABY_SCALE, BABY_SCALE);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(RimehornEntity entity) {
        return TEXTURE;
    }
}
