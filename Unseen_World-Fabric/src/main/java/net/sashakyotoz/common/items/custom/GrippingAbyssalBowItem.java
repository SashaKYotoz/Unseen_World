package net.sashakyotoz.common.items.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.custom.projectiles.GrippingCrystalProjectileEntity;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.List;

public class GrippingAbyssalBowItem extends BowItem {
    public GrippingAbyssalBowItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (IGrippingWeapons.getPhase(stack).isEmpty())
            return super.use(world, user, hand);
        else if (user instanceof ServerPlayer player &&
                (GripcrystalManaData.getMana((IEntityDataSaver) player) > 0 || IGrippingWeapons.getPhase(stack).equals("crystal_suctioning"))) {
            user.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        } else
            return InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        return ingredient.is(ModItems.GRIPCRYSTAL) || super.isValidRepairItem(stack, ingredient);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player playerEntity) {
            int i = this.getUseDuration(stack) - remainingUseTicks;
            float f = getPowerForTime(i);
            switch (IGrippingWeapons.getPhase(stack)) {
                case "crystal_crushing" -> {
                    if (playerEntity instanceof ServerPlayer player && GripcrystalManaData.getMana((IEntityDataSaver) player) > 2) {
                        GripcrystalManaData.removeMana((IEntityDataSaver) player, 2);
                        if (!world.isClientSide) {
                            GrippingCrystalProjectileEntity projectile = new GrippingCrystalProjectileEntity(ModEntities.GRIPPING_CRYSTAL_PROJECTILE, player.level());
                            projectile.setKnockback(3);
                            projectile.setOwner(player);
                            projectile.moveTo(player.getX(), player.getEyeY(), player.getZ());
                            projectile.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, f * 3.0F, 1.0F);
                            stack.hurtAndBreak(1, playerEntity, p -> p.broadcastBreakEvent(playerEntity.getUsedItemHand()));
                            world.addFreshEntity(projectile);
                        }
                        player.playNotifySound(SoundEvents.GLASS_HIT, SoundSource.PLAYERS, 2, 2);
                        player.serverLevel().sendParticles(ModParticleTypes.GRIPPING_CRYSTAL,
                                player.getX(), player.getY(), player.getZ(), 9,
                                ActionsUtils.getXVector(2, player.getYRot()),
                                ActionsUtils.getYVector(1, player.getXRot()),
                                ActionsUtils.getZVector(2, player.getYRot()), 1);
                    }
                }
                case "crystal_rain" -> {
                    if (playerEntity instanceof ServerPlayer player && GripcrystalManaData.getMana((IEntityDataSaver) player) > 6) {
                        GripcrystalManaData.removeMana((IEntityDataSaver) player, 6);
                        if (!world.isClientSide()) {
                            for (int j = -2; j < 2; j++) {
                                GrippingCrystalProjectileEntity projectile = new GrippingCrystalProjectileEntity(ModEntities.GRIPPING_CRYSTAL_PROJECTILE, player.level());
                                projectile.setOwner(player);
                                projectile.moveTo(player.getX(), player.getEyeY(), player.getZ());
                                projectile.shootFromRotation(playerEntity, playerEntity.getXRot() + 15 * j, playerEntity.getYRot() + 15 * j, 0.0F, f * 1.5F, 0.75F);
                                world.addFreshEntity(projectile);
                            }
                            for (int j = 2; j > -2; j--) {
                                GrippingCrystalProjectileEntity projectile = new GrippingCrystalProjectileEntity(ModEntities.GRIPPING_CRYSTAL_PROJECTILE, player.level());
                                projectile.setOwner(player);
                                projectile.moveTo(player.getX(), player.getEyeY(), player.getZ());
                                projectile.shootFromRotation(playerEntity, playerEntity.getXRot() + 15 * j, playerEntity.getYRot() - 15 * j, 0.0F, f * 1.5F, 0.75F);
                                world.addFreshEntity(projectile);
                            }
                        }
                        stack.hurtAndBreak(1, playerEntity, p -> p.broadcastBreakEvent(playerEntity.getUsedItemHand()));
                        player.playNotifySound(SoundEvents.GLASS_PLACE, SoundSource.PLAYERS, 2, 2);
                    }
                }
                case "crystal_suctioning" -> {
                    float scaling = 0;
                    if (playerEntity instanceof ServerPlayer player && GripcrystalManaData.getMana((IEntityDataSaver) player) < 12) {
                        for (int j = 0; j < 7; j++) {
                            BlockPos pos = playerEntity.level().clip(new ClipContext(playerEntity.getEyePosition(), playerEntity.getEyePosition().add(playerEntity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, playerEntity)).getBlockPos();
                            if (!playerEntity.level().getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).canOcclude())
                                scaling = scaling + 1;
                            List<LivingEntity> entities = playerEntity.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos.getCenter(), pos.getCenter()).inflate(1.5), LivingEntity::isPickable);
                            for (LivingEntity entity : entities) {
                                if (entity != playerEntity) {
                                    entity.hurt(playerEntity.damageSources().generic(), 6);
                                    playerEntity.heal(6);
                                    GripcrystalManaData.addMana((IEntityDataSaver) player, 4);
                                    stack.hurtAndBreak(2, playerEntity, p -> p.broadcastBreakEvent(playerEntity.getUsedItemHand()));
                                    playerEntity.playNotifySound(SoundEvents.GLASS_STEP, SoundSource.PLAYERS, 2, 2.5f);
                                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                                    break;
                                }
                            }
                        }
                    }
                }
                default -> {
                    boolean bl = playerEntity.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
                    ItemStack itemStack = playerEntity.getProjectile(stack);
                    if (!itemStack.isEmpty() || bl) {
                        if (itemStack.isEmpty())
                            itemStack = new ItemStack(Items.ARROW);
                        if (!((double) f < 0.1)) {
                            boolean bl2 = bl && itemStack.is(Items.ARROW);
                            if (!world.isClientSide()) {
                                ArrowItem arrowItem = (ArrowItem) (itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                                AbstractArrow projectile = arrowItem.createArrow(world, itemStack, playerEntity);
                                projectile.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, f * 3.0F, 1.0F);
                                if (f == 1.0F)
                                    projectile.setCritArrow(true);
                                int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                                if (j > 0)
                                    projectile.setBaseDamage(projectile.getBaseDamage() + (double) j * 0.5 + 0.5);
                                int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                                if (k > 0)
                                    projectile.setKnockback(k);
                                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0)
                                    projectile.setSecondsOnFire(100);
                                stack.hurtAndBreak(1, playerEntity, p -> p.broadcastBreakEvent(playerEntity.getUsedItemHand()));
                                if (bl2 || playerEntity.getAbilities().instabuild && (itemStack.is(Items.SPECTRAL_ARROW) || itemStack.is(Items.TIPPED_ARROW)))
                                    projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                                world.addFreshEntity(projectile);
                            }

                            world.playSound(
                                    null,
                                    playerEntity.getX(),
                                    playerEntity.getY(),
                                    playerEntity.getZ(),
                                    SoundEvents.ARROW_SHOOT,
                                    SoundSource.PLAYERS,
                                    1.0F,
                                    1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                            );
                            if (!bl2 && !playerEntity.getAbilities().instabuild) {
                                itemStack.shrink(1);
                                if (itemStack.isEmpty())
                                    playerEntity.getInventory().removeItem(itemStack);
                            }

                            playerEntity.awardStat(Stats.ITEM_USED.get(this));
                        }
                    }
                }
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        if (ActionsUtils.isModLoaded("bettercombat")) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(
                    Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 5, AttributeModifier.Operation.ADDITION)
            );
            builder.put(
                    Attributes.ATTACK_SPEED,
                    new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2, AttributeModifier.Operation.ADDITION)
            );
            return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getDefaultAttributeModifiers(slot);
        } else
            return super.getAttributeModifiers(stack, slot);
    }
}