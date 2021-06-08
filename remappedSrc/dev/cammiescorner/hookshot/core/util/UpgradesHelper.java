package dev.cammiescorner.hookshot.core.util;

import net.minecraft.item.ItemStack;

public class UpgradesHelper
{
	public static boolean hasDurabilityUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasDurabilityUpgrade");
	}

	public static boolean hasAutomaticUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasAutomaticUpgrade");
	}

	public static boolean hasSwingingUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasSwingingUpgrade");
	}

	public static boolean hasAquaticUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasAquaticUpgrade");
	}

	public static boolean hasEndericUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasEndericUpgrade");
	}

	public static boolean hasQuickUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasQuickUpgrade");
	}

	public static boolean hasRangeUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasRangeUpgrade");
	}

	public static boolean hasBleedUpgrade(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().getBoolean("hasBleedUpgrade");
	}
}
