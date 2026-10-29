package net.sashakyotoz.common.items;

import com.google.common.base.Suppliers;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public enum ModTiers implements Tier {
    UNSEENIUM(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1263, 12.0F, 4.0F, 36, () -> Ingredient.of(ModItems.UNSEENIUM_INGOT)),
    ABYSSAL(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2350, 13.0F, 5.0F, 24, () -> Ingredient.of(ModItems.ABYSSAL_INGOT)),
    TITANIUM(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2485, 14.0F, 5.0F, 20, () -> Ingredient.of(ModItems.RED_TITANIUM_INGOT)),
    AURIC_ARC(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 2631, 8.0F, 8.0F, 36, () -> Ingredient.of(ModItems.UNSEENIUM_INGOT)),
    ROCKBREAKER_HAMMER(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2374, 10.0F, 9.0F, 30, () -> Ingredient.of(ModItems.ABYSSAL_INGOT)),
    ECLIPSEBANE(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 2169, 8.0F, 7.0F, 32, () -> Ingredient.of(ModItems.ABYSSAL_INGOT));

    private final TagKey<Block> incorrectBlocksForDrops;
    private final int durability;
    private final float speed;
    private final float damage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModTiers(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float damage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.durability = durability;
        this.speed = speed;
        this.damage = damage;
        this.enchantability = enchantability;
        this.repairIngredient = Suppliers.memoize(repairIngredient::get);
    }

    @Override
    public int getUses() {
        return this.durability;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
