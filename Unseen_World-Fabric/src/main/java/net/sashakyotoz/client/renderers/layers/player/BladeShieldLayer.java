package net.sashakyotoz.client.renderers.layers.player;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BlazeEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.common.items.custom.IGrippingWeapons;
import net.sashakyotoz.utils.Oscillator;

public class BladeShieldLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private final EntityRendererFactory.Context context;

    public BladeShieldLayer(FeatureRendererContext<T, M> context, EntityRendererFactory.Context context1) {
        super(context);
        this.context = context1;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        BlazeEntityModel<BlazeEntity> blazeModel = new BlazeEntityModel<>(context.getPart(EntityModelLayers.BLAZE));
        if (entity instanceof PlayerEntity player
                && GripcrystalManaData.getMana((IEntityDataSaver) player) > 0) {
            if (!player.getInventory().isEmpty()
                    && player.getInventory().containsAny(itemStack -> itemStack.getItem() instanceof EclipsebaneItem
                    && IGrippingWeapons.getPhase(itemStack).equals("blade_shield"))) {
                for (int i = 1; i < 6; i++) {
                    matrices.push();
                    float scaleModifier = 2.5f;
                    matrices.scale(scaleModifier, scaleModifier, scaleModifier);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotation((ClientTicks.getHalfTicks() / 2) % 360 + (i * 4f)), 0, 0, 0);
                    blazeModel.getChild("part" + (i + 1))
                            .ifPresent(part -> part.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(UnseenWorld.makeID("textures/entity/layers/blade_shield.png"),
                                    Oscillator.getOscillatingValue(), Oscillator.getOscillatingValue())), light, OverlayTexture.DEFAULT_UV));
                    matrices.pop();
                }
            }
        }
    }
}