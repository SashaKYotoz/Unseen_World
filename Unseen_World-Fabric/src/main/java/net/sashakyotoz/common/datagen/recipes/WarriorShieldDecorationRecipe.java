package net.sashakyotoz.common.datagen.recipes;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.sashakyotoz.common.ModRegistry;

public class WarriorShieldDecorationRecipe extends CustomRecipe {
    public WarriorShieldDecorationRecipe(ResourceLocation identifier, CraftingBookCategory craftingRecipeCategory) {
        super(identifier, craftingRecipeCategory);
    }

    public boolean matches(CraftingContainer recipeInputInventory, Level world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < recipeInputInventory.getContainerSize(); i++) {
            ItemStack itemStack3 = recipeInputInventory.getItem(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() instanceof BannerItem) {
                    if (!itemStack2.isEmpty())
                        return false;

                    itemStack2 = itemStack3;
                } else {
                    if (!itemStack3.is(ConventionalItemTags.SHIELDS))
                        return false;

                    if (!itemStack.isEmpty())
                        return false;

                    if (BlockItem.getBlockEntityData(itemStack3) != null)
                        return false;

                    itemStack = itemStack3;
                }
            }
        }

        return !itemStack.isEmpty() && !itemStack2.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack3 = container.getItem(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() instanceof BannerItem)
                    itemStack = itemStack3;
                else if (itemStack3.is(ConventionalItemTags.SHIELDS))
                    itemStack2 = itemStack3.copy();
            }
        }

        if (itemStack2.isEmpty()) {
            return itemStack2;
        } else {
            CompoundTag nbtCompound = BlockItem.getBlockEntityData(itemStack);
            CompoundTag nbtCompound2 = nbtCompound == null ? new CompoundTag() : nbtCompound.copy();
            nbtCompound2.putInt("Base", ((BannerItem) itemStack.getItem()).getColor().getId());
            BlockItem.setBlockEntityData(itemStack2, BlockEntityType.BANNER, nbtCompound2);
            return itemStack2;
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.WARRIOR_SHIELD_DECORATION;
    }
}