package dev.cammiescorner.hookshot.core.util;

import net.minecraft.item.ItemStack;

public class UpgradesHelper
{
	public static boolean hasAquaticUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasAqua");
	}

	public static boolean hasAutomaticUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasAuto");
	}

	public static boolean hasEndericUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasEnder");
	}

	public static boolean hasQuickUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasQuick");
	}

	public static boolean hasRangeUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasRange");
	}
}
