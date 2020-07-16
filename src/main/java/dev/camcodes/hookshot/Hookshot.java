package dev.camcodes.hookshot;

import dev.camcodes.hookshot.core.registry.ModEntities;
import dev.camcodes.hookshot.core.registry.ModItems;
import net.fabricmc.api.ModInitializer;

public class Hookshot implements ModInitializer
{
	public static final String MOD_ID = "hookshot";

	@Override
	public void onInitialize()
	{
		ModItems.register();
		ModEntities.register();
	}
}
