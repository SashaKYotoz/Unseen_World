package net.sashakyotoz.common.datagen.recipes;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.sashakyotoz.common.ModRegistry;

public class WarriorShieldDecorationRecipe extends SpecialCraftingRecipe {
    public WarriorShieldDecorationRecipe(Identifier identifier, CraftingRecipeCategory craftingRecipeCategory) {
        super(identifier, craftingRecipeCategory);
    }

    public boolean matches(RecipeInputInventory recipeInputInventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < recipeInputInventory.size(); i++) {
            ItemStack itemStack3 = recipeInputInventory.getStack(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() instanceof BannerItem) {
                    if (!itemStack2.isEmpty())
                        return false;

                    itemStack2 = itemStack3;
                } else {
                    if (!itemStack3.isIn(ConventionalItemTags.SHIELDS))
                        return false;

                    if (!itemStack.isEmpty())
                        return false;

                    if (BlockItem.getBlockEntityNbt(itemStack3) != null)
                        return false;

                    itemStack = itemStack3;
                }
            }
        }

        return !itemStack.isEmpty() && !itemStack2.isEmpty();
    }

    public ItemStack craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < recipeInputInventory.size(); i++) {
            ItemStack itemStack3 = recipeInputInventory.getStack(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() instanceof BannerItem)
                    itemStack = itemStack3;
                else if (itemStack3.isIn(ConventionalItemTags.SHIELDS))
                    itemStack2 = itemStack3.copy();
            }
        }

        if (itemStack2.isEmpty()) {
            return itemStack2;
        } else {
            NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(itemStack);
            NbtCompound nbtCompound2 = nbtCompound == null ? new NbtCompound() : nbtCompound.copy();
            nbtCompound2.putInt("Base", ((BannerItem) itemStack.getItem()).getColor().getId());
            BlockItem.setBlockEntityNbt(itemStack2, BlockEntityType.BANNER, nbtCompound2);
            return itemStack2;
        }
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.WARRIOR_SHIELD_DECORATION;
    }
}