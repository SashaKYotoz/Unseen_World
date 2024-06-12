package net.sashakyotoz.unseenworld.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.sashakyotoz.anitexlib.AniTexLib;
import net.sashakyotoz.unseenworld.managers.MiningBootsProcedure;

import java.util.Objects;
import java.util.function.Supplier;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MiningBootsAbilityMessage {
    int type, pressedms;

    public MiningBootsAbilityMessage(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public MiningBootsAbilityMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void buffer(MiningBootsAbilityMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(MiningBootsAbilityMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(Objects.requireNonNull(context.getSender()), message.type,message.pressedms));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type, int pressedms) {
        if (type == 0)
            MiningBootsProcedure.execute(player);
    }
    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        ModNetwork.addNetworkMessage(MiningBootsAbilityMessage.class, MiningBootsAbilityMessage::buffer, MiningBootsAbilityMessage::new, MiningBootsAbilityMessage::handler);
    }
}
