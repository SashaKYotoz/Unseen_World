package net.sashakyotoz.common.items;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public enum ModTiers implements ToolMaterial {
    UNSEENIUM(4,1263,12f,5f,36,Ingredient.ofItems(ModItems.UNSEENIUM_INGOT)),
    ABYSSAL(5,2350,13f,6f,24,Ingredient.ofItems(ModItems.ABYSSAL_INGOT)),
    TITANIUM(5,2485,14f,6f,20,Ingredient.ofItems(ModItems.RED_TITANIUM_INGOT)),
    ROCKBREAKER_HAMMER(4,2374,10f,7f,30,Ingredient.ofItems(ModItems.ABYSSAL_INGOT)),
    ECLIPSEBANE(3,2169,8f,9f,32,Ingredient.ofItems(ModItems.RED_TITANIUM_INGOT));

    private final int durability;
    private final float speed;
    private final float damage;
    private final int mining_level;
    private final int enchantability;
    private final Ingredient repair;

    ModTiers(int mining_level, int durability, float speed, float damage, int enchantability, Ingredient repair) {
        this.durability = durability;
        this.speed = speed;
        this.damage = damage;
        this.mining_level = mining_level;
        this.enchantability = enchantability;
        this.repair = repair;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.speed;
    }

    @Override
    public float getAttackDamage() {
        return this.damage;
    }

    @Override
    public int getMiningLevel() {
        return this.mining_level;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repair;
    }
}