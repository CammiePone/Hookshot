package dev.cammiescorner.hookshot.core.registry;

import dev.cammiescorner.hookshot.Hookshot;
import java.util.LinkedHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {
	//-----Sound Map-----//
	public static final LinkedHashMap<SoundEvent, ResourceLocation> SOUNDS = new LinkedHashMap<>();

	//-----Sound Events-----//
	public static final SoundEvent HOOKSHOT_REEL = create("hookshot_reel");

	//-----Registry-----//
	public static void register() {
		//SOUNDS.keySet().forEach(sound -> Registry.register(Registry.SOUND_EVENT, SOUNDS.get(sound), sound));
		SOUNDS.keySet().forEach(sound -> Registry.register(BuiltInRegistries.SOUND_EVENT, SOUNDS.get(sound), sound));
	}

	private static SoundEvent create(String name) {
		SoundEvent sound = SoundEvent.createVariableRangeEvent(new ResourceLocation(Hookshot.MOD_ID, name));
		SOUNDS.put(sound, new ResourceLocation(Hookshot.MOD_ID, name));
		return sound;
	}
}
