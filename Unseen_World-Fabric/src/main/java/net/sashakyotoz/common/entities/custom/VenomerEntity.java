package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.ai.goals.spells.BlindTargetGoal;
import net.sashakyotoz.common.entities.ai.goals.spells.ConjureBoltsGoal;
import net.sashakyotoz.common.entities.ai.goals.spells.InvisibilityGoal;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class VenomerEntity extends ViolegerEntity {
    private static final EntityDataAccessor<Byte> SPELL = SynchedEntityData.defineId(VenomerEntity.class, EntityDataSerializers.BYTE);
    protected int spellTicks;
    private Spell spell = Spell.NONE;

    public VenomerEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.ATTACK_DAMAGE, 4)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(3, new ConjureBoltsGoal(this));
        this.goalSelector.addGoal(4, new BlindTargetGoal(this));
        this.goalSelector.addGoal(5, new InvisibilityGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, ViolegerEntity.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false).setUnseenMemoryTicks(300));
    }

    public void flee(int hDistanceOfFlee){
        if (this.getTarget() != null) {
            Vec3 vec3d = DefaultRandomPos.getPosAway(this, hDistanceOfFlee, 7, this.getTarget().position());
            if (vec3d != null)
                this.getNavigation().moveTo(this.getNavigation().createPath(vec3d.x, vec3d.y, vec3d.z, 0), 1.5f);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPELL, (byte) 0);
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
        return this.level().isClientSide ? this.entityData.get(SPELL) > 0 : this.spellTicks > 0;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
        this.entityData.set(SPELL, (byte) spell.id);
    }

    protected Spell getSpell() {
        return !this.level().isClientSide ? this.spell : Spell.byId(this.entityData.get(SPELL));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spellTicks > 0) {
            this.spellTicks--;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.isSpellcasting()) {
            Spell spell = this.getSpell();
            double d = spell.particleVelocity[0];
            double e = spell.particleVelocity[1];
            double f = spell.particleVelocity[2];
            float g = this.yBodyRot * (float) (Math.PI / 180.0) + Mth.cos((float) this.tickCount * 0.6662F) * 0.25F;
            float h = Mth.cos(g);
            float i = Mth.sin(g);
            this.level().addParticle(ParticleTypes.LANDING_OBSIDIAN_TEAR, this.getX() + (double) h * 0.6, this.getY() + 1.8, this.getZ() + (double) i * 0.6, d, e, f);
            this.level().addParticle(ParticleTypes.LANDING_OBSIDIAN_TEAR, this.getX() - (double) h * 0.6, this.getY() + 1.8, this.getZ() - (double) i * 0.6, d, e, f);
        }
    }

    protected int getSpellTicks() {
        return this.spellTicks;
    }

    protected SoundEvent getCastSpellSound() {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
    }

    public abstract static class CastSpellGoal extends Goal {
        protected int spellCooldown;
        protected int startTime;
        protected VenomerEntity venomer;

        public CastSpellGoal(VenomerEntity venomerEntity) {
            this.venomer = venomerEntity;
        }

        @Override
        public boolean canUse() {
            LivingEntity livingEntity = venomer.getTarget();
            if (livingEntity == null || !livingEntity.isAlive()) {
                return false;
            } else {
                return !venomer.isSpellcasting() && venomer.tickCount >= this.startTime;
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = venomer.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.spellCooldown > 0;
        }

        @Override
        public void start() {
            this.spellCooldown = this.adjustedTickDelay(this.getInitialCooldown());
            venomer.spellTicks = this.getSpellTicks();
            this.startTime = venomer.tickCount + this.startTimeDelay();
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
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }


        @Override
        public boolean canUse() {
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
                        .setLookAt(
                                venomer.getTarget(),
                                (float) venomer.getMaxHeadYRot(),
                                (float) venomer.getMaxHeadXRot()
                        );
            }
        }
    }

    public enum Spell {
        NONE(0, 0.0, 0.0, 0.0),
        GRIPPING_BOLTS(1, 0.7, 0.7, 0.8),
        DISAPPEAR(2, 0.3, 0.3, 0.8),
        BLINDNESS(3, 0.1, 0.1, 0.2);

        private static final IntFunction<Spell> BY_ID = ByIdMap.continuous((ToIntFunction<Spell>) spell -> spell.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
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