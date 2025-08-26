package dev.cammiescorner.hookshot.common.compat;

import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.HookshotConfig;
import org.jetbrains.annotations.Nullable;

public class ModmenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            @Nullable var config = Hookshot.configurator.getConfig(HookshotConfig.class);

            return config != null ? new ConfigScreen(null, config) : null;
        };
    }
}
