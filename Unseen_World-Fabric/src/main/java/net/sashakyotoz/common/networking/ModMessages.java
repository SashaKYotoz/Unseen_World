package net.sashakyotoz.common.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.weather.Grippfall;
import net.sashakyotoz.common.networking.packets.AbilityClickC2SPacket;
import net.sashakyotoz.common.networking.packets.GripcrystalManaDataS2CPacket;
import net.sashakyotoz.common.networking.packets.GripcrystalWeaponsC2SPacket;

public class ModMessages {
    public static final Identifier GRIPCRYSTAL_ABILITY_HANDLER = UnseenWorld.makeID("gripcrystal_weapons_handler");
    public static final Identifier ABILITY_CLICK_HANDLER = UnseenWorld.makeID("left_click_handler");

    public static final Identifier GRIPCRYSTAL_MANA_HANDLER = UnseenWorld.makeID("gripcrystal_mana_handler");
    public static final Identifier WEATHER_PACKET_ID = UnseenWorld.makeID("weather_sync");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(GRIPCRYSTAL_ABILITY_HANDLER, GripcrystalWeaponsC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_CLICK_HANDLER, AbilityClickC2SPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(GRIPCRYSTAL_MANA_HANDLER, GripcrystalManaDataS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.WEATHER_PACKET_ID, (client, handler, buf, responseSender) -> {
            boolean active = buf.readBoolean();
            String direction = buf.readString();
            client.execute(() -> {
                Grippfall.isGrippfallActive = active;
                Grippfall.direction = direction;
            });
        });
    }
}