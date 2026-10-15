package net.sashakyotoz.unseenworld.item;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.sashakyotoz.anitexlib.api.common.item.IEntityTickItem;

public class ParticleAnimatedItem extends Item implements IEntityTickItem {
    private final RandomSource random = RandomSource.create();
    private final ParticleOptions particleOptions;

    public ParticleAnimatedItem(Properties properties, ParticleOptions particle) {
        super(properties);
        this.particleOptions = particle;
    }

    @Override
    public void onTick(Level level, ItemEntity entity) {
        if (random.nextFloat() < 0.1 && level.isClientSide())
            level.addParticle(particleOptions, entity.getX(), entity.getY() + 0.5 + (random.nextDouble() - 0.5) * 0.25, entity.getZ(), 1.0, 1.0, 1.0);
    }
}
