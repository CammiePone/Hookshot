package dev.cammiescorner.hookshot.core.config;

import dev.cammiescorner.hookshot.Hookshot;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Hookshot.MOD_ID)
public class HookshotConfig implements ConfigData
{
	public boolean useClassicHookshotLogic = false;
	public boolean unhookableBlacklist = true;
	public boolean hookshotCancelsFallDamage = false;
	public double defaultMaxRange = 24D;
	public double defaultMaxSpeed = 10D;
	public double rangeMultiplier = 2D;
	public double quickMultiplier = 1.5D;
	public int durability = 250;
	public String hookshotRepairItem = "minecraft:iron_ingot";
}
