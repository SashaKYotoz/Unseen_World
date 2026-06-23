package net.sashakyotoz.common.entities.animations;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class DreadflapAnimations {
    public static final Animation FLYING = Animation.Builder.create(0.75F).looping()
            .addBoneAnimation("body", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(12.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("body", new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("head", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("main_body", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("left_wing", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 20.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("left_s_b", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, -30.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(15.0F, 15.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("right_s_b", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 30.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(15.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, -5.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("right_wing", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 20.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .build();

    public static final Animation ROOSTING = Animation.Builder.create(0.0833F).looping()
            .addBoneAnimation("body", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 180.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("main_body", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("left_s_b", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -12.5F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("right_s_b", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 12.5F), Transformation.Interpolations.LINEAR)
            ))
            .build();
}