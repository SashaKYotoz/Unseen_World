
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.sashakyotoz.unseenworld.client.model.Modelvoid_endermen;
import net.sashakyotoz.unseenworld.client.model.Modelthe_blazer;
import net.sashakyotoz.unseenworld.client.model.Modelspirit_of_wolf;
import net.sashakyotoz.unseenworld.client.model.Modelmoon_fish;
import net.sashakyotoz.unseenworld.client.model.Modelarmored_skeleton;
import net.sashakyotoz.unseenworld.client.model.Modelamethyst_golem;
import net.sashakyotoz.unseenworld.client.model.ModelWarrior_of_the_Chimeric_Darkness;
import net.sashakyotoz.unseenworld.client.model.ModelVoid_Arrow;
import net.sashakyotoz.unseenworld.client.model.ModelThe_Wither_Knight_Armor;
import net.sashakyotoz.unseenworld.client.model.ModelThe_Wither_Knight_Advanced;
import net.sashakyotoz.unseenworld.client.model.ModelThe_Wither_Knight;
import net.sashakyotoz.unseenworld.client.model.ModelTealivy_Void_Spear;
import net.sashakyotoz.unseenworld.client.model.ModelTealive_Skeleton;
import net.sashakyotoz.unseenworld.client.model.ModelTanzanite_Guardian;
import net.sashakyotoz.unseenworld.client.model.ModelStreder_with_Saddle;
import net.sashakyotoz.unseenworld.client.model.ModelStreder;
import net.sashakyotoz.unseenworld.client.model.ModelSavage_Small_Blaze;
import net.sashakyotoz.unseenworld.client.model.ModelRed_Sylph;
import net.sashakyotoz.unseenworld.client.model.ModelRed_Ravager;
import net.sashakyotoz.unseenworld.client.model.ModelRed_Armored_Blaze;
import net.sashakyotoz.unseenworld.client.model.ModelLava_Enderman;
import net.sashakyotoz.unseenworld.client.model.ModelGhast_Of_Tealive_Valley;
import net.sashakyotoz.unseenworld.client.model.ModelDustyPinkMaxorFish;
import net.sashakyotoz.unseenworld.client.model.ModelDark_hoglin;
import net.sashakyotoz.unseenworld.client.model.ModelDark_Phantom;
import net.sashakyotoz.unseenworld.client.model.ModelChimeric_Redmarer_With_Saddle;
import net.sashakyotoz.unseenworld.client.model.ModelChimeric_Redmarer;
import net.sashakyotoz.unseenworld.client.model.ModelCavern_Scarecrow;
import net.sashakyotoz.unseenworld.client.model.ModelBlazer_Helmet;

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
	}
}
