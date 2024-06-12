package net.sashakyotoz.unseenworld.item;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.sashakyotoz.anitexlib.client.renderer.IParticleItem;

public class ParticleAnimatedItem extends Item implements IParticleItem {
    private final ParticleOptions particleOptions;
    public ParticleAnimatedItem(Properties properties,ParticleOptions particle) {
        super(properties);
        this.particleOptions = particle;
    }

    @Override
    public void addParticles(Level level, ItemEntity entity) {
        RandomSource random = RandomSource.create();
        if (random.nextFloat() < 0.1)
            level.addParticle(particleOptions,entity.getX() + (random.nextDouble() - 0.5) * 0.25, entity.getY() + 0.5 + (random.nextDouble() - 0.5) * 0.25, entity.getZ() + (random.nextDouble() - 0.5) * 0.25, 1.0, 1.0, 1.0);
    }
}
