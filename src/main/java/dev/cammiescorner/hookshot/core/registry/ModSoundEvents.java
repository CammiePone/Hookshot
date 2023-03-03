package dev.cammiescorner.hookshot.core.registry;

import dev.cammiescorner.hookshot.Hookshot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;

public class ModSoundEvents
{
	//-----Sound Map-----//
	public static final LinkedHashMap<SoundEvent, Identifier> SOUNDS = new LinkedHashMap<>();

	//-----Sound Events-----//
	public static final SoundEvent HOOKSHOT_REEL = create("hookshot_reel");

	//-----Registry-----//
	public static void register()
	{
		//SOUNDS.keySet().forEach(sound -> Registry.register(Registry.SOUND_EVENT, SOUNDS.get(sound), sound));
		SOUNDS.keySet().forEach(sound -> Registry.register(Registries.SOUND_EVENT, SOUNDS.get(sound), sound));
	}

	private static SoundEvent create(String name)
	{
		SoundEvent sound = SoundEvent.of(new Identifier(Hookshot.MOD_ID, name));
		SOUNDS.put(sound, new Identifier(Hookshot.MOD_ID, name));
		return sound;
	}
}
