package net.sashakyotoz.common.entities.ai.listeners;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.client.particles.custom.effects.WindVibrationParticleEffect;
import net.sashakyotoz.common.entities.custom.EldritchWatcherEntity;
import net.sashakyotoz.common.tags.ModTags;

public class EldritchEventListener implements GameEventListener {
    private final EldritchWatcherEntity watcher;

    public EldritchEventListener(EldritchWatcherEntity watcher) {
        this.watcher = watcher;
    }

    @Override
    public PositionSource getListenerSource() {
        return new EntityPositionSource(watcher, watcher.getEyeHeight());
    }

    @Override
    public int getListenerRadius() {
        return 20;
    }

    @Override
    public boolean handleGameEvent(ServerLevel world, GameEvent event, GameEvent.Context emitter, Vec3 emitterPos) {
        if (event.is(ModTags.GameEvents.ELDRITCH_CAN_LISTEN) && !(emitter.sourceEntity() instanceof EldritchWatcherEntity)
                && (emitter.sourceEntity() != null && !emitter.sourceEntity().isShiftKeyDown()) && this.watcher.getNavigation().isDone()) {
            world.sendParticles(new WindVibrationParticleEffect(new EntityPositionSource(watcher, watcher.getEyeHeight()), 15),
                    emitterPos.x(), emitterPos.y(), emitterPos.z(), 2, 0, 0, 0, 1);
            watcher.lookAt(EntityAnchorArgument.Anchor.EYES, emitterPos);
            watcher.getNavigation().moveTo(emitterPos.x(), emitterPos.y(), emitterPos.z(), 1.5);
            return true;
        }
        return false;
    }
}