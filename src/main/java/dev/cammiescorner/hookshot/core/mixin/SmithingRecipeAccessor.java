package dev.cammiescorner.hookshot.core.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(UpgradeRecipe.class)
public interface SmithingRecipeAccessor {
	@Accessor("base")
	Ingredient getBase();

	@Accessor("addition")
	Ingredient getAddition();

	@Accessor("result")
	ItemStack getResult();
}
