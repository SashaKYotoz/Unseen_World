package net.sashakyotoz.unseenworld.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.*;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.unseenworld.UnseenWorldConfigs;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.*;

@Mod.EventBusSubscriber
public class EventManager {
    public static int shakingTime = 0;
    public static Vec3 vec3 = new Vec3(0,-256,0);

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getHand() != event.getEntity().getUsedItemHand())
            return;
        claymoreAbility(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getEntity());
    }
    @SubscribeEvent
    public static void furnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
        ItemStack itemstack = event.getItemStack();
        if (itemstack.getItem() == UnseenWorldItems.DARK_WATER_BUCKET.get())
            event.setBurnTime(2400);
        else if (itemstack.getItem() == UnseenWorldBlocks.CRIMSERRY_SOUL_CROP.get().asItem())
            event.setBurnTime(60);
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
        if (event.getItemStack().is(UnseenWorldItems.VOID_ENDERMAN_SWORD.get()))
            voidEndermanSwordClick(event.getLevel(),event.getPos(),event.getEntity());
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
        if (minecraft.getCameraEntity() instanceof Player player && !player.isSpectator() && player.distanceToSqr(vec3) < 12){
            float delta = Minecraft.getInstance().getFrameTime();
            float ticksExistedDelta = player.tickCount + delta;
            float intensity = 0.025f;
            if (!minecraft.isPaused() && player.level().isClientSide() && shakingTime > 0) {
                event.setPitch((float) (event.getPitch() + intensity * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                event.setYaw((float) (event.getYaw() + intensity * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                event.setRoll((float) (event.getRoll() + intensity * Math.cos(ticksExistedDelta * 4) * 25));
            }
            else if (shakingTime <= 0)
                vec3 = new Vec3(0,-256,0);
        }
    }
    public static void diggingParticles(LivingEntity entity,boolean flag) {
        if (flag) {
            RandomSource randomsource = entity.getRandom();
            BlockState blockstate = entity.level().getBlockState(entity.getOnPos().below(2));
            if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                for (int i = 0; i < 30; ++i) {
                    double d0 = entity.getX() + (double) Mth.randomBetween(randomsource, -0.7F, 0.7F);
                    double d1 = entity.getY() + 0.5f;
                    double d2 = entity.getZ() + (double) Mth.randomBetween(randomsource, -0.7F, 0.7F);
                    entity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
    public static void waveFlaming(ParticleOptions options,Level level, BlockPos pos){
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            UnseenWorldMod.queueServerWork(20 + 10 * i,()-> addParticles(options,level,pos.getX(),pos.getY(),pos.getZ(),2 * finalI));
        }
    }
    public static void addParticles(ParticleOptions type, Level level, double x, double y, double z, float modifier) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                level.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier);
        }
    }
    private static void claymoreAbility(double x, double y, double z, Player player) {
        if (player == null)
            return;
        if (player.getMainHandItem().is(UnseenWorldItems.HEAVY_CLAYMORE.get()) || player.getOffhandItem().is(UnseenWorldItems.HEAVY_CLAYMORE.get())) {
            shakingTime +=30;
            vec3 = new Vec3(player.getOnPos().getX(),player.getOnPos().getY(),player.getOnPos().getZ());
            if (player.onGround()){
                for (int i = 0; i < 10 && player.tickCount % 10 == 0; i++) {
                    diggingParticles(player,i < 9);
                }
            }
            if (player.level() instanceof ServerLevel level)
                level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, level, 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
                        "/effect give @e[distance=..7,type=!minecraft:player,type=!minecraft:wolf] minecraft:levitation 2 3");
        }
    }

    private static void hammerAbilities(LivingEntity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (sourceentity instanceof Player player) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == UnseenWorldItems.VOID_HAMMER.get()) {
                if (!entity.level().isClientSide())
                    entity.addEffect(new MobEffectInstance(UnseenWorldMobEffects.DARK_VOID.get(), 20, 0));
                if (!player.level().isClientSide()) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 4));
                    player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 60);
                }
            }
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == UnseenWorldItems.REDNESS_HAMMER.get()) {
                entity.setSecondsOnFire(10);
                if (!entity.level().isClientSide()) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 4));
                    player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 60);
                }
            }
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == UnseenWorldItems.AMETHYST_HAMMER.get()) {
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
        for (int i = 0; i < 4; i++) {
            sy = -2;
            for (int j = 0; j < 4; j++) {
                sz = -2;
                for (int k = 0; k < 4; k++) {
                    if ((level.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldBlocks.TOTEM_OF_GUDDY_BLAZE.get()) {
                        level.destroyBlock(BlockPos.containing(x + sx, y + sy, z + sz), false);
                        if (level instanceof ServerLevel serverLevel) {
                            Entity entityToSpawn = UnseenWorldEntities.SAVAGE_SMALL_BLAZE.get().spawn(serverLevel, BlockPos.containing(x + sx, y + sy, z + sz), MobSpawnType.MOB_SUMMONED);
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
        if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UnseenWorldBlocks.TOTEM_OF_GUDDY_BLAZE.get()) {
            world.removeBlock(BlockPos.containing(x,y,z),true);
            world.addParticle(UnseenWorldParticleTypes.REDNESS.get(), x, y, z, 0, 1, 0);
            if (world instanceof ServerLevel level) {
                Entity entityToSpawn = UnseenWorldEntities.SAVAGE_SMALL_BLAZE.get().spawn(level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                if (entityToSpawn != null) {
                    entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
                }
            }
        }
    }

    private static void shinyBladeAction(LevelAccessor world, double x, double y, double z, Player player) {
        if (player == null)
            return;
        if (!UnseenWorldConfigs.DEACTIVATE_SHINING_BLADE.get()) {
            double shiningPower = (player.getMainHandItem().getEnchantmentLevel(UnseenWorldEnchantments.SHINING_BLADE.get()));
            if (shiningPower > 0) {
                if (player.getMainHandItem().is(UnseenWorldItems.TANZANITE_STAFF.get()) || player.getOffhandItem().is(UnseenWorldItems.TANZANITE_STAFF.get()))
                    shiningPower += 3;
                if (world instanceof ServerLevel level)
                    level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, level, 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
                            "/effect give @e[distance=.." + shiningPower * 2 + UnseenWorldConfigs.SHINING_BLADE_POWER.get() + " ,type=!minecraft:player] minecraft:glowing 5");
            }
        }
    }

    private static void gravityThornsAction(Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (!UnseenWorldConfigs.DEACTIVATE_GRAVITY_SPIKES.get()) {
            double enchantLevel = (entity instanceof LivingEntity living ? living.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getEnchantmentLevel(UnseenWorldEnchantments.GRAVITY_SPIKE.get());
            if (enchantLevel > 0) {
                sourceentity.setDeltaMovement(new Vec3(0, ((enchantLevel / 2.5f + UnseenWorldConfigs.GRAVITY_SPIKES_POWER.get())), 0));
            }
        }
    }

    private static void lifeSteelAction(LivingEntity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (!UnseenWorldConfigs.DEACTIVATE_LIFE_STEELING.get()) {
            int lifeSteel = (sourceentity instanceof LivingEntity livingEntity ? livingEntity.getMainHandItem() : ItemStack.EMPTY).getEnchantmentLevel(UnseenWorldEnchantments.LIFE_STEEL.get());
            if (lifeSteel > 0) {
                if (sourceentity instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide()) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60 + UnseenWorldConfigs.LIFE_STEELING_POWER.get() * 10, lifeSteel + UnseenWorldConfigs.LIFE_STEELING_POWER.get()));
                    if (!entity.hasEffect(MobEffects.HARM))
                        entity.addEffect(new MobEffectInstance(MobEffects.HARM, 1, -1 + (lifeSteel > 2 ? 3 : 1)));
                }
            }
        }
    }

    private static void randomEffectGiving(LevelAccessor world, Player player) {
        if (player == null)
            return;
        if ((world instanceof Level level ? level.dimension() : Level.OVERWORLD) == (ResourceKey.create(Registries.DIMENSION, new ResourceLocation("unseen_world:the_darkness"))) && Math.random() < 0.0025 && UnseenWorldConfigs.SPEC.isLoaded()) {
            if (Math.random() < UnseenWorldConfigs.METEORITESTROPHY_CHANCE.get()) {
                if (!player.level().isClientSide())
                    player.addEffect(new MobEffectInstance(UnseenWorldMobEffects.METEORITESTROPHY.get(), 100, 1));
            } else if (Math.random() < UnseenWorldConfigs.REDUCING_OF_GRAVITY_CHANCE.get()) {
                if (!player.level().isClientSide())
                    player.addEffect(new MobEffectInstance(UnseenWorldMobEffects.REDUCED_OF_GRAVITY.get(), 600, 0));
            }
        }
    }

    private static void unseenToolsAbility(Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (sourceentity instanceof LivingEntity livingEntity && (livingEntity.getMainHandItem().is(UnseenWorldItems.UNSEEN_SWORD.get())
                || livingEntity.getMainHandItem().is(UnseenWorldItems.UNSEEN_PICKAXE.get())
                || livingEntity.getMainHandItem().is(UnseenWorldItems.UNSEEN_AXE.get())
                || livingEntity.getMainHandItem().is(UnseenWorldItems.UNSEEN_SHOVEL.get())
                || livingEntity.getMainHandItem().is(UnseenWorldItems.UNSEEN_HOE.get()))) {
            if (entity instanceof LivingEntity living && !living.level().isClientSide())
                living.addEffect(new MobEffectInstance(UnseenWorldMobEffects.UNSEENIUM.get(), 60, 1));
        }
    }

    private static void netheriumStaffCharging(Player player) {
        if (player == null)
            return;
        if ((player.getMainHandItem().is(Items.BLAZE_POWDER) && player.getOffhandItem().is(UnseenWorldItems.NETHERIUM_STAFF.get())) ||
                player.getMainHandItem().is(UnseenWorldItems.TEALIVE_STONY_SHARD.get()) && player.getOffhandItem().is(UnseenWorldItems.TEALIVY_FIRE_STAFF.get())) {
            player.getOffhandItem().setDamageValue(player.getOffhandItem().getDamageValue() - 1);
            player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);
        }
    }
    private static void voidEndermanSwordClick(LevelAccessor world, BlockPos pos, LivingEntity entity){
        double speed = 0.75;
        double Yaw = entity.getYRot();
        entity.setDeltaMovement(new Vec3((speed * Math.cos((Yaw + 90) * (Math.PI / 180))), (entity.getXRot() * (-0.025)), (speed * Math.sin((Yaw + 90) * (Math.PI / 180)))));
        if (world.getBlockState(pos.below(2)).canOcclude() || world.getBlockState(pos.below(3)).canOcclude() || world.getBlockState(pos.below(4)).canOcclude()) {
            if (!entity.level().isClientSide())
                entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 60, 0));
        }
    }
}
