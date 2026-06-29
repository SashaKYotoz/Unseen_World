package net.sashakyotoz.client.renderers.projectiles;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.projectiles.GrippingCrystalProjectileEntity;

public class GrippingCrystalProjectileRenderer extends ArrowRenderer<GrippingCrystalProjectileEntity> {
    public static final ResourceLocation TEXTURE = UnseenWorld.makeID("textures/entity/projectiles/gripping_crystal.png");

    public GrippingCrystalProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(GrippingCrystalProjectileEntity arrowEntity) {
        return TEXTURE;
    }
}