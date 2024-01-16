
package net.sashakyotoz.unseenworld.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.sashakyotoz.unseenworld.entity.UnseenTitaniumSpearEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.jarjar.nio.util.Lazy;

import java.util.UUID;

public class UnseenTitaniumSpearItem extends Item implements Vanishable {
	public static final UUID REACH_MOD = UUID.fromString("dccd59ec-6391-436d-9e00-47f2e6005e20");
	public static double reach = 3;
	public static int damage = 13;
	public static Lazy<? extends Multimap<Attribute, AttributeModifier>> ATTRIBUTE_LAZY_MAP = Lazy.of(() -> {
		Multimap<Attribute, AttributeModifier> map;
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", damage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8, AttributeModifier.Operation.ADDITION));
		if (ForgeMod.ENTITY_REACH.isPresent()) {
			builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(REACH_MOD, "Weapon modifier", reach, AttributeModifier.Operation.ADDITION));
		}
		map = builder.build();
		return map;
	});

	public UnseenTitaniumSpearItem() {
		super(new Item.Properties().durability(1850));
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == EquipmentSlot.MAINHAND ? ATTRIBUTE_LAZY_MAP.get() : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		double reachSqr = reach * reach;
		Level world = entity.level();
		Vec3 viewVec = entity.getViewVector(1.0F);
		Vec3 eyeVec = entity.getEyePosition(1.0F);
		Vec3 targetVec = eyeVec.add(viewVec.x * reach, viewVec.y * reach, viewVec.z * reach);
		AABB viewBB = entity.getBoundingBox().expandTowards(viewVec.scale(reach)).inflate(4.0D, 4.0D, 4.0D);
		EntityHitResult result = ProjectileUtil.getEntityHitResult(world, entity, eyeVec, targetVec, viewBB, EntitySelector.NO_CREATIVE_OR_SPECTATOR, 4f);
		if (result == null || !(result.getEntity() instanceof LivingEntity target))
			return false;
		double distanceToTargetSqr = entity.distanceToSqr(target);
		boolean hitResult = (result != null ? target : null) != null;
		if (hitResult) {
			if (entity instanceof Player) {
				if (reachSqr >= distanceToTargetSqr) {
					target.hurt(new DamageSource(target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK)), 1);
				}
			}
		}
		return super.onEntitySwing(stack, entity);
	}

	public boolean canAttackBlock(BlockState state, Level p_43410_, BlockPos p_43411_, Player player) {
		return !player.isCreative();
	}

	@Override
	public UseAnim getUseAnimation(ItemStack p_43417_) {
		return UseAnim.SPEAR;
	}

	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int p_43397_) {
		if (livingEntity instanceof Player player) {
			int i = this.getUseDuration(itemStack) - p_43397_;
			if (i >= 10) {
				int j = EnchantmentHelper.getRiptide(itemStack);
				if (j <= 0 || player.isInWaterOrRain()) {
					if (!level.isClientSide) {
						itemStack.hurtAndBreak(1, player, (player1) -> player1.broadcastBreakEvent(livingEntity.getUsedItemHand()));
						if (j == 0) {
							UnseenTitaniumSpearEntity thrownSpear = new UnseenTitaniumSpearEntity(level, player, itemStack);
							thrownSpear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + (float) j * 0.5F, 1.0F);
							if (player.getAbilities().instabuild) {
								thrownSpear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
							}
							level.addFreshEntity(thrownSpear);
							level.playSound(null, thrownSpear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
							if (!player.getAbilities().instabuild) {
								player.getInventory().removeItem(itemStack);
							}
						}
					}
					player.awardStat(Stats.ITEM_USED.get(this));
					if (j > 0) {
						float f7 = player.getYRot();
						float f = player.getXRot();
						float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
						float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
						float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
						float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
						float f5 = 3.0F * ((1.0F + (float) j) / 4.0F);
						f1 *= f5 / f4;
						f2 *= f5 / f4;
						f3 *= f5 / f4;
						player.push(f1, f2, f3);
						player.startAutoSpinAttack(20);
						if (player.onGround()) {
							player.move(MoverType.SELF, new Vec3(0.0D, 1.2F, 0.0D));
						}
						SoundEvent soundevent;
						if (j >= 3) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
						} else if (j == 2) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
						} else {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
						}
						level.playSound(null, player, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
					}
				}
			}
		}
	}

	public InteractionResultHolder<ItemStack> use(Level p_43405_, Player p_43406_, InteractionHand p_43407_) {
		ItemStack itemstack = p_43406_.getItemInHand(p_43407_);
		if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
			return InteractionResultHolder.fail(itemstack);
		} else if (EnchantmentHelper.getRiptide(itemstack) > 0 && !p_43406_.isInWaterOrRain()) {
			return InteractionResultHolder.fail(itemstack);
		} else {
			p_43406_.startUsingItem(p_43407_);
			return InteractionResultHolder.consume(itemstack);
		}
	}

	public boolean hurtEnemy(ItemStack p_43390_, LivingEntity p_43391_, LivingEntity p_43392_) {
		p_43390_.hurtAndBreak(1, p_43392_, (p_43414_) -> {
			p_43414_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		return true;
	}

	public boolean mineBlock(ItemStack p_43399_, Level p_43400_, BlockState p_43401_, BlockPos p_43402_, LivingEntity p_43403_) {
		if ((double) p_43401_.getDestroySpeed(p_43400_, p_43402_) != 0.0D) {
			p_43399_.hurtAndBreak(2, p_43403_, (p_43385_) -> {
				p_43385_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
			});
		}
		return true;
	}

	@Override
	public int getEnchantmentValue() {
		return 5;
	}
}