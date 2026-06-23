package net.sashakyotoz.common.entities.animations;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class VexalBeetleAnimations {
    public static final Animation FLYING = Animation.Builder.create(0.75F).looping()
            .addBoneAnimation("head", new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("body", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("right_leg_b", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 15.0F, -10.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("left_leg_b", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, -15.0F, 10.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("right_leg_f", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 10.0F, -10.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("left_leg_f", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, -10.0F, 10.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("left_wing", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 7.5F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("right_wing", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -7.5F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("wing", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 15.0F, 10.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, -2.5F, -10.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("wing1", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, -15.0F, -10.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, -2.5F, 10.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .build();
}