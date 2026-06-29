package net.sashakyotoz.common.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.sashakyotoz.UnseenWorld;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    UNSEENIUM("unseenium", 25, new int[]{4, 9, 7, 4}, 24,
            SoundEvents.ARMOR_EQUIP_DIAMOND, 2f, 0.1f, () -> Ingredient.of(ModItems.UNSEENIUM_INGOT)),
    ABYSSAL("abyssal", 36, new int[]{5, 10, 8, 5}, 28,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 2f, 0.1f, () -> Ingredient.of(ModItems.ABYSSAL_INGOT)),
    TITANIUM("titanium", 42, new int[]{5, 10, 8, 5}, 20,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 2f, 0.1f, () -> Ingredient.of(ModItems.RED_TITANIUM_INGOT));

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
    public int getDurabilityForType(ArmorItem.Type type) {
        return BASE_DURABILITY[type.ordinal()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return protectionAmounts[type.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
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