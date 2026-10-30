package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.ai.goals.GrippingAbyssalBowAttackGoal;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class ViolegerEntity extends Monster implements RangedAttackMob {
    public static final ResourceKey<LootTable> GRIPCRYSTAL_BARTER = ResourceKey.create(Registries.LOOT_TABLE, UnseenWorld.MOD.makeID("gameplay/violeger_barter_gripcrystal"));
    public static final ResourceKey<LootTable> GRIPTONITE_BARTER = ResourceKey.create(Registries.LOOT_TABLE, UnseenWorld.MOD.makeID("gameplay/violeger_barter_griptonite"));
    private static final EntityDataAccessor<Byte> ACTION = SynchedEntityData.defineId(ViolegerEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> IS_HOSTILE = SynchedEntityData.defineId(ViolegerEntity.class, EntityDataSerializers.BOOLEAN);

    private static final byte ACTION_NONE = 0;
    private static final byte ACTION_INSPECTING = 1;
    private static final byte ACTION_STEALING = 2;
    private static final byte ACTION_CELEBRATING = 3;
    private static final int INSPECT_TICKS = 60;
    private static final int CELEBRATE_TICKS = 20;
    private static final float GUARANTEED_DROP = 2.0F;
    private static final float DEFAULT_HAND_DROP = 0.085F;
    private static final double CUSTOMER_RANGE = 8.0D;

    private final GrippingAbyssalBowAttackGoal<ViolegerEntity> bowGoal = new GrippingAbyssalBowAttackGoal<>(this, 1.0D, 20, 16.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false);
    private boolean spawnerBorn;
    private boolean provoked;
    private int inspectTicks;
    private int celebrateTicks;
    private int stealCooldown;
    @Nullable
    private UUID customer;

    public ViolegerEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ARMOR, 8)
                .add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.ATTACK_DAMAGE, 4)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1)
                .add(Attributes.FOLLOW_RANGE, 24)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_HOSTILE, false);
        builder.define(ACTION, ACTION_NONE);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new InspectOfferGoal(this));
        this.goalSelector.addGoal(2, new BundleStealGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D, 0.25F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, player -> this.isHostile()));
    }

    private boolean isTrader() {
        return this.getType() == ModEntities.VIOLEGER;
    }

    public boolean isSpawnerBorn() {
        return this.spawnerBorn;
    }

    public boolean isHostile() {
        return this.entityData.get(IS_HOSTILE);
    }

    public void setIsHostile(boolean flag) {
        this.setProvoked(flag);
    }

    private void refreshHostility() {
        boolean hostile = !this.isTrader() || this.spawnerBorn || this.provoked;
        if (this.entityData.get(IS_HOSTILE) != hostile) {
            this.entityData.set(IS_HOSTILE, hostile);
        }
    }

    private void setProvoked(boolean flag) {
        if (this.provoked == flag) {
            return;
        }
        this.provoked = flag;
        this.refreshHostility();
        if (flag && this.level() instanceof ServerLevel serverLevel) {
            this.abortInspection();
            serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER, this.getX(), this.getEyeY() + 0.4D, this.getZ(), 4, 0.3D, 0.2D, 0.3D, 0.0D);
            this.playSound(SoundEvents.PILLAGER_AMBIENT, 1.2F, 0.7F);
        }
    }

    private void becomeSpawnerBorn() {
        this.spawnerBorn = true;
        this.abortInspection();
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GRIPPING_ABYSSAL_BOW));
        this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        this.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
        this.refreshHostility();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnData);
        if (this.isTrader()) {
            if (spawnType == MobSpawnType.SPAWNER) {
                this.becomeSpawnerBorn();
            } else {
                this.populateTraderEquipment(level.getRandom());
            }
        }
        this.refreshHostility();
        this.reassessWeaponGoal();
        return data;
    }

    private void populateTraderEquipment(RandomSource random) {
        this.setItemSlot(EquipmentSlot.MAINHAND, random.nextFloat() < 0.4F ? new ItemStack(Items.IRON_AXE) : ItemStack.EMPTY);
        this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        super.setItemSlot(slot, stack);
        if (slot == EquipmentSlot.MAINHAND && !this.level().isClientSide()) {
            this.reassessWeaponGoal();
        }
    }

    public void reassessWeaponGoal() {
        if (this.bowGoal == null || this.meleeGoal == null || this.level().isClientSide() || !this.isTrader()) {
            return;
        }
        this.goalSelector.removeGoal(this.meleeGoal);
        this.goalSelector.removeGoal(this.bowGoal);
        ItemStack weapon = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, ModItems.GRIPPING_ABYSSAL_BOW));
        if (weapon.is(ModItems.GRIPPING_ABYSSAL_BOW)) {
            this.bowGoal.setAttackInterval(this.level().getDifficulty() == Difficulty.HARD ? 20 : 40);
            this.goalSelector.addGoal(4, this.bowGoal);
        } else {
            this.goalSelector.addGoal(4, this.meleeGoal);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getOffhandItem().getItem() instanceof ShieldItem && this.getMainHandItem().getItem() instanceof SwordItem) {
            this.startUsingItem(InteractionHand.OFF_HAND);
        }
        if (this.getTarget() != null && !this.level().isClientSide()) {
            if (this.tickCount % 60 == 0 && this.getHealth() < this.getMaxHealth() && !this.hasEffect(MobEffects.REGENERATION)) {
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 1));
            }
        }
        this.setShiftKeyDown(this.isUsingItem());
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide() || !this.isTrader()) {
            return;
        }
        if (!this.spawnerBorn && this.getTags().contains(OrdealSpawnerBlockEntity.ORDEAL_TAG)) {
            this.becomeSpawnerBorn();
        }
        if (this.stealCooldown > 0) {
            this.stealCooldown--;
        }
        if (this.getAction() == ACTION_INSPECTING && (this.isHostile() || this.getTarget() != null)) {
            this.abortInspection();
        }
        if (this.getAction() == ACTION_CELEBRATING && --this.celebrateTicks <= 0) {
            this.setAction(ACTION_NONE);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide() && this.isTrader() && source.getEntity() instanceof Player player && !player.isCreative()) {
            this.setProvoked(true);
        }
        return result;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.isTrader() || !isCurrency(stack)) {
            return super.mobInteract(player, hand);
        }
        if (this.isHostile() || this.getTarget() != null) {
            this.playSound(SoundEvents.VILLAGER_NO, 1.0F, 0.7F);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        if (this.getAction() != ACTION_NONE && this.getAction() != ACTION_CELEBRATING) {
            return InteractionResult.PASS;
        }
        if (!this.level().isClientSide()) {
            ItemStack offer = stack.copyWithCount(1);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            this.setItemSlot(EquipmentSlot.OFFHAND, offer);
            this.setDropChance(EquipmentSlot.OFFHAND, GUARANTEED_DROP);
            this.customer = player.getUUID();
            this.inspectTicks = INSPECT_TICKS;
            this.getNavigation().stop();
            this.setAction(ACTION_INSPECTING);
            this.playSound(SoundEvents.VILLAGER_TRADE, 1.0F, 0.7F);
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide());
    }

    private static boolean isCurrency(ItemStack stack) {
        return stack.is(ModItems.GRIPCRYSTAL) || stack.is(ModItems.GRIPTONITE);
    }

    private void finishInspection() {
        ItemStack offer = this.getOffhandItem();
        this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        this.setDropChance(EquipmentSlot.OFFHAND, DEFAULT_HAND_DROP);
        ResourceKey<LootTable> table = offer.is(ModItems.GRIPTONITE) ? GRIPTONITE_BARTER : offer.is(ModItems.GRIPCRYSTAL) ? GRIPCRYSTAL_BARTER : null;
        if (table != null && this.level() instanceof ServerLevel serverLevel) {
            LootTable loot = serverLevel.getServer().reloadableRegistries().getLootTable(table);
            LootParams params = new LootParams.Builder(serverLevel).withParameter(LootContextParams.THIS_ENTITY, this).create(LootContextParamSets.PIGLIN_BARTER);
            Vec3 destination = this.customerPosition();
            for (ItemStack reward : loot.getRandomItems(params)) {
                BehaviorUtils.throwItem(this, reward, destination);
            }
            this.playSound(SoundEvents.VILLAGER_YES, 1.0F, 0.7F);
            this.celebrateTicks = CELEBRATE_TICKS;
            this.setAction(ACTION_CELEBRATING);
        } else {
            this.setAction(ACTION_NONE);
        }
        this.customer = null;
    }

    private void abortInspection() {
        if (this.getAction() != ACTION_INSPECTING) {
            return;
        }
        ItemStack offer = this.getOffhandItem();
        this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        this.setDropChance(EquipmentSlot.OFFHAND, DEFAULT_HAND_DROP);
        if (!offer.isEmpty()) {
            this.spawnAtLocation(offer, 0.5F);
        }
        this.customer = null;
        this.inspectTicks = 0;
        this.setAction(ACTION_NONE);
    }

    private Vec3 customerPosition() {
        if (this.customer != null) {
            Player player = this.level().getPlayerByUUID(this.customer);
            if (player != null && player.distanceToSqr(this) <= CUSTOMER_RANGE * CUSTOMER_RANGE) {
                return player.position();
            }
        }
        Player nearest = this.level().getNearestPlayer(this, CUSTOMER_RANGE);
        return nearest != null ? nearest.position() : this.position().add(this.getLookAngle().scale(2.0D));
    }

    @Nullable
    private Player customerEntity() {
        return this.customer == null ? null : this.level().getPlayerByUUID(this.customer);
    }

    private byte getAction() {
        return this.entityData.get(ACTION);
    }

    private void setAction(byte action) {
        this.entityData.set(ACTION, action);
    }

    public boolean isCelebrating() {
        return this.getAction() == ACTION_CELEBRATING;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, ModItems.GRIPPING_ABYSSAL_BOW)));
        AbstractArrow abstractArrow = ProjectileUtil.getMobArrow(this, itemstack, pullProgress, this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, ModItems.GRIPPING_ABYSSAL_BOW)));
        double d0 = target.getX() - this.getX();
        double d1 = target.getY() + 0.35F - abstractArrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractArrow.shoot(d0, d1 + d3 * 0.2D, d2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractArrow);
    }

    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return !this.isTrader() || this.spawnerBorn;
    }

    @Override
    public boolean isPreventingPlayerRest(Player player) {
        return this.isHostile();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return 0.75F + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("SpawnerBorn", this.spawnerBorn);
        compound.putBoolean("Provoked", this.provoked);
        compound.putInt("StealCooldown", this.stealCooldown);
        if (this.getAction() == ACTION_INSPECTING) {
            compound.putInt("InspectTicks", this.inspectTicks);
            if (this.customer != null) {
                compound.putUUID("Customer", this.customer);
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.spawnerBorn = compound.getBoolean("SpawnerBorn");
        this.provoked = compound.getBoolean("Provoked");
        this.stealCooldown = compound.getInt("StealCooldown");
        this.customer = compound.hasUUID("Customer") ? compound.getUUID("Customer") : null;
        if (this.isTrader() && !this.spawnerBorn && isCurrency(this.getOffhandItem())) {
            this.inspectTicks = compound.contains("InspectTicks") ? Math.max(1, compound.getInt("InspectTicks")) : 20;
            this.setDropChance(EquipmentSlot.OFFHAND, GUARANTEED_DROP);
            this.setAction(ACTION_INSPECTING);
        }
        this.refreshHostility();
        this.reassessWeaponGoal();
    }

    public State getState() {
        byte action = this.getAction();
        if (action == ACTION_INSPECTING) {
            return State.INSPECTING;
        }
        if (action == ACTION_STEALING) {
            return State.STEALING;
        }
        if (action == ACTION_CELEBRATING) {
            return State.CELEBRATING;
        }
        if (this.isAggressive() && this.isHolding(ModItems.GRIPPING_ABYSSAL_BOW)) {
            return State.BOW_AND_ARROW;
        }
        if (this.isAggressive()) {
            return State.ATTACKING;
        }
        return this.getMainHandItem().isEmpty() && this.getOffhandItem().isEmpty() ? State.CROSSED : State.NEUTRAL;
    }

    public enum State {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW,
        CELEBRATING,
        INSPECTING,
        STEALING,
        NEUTRAL
    }

    static class InspectOfferGoal extends Goal {
        private final ViolegerEntity violeger;

        InspectOfferGoal(ViolegerEntity violeger) {
            this.violeger = violeger;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.violeger.getAction() == ACTION_INSPECTING;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.violeger.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.violeger.getNavigation().stop();
            Player customer = this.violeger.customerEntity();
            if (customer != null) {
                this.violeger.getLookControl().setLookAt(customer, 10.0F, 10.0F);
            }
            if (--this.violeger.inspectTicks <= 0) {
                this.violeger.finishInspection();
            }
        }
    }

    static class BundleStealGoal extends Goal {
        private static final double RANGE_SQR = 36.0D;
        private static final int WINDUP_TICKS = 20;
        private static final int RECOVER_TICKS = 6;
        private static final int BASE_COOLDOWN = 240;
        private static final int COOLDOWN_SPREAD = 160;
        private static final int PICKUP_DELAY = 40;
        private static final double THROW_DISTANCE = 4.0D;

        private final ViolegerEntity violeger;
        private int timer;
        private boolean stole;

        BundleStealGoal(ViolegerEntity violeger) {
            this.violeger = violeger;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!this.violeger.spawnerBorn || this.violeger.stealCooldown > 0 || this.violeger.getAction() != ACTION_NONE) {
                return false;
            }
            return this.violeger.getTarget() instanceof Player player && this.isValid(player) && !stealableSlots(player.getInventory()).isEmpty();
        }

        private boolean isValid(Player player) {
            return player.isAlive() && !player.isCreative() && !player.isSpectator()
                    && this.violeger.distanceToSqr(player) <= RANGE_SQR && this.violeger.getSensing().hasLineOfSight(player);
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < WINDUP_TICKS + RECOVER_TICKS && this.violeger.getTarget() instanceof Player player && player.isAlive();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.timer = 0;
            this.stole = false;
            this.violeger.getNavigation().stop();
            this.violeger.stopUsingItem();
            this.violeger.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.GRIPPING_GAUNTLET));
            this.violeger.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
            this.violeger.setAction(ACTION_STEALING);
            this.violeger.playSound(SoundEvents.BUNDLE_INSERT, 1.0F, 0.6F);
        }

        @Override
        public void tick() {
            if (!(this.violeger.getTarget() instanceof Player player)) {
                return;
            }
            this.violeger.getNavigation().stop();
            this.violeger.getLookControl().setLookAt(player, 30.0F, 30.0F);
            this.timer++;
            if (this.timer == WINDUP_TICKS && this.isValid(player)) {
                this.stole = this.steal(player);
            }
        }

        @Override
        public void stop() {
            this.violeger.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            this.violeger.stealCooldown = BASE_COOLDOWN + this.violeger.getRandom().nextInt(COOLDOWN_SPREAD);
            if (this.stole) {
                this.violeger.celebrateTicks = CELEBRATE_TICKS;
                this.violeger.setAction(ACTION_CELEBRATING);
            } else {
                this.violeger.setAction(ACTION_NONE);
            }
        }

        private static List<Integer> stealableSlots(Inventory inventory) {
            List<Integer> slots = new ArrayList<>();
            for (int slot = 0; slot < inventory.items.size(); slot++) {
                if (!inventory.items.get(slot).isEmpty()) {
                    slots.add(slot);
                }
            }
            if (!inventory.offhand.get(0).isEmpty()) {
                slots.add(-1);
            }
            return slots;
        }

        private boolean steal(Player player) {
            Inventory inventory = player.getInventory();
            List<Integer> slots = stealableSlots(inventory);
            if (slots.isEmpty() || !(this.violeger.level() instanceof ServerLevel serverLevel)) {
                return false;
            }
            int slot = slots.get(this.violeger.getRandom().nextInt(slots.size()));
            ItemStack stolen;
            if (slot < 0) {
                stolen = inventory.offhand.get(0);
                inventory.offhand.set(0, ItemStack.EMPTY);
            } else {
                stolen = inventory.items.get(slot);
                inventory.items.set(slot, ItemStack.EMPTY);
            }
            inventory.setChanged();
            Vec3 away = this.violeger.position().subtract(player.position()).multiply(1.0D, 0.0D, 1.0D);
            away = away.lengthSqr() < 1.0E-4D ? this.violeger.getLookAngle().reverse() : away.normalize();
            Vec3 origin = this.violeger.position().add(0.0D, this.violeger.getBbHeight() * 0.6D, 0.0D);
            ItemEntity drop = new ItemEntity(serverLevel, origin.x, origin.y, origin.z, stolen);
            Vec3 toss = away.scale(THROW_DISTANCE * 0.1D).add(0.0D, 0.25D, 0.0D);
            drop.setDeltaMovement(toss);
            drop.setPickUpDelay(PICKUP_DELAY);
            serverLevel.addFreshEntity(drop);
            Vec3 from = player.position().add(0.0D, player.getBbHeight() * 0.5D, 0.0D);
            for (int i = 0; i <= 8; i++) {
                Vec3 point = from.lerp(origin, i / 8.0D);
                serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, point.x, point.y, point.z, 2, 0.05D, 0.05D, 0.05D, 0.01D);
            }
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BUNDLE_DROP_CONTENTS, this.violeger.getSoundSource(), 1.0F, 0.8F);
            this.violeger.playSound(SoundEvents.PILLAGER_CELEBRATE, 1.0F, 0.8F);
            return true;
        }
    }
}