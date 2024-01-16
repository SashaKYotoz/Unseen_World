
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.util;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.sashakyotoz.unseenworld.client.particles.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class UnseenWorldModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(UnseenWorldModParticleTypes.BLUEVOIDPARTICLE.get(), BlueVoidParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.REDNESS.get(), RednessParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.GOLDEN.get(), GoldenParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.GREENISH_PARTICLE.get(), GreenishParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.FIRE_PARTICLE.get(), FireParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.LIQUID_OF_CHIMERY_PARTICLE.get(), LiquidOfChimeryParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.GRIZZLY_PARTICLE.get(), GrizzlyParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.TANZANITE_RAY.get(), TanzaniteRayParticle::provider);
	}
}
