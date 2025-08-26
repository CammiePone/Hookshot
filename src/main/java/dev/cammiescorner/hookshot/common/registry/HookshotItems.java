package dev.cammiescorner.hookshot.common.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

public class HookshotItems {

    public static final RegistryHandler<Item> ITEMS = RegistryHandler.create(Registries.ITEM, Hookshot.MOD_ID);

    public static final RegistrySupplier<HookshotItem> WHITE_HOOKSHOT = ITEMS.register("white_hookshot", () -> new HookshotItem(DyeColor.WHITE));
    public static final RegistrySupplier<HookshotItem> ORANGE_HOOKSHOT = ITEMS.register("orange_hookshot", () -> new HookshotItem(DyeColor.ORANGE));
    public static final RegistrySupplier<HookshotItem> MAGENTA_HOOKSHOT = ITEMS.register("magenta_hookshot", () -> new HookshotItem(DyeColor.MAGENTA));
    public static final RegistrySupplier<HookshotItem> LIGHT_BLUE_HOOKSHOT = ITEMS.register("light_blue_hookshot", () -> new HookshotItem(DyeColor.LIGHT_BLUE));
    public static final RegistrySupplier<HookshotItem> YELLOW_HOOKSHOT = ITEMS.register("yellow_hookshot", () -> new HookshotItem(DyeColor.YELLOW));
    public static final RegistrySupplier<HookshotItem> LIME_HOOKSHOT = ITEMS.register("lime_hookshot", () -> new HookshotItem(DyeColor.LIME));
    public static final RegistrySupplier<HookshotItem> PINK_HOOKSHOT = ITEMS.register("pink_hookshot", () -> new HookshotItem(DyeColor.PINK));
    public static final RegistrySupplier<HookshotItem> GRAY_HOOKSHOT = ITEMS.register("gray_hookshot", () -> new HookshotItem(DyeColor.GRAY));
    public static final RegistrySupplier<HookshotItem> LIGHT_GRAY_HOOKSHOT = ITEMS.register("light_gray_hookshot", () -> new HookshotItem(DyeColor.LIGHT_GRAY));
    public static final RegistrySupplier<HookshotItem> CYAN_HOOKSHOT = ITEMS.register("cyan_hookshot", () -> new HookshotItem(DyeColor.CYAN));
    public static final RegistrySupplier<HookshotItem> PURPLE_HOOKSHOT = ITEMS.register("purple_hookshot", () -> new HookshotItem(DyeColor.PURPLE));
    public static final RegistrySupplier<HookshotItem> BLUE_HOOKSHOT = ITEMS.register("blue_hookshot", () -> new HookshotItem(DyeColor.BLUE));
    public static final RegistrySupplier<HookshotItem> BROWN_HOOKSHOT = ITEMS.register("brown_hookshot", () -> new HookshotItem(DyeColor.BROWN));
    public static final RegistrySupplier<HookshotItem> GREEN_HOOKSHOT = ITEMS.register("green_hookshot", () -> new HookshotItem(DyeColor.GREEN));
    public static final RegistrySupplier<HookshotItem> RED_HOOKSHOT = ITEMS.register("red_hookshot", () -> new HookshotItem(DyeColor.RED));
    public static final RegistrySupplier<HookshotItem> BLACK_HOOKSHOT = ITEMS.register("black_hookshot", () -> new HookshotItem(DyeColor.BLACK));
}
