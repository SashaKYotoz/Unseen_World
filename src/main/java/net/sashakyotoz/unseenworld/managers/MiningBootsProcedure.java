package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.level.Level;
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
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        double sx, sy, sz;
        Level level = entity.level();
        if (!UnseenWorldConfigs.DEACTIVATE_MINING_BOOTS.get()) {
            if (entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.FEET).getEnchantmentLevel(UnseenWorldModEnchantments.MININGBOOTS.get()) > 0) {
                sx = -1;
                for (int i = 0; i < 3; i++) {
                    sy = -1;
                    for (int j = 0; j < 3; j++) {
                        sz = -1;
                        for (int k = 0; k < 3; k++) {
                            if (new Object() {
                                public int getHarvestLevel(BlockState _bs) {
                                    return TierSortingRegistry.getSortedTiers().stream().filter(t -> t.getTag() != null && _bs.is(t.getTag())).map(Tier::getLevel).findFirst().orElse(0);
                                }
                            }.getHarvestLevel(level.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))) <= 4
                                    && !(level.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz)).getBlock().defaultDestroyTime() <= -1)) {
                                BlockPos pos = BlockPos.containing(x + sx, y + sy, z + sz);
                                Block.dropResources(level.getBlockState(pos), level, BlockPos.containing(x, y, z), null);
                                level.destroyBlock(pos, false);
                            }
                            sz = sz + 1;
                        }
                        sy = sy - 1;
                    }
                    sx = sx + 1;
                }
                ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
                if (stack.hurt((int) (Mth.nextDouble(RandomSource.create(), 1, 6) - stack.getEnchantmentLevel(Enchantments.UNBREAKING)),
                        RandomSource.create(), null)) {
                    stack.shrink(1);
                    stack.setDamageValue(0);
                }
            }
        }
    }
}
