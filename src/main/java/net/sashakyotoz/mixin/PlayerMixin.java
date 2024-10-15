package net.sashakyotoz.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sashakyotoz.utils.JsonWorldController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void onSetUp(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
        if (world instanceof ServerWorld serverWorld)
            JsonWorldController.data.put(0, JsonWorldController.loadData(serverWorld));
    }
}