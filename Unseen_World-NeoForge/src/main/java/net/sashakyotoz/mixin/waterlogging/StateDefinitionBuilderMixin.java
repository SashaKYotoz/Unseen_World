package net.sashakyotoz.mixin.waterlogging;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.sashakyotoz.common.blocks.custom.fluids.DarkWaterlogging;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(StateDefinition.Builder.class)
public abstract class StateDefinitionBuilderMixin<O, S extends StateHolder<O, S>> {
    @Shadow
    @Final
    private O owner;

    @Shadow
    @Final
    private Map<String, Property<?>> properties;

    @Inject(method = "add", at = @At("TAIL"))
    private void unseen_world$addDarkWater(Property<?>[] added, CallbackInfoReturnable<StateDefinition.Builder<O, S>> cir) {
        if (!(this.owner instanceof Block)) {
            return;
        }
        for (Property<?> property : added) {
            if (property == BlockStateProperties.WATERLOGGED) {
                this.properties.putIfAbsent(DarkWaterlogging.DARK_WATER.getName(), DarkWaterlogging.DARK_WATER);
                return;
            }
        }
    }
}