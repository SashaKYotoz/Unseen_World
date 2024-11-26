package net.sashakyotoz.common.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.networking.packets.GripcrystalManaDataS2CPacket;
import net.sashakyotoz.common.networking.packets.GripcrystalWeaponsC2SPacket;
import net.sashakyotoz.common.networking.packets.GrippingDataS2CPacket;
import net.sashakyotoz.common.networking.packets.LeftClickC2SPacket;

public class ModMessages {
    public static final Identifier GRIPCRYSTAL_ABILITY_HANDLER = UnseenWorld.makeID("gripcrystal_weapons_handler");
    public static final Identifier LEFT_CLICK_HANDLER = UnseenWorld.makeID("left_click_handler");

    public static final Identifier GRIPCRYSTAL_MANA_HANDLER = UnseenWorld.makeID("gripcrystal_mana_handler");
    public static final Identifier GRIPPING_HANDLER = UnseenWorld.makeID("gripping_handler");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(GRIPCRYSTAL_ABILITY_HANDLER, GripcrystalWeaponsC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(LEFT_CLICK_HANDLER, LeftClickC2SPacket::receive);
    }
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(GRIPCRYSTAL_MANA_HANDLER, GripcrystalManaDataS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(GRIPPING_HANDLER, GrippingDataS2CPacket::receive);
    }
}