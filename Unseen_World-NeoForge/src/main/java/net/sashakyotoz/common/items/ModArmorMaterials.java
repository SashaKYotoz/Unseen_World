package net.sashakyotoz.common.items;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.registry.ModRegistrar;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public enum ModArmorMaterials {
    UNSEENIUM("unseenium", 25, new int[]{4, 9, 7, 4}, 24,
            SoundEvents.ARMOR_EQUIP_DIAMOND, 2f, 0.1f, () -> Ingredient.of(ModItems.UNSEENIUM_INGOT)),
    ABYSSAL("abyssal", 36, new int[]{5, 10, 8, 5}, 28,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 2f, 0.1f, () -> Ingredient.of(ModItems.ABYSSAL_INGOT)),
    TITANIUM("titanium", 42, new int[]{5, 10, 8, 5}, 20,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 2f, 0.1f, () -> Ingredient.of(ModItems.RED_TITANIUM_INGOT));

    private final Holder<ArmorMaterial> material;
    private final int durabilityMultiplier;

    ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, Holder<SoundEvent> equipSound,
                      float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.durabilityMultiplier = durabilityMultiplier;
        EnumMap<ArmorItem.Type, Integer> defense = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.HELMET, protectionAmounts[0]);
            map.put(ArmorItem.Type.CHESTPLATE, protectionAmounts[1]);
            map.put(ArmorItem.Type.LEGGINGS, protectionAmounts[2]);
            map.put(ArmorItem.Type.BOOTS, protectionAmounts[3]);
            map.put(ArmorItem.Type.BODY, protectionAmounts[1]);
        });
        this.material = ModRegistrar.registerForHolder(Registries.ARMOR_MATERIAL, UnseenWorld.MOD.makeID(name),
                new ArmorMaterial(defense, enchantability, equipSound, repairIngredient,
                        List.of(new ArmorMaterial.Layer(UnseenWorld.MOD.makeID(name))), toughness, knockbackResistance));
    }

    public Holder<ArmorMaterial> holder() {
        return this.material;
    }

    public int durability(ArmorItem.Type type) {
        return type.getDurability(this.durabilityMultiplier);
    }

    public boolean matches(Holder<ArmorMaterial> other) {
        return other.value() == this.material.value();
    }
}
