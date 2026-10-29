package net.sashakyotoz.common.entities.bosses.parts;

import net.minecraft.world.damagesource.DamageSource;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.common.entities.bosses.MultipartBoss;

public class BossPart extends EntityPart {
    public final MultipartBoss<?> owner;
    public final String name;

    public BossPart(MultipartBoss<?> owner, String name, float width, float height) {
        super(owner, width, height);
        this.refreshDimensions();
        this.owner = owner;
        this.name = name;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !this.isInvulnerableTo(source) && this.owner.hurtFromPart(this, source, amount);
    }
}
