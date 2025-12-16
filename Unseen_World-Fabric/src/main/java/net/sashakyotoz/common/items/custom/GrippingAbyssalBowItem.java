package net.sashakyotoz.common.items.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.custom.projectiles.GrippingCrystalProjectileEntity;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.List;

public class GrippingAbyssalBowItem extends BowItem {
    public GrippingAbyssalBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (IGrippingWeapons.getPhase(stack).isEmpty())
            return super.use(world, user, hand);
        else if (user instanceof ServerPlayerEntity player &&
                (GripcrystalManaData.getMana((IEntityDataSaver) player) > 0 || IGrippingWeapons.getPhase(stack).equals("crystal_suctioning"))) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        } else
            return TypedActionResult.fail(stack);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(ModItems.GRIPCRYSTAL) || super.canRepair(stack, ingredient);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            float f = getPullProgress(i);
            switch (IGrippingWeapons.getPhase(stack)) {
                case "crystal_crushing" -> {
                    if (playerEntity instanceof ServerPlayerEntity player && GripcrystalManaData.getMana((IEntityDataSaver) player) > 2) {
                        GripcrystalManaData.removeMana((IEntityDataSaver) player, 2);
                        if (!world.isClient) {
                            GrippingCrystalProjectileEntity projectile = new GrippingCrystalProjectileEntity(ModEntities.GRIPPING_CRYSTAL_PROJECTILE, player.getWorld());
                            projectile.setPunch(3);
                            projectile.setOwner(player);
                            projectile.refreshPositionAfterTeleport(player.getX(), player.getEyeY(), player.getZ());
                            projectile.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, f * 3.0F, 1.0F);
                            stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
                            world.spawnEntity(projectile);
                        }
                        player.playSound(SoundEvents.BLOCK_GLASS_HIT, SoundCategory.PLAYERS, 2, 2);
                        player.getServerWorld().spawnParticles(ModParticleTypes.GRIPPING_CRYSTAL,
                                player.getX(), player.getY(), player.getZ(), 9,
                                ActionsUtils.getXVector(2, player.getYaw()),
                                ActionsUtils.getYVector(1, player.getPitch()),
                                ActionsUtils.getZVector(2, player.getYaw()), 1);
                    }
                }
                case "crystal_rain" -> {
                    if (playerEntity instanceof ServerPlayerEntity player && GripcrystalManaData.getMana((IEntityDataSaver) player) > 6) {
                        GripcrystalManaData.removeMana((IEntityDataSaver) player, 6);
                        if (!world.isClient()) {
                            for (int j = -2; j < 2; j++) {
                                GrippingCrystalProjectileEntity projectile = new GrippingCrystalProjectileEntity(ModEntities.GRIPPING_CRYSTAL_PROJECTILE, player.getWorld());
                                projectile.setOwner(player);
                                projectile.refreshPositionAfterTeleport(player.getX(), player.getEyeY(), player.getZ());
                                projectile.setVelocity(playerEntity, playerEntity.getPitch() + 15 * j, playerEntity.getYaw() + 15 * j, 0.0F, f * 1.5F, 0.75F);
                                world.spawnEntity(projectile);
                            }
                            for (int j = 2; j > -2; j--) {
                                GrippingCrystalProjectileEntity projectile = new GrippingCrystalProjectileEntity(ModEntities.GRIPPING_CRYSTAL_PROJECTILE, player.getWorld());
                                projectile.setOwner(player);
                                projectile.refreshPositionAfterTeleport(player.getX(), player.getEyeY(), player.getZ());
                                projectile.setVelocity(playerEntity, playerEntity.getPitch() + 15 * j, playerEntity.getYaw() - 15 * j, 0.0F, f * 1.5F, 0.75F);
                                world.spawnEntity(projectile);
                            }
                        }
                        stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
                        player.playSound(SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.PLAYERS, 2, 2);
                    }
                }
                case "crystal_suctioning" -> {
                    float scaling = 0;
                    if (playerEntity instanceof ServerPlayerEntity player && GripcrystalManaData.getMana((IEntityDataSaver) player) < 12) {
                        for (int j = 0; j < 7; j++) {
                            BlockPos pos = playerEntity.getWorld().raycast(new RaycastContext(playerEntity.getEyePos(), playerEntity.getEyePos().add(playerEntity.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, playerEntity)).getBlockPos();
                            if (!playerEntity.getWorld().getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).isOpaque())
                                scaling = scaling + 1;
                            List<LivingEntity> entities = playerEntity.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos.toCenterPos(), pos.toCenterPos()).expand(1.5), LivingEntity::canHit);
                            for (LivingEntity entity : entities) {
                                if (entity != playerEntity) {
                                    entity.damage(playerEntity.getDamageSources().generic(), 6);
                                    playerEntity.heal(6);
                                    GripcrystalManaData.addMana((IEntityDataSaver) player, 4);
                                    stack.damage(2, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
                                    playerEntity.playSound(SoundEvents.BLOCK_GLASS_STEP, SoundCategory.PLAYERS, 2, 2.5f);
                                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
                                    break;
                                }
                            }
                        }
                    }
                }
                default -> {
                    boolean bl = playerEntity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
                    ItemStack itemStack = playerEntity.getProjectileType(stack);
                    if (!itemStack.isEmpty() || bl) {
                        if (itemStack.isEmpty())
                            itemStack = new ItemStack(Items.ARROW);
                        if (!((double) f < 0.1)) {
                            boolean bl2 = bl && itemStack.isOf(Items.ARROW);
                            if (!world.isClient()) {
                                ArrowItem arrowItem = (ArrowItem) (itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                                PersistentProjectileEntity projectile = arrowItem.createArrow(world, itemStack, playerEntity);
                                projectile.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, f * 3.0F, 1.0F);
                                if (f == 1.0F)
                                    projectile.setCritical(true);
                                int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                                if (j > 0)
                                    projectile.setDamage(projectile.getDamage() + (double) j * 0.5 + 0.5);
                                int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                                if (k > 0)
                                    projectile.setPunch(k);
                                if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0)
                                    projectile.setOnFireFor(100);
                                stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
                                if (bl2 || playerEntity.getAbilities().creativeMode && (itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(Items.TIPPED_ARROW)))
                                    projectile.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                                world.spawnEntity(projectile);
                            }

                            world.playSound(
                                    null,
                                    playerEntity.getX(),
                                    playerEntity.getY(),
                                    playerEntity.getZ(),
                                    SoundEvents.ENTITY_ARROW_SHOOT,
                                    SoundCategory.PLAYERS,
                                    1.0F,
                                    1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                            );
                            if (!bl2 && !playerEntity.getAbilities().creativeMode) {
                                itemStack.decrement(1);
                                if (itemStack.isEmpty())
                                    playerEntity.getInventory().removeOne(itemStack);
                            }

                            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                        }
                    }
                }
            }
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        if (ActionsUtils.isModLoaded("bettercombat")) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 5, EntityAttributeModifier.Operation.ADDITION)
            );
            builder.put(
                    EntityAttributes.GENERIC_ATTACK_SPEED,
                    new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -2, EntityAttributeModifier.Operation.ADDITION)
            );
            return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getAttributeModifiers(slot);
        } else
            return super.getAttributeModifiers(stack, slot);
    }
}