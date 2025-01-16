package dev.cammiescorner.hookshot;

import com.teamresourceful.resourcefulconfig.common.annotations.Config;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;

@Config(Hookshot.MOD_ID)
public final class HookshotConfig {
	@ConfigEntry(id = "useClassicHookshotLogic", type = EntryType.BOOLEAN, translation = "config.hookshot.useClassicHookshotLogic")
	public static boolean useClassicHookshotLogic = false;

	@ConfigEntry(id = "hookshotCancelsFallDamage", type = EntryType.BOOLEAN, translation = "config.hookshot.hookshotCancelsFallDamage")
	public static boolean hookshotCancelsFallDamage = false;

	@ConfigEntry(id = "quickUpgradeAffectsPullSpeed", type = EntryType.BOOLEAN, translation = "config.hookshot.quickUpgradeAffectsPullSpeed")
	public static boolean quickUpgradeAffectsPullSpeed = false;

	@ConfigEntry(id = "defaultMaxRange", type = EntryType.DOUBLE, translation = "config.hookshot.defaultMaxRange")
	public static double defaultMaxRange = 24D;

	@ConfigEntry(id = "defaultMaxSpeed", type = EntryType.DOUBLE, translation = "config.hookshot.defaultMaxSpeed")
	public static double defaultMaxSpeed = 10D;

	@ConfigEntry(id = "rangeMultiplier", type = EntryType.DOUBLE, translation = "config.hookshot.rangeMultiplier")
	public static double rangeMultiplier = 2D;

	@ConfigEntry(id = "quickMultiplier", type = EntryType.DOUBLE, translation = "config.hookshot.quickMultiplier")
	public static double quickMultiplier = 1.5D;

	@ConfigEntry(id = "durabilityMultiplier", type = EntryType.DOUBLE, translation = "config.hookshot.durabilityMultiplier")
	public static double durabilityMultiplier = 2D;

	@ConfigEntry(id = "defaultMaxDurability", type = EntryType.INTEGER, translation = "config.hookshot.defaultMaxDurability")
	public static int defaultMaxDurability = 512;

	@ConfigEntry(id = "hookshotCooldown", type = EntryType.INTEGER, translation = "config.hookshot.hookshotCooldown")
	public static int hookshotCooldown = 0;

	@ConfigEntry(id = "hookshotAffectsVehicle", type = EntryType.BOOLEAN, translation = "config.hookshot.hookshotAffectsVehicle")
	public static boolean hooksAffectVehicles = false;
}
