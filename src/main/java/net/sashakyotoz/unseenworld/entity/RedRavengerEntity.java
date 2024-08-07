
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.nbt.CompoundTag;
import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class RedRavengerEntity extends TamableAnimal implements RiderShieldingMount, Saddleable {
    private static final EntityDataAccessor<Boolean> DATA_IS_SADDLED = SynchedEntityData.defineId(RedRavengerEntity.class, EntityDataSerializers.BOOLEAN);

    public RedRavengerEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(UnseenWorldEntities.RED_RAVENGER.get(), world);
    }

    public RedRavengerEntity(EntityType<RedRavengerEntity> type, Level world) {
        super(type, world);
        setMaxUpStep(1.5f);
        xpReward = 0;
        setNoAi(false);
        setPersistenceRequired();
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_SADDLED, false);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1));
        this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 0.8));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2, true));
        this.targetSelector.addGoal(7, new HurtByTargetGoal(this).setAlertOthers());
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new FloatGoal(this));
    }

    @Override
    public MobType getMobType() {
        return MobType.ILLAGER;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }


    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.RAVAGER_STEP, 0.15f, 1);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.RAVAGER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.RAVAGER_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_FIRE))
            return false;
        if (source.is(DamageTypes.EXPLOSION))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.is(DamageTypes.WITHER_SKULL))
            return false;
        return super.hurt(source, amount);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        InteractionResult result = InteractionResult.sidedSuccess(this.level().isClientSide());
        Item item = itemstack.getItem();
        if (itemstack.getItem() instanceof SpawnEggItem) {
            result = super.mobInteract(player, hand);
        } else if (this.level().isClientSide()) {
            result = (this.isTame() && this.isOwnedBy(player) || this.isFood(itemstack)) ? InteractionResult.sidedSuccess(this.level().isClientSide()) : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                if (isSaddle(itemstack)) {
                    setSaddled(true);
                    player.getInventory().clearOrCountMatchingItems(p -> itemstack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
                }
                if (this.getPassengers().size() < 2 && !this.isBaby() && isSaddled()) {
                    player.startRiding(this);
                }
                if (this.isOwnedBy(player)) {
                    if (item.isEdible() && this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                        this.usePlayerItem(player, hand, itemstack);
                        this.heal((float) item.getFoodProperties().getNutrition());
                        result = InteractionResult.sidedSuccess(this.level().isClientSide());
                    } else if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                        this.usePlayerItem(player, hand, itemstack);
                        this.heal(4);
                        result = InteractionResult.sidedSuccess(this.level().isClientSide());
                    } else {
                        result = super.mobInteract(player, hand);
                    }
                }
            } else if (this.isFood(itemstack)) {
                this.usePlayerItem(player, hand, itemstack);
                if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
                this.setPersistenceRequired();
                result = InteractionResult.sidedSuccess(this.level().isClientSide());
            } else {
                result = super.mobInteract(player, hand);
                if (result == InteractionResult.SUCCESS || result == InteractionResult.CONSUME) {
                    this.setPersistenceRequired();
                }
            }
        }
        return result;
    }
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("serpentSaddled", isSaddled());
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        setSaddled(tag.getBoolean("serpentSaddled"));
        super.readAdditionalSaveData(tag);
    }

    private void clampRotation(Entity entity) {
        entity.setYBodyRot(this.getYRot());
        float f = entity.getYRot();
        float f1 = Mth.wrapDegrees(f - this.getYRot());
        float f2 = Mth.clamp(f1, -160.0F, 160.0F);
        entity.yRotO += f2 - f1;
        float f3 = f + f2 - f1;
        entity.setYRot(f3);
        entity.setYHeadRot(f3);
    }

    protected boolean canAddPassenger(Entity p_248594_) {
        return this.getPassengers().size() <= 2;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
        RedRavengerEntity entity = UnseenWorldEntities.RED_RAVENGER.get().create(serverWorld);
        entity.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.BREEDING, null, null);
        return entity;
    }
    protected float getSinglePassengerXOffset() {
        return 0.0F;
    }

    public void positionRider(@NotNull Entity entity,Entity.MoveFunction function) {
        if (this.hasPassenger(entity)) {
            float f = this.getSinglePassengerXOffset();
            float f1 = (float)((this.isRemoved() ? (double)0.01F : this.getPassengersRidingOffset()) + entity.getMyRidingOffset());
            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(entity);
                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }
                if (entity instanceof Animal) {
                    f += 0.2F;
                }
            }
            Vec3 vec3 = (new Vec3(f, 0.0D, 0.0D)).yRot(-this.getYRot() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
            entity.setPos(this.getX() + vec3.x, this.getY() + (double)f1 + 0.5, this.getZ() + vec3.z);
            this.clampRotation(entity);
            if (entity instanceof Animal && this.getPassengers().size() == 2) {
                int j = entity.getId() % 2 == 0 ? 90 : 270;
                entity.setYBodyRot(((Animal)entity).yBodyRot + (float)j);
                entity.setYHeadRot(entity.getYHeadRot() + (float)j);
            }

        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return List.of(UnseenWorldItems.LUMINOUS_PORKCHOP.get(), Items.BEEF, Items.MUTTON, Items.PORKCHOP).contains(stack.getItem());
    }


    public boolean isSaddle(ItemStack stack) {
        return Objects.equals(Items.SADDLE, stack.getItem());
    }

    @Override
    public void travel(Vec3 dir) {
        Entity entity = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        if (this.isVehicle()) {
            this.setYRot(entity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(entity.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = entity.getYRot();
            this.yHeadRot = entity.getYRot();
            if (entity instanceof LivingEntity passenger) {
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float forward = passenger.zza;
                float strafe = passenger.xxa;
                super.travel(new Vec3(strafe, 0, forward));
            }
            double d1 = this.getX() - this.xo;
            double d0 = this.getZ() - this.zo;
            float f1 = (float) Math.sqrt(d1 * d1 + d0 * d0) * 4;
            if (f1 > 1.0F)
                f1 = 1.0F;
            return;
        }
        super.travel(dir);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.15);
        builder = builder.add(Attributes.MAX_HEALTH, 36);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 5);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
        return builder;
    }

    @Override
    public double getRiderShieldingHeight() {
        return 0.5;
    }
    @Override
    public boolean isSaddleable() {
        return isAlive() && !isBaby();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource p_21748_) {
        setSaddled(true);
        if (p_21748_ != null) {
            this.level().playSound(null, this, SoundEvents.RAVAGER_STUNNED, p_21748_, 0.5F, 1.0F);
        }
    }
    @Override
    public boolean isSaddled() {
        return this.entityData.get(DATA_IS_SADDLED);
    }

    public void setSaddled(boolean b) {
        if (isSaddleable())
            this.entityData.set(DATA_IS_SADDLED, b);
    }
}
