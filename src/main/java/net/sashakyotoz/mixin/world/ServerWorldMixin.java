package net.sashakyotoz.mixin.world;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.sashakyotoz.api.multipart_entity.WorldMultipartHelper;
import net.sashakyotoz.common.entities.spawners.ViolegersSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements WorldMultipartHelper {
    @Inject(method = "getDragonPart", at = @At("RETURN"), cancellable = true)
    public void getEntityParts(int id, CallbackInfoReturnable<Entity> cir) {
        Entity entity = cir.getReturnValue();
        if (entity == null) cir.setReturnValue(getPMEPartMap().get(id));
    }

    @Shadow
    public abstract ServerWorld toServerWorld();

    @Unique
    private final ViolegersSpawner guardiansSpawner = new ViolegersSpawner();

    @Inject(method = "tickSpawners", at = @At("HEAD"))
    public void tick(boolean spawnMonsters, boolean spawnAnimals, CallbackInfo ci) {
        guardiansSpawner.spawn(this.toServerWorld(), spawnMonsters, spawnAnimals);
    }
}