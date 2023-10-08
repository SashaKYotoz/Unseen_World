
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.util;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.sashakyotoz.unseenworld.client.particle.RednessParticle;
import net.sashakyotoz.unseenworld.client.particle.LiquidOfChimeryParticleParticle;
import net.sashakyotoz.unseenworld.client.particle.GrizzlyParticleParticle;
import net.sashakyotoz.unseenworld.client.particle.GreenishParticleParticle;
import net.sashakyotoz.unseenworld.client.particle.GoldenParticle;
import net.sashakyotoz.unseenworld.client.particle.FireParticleParticle;
import net.sashakyotoz.unseenworld.client.particle.BluevoidparticleParticle;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class UnseenWorldModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(UnseenWorldModParticleTypes.BLUEVOIDPARTICLE.get(), BluevoidparticleParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.REDNESS.get(), RednessParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.GOLDEN.get(), GoldenParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.GREENISH_PARTICLE.get(), GreenishParticleParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.FIRE_PARTICLE.get(), FireParticleParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.LIQUID_OF_CHIMERY_PARTICLE.get(), LiquidOfChimeryParticleParticle::provider);
		event.registerSpriteSet(UnseenWorldModParticleTypes.GRIZZLY_PARTICLE.get(), GrizzlyParticleParticle::provider);
	}
}
