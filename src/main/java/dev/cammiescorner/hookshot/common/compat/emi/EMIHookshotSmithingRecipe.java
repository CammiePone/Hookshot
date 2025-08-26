package dev.cammiescorner.hookshot.common.compat.emi;

import dev.cammiescorner.hookshot.common.util.recipe.HookshotSmithingUpgradeRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class EMIHookshotSmithingRecipe implements EmiRecipe {

    private static final int SEED = 1208740926;
    private final EmiIngredient template;
    private final EmiIngredient base;
    private final EmiIngredient addition;
    private final EmiStack output;
    private final HookshotSmithingUpgradeRecipe recipe;

    public EMIHookshotSmithingRecipe(HookshotSmithingUpgradeRecipe recipe, RegistryAccess registryAccess) {
        this.template = EmiIngredient.of(recipe.getTemplate());
        this.base = EmiIngredient.of(recipe.getBase());
        this.addition = EmiIngredient.of(recipe.getAddition());
        this.output = EmiStack.of(recipe.getResultItem(registryAccess));
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return VanillaEmiRecipeCategories.SMITHING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(template, base, addition);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }

    @Override
    public int getDisplayWidth() {
        return 112;
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);
        var templateWidget = widgets.addGeneratedSlot(random -> getStack(random, template), SEED, 0, 0).appendTooltip(() -> EmiTooltipComponents.getIngredientTooltipComponent(template.getEmiStacks()));
        var baseWidget = widgets.addGeneratedSlot(random -> getStack(random, base), SEED, 18, 0).appendTooltip(() -> EmiTooltipComponents.getIngredientTooltipComponent(base.getEmiStacks()));
        var additionWidget = widgets.addGeneratedSlot(random -> getStack(random, addition), SEED, 36, 0).appendTooltip(() -> EmiTooltipComponents.getIngredientTooltipComponent(addition.getEmiStacks()));
        widgets.addGeneratedSlot(random -> {
            var inv = new SimpleContainer(getStackFrom(templateWidget), getStackFrom(baseWidget), getStackFrom(additionWidget), ItemStack.EMPTY);
            var result = recipe.assemble(inv, Minecraft.getInstance().level.registryAccess());
            return EmiStack.of(result);
        }, SEED, 94, 0).recipeContext(this);
    }

    private ItemStack getStackFrom(SlotWidget widget) {
        var stacks = widget.getStack().getEmiStacks();
        if(!stacks.isEmpty()) {
            return stacks.get(0).getItemStack();
        }

        return ItemStack.EMPTY;
    }

    private EmiStack getStack(Random random, EmiIngredient ingredient) {
        var stacks = ingredient.getEmiStacks();
        if(!stacks.isEmpty()) {
            return stacks.get(random.nextInt(stacks.size()));
        }

        return EmiStack.EMPTY;
    }
}
