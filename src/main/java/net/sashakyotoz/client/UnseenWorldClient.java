package net.sashakyotoz.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.WorldRendererEventsHandler;
import net.sashakyotoz.client.models.*;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.client.particles.custom.WindVibrationParticle;
import net.sashakyotoz.client.renderers.DarkGuardianRenderer;
import net.sashakyotoz.client.renderers.GleamcarverRenderer;
import net.sashakyotoz.client.renderers.HarmonyWatcherRenderer;
import net.sashakyotoz.client.renderers.WarriorOfChimericDarknessRenderer;
import net.sashakyotoz.client.renderers.blocks.GlaciemiteTranslocatoneRenderer;
import net.sashakyotoz.client.renderers.blocks.KeyHandlerStoneRenderer;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;

public class UnseenWorldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Block block : ModRegistry.BLOCK_CUTOUT)
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        for (Block block : ModRegistry.BLOCK_TRANSLUCENT)
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent());

        FluidRenderHandlerRegistry.INSTANCE.register(ModBlocks.DARK_WATER,
                new SimpleFluidRenderHandler(UnseenWorld.makeID("block/dark_water_still"),
                        UnseenWorld.makeID("block/dark_water_flow"),
                        null,0x0));
        FluidRenderHandlerRegistry.INSTANCE.register(ModBlocks.DARK_FLOWING_WATER,
                new SimpleFluidRenderHandler(UnseenWorld.makeID("block/dark_water_still"),
                        UnseenWorld.makeID("block/dark_water_flow"),
                        null,0x0));

        EntityModelLayerRegistry.registerModelLayer(GleamcarverModel.GLEAMCARVER, GleamcarverModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HarmonyWatcherModel.HARMONY_WATCHER, HarmonyWatcherModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DarkGuardianModel.DARK_GUARDIAN, DarkGuardianModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WarriorOfChimericDarknessModel.WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarknessModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ShieldOfWarriorModel.SHIELD_OF_WARRIOR, ShieldOfWarriorModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntities.GLEAMCARVER, GleamcarverRenderer::new);
        EntityRendererRegistry.register(ModEntities.HARMONY_WATCHER, HarmonyWatcherRenderer::new);
        EntityRendererRegistry.register(ModEntities.DARK_GUARDIAN, DarkGuardianRenderer::new);

        EntityRendererRegistry.register(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarknessRenderer::new);

        BlockEntityRendererFactories.register(ModBlockEntities.KEY_HANDLER, KeyHandlerStoneRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.GLACIEMITE_TRANSLOCATONE, GlaciemiteTranslocatoneRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.WIND_VIBRATION, WindVibrationParticle.Factory::new);

        WorldRenderEvents.AFTER_TRANSLUCENT.register(new WorldRendererEventsHandler());

        ModelPredicateProviderRegistry.register(ModItems.SHIELD_OF_CHIMERIC_WARRIOR,new Identifier("blocking"),(stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
    }
}