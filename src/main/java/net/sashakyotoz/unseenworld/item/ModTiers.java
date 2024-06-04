package net.sashakyotoz.unseenworld.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.minecraft.world.item.Tier;

import java.util.List;

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
            return Ingredient.of(new ItemStack(UnseenWorldModItems.VOID_INGOT.get()));
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
            return Ingredient.of(new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get()));
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
            return Ingredient.of(new ItemStack(Items.AMETHYST_SHARD), new ItemStack(UnseenWorldModItems.TANZANITE_SHARD.get()));
        }
    };
    //found treasures weapon
    public static Tier BLASTING_LANCER = new Tier() {
        public int getUses() {
            return 1297;
        }

        public float getSpeed() {
            return 5f;
        }

        public float getAttackDamageBonus() {
            return 5;
        }

        public int getLevel() {
            return 5;
        }

        public int getEnchantmentValue() {
            return 24;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(UnseenWorldModItems.BLAZE_SHIELD_SHARD.get()), new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get()));
        }
    };
    public static Tier LIGHT_TULVAR = new Tier() {
        public int getUses() {
            return 1683;
        }

        public float getSpeed() {
            return 5f;
        }

        public float getAttackDamageBonus() {
            return 4;
        }

        public int getLevel() {
            return 5;
        }

        public int getEnchantmentValue() {
            return 20;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(UnseenWorldModItems.UNSEEN_INGOT.get()), new ItemStack(UnseenWorldModItems.DEEP_GEM.get()));
        }
    };
    public static Tier HEAVY_CLAYMORE = new Tier() {
        public int getUses() {
            return 2478;
        }

        public float getSpeed() {
            return 5f;
        }

        public float getAttackDamageBonus() {
            return 6f;
        }

        public int getLevel() {
            return 5;
        }

        public int getEnchantmentValue() {
            return 20;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Items.NETHERITE_INGOT));
        }
    };
    public static Tier FIERY_SABER = new Tier() {
        public int getUses() {
            return 1957;
        }

        public float getSpeed() {
            return 5f;
        }

        public float getAttackDamageBonus() {
            return 5f;
        }

        public int getLevel() {
            return 5;
        }

        public int getEnchantmentValue() {
            return 2;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(UnseenWorldModItems.FIRE_PEARL.get()));
        }
    };
    public static Tier DEEP_GEM = TierSortingRegistry.registerTier(new ForgeTier(3,1164,8.5f,3f,24,BlockTags.NEEDS_DIAMOND_TOOL,()->Ingredient.of(new ItemStack(UnseenWorldModItems.DEEP_GEM.get()))),new ResourceLocation(UnseenWorldMod.MODID,"deep_gem"), List.of(Tiers.IRON),List.of(Tiers.NETHERITE));
    public static Tier NATURERIUM = TierSortingRegistry.registerTier(new ForgeTier(4,778,10f,4f,24,BlockTags.NEEDS_DIAMOND_TOOL,()->Ingredient.of(new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get()))),new ResourceLocation(UnseenWorldMod.MODID,"naturerium"), List.of(Tiers.NETHERITE),List.of());
    public static Tier VOID = TierSortingRegistry.registerTier(new ForgeTier(5,2350,13f,6f,24,Tags.Blocks.NEEDS_NETHERITE_TOOL,()->Ingredient.of(new ItemStack(UnseenWorldModItems.VOID_INGOT.get()))),new ResourceLocation(UnseenWorldMod.MODID,"void"), List.of(NATURERIUM),List.of());
    public static Tier UNSEENIUM = TierSortingRegistry.registerTier(new ForgeTier(4,1263,12f,5f,36,Tags.Blocks.NEEDS_NETHERITE_TOOL,()->Ingredient.of(new ItemStack(UnseenWorldModItems.UNSEEN_INGOT.get()))),new ResourceLocation(UnseenWorldMod.MODID,"unseenium"), List.of(NATURERIUM),List.of(VOID));
    public static Tier TITANIUM = TierSortingRegistry.registerTier(new ForgeTier(5,2485,15f,7f,18,Tags.Blocks.NEEDS_NETHERITE_TOOL,()->Ingredient.of(new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get()))),new ResourceLocation(UnseenWorldMod.MODID,"titanium"), List.of(VOID),List.of());
}
