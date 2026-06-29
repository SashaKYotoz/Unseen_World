package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.biomes.ModBiomes;

public class GleamcarverEntity extends PathfinderMob {
    public final AnimationState death = new AnimationState();
    public Type gleamcarverType;

    public GleamcarverEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.getString("entityType") != null) {
            for (Type type : Type.values()) {
                if (type.typeName.equals(nbt.getString("entityType"))) {
                    this.gleamcarverType = type;
                }
            }
        } else
            this.gleamcarverType = Type.GLACIEMITE;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (this.gleamcarverType != null)
            nbt.putString("entityType", this.gleamcarverType.typeName);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).getItem() instanceof PickaxeItem && this.gleamcarverType != null) {
            switch (this.gleamcarverType) {
                case ABYSSAL -> {
                    this.spawnAtLocation(ModItems.RAW_ABYSSAL_ORE);
                    this.gleamcarverType = Type.GLACIEMITE;
                    player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                    player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 40);
                    return InteractionResult.sidedSuccess(true);
                }
                case TANZANITE -> {
                    this.spawnAtLocation(ModBlocks.TANZANITE_BLOCK);
                    this.gleamcarverType = Type.DARK_CURRANTSLATE;
                    player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                    player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 40);
                    return InteractionResult.sidedSuccess(true);
                }
                case UNSEENIUM -> {
                    this.spawnAtLocation(ModItems.RAW_UNSEENIUM_ORE);
                    this.gleamcarverType = Type.DARK_CURRANTSLATE;
                    player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                    player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 40);
                    return InteractionResult.sidedSuccess(true);
                }
            }
            this.level().playSound(this, this.blockPosition(), SoundEvents.DEEPSLATE_HIT, SoundSource.NEUTRAL, 2, 2);
        }
        return super.mobInteract(player, hand);
    }
    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(3) == 0 ? ModSoundEvents.GLEAMCARVER_AMBIENT_SOUND : null;
    }
    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide()) {
            if (this.tickCount % 1000 == 0 && this.gleamcarverType != null) {
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
            if (this.level().getBiome(this.blockPosition()).is(ModBiomes.DEEP_GLACIEMITE_CAVES))
                this.gleamcarverType = Type.ABYSSAL;
            else if (this.level().getBiome(this.blockPosition()).is(ModBiomes.TANZANITE_CAVES))
                this.gleamcarverType = Type.TANZANITE;
            else
                this.gleamcarverType = this.getRandom().nextBoolean() ? Type.DARK_CURRANTSLATE : Type.UNSEENIUM;
        }
    }

    public static boolean canGleamcarverSpawn(EntityType<? extends GleamcarverEntity> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.above()).isAir() && pos.getY() < 16;
    }

    @Override
    public void die(DamageSource damageSource) {
        this.deathTime = -20;
        super.die(damageSource);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (!this.death.isStarted())
            this.death.start(this.tickCount);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new FleeSunGoal(this, 1));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.75, 0.05f));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
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