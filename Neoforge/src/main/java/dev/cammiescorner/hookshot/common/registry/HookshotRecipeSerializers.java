package dev.cammiescorner.hookshot.common.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.util.recipe.HookshotShapelessRecipe;
import dev.cammiescorner.hookshot.common.util.recipe.HookshotSmithingUpgradeRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HookshotRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Hookshot.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HookshotSmithingUpgradeRecipe>> UPGRADE_SMITHING = RECIPE_SERIALIZERS.register("upgrade_smithing", HookshotSmithingUpgradeRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HookshotShapelessRecipe>> DYE_CRAFTING_SHAPELESS = RECIPE_SERIALIZERS.register("dye_crafting_shapeless", HookshotShapelessRecipe.Serializer::new);
}
