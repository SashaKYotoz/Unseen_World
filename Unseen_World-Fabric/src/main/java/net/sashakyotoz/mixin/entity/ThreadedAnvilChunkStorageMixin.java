package net.sashakyotoz.mixin.entity;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.entity.Entity;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
public abstract class ThreadedAnvilChunkStorageMixin {
    @Inject(method = "addEntity(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void cancelEntityPartLoad(Entity entity, CallbackInfo ci) {
        if (entity instanceof EntityPart) ci.cancel();
    }
}
