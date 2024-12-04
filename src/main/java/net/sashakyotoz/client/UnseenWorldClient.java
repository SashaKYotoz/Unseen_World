package net.sashakyotoz.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.minecraft.block.Block;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.WorldClientTickEventHandler;
import net.sashakyotoz.client.environment.WorldRendererEventsHandler;
import net.sashakyotoz.client.gui.FullScreenOverlay;
import net.sashakyotoz.client.gui.GripCrystalHUDOverlay;
import net.sashakyotoz.client.models.*;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.client.particles.custom.GrippingCrystalParticle;
import net.sashakyotoz.client.particles.custom.LeafParticle;
import net.sashakyotoz.client.particles.custom.LightVibrationParticle;
import net.sashakyotoz.client.particles.custom.WindVibrationParticle;
import net.sashakyotoz.client.renderers.*;
import net.sashakyotoz.client.renderers.blocks.GlaciemiteTranslocatoneRenderer;
import net.sashakyotoz.client.renderers.blocks.KeyHandlerStoneRenderer;
import net.sashakyotoz.client.renderers.projectiles.GrippingCrystalProjectileRenderer;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.networking.KeyInputHandler;
import net.sashakyotoz.common.networking.ModMessages;
import net.sashakyotoz.common.networking.data.GripcrystalManaData;
import net.sashakyotoz.utils.IEntityDataSaver;

public class UnseenWorldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Block block : ModRegistry.BLOCK_CUTOUT)
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        for (Block block : ModRegistry.BLOCK_TRANSLUCENT)
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent());

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.DARK_WATER, ModFluids.DARK_FLOWING_WATER,
                new SimpleFluidRenderHandler(UnseenWorld.makeID("block/dark_water_still"),
                        UnseenWorld.makeID("block/dark_water_flow"),
                        0x75050005
                ));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ModFluids.DARK_WATER, ModFluids.DARK_FLOWING_WATER);

        EntityModelLayerRegistry.registerModelLayer(GleamcarverModel.GLEAMCARVER, GleamcarverModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HarmonyWatcherModel.HARMONY_WATCHER, HarmonyWatcherModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DarkGuardianModel.DARK_GUARDIAN, DarkGuardianModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WarriorOfChimericDarknessModel.WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarknessModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ShieldOfWarriorModel.SHIELD_OF_WARRIOR, ShieldOfWarriorModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(EclipsebaneModel.ECLIPSEBANE, EclipsebaneModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(EclipseSentinelModel.ECLIPSE_SENTINEL, EclipseSentinelModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(GloomwhaleModel.GLOOMWHALE, GloomwhaleModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SaberpardModel.SABERPARD, SaberpardModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(EspyerModel.ESPYER, EspyerModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(EldritchWatcherModel.ELDRITCH_WATCHER, EldritchWatcherModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntities.GLEAMCARVER, GleamcarverRenderer::new);
        EntityRendererRegistry.register(ModEntities.HARMONY_WATCHER, HarmonyWatcherRenderer::new);
        EntityRendererRegistry.register(ModEntities.DARK_GUARDIAN, DarkGuardianRenderer::new);
        EntityRendererRegistry.register(ModEntities.GLOOMWHALE, GloomwhaleRenderer::new);
        EntityRendererRegistry.register(ModEntities.SABERPARD, SaberpardRenderer::new);
        EntityRendererRegistry.register(ModEntities.ESPYER, EspyerRenderer::new);
        EntityRendererRegistry.register(ModEntities.ELDRITCH_WATCHER, EldritchWatcherRenderer::new);

        EntityRendererRegistry.register(ModEntities.GRIPPING_CRYSTAL_PROJECTILE, GrippingCrystalProjectileRenderer::new);

        ColorProviderRegistryImpl.BLOCK.register((state, world, pos, tintIndex) ->
                        world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getDefaultColor(),
                ModBlocks.GLOOMWEED,
                ModBlocks.TALL_GLOOMWEED);

        EntityRendererRegistry.register(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarknessRenderer::new);
        EntityRendererRegistry.register(ModEntities.ECLIPSE_SENTINEL, EclipseSentinelRenderer::new);

        BlockEntityRendererFactories.register(ModBlockEntities.KEY_HANDLER, KeyHandlerStoneRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.TRANSLOCATONE, GlaciemiteTranslocatoneRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.WIND_VIBRATION, WindVibrationParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.LIGHT_VIBRATION, LightVibrationParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.LEAF, LeafParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.GRIPPING_CRYSTAL, GrippingCrystalParticle.Factory::new);

        KeyInputHandler.register();
        ModMessages.registerS2CPackets();
        HudRenderCallback.EVENT.register(new GripCrystalHUDOverlay());
        HudRenderCallback.EVENT.register(new FullScreenOverlay());

        WorldRenderEvents.AFTER_TRANSLUCENT.register(new WorldRendererEventsHandler());
        ClientTickEvents.END_CLIENT_TICK.register(new WorldClientTickEventHandler());
        ModelPredicateProviderRegistry.register(ModItems.GRIPPING_ABYSSAL_BOW, new Identifier("pull"), (stack, world, entity, seed) -> {
            if (entity == null)
                return 0.0F;
            else
                return entity.getActiveItem() != stack ? 0.0F : (float) (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
        });
        ModelPredicateProviderRegistry.register(ModItems.GRIPPING_ABYSSAL_BOW, new Identifier("pulling"), (stack, world, entity, seed) -> entity != null
                && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(ModItems.GRIPPING_ABYSSAL_BOW, UnseenWorld.makeID("charge"), (stack, world, entity, seed) -> {
            switch (stack.getOrCreateNbt().getString("bow_phase")) {
                case "crystal_crushing", "crystal_suctioning" -> {
                    return 2;
                }
                case "crystal_rain" -> {
                    return 1;
                }
                default -> {
                    return 0;
                }
            }
        });
        ModelPredicateProviderRegistry.register(ModItems.SHIELD_OF_CHIMERIC_WARRIOR, new Identifier("blocking"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
    }
}