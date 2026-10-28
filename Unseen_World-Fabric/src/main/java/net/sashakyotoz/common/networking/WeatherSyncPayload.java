package net.sashakyotoz.common.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.sashakyotoz.UnseenWorld;

public record WeatherSyncPayload(boolean active, String direction) implements CustomPacketPayload {
    public static final Type<WeatherSyncPayload> TYPE = new Type<>(UnseenWorld.MOD.makeID("weather_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, WeatherSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, WeatherSyncPayload::active,
            ByteBufCodecs.STRING_UTF8, WeatherSyncPayload::direction,
            WeatherSyncPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
