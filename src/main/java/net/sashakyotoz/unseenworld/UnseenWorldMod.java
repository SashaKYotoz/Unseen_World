package net.sashakyotoz.unseenworld;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.ItemStack;
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
        UnseenWorldModSounds.REGISTRY.register(bus);
        UnseenWorldModBlocks.BLOCKS.register(bus);
        UnseenWorldModBlockEntities.REGISTRY.register(bus);
        UnseenWorldModItems.ITEMS.register(bus);
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
        ComposterBlock.COMPOSTABLES.put(UnseenWorldModBlocks.CRIMSERRY_SOUL_CROP.get(), 0.1f);
        ComposterBlock.COMPOSTABLES.put(UnseenWorldModBlocks.MISTERY_CROP_FLOWER.get(), 0.1f);
        ComposterBlock.COMPOSTABLES.put(UnseenWorldModItems.CRIMSERRY_SOUL_BERRY.get(), 0.2f);
        ComposterBlock.COMPOSTABLES.put(UnseenWorldModItems.BERRIES_OF_BLOOMING_VINE.get(), 0.2f);
        PotionBrewing.addMix(Potions.AWKWARD,UnseenWorldModItems.TEALIVE_STONY_SHARD.get(),Potions.LONG_NIGHT_VISION);
        PotionBrewing.addMix(Potions.AWKWARD,UnseenWorldModItems.DARK_FREE_SOUL.get(),UnseenWorldModPotions.DARK_IMMUNITE_POTION.get());
    }
    private void clientSetup(final FMLClientSetupEvent event){
        ModItemProperties.addCustomItemProperties();
        event.enqueueWork(() -> MenuScreens.register(UnseenWorldModMenus.GOLDEN_CHEST_GUI.get(), GoldenChestGUIScreen::new));
    }
}
