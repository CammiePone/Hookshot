package dev.cammiescorner.hookshot;

import com.teamresourceful.resourcefulconfig.common.config.Configurator;
import dev.cammiescorner.hookshot.registry.*;
import dev.cammiescorner.hookshot.util.UpgradesHelper;
import dev.upcraft.sparkweave.api.registry.RegistryService;
import dev.upcraft.sparkweave.api.util.logging.SparkweaveLoggerFactory;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Logger;

public class Hookshot implements ModInitializer {

    public static final String MOD_ID = "hookshot";
    public static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();
    public static final Configurator configurator = new Configurator();

    @Override
    public void onInitialize() {
        configurator.registerConfig(HookshotConfig.class);

        var registryService = RegistryService.get();
        HookshotEntities.ENTITY_TYPES.accept(registryService);
        HookshotItems.ITEMS.accept(registryService);
        HookshotRecipeSerializers.RECIPE_SERIALIZERS.accept(registryService);
        HookshotSoundEvents.SOUND_EVENTS.accept(registryService);
        HookshotUpgrades.UPGRADES.accept(registryService);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.accept(HookshotItems.WHITE_HOOKSHOT.get());

            HookshotRegistries.HOOKSHOT_UPGRADES_REGISTRY.forEach(upgrade -> {
                var stack = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());
                UpgradesHelper.addUpgrade(stack, upgrade);
                entries.accept(stack);
            });

            entries.accept(HookshotItems.ORANGE_HOOKSHOT.get());
            entries.accept(HookshotItems.MAGENTA_HOOKSHOT.get());
            entries.accept(HookshotItems.LIGHT_BLUE_HOOKSHOT.get());
            entries.accept(HookshotItems.YELLOW_HOOKSHOT.get());
            entries.accept(HookshotItems.LIME_HOOKSHOT.get());
            entries.accept(HookshotItems.PINK_HOOKSHOT.get());
            entries.accept(HookshotItems.GRAY_HOOKSHOT.get());
            entries.accept(HookshotItems.LIGHT_GRAY_HOOKSHOT.get());
            entries.accept(HookshotItems.CYAN_HOOKSHOT.get());
            entries.accept(HookshotItems.PURPLE_HOOKSHOT.get());
            entries.accept(HookshotItems.BLUE_HOOKSHOT.get());
            entries.accept(HookshotItems.BROWN_HOOKSHOT.get());
            entries.accept(HookshotItems.GREEN_HOOKSHOT.get());
            entries.accept(HookshotItems.RED_HOOKSHOT.get());
            entries.accept(HookshotItems.BLACK_HOOKSHOT.get());
        });
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
