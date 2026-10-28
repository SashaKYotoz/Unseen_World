package net.sashakyotoz.common.items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.items.combat.AbilityHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShieldOfChimericWarriorItem extends ShieldItem {
    public ShieldOfChimericWarriorItem(Properties settings) {
        super(settings);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return this.getDescriptionId();
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return repair.is(ModItems.GRIPTONITE);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        AbilityHelper.appendAbilityLines(this.getDescriptionId(), 2, tooltip);
    }
}
