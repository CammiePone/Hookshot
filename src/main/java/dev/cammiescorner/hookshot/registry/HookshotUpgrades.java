package dev.cammiescorner.hookshot.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.upgrade.HookshotUpgrade;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;

public class HookshotUpgrades {

    public static final RegistryHandler<HookshotUpgrade> UPGRADES = RegistryHandler.create(HookshotRegistries.HOOKSHOT_UPGRADES, Hookshot.MOD_ID);

    public static final RegistrySupplier<HookshotUpgrade> AQUATIC = UPGRADES.register("aquatic", HookshotUpgrade::new);
    public static final RegistrySupplier<HookshotUpgrade> AUTOMATIC = UPGRADES.register("automatic", HookshotUpgrade::new);
    public static final RegistrySupplier<HookshotUpgrade> BLEED = UPGRADES.register("bleed", HookshotUpgrade::new);
    public static final RegistrySupplier<HookshotUpgrade> DURABILITY = UPGRADES.register("durability", HookshotUpgrade::new);
    public static final RegistrySupplier<HookshotUpgrade> ENDERIC = UPGRADES.register("enderic", HookshotUpgrade::new);
    public static final RegistrySupplier<HookshotUpgrade> RANGE = UPGRADES.register("range", HookshotUpgrade::new);
    public static final RegistrySupplier<HookshotUpgrade> SPEED = UPGRADES.register("speed", HookshotUpgrade::new);
//    public static final RegistrySupplier<HookshotUpgrade> SWINGING = UPGRADES.register("swinging", HookshotUpgrade::new);
}
