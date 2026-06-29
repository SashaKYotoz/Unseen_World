package net.sashakyotoz.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.WeatherCommand;
import net.sashakyotoz.client.environment.weather.ChimericWeatherState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeatherCommand.class)
public class WeatherCommandMixin {
    @WrapOperation(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;", ordinal = 1))
    private static ArgumentBuilder registerCustomWeather(LiteralArgumentBuilder<CommandSourceStack> instance, ArgumentBuilder argumentBuilder, Operation<ArgumentBuilder> original) {
        return original.call(instance, argumentBuilder).then(
                Commands.literal("grippfall")
                        .executes(context -> executeGrippfall(context.getSource(), -1))
                        .then(
                                Commands.argument("duration", TimeArgument.time(1))
                                        .executes(context -> executeGrippfall(context.getSource(), IntegerArgumentType.getInteger(context, "duration")))
                        )
        );
    }

    @Unique
    private static int executeGrippfall(CommandSourceStack source, int duration) {
        ChimericWeatherState.get(source.getLevel()).setGrippfallDuration(duration);
        source.sendSuccess(() -> Component.translatable("commands.unseen_world.weather.set.grippfall"), true);
        return duration;
    }

    @Inject(method = "setClear", at = @At("TAIL"))
    private static void executeClear(CommandSourceStack source, int duration, CallbackInfoReturnable<Integer> cir) {
        ChimericWeatherState.get(source.getLevel()).setGrippfallDuration(0);
    }
}