package net.sashakyotoz.common.entities.bosses.parts;

import net.minecraft.entity.damage.DamageSource;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;

public class WarriorPartEntity extends EntityPart {
    public final WarriorOfChimericDarkness owner;
    public final String name;

    public WarriorPartEntity(WarriorOfChimericDarkness owner, String name, float width, float height) {
        super(owner, width, height);
        this.calculateDimensions();
        this.owner = owner;
        this.name = name;
    }


    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.random.nextInt(3) == 1)
            this.owner.setPhase();
        return this.owner.damage(this.owner.getDamageSources().generic(), amount);
    }
}