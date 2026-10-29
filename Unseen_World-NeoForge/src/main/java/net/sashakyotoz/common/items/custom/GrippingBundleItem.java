package net.sashakyotoz.common.items.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BundleContents;
import net.sashakyotoz.common.items.combat.AbilityHelper;
import net.sashakyotoz.common.tags.ModTags;

import java.util.ArrayList;
import java.util.List;

public class GrippingBundleItem extends BundleItem {
    private static final int BAR_COLOR = Mth.color(0.4F, 0.6F, 1.0F);

    public GrippingBundleItem(Properties settings) {
        super(settings);
    }

    public static boolean contains(ItemStack bundle, Item item) {
        BundleContents contents = bundle.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null) {
            return false;
        }
        for (ItemStack stored : contents.items()) {
            if (stored.is(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean removeOne(ItemStack bundle, Item item) {
        BundleContents contents = bundle.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null || contents.isEmpty()) {
            return false;
        }
        List<ItemStack> remaining = new ArrayList<>();
        boolean removed = false;
        for (ItemStack stored : contents.itemsCopy()) {
            if (!removed && stored.is(item)) {
                stored.shrink(1);
                removed = true;
                if (stored.isEmpty()) {
                    continue;
                }
            }
            remaining.add(stored);
        }
        if (removed) {
            bundle.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(remaining));
        }
        return removed;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack bundle, Slot slot, ClickAction action, Player player) {
        if (action == ClickAction.SECONDARY && !slot.getItem().isEmpty() && !accepts(slot.getItem())) {
            return true;
        }
        return super.overrideStackedOnOther(bundle, slot, action, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack bundle, ItemStack carried, Slot slot, ClickAction action, Player player, SlotAccess carriedAccess) {
        if (action == ClickAction.SECONDARY && slot.allowModification(player) && !carried.isEmpty() && !accepts(carried)) {
            return true;
        }
        return super.overrideOtherStackedOnMe(bundle, carried, slot, action, player, carriedAccess);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        AbilityHelper.appendAbilityLines(this.getDescriptionId(), 1, tooltip);
    }

    private static boolean accepts(ItemStack stack) {
        return stack.is(ModTags.Items.GRIPPING_BUNDLE_CAN_HANDLE);
    }
}
