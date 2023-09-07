package dev.cammiescorner.hookshot.core.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.SmithingTransformRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SmithingTransformRecipe.class)
public interface SmithingRecipeAccessor {
	
	@Accessor("base")
	Ingredient testBase();

	@Accessor("addition")
	Ingredient testAddition();

	@Accessor("result")
	ItemStack getOutput();
}
