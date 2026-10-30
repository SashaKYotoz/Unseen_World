package net.sashakyotoz.common.entities.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiConsumer;

public final class GripConversion {
    private static final String TIME_KEY = "ConversionTime";
    private static final String PLAYER_KEY = "ConversionPlayer";

    private final Mob mob;
    private final EntityDataAccessor<Boolean> flag;
    private final BiConsumer<ServerLevel, GripConversion> finisher;
    private int timer;
    @Nullable
    private UUID converter;

    public GripConversion(Mob mob, EntityDataAccessor<Boolean> flag, BiConsumer<ServerLevel, GripConversion> finisher) {
        this.mob = mob;
        this.flag = flag;
        this.finisher = finisher;
    }

    public static void define(SynchedEntityData.Builder data, EntityDataAccessor<Boolean> flag) {
        data.define(flag, false);
    }

    public boolean isConverting() {
        return this.mob.getEntityData().get(this.flag);
    }

    @Nullable
    public UUID converter() {
        return this.converter;
    }

    public void start(@Nullable UUID uuid, int delay) {
        this.converter = uuid;
        this.timer = delay;
        this.mob.getEntityData().set(this.flag, true);
        this.mob.removeEffect(MobEffects.GLOWING);
        this.mob.addEffect(new MobEffectInstance(MobEffects.DARKNESS, delay, Math.max(this.mob.level().getDifficulty().getId() - 1, 0)));
        this.mob.level().broadcastEntityEvent(this.mob, EntityEvent.VILLAGER_SWEAT);
    }

    public void tick() {
        if (!this.mob.level().isClientSide() && this.mob.isAlive() && this.isConverting() && --this.timer <= 0) {
            this.mob.getEntityData().set(this.flag, false);
            this.finisher.accept((ServerLevel) this.mob.level(), this);
        }
    }

    public InteractionResult tryGriptonite(Player player, InteractionHand hand, int minDelay, int extraDelay) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(ModItems.GRIPTONITE)) {
            return InteractionResult.PASS;
        }
        if (!this.mob.level().isClientSide()) {
            if (!player.getAbilities().instabuild) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            this.start(player.getUUID(), this.mob.getRandom().nextInt(extraDelay + 1) + minDelay);
        }
        return InteractionResult.sidedSuccess(this.mob.level().isClientSide());
    }

    public InteractionResult tryCrystieApple(Player player, InteractionHand hand, int minDelay, int extraDelay) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(ModItems.CRYSTIE_APPLE)) {
            return InteractionResult.PASS;
        }
        if (!this.mob.hasEffect(MobEffects.GLOWING)) {
            return InteractionResult.CONSUME;
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        if (!this.mob.level().isClientSide()) {
            this.start(player.getUUID(), this.mob.getRandom().nextInt(extraDelay + 1) + minDelay);
        }
        return InteractionResult.sidedSuccess(this.mob.level().isClientSide());
    }

    public void save(CompoundTag tag) {
        tag.putInt(TIME_KEY, this.isConverting() ? this.timer : -1);
        if (this.converter != null) {
            tag.putUUID(PLAYER_KEY, this.converter);
        }
    }

    public void load(CompoundTag tag) {
        if (tag.contains(TIME_KEY, Tag.TAG_ANY_NUMERIC) && tag.getInt(TIME_KEY) > -1) {
            this.start(tag.hasUUID(PLAYER_KEY) ? tag.getUUID(PLAYER_KEY) : null, tag.getInt(TIME_KEY));
        }
    }
}
