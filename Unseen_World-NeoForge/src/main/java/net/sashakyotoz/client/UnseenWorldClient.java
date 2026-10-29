package net.sashakyotoz.client;

import net.lcc.sollib.api.client.SolClientRegistries;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BrushableBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.ChimericDarknessEffects;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.client.gui.FullScreenOverlay;
import net.sashakyotoz.client.gui.PhasedBossBar;
import net.sashakyotoz.client.gui.blocks.ModScreenHandlers;
import net.sashakyotoz.client.models.*;
import net.sashakyotoz.client.networking.ModClientMessages;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.client.particles.custom.*;
import net.sashakyotoz.client.renderers.*;
import net.sashakyotoz.client.renderers.blocks.EffigyRenderer;
import net.sashakyotoz.client.renderers.blocks.ModChestRenderer;
import net.sashakyotoz.client.renderers.blocks.OrdealSpawnerRenderer;
import net.sashakyotoz.client.renderers.bosses.AuricPatriarchRenderer;
import net.sashakyotoz.client.renderers.bosses.EclipseSentinelRenderer;
import net.sashakyotoz.client.renderers.bosses.WarriorOfChimericDarknessRenderer;
import net.sashakyotoz.client.renderers.fx.BossFxRenderer;
import net.sashakyotoz.client.renderers.layers.DarkeningLayer;
import net.sashakyotoz.client.renderers.layers.GrippingLayer;
import net.sashakyotoz.client.renderers.projectiles.GrippingCrystalProjectileRenderer;
import net.sashakyotoz.client.renderers.projectiles.ParticleProjectileRenderer;
import net.sashakyotoz.client.renderers.projectiles.ThrownAuricArcRenderer;
import net.sashakyotoz.client.shaders.AuroraShader;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.common.world.portal.ChimericPortals;

import java.io.IOException;

@Mod(value = UnseenWorld.MOD_ID, dist = Dist.CLIENT)
public class UnseenWorldClient {
    private static final String WARRIOR_KEY = "entity.unseen_world.warrior_of_chimeric_darkness";
    private static final String SENTINEL_KEY = "entity.unseen_world.eclipse_sentinel";
    private static final String PATRIARCH_KEY = "entity.unseen_world.auric_patriarch";

    public UnseenWorldClient(IEventBus modBus, ModContainer container) {
        modBus.addListener(EventPriority.LOW, UnseenWorldClient::registerInHandModels);
        modBus.addListener(UnseenWorldClient::clientSetup);
        modBus.addListener(UnseenWorldClient::registerLayers);
        modBus.addListener(UnseenWorldClient::registerRenderers);
        modBus.addListener(UnseenWorldClient::addLayers);
        modBus.addListener(UnseenWorldClient::registerParticles);
        modBus.addListener(UnseenWorldClient::registerBlockColors);
        modBus.addListener(UnseenWorldClient::registerGuiLayers);
        modBus.addListener(UnseenWorldClient::registerShaders);
        modBus.addListener(UnseenWorldClient::registerDimensionEffects);
        modBus.addListener(UnseenWorldClient::registerMenuScreens);
        modBus.addListener(UnseenWorldClient::registerReloadListeners);

        IEventBus gameBus = NeoForge.EVENT_BUS;
        gameBus.addListener(ClientTicks::onEndTick);
        gameBus.addListener(BossFxRenderer::render);
        gameBus.addListener(ModClientMessages::onLoggingOut);
        gameBus.addListener(UnseenWorldClient::onEntityJoin);
        gameBus.addListener(UnseenWorldClient::onEntityLeave);
        gameBus.addListener(UnseenWorldClient::computeFov);

        PhasedBossBar.of(WARRIOR_KEY, "warrior_of_darkness_bossbar")
                .phase(BossEvent.BossBarColor.PURPLE, MapColor.COLOR_BLUE.col)
                .phase(BossEvent.BossBarColor.RED, MapColor.COLOR_RED.col)
                .register();
        PhasedBossBar.of(SENTINEL_KEY, "eclipse_sentinel_bossbar")
                .phase(BossEvent.BossBarColor.PINK, MapColor.COLOR_MAGENTA.col)
                .phase(BossEvent.BossBarColor.PURPLE, MapColor.COLOR_PURPLE.col)
                .register();
        PhasedBossBar.of(PATRIARCH_KEY, "auric_patriarch_bossbar")
                .phase(BossEvent.BossBarColor.YELLOW, MapColor.COLOR_ORANGE.col)
                .phase(BossEvent.BossBarColor.WHITE, MapColor.GOLD.col)
                .phase(BossEvent.BossBarColor.RED, MapColor.FIRE.col)
                .register();
    }

