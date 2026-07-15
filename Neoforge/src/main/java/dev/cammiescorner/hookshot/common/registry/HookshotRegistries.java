package dev.cammiescorner.hookshot.common.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.upgrade.HookshotUpgrade;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class HookshotRegistries {

    public static final ResourceKey<Registry<HookshotUpgrade>> HOOKSHOT_UPGRADES = ResourceKey.createRegistryKey(Hookshot.id("hookshot_upgrades"));
}
