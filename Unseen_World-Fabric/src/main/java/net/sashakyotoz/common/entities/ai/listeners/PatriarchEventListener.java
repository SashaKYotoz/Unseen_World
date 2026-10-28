package net.sashakyotoz.common.entities.ai.listeners;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.bosses.AuricPatriarch;
import net.sashakyotoz.common.tags.ModTags;

public record PatriarchEventListener(AuricPatriarch patriarch,
                                     PositionSource positionSource) implements GameEventListener {

    @Override
    public PositionSource getListenerSource() {
        return this.positionSource;
    }

    @Override
    public int getListenerRadius() {
        return 32;
    }

    public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 pos) {
        Entity entity = context.sourceEntity();

        if (!(entity instanceof AuricPatriarch) && entity instanceof LivingEntity target) {
            AuricPatriarch.PatriarchState currentState = this.patriarch.getPatriarchState();
            if (currentState == AuricPatriarch.PatriarchState.STANDING
                    && !this.patriarch.isFlying()) {
                if (event.is(ModTags.GameEvents.ENTITY_SHOOT)) {
                    this.patriarch.setPatriarchState(AuricPatriarch.PatriarchState.WING_KNOCKBACK);
                    this.patriarch.lookAt(EntityAnchorArgument.Anchor.EYES, pos.add(0, 1, 0));
                    return true;
                }
                if (event.is(ModTags.GameEvents.ENTITY_MOVE) && this.patriarch.distanceToSqr(pos) >= 121) {

                    Vec3 motion = target.getDeltaMovement();
                    if (motion.lengthSqr() > 1.0E-4D) {
                        Vec3 targetMotionDir = motion.normalize();
                        Vec3 toPatriarchDir = this.patriarch.position().subtract(pos).normalize();

                        double dotProduct = targetMotionDir.dot(toPatriarchDir);

                        this.patriarch.setTarget(target);
                        this.patriarch.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());

                        if (dotProduct < 0) {
                            this.patriarch.setPatriarchState(AuricPatriarch.PatriarchState.GRAB_AND_STRIKE);
                            return true;
                        } else if (dotProduct > 0) {
                            this.patriarch.setPatriarchState(AuricPatriarch.PatriarchState.SUDDEN_BEAM);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}