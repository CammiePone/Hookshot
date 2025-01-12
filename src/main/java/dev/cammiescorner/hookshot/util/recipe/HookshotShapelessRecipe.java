package dev.cammiescorner.hookshot.util.recipe;

import com.google.gson.JsonObject;
import dev.cammiescorner.hookshot.registry.HookshotRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class HookshotShapelessRecipe extends ShapelessRecipe {

	private final Ingredient hookshot;

	public HookshotShapelessRecipe(ResourceLocation id, String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients, Ingredient hookshot) {
		super(id, group, category, result, ingredients);
		this.hookshot = hookshot;
	}

	public static HookshotShapelessRecipe wrap(ShapelessRecipe recipe) {
		return new HookshotShapelessRecipe(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.result, recipe.getIngredients(), recipe.getIngredients().get(0));
	}

	public static HookshotShapelessRecipe wrap(ShapelessRecipe recipe, Ingredient hookshot) {
		NonNullList<Ingredient> ingredients = NonNullList.withSize(recipe.getIngredients().size() + 1, Ingredient.EMPTY);
		ingredients.set(0, hookshot);

		for(int i = 0; i < recipe.getIngredients().size(); i++) {
			ingredients.set(i + 1, recipe.getIngredients().get(i));
		}

		return new HookshotShapelessRecipe(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.result, ingredients, hookshot);
	}

	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
		ItemStack result = super.assemble(inv, registryAccess);

		ItemStack hook = null;
		for(int i = 0; i < inv.getContainerSize(); ++i) {
			ItemStack stack = inv.getItem(i);

			if(this.hookshot.test(stack)) {
				if(hook != null) {
					return ItemStack.EMPTY;
				}

				hook = stack;
			}
		}

		if(hook == null) {
			return ItemStack.EMPTY;
		}

		if(hook.hasTag()) {
			result.getOrCreateTag().merge(hook.getTag());
		}

		return result;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return HookshotRecipeSerializers.DYE_CRAFTING_SHAPELESS.get();
	}

	public static class Serializer implements RecipeSerializer<HookshotShapelessRecipe> {

		@Override
		public HookshotShapelessRecipe fromJson(ResourceLocation identifier, JsonObject jsonObject) {
			ShapelessRecipe wrappedRecipe = RecipeSerializer.SHAPELESS_RECIPE.fromJson(identifier, jsonObject);
			Ingredient hookshot = Ingredient.fromJson(GsonHelper.getNonNull(jsonObject, "hookshot"), false);

			return HookshotShapelessRecipe.wrap(wrappedRecipe, hookshot);
		}

		@Override
		public HookshotShapelessRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf packetByteBuf) {
			return HookshotShapelessRecipe.wrap(RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(identifier, packetByteBuf));
		}

		@Override
		public void toNetwork(FriendlyByteBuf packetByteBuf, HookshotShapelessRecipe shapelessRecipe) {
			RecipeSerializer.SHAPELESS_RECIPE.toNetwork(packetByteBuf, shapelessRecipe);
		}
	}
}
