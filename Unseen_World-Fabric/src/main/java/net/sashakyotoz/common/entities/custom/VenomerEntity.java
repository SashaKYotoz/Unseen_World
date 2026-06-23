package net.sashakyotoz.common.entities.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.sashakyotoz.common.entities.ai.goals.spells.BlindTargetGoal;
import net.sashakyotoz.common.entities.ai.goals.spells.ConjureBoltsGoal;
import net.sashakyotoz.common.entities.ai.goals.spells.InvisibilityGoal;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class VenomerEntity extends ViolegerEntity {
    private static final TrackedData<Byte> SPELL = DataTracker.registerData(VenomerEntity.class, TrackedDataHandlerRegistry.BYTE);
    protected int spellTicks;
    private Spell spell = Spell.NONE;

    public VenomerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
                .add(EntityAttributes.GENERIC_ARMOR, 4)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new LookAtTargetGoal(this));
        this.goalSelector.add(3, new ConjureBoltsGoal(this));
        this.goalSelector.add(4, new BlindTargetGoal(this));
        this.goalSelector.add(5, new InvisibilityGoal(this));
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.add(1, new RevengeGoal(this, ViolegerEntity.class).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true).setMaxTimeWithoutVisibility(300));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, false).setMaxTimeWithoutVisibility(300));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, false).setMaxTimeWithoutVisibility(300));
    }

    public void flee(int hDistanceOfFlee){
        if (this.getTarget() != null) {
            Vec3d vec3d = NoPenaltyTargeting.findFrom(this, hDistanceOfFlee, 7, this.getTarget().getPos());
            if (vec3d != null)
                this.getNavigation().startMovingAlong(this.getNavigation().findPathTo(vec3d.x, vec3d.y, vec3d.z, 0), 1.5f);
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SPELL, (byte) 0);
    }

    @Override
    public State getState() {
        if (this.isSpellcasting()) {
            return State.SPELLCASTING;
        } else {
            return this.isCelebrating() ? State.CELEBRATING : State.CROSSED;
        }
    }

    public boolean isSpellcasting() {
        return this.getWorld().isClient ? this.dataTracker.get(SPELL) > 0 : this.spellTicks > 0;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
        this.dataTracker.set(SPELL, (byte) spell.id);
    }

    protected Spell getSpell() {
        return !this.getWorld().isClient ? this.spell : Spell.byId(this.dataTracker.get(SPELL));
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (this.spellTicks > 0) {
            this.spellTicks--;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient && this.isSpellcasting()) {
            Spell spell = this.getSpell();
            double d = spell.particleVelocity[0];
            double e = spell.particleVelocity[1];
            double f = spell.particleVelocity[2];
            float g = this.bodyYaw * (float) (Math.PI / 180.0) + MathHelper.cos((float) this.age * 0.6662F) * 0.25F;
            float h = MathHelper.cos(g);
            float i = MathHelper.sin(g);
            this.getWorld().addParticle(ParticleTypes.LANDING_OBSIDIAN_TEAR, this.getX() + (double) h * 0.6, this.getY() + 1.8, this.getZ() + (double) i * 0.6, d, e, f);
            this.getWorld().addParticle(ParticleTypes.LANDING_OBSIDIAN_TEAR, this.getX() - (double) h * 0.6, this.getY() + 1.8, this.getZ() - (double) i * 0.6, d, e, f);
        }
    }

    protected int getSpellTicks() {
        return this.spellTicks;
    }

    protected SoundEvent getCastSpellSound() {
        return SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL;
    }

    public abstract static class CastSpellGoal extends Goal {
        protected int spellCooldown;
        protected int startTime;
        protected VenomerEntity venomer;

        public CastSpellGoal(VenomerEntity venomerEntity) {
            this.venomer = venomerEntity;
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = venomer.getTarget();
            if (livingEntity == null || !livingEntity.isAlive()) {
                return false;
            } else {
                return !venomer.isSpellcasting() && venomer.age >= this.startTime;
            }
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity livingEntity = venomer.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.spellCooldown > 0;
        }

        @Override
        public void start() {
            this.spellCooldown = this.getTickCount(this.getInitialCooldown());
            venomer.spellTicks = this.getSpellTicks();
            this.startTime = venomer.age + this.startTimeDelay();
            SoundEvent soundEvent = this.getSoundPrepare();
            if (soundEvent != null) {
                venomer.playSound(soundEvent, 1.0F, 1.0F);
            }

            venomer.setSpell(this.getSpell());
        }

        @Override
        public void tick() {
            this.spellCooldown--;
            if (this.spellCooldown == 0) {
                this.castSpell();
                venomer.playSound(venomer.getCastSpellSound(), 0.9F, 1.5F);
            }
        }

        protected abstract void castSpell();

        protected int getInitialCooldown() {
            return 30;
        }

        protected abstract int getSpellTicks();

        protected abstract int startTimeDelay();

        @Nullable
        protected abstract SoundEvent getSoundPrepare();

        protected abstract VenomerEntity.Spell getSpell();
    }

    public static class LookAtTargetGoal extends Goal {
        protected VenomerEntity venomer;

        public LookAtTargetGoal(VenomerEntity venomerEntity) {
            this.venomer = venomerEntity;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }


        @Override
        public boolean canStart() {
            return venomer.getSpellTicks() > 0;
        }

        @Override
        public void start() {
            super.start();
            venomer.navigation.stop();
        }

        @Override
        public void stop() {
            super.stop();
            venomer.setSpell(VenomerEntity.Spell.NONE);
        }

        @Override
        public void tick() {
            if (venomer.getTarget() != null) {
                venomer.getLookControl()
                        .lookAt(
                                venomer.getTarget(),
                                (float) venomer.getMaxHeadRotation(),
                                (float) venomer.getMaxLookPitchChange()
                        );
            }
        }
    }

    public enum Spell {
        NONE(0, 0.0, 0.0, 0.0),
        GRIPPING_BOLTS(1, 0.7, 0.7, 0.8),
        DISAPPEAR(2, 0.3, 0.3, 0.8),
        BLINDNESS(3, 0.1, 0.1, 0.2);

        private static final IntFunction<Spell> BY_ID = ValueLists.createIdToValueFunction((ToIntFunction<Spell>) spell -> spell.id, values(), ValueLists.OutOfBoundsHandling.ZERO);
        final int id;
        final double[] particleVelocity;

        Spell(int id, double particleVelocityX, double particleVelocityY, double particleVelocityZ) {
            this.id = id;
            this.particleVelocity = new double[]{particleVelocityX, particleVelocityY, particleVelocityZ};
        }

        public static Spell byId(int id) {
            return BY_ID.apply(id);
        }
    }
}