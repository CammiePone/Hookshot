package dev.cammiescorner.hookshot.util.datagen;

import com.google.gson.JsonObject;
import dev.cammiescorner.hookshot.registry.HookshotRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Supplier;

public class HookshotShapelessRecipeBuilder extends ShapelessRecipeBuilder {

    private final Ingredient hookshot;

    protected HookshotShapelessRecipeBuilder(RecipeCategory category, Ingredient hookshot, ItemLike result, int count) {
        super(category, result, count);
        this.hookshot = hookshot;
    }

    public static HookshotShapelessRecipeBuilder shapeless(RecipeCategory category, Ingredient hookshot, Supplier<? extends ItemLike> result) {
        return shapeless(category, hookshot, result, 1);
    }

    public static HookshotShapelessRecipeBuilder shapeless(RecipeCategory category, Ingredient hookshot, Supplier<? extends ItemLike> result, int count) {
        return new HookshotShapelessRecipeBuilder(category, hookshot, result.get(), count);
    }

    public Ingredient getHookshot() {
        return hookshot;
    }

    public static class Result extends ShapelessRecipeBuilder.Result {

        private final Ingredient hookshot;

        public Result(ResourceLocation id, Item result, int count, String group, CraftingBookCategory category, List<Ingredient> ingredients, Ingredient hookshot, Advancement.Builder advancement, ResourceLocation advancementId) {
            super(id, result, count, group, category, ingredients, advancement, advancementId);
            this.hookshot = hookshot;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return HookshotRecipeSerializers.DYE_CRAFTING_SHAPELESS.get();
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            super.serializeRecipeData(json);
            json.add("hookshot", hookshot.toJson());
        }
    }
}
