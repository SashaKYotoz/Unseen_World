package net.sashakyotoz.common.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;

public class GripcrystalManaDataS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        if (client.player != null)
            ((IEntityDataSaver) client.player).getPersistentData().putInt("gripcrystal_mana", buf.readInt());
    }
}
