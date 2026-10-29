package net.sashakyotoz.compat.wthit;

import mcp.mobius.waila.api.*;
import net.minecraft.world.entity.LivingEntity;
import net.sashakyotoz.common.blocks.custom.BurlywoodHeartBlock;
import net.sashakyotoz.common.blocks.custom.EffigyBlock;
import net.sashakyotoz.common.blocks.custom.EffigyPartBlock;
import net.sashakyotoz.common.blocks.custom.OrdealSpawnerBlock;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.blocks.custom.plants.HangingFruitBlock;
import net.sashakyotoz.common.blocks.custom.plants.TreeBushLikeBlock;
import net.sashakyotoz.compat.UWTooltips;

public class UWWthitPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar registrar) {
        registrar.addBlockData(OrdealData.INSTANCE, OrdealSpawnerBlockEntity.class);
        registrar.addComponent(Effigy.INSTANCE, TooltipPosition.BODY, EffigyBlock.class);
        registrar.addComponent(Effigy.INSTANCE, TooltipPosition.BODY, EffigyPartBlock.class);
        registrar.addComponent(Ordeal.INSTANCE, TooltipPosition.BODY, OrdealSpawnerBlock.class);
        registrar.addComponent(Fruit.INSTANCE, TooltipPosition.BODY, HangingFruitBlock.class);
        registrar.addComponent(Fruit.INSTANCE, TooltipPosition.BODY, TreeBushLikeBlock.class);
        registrar.addComponent(Heart.INSTANCE, TooltipPosition.BODY, BurlywoodHeartBlock.class);
        registrar.addComponent(Gripping.INSTANCE, TooltipPosition.BODY, LivingEntity.class);
    }

    enum OrdealData implements IDataProvider<OrdealSpawnerBlockEntity> {
        INSTANCE;

        @Override
        public void appendData(IDataWriter data, IServerAccessor<OrdealSpawnerBlockEntity> accessor, IPluginConfig config) {
            UWTooltips.writeOrdealData(accessor.getTarget(), data.raw());
        }
    }

    enum Effigy implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            UWTooltips.effigy(accessor.getWorld(), accessor.getPosition(), accessor.getBlockState(), tooltip::addLine);
        }
    }

    enum Ordeal implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            UWTooltips.ordealSpawner(accessor.getBlockState(), accessor.getData().raw(), tooltip::addLine);
        }
    }

    enum Fruit implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            UWTooltips.fruit(accessor.getBlockState(), tooltip::addLine);
        }
    }

    enum Heart implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            UWTooltips.burlywoodHeart(accessor.getBlockState(), tooltip::addLine);
        }
    }

    enum Gripping implements IEntityComponentProvider {
        INSTANCE;

        @Override
        public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
            UWTooltips.gripping(accessor.getEntity(), tooltip::addLine);
        }
    }
}
