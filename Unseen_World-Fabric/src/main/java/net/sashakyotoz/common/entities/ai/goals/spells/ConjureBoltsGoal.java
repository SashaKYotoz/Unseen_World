package net.sashakyotoz.common.entities.ai.goals.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.custom.VenomerEntity;
import net.sashakyotoz.common.entities.custom.ViolegerEntity;
import net.sashakyotoz.common.entities.custom.projectiles.GrippingCrystalProjectileEntity;

public class ConjureBoltsGoal extends VenomerEntity.CastSpellGoal {

    public ConjureBoltsGoal(VenomerEntity venomerEntity) {
        super(venomerEntity);
    }

    private final TargetPredicate closeBoltPredicate = TargetPredicate.createNonAttackable()
            .setBaseMaxDistance(16.0)
            .ignoreVisibility()
            .ignoreDistanceScalingFactor();

    @Override
    public boolean canStart() {
        if (!super.canStart())
            return false;
        else if (venomer.hasStatusEffect(StatusEffects.INVISIBILITY))
            return false;
        else {
            int i = venomer.getWorld()
                    .getTargets(ViolegerEntity.class, this.closeBoltPredicate, venomer, venomer.getBoundingBox().expand(16.0))
                    .size();
            return venomer.getRandom().nextInt(8) + 1 > i;
        }
    }

    @Override
    protected int getSpellTicks() {
        return 100;
    }

    @Override
    protected int startTimeDelay() {
        return 200;
    }

    @Override
    protected int getInitialCooldown() {
        return 60;
    }

    @Override
    protected void castSpell() {
        ServerWorld serverWorld = (ServerWorld) venomer.getWorld();
        LivingEntity target = venomer.getTarget();
        if (target != null) {

            for (int i = 0; i < 4; i++) {
                double spawnX = venomer.getX() + (-2.0 + venomer.getRandom().nextDouble() * 4.0);
                double spawnY = venomer.getY() + 1.0;
                double spawnZ = venomer.getZ() + (-2.0 + venomer.getRandom().nextDouble() * 4.0);

                GrippingCrystalProjectileEntity projectile = ModEntities.GRIPPING_CRYSTAL_PROJECTILE.create(serverWorld);

                if (projectile != null) {
                    projectile.setPosition(spawnX, spawnY, spawnZ);
                    projectile.setOwner(venomer);

                    double dX = target.getX() - spawnX;
                    double dY = target.getBodyY(0.5) - spawnY;
                    double dZ = target.getZ() - spawnZ;

                    float speed = 1.0F;
                    float divergence = 1.0F;
                    projectile.setVelocity(dX, dY, dZ, speed, divergence);

                    serverWorld.spawnEntityAndPassengers(projectile);
                }
            }
            venomer.flee(24);
        }
    }

    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
    }

    @Override
    protected VenomerEntity.Spell getSpell() {
        return VenomerEntity.Spell.GRIPPING_BOLTS;
    }
}