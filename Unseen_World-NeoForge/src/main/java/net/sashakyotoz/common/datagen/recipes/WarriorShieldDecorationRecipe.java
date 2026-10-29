package net.sashakyotoz.common.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.neoforged.neoforge.common.Tags;
import net.sashakyotoz.common.ModRegistry;

public class WarriorShieldDecorationRecipe extends CustomRecipe {
    public WarriorShieldDecorationRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack shield = ItemStack.EMPTY;
        ItemStack banner = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.getItem() instanceof BannerItem) {
                if (!banner.isEmpty()) {
                    return false;
                }
                banner = stack;
            } else {
                if (!stack.is(Tags.Items.TOOLS_SHIELD) || !shield.isEmpty()) {
                    return false;
                }
                if (!stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY).layers().isEmpty()) {
                    return false;
                }
                shield = stack;
            }
        }

        return !shield.isEmpty() && !banner.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack shield = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.getItem() instanceof BannerItem) {
                banner = stack;
            } else if (stack.is(Tags.Items.TOOLS_SHIELD)) {
                shield = stack.copy();
            }
        }

        if (shield.isEmpty() || banner.isEmpty()) {
            return ItemStack.EMPTY;
        }
        shield.set(DataComponents.BANNER_PATTERNS, banner.get(DataComponents.BANNER_PATTERNS));
        shield.set(DataComponents.BASE_COLOR, ((BannerItem) banner.getItem()).getColor());
        return shield;
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
