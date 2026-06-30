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
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.BrushableBlockRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import net.sashakyotoz.api.multipart_entity.WorldMultipartHelper;
import net.sashakyotoz.client.environment.ChimericDarknessSkyRenderer;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.client.environment.weather.Grippfall;
import net.sashakyotoz.client.gui.FullScreenOverlay;
import net.sashakyotoz.client.gui.blocks.ChestScreenHandler;
import net.sashakyotoz.client.gui.blocks.ModScreenHandlers;
import net.sashakyotoz.client.models.*;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.client.particles.custom.GrippingCrystalParticle;
import net.sashakyotoz.client.particles.custom.LeafParticle;
import net.sashakyotoz.client.particles.custom.LightVibrationParticle;
import net.sashakyotoz.client.particles.custom.WindVibrationParticle;
import net.sashakyotoz.client.renderers.*;
import net.sashakyotoz.client.renderers.blocks.KeyHandlerStoneRenderer;
import net.sashakyotoz.client.renderers.blocks.ModChestRenderer;
import net.sashakyotoz.client.renderers.blocks.TranslocatoneRenderer;
import net.sashakyotoz.client.renderers.bosses.EclipseSentinelRenderer;
import net.sashakyotoz.client.renderers.bosses.WarriorOfChimericDarknessRenderer;
import net.sashakyotoz.client.renderers.layers.DarkeningLayer;
import net.sashakyotoz.client.renderers.layers.GrippingLayer;
import net.sashakyotoz.client.renderers.projectiles.GrippingCrystalProjectileRenderer;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.networking.ModMessages;
import net.sashakyotoz.common.world.ModDimensions;

public class UnseenWorldClient implements ClientModInitializer {
    private static final ResourceLocation GUI_BARS_LOCATION = new ResourceLocation("textures/gui/bars.png");
    private static final ResourceLocation BOSSBAR_WARRIOR_OF_DARKNESS = UnseenWorld.makeID("textures/gui/bossbars/warrior_of_darkness_bossbar.png");
    private static final ResourceLocation BOSSBAR_ECLIPSE_SENTINEL = UnseenWorld.makeID("textures/gui/bossbars/eclipse_sentinel_bossbar.png");

