package dev.cammiescorner.hookshot.core.util.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.cammiescorner.hookshot.core.mixin.SmithingRecipeAccessor;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class HookshotSmithingRecipe extends SmithingRecipe
{
	public HookshotSmithingRecipe(Identifier id, Ingredient base, Ingredient addition, ItemStack result)
	{
		super(id, base, addition, result);
	}

	// I don't know why, I don't want to know why, I shouldn't
	// have to wonder why, but for whatever reason this stupid
	// NBT data won't add new tags unless we do this terribleness.
	@Override
	public ItemStack craft(Inventory inv)
	{
		ItemStack stack = ((SmithingRecipeAccessor) this).getResult().copy();
		NbtCompound tag = inv.getStack(0).getNbt();

		if(tag != null && stack.hasNbt())
			tag.copyFrom(tag);

		return stack;
	}

	public static ItemStack getItemStack(JsonObject json)
	{
		String string = JsonHelper.getString(json, "item");

		Item item = Registry.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() ->
				new JsonSyntaxException("Unknown item '" + string + "'"));

		if(json.has("data"))
		{
			throw new JsonParseException("Disallowed data tag found");
		}
		else
		{
			int count = JsonHelper.getInt(json, "count", 1);
			String nbt = JsonHelper.getString(json, "nbt");
			ItemStack stack = new ItemStack(item, count);

			stack.getOrCreateNbt().putBoolean(nbt, true);

			return stack;
		}
	}

	public static class Serializer implements RecipeSerializer<HookshotSmithingRecipe>
	{
		@Override
		public HookshotSmithingRecipe read(Identifier identifier, JsonObject jsonObject)
		{
			Ingredient base = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base"));
			Ingredient addition = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "addition"));
			ItemStack result = HookshotSmithingRecipe.getItemStack(JsonHelper.getObject(jsonObject, "result"));

			return new HookshotSmithingRecipe(identifier, base, addition, result);
		}

		@Override
		public HookshotSmithingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf)
		{
			Ingredient base = Ingredient.fromPacket(packetByteBuf);
			Ingredient addition = Ingredient.fromPacket(packetByteBuf);
			ItemStack result = packetByteBuf.readItemStack();

			return new HookshotSmithingRecipe(identifier, base, addition, result);
		}

		@Override
		public void write(PacketByteBuf packetByteBuf, HookshotSmithingRecipe smithingRecipe)
		{
			((SmithingRecipeAccessor) smithingRecipe).getBase().write(packetByteBuf);
			((SmithingRecipeAccessor) smithingRecipe).getAddition().write(packetByteBuf);
			packetByteBuf.writeItemStack(((SmithingRecipeAccessor) smithingRecipe).getResult());
		}
	}
}
