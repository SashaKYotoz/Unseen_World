package net.sashakyotoz.common.entities.ai.listeners;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;
import net.sashakyotoz.client.particles.custom.effects.WindVibrationParticleEffect;
import net.sashakyotoz.common.entities.custom.EldritchWatcherEntity;
import net.sashakyotoz.common.tags.ModTags;

public class EldritchEventListener implements GameEventListener {
    private final EldritchWatcherEntity watcher;

    public EldritchEventListener(EldritchWatcherEntity watcher) {
        this.watcher = watcher;
    }

    @Override
    public PositionSource getPositionSource() {
        return new EntityPositionSource(watcher, watcher.getStandingEyeHeight());
    }

    @Override
    public int getRange() {
        return 20;
    }

    @Override
    public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos) {
        if (event.isIn(ModTags.GameEvents.ELDRITCH_CAN_LISTEN) && !(emitter.sourceEntity() instanceof EldritchWatcherEntity)
                && (emitter.sourceEntity() != null && !emitter.sourceEntity().isSneaking()) && this.watcher.getNavigation().isIdle()) {
            world.spawnParticles(new WindVibrationParticleEffect(new EntityPositionSource(watcher, watcher.getStandingEyeHeight()), 15),
                    emitterPos.getX(), emitterPos.getY(), emitterPos.getZ(), 2, 0, 0, 0, 1);
            watcher.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, emitterPos);
            watcher.getNavigation().startMovingTo(emitterPos.getX(), emitterPos.getY(), emitterPos.getZ(), 1.5);
            return true;
        }
        return false;
    }
}