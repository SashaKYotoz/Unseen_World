package net.sashakyotoz.unseenworld.registries;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.client.model.*;
import net.sashakyotoz.unseenworld.client.particles.*;
import net.sashakyotoz.unseenworld.client.renderer.*;
import net.sashakyotoz.unseenworld.client.renderer.layers.KnightArmorRodsLayer;
import net.sashakyotoz.unseenworld.network.keys.ModKeyMappings;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = UnseenWorldMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class UnseenWorldModClientsRegistries {
    @SubscribeEvent
    public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event) {
        DimensionSpecialEffects customEffect = new DimensionSpecialEffects(Float.NaN, true, DimensionSpecialEffects.SkyType.NONE, false, false) {
            @Override
            public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
                return color.scale(0.15f);
            }

            @Override
            public boolean isFoggyAt(int x, int y) {
                return true;
            }

            @Override
            public boolean renderSnowAndRain(ClientLevel level, int ticks, float partialTick, LightTexture lightTexture, double camX, double camY, double camZ) {
                return true;
            }

            @Nullable
            public float[] getSunriseColor(float p_108888_, float p_108889_) {
                return null;
            }
        };
        event.register(new ResourceLocation("unseen_world:the_darkness"), customEffect);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(UnseenWorldModFluids.DARK_WATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(UnseenWorldModFluids.FLOWING_DARK_WATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(UnseenWorldModFluids.LIQUID_OF_CHIMERY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(UnseenWorldModFluids.FLOWING_LIQUID_OF_CHIMERY.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.AddLayers event) {
        EntityModelSet entityModels = event.getEntityModels();
        event.getSkins().forEach((s) -> {
            LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> livingEntityRenderer = event.getSkin(s);
            if (livingEntityRenderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new KnightArmorRodsLayer<>(playerRenderer, entityModels));
            }
        });
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(UnseenWorldModEntities.TEALIVY_VOID_SPEAR.get(), TealivyVoidSpearRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.UNSEEN_TITANIUM_SPEAR.get(), UnseenTitaniumSpearRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.VOID_HAMMER.get(), VoidHammerRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.NETHERIUM_STAFF.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.VOID_STAFF.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.TEALIVY_FIRE_STAFF.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.VOID_BOW.get(), VoidArrowRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.DARK_SKELETON.get(), DarkSkeletonRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.AMETHYST_GOLEM.get(), AmethystGolemRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.DARK_PHANTOM.get(), DarkPhantomRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.DUSTY_PINK_MAXOR_FISH.get(), DustyPinkMaxorFishRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.MOONFISH.get(), MoonFishRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.CAVERN_SCARECROW.get(), CavernScarecrowRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.SAVAGE_SMALL_BLAZE.get(), SavageSmallBlazeRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.CHIMERIC_PURPLEMARER.get(), ChimericPurplemarerRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.CHIMERIC_REDMARER.get(), ChimericRedmarerRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.NETHERMAN.get(), NethermanRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.RED_SLYLF.get(), RedSlylfRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.RED_BLAZE.get(), RedblazeRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.STREDER.get(), StrederRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.GHAST_OF_TEALIVE_VALLEY.get(), GhastOfTealiveValleyRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.TANZANITE_GUARDIAN.get(), TanzaniteGuardianRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.DARK_SPIRIT_WOLF.get(), DarkSpiritWolfRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.SNOWDRIFTER.get(), SnowdrifterRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.VOID_ENDERMEN.get(), VoidEndermenRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.TEALIVE_SKELETON.get(), TealiveSkeletonRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.RED_RAVENGER.get(), RedRavengerRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.DARK_HOGLIN.get(), DarkHoglinRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.DARK_GOLEM.get(), DarkGolemRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.THE_BLAZER.get(), TheBlazerRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.THE_WITHER_KNIGHT.get(), TheWitherKnightRenderer::new);
        event.registerEntityRenderer(UnseenWorldModEntities.DARK_PEARL.get(), ThrownItemRenderer::new);
        event.registerBlockEntityRenderer(UnseenWorldModBlockEntities.BEACON_OF_WEAPONS.get(), BeaconOfWeaponsRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(UnseenWorldModParticleTypes.BLUE_VOID_PARTICLE.get(), BlueVoidParticle::provider);
        event.registerSpriteSet(UnseenWorldModParticleTypes.REDNESS.get(), RednessParticle::provider);
        event.registerSpriteSet(UnseenWorldModParticleTypes.GOLDEN.get(), GoldenParticle::provider);
        event.registerSpriteSet(UnseenWorldModParticleTypes.GREENISH_PARTICLE.get(), GreenishParticle::provider);
        event.registerSpriteSet(UnseenWorldModParticleTypes.FIRE_PARTICLE.get(), FireParticle::provider);
        event.registerSpriteSet(UnseenWorldModParticleTypes.LIQUID_OF_CHIMERY_PARTICLE.get(), LiquidOfChimeryParticle::provider);
        event.registerSpriteSet(UnseenWorldModParticleTypes.GRIZZLY_PARTICLE.get(), GrizzlyParticle::provider);
        event.registerSpriteSet(UnseenWorldModParticleTypes.TANZANITE_RAY.get(), TanzaniteRayParticle::provider);
        event.registerSpriteSet(UnseenWorldModParticleTypes.VOID_PORTAL.get(), VoidPortalParticle::provider);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelBeaconOfWeapons.LAYER_LOCATION, ModelBeaconOfWeapons::createBodyLayer);
        event.registerLayerDefinition(ModelVoid_Arrow.LAYER_LOCATION, ModelVoid_Arrow::createBodyLayer);
        event.registerLayerDefinition(ModelRed_Sylph.LAYER_LOCATION, ModelRed_Sylph::createBodyLayer);
        event.registerLayerDefinition(ModelAmethystGolem.LAYER_LOCATION, ModelAmethystGolem::createBodyLayer);
        event.registerLayerDefinition(ModelTheBlazer.LAYER_LOCATION, ModelTheBlazer::createBodyLayer);
        event.registerLayerDefinition(ModelTheBlazerShieldUp.LAYER_LOCATION, ModelTheBlazerShieldUp::createBodyLayer);
        event.registerLayerDefinition(ModelTealive_Skeleton.LAYER_LOCATION, ModelTealive_Skeleton::createBodyLayer);
        event.registerLayerDefinition(ModelThe_Wither_Knight_Armor.LAYER_LOCATION, ModelThe_Wither_Knight_Armor::createBodyLayer);
        event.registerLayerDefinition(ModelThe_Wither_Knight_Armor_Rods.LAYER_LOCATION, ModelThe_Wither_Knight_Armor_Rods::createBodyLayer);
        event.registerLayerDefinition(ModelChimericRedmarerWithSaddle.LAYER_LOCATION, ModelChimericRedmarerWithSaddle::createBodyLayer);
        event.registerLayerDefinition(ModelVoidEndermen.LAYER_LOCATION, ModelVoidEndermen::createBodyLayer);
        event.registerLayerDefinition(ModelDustyPinkMaxorFish.LAYER_LOCATION, ModelDustyPinkMaxorFish::createBodyLayer);
        event.registerLayerDefinition(ModelNetherman.LAYER_LOCATION, ModelNetherman::createBodyLayer);
        event.registerLayerDefinition(ModelWarriorOfTheChimericDarkness.LAYER_LOCATION, ModelWarriorOfTheChimericDarkness::createBodyLayer);
        event.registerLayerDefinition(ModelSpiritOfWolf.LAYER_LOCATION, ModelSpiritOfWolf::createBodyLayer);
        event.registerLayerDefinition(ModelTealivy_Void_Spear.LAYER_LOCATION, ModelTealivy_Void_Spear::createBodyLayer);
        event.registerLayerDefinition(ModelBlazerHelmet.LAYER_LOCATION, ModelBlazerHelmet::createBodyLayer);
        event.registerLayerDefinition(ModelCavern_Scarecrow.LAYER_LOCATION, ModelCavern_Scarecrow::createBodyLayer);
        event.registerLayerDefinition(ModelDark_Phantom.LAYER_LOCATION, ModelDark_Phantom::createBodyLayer);
        event.registerLayerDefinition(ModelTanzanite_Guardian.LAYER_LOCATION, ModelTanzanite_Guardian::createBodyLayer);
        event.registerLayerDefinition(ModelThe_Wither_Knight_Advanced.LAYER_LOCATION, ModelThe_Wither_Knight_Advanced::createBodyLayer);
        event.registerLayerDefinition(ModelSavageSmallBlaze.LAYER_LOCATION, ModelSavageSmallBlaze::createBodyLayer);
        event.registerLayerDefinition(ModelRed_Ravager.LAYER_LOCATION, ModelRed_Ravager::createBodyLayer);
        event.registerLayerDefinition(ModelMoonFish.LAYER_LOCATION, ModelMoonFish::createBodyLayer);
        event.registerLayerDefinition(ModelArmoredSkeleton.LAYER_LOCATION, ModelArmoredSkeleton::createBodyLayer);
        event.registerLayerDefinition(ModelRed_Armored_Blaze.LAYER_LOCATION, ModelRed_Armored_Blaze::createBodyLayer);
        event.registerLayerDefinition(ModelDarkHoglin.LAYER_LOCATION, ModelDarkHoglin::createBodyLayer);
        event.registerLayerDefinition(ModelGhast_Of_Tealive_Valley.LAYER_LOCATION, ModelGhast_Of_Tealive_Valley::createBodyLayer);
        event.registerLayerDefinition(ModelStrederWithSaddle.LAYER_LOCATION, ModelStrederWithSaddle::createBodyLayer);
        event.registerLayerDefinition(ModelThe_Wither_Knight.LAYER_LOCATION, ModelThe_Wither_Knight::createBodyLayer);
        event.registerLayerDefinition(ModelSnowdrifter.LAYER_LOCATION, ModelSnowdrifter::createBodyLayer);
        event.registerLayerDefinition(ModelVoidHammer.LAYER_LOCATION, ModelVoidHammer::createBodyLayer);
        event.registerLayerDefinition(ModelPortalLikeParticleModel.LAYER_LOCATION, ModelPortalLikeParticleModel::createBodyLayer);
        event.registerLayerDefinition(ModelFireParticle.LAYER_LOCATION, ModelFireParticle::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.BLAZER_HELMET_ABILITY);
        event.register(ModKeyMappings.MINING_BOOTS_ABILITY);
    }
}
