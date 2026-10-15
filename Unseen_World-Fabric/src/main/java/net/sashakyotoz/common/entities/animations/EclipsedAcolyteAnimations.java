package net.sashakyotoz.common.entities.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public final class EclipsedAcolyteAnimations {
    public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(3.0F).looping()
            .addAnimation("waist", rot(
                    k(0.0F, 0.0F, 0.0F, 0.0F),
                    k(1.5F, 2.0F, 0.0F, 1.5F),
                    k(3.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("head", rot(
                    k(0.0F, 0.0F, 0.0F, 0.0F),
                    k(1.0F, 3.0F, 0.0F, -2.0F),
                    k(2.0F, -2.0F, 0.0F, 2.0F),
                    k(3.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("left_arm", rot(
                    k(0.0F, 0.0F, 0.0F, -3.0F),
                    k(1.5F, 4.0F, 0.0F, -6.0F),
                    k(3.0F, 0.0F, 0.0F, -3.0F)))
            .addAnimation("right_arm", rot(
                    k(0.0F, 0.0F, 0.0F, 3.0F),
                    k(1.5F, -3.0F, 0.0F, 5.0F),
                    k(3.0F, 0.0F, 0.0F, 3.0F)))
            .build();

    public static final AnimationDefinition WALK = AnimationDefinition.Builder.withLength(1.2F).looping()
            .addAnimation("left_leg", rot(
                    k(0.0F, -24.0F, 0.0F, 0.0F),
                    k(0.6F, 24.0F, 0.0F, 0.0F),
                    k(1.2F, -24.0F, 0.0F, 0.0F)))
            .addAnimation("right_leg", rot(
                    k(0.0F, 24.0F, 0.0F, 0.0F),
                    k(0.6F, -24.0F, 0.0F, 0.0F),
                    k(1.2F, 24.0F, 0.0F, 0.0F)))
            .addAnimation("right_arm", rot(
                    k(0.0F, -20.0F, 0.0F, 4.0F),
                    k(0.6F, 20.0F, 0.0F, 4.0F),
                    k(1.2F, -20.0F, 0.0F, 4.0F)))
            .addAnimation("left_arm", rot(
                    k(0.0F, 12.0F, 0.0F, -6.0F),
                    k(0.6F, -4.0F, 0.0F, -4.0F),
                    k(1.2F, 12.0F, 0.0F, -6.0F)))
            .addAnimation("waist", rot(
                    k(0.0F, 6.0F, -4.0F, 3.0F),
                    k(0.6F, 6.0F, 4.0F, -3.0F),
                    k(1.2F, 6.0F, -4.0F, 3.0F)))
            .addAnimation("waist", pos(
                    p(0.0F, 0.0F),
                    p(0.3F, -0.6F),
                    p(0.6F, 0.0F),
                    p(0.9F, -0.6F),
                    p(1.2F, 0.0F)))
            .build();

    public static final AnimationDefinition DORMANT = AnimationDefinition.Builder.withLength(2.0F).looping()
            .addAnimation("head", rot(
                    k(0.0F, 28.0F, 0.0F, 0.0F),
                    k(2.0F, 28.0F, 0.0F, 0.0F)))
            .addAnimation("waist", rot(
                    k(0.0F, 14.0F, 0.0F, 0.0F),
                    k(2.0F, 14.0F, 0.0F, 0.0F)))
            .addAnimation("waist", pos(
                    p(0.0F, -1.0F),
                    p(2.0F, -1.0F)))
            .addAnimation("left_arm", rot(
                    k(0.0F, -10.0F, 0.0F, 6.0F),
                    k(2.0F, -10.0F, 0.0F, 6.0F)))
            .addAnimation("right_arm", rot(
                    k(0.0F, -8.0F, 0.0F, -6.0F),
                    k(2.0F, -8.0F, 0.0F, -6.0F)))
            .build();

    public static final AnimationDefinition AWAKEN = AnimationDefinition.Builder.withLength(1.5F)
            .addAnimation("head", rot(
                    k(0.0F, 28.0F, 0.0F, 0.0F),
                    k(0.5F, 10.0F, 0.0F, 0.0F),
                    k(0.8F, -18.0F, 0.0F, 0.0F),
                    k(1.5F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("waist", rot(
                    k(0.0F, 14.0F, 0.0F, 0.0F),
                    k(0.8F, -10.0F, 0.0F, 0.0F),
                    k(1.5F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("waist", pos(
                    p(0.0F, -1.0F),
                    p(0.8F, 0.5F),
                    p(1.5F, 0.0F)))
            .addAnimation("left_arm", rot(
                    k(0.0F, -10.0F, 0.0F, 6.0F),
                    k(0.8F, -20.0F, 0.0F, -40.0F),
                    k(1.5F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("right_arm", rot(
                    k(0.0F, -8.0F, 0.0F, -6.0F),
                    k(0.8F, -20.0F, 0.0F, 40.0F),
                    k(1.5F, 0.0F, 0.0F, 0.0F)))
            .build();

    public static final AnimationDefinition SLAM = AnimationDefinition.Builder.withLength(1.0F)
            .addAnimation("left_arm", rot(
                    k(0.0F, 0.0F, 0.0F, 0.0F),
                    k(0.35F, -165.0F, 0.0F, -10.0F),
                    k(0.5F, -30.0F, 0.0F, 0.0F),
                    k(0.75F, -25.0F, 0.0F, 0.0F),
                    k(1.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("right_arm", rot(
                    k(0.0F, 0.0F, 0.0F, 0.0F),
                    k(0.35F, 20.0F, 0.0F, 15.0F),
                    k(0.5F, -10.0F, 0.0F, 5.0F),
                    k(1.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("waist", rot(
                    k(0.0F, 0.0F, 0.0F, 0.0F),
                    k(0.35F, -12.0F, -20.0F, 0.0F),
                    k(0.5F, 24.0F, 5.0F, 0.0F),
                    k(0.75F, 22.0F, 5.0F, 0.0F),
                    k(1.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("head", rot(
                    k(0.0F, 0.0F, 0.0F, 0.0F),
                    k(0.35F, -10.0F, 0.0F, 0.0F),
                    k(0.5F, -14.0F, 0.0F, 0.0F),
                    k(1.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("waist", pos(
                    p(0.0F, 0.0F),
                    p(0.35F, 0.6F),
                    p(0.5F, -1.5F),
                    p(0.75F, -1.5F),
                    p(1.0F, 0.0F)))
            .build();

    public static final AnimationDefinition SWEEP = AnimationDefinition.Builder.withLength(0.75F)
            .addAnimation("right_arm", rot(
                    k(0.0F, 0.0F, 0.0F, 0.0F),
                    k(0.25F, -80.0F, 50.0F, 30.0F),
                    k(0.4F, -80.0F, -60.0F, 30.0F),
                    k(0.75F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("waist", rot(
                    k(0.0F, 0.0F, 0.0F, 0.0F),
                    k(0.25F, 0.0F, -35.0F, 0.0F),
                    k(0.4F, 4.0F, 35.0F, 0.0F),
                    k(0.75F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("left_arm", rot(
                    k(0.0F, 0.0F, 0.0F, 0.0F),
                    k(0.25F, 15.0F, 0.0F, -10.0F),
                    k(0.4F, -15.0F, 0.0F, -15.0F),
                    k(0.75F, 0.0F, 0.0F, 0.0F)))
            .build();

    private EclipsedAcolyteAnimations() {
    }

    private static AnimationChannel rot(Keyframe... keyframes) {
        return new AnimationChannel(AnimationChannel.Targets.ROTATION, keyframes);
    }

    private static AnimationChannel pos(Keyframe... keyframes) {
        return new AnimationChannel(AnimationChannel.Targets.POSITION, keyframes);
    }

    private static Keyframe k(float time, float x, float y, float z) {
        return new Keyframe(time, KeyframeAnimations.degreeVec(x, y, z), AnimationChannel.Interpolations.CATMULLROM);
    }

    private static Keyframe p(float time, float y) {
        return new Keyframe(time, KeyframeAnimations.posVec(0.0F, y, 0.0F), AnimationChannel.Interpolations.CATMULLROM);
    }
}
