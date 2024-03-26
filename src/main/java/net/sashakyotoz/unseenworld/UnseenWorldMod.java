package net.sashakyotoz.unseenworld;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sashakyotoz.unseenworld.client.gui.GoldenChestGUIScreen;
import net.sashakyotoz.unseenworld.client.renderer.BeaconOfWeaponsRenderer;
import net.sashakyotoz.unseenworld.client.renderer.layers.KnightArmorRodsLayer;
import net.sashakyotoz.unseenworld.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod("unseen_world")
public class UnseenWorldMod {
    public static final Logger LOGGER = LogManager.getLogger(UnseenWorldMod.class);
    public static final String MODID = "unseen_world";

    public UnseenWorldMod() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        UnseenWorldModSounds.REGISTRY.register(bus);
        UnseenWorldModBlocks.REGISTRY.register(bus);
        UnseenWorldModBlockEntities.REGISTRY.register(bus);
        UnseenWorldModItems.REGISTRY.register(bus);
        UnseenWorldModEntities.REGISTRY.register(bus);
        UnseenWorldModEnchantments.REGISTRY.register(bus);
        UnseenWorldModTabs.REGISTRY.register(bus);
        UnseenWorldModFeatures.REGISTRY.register(bus);
        UnseenWorldModMobEffects.REGISTRY.register(bus);
        UnseenWorldModPotions.REGISTRY.register(bus);
        UnseenWorldModPaintings.REGISTRY.register(bus);
        UnseenWorldModParticleTypes.REGISTRY.register(bus);
        UnseenWorldModVillagerProfessions.PROFESSIONS.register(bus);
        UnseenWorldModMenus.REGISTRY.register(bus);
        UnseenWorldModFluids.REGISTRY.register(bus);
        UnseenWorldModFluidTypes.REGISTRY.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UnseenWorldModConfigs.SPEC);
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(this::registerLayer);
            bus.addListener(this::commonSetup);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void registerLayer(EntityRenderersEvent event) {
        if (event instanceof EntityRenderersEvent.AddLayers addLayersEvent) {
            EntityModelSet entityModels = addLayersEvent.getEntityModels();
            addLayersEvent.getSkins().forEach((s) -> {
                LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> livingEntityRenderer = addLayersEvent.getSkin(s);
                if (livingEntityRenderer instanceof PlayerRenderer playerRenderer) {
                    playerRenderer.addLayer(new KnightArmorRodsLayer<>(playerRenderer, entityModels));
                }
            });
        }
    }

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void commonSetup(final FMLCommonSetupEvent event) {
        UnseenWorldModItemProperties.addCustomItemProperties();
        event.enqueueWork(() -> MenuScreens.register(UnseenWorldModMenus.GOLDEN_CHEST_GUI.get(), GoldenChestGUIScreen::new));
        BlockEntityRenderers.register(UnseenWorldModBlockEntities.BEACON_OF_WEAPONS.get(), BeaconOfWeaponsRenderer::new);
        ComposterBlock.COMPOSTABLES.put(UnseenWorldModBlocks.SMALL_CRIMSERRY_SOUL_BERRY.get().asItem(), 0.2f);
        ComposterBlock.COMPOSTABLES.put(UnseenWorldModBlocks.MISTERYFLOWER_SAPLING.get().asItem(), 0.2f);
        ComposterBlock.COMPOSTABLES.put(UnseenWorldModItems.CRIMSERRY_SOUL_BERRY_FOOD.get().asItem(), 0.2f);
        ComposterBlock.COMPOSTABLES.put(UnseenWorldModItems.BERRIESFROM_BLOOMING_VINE.get().asItem(), 0.2f);
    }
}
