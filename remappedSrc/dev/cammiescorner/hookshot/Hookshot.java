package dev.cammiescorner.hookshot;

import dev.cammiescorner.hookshot.core.integration.HookshotConfig;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.registry.ModItems;
import dev.cammiescorner.hookshot.core.util.recipe.HookshotShapelessRecipe;
import dev.cammiescorner.hookshot.core.util.recipe.HookshotSmithingRecipe;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Hookshot implements ModInitializer
{
	public static final String MOD_ID = "hookshot";
	public static HookshotConfig config;

	@Override
	public void onInitialize()
	{
		// Config
		AutoConfig.register(HookshotConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(HookshotConfig.class).getConfig();

		// Objects
		ModItems.register();
		ModEntities.register();

		// Recipes
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "smithing"), new HookshotSmithingRecipe.Serializer());
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "crafting_shapeless"), new HookshotShapelessRecipe.Serializer());
	}
}
