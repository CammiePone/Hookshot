package dev.cammiescorner.hookshot.compat;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.item.HookshotItem;
import dev.cammiescorner.hookshot.registry.HookshotItems;
import dev.cammiescorner.hookshot.upgrade.HookshotUpgrade;
import dev.cammiescorner.hookshot.util.UpgradesHelper;
import dev.cammiescorner.hookshot.util.recipe.HookshotSmithingUpgradeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Collectors;

@JeiPlugin
public class JeiCompat implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = Hookshot.id("jei_plugin");

    public JeiCompat() {
        // NO-OP
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        HookshotItems.ITEMS.stream().filter(it -> it.get() instanceof HookshotItem).forEach(it -> {
            var item = it.get();
            registration.registerSubtypeInterpreter(item, (stack, context) -> {
                var upgrades = UpgradesHelper.getUpgrades(stack);
                if(upgrades.isEmpty()) {
                    return IIngredientSubtypeInterpreter.NONE;
                }

                return upgrades.stream().map(HookshotUpgrade::getId).map(ResourceLocation::toString).sorted().collect(Collectors.joining(","));
            });
        });
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getSmithingCategory().addExtension(HookshotSmithingUpgradeRecipe.class, new ISmithingCategoryExtension<>() {
            @Override
            public <T extends IIngredientAcceptor<T>> void setTemplate(HookshotSmithingUpgradeRecipe recipe, T ingredientAcceptor) {
                ingredientAcceptor.addIngredients(recipe.getTemplate());
            }

            @Override
            public <T extends IIngredientAcceptor<T>> void setBase(HookshotSmithingUpgradeRecipe recipe, T ingredientAcceptor) {
                ingredientAcceptor.addIngredients(recipe.getBase());
            }

            @Override
            public <T extends IIngredientAcceptor<T>> void setAddition(HookshotSmithingUpgradeRecipe recipe, T ingredientAcceptor) {
                ingredientAcceptor.addIngredients(recipe.getAddition());
            }

            @Override
            public <T extends IIngredientAcceptor<T>> void setOutput(HookshotSmithingUpgradeRecipe recipe, T ingredientAcceptor) {
                var stack = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());
                UpgradesHelper.addUpgrade(stack, recipe.getUpgrade());
                ingredientAcceptor.addItemStack(stack);
            }

            @Override
            public void onDisplayedIngredientsUpdate(HookshotSmithingUpgradeRecipe recipe, IRecipeSlotDrawable templateSlot, IRecipeSlotDrawable baseSlot, IRecipeSlotDrawable additionSlot, IRecipeSlotDrawable outputSlot, IFocusGroup focuses) {
                var stack = baseSlot.getDisplayedItemStack().map(ItemStack::copy).orElseGet(() -> new ItemStack(HookshotItems.WHITE_HOOKSHOT.get()));
                UpgradesHelper.addUpgrade(stack, recipe.getUpgrade());
                outputSlot.createDisplayOverrides().addItemStack(stack);
            }
        });
    }
}
