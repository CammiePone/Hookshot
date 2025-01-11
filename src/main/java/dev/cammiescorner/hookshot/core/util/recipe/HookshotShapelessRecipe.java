package dev.cammiescorner.hookshot.core.util.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class HookshotShapelessRecipe extends ShapelessRecipe {
	public HookshotShapelessRecipe(ResourceLocation id, String group, CraftingBookCategory category, ItemStack output, NonNullList<Ingredient> input) {
		super(id, group, category, output, input);
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
		ItemStack stack = this.getResultItem().copy();
		CompoundTag tag = null;

		for(int i = 0; i < inv.getContainerSize(); ++i) {
			ItemStack stacks = inv.getItem(i);

			if(stacks.getItem() instanceof HookshotItem)
				tag = stacks.getTag();
		}

		if(tag != null && stack.hasTag())
			tag.merge(tag);

		return stack;
	}

	public static class Serializer implements RecipeSerializer<HookshotShapelessRecipe> {
		private static NonNullList<Ingredient> getIngredients(JsonArray json) {
			NonNullList<Ingredient> defaultedList = NonNullList.create();

			for(int i = 0; i < json.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(json.get(i));

				if(!ingredient.isEmpty())
					defaultedList.add(ingredient);
			}

			return defaultedList;
		}

		@Override
		public HookshotShapelessRecipe fromJson(ResourceLocation identifier, JsonObject jsonObject) {
			String string = GsonHelper.getAsString(jsonObject, "group", "");
			NonNullList<Ingredient> defaultedList = getIngredients(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));

			if(defaultedList.isEmpty()) {
				throw new JsonParseException("No ingredients for shapeless recipe");
			}
			else if(defaultedList.size() > 9) {
				throw new JsonParseException("Too many ingredients for shapeless recipe");
			}
			else {
				ItemStack itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
				return new HookshotShapelessRecipe(identifier, string, CraftingBookCategory.MISC, itemStack, defaultedList);
			}
		}

		@Override
		public HookshotShapelessRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf packetByteBuf) {
			String string = packetByteBuf.readUtf(32767);
			int i = packetByteBuf.readVarInt();
			NonNullList<Ingredient> defaultedList = NonNullList.withSize(i, Ingredient.EMPTY);

			for(int j = 0; j < defaultedList.size(); ++j)
				defaultedList.set(j, Ingredient.fromNetwork(packetByteBuf));

			ItemStack itemStack = packetByteBuf.readItem();

			return new HookshotShapelessRecipe(identifier, string, CraftingBookCategory.MISC, itemStack, defaultedList);
		}

		@Override
		public void write(FriendlyByteBuf packetByteBuf, HookshotShapelessRecipe shapelessRecipe) {
			packetByteBuf.writeUtf(shapelessRecipe.getGroup());
			packetByteBuf.writeVarInt(shapelessRecipe.getIngredients().size());

			for(Ingredient ingredient : shapelessRecipe.getIngredients())
				ingredient.toNetwork(packetByteBuf);

			packetByteBuf.writeItem(shapelessRecipe.getResultItem());
		}
	}
}
