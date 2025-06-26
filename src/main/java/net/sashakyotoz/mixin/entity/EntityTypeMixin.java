package net.sashakyotoz.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityType.class)
public class EntityTypeMixin {
    @Inject(method = "getEntityFromNbt", at = @At("HEAD"))
    private static void onGetEntityFromNbt(NbtCompound nbt, World world, CallbackInfoReturnable<Optional<Entity>> cir) {
        if ("unseen_world:dark_guardian".equals(nbt.getString("id"))) {
            nbt.putString("id", "unseen_world:violeger");
        }
    }
}