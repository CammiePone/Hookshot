package dev.cammiescorner.hookshot;

import dev.cammiescorner.hookshot.core.integration.HookshotConfig;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.registry.ModItems;
import dev.cammiescorner.hookshot.core.registry.ModSoundEvents;
import dev.cammiescorner.hookshot.core.util.recipe.HookshotShapelessRecipe;
import dev.cammiescorner.hookshot.core.util.recipe.HookshotSmithingRecipe;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

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
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(MOD_ID, "smithing"), new HookshotSmithingRecipe.Serializer());
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(MOD_ID, "crafting_shapeless"), new HookshotShapelessRecipe.Serializer());
	}

	public static class DataTrackers {
		public static final EntityDataAccessor<Boolean> HOOK_TRACKER = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
	}
}
