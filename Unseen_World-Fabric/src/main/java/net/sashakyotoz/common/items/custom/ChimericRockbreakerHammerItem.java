package net.sashakyotoz.common.items.custom;

import net.minecraft.world.item.PickaxeItem;
import net.sashakyotoz.common.items.ModTiers;

public class ChimericRockbreakerHammerItem extends PickaxeItem {

    public ChimericRockbreakerHammerItem(int attackDamage, float attackSpeed, Properties settings) {
        super(ModTiers.ROCKBREAKER_HAMMER, attackDamage, attackSpeed, settings);
    }
}