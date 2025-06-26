package net.sashakyotoz.common.entities.custom;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.entities.ai.GrippingAbyssalBowAttackGoal;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.biomes.ModBiomes;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public class ViolegerEntity extends HostileEntity implements RangedAttackMob, VariantHolder<ViolegerEntity.Type>, InventoryOwner {
    private static final TrackedData<Integer> TYPE = DataTracker.registerData(ViolegerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IS_HOSTILE = DataTracker.registerData(ViolegerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private final GrippingAbyssalBowAttackGoal<ViolegerEntity> bowGoal = new GrippingAbyssalBowAttackGoal<>(this, 1.0D, 20, 16.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false);

    public ViolegerEntity.Type guardianType;

    private final SimpleInventory inventory = new SimpleInventory(8);

    private final List<Item> canBeInPocketSlots = List.of(ModItems.GRIPTONITE, ModItems.ABYSSAL_KEY, ModItems.CRYSTIE_APPLE, Items.OBSIDIAN, Items.RAW_IRON);

    public ViolegerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5;
        this.setEquipment();
    }

    private void setEquipment() {
        int random = this.getRandom().nextInt(3);
        if (!this.getWorld().isClient()) {
            this.setEquipIntoSlot(this.random.nextBoolean() ? new ItemStack(ModItems.GRIPPING_ABYSSAL_BOW) : new ItemStack(ModItems.ABYSSAL_SWORD), EquipmentSlot.MAINHAND);
            if (this.getRandom().nextBoolean())
                this.setEquipIntoSlot(new ItemStack(
                        ModRegistry.ITEMS.stream()
                                .filter(item -> item instanceof ArmorItem item1 && item1.getType() == ArmorItem.Type.CHESTPLATE)
                                .toList().get(random)
                ), EquipmentSlot.CHEST);
            if (this.getRandom().nextBoolean())
                this.setEquipIntoSlot(new ItemStack(
                        ModRegistry.ITEMS.stream()
                                .filter(item -> item instanceof ArmorItem item1 && item1.getType() == ArmorItem.Type.LEGGINGS)
                                .toList().get(random)
                ), EquipmentSlot.LEGS);
            if (this.getRandom().nextBoolean())
                this.setEquipIntoSlot(new ItemStack(
                        ModRegistry.ITEMS.stream()
                                .filter(item -> item instanceof ArmorItem item1 && item1.getType() == ArmorItem.Type.BOOTS)
                                .toList().get(random)
                ), EquipmentSlot.FEET);
            if (this.getRandom().nextBoolean())
                this.setEquipIntoSlot(new ItemStack(Items.SHIELD), EquipmentSlot.OFFHAND);
            this.setEquipIntoSlot(getRandomItemStack(), 6);
        }
    }

    private void setEquipIntoSlot(ItemStack stack, int slot) {
        this.getInventory().setStack(slot, stack);
    }

    private void setEquipIntoSlot(ItemStack stack, EquipmentSlot slot) {
        switch (slot) {
            case MAINHAND -> setEquipIntoSlot(stack, 0);
            case OFFHAND -> setEquipIntoSlot(stack, 1);
            case HEAD -> setEquipIntoSlot(stack, 2);
            case CHEST -> setEquipIntoSlot(stack, 3);
            case LEGS -> setEquipIntoSlot(stack, 4);
            case FEET -> setEquipIntoSlot(stack, 5);
        }
    }

    @Override
    protected float getDropChance(EquipmentSlot slot) {
        return super.getDropChance(slot) * 0.75f;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TYPE, 0);
        this.dataTracker.startTracking(IS_HOSTILE, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new LookAroundGoal(this));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new AvoidSunlightGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0f, 0.25f));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 16));
        this.goalSelector.add(3, new RevengeGoal(this));
        this.goalSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, false));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getOffHandStack().getItem() instanceof ShieldItem && this.getMainHandStack().getItem() instanceof SwordItem)
            this.setCurrentHand(Hand.OFF_HAND);
        if (!this.isHostile() && !this.getWorld().isClient()) {
            if (this.age % 1000 == 0 && this.getInventory().getStack(6).isEmpty())
                this.setEquipIntoSlot(this.getRandomItemStack(), 6);
            if (this.getMainHandStack().isEmpty() && !this.getInventory().getStack(6).isEmpty()) {
                PlayerEntity player = this.getWorld().getClosestPlayer(this, 9);
                if (player != null && isEntityLookingAtMe(player, 0.5, false, true, LivingEntity::canHit, this::getEyeY, this::getY, () -> (this.getEyeY() + this.getY()) / 2.0))
                    this.equipStack(EquipmentSlot.MAINHAND, this.getEquippedStack(EquipmentSlot.MAINHAND).isOf(ModItems.GRANULATED_GRIPCRYSTAL)
                            ? this.getInventory().getStack(6) : ModItems.GRANULATED_GRIPCRYSTAL.getDefaultStack());
            }
            if (!this.getEquippedStack(EquipmentSlot.CHEST).isEmpty())
                this.setIsHostile(false);
        }
        if (this.getTarget() != null) {
            if (this.age % 60 == 0 && this.getHealth() < this.getMaxHealth() && !this.hasStatusEffect(StatusEffects.REGENERATION)) {
                if (!this.getWorld().isClient)
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 40, 1));
            }
        }
        this.setSneaking(this.isUsingItem());
    }

    public boolean isEntityLookingAtMe(LivingEntity entity, double d, boolean bl, boolean visualShape, Predicate<LivingEntity> predicate, DoubleSupplier... entityYChecks) {
        if (predicate.test(entity)) {
            Vec3d vec3d = entity.getRotationVec(1.0F).normalize();
            for (DoubleSupplier doubleSupplier : entityYChecks) {
                Vec3d vec3d2 = new Vec3d(this.getX() - entity.getX(), doubleSupplier.getAsDouble() - entity.getEyeY(), this.getZ() - entity.getZ());
                double e = vec3d2.length();
                vec3d2 = vec3d2.normalize();
                double f = vec3d.dotProduct(vec3d2);
                if (f > 1.0 - d / (bl ? e : 1.0))
                    return canSee(this, visualShape ? RaycastContext.ShapeType.VISUAL : RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, doubleSupplier);
            }
        }
        return false;
    }

    public boolean canSee(Entity entity, RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluidHandling, DoubleSupplier entityY) {
        if (entity.getWorld() != this.getWorld()) {
            return false;
        } else {
            Vec3d vec3d = new Vec3d(this.getX(), this.getEyeY(), this.getZ());
            Vec3d vec3d2 = new Vec3d(entity.getX(), entityY.getAsDouble(), entity.getZ());
            if (vec3d2.distanceTo(vec3d) > 128.0)
                return false;
            else
                return this.getWorld().raycast(new RaycastContext(vec3d, vec3d2, shapeType, fluidHandling, this)).getType() == HitResult.Type.MISS;
        }
    }

    public void reassessWeaponGoal() {
        if (!this.getWorld().isClient() && this.isHostile()) {
            this.goalSelector.remove(this.meleeGoal);
            this.goalSelector.remove(this.bowGoal);
            ItemStack itemstack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, ModItems.GRIPPING_ABYSSAL_BOW));
            if (itemstack.isOf(ModItems.GRIPPING_ABYSSAL_BOW)) {
                int i = 20;
                if (this.getWorld().getDifficulty() != Difficulty.HARD)
                    i = 40;
                this.bowGoal.setAttackInterval(i);
                this.goalSelector.add(4, this.bowGoal);
            } else
                this.goalSelector.add(4, this.meleeGoal);
        }
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        ItemStack itemstack = this.getProjectileType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, ModItems.GRIPPING_ABYSSAL_BOW)));
        PersistentProjectileEntity abstractArrow = ProjectileUtil.createArrowProjectile(this, itemstack, pullProgress);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY() + 0.35f - abstractArrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractArrow.setVelocity(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.getWorld().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ITEM_CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(abstractArrow);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
                .add(EntityAttributes.GENERIC_ARMOR, 8)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.1)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity && !this.getWorld().isClient() && !this.isHostile())
            this.setIsHostile(true);
        return super.damage(source, amount);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Type", this.getVariant().asString());
        this.writeInventory(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVariant(Type.byName(nbt.getString("Type")));
        this.readInventory(nbt);
    }

    @Override
    public boolean canGather(ItemStack stack) {
        return false;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack item = player.getStackInHand(hand);
        if (this.isHostile() && item.isOf(ModItems.GRIPCRYSTAL)) {
            item.decrement(1);
            this.setIsHostile(false);
            this.getWorld().addParticle(ParticleTypes.HEART, this.getX(), this.getY() + 1.5f, this.getZ(), 0, 1, 0);
            return ActionResult.CONSUME;
        }
        if (!this.isHostile() && !this.getInventory().getStack(6).isEmpty() && item.isOf(ModItems.GRANULATED_GRIPCRYSTAL)) {
            this.lookAtEntity(player, 30, 30);
            ActionsUtils.dropItem(this, this.getInventory().getStack(6));
            item.decrement(1);
            this.getInventory().setStack(6, ItemStack.EMPTY);
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            if (this.getWorld().isClient())
                this.playSound(SoundEvents.ENTITY_VILLAGER_TRADE, 1.2f, 1);
            return ActionResult.CONSUME;
        }
        return super.interactMob(player, hand);
    }

    @Nullable
    @Override
    public EntityData initialize(
            ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt
    ) {
        RegistryEntry<Biome> registryEntry = world.getBiome(this.getBlockPos());
        ViolegerEntity.Type type = ViolegerEntity.Type.fromBiome(registryEntry);
        this.setVariant(type);
        if (spawnReason.equals(SpawnReason.SPAWNER) || spawnReason.equals(SpawnReason.PATROL))
            this.setIsHostile(true);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }


    public void setIsHostile(boolean flag) {
        this.dataTracker.set(IS_HOSTILE, flag);
        if (flag) {
            if (this.getWorld().isClient())
                this.getWorld().addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX(), this.getY() + 1, this.getZ(), 0, 1, 0);
            this.equipStack(EquipmentSlot.MAINHAND, this.getInventory().getStack(0));
            this.equipStack(EquipmentSlot.OFFHAND, this.getInventory().getStack(1));
            this.equipStack(EquipmentSlot.HEAD, this.getInventory().getStack(2));
            this.equipStack(EquipmentSlot.CHEST, this.getInventory().getStack(3));
            this.equipStack(EquipmentSlot.LEGS, this.getInventory().getStack(4));
            this.equipStack(EquipmentSlot.FEET, this.getInventory().getStack(5));
            this.reassessWeaponGoal();
            for (EquipmentSlot value : EquipmentSlot.values())
                this.setEquipIntoSlot(ItemStack.EMPTY, value);
            this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.5f, 1);
        } else {
            for (EquipmentSlot value : EquipmentSlot.values()) {
                this.setEquipIntoSlot(this.getEquippedStack(value), value);
                this.equipStack(value, ItemStack.EMPTY);
            }
            this.equipStack(EquipmentSlot.MAINHAND, this.getInventory().getStack(6));
        }
    }

    public ItemStack getRandomItemStack() {
        Item chosen = canBeInPocketSlots.get(this.getRandom().nextInt(canBeInPocketSlots.size()));
        return new ItemStack(chosen);
    }

    public boolean isHostile() {
        return this.dataTracker.get(IS_HOSTILE);
    }

    @Override
    public void setVariant(Type variant) {
        this.dataTracker.set(TYPE, variant.getId());
    }

    @Override
    public Type getVariant() {
        return Type.fromId(this.dataTracker.get(TYPE));
    }

    @Override
    public SimpleInventory getInventory() {
        return this.inventory;
    }

    public enum Type implements StringIdentifiable {
        BLUEISH(0, "blueish"),
        YELLOW(1, "yellow"),
        PURPLE(2, "purple");

        public static final StringIdentifiable.Codec<ViolegerEntity.Type> CODEC = StringIdentifiable.createCodec(ViolegerEntity.Type::values);
        private static final IntFunction<ViolegerEntity.Type> BY_ID = ValueLists.createIdToValueFunction(
                ViolegerEntity.Type::getId, values(), ValueLists.OutOfBoundsHandling.ZERO
        );
        private final int id;
        private final String key;

        Type(int id, String key) {
            this.id = id;
            this.key = key;
        }

        @Override
        public String asString() {
            return this.key;
        }

        public int getId() {
            return this.id;
        }

        public static ViolegerEntity.Type byName(String name) {
            return CODEC.byId(name, BLUEISH);
        }

        public static ViolegerEntity.Type fromId(int id) {
            return BY_ID.apply(id);
        }

        public static ViolegerEntity.Type fromBiome(RegistryEntry<Biome> biome) {
            if (biome.matchesKey(ModBiomes.THE_DARKNESS) || biome.isIn(ModTags.Biomes.GLACIEMITE_BOULDER_SPAWNS_ON))
                return BLUEISH;
            if (biome.isIn(ModTags.Biomes.HAS_CRIMSONVEIL_TREE))
                return YELLOW;
            return Random.create().nextBoolean() ? BLUEISH : PURPLE;
        }
    }
}