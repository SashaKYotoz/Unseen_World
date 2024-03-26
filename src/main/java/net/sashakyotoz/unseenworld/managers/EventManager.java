package net.sashakyotoz.unseenworld.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.unseenworld.UnseenWorldModConfigs;
import net.sashakyotoz.unseenworld.util.*;

@Mod.EventBusSubscriber
public class EventManager {
    public static int shakingTime = 0;

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getHand() != event.getEntity().getUsedItemHand())
            return;
        claymoreAbility(event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getEntity());
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {
            lifeSteelAction(event.getEntity(), event.getSource().getEntity());
            gravityThornsAction(event.getEntity(), event.getSource().getEntity());
            unseenToolsAbility(event.getEntity(), event.getSource().getEntity());
            hammerAbilities(event.getEntity(), event.getSource().getEntity());
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != event.getEntity().getUsedItemHand())
            return;
        savageBlazeMonumentClicked(event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getHand() != event.getEntity().getUsedItemHand())
            return;
        netheriumStaffCharging(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (shakingTime > 0)
            shakingTime--;
        if (event.phase == TickEvent.Phase.END) {
            checkIfSavageBlazeMonumentIsClose(event.player.level(), event.player.getX(), event.player.getY(), event.player.getZ());
            randomEffectGiving(event.player.level(), event.player);
            shinyBladeAction(event.player.level(), event.player.getX(), event.player.getY(), event.player.getZ(), event.player);
            AdvancementManager.everyTickCheckingAdvancements(event.player.level(), event.player);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getCameraEntity() instanceof Player player && !player.isSpectator()) {
            float delta = Minecraft.getInstance().getFrameTime();
            float ticksExistedDelta = player.tickCount + delta;
            float intensity = 0.025f;
            if (!minecraft.isPaused() && player.level().isClientSide() && shakingTime > 0) {
                event.setPitch((float) (event.getPitch() + intensity * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                event.setYaw((float) (event.getYaw() + intensity * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                event.setRoll((float) (event.getRoll() + intensity * Math.cos(ticksExistedDelta * 4) * 25));
            }
        }
    }

    private static void claymoreAbility(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        if ((entity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY).getItem() == UnseenWorldModItems.HEAVY_CLAYMORE.get()
                || (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == UnseenWorldModItems.HEAVY_CLAYMORE.get()) {
            if (world instanceof ServerLevel level)
                level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, level, 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
                        "/effect give @e[distance=..7,type=!minecraft:player,type=!minecraft:wolf] minecraft:levitation 2 2");
        }
    }

    private static void hammerAbilities(LivingEntity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (sourceentity instanceof Player player) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == UnseenWorldModItems.VOID_HAMMER.get()) {
                if (!entity.level().isClientSide())
                    entity.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.DARK_VOID.get(), 20, 0));
                if (!player.level().isClientSide()) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 4));
                    player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 60);
                }
            }
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == UnseenWorldModItems.REDNESS_HAMMER.get()) {
                entity.setSecondsOnFire(10);
                if (!entity.level().isClientSide()) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 4));
                    player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 60);
                }
            }
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == UnseenWorldModItems.AMETHYST_HAMMER.get()) {
                if (!entity.level().isClientSide())
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 4));
                if (!player.level().isClientSide()) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 0));
                    player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 60);
                }
            }
        }
    }

    private static void checkIfSavageBlazeMonumentIsClose(Level level, double x, double y, double z) {
        double sx;
        double sy;
        double sz;
        sx = -2;
        for (int index0 = 0; index0 < 4; index0++) {
            sy = -2;
            for (int index1 = 0; index1 < 4; index1++) {
                sz = -2;
                for (int index2 = 0; index2 < 4; index2++) {
                    if ((level.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldModBlocks.TOTEM_OF_GUDDY_BLAZE.get()) {
                        level.destroyBlock(BlockPos.containing(x + sx, y + sy, z + sz), false);
                        if (level instanceof ServerLevel serverLevel) {
                            Entity entityToSpawn = UnseenWorldModEntities.SAVAGE_SMALL_BLAZE.get().spawn(serverLevel, BlockPos.containing(x + sx, y + sy, z + sz), MobSpawnType.MOB_SUMMONED);
                            if (entityToSpawn != null) {
                                entityToSpawn.setYRot(serverLevel.getRandom().nextFloat() * 360F);
                            }
                        }
                    }
                    sz = sz + 1;
                }
                sy = sy + 1;
            }
            sx = sx + 1;
        }
    }

    private static void savageBlazeMonumentClicked(LevelAccessor world, double x, double y, double z) {
        if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UnseenWorldModBlocks.TOTEM_OF_GUDDY_BLAZE.get()) {
            world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
            world.addParticle(UnseenWorldModParticleTypes.REDNESS.get(), x, y, z, 0, 1, 0);
            if (world instanceof ServerLevel level) {
                Entity entityToSpawn = UnseenWorldModEntities.SAVAGE_SMALL_BLAZE.get().spawn(level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                if (entityToSpawn != null) {
                    entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
                }
            }
        }
    }

    private static void shinyBladeAction(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        if (!UnseenWorldModConfigs.DEACTIVATE_SHINING_BLADE.get()) {
            double shiningPower = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getEnchantmentLevel(UnseenWorldModEnchantments.SHINING_BLADE.get());
            if (shiningPower > 0) {
                if (world instanceof ServerLevel level)
                    level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, level, 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
                            "/effect give @e[distance=.." + shiningPower + UnseenWorldModConfigs.SHINING_BLADE_POWER.get() + " ,type=!minecraft:player] minecraft:glowing 5");
            }
        }
    }

    private static void gravityThornsAction(Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (!UnseenWorldModConfigs.DEACTIVATE_GRAVITY_SPIKES.get()) {
            double enchantLevel = (entity instanceof LivingEntity living ? living.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getEnchantmentLevel(UnseenWorldModEnchantments.GRAVITY_SPIKE.get());
            if (enchantLevel > 0) {
                sourceentity.setDeltaMovement(new Vec3(0, ((enchantLevel / 2.5f + UnseenWorldModConfigs.GRAVITY_SPIKES_POWER.get())), 0));
            }
        }
    }

    private static void lifeSteelAction(LivingEntity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (!UnseenWorldModConfigs.DEACTIVATE_LIFE_STEELING.get()) {
            int lifeSteel = (sourceentity instanceof LivingEntity livingEntity ? livingEntity.getMainHandItem() : ItemStack.EMPTY).getEnchantmentLevel(UnseenWorldModEnchantments.LIFE_STEEL.get());
            if (lifeSteel > 0) {
                if (sourceentity instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide()) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60 + UnseenWorldModConfigs.LIFE_STEELING_POWER.get() * 10, lifeSteel + UnseenWorldModConfigs.LIFE_STEELING_POWER.get()));
                    if (!entity.hasEffect(MobEffects.HARM))
                        entity.addEffect(new MobEffectInstance(MobEffects.HARM, 1, -1 + (lifeSteel > 2 ? 3 : 1)));
                }
            }
        }
    }

    private static void randomEffectGiving(LevelAccessor world, Player player) {
        if (player == null)
            return;
        if ((world instanceof Level level ? level.dimension() : Level.OVERWORLD) == (ResourceKey.create(Registries.DIMENSION, new ResourceLocation("unseen_world:the_darkness"))) && Math.random() < 0.0025 && UnseenWorldModConfigs.SPEC.isLoaded()) {
            if (Math.random() < UnseenWorldModConfigs.METEORITESTROPHY_CHANCE.get()) {
                if (!player.level().isClientSide())
                    player.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.METEORITESTROPHY.get(), 100, 1));
            } else if (Math.random() < UnseenWorldModConfigs.REDUCING_OF_GRAVITY_CHANCE.get()) {
                if (!player.level().isClientSide())
                    player.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.REDUCED_OF_GRAVITY.get(), 600, 0));
            }
        }
    }

    private static void unseenToolsAbility(Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (sourceentity instanceof LivingEntity livingEntity && (livingEntity.getMainHandItem().is(UnseenWorldModItems.UNSEEN_SWORD.get())
                || livingEntity.getMainHandItem().is(UnseenWorldModItems.UNSEEN_PICKAXE.get())
                || livingEntity.getMainHandItem().is(UnseenWorldModItems.UNSEEN_AXE.get())
                || livingEntity.getMainHandItem().is(UnseenWorldModItems.UNSEEN_SHOVEL.get())
                || livingEntity.getMainHandItem().is(UnseenWorldModItems.UNSEEN_HOE.get()))) {
            if (entity instanceof LivingEntity living && !living.level().isClientSide())
                living.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.UNSEENIUM.get(), 60, 1));
        }
    }

    private static void netheriumStaffCharging(Player player) {
        if (player == null)
            return;
        if (player.getMainHandItem().is(Items.BLAZE_POWDER) && player.getOffhandItem().is(UnseenWorldModItems.NETHERIUM_STAFF.get())) {
            player.getOffhandItem().setDamageValue(player.getOffhandItem().getDamageValue() - 1);
            player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);
        }
    }
}
