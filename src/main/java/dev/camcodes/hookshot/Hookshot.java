package dev.camcodes.hookshot;

import dev.camcodes.hookshot.core.config.HookshotConfig;
import dev.camcodes.hookshot.core.registry.ModEntities;
import dev.camcodes.hookshot.core.registry.ModItems;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

public class Hookshot implements ModInitializer
{
	public static final String MOD_ID = "hookshot";
	public static HookshotConfig config;

	@Override
	public void onInitialize()
	{
		config = ConfigManager.loadConfig(HookshotConfig.class);
		ModItems.register();
		ModEntities.register();
	}
}
