
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.*;
import org.jetbrains.annotations.NotNull;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class VoidStaffEntity extends AbstractArrow implements ItemSupplier {

    public VoidStaffEntity(EntityType<? extends VoidStaffEntity> type, Level world) {
        super(UnseenWorldEntities.VOID_STAFF.get(), world);
    }

    public VoidStaffEntity(EntityType<? extends VoidStaffEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }

    @Override
    public void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (this.getOwner() != null && this.getOwner() instanceof Player player) {
            if (this.level().isClientSide())
                this.level().addParticle(UnseenWorldParticleTypes.VOID_PORTAL.get(), player.getX(), player.getY() + 2.5, player.getZ(), 1, 1, 1);
            BlockPos hitPos = blockHitResult.getBlockPos().above();
            UnseenWorldMod.queueServerWork(20, () -> {
                float x = hitPos.getX();
                float y = hitPos.getY();
                float z = hitPos.getZ();
                player.teleportTo(x, y, z);
                this.level().addParticle(UnseenWorldParticleTypes.VOID_PORTAL.get(), x, y + 1.5, z, 1, 1, 1);
                if (player instanceof ServerPlayer serverPlayer)
                    serverPlayer.connection.teleport(x, y, z, player.getYRot(), player.getXRot());
            });
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround || this.level().getBlockState(this.getOnPos().below()).is(UnseenWorldBlocks.DARK_WATER.get()))
            this.discard();
    }

    public static VoidStaffEntity shoot(Level level, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        VoidStaffEntity staffEntity = new VoidStaffEntity(UnseenWorldEntities.VOID_STAFF.get(), entity, level);
        staffEntity.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2.5f, 0);
        staffEntity.setSilent(true);
        staffEntity.setCritArrow(false);
        staffEntity.setBaseDamage(damage);
        staffEntity.setKnockback(knockback);
        level.addFreshEntity(staffEntity);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), UnseenWorldSounds.ITEM_STAFF_SHOT, SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
        return staffEntity;
    }

    public static VoidStaffEntity shoot(LivingEntity entity, LivingEntity target) {
        VoidStaffEntity staffEntity = new VoidStaffEntity(UnseenWorldEntities.VOID_STAFF.get(), entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        staffEntity.shoot(dx, dy - staffEntity.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1.5f * 2, 12.0F);
        staffEntity.setSilent(true);
        staffEntity.setBaseDamage(4);
        staffEntity.setKnockback(2);
        staffEntity.setCritArrow(false);
        entity.level().addFreshEntity(staffEntity);
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), UnseenWorldSounds.ITEM_STAFF_SHOT, SoundSource.PLAYERS, 1,
                1f / (RandomSource.create().nextFloat() * 0.5f + 1));
        return staffEntity;
    }

    @Override
    public ItemStack getItem() {
        return UnseenWorldItems.DARK_PEARL.get().getDefaultInstance();
    }
}
