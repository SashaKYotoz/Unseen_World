package net.sashakyotoz.common.entities.ai.goals.spells;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.custom.VenomerEntity;
import net.sashakyotoz.common.entities.custom.ViolegerEntity;
import net.sashakyotoz.common.entities.custom.projectiles.GrippingCrystalProjectileEntity;

public class ConjureBoltsGoal extends VenomerEntity.CastSpellGoal {

    public ConjureBoltsGoal(VenomerEntity venomerEntity) {
        super(venomerEntity);
    }

    private final TargetingConditions closeBoltPredicate = TargetingConditions.forNonCombat()
            .range(16.0)
            .ignoreLineOfSight()
            .ignoreInvisibilityTesting();

    @Override
    public boolean canUse() {
        if (!super.canUse())
            return false;
        else if (venomer.hasEffect(MobEffects.INVISIBILITY))
            return false;
        else {
            int i = venomer.level()
                    .getNearbyEntities(ViolegerEntity.class, this.closeBoltPredicate, venomer, venomer.getBoundingBox().inflate(16.0))
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
        ServerLevel serverWorld = (ServerLevel) venomer.level();
        LivingEntity target = venomer.getTarget();
        if (target != null) {

            for (int i = 0; i < 4; i++) {
                double spawnX = venomer.getX() + (-2.0 + venomer.getRandom().nextDouble() * 4.0);
                double spawnY = venomer.getY() + 1.0;
                double spawnZ = venomer.getZ() + (-2.0 + venomer.getRandom().nextDouble() * 4.0);

                GrippingCrystalProjectileEntity projectile = ModEntities.GRIPPING_CRYSTAL_PROJECTILE.create(serverWorld);

                if (projectile != null) {
                    projectile.setPos(spawnX, spawnY, spawnZ);
                    projectile.setOwner(venomer);

                    double dX = target.getX() - spawnX;
                    double dY = target.getY(0.5) - spawnY;
                    double dZ = target.getZ() - spawnZ;

                    float speed = 1.0F;
                    float divergence = 1.0F;
                    projectile.shoot(dX, dY, dZ, speed, divergence);

                    serverWorld.addFreshEntityWithPassengers(projectile);
                }
            }
            venomer.flee(24);
        }
    }

    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    @Override
    protected VenomerEntity.Spell getSpell() {
        return VenomerEntity.Spell.GRIPPING_BOLTS;
    }
}