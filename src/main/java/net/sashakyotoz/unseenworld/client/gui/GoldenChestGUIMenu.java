
package net.sashakyotoz.unseenworld.client.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModMenus;
import net.sashakyotoz.unseenworld.managers.GoldenChestGUIUpdate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GoldenChestGUIMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    public final Level world;
    public final Player player;
    public int x, y, z;
    private IItemHandler internal;
    private final Map<Integer, Slot> customSlots = new HashMap<>();
    private boolean bound = false;

    public GoldenChestGUIMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(UnseenWorldModMenus.GOLDEN_CHEST_GUI.get(), id);
        this.player = inv.player;
        this.world = inv.player.level();
        this.internal = new ItemStackHandler(30);
        BlockPos pos = null;
        if (extraData != null) {
            pos = extraData.readBlockPos();
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
        }
        if (pos != null) {
            if (extraData.readableBytes() == 1) {
                byte hand = extraData.readByte();
                ItemStack itemstack;
                if (hand == 0)
                    itemstack = this.player.getMainHandItem();
                else
                    itemstack = this.player.getOffhandItem();
                itemstack.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
                    this.internal = capability;
                    this.bound = true;
                });
            } else if (extraData.readableBytes() > 1) {
                extraData.readByte();
                Entity entity = world.getEntity(extraData.readVarInt());
                if (entity != null)
                    entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
                        this.internal = capability;
                        this.bound = true;
                    });
            } else {
                BlockEntity ent = inv.player.level().getBlockEntity(pos);
                if (ent != null) {
                    ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
                        this.internal = capability;
                        this.bound = true;
                    });
                }
            }
        }
        this.customSlots.put(0, this.addSlot(new SlotItemHandler(internal, 0, 6, 4) {
        }));
        this.customSlots.put(1, this.addSlot(new SlotItemHandler(internal, 1, 25, 4) {
        }));
        this.customSlots.put(2, this.addSlot(new SlotItemHandler(internal, 2, 44, 4) {
        }));
        this.customSlots.put(3, this.addSlot(new SlotItemHandler(internal, 3, 63, 4) {
        }));
        this.customSlots.put(4, this.addSlot(new SlotItemHandler(internal, 4, 82, 4) {
        }));
        this.customSlots.put(5, this.addSlot(new SlotItemHandler(internal, 5, 101, 4) {
        }));
        this.customSlots.put(6, this.addSlot(new SlotItemHandler(internal, 6, 120, 4) {
        }));
        this.customSlots.put(7, this.addSlot(new SlotItemHandler(internal, 7, 139, 4) {
        }));
        this.customSlots.put(8, this.addSlot(new SlotItemHandler(internal, 8, 158, 4) {
        }));
        this.customSlots.put(9, this.addSlot(new SlotItemHandler(internal, 9, 176, 4) {
        }));
        this.customSlots.put(10, this.addSlot(new SlotItemHandler(internal, 10, 6, 23) {
        }));
        this.customSlots.put(11, this.addSlot(new SlotItemHandler(internal, 11, 25, 23) {
        }));
        this.customSlots.put(12, this.addSlot(new SlotItemHandler(internal, 12, 44, 23) {
        }));
        this.customSlots.put(13, this.addSlot(new SlotItemHandler(internal, 13, 63, 23) {
        }));
        this.customSlots.put(14, this.addSlot(new SlotItemHandler(internal, 14, 82, 23) {
        }));
        this.customSlots.put(15, this.addSlot(new SlotItemHandler(internal, 15, 101, 23) {
        }));
        this.customSlots.put(16, this.addSlot(new SlotItemHandler(internal, 16, 120, 23) {
        }));
        this.customSlots.put(17, this.addSlot(new SlotItemHandler(internal, 17, 139, 23) {
        }));
        this.customSlots.put(18, this.addSlot(new SlotItemHandler(internal, 18, 158, 23) {
        }));
        this.customSlots.put(19, this.addSlot(new SlotItemHandler(internal, 19, 176, 23) {
        }));
        this.customSlots.put(20, this.addSlot(new SlotItemHandler(internal, 20, 6, 42) {
        }));
        this.customSlots.put(21, this.addSlot(new SlotItemHandler(internal, 21, 176, 42) {
        }));
        this.customSlots.put(22, this.addSlot(new SlotItemHandler(internal, 22, 176, 60) {
        }));
        this.customSlots.put(23, this.addSlot(new SlotItemHandler(internal, 23, 6, 60) {
        }));
        this.customSlots.put(24, this.addSlot(new SlotItemHandler(internal, 24, 24, 42) {
        }));
        this.customSlots.put(25, this.addSlot(new SlotItemHandler(internal, 25, 158, 42) {
        }));
        this.customSlots.put(26, this.addSlot(new SlotItemHandler(internal, 26, 45, 64) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                if(UnseenWorldModItems.KNIGHT_ARMOR_HELMET.get() == stack.getItem()) {
                    if(stack.isDamaged())
                        stack.setDamageValue(0);
                }
                return UnseenWorldModItems.KNIGHT_ARMOR_HELMET.get() == stack.getItem();
            }
        }));
        this.customSlots.put(27, this.addSlot(new SlotItemHandler(internal, 27, 72, 64) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                if(UnseenWorldModItems.KNIGHT_ARMOR_CHESTPLATE.get() == stack.getItem()) {
                    if(stack.isDamaged())
                        stack.setDamageValue(0);
                }
                return UnseenWorldModItems.KNIGHT_ARMOR_CHESTPLATE.get() == stack.getItem();
            }
        }));
        this.customSlots.put(28, this.addSlot(new SlotItemHandler(internal, 28, 108, 64) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                if(UnseenWorldModItems.KNIGHT_ARMOR_LEGGINGS.get() == stack.getItem()) {
                    if(stack.isDamaged())
                        stack.setDamageValue(0);
                }
                return UnseenWorldModItems.KNIGHT_ARMOR_LEGGINGS.get() == stack.getItem();
            }
        }));
        this.customSlots.put(29, this.addSlot(new SlotItemHandler(internal, 29, 135, 64) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                if(UnseenWorldModItems.KNIGHT_ARMOR_BOOTS.get() == stack.getItem()) {
                    if(stack.isDamaged())
                        stack.setDamageValue(0);
                }
                return UnseenWorldModItems.KNIGHT_ARMOR_BOOTS.get() == stack.getItem();
            }
        }));
        for (int si = 0; si < 3; ++si)
            for (int sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 10 + 8 + sj * 18, 7 + 84 + si * 18));
        for (int si = 0; si < 9; ++si)
            this.addSlot(new Slot(inv, si, 10 + 8 + si * 18, 7 + 142));
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 30) {
                if (!this.moveItemStackTo(itemstack1, 30, this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (!this.moveItemStackTo(itemstack1, 0, 30, false)) {
                if (index < 30 + 27) {
                    if (!this.moveItemStackTo(itemstack1, 30 + 27, this.slots.size(), true))
                        return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(itemstack1, 30, 30 + 27, false))
                        return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            }
            if (itemstack1.getCount() == 0)
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int index, int p_38906_, boolean p_38907_) {
        boolean flag = false;
        int i = index;
        if (p_38907_) {
            i = p_38906_ - 1;
        }
        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (p_38907_) {
                    if (i < index) {
                        break;
                    }
                } else if (i >= p_38906_) {
                    break;
                }
                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (slot.mayPlace(itemstack) && !itemstack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.set(itemstack);
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.set(itemstack);
                        flag = true;
                    }
                }
                if (p_38907_) {
                    --i;
                } else {
                    ++i;
                }
            }
        }
        if (!stack.isEmpty()) {
            if (p_38907_) {
                i = p_38906_ - 1;
            } else {
                i = index;
            }
            while (true) {
                if (p_38907_) {
                    if (i < index) {
                        break;
                    }
                } else if (i >= p_38906_) {
                    break;
                }
                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
                    if (stack.getCount() > slot1.getMaxStackSize()) {
                        slot1.setByPlayer(stack.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.setByPlayer(stack.split(stack.getCount()));
                    }
                    slot1.setChanged();
                    flag = true;
                    break;
                }
                if (p_38907_) {
                    --i;
                } else {
                    ++i;
                }
            }
        }
        return flag;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        GoldenChestGUIUpdate.execute(world, x, y, z);
        if (!bound && playerIn instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()) {
                for (int j = 0; j < internal.getSlots(); ++j) {
                    if (j == 0)
                        continue;
                    if (j == 1)
                        continue;
                    if (j == 2)
                        continue;
                    if (j == 3)
                        continue;
                    if (j == 4)
                        continue;
                    if (j == 5)
                        continue;
                    if (j == 6)
                        continue;
                    if (j == 7)
                        continue;
                    if (j == 8)
                        continue;
                    if (j == 9)
                        continue;
                    if (j == 26)
                        continue;
                    if (j == 27)
                        continue;
                    if (j == 28)
                        continue;
                    if (j == 29)
                        continue;
                    playerIn.drop(internal.extractItem(j, internal.getStackInSlot(j).getCount(), false), false);
                }
            } else {
                for (int i = 0; i < internal.getSlots(); ++i) {
                    if (i == 0)
                        continue;
                    if (i == 1)
                        continue;
                    if (i == 2)
                        continue;
                    if (i == 3)
                        continue;
                    if (i == 4)
                        continue;
                    if (i == 5)
                        continue;
                    if (i == 6)
                        continue;
                    if (i == 7)
                        continue;
                    if (i == 8)
                        continue;
                    if (i == 9)
                        continue;
                    if (i == 26)
                        continue;
                    if (i == 27)
                        continue;
                    if (i == 28)
                        continue;
                    if (i == 29)
                        continue;
                    playerIn.getInventory().placeItemBackInInventory(internal.extractItem(i, internal.getStackInSlot(i).getCount(), false));
                }
            }
        }
    }
    public Map<Integer, Slot> get() {
        return customSlots;
    }
}
