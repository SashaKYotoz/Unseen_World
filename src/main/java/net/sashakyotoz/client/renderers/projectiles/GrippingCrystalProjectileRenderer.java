package net.sashakyotoz.client.renderers.projectiles;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.projectiles.GrippingCrystalProjectileEntity;

public class GrippingCrystalProjectileRenderer extends ProjectileEntityRenderer<GrippingCrystalProjectileEntity> {
    public static final Identifier TEXTURE = UnseenWorld.makeID("textures/entity/projectiles/gripping_crystal.png");

    public GrippingCrystalProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(GrippingCrystalProjectileEntity arrowEntity) {
        return TEXTURE;
    }
}