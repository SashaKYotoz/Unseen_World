package net.sashakyotoz.common.entities.bosses;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import net.sashakyotoz.common.entities.bosses.parts.BossPart;

public abstract class MultipartBoss<S extends Enum<S> & BossState> extends StateMachineBoss<S> implements MultipartEntity {
    private static final float WEAK_POINT_MULTIPLIER = 1.5F;

    private final BossPart[] parts;
    private final PartLayout[] layout;

    protected MultipartBoss(EntityType<? extends BossLikePathfinderMob> entityType, Level level, Class<S> stateClass, PartLayout... layout) {
        super(entityType, level, stateClass);
        this.layout = layout;
        this.parts = new BossPart[layout.length];
        for (int i = 0; i < layout.length; i++) {
            this.parts[i] = new BossPart(this, layout[i].name(), layout[i].width(), layout[i].height());
        }
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        if (this.parts != null) {
            for (int i = 0; i < this.parts.length; i++) {
                this.parts[i].setId(id + i + 1);
            }
        }
    }

    @Override
    public EntityPart[] getMultipartParts() {
        return this.parts;
    }

    public BossPart[] getBodyParts() {
        return this.parts;
    }

    public boolean hurtFromPart(BossPart part, DamageSource source, float amount) {
        int index = -1;
        for (int i = 0; i < this.parts.length; i++) {
            if (this.parts[i] == part) {
                index = i;
                break;
            }
        }
        float scaled = index >= 0 && this.layout[index].weakPoint() ? amount * WEAK_POINT_MULTIPLIER : amount;
        return this.hurt(source, scaled);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        double forwardX = -Mth.sin(yaw);
        double forwardZ = Mth.cos(yaw);
        for (int i = 0; i < this.parts.length; i++) {
            BossPart part = this.parts[i];
            PartLayout spec = this.layout[i];
            double oldX = part.getX();
            double oldY = part.getY();
            double oldZ = part.getZ();
            part.setPos(this.getX() + forwardX * spec.forward(), this.getY() + spec.up(), this.getZ() + forwardZ * spec.forward());
            part.xo = oldX;
            part.yo = oldY;
            part.zo = oldZ;
            part.xOld = oldX;
            part.yOld = oldY;
            part.zOld = oldZ;
        }
    }

    public record PartLayout(String name, float width, float height, double forward, double up, boolean weakPoint) {
    }
}