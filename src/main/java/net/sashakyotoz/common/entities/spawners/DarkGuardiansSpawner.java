package net.sashakyotoz.common.entities.spawners;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.Spawner;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.DarkGuardianEntity;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.ModDimensions;

public class DarkGuardiansSpawner implements Spawner {

    public DarkGuardiansSpawner() {
        UnseenWorld.log("Dark guardians spawner was set up");
    }

    private int cooldown;

    @Override
    public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
        if (!spawnMonsters) {
            return 0;
        } else if (!world.getGameRules().getBoolean(GameRules.DO_PATROL_SPAWNING)) {
            return 0;
        } else {
            Random random = world.random;
            this.cooldown--;
            if (this.cooldown > 0) {
                return 0;
            } else {
                this.cooldown = this.cooldown + 12000 + random.nextInt(1200);
                long l = world.getTime() / 24000L;
                if (l < 4L) {
                    return 0;
                } else if (random.nextInt(4) != 0) {
                    return 0;
                } else {
                    int i = world.getPlayers().size();
                    if (i < 1) {
                        return 0;
                    } else {
                        PlayerEntity playerEntity = world.getPlayers().get(random.nextInt(i));
                        if (playerEntity.isSpectator()) {
                            return 0;
                        } else if (world.isNearOccupiedPointOfInterest(playerEntity.getBlockPos(), 2)) {
                            return 0;
                        } else {
                            int j = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                            int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                            BlockPos.Mutable mutable = playerEntity.getBlockPos().mutableCopy().move(j, 0, k);
                            int m = 10;
                            if (!world.isRegionLoaded(mutable.getX() - m, mutable.getZ() - m, mutable.getX() + m, mutable.getZ() + m)) {
                                return 0;
                            } else {
                                if (!world.getDimensionKey().equals(ModDimensions.CHIMERIC_DARKNESS_TYPE)) {
                                    return 0;
                                } else {
                                    int n = 0;
                                    int o = (int) Math.ceil(world.getLocalDifficulty(mutable).getLocalDifficulty()) + 1;

                                    for (int p = 0; p < o; p++) {
                                        n++;
                                        mutable.setY(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutable).getY());
                                        if (p == 0) {
                                            if (!this.spawnGuardians(world, mutable, true)) {
                                                break;
                                            }
                                        } else {
                                            this.spawnGuardians(world, mutable, false);
                                        }

                                        mutable.setX(mutable.getX() + random.nextInt(5) - random.nextInt(5));
                                        mutable.setZ(mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                                    }

                                    return n;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean spawnGuardians(ServerWorld world, BlockPos pos, boolean captain) {
        BlockState blockState = world.getBlockState(pos);
        if (!SpawnHelper.isClearForSpawn(world, pos, blockState, blockState.getFluidState(), ModEntities.DARK_GUARDIAN)) {
            return false;
        } else {
            DarkGuardianEntity guardianEntity = ModEntities.DARK_GUARDIAN.create(world);
            if (guardianEntity != null) {
                if (captain) {
                    ItemStack stack = new ItemStack(ModItems.ABYSSAL_HELMET);
                    stack.addEnchantment(Enchantments.PROTECTION, 4);
                    guardianEntity.equipStack(EquipmentSlot.HEAD, stack);
                }
                guardianEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                guardianEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.PATROL, null, null);
                world.spawnEntityAndPassengers(guardianEntity);
                return true;
            } else {
                return false;
            }
        }
    }
}