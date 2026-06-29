package net.sashakyotoz.common.entities.bosses.parts;

import net.minecraft.world.damagesource.DamageSource;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;

public class EclipseSentinelPartEntity extends EntityPart {
    public final EclipseSentinel owner;
    public final String name;

    public EclipseSentinelPartEntity(EclipseSentinel owner, String name, float width, float height) {
        super(owner, width, height);
        this.refreshDimensions();
        this.owner = owner;
        this.name = name;
    }


    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.random.nextInt(3) == 1)
            this.owner.setPhase();
        return this.owner.hurt(this.owner.damageSources().generic(), amount);
    }
}