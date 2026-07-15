package dev.cammiescorner.hookshot.common.util;

import net.minecraft.world.item.DyeColor;

public class ColorHelper {

    public static int dyeToDecimal(DyeColor color) {
        return 0xFF000000 | color.getTextureDiffuseColor();
    }
}
