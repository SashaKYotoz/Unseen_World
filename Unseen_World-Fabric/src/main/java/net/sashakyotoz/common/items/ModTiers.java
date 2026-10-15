package net.sashakyotoz.common.items;

import com.google.common.base.Suppliers;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModTiers implements Tier {
    UNSEENIUM(4, 1263, 12.0F, 4.0F, 36, () -> Ingredient.of(ModItems.UNSEENIUM_INGOT)),
    ABYSSAL(5, 2350, 13.0F, 5.0F, 24, () -> Ingredient.of(ModItems.ABYSSAL_INGOT)),
    TITANIUM(5, 2485, 14.0F, 5.0F, 20, () -> Ingredient.of(ModItems.RED_TITANIUM_INGOT)),
    AURIC_ARC(3, 2631, 8.0F, 8.0F, 36, () -> Ingredient.of(ModItems.UNSEENIUM_INGOT)),
    ROCKBREAKER_HAMMER(4, 2374, 10.0F, 7.0F, 30, () -> Ingredient.of(ModItems.ABYSSAL_INGOT)),
    ECLIPSEBANE(3, 2169, 8.0F, 8.0F, 32, () -> Ingredient.of(ModItems.ABYSSAL_INGOT));

    private final int miningLevel;
    private final int durability;
    private final float speed;
    private final float damage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModTiers(int miningLevel, int durability, float speed, float damage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
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
    public int getLevel() {
        return this.miningLevel;
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
