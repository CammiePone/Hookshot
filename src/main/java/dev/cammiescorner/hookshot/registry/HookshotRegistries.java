package dev.cammiescorner.hookshot.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.upgrade.HookshotUpgrade;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class HookshotRegistries {

    public static final ResourceKey<Registry<HookshotUpgrade>> HOOKSHOT_UPGRADES = ResourceKey.createRegistryKey(Hookshot.id("hookshot_upgrades"));
    public static final Registry<HookshotUpgrade> HOOKSHOT_UPGRADES_REGISTRY = FabricRegistryBuilder.createSimple(HOOKSHOT_UPGRADES).attribute(RegistryAttribute.SYNCED).buildAndRegister();
}
