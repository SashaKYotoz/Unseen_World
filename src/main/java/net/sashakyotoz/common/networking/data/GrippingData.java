package net.sashakyotoz.common.networking.data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.sashakyotoz.common.networking.ModMessages;
import net.sashakyotoz.utils.IEntityDataSaver;

public class GrippingData {
    public static int addGrippingSeconds(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int seconds = nbt.getInt("gripping");
        seconds += amount;
        nbt.putInt("gripping", seconds);
        syncGripping(seconds, (ServerPlayerEntity) player);
        return seconds;
    }

    public static int getGrippingTime(IEntityDataSaver entityDataSaver) {
        return entityDataSaver.getPersistentData().getInt("gripping");
    }

    public static int removeGrippingPerTick(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        int gripping = nbt.getInt("gripping");
        gripping = gripping - 1;
        nbt.putInt("gripping", gripping);
        syncGripping(gripping, (ServerPlayerEntity) player);
        return gripping;
    }

    public static int removeGripping(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putInt("gripping", 0);
        syncGripping(0, (ServerPlayerEntity) player);
        return 0;
    }

    public static void syncGripping(int seconds, ServerPlayerEntity player) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(seconds);
        ServerPlayNetworking.send(player, ModMessages.GRIPPING_HANDLER, buffer);
    }
}