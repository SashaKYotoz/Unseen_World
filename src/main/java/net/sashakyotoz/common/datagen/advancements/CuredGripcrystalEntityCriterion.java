package net.sashakyotoz.common.datagen.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CuredGripcrystalEntityCriterion extends AbstractCriterion<CuredGripcrystalEntityCriterion.Conditions> {
    static final Identifier ID = new Identifier("cured_gripcrystal_entity");

    @Override
    public Identifier getId() {
        return ID;
    }

    public CuredGripcrystalEntityCriterion.Conditions conditionsFromJson(
            JsonObject jsonObject, LootContextPredicate lootContextPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer
    ) {
        LootContextPredicate lootContextPredicate2 = EntityPredicate.contextPredicateFromJson(jsonObject, "gripcrystalled", advancementEntityPredicateDeserializer);
        LootContextPredicate lootContextPredicate3 = EntityPredicate.contextPredicateFromJson(jsonObject, "entity", advancementEntityPredicateDeserializer);
        return new CuredGripcrystalEntityCriterion.Conditions(lootContextPredicate, lootContextPredicate2, lootContextPredicate3);
    }

    public void trigger(ServerPlayerEntity player, LivingEntity sickEntity, LivingEntity entity) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, sickEntity);
        LootContext lootContext2 = EntityPredicate.createAdvancementEntityLootContext(player, entity);
        this.trigger(player, conditions -> conditions.matches(lootContext, lootContext2));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final LootContextPredicate gripcrystalled;
        private final LootContextPredicate entity;

        public Conditions(LootContextPredicate player, LootContextPredicate gripcrystalled, LootContextPredicate entity) {
            super(CuredGripcrystalEntityCriterion.ID, player);
            this.gripcrystalled = gripcrystalled;
            this.entity = entity;
        }

        public static CuredGripcrystalEntityCriterion.Conditions any() {
            return new CuredGripcrystalEntityCriterion.Conditions(LootContextPredicate.EMPTY, LootContextPredicate.EMPTY, LootContextPredicate.EMPTY);
        }

        public boolean matches(LootContext zombieContext, LootContext villagerContext) {
            return this.gripcrystalled.test(zombieContext) && this.entity.test(villagerContext);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("gripcrystalled", this.gripcrystalled.toJson(predicateSerializer));
            jsonObject.add("entity", this.entity.toJson(predicateSerializer));
            return jsonObject;
        }
    }
}