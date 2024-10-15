package net.sashakyotoz.mixin;

import net.minecraft.server.world.ServerWorld;
import net.sashakyotoz.common.entities.spawners.DarkGuardiansSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow
    public abstract ServerWorld toServerWorld();

    @Unique
    private final DarkGuardiansSpawner guardiansSpawner = new DarkGuardiansSpawner();

    @Inject(method = "tickSpawners", at = @At("HEAD"))
    public void tick(boolean spawnMonsters, boolean spawnAnimals, CallbackInfo ci) {
        guardiansSpawner.spawn(this.toServerWorld(), spawnMonsters, spawnAnimals);
    }
}