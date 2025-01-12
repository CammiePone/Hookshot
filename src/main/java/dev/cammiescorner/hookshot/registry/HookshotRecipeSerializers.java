package dev.cammiescorner.hookshot.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.util.recipe.HookshotShapelessRecipe;
import dev.cammiescorner.hookshot.util.recipe.HookshotSmithingUpgradeRecipe;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class HookshotRecipeSerializers {

    public static final RegistryHandler<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistryHandler.create(Registries.RECIPE_SERIALIZER, Hookshot.MOD_ID);

    public static final RegistrySupplier<RecipeSerializer<HookshotSmithingUpgradeRecipe>> UPGRADE_SMITHING = RECIPE_SERIALIZERS.register("upgrade_smithing", HookshotSmithingUpgradeRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeSerializer<HookshotShapelessRecipe>> DYE_CRAFTING_SHAPELESS = RECIPE_SERIALIZERS.register("dye_crafting_shapeless", HookshotShapelessRecipe.Serializer::new);
}
