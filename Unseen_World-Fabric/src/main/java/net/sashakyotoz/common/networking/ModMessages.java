package net.sashakyotoz.common.networking;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.sashakyotoz.UnseenWorld;

public final class ModMessages {
    public static final ResourceLocation WEATHER_PACKET_ID = UnseenWorld.MOD.makeID("weather_sync");

    private ModMessages() {
    }

    public static void sendWeather(ServerPlayer player, boolean active, String direction) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(active);
        buf.writeUtf(direction);
        ServerPlayNetworking.send(player, WEATHER_PACKET_ID, buf);
    }
}