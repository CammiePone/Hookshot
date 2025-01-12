package dev.cammiescorner.hookshot;

import com.teamresourceful.resourcefulconfig.common.config.Configurator;
import dev.cammiescorner.hookshot.registry.*;
import dev.upcraft.sparkweave.api.registry.RegistryService;
import dev.upcraft.sparkweave.api.util.logging.SparkweaveLoggerFactory;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class Hookshot implements ModInitializer {

    public static final String MOD_ID = "hookshot";
    public static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();
    public static final Configurator configurator = new Configurator();

    @Override
    public void onInitialize() {
        configurator.registerConfig(HookshotConfig.class);

        // FIXME use CCA instead

        var registryService = RegistryService.get();
        HookshotEntities.ENTITY_TYPES.accept(registryService);
        HookshotItems.ITEMS.accept(registryService);
        HookshotRecipeSerializers.RECIPE_SERIALIZERS.accept(registryService);
        HookshotSoundEvents.SOUND_EVENTS.accept(registryService);
        HookshotUpgrades.UPGRADES.accept(registryService);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
