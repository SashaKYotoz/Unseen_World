package net.sashakyotoz.unseenworld.network.keys;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.unseenworld.network.BlazerHelmetAbilityMessage;
import net.sashakyotoz.unseenworld.network.MiningBootsAbilityMessage;
import net.sashakyotoz.unseenworld.network.ModNetwork;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.KeyMapping;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ModKeyMappings {
    public static final KeyMapping BLAZER_HELMET_ABILITY = new KeyMapping("key.unseen_world.blazer_helmet_ability", GLFW.GLFW_KEY_UP, "key.categories.abilities") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                BLAZER_HELMET_ABILITY_LASTPRESS = System.currentTimeMillis();
            } else if (isDownOld != isDown) {
                int dt = (int) (System.currentTimeMillis() - BLAZER_HELMET_ABILITY_LASTPRESS);
                ModNetwork.PACKET_HANDLER.sendToServer(new BlazerHelmetAbilityMessage(0, dt));
                BlazerHelmetAbilityMessage.pressAction(Minecraft.getInstance().player, 0,dt);
            }
            isDownOld = isDown;
        }
    };
    public static final KeyMapping MINING_BOOTS_ABILITY = new KeyMapping("key.unseen_world.mining_boots_ability", GLFW.GLFW_KEY_DOWN, "key.categories.abilities") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ModNetwork.PACKET_HANDLER.sendToServer(new MiningBootsAbilityMessage(0, 0));
                MiningBootsAbilityMessage.pressAction(Minecraft.getInstance().player, 0,0);
            }
            isDownOld = isDown;
        }
    };
    private static long BLAZER_HELMET_ABILITY_LASTPRESS = 0;

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (Minecraft.getInstance().screen == null) {
                BLAZER_HELMET_ABILITY.consumeClick();
                MINING_BOOTS_ABILITY.consumeClick();
            }
        }
    }
}