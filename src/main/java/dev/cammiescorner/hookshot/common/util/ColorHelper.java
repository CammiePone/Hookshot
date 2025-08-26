package dev.cammiescorner.hookshot.common.util;

import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

public class ColorHelper {

	public static int dyeToDecimal(DyeColor color) {
		float[] rgb = color.getTextureDiffuseColors();
		return Mth.color(rgb[0], rgb[1], rgb[2]);
	}
}
