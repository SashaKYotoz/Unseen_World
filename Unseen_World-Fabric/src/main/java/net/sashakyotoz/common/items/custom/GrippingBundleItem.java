package net.sashakyotoz.common.items.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.level.Level;
import net.sashakyotoz.common.items.combat.AbilityHelper;
import net.sashakyotoz.common.tags.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GrippingBundleItem extends BundleItem {
    private static final String ITEMS_KEY = "Items";
    private static final int BAR_COLOR = Mth.color(0.4F, 0.6F, 1.0F);

    public GrippingBundleItem(Properties settings) {
        super(settings);
    }

    public static boolean contains(ItemStack bundle, Item item) {
        ListTag contents = contents(bundle);
        for (int index = 0; index < contents.size(); index++) {
            if (ItemStack.of(contents.getCompound(index)).is(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean removeOne(ItemStack bundle, Item item) {
        ListTag contents = contents(bundle);
        for (int index = 0; index < contents.size(); index++) {
            ItemStack stored = ItemStack.of(contents.getCompound(index));
            if (stored.is(item)) {
                stored.shrink(1);
                if (stored.isEmpty()) {
                    contents.remove(index);
                } else {
                    contents.set(index, stored.save(new CompoundTag()));
                }
                if (contents.isEmpty()) {
                    bundle.removeTagKey(ITEMS_KEY);
                }
                return true;
            }
        }
        return false;
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
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        AbilityHelper.appendAbilityLines(this.getDescriptionId(), 1, tooltip);
    }

    private static boolean accepts(ItemStack stack) {
        return stack.is(ModTags.Items.GRIPPING_BUNDLE_CAN_HANDLE);
    }

    private static ListTag contents(ItemStack bundle) {
        CompoundTag tag = bundle.getTag();
        return tag == null ? new ListTag() : tag.getList(ITEMS_KEY, Tag.TAG_COMPOUND);
    }
}
