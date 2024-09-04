package net.sashakyotoz.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
<<<<<<< Updated upstream
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.sashakyotoz.common.ModRegistry;
=======
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
>>>>>>> Stashed changes

public class UnseenWorldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Block block : ModRegistry.BLOCK_CUTOUT)
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
<<<<<<< Updated upstream
=======

        FluidRenderHandlerRegistry.INSTANCE.register(ModBlocks.DARK_WATER,
                new SimpleFluidRenderHandler(SimpleFluidRenderHandler.WATER_STILL,
                        SimpleFluidRenderHandler.WATER_FLOWING,
                        SimpleFluidRenderHandler.WATER_OVERLAY, 0x000));
        FluidRenderHandlerRegistry.INSTANCE.register(ModBlocks.DARK_FLOWING_WATER,
                new SimpleFluidRenderHandler(SimpleFluidRenderHandler.WATER_STILL,
                        SimpleFluidRenderHandler.WATER_FLOWING,
                        SimpleFluidRenderHandler.WATER_OVERLAY, 0x000));
>>>>>>> Stashed changes
    }
}
