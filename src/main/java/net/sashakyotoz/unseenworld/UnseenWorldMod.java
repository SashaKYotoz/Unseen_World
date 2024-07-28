package net.sashakyotoz.unseenworld;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import net.sashakyotoz.unseenworld.client.gui.GoldenChestGUIScreen;
import net.sashakyotoz.unseenworld.registries.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod("unseen_world")
public class UnseenWorldMod {
    public static final Logger LOGGER = LogManager.getLogger(UnseenWorldMod.class);
    public static final String MODID = "unseen_world";

    public UnseenWorldMod() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        UnseenWorldSounds.REGISTRY.register(bus);
        UnseenWorldBlocks.BLOCKS.register(bus);
        UnseenWorldBlockEntities.REGISTRY.register(bus);
        UnseenWorldItems.ITEMS.register(bus);
        UnseenWorldEntities.REGISTRY.register(bus);
        UnseenWorldEnchantments.REGISTRY.register(bus);
        UnseenWorldTabs.REGISTRY.register(bus);
        UnseenWorldFeatures.REGISTRY.register(bus);
        UnseenWorldMobEffects.REGISTRY.register(bus);
        UnseenWorldPotions.REGISTRY.register(bus);
        UnseenWorldPaintings.REGISTRY.register(bus);
        UnseenWorldParticleTypes.REGISTRY.register(bus);
        UnseenWorldVillagerProfessions.PROFESSIONS.register(bus);
        UnseenWorldMenus.REGISTRY.register(bus);
        UnseenWorldFluids.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UnseenWorldConfigs.SPEC);
        TextureAnimator.addEntityToAnimate(UnseenWorldMod.class,MODID,"particle/portal_like","void_portal");
        TextureAnimator.addEntityToAnimate(UnseenWorldMod.class,MODID,"particle/fireball","fireball_particle");
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
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
    private void commonSetup(FMLCommonSetupEvent event) {
        ComposterBlock.COMPOSTABLES.put(UnseenWorldBlocks.CRIMSERRY_SOUL_CROP.get(), 0.1f);
        ComposterBlock.COMPOSTABLES.put(UnseenWorldBlocks.MISTERY_CROP_FLOWER.get(), 0.1f);
        ComposterBlock.COMPOSTABLES.put(UnseenWorldItems.CRIMSERRY_SOUL_BERRY.get(), 0.2f);
        ComposterBlock.COMPOSTABLES.put(UnseenWorldItems.BERRIES_OF_BLOOMING_VINE.get(), 0.2f);
        PotionBrewing.addMix(Potions.AWKWARD, UnseenWorldItems.TEALIVE_STONY_SHARD.get(),Potions.LONG_NIGHT_VISION);
        PotionBrewing.addMix(Potions.AWKWARD, UnseenWorldItems.DARK_FREE_SOUL.get(), UnseenWorldPotions.DARK_IMMUNITE_POTION.get());
    }
    private void clientSetup(final FMLClientSetupEvent event){
        ModItemProperties.addCustomItemProperties();
        event.enqueueWork(() -> MenuScreens.register(UnseenWorldMenus.GOLDEN_CHEST_GUI.get(), GoldenChestGUIScreen::new));
    }
}
