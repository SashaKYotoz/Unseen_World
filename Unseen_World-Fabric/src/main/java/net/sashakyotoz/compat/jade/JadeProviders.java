package net.sashakyotoz.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.compat.UWTooltips;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

public final class JadeProviders {
    private JadeProviders() {
    }

    public enum Effigy implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            UWTooltips.effigy(accessor.getLevel(), accessor.getPosition(), accessor.getBlockState(), tooltip::add);
        }

        @Override
        public ResourceLocation getUid() {
            return UWTooltips.EFFIGY;
        }
    }

    public enum Ordeal implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            UWTooltips.ordealSpawner(accessor.getBlockState(), accessor.getServerData(), tooltip::add);
        }

        @Override
        public void appendServerData(CompoundTag data, BlockAccessor accessor) {
            UWTooltips.writeOrdealData(accessor.getBlockEntity(), data);
        }

        @Override
        public ResourceLocation getUid() {
            return UWTooltips.ORDEAL_SPAWNER;
        }
    }

    public enum Fruit implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            UWTooltips.fruit(accessor.getBlockState(), tooltip::add);
        }

        @Override
        public ResourceLocation getUid() {
            return UWTooltips.FRUIT;
        }
    }

    public enum Heart implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            UWTooltips.burlywoodHeart(accessor.getBlockState(), tooltip::add);
        }

        @Override
        public ResourceLocation getUid() {
            return UWTooltips.BURLYWOOD_HEART;
        }
    }

    public enum Gripping implements IEntityComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
            UWTooltips.gripping(accessor.getEntity(), tooltip::add);
        }

        @Override
        public ResourceLocation getUid() {
            return UWTooltips.GRIPPING;
        }
    }
}