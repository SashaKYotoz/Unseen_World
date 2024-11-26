package net.sashakyotoz.common.entities.spawners;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.Spawner;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.DarkGuardianEntity;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.utils.JsonWorldController;

import java.util.List;

public class DarkGuardiansSpawner implements Spawner {

    public DarkGuardiansSpawner() {
        UnseenWorld.log("Dark guardians spawner was set up");
    }

    private int pastTick;

    @Override
    public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
        List<ServerPlayerEntity> players = world.getPlayers();
        for (ServerPlayerEntity player : players) {
            if (world.getDimensionKey() == ModDimensions.CHIMERIC_DARKNESS_TYPE
                    && checkGripcrystalInInventory(player.getInventory())) {
                if (this.pastTick < 9000)
                    this.pastTick++;
                else {
                    spawnGuardian(world, getNearbySpawnPos(world, player, player.getBlockPos(), 8), true);
                    for (int i = 0; i < 3; i++) {
                        spawnGuardian(world, getNearbySpawnPos(world, player, player.getBlockPos(), 7), false);
                    }
                    world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_WANDERING_TRADER_DISAPPEARED, SoundCategory.HOSTILE, 2, 2);
                    pastTick = 0;
                    break;
                }
            }
        }
        return pastTick;
    }

    private boolean checkGripcrystalInInventory(PlayerInventory inventory) {
        for (int i = 0; i < inventory.size() - 1; i++) {
            if (inventory.getStack(i).isOf(ModItems.GRIPCRYSTAL))
                return !JsonWorldController.data.get(0).starsUnlock();
        }
        return false;
    }

    private BlockPos getNearbySpawnPos(ServerWorld world, PlayerEntity player, BlockPos pos, int range) {
        BlockPos blockPos = pos;
        for (int i = 0; i < 10; i++) {
            int j = pos.getX() + world.getRandom().nextInt(range * 2) - range;
            int k = pos.getZ() + world.getRandom().nextInt(range * 2) - range;
            int l = (int) (player.getY() + 1);
            BlockPos blockPos2 = new BlockPos(j, l, k);
            if (SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, blockPos2, ModEntities.DARK_GUARDIAN)) {
                blockPos = blockPos2;
                break;
            }
        }

        return blockPos;
    }

    private void spawnGuardian(ServerWorld world, BlockPos pos, boolean captain) {
        DarkGuardianEntity guardianEntity = ModEntities.DARK_GUARDIAN.create(world);
        if (guardianEntity != null) {
            if (captain) {
                ItemStack stack = new ItemStack(ModItems.ABYSSAL_HELMET);
                stack.addEnchantment(Enchantments.PROTECTION, 4);
                guardianEntity.equipStack(EquipmentSlot.HEAD, stack);
            }
            guardianEntity.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
            guardianEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.PATROL, null, null);
            world.spawnEntityAndPassengers(guardianEntity);
        }
    }
}