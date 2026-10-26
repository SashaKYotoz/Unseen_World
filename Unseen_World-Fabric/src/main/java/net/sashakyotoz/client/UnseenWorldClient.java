package net.sashakyotoz.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.lcc.sollib.api.client.SolClientRegistries;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.BrushableBlockRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.material.MapColor;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import net.sashakyotoz.api.multipart_entity.WorldMultipartHelper;
import net.sashakyotoz.client.environment.ChimericDarknessEffects;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.client.environment.weather.Grippfall;
import net.sashakyotoz.client.gui.FullScreenOverlay;
import net.sashakyotoz.client.gui.PhasedBossBar;
import net.sashakyotoz.client.gui.blocks.ChestScreenHandler;
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

public class UnseenWorldClient implements ClientModInitializer {
    private static final String WARRIOR_KEY = "entity.unseen_world.warrior_of_chimeric_darkness";
    private static final String SENTINEL_KEY = "entity.unseen_world.eclipse_sentinel";
    private static final String PATRIARCH_KEY = "entity.unseen_world.auric_patriarch";

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RIME_ICE, RenderType.translucent());

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.DARK_WATER, ModFluids.DARK_FLOWING_WATER,
                new SimpleFluidRenderHandler(UnseenWorld.MOD.makeID("block/dark_water_still"),
                        UnseenWorld.MOD.makeID("block/dark_water_flow"),
                        0x75050005
                ));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), ModFluids.DARK_WATER, ModFluids.DARK_FLOWING_WATER);

        EntityModelLayerRegistry.registerModelLayer(GleamcarverModel.GLEAMCARVER, GleamcarverModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(HarmonyWatcherModel.HARMONY_WATCHER, HarmonyWatcherModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(ViolegerModel.VIOLEGER, ViolegerModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(AuricPatriarchModel.AURIC_PATRIARCH, AuricPatriarchModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(WarriorOfChimericDarknessModel.WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarknessModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(ShieldOfWarriorModel.SHIELD_OF_WARRIOR, ShieldOfWarriorModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(EclipseSentinelModel.ECLIPSE_SENTINEL, EclipseSentinelModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(GloomwhaleModel.GLOOMWHALE, GloomwhaleModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(ShimmerModel.SHIMMER, ShimmerModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(SaberpardModel.SABERPARD, SaberpardModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(EspyerModel.ESPYER, EspyerModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(EldritchWatcherModel.ELDRITCH_WATCHER, EldritchWatcherModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(TuskhogModel.TUSKHOG, TuskhogModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(DreadflapModel.DREADFLAP, DreadflapModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(ReaverModel.REAVER, ReaverModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(VexalBeetleModel.VEXAL_BEETLE, VexalBeetleModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(AureateVexModel.AUREATE_VEX, AureateVexModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(RimehornModel.RIMEHORN, RimehornModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(EclipsedAcolyteModel.ECLIPSED_ACOLYTE, EclipsedAcolyteModel::getModelData);
        EntityModelLayerRegistry.registerModelLayer(UmbralSerpentModel.UMBRAL_SERPENT, UmbralSerpentModel::getModelData);

        EntityModelLayerRegistry.registerModelLayer(EffigyPedestalModel.LAYER, EffigyPedestalModel::getModelData);

        EntityRendererRegistry.register(ModEntities.GLEAMCARVER, GleamcarverRenderer::new);
        EntityRendererRegistry.register(ModEntities.HARMONY_WATCHER, HarmonyWatcherRenderer::new);
        EntityRendererRegistry.register(ModEntities.VIOLEGER, ViolegerRenderer::new);
        EntityRendererRegistry.register(ModEntities.VENOMER, VenomerRenderer::new);
        EntityRendererRegistry.register(ModEntities.GLOOMWHALE, GloomwhaleRenderer::new);
        EntityRendererRegistry.register(ModEntities.GRIPPING_GLOOMWHALE, GrippingGloomwhaleRenderer::new);
        EntityRendererRegistry.register(ModEntities.SHIMMER, ShimmerRenderer::new);
        EntityRendererRegistry.register(ModEntities.SABERPARD, SaberpardRenderer::new);
        EntityRendererRegistry.register(ModEntities.ESPYER, EspyerRenderer::new);
        EntityRendererRegistry.register(ModEntities.ELDRITCH_WATCHER, EldritchWatcherRenderer::new);
        EntityRendererRegistry.register(ModEntities.TUSKHOG, TuskhogRenderer::new);
        EntityRendererRegistry.register(ModEntities.DREADFLAP, DreadflapRenderer::new);
        EntityRendererRegistry.register(ModEntities.REAVER, ReaverRenderer::new);
        EntityRendererRegistry.register(ModEntities.VEXAL_BEETLE, VexalBeetleRenderer::new);
        EntityRendererRegistry.register(ModEntities.AUREATE_VEX, AureateVexRenderer::new);
        EntityRendererRegistry.register(ModEntities.RIMEHORN, RimehornRenderer::new);
        EntityRendererRegistry.register(ModEntities.ECLIPSED_ACOLYTE, EclipsedAcolyteRenderer::new);
        EntityRendererRegistry.register(ModEntities.UMBRAL_SERPENT, UmbralSerpentRenderer::new);

        EntityRendererRegistry.register(ModEntities.GRIPPING_CRYSTAL_PROJECTILE, GrippingCrystalProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.PARTICLE_PROJECTILE, ParticleProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.THROWN_AURIC_ARC, ThrownAuricArcRenderer::new);

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

        EntityRendererRegistry.register(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarknessRenderer::new);
        EntityRendererRegistry.register(ModEntities.ECLIPSE_SENTINEL, EclipseSentinelRenderer::new);
        EntityRendererRegistry.register(ModEntities.AURIC_PATRIARCH, AuricPatriarchRenderer::new);

        BlockEntityRenderers.register(ModBlockEntities.ORDEAL_SPAWNER, OrdealSpawnerRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.EFFIGY, EffigyRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.SUSBLOCK, BrushableBlockRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.DARK_CURRANTSLATE_CHEST, ModChestRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.GLACIEMITE_CHEST, ModChestRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.CORONA_STONE_CHEST, ModChestRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.WIND_VIBRATION, WindVibrationParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.LIGHT_VIBRATION, LightVibrationParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.LEAF, LeafParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.GRIPPING_CRYSTAL, GrippingCrystalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.EXPLOSION_CIRCLE, ExplosionCircleParticle.Factory::new);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
                        world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.getDefaultColor(),
                ModBlocks.GLOOMWEED,
                ModBlocks.TALL_GLOOMWEED);
        ModClientMessages.register();
        HudRenderCallback.EVENT.register(new FullScreenOverlay());
        ClientTickEvents.END_CLIENT_TICK.register(new ClientTicks());

        CoreShaderRegistrationCallback.EVENT.register(AuroraShader::register);

        DimensionRenderingRegistry.registerDimensionEffects(ModDimensions.CHIMERIC_DARKNESS_LOCATION, new ChimericDarknessEffects());
        DimensionRenderingRegistry.registerWeatherRenderer(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY, new Grippfall());

        ItemProperties.register(ModItems.GRIPPING_ABYSSAL_BOW, new ResourceLocation("pull"), (stack, world, entity, seed) -> {
            if (entity == null)
                return 0.0F;
            else
                return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
        });
        ItemProperties.register(ModItems.GRIPPING_ABYSSAL_BOW, new ResourceLocation("pulling"), (stack, world, entity, seed) -> entity != null
                && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        ItemProperties.register(ModItems.SHIELD_OF_CHIMERIC_WARRIOR, new ResourceLocation("blocking"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        ItemProperties.register(ModItems.GRIPPING_BUNDLE, new ResourceLocation("filled"), (stack, world, entity, seed) -> BundleItem.getFullnessDisplay(stack));
        ItemProperties.register(ModItems.GRIPPING_GAUNTLET, new ResourceLocation("grip"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

        SolClientRegistries.ITEM_MODEL.addInHandModel(ModItems.AURIC_ARC);
        SolClientRegistries.ITEM_MODEL.addInHandModel(ModItems.CHIMERIC_ROCKBREAKER_HAMMER);
        SolClientRegistries.ITEM_MODEL.addInHandModel(ModItems.ECLIPSEBANE);

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            BossFxRenderer.track(entity);
            if (entity instanceof MultipartEntity multipartEntity) {
                Int2ObjectMap<EntityPart> partMap = ((WorldMultipartHelper) world).unseen_World_Fabric$getPMEPartMap();
                for (EntityPart part : multipartEntity.getParts()) partMap.put(part.getId(), part);
            }
        });
        ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            BossFxRenderer.untrack(entity);
            if (entity instanceof MultipartEntity multipartEntity) {
                Int2ObjectMap<EntityPart> partMap = ((WorldMultipartHelper) world).unseen_World_Fabric$getPMEPartMap();
                for (EntityPart part : multipartEntity.getParts()) partMap.remove(part.getId());
            }
        });
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            registrationHelper.register(new GrippingLayer<>(entityRenderer, context));
            registrationHelper.register(new DarkeningLayer<>(entityRenderer));
        });
        WorldRenderEvents.AFTER_ENTITIES.register(BossFxRenderer::render);

        MenuScreens.register(
                ModScreenHandlers.DARK_CURRANTSLATE_CHEST,
                (ChestScreenHandler handler, Inventory inv, Component title) -> new CottonInventoryScreen<>(handler, inv.player, title)
        );
        MenuScreens.register(ModScreenHandlers.GLACIEMITE_CHEST,
                (ChestScreenHandler handler, Inventory inv, Component title) -> new CottonInventoryScreen<>(handler, inv.player, title)
        );
        MenuScreens.register(ModScreenHandlers.CORONA_STONE_CHEST,
                (ChestScreenHandler handler, Inventory inv, Component title) -> new CottonInventoryScreen<>(handler, inv.player, title)
        );
    }
}