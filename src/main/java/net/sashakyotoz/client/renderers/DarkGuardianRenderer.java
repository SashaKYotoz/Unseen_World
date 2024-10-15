package net.sashakyotoz.client.renderers;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.DarkGuardianModel;
import net.sashakyotoz.client.models.GleamcarverModel;
import net.sashakyotoz.common.entities.DarkGuardianEntity;
import net.sashakyotoz.common.entities.GleamcarverEntity;

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
                : UnseenWorld.makeID("textures/entity/dark_guardian/dark_guardian_" + entity.guardianType.typeName + ".png");
    }
}