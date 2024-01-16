package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;

public class SmallCrimserrySoulBerryPlantRightClickedProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
        if (entity == null)
            return;
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.BONE_MEAL) {
            if (entity instanceof LivingEntity livingEntity) {
                ItemStack itemStack = new ItemStack(Items.BONE_MEAL);
                itemStack.setCount(livingEntity.getMainHandItem().getCount() - 1);
                livingEntity.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                if (livingEntity instanceof Player playerInventory)
                    playerInventory.getInventory().setChanged();
            }
            if (Math.random() < 0.25) {
                if ((blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty property ? blockstate.getValue(property) : -1) == 0) {
                    int _value = 1;
                    BlockPos _pos = BlockPos.containing(x, y, z);
                    BlockState _bs = world.getBlockState(_pos);
                    if (_bs.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
                        world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
                } else if ((blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty property ? blockstate.getValue(property) : -1) == 1) {
                    int _value = 2;
                    BlockPos _pos = BlockPos.containing(x, y, z);
                    BlockState _bs = world.getBlockState(_pos);
                    if (_bs.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
                        world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
                }
            }
        }
    }
}
