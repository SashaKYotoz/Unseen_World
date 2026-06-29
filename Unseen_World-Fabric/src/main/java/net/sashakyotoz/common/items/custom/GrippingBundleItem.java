package net.sashakyotoz.common.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GrippingBundleItem extends Item {
    public GrippingBundleItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof ServerPlayer player && player.tickCount % 20 == 0) {
            int i = getGrippingModifier(stack);
            IEntityDataSaver keeper = (IEntityDataSaver) player;
            if (i < 0) {
                if (entity instanceof IGrippingEntity entity1 && entity1.getGrippingData() > 0)
                    GrippingData.addGrippingSeconds(entity1, i);
                else
                    GripcrystalManaData.removeMana(keeper, Math.abs(Math.round(i / 2f)));
            } else {
                if (GripcrystalManaData.getMana(keeper) > 49 && entity instanceof IGrippingEntity entity1 && entity1.getGrippingData() < 10)
                    GrippingData.addGrippingSeconds(entity1, i);
                else
                    GripcrystalManaData.addMana(keeper, i);
            }
            if (((entity instanceof IGrippingEntity entity1 && entity1.getGrippingData() > 0) || (GripcrystalManaData.getMana(keeper) < 48)))
                decrementFirstStack(stack);
        }
    }

    private int getGrippingModifier(ItemStack stack) {
        if (getBundleOccupancy(stack) > 0) {
            Item item = getBundledStacks(stack).toList().get(0).getItem();
            if (item.getDescriptionId().contains("gripcrystal"))
                return item.getDescriptionId().contains("granulated") ? 1 : 4;

            if (item.getDescriptionId().contains("griptonite"))
                return item.getDescriptionId().contains("granulated") ? -3 : -6;
        }
        return 0;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickType, Player player) {
        if (clickType != ClickAction.SECONDARY)
            return false;
        else {
            ItemStack itemStack = slot.getItem();
            if (itemStack.isEmpty()) {
                this.playRemoveOneSound(player);
                removeFirstStack(stack).ifPresent(removedStack -> addToBundle(stack, slot.safeInsert(removedStack)));
            } else if (itemStack.getItem().canFitInsideContainerItems()) {
                int i = (64 - getBundleOccupancy(stack)) / getItemOccupancy(itemStack);
                int j = addToBundle(stack, slot.safeTake(itemStack.getCount(), i, player));
                if (j > 0) {
                    this.playInsertSound(player);
                }
            }

            return true;
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
        if (clickType == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (otherStack.isEmpty()) {
                removeFirstStack(stack).ifPresent(itemStack -> {
                    this.playRemoveOneSound(player);
                    cursorStackReference.set(itemStack);
                });
            } else {
                int i = addToBundle(stack, otherStack);
                if (i > 0) {
                    this.playInsertSound(player);
                    otherStack.shrink(i);
                }
            }
            return true;
        } else
            return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (dropAllBundledItems(itemStack, user)) {
            this.playDropContentsSound(user);
            user.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemStack);
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getBundleOccupancy(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.min(1 + 12 * getBundleOccupancy(stack) / 64, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.color(0.4F, 0.6F, 1.0F);
    }

    private int addToBundle(ItemStack bundle, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().canFitInsideContainerItems() && stack.is(ModTags.Items.GRIPPING_BUNDLE_CAN_HANDLE)) {
            CompoundTag nbtCompound = bundle.getOrCreateTag();
            if (!nbtCompound.contains("Items"))
                nbtCompound.put("Items", new ListTag());

            int i = getBundleOccupancy(bundle);
            int j = getItemOccupancy(stack);
            int k = Math.min(stack.getCount(), (64 - i) / j);
            if (k == 0) {
                return 0;
            } else {
                ListTag nbtList = nbtCompound.getList("Items", Tag.TAG_COMPOUND);
                Optional<CompoundTag> optional = canMergeStack(stack, nbtList);
                if (optional.isPresent()) {
                    CompoundTag nbtCompound2 = optional.get();
                    ItemStack itemStack = ItemStack.of(nbtCompound2);
                    itemStack.grow(k);
                    itemStack.save(nbtCompound2);
                    nbtList.remove(nbtCompound2);
                    nbtList.add(0, nbtCompound2);
                } else {
                    ItemStack itemStack2 = stack.copyWithCount(k);
                    CompoundTag nbtCompound3 = new CompoundTag();
                    itemStack2.save(nbtCompound3);
                    nbtList.add(0, nbtCompound3);
                }

                return k;
            }
        } else {
            return 0;
        }
    }

    private static Optional<CompoundTag> canMergeStack(ItemStack stack, ListTag items) {
        return stack.is(ModItems.GRIPPING_BUNDLE)
                ? Optional.empty()
                : items.stream()
                .filter(CompoundTag.class::isInstance)
                .map(CompoundTag.class::cast)
                .filter(item -> ItemStack.isSameItemSameTags(ItemStack.of(item), stack))
                .findFirst();
    }

    private static int getItemOccupancy(ItemStack stack) {
        return 64 / stack.getMaxStackSize();
    }

    private int getBundleOccupancy(ItemStack stack) {
        return getBundledStacks(stack).mapToInt(itemStack -> getItemOccupancy(itemStack) * itemStack.getCount()).sum();
    }

    private Optional<ItemStack> removeFirstStack(ItemStack stack) {
        CompoundTag nbtCompound = stack.getOrCreateTag();
        if (!nbtCompound.contains("Items"))
            return Optional.empty();
        else {
            ListTag nbtList = nbtCompound.getList("Items", Tag.TAG_COMPOUND);
            if (nbtList.isEmpty()) {
                return Optional.empty();
            } else {
                CompoundTag nbtCompound2 = nbtList.getCompound(0);
                ItemStack itemStack = ItemStack.of(nbtCompound2);
                nbtList.remove(0);
                if (nbtList.isEmpty()) {
                    stack.removeTagKey("Items");
                }

                return Optional.of(itemStack);
            }
        }
    }

    private Optional<ItemStack> decrementFirstStack(ItemStack stack) {
        CompoundTag nbtCompound = stack.getOrCreateTag();
        if (!nbtCompound.contains("Items"))
            return Optional.empty();
        else {
            ListTag nbtList = nbtCompound.getList("Items", Tag.TAG_COMPOUND);
            if (nbtList.isEmpty()) {
                return Optional.empty();
            } else {
                CompoundTag nbtCompound2 = nbtList.getCompound(0);
                ItemStack itemStack = ItemStack.of(nbtCompound2);
                if (itemStack.getCount() > 1) {
                    itemStack.shrink(1);
                    nbtList.set(0, itemStack.save(new CompoundTag()));
                } else
                    nbtList.remove(0);
                if (nbtList.isEmpty())
                    stack.removeTagKey("Items");

                return Optional.of(itemStack);
            }
        }
    }

    private boolean dropAllBundledItems(ItemStack stack, Player player) {
        CompoundTag nbtCompound = stack.getOrCreateTag();
        if (!nbtCompound.contains("Items")) {
            return false;
        } else {
            if (player instanceof ServerPlayer) {
                ListTag nbtList = nbtCompound.getList("Items", Tag.TAG_COMPOUND);

                for (int i = 0; i < nbtList.size(); i++) {
                    CompoundTag nbtCompound2 = nbtList.getCompound(i);
                    ItemStack itemStack = ItemStack.of(nbtCompound2);
                    player.drop(itemStack, true);
                }
            }

            stack.removeTagKey("Items");
            return true;
        }
    }

    private Stream<ItemStack> getBundledStacks(ItemStack stack) {
        CompoundTag nbtCompound = stack.getTag();
        if (nbtCompound == null) {
            return Stream.empty();
        } else {
            ListTag nbtList = nbtCompound.getList("Items", Tag.TAG_COMPOUND);
            return nbtList.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        NonNullList<ItemStack> defaultedList = NonNullList.create();
        getBundledStacks(stack).forEach(defaultedList::add);
        return Optional.of(new BundleTooltip(defaultedList, getBundleOccupancy(stack)));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("item.minecraft.bundle.fullness", getBundleOccupancy(stack), 64).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onDestroyed(ItemEntity entity) {
        ItemUtils.onContainerDestroyed(entity, getBundledStacks(entity.getItem()));
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
}