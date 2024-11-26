package net.sashakyotoz.client.renderers.layers.player;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.WorldClientTickEventHandler;
import net.sashakyotoz.client.models.EclipsebaneModel;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.common.networking.data.GripcrystalManaData;
import net.sashakyotoz.utils.IEntityDataSaver;
import net.sashakyotoz.utils.Oscillator;

public class BladeShieldLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private final EntityRendererFactory.Context rendererContext;

    public BladeShieldLayer(FeatureRendererContext<T, M> context, EntityRendererFactory.Context context1) {
        super(context);
        rendererContext = context1;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemStack stack = entity.getMainHandStack();
        if ((stack.getItem() instanceof EclipsebaneItem item && item.getItemPhase(stack).equals("blade_shield") && GripcrystalManaData.getMana((IEntityDataSaver) entity) > 0)) {
            for (int i = 0; i < 4; i++) {
                EclipsebaneModel model = new EclipsebaneModel(rendererContext.getPart(EclipsebaneModel.ECLIPSEBANE));
                matrices.push();
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
                if (entity instanceof PlayerEntity) {
                    matrices.translate(0, -(Oscillator.getOscillatingValue(Math.round(WorldClientTickEventHandler.halfTicks.get(0) * 2))) / 4f + 0.375f, 0);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotation((WorldClientTickEventHandler.halfTicks.get(0) * i) % 360), 0, 0, 0);
                }
//                if (entity instanceof EclipseSentinelEntity eclipseSentinel) {
//                    double offset = Oscillator.getOscillatingValue(Math.round(WorldClientTickEventHandler.halfTicks.get(0)));
//                    matrices.translate(0, -(offset * 2) / 2f + 0.375f, 0);
//                    matrices.scale((float) (1.5f + offset / 2.5f), (float) (1.5f + offset / 2.5f), (float) (1.5f + offset / 2.5f));
//                    if (eclipseSentinel.getTarget() != null && eclipseSentinel.getTarget().squaredDistanceTo(eclipseSentinel) > 9)
//                        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(eclipseSentinel.getYaw() - 15 + 15 * i), 0, 0, 0);
//                    else
//                        matrices.multiply(RotationAxis.POSITIVE_Z.rotation((WorldClientTickEventHandler.halfTicks.get(0) * i) % 360), 0, 0, 0);
//                }
                model.getBlade().render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(UnseenWorld.makeID("textures/item/eclipsebane.png"))), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 0.35f);
                matrices.pop();
            }
        }
    }
}