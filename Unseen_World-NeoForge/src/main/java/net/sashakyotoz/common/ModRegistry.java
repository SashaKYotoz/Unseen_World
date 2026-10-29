package net.sashakyotoz.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.datagen.advancements.CuredGripcrystalEntityCriterion;
import net.sashakyotoz.common.datagen.recipes.WarriorShieldDecorationRecipe;
import net.sashakyotoz.common.registry.ModRegistrar;

public class ModRegistry {
    public static CuredGripcrystalEntityCriterion CURED_GRIPCRYSTAL_ENTITY_CRITERION;
    public static RecipeSerializer<WarriorShieldDecorationRecipe> WARRIOR_SHIELD_DECORATION;

    private static boolean registered;

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        CURED_GRIPCRYSTAL_ENTITY_CRITERION = ModRegistrar.register(Registries.TRIGGER_TYPE, UnseenWorld.MOD.makeID("cured_gripcrystal_entity"), new CuredGripcrystalEntityCriterion());
        WARRIOR_SHIELD_DECORATION = register("crafting_warrior_shield_decoration", new SimpleCraftingRecipeSerializer<>(WarriorShieldDecorationRecipe::new));
        UnseenWorld.log().debug("Registering recipe serializers and criteria for modid : " + UnseenWorld.MOD_ID);
    }

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return ModRegistrar.register(Registries.RECIPE_SERIALIZER, UnseenWorld.MOD.makeID(id), serializer);
    }

    public static class Foods {
        public static final FoodProperties CRYSTIE_APPLE = new FoodProperties.Builder().saturationModifier(2).nutrition(3).effect(
                        new MobEffectInstance(MobEffects.HARM, 1, 0, true, false),
                        1)
                .effect(
                        new MobEffectInstance(MobEffects.DIG_SPEED, 240, 1, true, false),
                        1).build();
        public static final FoodProperties GLOW_APPLE = new FoodProperties.Builder().saturationModifier(3).nutrition(3)
                .effect(
                        new MobEffectInstance(MobEffects.GLOWING, 300, 1, true, false),
                        1).build();
        public static final FoodProperties BEARFRUIT_BRAMBLE = new FoodProperties.Builder().alwaysEdible().saturationModifier(1).nutrition(3).effect(
                        new MobEffectInstance(MobEffects.REGENERATION, 100, 1, true, true),
                        1)
                .effect(
                        new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, true, true),
                        1).build();
        public static final FoodProperties NIGHTBERRY = new FoodProperties.Builder().alwaysEdible().saturationModifier(2).nutrition(2).effect(
                        new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, true, true),
                        1)
                .effect(
                        new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100, 0, true, true),
                        1).build();
        public static final FoodProperties WARPEDVEIL_VINE_FRUIT = new FoodProperties.Builder().saturationModifier(1).nutrition(3).effect(
                new MobEffectInstance(MobEffects.GLOWING, 300, 0, true, true),
                1).build();
        public static final FoodProperties GLOWROOT_BULB = new FoodProperties.Builder().saturationModifier(1).nutrition(3).effect(
                new MobEffectInstance(MobEffects.SATURATION, 60, 0, true, true),
                1).build();
        public static final FoodProperties RIMEHORN_MEAT = new FoodProperties.Builder().saturationModifier(2).nutrition(2).build();
        public static final FoodProperties COOKED_RIMEHORN_MEAT = new FoodProperties.Builder().saturationModifier(4).nutrition(4).build();
    }
}