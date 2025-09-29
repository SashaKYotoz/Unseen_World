package net.sashakyotoz.unseenworld.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.jarjar.nio.util.Lazy;
import net.sashakyotoz.unseenworld.managers.LivingEntityIsHitWithTreasureWeaponProcedure;
import net.sashakyotoz.unseenworld.managers.TreasureWeaponOnBeaconClick;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.UUID;
import java.util.function.Consumer;
public class BlastingLancer extends SwordItem implements Vanishable {
    public static final UUID REACH_MOD = UUID.fromString("dccd59ec-6391-436d-9e00-47f2e6005e20");
    private int abilityDelta;
    private boolean flag;

    public BlastingLancer() {
        super(ModTiers.BLASTING_LANCER, 10, -2.8f, new Item.Properties().fireResistant());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? ATTRIBUTE_LAZY_MAP.get() : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        Level world = entity.level();
        Vec3 viewVec = entity.getViewVector(1.0F);
        Vec3 eyeVec = entity.getEyePosition(1.0F);
        Vec3 targetVec = eyeVec.add(viewVec.x * 4, viewVec.y * 4, viewVec.z * 4);
        AABB viewBB = entity.getBoundingBox().expandTowards(viewVec.scale(4)).inflate(4.0D, 4.0D, 4.0D);
        EntityHitResult result = ProjectileUtil.getEntityHitResult(world, entity, eyeVec, targetVec, viewBB, EntitySelector.NO_CREATIVE_OR_SPECTATOR, 8f);
        if (result == null || !(result.getEntity() instanceof LivingEntity target))
            return false;
        if (target != null) {
            double distanceToTargetSqr = entity.distanceToSqr(target);
            if (entity instanceof Player player && 8 >= distanceToTargetSqr)
                player.doHurtTarget(target);
        }
        return super.onEntitySwing(stack, entity);
    }
    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        LivingEntityIsHitWithTreasureWeaponProcedure.onHit(entity, itemstack);
        return super.hurtEnemy(itemstack, entity, sourceentity);
    }
    public static Lazy<? extends Multimap<Attribute, AttributeModifier>> ATTRIBUTE_LAZY_MAP = Lazy.of(() -> {
        Multimap<Attribute, AttributeModifier> map;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 10f, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8, AttributeModifier.Operation.ADDITION));
        if (ForgeMod.ENTITY_REACH.isPresent())
            builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(REACH_MOD, "Weapon modifier", 2, AttributeModifier.Operation.ADDITION));
        map = builder.build();
        return map;
    });
    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = super.useOn(context);
        this.flag = false;
        this.abilityDelta = 20;
        TreasureWeaponOnBeaconClick.onClick(context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getItemInHand());
        return result;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!this.flag && !player.isFallFlying()) {
            this.abilityDelta = 20;
            this.flag = true;
        }
        return super.use(level, player, hand);
    }

    private ItemStack createFirework(ItemStack stack) {
        CompoundTag fireworkTag = new CompoundTag();
        CompoundTag fireworksTag = new CompoundTag();
        int[] colors = new int[]{0xFFC600};
        int getState = (int) stack.getOrCreateTag().getDouble("CustomModelData");
        switch (getState){
            case 0 -> colors = new int[]{0xFFC600};
            case 1 -> colors = new int[]{0x3D405B};
            case 2 -> colors = new int[]{0x4A5043};
            case 3 -> colors = new int[]{0xF96F5D};
        }
        ListTag explosionsTag = new ListTag();
        CompoundTag explosionTag = new CompoundTag();
        explosionTag.putInt("Type", 1);
        explosionTag.putIntArray("Colors", colors);
        explosionTag.putIntArray("FadeColors", new int[]{0xFCF300});
        explosionTag.putBoolean("Flicker", true);
        explosionTag.putBoolean("Trail", true);
        explosionsTag.add(explosionTag);
        fireworksTag.put("Explosions", explosionsTag);
        fireworksTag.putInt("Flight", stack.getOrCreateTag().getDouble("CustomModelData") > 0 ? 6 : 5);
        fireworkTag.put("Fireworks", fireworksTag);
        stack.setTag(fireworkTag);
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean p_41408_) {
        if (entity instanceof Player player) {
            if ((player.getItemInHand(InteractionHand.MAIN_HAND).is(stack.getItem()) || player.getItemInHand(InteractionHand.OFF_HAND).is(stack.getItem())) && this.flag) {
                if (abilityDelta > -20)
                    abilityDelta--;
                if (abilityDelta == 0){
                    ItemStack stack1 = this.createFirework(stack.copy());
                    Projectile projectile = new FireworkRocketEntity(level, stack1, player,player.getX(), player.getEyeY() - (double)0.15F, player.getZ(), true);
                    Vec3 vec31 = player.getUpVector(1.0F);
                    Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0, vec31.x, vec31.y, vec31.z);
                    Vec3 vec3 = player.getViewVector(1.0F);
                    Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
                    projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 3.15F, 1);
                    level.addFreshEntity(projectile);
                }
                if (abilityDelta <= -20)
                    this.flag = false;
            }
        }
        super.inventoryTick(stack, level, entity, i, p_41408_);
    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final HumanoidModel.ArmPose BLASTING = HumanoidModel.ArmPose.create("BLASTING", false, (model, entity, arm) -> {
                if (arm == HumanoidArm.RIGHT) {
                    model.rightArm.xRot = -0.5f + BlastingLancer.this.abilityDelta > 0 ? BlastingLancer.this.abilityDelta/10f + 0.2f : -BlastingLancer.this.abilityDelta/20f;
                } else {
                    model.leftArm.xRot = -0.5f + BlastingLancer.this.abilityDelta > 0 ? BlastingLancer.this.abilityDelta/10f + 0.2f : -BlastingLancer.this.abilityDelta/20f;
                }
            });

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return BlastingLancer.this.flag ? BLASTING : HumanoidModel.ArmPose.ITEM;
            }

            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                int k = arm == HumanoidArm.RIGHT ? 1 : -1;
                poseStack.translate(k * 0.56F, -0.52F, -0.72F);
                if (BlastingLancer.this.flag && BlastingLancer.this.abilityDelta > -20) {
                    poseStack.mulPose(Axis.XN.rotationDegrees(45f));
                    poseStack.translate(k * 0.56F, -0.52F, -0.22F + BlastingLancer.this.abilityDelta > 0 ? BlastingLancer.this.abilityDelta/10f -0.15f : -BlastingLancer.this.abilityDelta/10f);
                }
                return BlastingLancer.this.flag && BlastingLancer.this.abilityDelta > -20;
            }
        });
    }
}