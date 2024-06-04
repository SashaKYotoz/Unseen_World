package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.UnseenWorldConfigs;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModEnchantments;
import net.minecraftforge.common.TierSortingRegistry;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;

public class MiningBootsProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        double sx, sy, sz;
        if (!UnseenWorldConfigs.DEACTIVATE_MINING_BOOTS.get()) {
            if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getEnchantmentLevel(UnseenWorldModEnchantments.MININGBOOTS.get()) > 0) {
                sx = -1;
                for (int index0 = 0; index0 < 3; index0++) {
                    sy = -1;
                    for (int index1 = 0; index1 < 3; index1++) {
                        sz = -1;
                        for (int index2 = 0; index2 < 3; index2++) {
                            if (new Object() {
                                public int getHarvestLevel(BlockState _bs) {
                                    return TierSortingRegistry.getSortedTiers().stream().filter(t -> t.getTag() != null && _bs.is(t.getTag())).map(Tier::getLevel).findFirst().orElse(0);
                                }
                            }.getHarvestLevel(world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))) <= 4
                                    && !((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == Blocks.BEDROCK || (world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == Blocks.END_PORTAL_FRAME)) {
                                {
                                    BlockPos _pos = BlockPos.containing(x + sx, y + sy, z + sz);
                                    Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
                                    world.destroyBlock(_pos, false);
                                }
                            }
                            sz = sz + 1;
                        }
                        sy = sy - 1;
                    }
                    sx = sx + 1;
                }
                ItemStack stack = (entity instanceof LivingEntity livingEntity ? livingEntity.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY);
                if (stack.hurt((int) (Mth.nextDouble(RandomSource.create(), 1, 6) - (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getEnchantmentLevel(Enchantments.UNBREAKING)),
                        RandomSource.create(), null)) {
                    stack.shrink(1);
                    stack.setDamageValue(0);
                }
            }
        }
    }
}
