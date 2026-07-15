package dev.cammiescorner.hookshot.common.util.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.hookshot.common.registry.HookshotRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.List;

/**
 * A shapeless recipe (hookshot + dye) that copies the input hookshot's components
 * (upgrades, damage, custom name, ...) onto the re-coloured result.
 */
public class HookshotShapelessRecipe extends ShapelessRecipe {

    private final Ingredient hookshot;
    private final NonNullList<Ingredient> extraIngredients;

    public HookshotShapelessRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients, Ingredient hookshot) {
        super(group, category, result, prepend(hookshot, ingredients));
        this.hookshot = hookshot;
        this.extraIngredients = ingredients;
    }

    private static NonNullList<Ingredient> prepend(Ingredient hookshot, NonNullList<Ingredient> ingredients) {
        NonNullList<Ingredient> combined = NonNullList.withSize(ingredients.size() + 1, Ingredient.EMPTY);
        combined.set(0, hookshot);

        for (int i = 0; i < ingredients.size(); i++) {
            combined.set(i + 1, ingredients.get(i));
        }

        return combined;
    }

    private static NonNullList<Ingredient> toNonNullList(List<Ingredient> ingredients) {
        NonNullList<Ingredient> list = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

        for (int i = 0; i < ingredients.size(); i++) {
            list.set(i, ingredients.get(i));
        }

        return list;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = super.assemble(input, registries);

        ItemStack hook = null;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);

            if (this.hookshot.test(stack)) {
                if (hook != null) {
                    return ItemStack.EMPTY;
                }

                hook = stack;
            }
        }

        if (hook == null) {
            return ItemStack.EMPTY;
        }

        result.applyComponents(hook.getComponentsPatch());

        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HookshotRecipeSerializers.DYE_CRAFTING_SHAPELESS.get();
    }

    public static class Serializer implements RecipeSerializer<HookshotShapelessRecipe> {

        private static final MapCodec<HookshotShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(HookshotShapelessRecipe::getGroup),
                CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(HookshotShapelessRecipe::category),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.getResultItem(null)),
                Ingredient.CODEC_NONEMPTY.listOf().xmap(HookshotShapelessRecipe::toNonNullList, list -> list).fieldOf("ingredients").forGetter(recipe -> recipe.extraIngredients),
                Ingredient.CODEC_NONEMPTY.fieldOf("hookshot").forGetter(recipe -> recipe.hookshot)
        ).apply(instance, HookshotShapelessRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, HookshotShapelessRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        private static HookshotShapelessRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);

            int count = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));

            Ingredient hookshot = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);

            return new HookshotShapelessRecipe(group, category, result, ingredients, hookshot);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, HookshotShapelessRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeEnum(recipe.category());

            buffer.writeVarInt(recipe.extraIngredients.size());
            for (Ingredient ingredient : recipe.extraIngredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.hookshot);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.getResultItem(null));
        }

        @Override
        public MapCodec<HookshotShapelessRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HookshotShapelessRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
