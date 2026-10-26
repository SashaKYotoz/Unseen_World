package net.sashakyotoz.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.compat.UWInfoPages;

import java.util.ArrayList;
import java.util.List;

@EmiEntrypoint
public class UWEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        for (UWInfoPages.Page page : UWInfoPages.PAGES) {
            List<EmiIngredient> stacks = new ArrayList<>();
            page.items().get().forEach(item -> stacks.add(EmiStack.of(item)));
            List<Component> text = new ArrayList<>();
            for (int line = 0; line < page.lines().size(); line++) {
                text.add(Component.translatable(UWInfoPages.lineKey(page, line)));
            }
            registry.addRecipe(new EmiInfoRecipe(stacks, text, UnseenWorld.MOD.makeID("/info/" + page.id())));
        }
    }
}