
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import net.sashakyotoz.unseenworld.client.renderer.VoidEndermenRenderer;
import net.sashakyotoz.unseenworld.client.renderer.UnseenTitaniumSpearRenderer;
import net.sashakyotoz.unseenworld.client.renderer.TheWitherKnightRenderer;
import net.sashakyotoz.unseenworld.client.renderer.TheBlazerRenderer;
import net.sashakyotoz.unseenworld.client.renderer.TealivyVoidSpearRenderer;
import net.sashakyotoz.unseenworld.client.renderer.TealiveSkeletonRenderer;
import net.sashakyotoz.unseenworld.client.renderer.TanzaniteGuardianRenderer;
import net.sashakyotoz.unseenworld.client.renderer.StrederRenderer;
import net.sashakyotoz.unseenworld.client.renderer.SavageSmallBlazeRenderer;
import net.sashakyotoz.unseenworld.client.renderer.RedblazeRenderer;
import net.sashakyotoz.unseenworld.client.renderer.RedSlylfRenderer;
import net.sashakyotoz.unseenworld.client.renderer.RedRavengerRenderer;
import net.sashakyotoz.unseenworld.client.renderer.NethermenRenderer;
import net.sashakyotoz.unseenworld.client.renderer.MoonfishRenderer;
import net.sashakyotoz.unseenworld.client.renderer.GhastOfTealiveValleyRenderer;
import net.sashakyotoz.unseenworld.client.renderer.DustyPinkMaxorFishRenderer;
import net.sashakyotoz.unseenworld.client.renderer.DarkspiritwolfRenderer;
import net.sashakyotoz.unseenworld.client.renderer.DarkskeletonRenderer;
import net.sashakyotoz.unseenworld.client.renderer.DarkPhantomRenderer;
import net.sashakyotoz.unseenworld.client.renderer.DarkHoglinRenderer;
import net.sashakyotoz.unseenworld.client.renderer.DarkGolemRenderer;
import net.sashakyotoz.unseenworld.client.renderer.ChimericRedmarerRenderer;
import net.sashakyotoz.unseenworld.client.renderer.ChimericPurplemarerRenderer;
import net.sashakyotoz.unseenworld.client.renderer.CavernScarecrowRenderer;
import net.sashakyotoz.unseenworld.client.renderer.AmethystGolemRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class UnseenWorldModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(UnseenWorldModEntities.TEALIVY_VOID_SPEAR.get(), TealivyVoidSpearRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.UNSEEN_TITANIUM_SPEAR.get(), UnseenTitaniumSpearRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.NETHERIUM_STAFF.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.VOID_STAFF.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.TEALIVY_FIRE_STAFF.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.VOID_BOW.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.DARKSKELETON.get(), DarkskeletonRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.AMETHYST_GOLEM.get(), AmethystGolemRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.DARK_PHANTOM.get(), DarkPhantomRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.DUSTY_PINK_MAXOR_FISH.get(), DustyPinkMaxorFishRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.MOONFISH.get(), MoonfishRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.CAVERN_SCARECROW.get(), CavernScarecrowRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.SAVAGE_SMALL_BLAZE.get(), SavageSmallBlazeRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.CHIMERIC_PURPLEMARER.get(), ChimericPurplemarerRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.CHIMERIC_REDMARER.get(), ChimericRedmarerRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.NETHERMEN.get(), NethermenRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.RED_SLYLF.get(), RedSlylfRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.RED_BLAZE.get(), RedblazeRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.STREDER.get(), StrederRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.GHAST_OF_TEALIVE_VALLEY.get(), GhastOfTealiveValleyRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.TANZANITE_GUARDIAN.get(), TanzaniteGuardianRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.DARKSPIRITWOLF.get(), DarkspiritwolfRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.VOID_ENDERMEN.get(), VoidEndermenRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.TEALIVE_SKELETON.get(), TealiveSkeletonRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.RED_RAVENGER.get(), RedRavengerRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.DARK_HOGLIN.get(), DarkHoglinRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.DARK_GOLEM.get(), DarkGolemRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.THE_BLAZER.get(), TheBlazerRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.THE_WITHER_KNIGHT.get(), TheWitherKnightRenderer::new);
		event.registerEntityRenderer(UnseenWorldModEntities.DARK_PEARL_RANGED_ITEM.get(), ThrownItemRenderer::new);
	}
}
