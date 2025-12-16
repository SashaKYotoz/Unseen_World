package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.common.config.ConfigEntries;
import net.sashakyotoz.common.entities.animations.EldritchWatcherAnimations;
import net.sashakyotoz.common.entities.custom.EldritchWatcherEntity;
import net.sashakyotoz.utils.Oscillator;

public class EldritchWatcherModel extends SinglePartEntityModel<EldritchWatcherEntity> {
    public static final EntityModelLayer ELDRITCH_WATCHER = new EntityModelLayer(UnseenWorld.makeID("eldritch_watcher"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart head;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public EldritchWatcherModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.head = this.body.getChild("head");
        this.right_leg = this.root.getChild("right_leg");
        this.left_leg = this.root.getChild("left_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData body = root.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r1 = body.addChild("cube_r1", ModelPartBuilder.create().uv(0, 28).cuboid(-6.0F, -12.0F, -4.0F, 12.0F, 20.0F, 8.0F, new Dilation(0.3F))
                .uv(0, 0).cuboid(-6.0F, -12.0F, -4.0F, 12.0F, 20.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 4.0F, -0.75F, 0.0436F, 0.0F, 0.0F));

        ModelPartData right_arm = body.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-6.0F, -4.0F, 0.0F));

        ModelPartData cube_r2 = right_arm.addChild("cube_r2", ModelPartBuilder.create().uv(61, 32).cuboid(-4.0F, 13.0F, -5.0F, 5.0F, 12.0F, 6.0F, new Dilation(0.35F)), ModelTransform.of(-0.25F, -5.0F, 1.0F, 0.0F, 0.0F, 0.0436F));

        ModelPartData cube_r3 = right_arm.addChild("cube_r3", ModelPartBuilder.create().uv(61, 32).mirrored().cuboid(-4.0F, 13.0F, -5.0F, 5.0F, 12.0F, 6.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.of(-0.25F, -8.0F, 1.0F, 0.0F, 0.0F, 0.0436F));

        ModelPartData cube_r4 = right_arm.addChild("cube_r4", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-4.0F, -5.0F, -2.0F, 5.0F, 12.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-6.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0436F));

        ModelPartData cube_r5 = right_arm.addChild("cube_r5", ModelPartBuilder.create().uv(84, 24).mirrored().cuboid(-4.0F, -5.0F, -5.0F, 5.0F, 30.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0436F));

        ModelPartData left_arm = body.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.pivot(6.0F, -4.0F, 0.0F));

        ModelPartData cube_r6 = left_arm.addChild("cube_r6", ModelPartBuilder.create().uv(40, 32).cuboid(-1.0F, -5.0F, -2.0F, 5.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(6.5F, 13.0F, 1.0F, 0.0F, 0.0F, -0.0436F));

        ModelPartData cube_r7 = left_arm.addChild("cube_r7", ModelPartBuilder.create().uv(61, 32).mirrored().cuboid(-1.0F, 13.0F, -5.0F, 5.0F, 12.0F, 6.0F, new Dilation(0.35F)).mirrored(false), ModelTransform.of(0.25F, -15.0F, 1.0F, 0.0F, 0.0F, -0.0436F));

        ModelPartData cube_r8 = left_arm.addChild("cube_r8", ModelPartBuilder.create().uv(61, 32).cuboid(-1.0F, 13.0F, -5.0F, 5.0F, 12.0F, 6.0F, new Dilation(0.25F)), ModelTransform.of(0.25F, -18.0F, 1.0F, 0.0F, 0.0F, -0.0436F));

        ModelPartData cube_r9 = left_arm.addChild("cube_r9", ModelPartBuilder.create().uv(106, 24).cuboid(-1.0F, -5.0F, -5.0F, 5.0F, 30.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.0436F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(40, 0).cuboid(-5.0F, -24.0F, -3.0F, 10.0F, 24.0F, 8.0F, new Dilation(0.001F))
                .uv(40, 0).mirrored().cuboid(-5.0F, -24.0F, -3.0F, 10.0F, 9.0F, 8.0F, new Dilation(0.015F)).mirrored(false), ModelTransform.pivot(0.0F, -8.0F, -2.0F));

        ModelPartData cube_r10 = head.addChild("cube_r10", ModelPartBuilder.create().uv(40, 44).cuboid(-3.0F, -4.0F, -3.0F, 4.0F, 12.0F, 8.0F, new Dilation(0.1F))
                .uv(76, 0).cuboid(-3.0F, -4.0F, -3.0F, 4.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-5.5F, -17.0F, 0.0F, 0.0F, -0.2618F, 0.0F));

        ModelPartData cube_r11 = head.addChild("cube_r11", ModelPartBuilder.create().uv(76, 0).mirrored().cuboid(-1.0F, -4.0F, -4.0F, 4.0F, 8.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(5.5F, -17.0F, 1.0F, 0.0F, 0.2618F, 0.0F));

        ModelPartData right_leg = root.addChild("right_leg", ModelPartBuilder.create().uv(106, 0).cuboid(-3.0F, -2.0F, -3.0F, 5.0F, 10.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, 12.0F, 0.0F));

        ModelPartData right_feet = right_leg.addChild("right_feet", ModelPartBuilder.create().uv(106, 0).cuboid(-2.0F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, 8.0F, 0.0F));

        ModelPartData left_leg = root.addChild("left_leg", ModelPartBuilder.create().uv(106, 0).mirrored().cuboid(-2.0F, -2.0F, -3.0F, 5.0F, 10.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(3.0F, 12.0F, 0.0F));

        ModelPartData left_feet = left_leg.addChild("left_feet", ModelPartBuilder.create().uv(106, 0).mirrored().cuboid(-3.0F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(1.0F, 8.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 64);
    }

    @Override
    public void setAngles(EldritchWatcherEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.animateMovement(EldritchWatcherAnimations.WALK, limbSwing, limbSwingAmount, 2, 2);
        if (!entity.isDead())
            this.body.roll = (float) Oscillator.getOscillatingWithNegativeValue(ClientTicks.getTicks()) * 0.35f;
        this.updateAnimation(entity.attack, EldritchWatcherAnimations.ATTACK, ageInTicks);
        if (ConfigEntries.doAdvancedDeathForMobs)
            this.updateAnimation(entity.death, EldritchWatcherAnimations.DEATH, ageInTicks);
        if (entity.isCarringBlock()) {
            this.right_arm.pitch = -0.5F;
            this.right_arm.roll = 0.05F;
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}