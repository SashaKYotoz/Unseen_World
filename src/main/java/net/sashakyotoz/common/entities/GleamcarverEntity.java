package net.sashakyotoz.common.entities;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.EscapeSunlightGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.biomes.ModBiomes;

import java.util.Arrays;

public class GleamcarverEntity extends PathAwareEntity {
    public final AnimationState death = new AnimationState();
    public Type gleamcarverType;

    public GleamcarverEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
//        if (nbt.getString("entityType") != null)
//            this.gleamcarverType = Arrays.stream(Type.values()).filter(type -> type.typeName.equals(nbt.getString("entityType"))).;
//        else
            this.gleamcarverType = Type.GLACIEMITE;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getStackInHand(hand).getItem() instanceof PickaxeItem && this.gleamcarverType != null) {
            switch (this.gleamcarverType) {
                case ABYSSAL -> {
                    this.dropItem(ModItems.RAW_ABYSSAL_ORE);
                    this.gleamcarverType = Type.GLACIEMITE;
                    player.getStackInHand(hand).damage(1, player, p -> p.sendToolBreakStatus(hand));
                    player.getItemCooldownManager().set(player.getStackInHand(hand).getItem(), 40);
                    return ActionResult.success(true);
                }
                case TANZANITE -> {
                    this.dropItem(ModBlocks.TANZANITE_BLOCK);
                    this.gleamcarverType = Type.DARK_CURRANTSLATE;
                    player.getStackInHand(hand).damage(1, player, p -> p.sendToolBreakStatus(hand));
                    player.getItemCooldownManager().set(player.getStackInHand(hand).getItem(), 40);
                    return ActionResult.success(true);
                }
                case UNSEENIUM -> {
                    this.dropItem(ModItems.RAW_UNSEENIUM_ORE);
                    this.gleamcarverType = Type.DARK_CURRANTSLATE;
                    player.getStackInHand(hand).damage(1, player, p -> p.sendToolBreakStatus(hand));
                    player.getItemCooldownManager().set(player.getStackInHand(hand).getItem(), 40);
                    return ActionResult.success(true);
                }
            }
            this.getWorld().playSound(this, this.getBlockPos(), SoundEvents.BLOCK_DEEPSLATE_HIT, SoundCategory.NEUTRAL, 2, 2);
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.getWorld().isClient()) {
            if (this.age % 1000 == 0 && this.gleamcarverType != null) {
                switch (this.gleamcarverType) {
                    case GLACIEMITE -> {
                        if (this.random.nextBoolean())
                            this.gleamcarverType = Type.ABYSSAL;
                    }
                    case DARK_CURRANTSLATE -> {
                        if (this.random.nextBoolean())
                            this.gleamcarverType = this.random.nextBoolean() ? Type.TANZANITE : Type.UNSEENIUM;
                    }
                }
            }
        }
        if (this.gleamcarverType == null) {
            if (this.getWorld().getBiome(this.getBlockPos()).matchesKey(ModBiomes.DEEP_GLACIEMITE_CAVES))
                this.gleamcarverType = Type.ABYSSAL;
            else if (this.getWorld().getBiome(this.getBlockPos()).matchesKey(ModBiomes.TANZANITE_CAVES))
                this.gleamcarverType = Type.TANZANITE;
            else
                this.gleamcarverType = this.getRandom().nextBoolean() ? Type.DARK_CURRANTSLATE : Type.UNSEENIUM;
        }
    }

    public static boolean canGleamcarverSpawn(EntityType<? extends GleamcarverEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.up()).isAir() && pos.getY() < 16;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.deathTime = -20;
        super.onDeath(damageSource);
    }

    @Override
    protected void updatePostDeath() {
        super.updatePostDeath();
        if (!this.death.isRunning())
            this.death.start(this.age);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAroundGoal(this));
        this.goalSelector.add(1, new EscapeSunlightGoal(this, 1));
        this.goalSelector.add(2, new SwimGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.75, 0.05f));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.gleamcarverType != null)
            nbt.putString("entityType", this.gleamcarverType.typeName);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12)
                .add(EntityAttributes.GENERIC_ARMOR, 4)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    public enum Type {
        GLACIEMITE("glaciemite"),
        DARK_CURRANTSLATE("dark_currantslate"),
        ABYSSAL("abyssal"),
        UNSEENIUM("unseenium"),
        TANZANITE("tanzanite");
        public final String typeName;

        Type(String nameOfType) {
            typeName = nameOfType;
        }
    }
}