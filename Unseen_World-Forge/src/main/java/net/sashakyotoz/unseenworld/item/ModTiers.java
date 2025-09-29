package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;

public class ModTiers {
    //hammers
    public static Tier VOID_HAMMER = new Tier() {
        public int getUses() {
            return 2500;
        }
        public float getSpeed() {
            return 10f;
        }
        public float getAttackDamageBonus() {
            return 11f;
        }
        public int getLevel() {
            return 5;
        }
        public int getEnchantmentValue() {
            return 18;
        }
        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(UnseenWorldItems.VOID_INGOT.get()));
        }
    };
    public static Tier REDNESS_HAMMER = new Tier() {
        public int getUses() {
            return 2750;
        }

        public float getSpeed() {
            return 8f;
        }

        public float getAttackDamageBonus() {
            return 12f;
        }

        public int getLevel() {
            return 5;
        }

        public int getEnchantmentValue() {
            return 20;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(UnseenWorldItems.RED_TITANIUM_INGOT.get()));
        }
    };
    public static Tier AMETHYST_HAMMER = new Tier() {
        public int getUses() {
            return 1750;
        }

        public float getSpeed() {
            return 10f;
        }

        public float getAttackDamageBonus() {
            return 10f;
        }

        public int getLevel() {
            return 5;
        }

        public int getEnchantmentValue() {
            return 18;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Items.AMETHYST_SHARD), new ItemStack(UnseenWorldItems.TANZANITE_SHARD.get()));
        }
    };
    //found treasures weapons
    public static ModTier BLASTING_LANCER = new ModTier(4,1297,5,5,24,()->Ingredient.of(new ItemStack(UnseenWorldItems.BLAZE_SHIELD_SHARD.get()), new ItemStack(UnseenWorldItems.RED_TITANIUM_INGOT.get())));
    public static ModTier LIGHT_TULVAR = new ModTier(4,1683,5,4,16,()-> Ingredient.of(new ItemStack(UnseenWorldItems.UNSEEN_INGOT.get()), new ItemStack(UnseenWorldItems.DEEP_GEM.get())));
    public static ModTier HEAVY_CLAYMORE = new ModTier(4,2478,5,6,16,()-> Ingredient.of(new ItemStack(Items.NETHERITE_INGOT)));
    public static ModTier FIERY_SABER = new ModTier(4,1957,5,5,24,()-> Ingredient.of(new ItemStack(UnseenWorldItems.FIRE_PEARL.get())));
    //tools tiers
    public static ModTier DEEP_GEM = new ModTier(3,1164,8.5f,3f,24,()->Ingredient.of(UnseenWorldItems.DEEP_GEM.get()));
    public static ModTier NATURERIUM = new ModTier(4,778,10f,4f,24,()->Ingredient.of(UnseenWorldItems.NATURERIUM_INGOT.get()));
    public static ModTier VOID = new ModTier(5,2350,13f,6f,24,()->Ingredient.of(UnseenWorldItems.VOID_INGOT.get()));
    public static ModTier UNSEENIUM = new ModTier(4,1263,12f,5f,36,()->Ingredient.of(UnseenWorldItems.UNSEEN_INGOT.get()));
    public static ModTier TITANIUM = new ModTier(5,2485,15f,7f,18,()->Ingredient.of(UnseenWorldItems.RED_TITANIUM_INGOT.get()));
}
