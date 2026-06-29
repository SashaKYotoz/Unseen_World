package net.sashakyotoz.client.renderers.layers.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.common.items.custom.IGrippingWeapons;
import net.sashakyotoz.utils.Oscillator;

public class BladeShieldLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final EntityRendererProvider.Context context;

    public BladeShieldLayer(RenderLayerParent<T, M> context, EntityRendererProvider.Context context1) {
        super(context);
        this.context = context1;
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        BlazeModel<Blaze> blazeModel = new BlazeModel<>(context.bakeLayer(ModelLayers.BLAZE));
        if (entity instanceof Player player
                && GripcrystalManaData.getMana((IEntityDataSaver) player) > 0) {
            if (!player.getInventory().isEmpty()
                    && player.getInventory().hasAnyMatching(itemStack -> itemStack.getItem() instanceof EclipsebaneItem
                    && IGrippingWeapons.getPhase(itemStack).equals("blade_shield"))) {
                for (int i = 1; i < 6; i++) {
                    matrices.pushPose();
                    float scaleModifier = 2.5f;
                    matrices.scale(scaleModifier, scaleModifier, scaleModifier);
                    matrices.rotateAround(Axis.YP.rotation((ClientTicks.getHalfTicks() / 2) % 360 + (i * 4f)), 0, 0, 0);
                    blazeModel.getAnyDescendantWithName("part" + (i + 1))
                            .ifPresent(part -> part.render(matrices, vertexConsumers.getBuffer(RenderType.energySwirl(UnseenWorld.makeID("textures/entity/layers/blade_shield.png"),
                                    Oscillator.getOscillatingValue(), Oscillator.getOscillatingValue())), light, OverlayTexture.NO_OVERLAY));
                    matrices.popPose();
                }
            }
        }
    }
}