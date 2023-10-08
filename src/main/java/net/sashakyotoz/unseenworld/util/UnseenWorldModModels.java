
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
		event.registerLayerDefinition(Modelamethyst_golem.LAYER_LOCATION, Modelamethyst_golem::createBodyLayer);
		event.registerLayerDefinition(Modelthe_blazer.LAYER_LOCATION, Modelthe_blazer::createBodyLayer);
		event.registerLayerDefinition(ModelTealive_Skeleton.LAYER_LOCATION, ModelTealive_Skeleton::createBodyLayer);
		event.registerLayerDefinition(ModelThe_Wither_Knight_Armor.LAYER_LOCATION, ModelThe_Wither_Knight_Armor::createBodyLayer);
		event.registerLayerDefinition(ModelThe_Wither_Knight_Armor_Rods.LAYER_LOCATION, ModelThe_Wither_Knight_Armor_Rods::createBodyLayer);
		event.registerLayerDefinition(ModelStreder.LAYER_LOCATION, ModelStreder::createBodyLayer);
		event.registerLayerDefinition(ModelChimeric_Redmarer_With_Saddle.LAYER_LOCATION, ModelChimeric_Redmarer_With_Saddle::createBodyLayer);
		event.registerLayerDefinition(Modelvoid_endermen.LAYER_LOCATION, Modelvoid_endermen::createBodyLayer);
		event.registerLayerDefinition(ModelDustyPinkMaxorFish.LAYER_LOCATION, ModelDustyPinkMaxorFish::createBodyLayer);
		event.registerLayerDefinition(ModelLava_Enderman.LAYER_LOCATION, ModelLava_Enderman::createBodyLayer);
		event.registerLayerDefinition(ModelWarrior_of_the_Chimeric_Darkness.LAYER_LOCATION, ModelWarrior_of_the_Chimeric_Darkness::createBodyLayer);
		event.registerLayerDefinition(Modelspirit_of_wolf.LAYER_LOCATION, Modelspirit_of_wolf::createBodyLayer);
		event.registerLayerDefinition(ModelTealivy_Void_Spear.LAYER_LOCATION, ModelTealivy_Void_Spear::createBodyLayer);
		event.registerLayerDefinition(ModelBlazer_Helmet.LAYER_LOCATION, ModelBlazer_Helmet::createBodyLayer);
		event.registerLayerDefinition(ModelCavern_Scarecrow.LAYER_LOCATION, ModelCavern_Scarecrow::createBodyLayer);
		event.registerLayerDefinition(ModelDark_Phantom.LAYER_LOCATION, ModelDark_Phantom::createBodyLayer);
		event.registerLayerDefinition(ModelTanzanite_Guardian.LAYER_LOCATION, ModelTanzanite_Guardian::createBodyLayer);
		event.registerLayerDefinition(ModelThe_Wither_Knight_Advanced.LAYER_LOCATION, ModelThe_Wither_Knight_Advanced::createBodyLayer);
		event.registerLayerDefinition(ModelSavage_Small_Blaze.LAYER_LOCATION, ModelSavage_Small_Blaze::createBodyLayer);
		event.registerLayerDefinition(ModelRed_Ravager.LAYER_LOCATION, ModelRed_Ravager::createBodyLayer);
		event.registerLayerDefinition(Modelmoon_fish.LAYER_LOCATION, Modelmoon_fish::createBodyLayer);
		event.registerLayerDefinition(Modelarmored_skeleton.LAYER_LOCATION, Modelarmored_skeleton::createBodyLayer);
		event.registerLayerDefinition(ModelRed_Armored_Blaze.LAYER_LOCATION, ModelRed_Armored_Blaze::createBodyLayer);
		event.registerLayerDefinition(ModelDark_hoglin.LAYER_LOCATION, ModelDark_hoglin::createBodyLayer);
		event.registerLayerDefinition(ModelGhast_Of_Tealive_Valley.LAYER_LOCATION, ModelGhast_Of_Tealive_Valley::createBodyLayer);
		event.registerLayerDefinition(ModelStreder_with_Saddle.LAYER_LOCATION, ModelStreder_with_Saddle::createBodyLayer);
		event.registerLayerDefinition(ModelChimeric_Redmarer.LAYER_LOCATION, ModelChimeric_Redmarer::createBodyLayer);
		event.registerLayerDefinition(ModelThe_Wither_Knight.LAYER_LOCATION, ModelThe_Wither_Knight::createBodyLayer);
		event.registerLayerDefinition(ModelBeaconOfWeapons.LAYER_LOCATION,ModelBeaconOfWeapons::createBodyLayer);
	}
}
