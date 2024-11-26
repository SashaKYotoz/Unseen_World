package net.sashakyotoz.client.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.DarkGuardianModel;
import net.sashakyotoz.common.entities.custom.DarkGuardianEntity;

public class DarkGuardianRenderer extends DeathFixedMobRenderer<DarkGuardianEntity, DarkGuardianModel> {

    public DarkGuardianRenderer(EntityRendererFactory.Context context) {
        super(context, new DarkGuardianModel(context.getPart(DarkGuardianModel.DARK_GUARDIAN)), 0.5f);
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));

        this.addFeature(new ArmorFeatureRenderer<>(
                this,
                new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER_INNER_ARMOR)),
                new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR)),
                context.getModelManager()));
    }

    @Override
    public void render(DarkGuardianEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        this.model.handSwingProgress = this.getHandSwingProgress(mobEntity, g);
        this.model.riding = mobEntity.hasVehicle();
        this.model.child = mobEntity.isBaby();
        float h = MathHelper.lerpAngleDegrees(g, mobEntity.prevBodyYaw, mobEntity.bodyYaw);
        float j = MathHelper.lerpAngleDegrees(g, mobEntity.prevHeadYaw, mobEntity.headYaw);
        float k = j - h;
        if (mobEntity.hasVehicle() && mobEntity.getVehicle() instanceof LivingEntity) {
            LivingEntity livingEntity2 = (LivingEntity) mobEntity.getVehicle();
            h = MathHelper.lerpAngleDegrees(g, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
            k = j - h;
            float l = MathHelper.wrapDegrees(k);
            if (l < -85.0F) {
                l = -85.0F;
            }

            if (l >= 85.0F) {
                l = 85.0F;
            }

            h = j - l;
            if (l * l > 2500.0F) {
                h += l * 0.2F;
            }

            k = j - h;
        }

        float m = MathHelper.lerp(g, mobEntity.prevPitch, mobEntity.getPitch());
        if (shouldFlipUpsideDown(mobEntity)) {
            m *= -1.0F;
            k *= -1.0F;
        }

        if (mobEntity.isInPose(EntityPose.SLEEPING)) {
            Direction direction = mobEntity.getSleepingDirection();
            if (direction != null) {
                float n = mobEntity.getEyeHeight(EntityPose.STANDING) - 0.1F;
                matrixStack.translate((float) (-direction.getOffsetX()) * n, 0.0F, (float) (-direction.getOffsetZ()) * n);
            }
        }

        float lx = this.getAnimationProgress(mobEntity, g);
        this.setupTransforms(mobEntity, matrixStack, lx, h, g);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(mobEntity, matrixStack, g);
        matrixStack.translate(0.0F, -1.501F, 0.0F);
        float n = 0.0F;
        float o = 0.0F;
        if (!mobEntity.hasVehicle() && mobEntity.isAlive()) {
            n = mobEntity.limbAnimator.getSpeed(g);
            o = mobEntity.limbAnimator.getPos(g);
            if (mobEntity.isBaby()) {
                o *= 3.0F;
            }

            if (n > 1.0F) {
                n = 1.0F;
            }
        }

        this.model.animateModel(mobEntity, o, n, g);
        this.model.setAngles(mobEntity, o, n, lx, k, m);
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean bl = this.isVisible(mobEntity);
        boolean bl2 = !bl && !mobEntity.isInvisibleTo(minecraftClient.player);
        boolean bl3 = minecraftClient.hasOutline(mobEntity);
        RenderLayer renderLayer = this.getRenderLayer(mobEntity, bl, bl2, bl3);
        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
            int p = getOverlay(mobEntity, this.getAnimationCounter(mobEntity, g));
            if (mobEntity.getWorld().getClosestPlayer(mobEntity, 32) != null
                    && mobEntity.isEntityLookingAtMe(mobEntity.getWorld().getClosestPlayer(mobEntity, 32), 0.5, false, true, LivingEntity::canHit, mobEntity::getEyeY, mobEntity::getY, () -> (mobEntity.getEyeY() + mobEntity.getY()) / 2.0))
                this.model.render(matrixStack, vertexConsumer, i, p, 0F, 0F, 0F, 0.5f);
            else
                this.model.render(matrixStack, vertexConsumer, i, p, 1.0F, 1.0F, 1.0F, bl2 ? 0.15F : 1.0F);
        }

        if (!mobEntity.isSpectator() && !(mobEntity.getWorld().getClosestPlayer(mobEntity, 32) != null
                && mobEntity.isEntityLookingAtMe(mobEntity.getWorld().getClosestPlayer(mobEntity, 32), 0.5, false, true, LivingEntity::canHit, mobEntity::getEyeY, mobEntity::getY, () -> (mobEntity.getEyeY() + mobEntity.getY()) / 2.0))) {
            for (FeatureRenderer<DarkGuardianEntity, DarkGuardianModel> featureRenderer : this.features)
                featureRenderer.render(matrixStack, vertexConsumerProvider, i, mobEntity, o, n, g, lx, k, m);
        }
        matrixStack.pop();
    }

    @Override
    protected void setupTransforms(DarkGuardianEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
        BipedEntityModel.ArmPose armPose = getArmPose(entity, Hand.MAIN_HAND);
        BipedEntityModel.ArmPose armPose2 = getArmPose(entity, Hand.OFF_HAND);
        if (armPose.isTwoHanded())
            armPose2 = entity.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
        this.getModel().rightArmPose = armPose;
        this.getModel().leftArmPose = armPose2;
    }

    private static BipedEntityModel.ArmPose getArmPose(DarkGuardianEntity guardian, Hand hand) {
        ItemStack itemStack = guardian.getStackInHand(hand);
        if (itemStack.isEmpty()) {
            return BipedEntityModel.ArmPose.EMPTY;
        } else {
            if (guardian.getActiveHand() == hand && guardian.getItemUseTimeLeft() > 0) {
                UseAction useAction = itemStack.getUseAction();
                if (useAction == UseAction.BLOCK)
                    return BipedEntityModel.ArmPose.BLOCK;
                if (useAction == UseAction.BOW)
                    return BipedEntityModel.ArmPose.BOW_AND_ARROW;
                if (useAction == UseAction.SPEAR)
                    return BipedEntityModel.ArmPose.THROW_SPEAR;
                if (useAction == UseAction.CROSSBOW && hand == guardian.getActiveHand())
                    return BipedEntityModel.ArmPose.CROSSBOW_CHARGE;
            } else if (!guardian.handSwinging && itemStack.isOf(Items.CROSSBOW) && CrossbowItem.isCharged(itemStack))
                return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
            return BipedEntityModel.ArmPose.ITEM;
        }
    }

    @Override
    public Identifier getTexture(DarkGuardianEntity entity) {
        return entity.guardianType == null ? UnseenWorld.makeID("textures/entity/dark_guardian/dark_guardian_purple.png")
                : UnseenWorld.makeID("textures/entity/dark_guardian/dark_guardian_" + DarkGuardianEntity.Type.fromId(entity.getVariant().getId()) + ".png");
    }
}