package net.sashakyotoz.common.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.client.particles.custom.effects.LightVibrationParticleEffect;
import net.sashakyotoz.client.particles.custom.effects.WindVibrationParticleEffect;
import net.sashakyotoz.common.config.ConfigController;
import net.sashakyotoz.common.items.custom.ChimericRockbreakerHammerItem;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.List;

public class AbilityClickC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        ItemStack stack = player.getMainHandStack();
        player.swingHand(Hand.MAIN_HAND);
        if (stack.isIn(ModTags.Items.CAN_BE_CHARGED_BY_GRIPCRYSTALS) && GripcrystalManaData.getMana((IEntityDataSaver) player) > 0) {
            if (stack.getItem() instanceof EclipsebaneItem item
                    && item.getItemPhase(player.getMainHandStack()).equals("light_ray")
                    && GripcrystalManaData.getMana((IEntityDataSaver) player) > 6) {
                GripcrystalManaData.removeMana((IEntityDataSaver) player, 6);
                player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA, 3, 2.5f);
                float scaling = 0;
                World world = player.getWorld();
                BlockPos particlePos = player.getWorld().raycast(new RaycastContext(player.getEyePos(), player.getEyePos().add(player.getRotationVec(1f).multiply(9)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getBlockPos();
                player.getServerWorld().spawnParticles(new LightVibrationParticleEffect(new BlockPositionSource(particlePos), 15), player.getX(), player.getY() + 1, player.getZ(), 3, 0, 0, 0, 1);
                for (int i1 = 0; i1 < 9; i1++) {
                    BlockPos pos = world.raycast(new RaycastContext(player.getEyePos(), player.getEyePos().add(player.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getBlockPos();
                    if (!player.getWorld().getBlockState(pos).isOpaque())
                        scaling = scaling + 1;
                    BlockPos pos1 = player.getWorld().raycast(new RaycastContext(player.getEyePos(), player.getEyePos().add(player.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getBlockPos();
                    List<LivingEntity> entities = player.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos1.toCenterPos(), pos1.toCenterPos()).expand(1.25), LivingEntity::canHit);
                    for (LivingEntity entity : entities) {
                        if (entity != player)
                            entity.damage(player.getDamageSources().magic(), 10);
                    }
                }
            }
            if (stack.getItem() instanceof ChimericRockbreakerHammerItem item
                    && item.getItemPhase(player.getMainHandStack()).equals("heavy_winding")
                    && GripcrystalManaData.getMana((IEntityDataSaver) player) > 12) {
                GripcrystalManaData.removeMana((IEntityDataSaver) player, 12);
                player.playSound(SoundEvents.ENTITY_LLAMA_SWAG, 2, 1.75f);
                float scaling = 0;
                World world = player.getWorld();
                BlockPos particlePos = player.getWorld().raycast(new RaycastContext(player.getEyePos(), player.getEyePos().add(player.getRotationVec(1f).multiply(10)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getBlockPos();
                player.getServerWorld().spawnParticles(new WindVibrationParticleEffect(new BlockPositionSource(particlePos), 15), player.getX(), player.getY() + 1, player.getZ(), 3, 0, 0, 0, 1);
                for (int i1 = 0; i1 < 10; i1++) {
                    BlockPos pos = world.raycast(new RaycastContext(player.getEyePos(), player.getEyePos().add(player.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getBlockPos();
                    if (!player.getWorld().getBlockState(pos).isOpaque())
                        scaling = scaling + 1;
                    else
                        player.getWorld().createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 2, World.ExplosionSourceType.BLOCK);
                    BlockPos pos1 = player.getWorld().raycast(new RaycastContext(player.getEyePos(), player.getEyePos().add(player.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getBlockPos();
                    List<LivingEntity> entities = player.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos1.toCenterPos(), pos1.toCenterPos()).expand(1.25), LivingEntity::canHit);
                    for (LivingEntity entity : entities) {
                        if (entity != player) {
                            entity.damage(player.getDamageSources().magic(), 6);
                            entity.setFrozenTicks(100);
                            entity.setVelocity(
                                    ActionsUtils.getXVector(2, player.getYaw()),
                                    ActionsUtils.getYVector(0.5, player.getPitch()),
                                    ActionsUtils.getZVector(2, player.getYaw())
                            );
                        }
                    }
                }
            }
        }
        ConfigController.DataItem dataItem = ConfigController.getDataToStack(stack);
        if (dataItem != null) {
            if (GripcrystalManaData.getMana((IEntityDataSaver) player) > dataItem.manaCost()) {
                if (dataItem.actionToPerform() != null)
                    dataItem.actionToPerform().forEach(s -> {
                        if (!s.contains("damageAll"))
                            player.getServerWorld().getServer().getCommandManager().executeWithPrefix(
                                    new ServerCommandSource(player, new Vec3d(player.getX(), player.getY(), player.getZ()), Vec2f.ZERO, player.getServerWorld(),
                                            4, "", Text.literal(""), server, player)
                                            .withOutput(CommandOutput.DUMMY), s
                            );
                        else
                            ActionsUtils.hitNearbyMobs(player, ConfigController.getIntegers(s).get(0), ConfigController.getIntegers(s).get(1));
                    });
                GripcrystalManaData.removeMana((IEntityDataSaver) player, dataItem.manaCost());
                player.getItemCooldownManager().set(stack.getItem(), 20);
            }
        }
    }
}