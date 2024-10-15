package net.sashakyotoz.common.items;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.sashakyotoz.UnseenWorld;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    UNSEENIUM("unseenium", 25, new int[]{5, 8, 10, 5}, 24,
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2f, 0.1f, () -> Ingredient.ofItems(ModItems.UNSEENIUM_INGOT)),
    ABYSSAL("abyssal", 36, new int[]{6, 9, 11, 6}, 28,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 2f, 0.1f, () -> Ingredient.ofItems(ModItems.ABYSSAL_INGOT)),
    TITANIUM("titanium", 42, new int[]{6, 9, 11, 6}, 20,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 2f, 0.1f, () -> Ingredient.ofItems(ModItems.RED_TITANIUM_INGOT));

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    private static final int[] BASE_DURABILITY = {11, 16, 15, 13};

    ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound,
                      float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY[type.ordinal()] * this.durabilityMultiplier;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return protectionAmounts[type.ordinal()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return UnseenWorld.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}