    @Override
    public void onInitializeClient() {
        for (Block block : ModRegistry.BLOCK_CUTOUT)
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        for (Block block : ModRegistry.BLOCK_TRANSLUCENT)
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.translucent());

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.DARK_WATER, ModFluids.DARK_FLOWING_WATER,
                new SimpleFluidRenderHandler(UnseenWorld.makeID("block/dark_water_still"),
                        UnseenWorld.makeID("block/dark_water_flow"),
                        0x75050005
                ));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), ModFluids.DARK_WATER, ModFluids.DARK_FLOWING_WATER);

        EntityModelLayerRegistry.registerModelLayer(GleamcarverModel.GLEAMCARVER, GleamcarverModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(HarmonyWatcherModel.HARMONY_WATCHER, HarmonyWatcherModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(ViolegerModel.VIOLEGER, ViolegerModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(AuricPatriarchModel.AURIC_PATRIARCH, AuricPatriarchModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(WarriorOfChimericDarknessModel.WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarknessModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(ShieldOfWarriorModel.SHIELD_OF_WARRIOR, ShieldOfWarriorModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(EclipseSentinelModel.ECLIPSE_SENTINEL, EclipseSentinelModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(GloomwhaleModel.GLOOMWHALE, GloomwhaleModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(ShimmerModel.SHIMMER, ShimmerModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(SaberpardModel.SABERPARD, SaberpardModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(EspyerModel.ESPYER, EspyerModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(EldritchWatcherModel.ELDRITCH_WATCHER, EldritchWatcherModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(TuskhogModel.TUSKHOG, TuskhogModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(DreadflapModel.DREADFLAP, DreadflapModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(ReaverModel.REAVER, ReaverModel::getTextureLocationdModelData);
        EntityModelLayerRegistry.registerModelLayer(VexalBeetleModel.VEXAL_BEETLE, VexalBeetleModel::getTextureLocationdModelData);

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

        EntityRendererRegistry.register(ModEntities.GRIPPING_CRYSTAL_PROJECTILE, GrippingCrystalProjectileRenderer::new);

        SolClientRegistries.BOSS_BAR.register(bossEvent ->
                        bossEvent.getName().contains(Component.translatable("entity.unseen_world.warrior_of_chimeric_darkness")),
                (guiGraphics, x, y, bossEvent) -> {
                    guiGraphics.blit(GUI_BARS_LOCATION, x, y, 0, bossEvent.getColor().ordinal() * 5 * 2 + 5, (int) (bossEvent.getProgress() * 183.0F), 5);
                    guiGraphics.blit(BOSSBAR_WARRIOR_OF_DARKNESS, x, y - 2, 0.0F, 0.0F, 183, 9, 183, 9);
                });
        SolClientRegistries.BOSS_BAR.register(bossEvent ->
                        bossEvent.getName().contains(Component.translatable("entity.unseen_world.eclipse_sentinel")),
                (guiGraphics, x, y, bossEvent) -> {
                    guiGraphics.blit(GUI_BARS_LOCATION, x, y, 0, bossEvent.getColor().ordinal() * 5 * 2 + 5, (int) (bossEvent.getProgress() * 183.0F), 5);
                    guiGraphics.blit(BOSSBAR_ECLIPSE_SENTINEL, x, y - 2, 0.0F, 0.0F, 183, 9, 183, 9);
                });

        EntityRendererRegistry.register(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS, WarriorOfChimericDarknessRenderer::new);
        EntityRendererRegistry.register(ModEntities.ECLIPSE_SENTINEL, EclipseSentinelRenderer::new);

        BlockEntityRenderers.register(ModBlockEntities.KEY_HANDLER, KeyHandlerStoneRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.TRANSLOCATONE, TranslocatoneRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.SUSBLOCK, BrushableBlockRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.DARK_CURRANTSLATE_CHEST, ModChestRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.GLACIEMITE_CHEST, ModChestRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.WIND_VIBRATION, WindVibrationParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.LIGHT_VIBRATION, LightVibrationParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.LEAF, LeafParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.GRIPPING_CRYSTAL, GrippingCrystalParticle.Factory::new);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
                        world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.getDefaultColor(),
                ModBlocks.GLOOMWEED,
                ModBlocks.TALL_GLOOMWEED);
        ModMessages.registerS2CPackets();
        HudRenderCallback.EVENT.register(new FullScreenOverlay());
        ClientTickEvents.END_CLIENT_TICK.register(new ClientTicks());

        DimensionRenderingRegistry.registerDimensionEffects(UnseenWorld.makeID("the_chimeric_darkness"),
                new DimensionSpecialEffects(Float.NaN, false, DimensionSpecialEffects.SkyType.NONE, false, true) {
                    @Override
                    public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
                        return color.scale(0.1);
                    }

                    @Override
                    public boolean isFoggyAt(int camX, int camY) {
                        return true;
                    }
                });
        DimensionRenderingRegistry.registerSkyRenderer(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY, new ChimericDarknessSkyRenderer());
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

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof MultipartEntity multipartEntity) {
                Int2ObjectMap<EntityPart> partMap = ((WorldMultipartHelper) world).getPMEPartMap();
                for (EntityPart part : multipartEntity.getParts()) partMap.put(part.getId(), part);
            }
        });
        ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity instanceof MultipartEntity multipartEntity) {
                Int2ObjectMap<EntityPart> partMap = ((WorldMultipartHelper) world).getPMEPartMap();
                for (EntityPart part : multipartEntity.getParts()) partMap.remove(part.getId());
            }
        });
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            registrationHelper.register(new GrippingLayer<>(entityRenderer, context));
            registrationHelper.register(new DarkeningLayer<>(entityRenderer));
        });
        MenuScreens.register(
                ModScreenHandlers.DARK_CURRANTSLATE_CHEST,
                (ChestScreenHandler handler, Inventory inv, Component title) -> new CottonInventoryScreen<>(handler, inv.player, title)
        );
        MenuScreens.register(ModScreenHandlers.GLACIEMITE_CHEST,
                (ChestScreenHandler handler, Inventory inv, Component title) -> new CottonInventoryScreen<>(handler, inv.player, title)
        );
    }
}