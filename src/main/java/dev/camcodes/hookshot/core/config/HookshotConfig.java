package dev.camcodes.hookshot.core.config;

import dev.camcodes.hookshot.Hookshot;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = Hookshot.MOD_ID)
public class HookshotConfig implements ConfigData
{
	public double defaultMaxRange = 24D;
	public double defaultMaxSpeed = 10D;
	public int durability = 0;
	public double rangeMult = 2D;
	public double quickMult = 1.5D;
}
