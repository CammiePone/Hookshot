package dev.cammiescorner.hookshot.compat;

import dev.cammiescorner.hookshot.compat.emi.EMIHookshotSmithingRecipe;
import dev.cammiescorner.hookshot.util.recipe.HookshotSmithingUpgradeRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeType;

@EmiEntrypoint
public class EmiCompat implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        var recipeManager = registry.getRecipeManager();

        recipeManager.getAllRecipesFor(RecipeType.SMITHING).stream().filter(HookshotSmithingUpgradeRecipe.class::isInstance)
                .map(HookshotSmithingUpgradeRecipe.class::cast)
                .map(it -> new EMIHookshotSmithingRecipe(it, Minecraft.getInstance().level.registryAccess()))
                .forEach(registry::addRecipe);
    }
}
