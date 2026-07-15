package dev.cammiescorner.hookshot.common.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HookshotItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Hookshot.MOD_ID);

    public static final DeferredHolder<Item, HookshotItem> WHITE_HOOKSHOT = ITEMS.register("white_hookshot", () -> new HookshotItem(DyeColor.WHITE));
    public static final DeferredHolder<Item, HookshotItem> ORANGE_HOOKSHOT = ITEMS.register("orange_hookshot", () -> new HookshotItem(DyeColor.ORANGE));
    public static final DeferredHolder<Item, HookshotItem> MAGENTA_HOOKSHOT = ITEMS.register("magenta_hookshot", () -> new HookshotItem(DyeColor.MAGENTA));
    public static final DeferredHolder<Item, HookshotItem> LIGHT_BLUE_HOOKSHOT = ITEMS.register("light_blue_hookshot", () -> new HookshotItem(DyeColor.LIGHT_BLUE));
    public static final DeferredHolder<Item, HookshotItem> YELLOW_HOOKSHOT = ITEMS.register("yellow_hookshot", () -> new HookshotItem(DyeColor.YELLOW));
    public static final DeferredHolder<Item, HookshotItem> LIME_HOOKSHOT = ITEMS.register("lime_hookshot", () -> new HookshotItem(DyeColor.LIME));
    public static final DeferredHolder<Item, HookshotItem> PINK_HOOKSHOT = ITEMS.register("pink_hookshot", () -> new HookshotItem(DyeColor.PINK));
    public static final DeferredHolder<Item, HookshotItem> GRAY_HOOKSHOT = ITEMS.register("gray_hookshot", () -> new HookshotItem(DyeColor.GRAY));
    public static final DeferredHolder<Item, HookshotItem> LIGHT_GRAY_HOOKSHOT = ITEMS.register("light_gray_hookshot", () -> new HookshotItem(DyeColor.LIGHT_GRAY));
    public static final DeferredHolder<Item, HookshotItem> CYAN_HOOKSHOT = ITEMS.register("cyan_hookshot", () -> new HookshotItem(DyeColor.CYAN));
    public static final DeferredHolder<Item, HookshotItem> PURPLE_HOOKSHOT = ITEMS.register("purple_hookshot", () -> new HookshotItem(DyeColor.PURPLE));
    public static final DeferredHolder<Item, HookshotItem> BLUE_HOOKSHOT = ITEMS.register("blue_hookshot", () -> new HookshotItem(DyeColor.BLUE));
    public static final DeferredHolder<Item, HookshotItem> BROWN_HOOKSHOT = ITEMS.register("brown_hookshot", () -> new HookshotItem(DyeColor.BROWN));
    public static final DeferredHolder<Item, HookshotItem> GREEN_HOOKSHOT = ITEMS.register("green_hookshot", () -> new HookshotItem(DyeColor.GREEN));
    public static final DeferredHolder<Item, HookshotItem> RED_HOOKSHOT = ITEMS.register("red_hookshot", () -> new HookshotItem(DyeColor.RED));
    public static final DeferredHolder<Item, HookshotItem> BLACK_HOOKSHOT = ITEMS.register("black_hookshot", () -> new HookshotItem(DyeColor.BLACK));
}
