
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.util;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.sashakyotoz.unseenworld.client.model.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class UnseenWorldModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelVoid_Arrow.LAYER_LOCATION, ModelVoid_Arrow::createBodyLayer);
		event.registerLayerDefinition(ModelRed_Sylph.LAYER_LOCATION, ModelRed_Sylph::createBodyLayer);
		event.registerLayerDefinition(ModelAmethystGolem.LAYER_LOCATION, ModelAmethystGolem::createBodyLayer);
		event.registerLayerDefinition(ModelTheBlazer.LAYER_LOCATION, ModelTheBlazer::createBodyLayer);
		event.registerLayerDefinition(ModelTheBlazerShieldUp.LAYER_LOCATION, ModelTheBlazerShieldUp::createBodyLayer);
		event.registerLayerDefinition(ModelTealive_Skeleton.LAYER_LOCATION, ModelTealive_Skeleton::createBodyLayer);
		event.registerLayerDefinition(ModelThe_Wither_Knight_Armor.LAYER_LOCATION, ModelThe_Wither_Knight_Armor::createBodyLayer);
		event.registerLayerDefinition(ModelThe_Wither_Knight_Armor_Rods.LAYER_LOCATION, ModelThe_Wither_Knight_Armor_Rods::createBodyLayer);
		event.registerLayerDefinition(ModelStreder.LAYER_LOCATION, ModelStreder::createBodyLayer);
		event.registerLayerDefinition(ModelChimeric_Redmarer_With_Saddle.LAYER_LOCATION, ModelChimeric_Redmarer_With_Saddle::createBodyLayer);
		event.registerLayerDefinition(ModelVoidEndermen.LAYER_LOCATION, ModelVoidEndermen::createBodyLayer);
		event.registerLayerDefinition(ModelDustyPinkMaxorFish.LAYER_LOCATION, ModelDustyPinkMaxorFish::createBodyLayer);
		event.registerLayerDefinition(ModelLava_Enderman.LAYER_LOCATION, ModelLava_Enderman::createBodyLayer);
		event.registerLayerDefinition(ModelWarriorOfTheChimericDarkness.LAYER_LOCATION, ModelWarriorOfTheChimericDarkness::createBodyLayer);
		event.registerLayerDefinition(ModelSpiritOfWolf.LAYER_LOCATION, ModelSpiritOfWolf::createBodyLayer);
		event.registerLayerDefinition(ModelTealivy_Void_Spear.LAYER_LOCATION, ModelTealivy_Void_Spear::createBodyLayer);
		event.registerLayerDefinition(ModelBlazer_Helmet.LAYER_LOCATION, ModelBlazer_Helmet::createBodyLayer);
		event.registerLayerDefinition(ModelCavern_Scarecrow.LAYER_LOCATION, ModelCavern_Scarecrow::createBodyLayer);
		event.registerLayerDefinition(ModelDark_Phantom.LAYER_LOCATION, ModelDark_Phantom::createBodyLayer);
		event.registerLayerDefinition(ModelTanzanite_Guardian.LAYER_LOCATION, ModelTanzanite_Guardian::createBodyLayer);
		event.registerLayerDefinition(ModelThe_Wither_Knight_Advanced.LAYER_LOCATION, ModelThe_Wither_Knight_Advanced::createBodyLayer);
		event.registerLayerDefinition(ModelSavage_Small_Blaze.LAYER_LOCATION, ModelSavage_Small_Blaze::createBodyLayer);
		event.registerLayerDefinition(ModelRed_Ravager.LAYER_LOCATION, ModelRed_Ravager::createBodyLayer);
		event.registerLayerDefinition(ModelMoonFish.LAYER_LOCATION, ModelMoonFish::createBodyLayer);
		event.registerLayerDefinition(ModelArmoredSkeleton.LAYER_LOCATION, ModelArmoredSkeleton::createBodyLayer);
		event.registerLayerDefinition(ModelRed_Armored_Blaze.LAYER_LOCATION, ModelRed_Armored_Blaze::createBodyLayer);
		event.registerLayerDefinition(ModelDarkHoglin.LAYER_LOCATION, ModelDarkHoglin::createBodyLayer);
		event.registerLayerDefinition(ModelGhast_Of_Tealive_Valley.LAYER_LOCATION, ModelGhast_Of_Tealive_Valley::createBodyLayer);
		event.registerLayerDefinition(ModelStreder_with_Saddle.LAYER_LOCATION, ModelStreder_with_Saddle::createBodyLayer);
		event.registerLayerDefinition(ModelChimeric_Redmarer.LAYER_LOCATION, ModelChimeric_Redmarer::createBodyLayer);
		event.registerLayerDefinition(ModelThe_Wither_Knight.LAYER_LOCATION, ModelThe_Wither_Knight::createBodyLayer);
		event.registerLayerDefinition(ModelBeaconOfWeapons.LAYER_LOCATION,ModelBeaconOfWeapons::createBodyLayer);
		event.registerLayerDefinition(ModelVoidHammer.LAYER_LOCATION,ModelVoidHammer::createBodyLayer);
	}
}
