package dev.camcodes.hookshot.core.registry;

import dev.camcodes.hookshot.Hookshot;
import dev.camcodes.hookshot.common.item.HookshotItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ModItems
{
	//----Item Map----//
	public static final LinkedHashMap<Item, Identifier> ITEMS = new LinkedHashMap<>();

	//-----Items-----//
	public static final Item HOOKSHOT = create("hookshot", new HookshotItem(24D, 10D));
	public static final Item LONGSHOT = create("longshot", new HookshotItem(48D, 10D));
	public static final Item SPEEDSHOT = create("speedshot", new HookshotItem(24D, 15D));

	//-----Registry-----//
	public static void register()
	{
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
	}

	private static <T extends Item> T create(String name, T item)
	{
		ITEMS.put(item, new Identifier(Hookshot.MOD_ID, name));
		return item;
	}
}
