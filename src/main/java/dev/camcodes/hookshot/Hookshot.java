package dev.camcodes.hookshot;

import dev.camcodes.hookshot.core.config.HookshotConfig;
import dev.camcodes.hookshot.core.registry.ModEntities;
import dev.camcodes.hookshot.core.registry.ModItems;
import dev.camcodes.hookshot.core.util.recipe.HookshotSmithingRecipe;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
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
		AutoConfig.register(HookshotConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(HookshotConfig.class).getConfig();
		ModItems.register();
		ModEntities.register();

		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "smithing"), new HookshotSmithingRecipe.Serializer());
	}
}
