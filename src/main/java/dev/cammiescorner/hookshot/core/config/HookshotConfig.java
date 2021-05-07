package dev.cammiescorner.hookshot.core.config;

import dev.cammiescorner.hookshot.Hookshot;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Hookshot.MOD_ID)
public class HookshotConfig implements ConfigData
{
	public boolean unhookableBlacklist = true;
	public double defaultMaxRange = 24D;
	public double defaultMaxSpeed = 10D;
	public int durability = 0;
	public double rangeMultiplier = 2D;
	public double quickMultiplier = 1.5D;
}
