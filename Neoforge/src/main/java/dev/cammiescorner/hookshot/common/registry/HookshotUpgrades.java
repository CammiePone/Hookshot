package dev.cammiescorner.hookshot.common.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.upgrade.HookshotUpgrade;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HookshotUpgrades {

    public static final DeferredRegister<HookshotUpgrade> UPGRADES = DeferredRegister.create(HookshotRegistries.HOOKSHOT_UPGRADES, Hookshot.MOD_ID);
    public static final Registry<HookshotUpgrade> REGISTRY = UPGRADES.makeRegistry(builder -> builder.sync(true));

    public static final DeferredHolder<HookshotUpgrade, HookshotUpgrade> AQUATIC = UPGRADES.register("aquatic", HookshotUpgrade::new);
    public static final DeferredHolder<HookshotUpgrade, HookshotUpgrade> AUTOMATIC = UPGRADES.register("automatic", HookshotUpgrade::new);
    public static final DeferredHolder<HookshotUpgrade, HookshotUpgrade> BLEED = UPGRADES.register("bleed", HookshotUpgrade::new);
    public static final DeferredHolder<HookshotUpgrade, HookshotUpgrade> DURABILITY = UPGRADES.register("durability", HookshotUpgrade::new);
    public static final DeferredHolder<HookshotUpgrade, HookshotUpgrade> ENDERIC = UPGRADES.register("enderic", HookshotUpgrade::new);
    public static final DeferredHolder<HookshotUpgrade, HookshotUpgrade> RANGE = UPGRADES.register("range", HookshotUpgrade::new);
    public static final DeferredHolder<HookshotUpgrade, HookshotUpgrade> SPEED = UPGRADES.register("speed", HookshotUpgrade::new);
}
