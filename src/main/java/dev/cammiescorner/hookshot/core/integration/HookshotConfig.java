package dev.cammiescorner.hookshot.core.integration;

import dev.cammiescorner.hookshot.Hookshot;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Hookshot.MOD_ID)
public class HookshotConfig implements ConfigData
{
	@Comment("Should the Hookshot use the \"hold Right Click to" +
			"\n  keep the hook out\" (New) or should it use the" +
			"\n  \"press Right Click to fire the hook, and right click" +
			"\n  again to retract\" (Classic) system. (Default: false)")
	public boolean useClassicHookshotLogic = false;

	@Comment("Should the Unhookable Tag act as a blacklist (true) or" +
			"\n  a whitelist (false)? (Default: true)")
	public boolean unhookableBlacklist = true;

	@Comment("Should the Hookshot cancel all fall damage if the" +
			"\n  Hook is active in the world and connected to" +
			"\n  something? (Default: false)")
	public boolean hookshotCancelsFallDamage = false;

	@Comment("The base maximum range of the Hookshot with no" +
			"\n  modifiers applied. (Default: 24 blocks)")
	public double defaultMaxRange = 24D;

	@Comment("The base maximum speed of the Hookshot with no" +
			"\n  modifiers applied. (Default: 10 blocks/s)")
	public double defaultMaxSpeed = 10D;

	@Comment("The multiplier the Range modifier applies to the" +
			"\n  default range. (Default: x2)")
	public double rangeMultiplier = 2D;

	@Comment("The multiplier the Quick modifier applies to the" +
			"\n  default speed. (Default: x1.5)")
	public double quickMultiplier = 1.5D;

	@Comment("The maximum durability of the Hookshot. Set to 0" +
			"\n  to be unbreakable. (Default: 250)")
	public int durability = 250;

	@Comment("The ID of the item used to repair the Hookshot." +
			"\n  (Default: minecraft:iron_ingot)")
	public String hookshotRepairItem = "minecraft:iron_ingot";
}
