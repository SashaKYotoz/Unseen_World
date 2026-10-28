package net.sashakyotoz.client.renderers.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.sashakyotoz.common.entities.custom.projectiles.ThrownAuricArc;
import net.sashakyotoz.common.items.ModItems;

public class ThrownAuricArcRenderer extends EntityRenderer<ThrownAuricArc> {
    private static final float SCALE = 1.5F;
    private static final float TIP_ANGLE = 45.0F;
    private static final float TIP_PULLBACK = 0.35F;

    private final ItemRenderer itemRenderer;
    private final ItemStack plain;
    private final ItemStack glinting;

    public ThrownAuricArcRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.plain = new ItemStack(ModItems.AURIC_ARC);
        this.glinting = this.plain.copy();
        this.glinting.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
    }

    @Override
    public void render(ThrownAuricArc entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, entity.getBbHeight() * 0.5F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        poseStack.translate(-TIP_PULLBACK * SCALE, 0.0F, 0.0F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-TIP_ANGLE));
        poseStack.scale(SCALE, SCALE, SCALE);
        this.itemRenderer.renderStatic(entity.isFoil() ? this.glinting : this.plain, ItemDisplayContext.GUI,
                packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffers, entity.level(), entity.getId());
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownAuricArc entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}