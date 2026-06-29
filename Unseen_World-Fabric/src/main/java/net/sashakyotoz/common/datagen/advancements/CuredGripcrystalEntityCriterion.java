package net.sashakyotoz.common.datagen.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;

public class CuredGripcrystalEntityCriterion extends SimpleCriterionTrigger<CuredGripcrystalEntityCriterion.Conditions> {
    static final ResourceLocation ID = new ResourceLocation("cured_gripcrystal_entity");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public CuredGripcrystalEntityCriterion.Conditions createInstance(
            JsonObject jsonObject, ContextAwarePredicate lootContextPredicate, DeserializationContext advancementEntityPredicateDeserializer
    ) {
        ContextAwarePredicate lootContextPredicate2 = EntityPredicate.fromJson(jsonObject, "gripcrystalled", advancementEntityPredicateDeserializer);
        ContextAwarePredicate lootContextPredicate3 = EntityPredicate.fromJson(jsonObject, "entity", advancementEntityPredicateDeserializer);
        return new CuredGripcrystalEntityCriterion.Conditions(lootContextPredicate, lootContextPredicate2, lootContextPredicate3);
    }

    public void trigger(ServerPlayer player, LivingEntity sickEntity, LivingEntity entity) {
        LootContext lootContext = EntityPredicate.createContext(player, sickEntity);
        LootContext lootContext2 = EntityPredicate.createContext(player, entity);
        this.trigger(player, conditions -> conditions.matches(lootContext, lootContext2));
    }

    public static class Conditions extends AbstractCriterionTriggerInstance {
        private final ContextAwarePredicate gripcrystalled;
        private final ContextAwarePredicate entity;

        public Conditions(ContextAwarePredicate player, ContextAwarePredicate gripcrystalled, ContextAwarePredicate entity) {
            super(CuredGripcrystalEntityCriterion.ID, player);
            this.gripcrystalled = gripcrystalled;
            this.entity = entity;
        }

        public static CuredGripcrystalEntityCriterion.Conditions any() {
            return new CuredGripcrystalEntityCriterion.Conditions(ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, ContextAwarePredicate.ANY);
        }

        public boolean matches(LootContext zombieContext, LootContext villagerContext) {
            return this.gripcrystalled.matches(zombieContext) && this.entity.matches(villagerContext);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            JsonObject jsonObject = super.serializeToJson(predicateSerializer);
            jsonObject.add("gripcrystalled", this.gripcrystalled.toJson(predicateSerializer));
            jsonObject.add("entity", this.entity.toJson(predicateSerializer));
            return jsonObject;
        }
    }
}