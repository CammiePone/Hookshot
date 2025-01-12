package dev.cammiescorner.hookshot.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class HookshotSoundEvents {

	public static final RegistryHandler<SoundEvent> SOUND_EVENTS = RegistryHandler.create(Registries.SOUND_EVENT, Hookshot.MOD_ID);

	public static final RegistrySupplier<SoundEvent> HOOKSHOT_REEL = register("hookshot_reel");

	private static RegistrySupplier<SoundEvent> register(String name) {
		return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Hookshot.id(name)));
	}
}
