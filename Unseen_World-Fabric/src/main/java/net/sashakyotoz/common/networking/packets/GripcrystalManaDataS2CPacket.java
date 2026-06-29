package net.sashakyotoz.common.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;

public class GripcrystalManaDataS2CPacket {
    public static void receive(Minecraft client, ClientPacketListener handler,
                               FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            ((IEntityDataSaver) client.player).getPersistentData().putInt("gripcrystal_mana", buf.readInt());
            ((IEntityDataSaver) client.player).getPersistentData().putFloat("ability_opacity", buf.readFloat());
        }
    }
}
