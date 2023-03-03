package dev.cammiescorner.hookshot.core.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.cammiescorner.hookshot.Hookshot;

public class HookshotModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> HookshotConfig.getScreen(parent, Hookshot.MOD_ID);
	}
}
