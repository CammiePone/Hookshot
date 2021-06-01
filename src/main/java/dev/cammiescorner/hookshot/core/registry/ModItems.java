package dev.cammiescorner.hookshot.core.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ModItems
{
	//----Item Map----//
	public static final LinkedHashMap<Item, Identifier> ITEMS = new LinkedHashMap<>();

	//-----Items-----//
	public static final Item WHITE_HOOKSHOT = create("white_hookshot", new HookshotItem(DyeColor.WHITE));
	public static final Item ORANGE_HOOKSHOT = create("orange_hookshot", new HookshotItem(DyeColor.ORANGE));
	public static final Item MAGENTA_HOOKSHOT = create("magenta_hookshot", new HookshotItem(DyeColor.MAGENTA));
	public static final Item LIGHT_BLUE_HOOKSHOT = create("light_blue_hookshot", new HookshotItem(DyeColor.LIGHT_BLUE));
	public static final Item YELLOW_HOOKSHOT = create("yellow_hookshot", new HookshotItem(DyeColor.YELLOW));
	public static final Item LIME_HOOKSHOT = create("lime_hookshot", new HookshotItem(DyeColor.LIME));
	public static final Item PINK_HOOKSHOT = create("pink_hookshot", new HookshotItem(DyeColor.PINK));
	public static final Item GREY_HOOKSHOT = create("gray_hookshot", new HookshotItem(DyeColor.GRAY));
	public static final Item LIGHT_GREY_HOOKSHOT = create("light_gray_hookshot", new HookshotItem(DyeColor.LIGHT_GRAY));
	public static final Item CYAN_HOOKSHOT = create("cyan_hookshot", new HookshotItem(DyeColor.CYAN));
	public static final Item PURPLE_HOOKSHOT = create("purple_hookshot", new HookshotItem(DyeColor.PURPLE));
	public static final Item BLUE_HOOKSHOT = create("blue_hookshot", new HookshotItem(DyeColor.BLUE));
	public static final Item BROWN_HOOKSHOT = create("brown_hookshot", new HookshotItem(DyeColor.BROWN));
	public static final Item GREEN_HOOKSHOT = create("green_hookshot", new HookshotItem(DyeColor.GREEN));
	public static final Item RED_HOOKSHOT = create("red_hookshot", new HookshotItem(DyeColor.RED));
	public static final Item BLACK_HOOKSHOT = create("black_hookshot", new HookshotItem(DyeColor.BLACK));

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
