package net.sashakyotoz.common.items;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum ModTiers implements Tier {
    UNSEENIUM(4,1263,12f,4f,36,Ingredient.of(ModItems.UNSEENIUM_INGOT)),
    ABYSSAL(5,2350,13f,5f,24,Ingredient.of(ModItems.ABYSSAL_INGOT)),
    TITANIUM(5,2485,14f,5f,20,Ingredient.of(ModItems.RED_TITANIUM_INGOT)),
    ROCKBREAKER_HAMMER(4,2374,10f,7f,30,Ingredient.of(ModItems.ABYSSAL_INGOT)),
    ECLIPSEBANE(3,2169,8f,8f,32,Ingredient.of(ModItems.RED_TITANIUM_INGOT));

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
    public int getLevel() {
        return this.mining_level;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repair;
    }
}