    private static void registerInHandModels(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            SolClientRegistries.ITEM_MODEL.addInHandModel(ModItems.AURIC_ARC);
            SolClientRegistries.ITEM_MODEL.addInHandModel(ModItems.CHIMERIC_ROCKBREAKER_HAMMER);
            SolClientRegistries.ITEM_MODEL.addInHandModel(ModItems.ECLIPSEBANE);
        }
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
                if (holder.isCutout() && holder.getID().getNamespace().equals(UnseenWorld.MOD_ID)) {
                    ItemBlockRenderTypes.setRenderLayer(holder.get(), RenderType.cutout());
                }
            });
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RIME_ICE, RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ChimericPortals.PORTAL, RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.DARK_WATER, RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.DARK_FLOWING_WATER, RenderType.translucent());

            Sheets.addWoodType(ModBlocks.GRIZZLY);
            Sheets.addWoodType(ModBlocks.AMETHYST);
            Sheets.addWoodType(ModBlocks.BURLYWOOD);
            Sheets.addWoodType(ModBlocks.TEALIVE);
            Sheets.addWoodType(ModBlocks.CRIMSONVEIL);

            ItemProperties.register(ModItems.GRIPPING_ABYSSAL_BOW, ResourceLocation.withDefaultNamespace("pull"), (ClampedItemPropertyFunction) (stack, world, entity, seed) -> {
                if (entity == null)
                    return 0.0F;
                else
                    return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
            });
            ItemProperties.register(ModItems.GRIPPING_ABYSSAL_BOW, ResourceLocation.withDefaultNamespace("pulling"), (ClampedItemPropertyFunction) (stack, world, entity, seed) -> entity != null
                    && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            ItemProperties.register(ModItems.SHIELD_OF_CHIMERIC_WARRIOR, ResourceLocation.withDefaultNamespace("blocking"), (ClampedItemPropertyFunction) (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            ItemProperties.register(ModItems.GRIPPING_BUNDLE, ResourceLocation.withDefaultNamespace("filled"), (ClampedItemPropertyFunction) (stack, world, entity, seed) -> BundleItem.getFullnessDisplay(stack));
            ItemProperties.register(ModItems.GRIPPING_GAUNTLET, ResourceLocation.withDefaultNamespace("grip"), (ClampedItemPropertyFunction) (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        });
    }

    private static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GleamcarverModel.GLEAMCARVER, GleamcarverModel::getModelData);
        event.registerLayerDefinition(HarmonyWatcherModel.HARMONY_WATCHER, HarmonyWatcherModel::getModelData);
        event.registerLayerDefinition(ViolegerModel.VIOLEGER, ViolegerModel::getModelData);
        event.registerLayerDefinition(AuricPatriarchModel.AURIC_PATRIARCH, AuricPatriarchModel::getModelData);
        event.registerLayerDefinition(WarriorOfChimericDarknessModel.WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarknessModel::getModelData);
        event.registerLayerDefinition(ShieldOfWarriorModel.SHIELD_OF_WARRIOR, ShieldOfWarriorModel::getModelData);
        event.registerLayerDefinition(EclipseSentinelModel.ECLIPSE_SENTINEL, EclipseSentinelModel::getModelData);
        event.registerLayerDefinition(GloomwhaleModel.GLOOMWHALE, GloomwhaleModel::getModelData);
        event.registerLayerDefinition(ShimmerModel.SHIMMER, ShimmerModel::getModelData);
        event.registerLayerDefinition(SaberpardModel.SABERPARD, SaberpardModel::getModelData);
        event.registerLayerDefinition(EspyerModel.ESPYER, EspyerModel::getModelData);
        event.registerLayerDefinition(EldritchWatcherModel.ELDRITCH_WATCHER, EldritchWatcherModel::getModelData);
        event.registerLayerDefinition(TuskhogModel.TUSKHOG, TuskhogModel::getModelData);
        event.registerLayerDefinition(DreadflapModel.DREADFLAP, DreadflapModel::getModelData);
        event.registerLayerDefinition(ReaverModel.REAVER, ReaverModel::getModelData);
        event.registerLayerDefinition(VexalBeetleModel.VEXAL_BEETLE, VexalBeetleModel::getModelData);
        event.registerLayerDefinition(AureateVexModel.AUREATE_VEX, AureateVexModel::getModelData);
        event.registerLayerDefinition(RimehornModel.RIMEHORN, RimehornModel::getModelData);
        event.registerLayerDefinition(EclipsedAcolyteModel.ECLIPSED_ACOLYTE, EclipsedAcolyteModel::getModelData);
        event.registerLayerDefinition(UmbralSerpentModel.UMBRAL_SERPENT, UmbralSerpentModel::getModelData);
        event.registerLayerDefinition(EffigyPedestalModel.LAYER, EffigyPedestalModel::getModelData);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.GLEAMCARVER, GleamcarverRenderer::new);
        event.registerEntityRenderer(ModEntities.HARMONY_WATCHER, HarmonyWatcherRenderer::new);
        event.registerEntityRenderer(ModEntities.VIOLEGER, ViolegerRenderer::new);
        event.registerEntityRenderer(ModEntities.VENOMER, VenomerRenderer::new);
        event.registerEntityRenderer(ModEntities.GLOOMWHALE, GloomwhaleRenderer::new);
        event.registerEntityRenderer(ModEntities.GRIPPING_GLOOMWHALE, GrippingGloomwhaleRenderer::new);
        event.registerEntityRenderer(ModEntities.SHIMMER, ShimmerRenderer::new);
        event.registerEntityRenderer(ModEntities.SABERPARD, SaberpardRenderer::new);
        event.registerEntityRenderer(ModEntities.ESPYER, EspyerRenderer::new);
        event.registerEntityRenderer(ModEntities.ELDRITCH_WATCHER, EldritchWatcherRenderer::new);
        event.registerEntityRenderer(ModEntities.TUSKHOG, TuskhogRenderer::new);
        event.registerEntityRenderer(ModEntities.DREADFLAP, DreadflapRenderer::new);
        event.registerEntityRenderer(ModEntities.REAVER, ReaverRenderer::new);
        event.registerEntityRenderer(ModEntities.VEXAL_BEETLE, VexalBeetleRenderer::new);
        event.registerEntityRenderer(ModEntities.AUREATE_VEX, AureateVexRenderer::new);
        event.registerEntityRenderer(ModEntities.RIMEHORN, RimehornRenderer::new);
        event.registerEntityRenderer(ModEntities.ECLIPSED_ACOLYTE, EclipsedAcolyteRenderer::new);
        event.registerEntityRenderer(ModEntities.UMBRAL_SERPENT, UmbralSerpentRenderer::new);
        event.registerEntityRenderer(ModEntities.GRIPPING_CRYSTAL_PROJECTILE, GrippingCrystalProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.PARTICLE_PROJECTILE, ParticleProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_AURIC_ARC, ThrownAuricArcRenderer::new);
        event.registerEntityRenderer(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarknessRenderer::new);
        event.registerEntityRenderer(ModEntities.ECLIPSE_SENTINEL, EclipseSentinelRenderer::new);
        event.registerEntityRenderer(ModEntities.AURIC_PATRIARCH, AuricPatriarchRenderer::new);

        event.registerBlockEntityRenderer(ModBlockEntities.ORDEAL_SPAWNER, OrdealSpawnerRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.EFFIGY, EffigyRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SUSBLOCK, BrushableBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DARK_CURRANTSLATE_CHEST, ModChestRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GLACIEMITE_CHEST, ModChestRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CORONA_STONE_CHEST, ModChestRenderer::new);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (EntityType<?> type : event.getEntityTypes()) {
            EntityRenderer<?> renderer = event.getRenderer(type);
            if (renderer instanceof LivingEntityRenderer livingRenderer) {
                addLivingLayers(livingRenderer, event.getContext());
            }
        }
        for (PlayerSkin.Model model : event.getSkins()) {
            EntityRenderer<? extends Player> renderer = event.getSkin(model);
            if (renderer instanceof LivingEntityRenderer livingRenderer) {
                addLivingLayers(livingRenderer, event.getContext());
            }
        }
    }

    private static <T extends LivingEntity, M extends net.minecraft.client.model.EntityModel<T>> void addLivingLayers(LivingEntityRenderer<T, M> renderer, net.minecraft.client.renderer.entity.EntityRendererProvider.Context context) {
        renderer.addLayer(new GrippingLayer<>(renderer, context));
        renderer.addLayer(new DarkeningLayer<>(renderer));
    }

    private static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.WIND_VIBRATION, WindVibrationParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.LIGHT_VIBRATION, LightVibrationParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.LEAF, LeafParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.GRIPPING_CRYSTAL, GrippingCrystalParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.EXPLOSION_CIRCLE, ExplosionCircleParticle.Factory::new);
    }

    private static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, world, pos, tintIndex) ->
                        world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.getDefaultColor(),
                ModBlocks.GLOOMWEED,
                ModBlocks.TALL_GLOOMWEED);
        event.register((state, world, pos, tintIndex) -> ChimericPortals.TINT, ChimericPortals.PORTAL);
    }

    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, UnseenWorld.MOD.makeID("full_screen_overlay"), new FullScreenOverlay());
    }

    private static void registerShaders(RegisterShadersEvent event) {
        try {
            AuroraShader.register(event);
        } catch (IOException exception) {
            UnseenWorld.log().error("Failed to load aurora shader: " + exception);
        }
    }

    private static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(ModDimensions.CHIMERIC_DARKNESS_LOCATION, new ChimericDarknessEffects());
    }

    private static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModScreenHandlers.DARK_CURRANTSLATE_CHEST, ContainerScreen::new);
        event.register(ModScreenHandlers.GLACIEMITE_CHEST, ContainerScreen::new);
        event.register(ModScreenHandlers.CORONA_STONE_CHEST, ContainerScreen::new);
    }

    private static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(PhasedBossBar.reloadListener());
    }

    private static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            BossFxRenderer.track(event.getEntity());
        }
    }

    private static void onEntityLeave(EntityLeaveLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            BossFxRenderer.untrack(event.getEntity());
        }
    }

    private static void computeFov(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getUseItem();
        if (player.isUsingItem() && itemStack.is(ModItems.GRIPPING_ABYSSAL_BOW)) {
            float g = (float) player.getTicksUsingItem() / 20.0F;
            g = g > 1.0F ? 1.0F : g * g;
            float f = event.getFovModifier() * (1.0F - g * 0.15F);
            event.setNewFovModifier(Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get().floatValue(), 1.0F, f));
        }
    }
}
