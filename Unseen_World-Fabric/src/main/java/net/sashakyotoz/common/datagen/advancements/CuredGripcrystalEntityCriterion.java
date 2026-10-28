package net.sashakyotoz.common.datagen.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.sashakyotoz.common.ModRegistry;

import java.util.Optional;

public class CuredGripcrystalEntityCriterion extends SimpleCriterionTrigger<CuredGripcrystalEntityCriterion.Conditions> {
    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayer player, LivingEntity sickEntity, LivingEntity entity) {
        LootContext sickContext = EntityPredicate.createContext(player, sickEntity);
        LootContext curedContext = EntityPredicate.createContext(player, entity);
        this.trigger(player, conditions -> conditions.matches(sickContext, curedContext));
    }

    public record Conditions(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> gripcrystalled,
                             Optional<ContextAwarePredicate> entity) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("gripcrystalled").forGetter(Conditions::gripcrystalled),
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(Conditions::entity)
        ).apply(instance, Conditions::new));

        public static Criterion<Conditions> any() {
            return ModRegistry.CURED_GRIPCRYSTAL_ENTITY_CRITERION.createCriterion(new Conditions(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public boolean matches(LootContext gripcrystalledContext, LootContext entityContext) {
            if (this.gripcrystalled.isPresent() && !this.gripcrystalled.get().matches(gripcrystalledContext)) {
                return false;
            }
            return this.entity.isEmpty() || this.entity.get().matches(entityContext);
        }

        @Override
        public void validate(CriterionValidator validator) {
            SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
            validator.validateEntity(this.gripcrystalled, ".gripcrystalled");
            validator.validateEntity(this.entity, ".entity");
        }
    }
}
