
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.phys.BlockHitResult;
import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;

import net.sashakyotoz.unseenworld.managers.FireLikeStaffProjectileFlyingTick;
import net.sashakyotoz.unseenworld.registries.UnseenWorldParticleTypes;
import net.sashakyotoz.unseenworld.registries.UnseenWorldSounds;
@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class TealivyFireStaffEntity extends AbstractArrow implements ItemSupplier{
    private static final EntityDataAccessor<Boolean> IS_MAIN_SHARD = SynchedEntityData.defineId(TealivyFireStaffEntity.class, EntityDataSerializers.BOOLEAN);

    public TealivyFireStaffEntity(EntityType<? extends TealivyFireStaffEntity> type, Level world) {
        super(UnseenWorldEntities.TEALIVY_FIRE_STAFF.get(), world);
    }

    public TealivyFireStaffEntity(EntityType<? extends TealivyFireStaffEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(IS_MAIN_SHARD, false);
        super.defineSynchedData();
    }

    private void setIsMainShard(boolean isMainShard) {
        entityData.set(IS_MAIN_SHARD, isMainShard);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(UnseenWorldItems.TEALIVE_STONY_SHARD.get());
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }
    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!this.level().isClientSide())
            this.level().explode(null, blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), 2, Level.ExplosionInteraction.BLOCK);
        this.level().addParticle(UnseenWorldParticleTypes.BLUE_VOID_PARTICLE.get(), blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), 0, 1, 0);
    }

    @Override
    public void tick() {
        super.tick();
        FireLikeStaffProjectileFlyingTick.onTickParticles(UnseenWorldParticleTypes.BLUE_VOID_PARTICLE.get(),this.level(), this.getX(), this.getY(), this.getZ(),0.5f,0,1f);
        if (this.inGround) {
            if (this.entityData.get(IS_MAIN_SHARD)) {
                for (int i = 0; i < 2; i++) {
                    if (this.random.nextBoolean())
                        this.setYRot(this.getYRot() + this.random.nextIntBetweenInclusive(45, 90));
                    else
                        this.setYRot(this.getYRot() + this.random.nextIntBetweenInclusive(270, 315));
                    this.setXRot(0);
                    this.setYHeadRot(this.getYRot());
                    this.yRotO = this.getYRot();
                    this.xRotO = this.getXRot();
                    if(!this.level().isClientSide() && this.getOwner() != null)
                        this.shootSmallShard(new BlockPos((int) this.getX() + random.nextIntBetweenInclusive(-5, 5), (int) this.getY() + 1, (int) this.getZ() + random.nextIntBetweenInclusive(-5, 5)));
                }
            }
            this.discard();
        }
    }

    public static TealivyFireStaffEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        TealivyFireStaffEntity entityarrow = new TealivyFireStaffEntity(UnseenWorldEntities.TEALIVY_FIRE_STAFF.get(), entity, world);
        entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
        entityarrow.setSilent(true);
        entityarrow.setCritArrow(false);
        entityarrow.setOwner(entity);
        entityarrow.setBaseDamage(damage);
        entityarrow.setKnockback(knockback);
        entityarrow.setSecondsOnFire(100);
        entityarrow.setIsMainShard(true);
        world.addFreshEntity(entityarrow);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), UnseenWorldSounds.ITEM_STAFF_SHOT, SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
        return entityarrow;
    }

    public static TealivyFireStaffEntity shoot(LivingEntity entity, LivingEntity target) {
        TealivyFireStaffEntity entityarrow = new TealivyFireStaffEntity(UnseenWorldEntities.TEALIVY_FIRE_STAFF.get(), entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 2.5f * 2, 12.0F);
        entityarrow.setSilent(true);
        entityarrow.setBaseDamage(4);
        entityarrow.setKnockback(2);
        entityarrow.setCritArrow(false);
        entityarrow.setSecondsOnFire(100);
        entityarrow.setIsMainShard(true);
        entity.level().addFreshEntity(entityarrow);
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), UnseenWorldSounds.ITEM_STAFF_SHOT, SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
        return entityarrow;
    }

    private TealivyFireStaffEntity shootSmallShard(BlockPos targetPos) {
        TealivyFireStaffEntity staffEntity = new TealivyFireStaffEntity(UnseenWorldEntities.TEALIVY_FIRE_STAFF.get(), (LivingEntity) this.getOwner(), this.level());
        double dx = targetPos.getX() - this.getX();
        double dy = targetPos.getY() + targetPos.getY() - 1.1;
        double dz = targetPos.getZ() - this.getZ();
        staffEntity.shoot(dx, dy - staffEntity.getY() + Math.hypot(dx, dz) * 0.2F, dz, 4, 10.0F);
        staffEntity.setSilent(false);
        staffEntity.setBaseDamage(4);
        staffEntity.setKnockback(2);
        staffEntity.setCritArrow(false);
        staffEntity.setSecondsOnFire(80);
        staffEntity.setIsMainShard(false);
        this.level().addFreshEntity(staffEntity);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), UnseenWorldSounds.ITEM_STAFF_SHOT, SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
        return staffEntity;
    }

    @Override
    public ItemStack getItem() {
        return UnseenWorldItems.TANZANITE_SHARD.get().getDefaultInstance();
    }
}
