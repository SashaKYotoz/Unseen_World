package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ModFoodItem extends Item {
    private final MobEffectInstance effect;
    public ModFoodItem(Properties properties,MobEffectInstance effect) {
        super(properties);
        this.effect = effect;
    }
    @Override
    public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
        if (!entity.level().isClientSide())
            entity.addEffect(effect);
        return super.finishUsingItem(itemstack, world, entity);
    }
}
