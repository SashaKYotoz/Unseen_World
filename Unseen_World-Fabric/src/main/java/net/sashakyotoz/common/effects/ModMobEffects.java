package net.sashakyotoz.common.effects;

import net.lcc.sollib.api.common.registry.holder.EffectHolder;
import net.lcc.sollib.mixin.access.MobEffectAccessor;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.alchemy.Potions;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.items.ModItems;

public class ModMobEffects {
    public static void register() {
        UnseenWorld.log().debug("Registering MobEffects for modid : " + UnseenWorld.MOD_ID);
    }

    public static final EffectHolder GRIPPING_RESISTANCE = UnseenWorld.MOD.register(EffectHolder.class, "gripping_resistance",
            () -> MobEffectAccessor.create(MobEffectCategory.BENEFICIAL, 172931))
            .withPotion(() -> Potions.AWKWARD,() -> ModItems.GRIPPING_MEMBRANE, 7200, false, false);
}