package dev.cammiescorner.hookshot.core.util.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class HookshotShapelessRecipe extends ShapelessRecipe
{
	public HookshotShapelessRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input)
	{
		super(id, group, output, input);
	}

	@Override
	public ItemStack craft(CraftingInventory inv)
	{
		ItemStack stack = this.getOutput().copy();
		NbtCompound tag = null;

		for(int i = 0; i < inv.size(); ++i)
		{
			ItemStack stacks = inv.getStack(i);

			if(stacks.getItem() instanceof HookshotItem)
				tag = stacks.getNbt();
		}

		if(tag != null && stack.hasNbt())
			tag.copyFrom(tag);

		return stack;
	}

	public static class Serializer implements RecipeSerializer<HookshotShapelessRecipe>
	{
		private static DefaultedList<Ingredient> getIngredients(JsonArray json)
		{
			DefaultedList<Ingredient> defaultedList = DefaultedList.of();

			for(int i = 0; i < json.size(); ++i)
			{
				Ingredient ingredient = Ingredient.fromJson(json.get(i));

				if(!ingredient.isEmpty())
					defaultedList.add(ingredient);
			}

			return defaultedList;
		}

		@Override
		public HookshotShapelessRecipe read(Identifier identifier, JsonObject jsonObject)
		{
			String string = JsonHelper.getString(jsonObject, "group", "");
			DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));

			if(defaultedList.isEmpty())
			{
				throw new JsonParseException("No ingredients for shapeless recipe");
			}
			else if (defaultedList.size() > 9)
			{
				throw new JsonParseException("Too many ingredients for shapeless recipe");
			}
			else
			{
				ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
				return new HookshotShapelessRecipe(identifier, string, itemStack, defaultedList);
			}
		}

		@Override
		public HookshotShapelessRecipe read(Identifier identifier, PacketByteBuf packetByteBuf)
		{
			String string = packetByteBuf.readString(32767);
			int i = packetByteBuf.readVarInt();
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

			for(int j = 0; j < defaultedList.size(); ++j)
				defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));

			ItemStack itemStack = packetByteBuf.readItemStack();

			return new HookshotShapelessRecipe(identifier, string, itemStack, defaultedList);
		}

		@Override
		public void write(PacketByteBuf packetByteBuf, HookshotShapelessRecipe shapelessRecipe)
		{
			packetByteBuf.writeString(shapelessRecipe.getGroup());
			packetByteBuf.writeVarInt(shapelessRecipe.getIngredients().size());

			for(Ingredient ingredient : shapelessRecipe.getIngredients())
				ingredient.write(packetByteBuf);

			packetByteBuf.writeItemStack(shapelessRecipe.getOutput());
		}
	}
}
