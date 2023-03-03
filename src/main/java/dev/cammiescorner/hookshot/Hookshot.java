package dev.cammiescorner.hookshot;

import dev.cammiescorner.hookshot.core.integration.HookshotConfig;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.registry.ModItems;
import dev.cammiescorner.hookshot.core.registry.ModSoundEvents;
import dev.cammiescorner.hookshot.core.util.recipe.HookshotShapelessRecipe;
import dev.cammiescorner.hookshot.core.util.recipe.HookshotSmithingRecipe;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Hookshot implements ModInitializer {
	public static final String MOD_ID = "hookshot";

	@Override
	public void onInitialize() {
		DataTrackers.HOOK_TRACKER.getId();
		// Config
		MidnightConfig.init(Hookshot.MOD_ID, HookshotConfig.class);

		// Objects
		ModItems.register();
		ModEntities.register();
		ModSoundEvents.register();

		// Recipes
		Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MOD_ID, "smithing"), new HookshotSmithingRecipe.Serializer());
		Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MOD_ID, "crafting_shapeless"), new HookshotShapelessRecipe.Serializer());
	}

	public static class DataTrackers {
		public static final TrackedData<Boolean> HOOK_TRACKER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	}
}
