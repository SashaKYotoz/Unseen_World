package net.sashakyotoz.compat.jade;

import net.minecraft.world.entity.LivingEntity;
import net.sashakyotoz.common.blocks.custom.BurlywoodHeartBlock;
import net.sashakyotoz.common.blocks.custom.EffigyBlock;
import net.sashakyotoz.common.blocks.custom.EffigyPartBlock;
import net.sashakyotoz.common.blocks.custom.OrdealSpawnerBlock;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.blocks.custom.plants.HangingFruitBlock;
import net.sashakyotoz.common.blocks.custom.plants.TreeBushLikeBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class UWJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(JadeProviders.Ordeal.INSTANCE, OrdealSpawnerBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(JadeProviders.Effigy.INSTANCE, EffigyBlock.class);
        registration.registerBlockComponent(JadeProviders.Effigy.INSTANCE, EffigyPartBlock.class);
        registration.registerBlockComponent(JadeProviders.Ordeal.INSTANCE, OrdealSpawnerBlock.class);
        registration.registerBlockComponent(JadeProviders.Fruit.INSTANCE, HangingFruitBlock.class);
        registration.registerBlockComponent(JadeProviders.Fruit.INSTANCE, TreeBushLikeBlock.class);
        registration.registerBlockComponent(JadeProviders.Heart.INSTANCE, BurlywoodHeartBlock.class);
        registration.registerEntityComponent(JadeProviders.Gripping.INSTANCE, LivingEntity.class);
    }
}