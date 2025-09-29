package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModTier implements Tier {
    private final int level;
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;
    @NotNull
    private final Supplier<Ingredient> repairIngredient;

    public ModTier(int level, int uses, float speed, float attackDamageBonus, int enchantmentValue, @NotNull Supplier<Ingredient> repairIngredient) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamageBonus;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @NotNull
    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String toString() {
        return String.format("ModTier[level=%s, uses=%s, speed=%s, attackDamageBonus=%s, enchantmentValue=%s, repairIngredient=%s]",
                level,uses,speed,attackDamageBonus,enchantmentValue,repairIngredient);
    }
}