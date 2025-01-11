package dev.cammiescorner.hookshot.core.util.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.cammiescorner.hookshot.core.mixin.SmithingRecipeAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.UpgradeRecipe;

public class HookshotSmithingRecipe extends UpgradeRecipe {
	public HookshotSmithingRecipe(ResourceLocation id, Ingredient base, Ingredient addition, ItemStack result) {
		super(id, base, addition, result);
	}

	// I don't know why, I don't want to know why, I shouldn't
	// have to wonder why, but for whatever reason this stupid
	// NBT data won't add new tags unless we do this terribleness.
	@Override
	public ItemStack assemble(Container inv) {
		ItemStack stack = ((SmithingRecipeAccessor) this).getResult().copy();
		CompoundTag tag = inv.getItem(0).getTag();

		if(tag != null)
			stack.getOrCreateTag().merge(tag);

		return stack;
	}

	public static ItemStack getItemStack(JsonObject json) {
		String string = GsonHelper.getAsString(json, "item");

		Item item = BuiltInRegistries.ITEM.getOptional(new ResourceLocation(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));

		if(json.has("data")) {
			throw new JsonParseException("Disallowed data tag found");
		}
		else {
			int count = GsonHelper.getAsInt(json, "count", 1);
			String nbt = GsonHelper.getAsString(json, "nbt");
			ItemStack stack = new ItemStack(item, count);

			stack.getOrCreateTag().putBoolean(nbt, true);

			return stack;
		}
	}

	public static class Serializer implements RecipeSerializer<HookshotSmithingRecipe> {
		@Override
		public HookshotSmithingRecipe fromJson(ResourceLocation identifier, JsonObject jsonObject) {
			Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "base"));
			Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "addition"));
			ItemStack result = HookshotSmithingRecipe.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "result"));

			return new HookshotSmithingRecipe(identifier, base, addition, result);
		}

		@Override
		public HookshotSmithingRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf packetByteBuf) {
			Ingredient base = Ingredient.fromNetwork(packetByteBuf);
			Ingredient addition = Ingredient.fromNetwork(packetByteBuf);
			ItemStack result = packetByteBuf.readItem();

			return new HookshotSmithingRecipe(identifier, base, addition, result);
		}

		@Override
		public void write(FriendlyByteBuf packetByteBuf, HookshotSmithingRecipe smithingRecipe) {
			((SmithingRecipeAccessor) smithingRecipe).getBase().toNetwork(packetByteBuf);
			((SmithingRecipeAccessor) smithingRecipe).getAddition().toNetwork(packetByteBuf);
			packetByteBuf.writeItem(((SmithingRecipeAccessor) smithingRecipe).getResult());
		}
	}
}
