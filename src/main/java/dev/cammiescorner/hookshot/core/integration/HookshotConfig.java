package dev.cammiescorner.hookshot.core.integration;

import eu.midnightdust.lib.config.MidnightConfig;

public class HookshotConfig extends MidnightConfig {
	@Entry public static boolean useClassicHookshotLogic = false;
	@Entry public static boolean unhookableBlacklist = true;
	@Entry public static boolean hookshotCancelsFallDamage = false;
	@Entry public static boolean quickModAffectsPullSpeed = false;
	@Entry public static double defaultMaxRange = 24D;
	@Entry public static double defaultMaxSpeed = 10D;
	@Entry public static double rangeMultiplier = 2D;
	@Entry public static double quickMultiplier = 1.5D;
	@Entry public static double durabilityMultiplier = 2D;
	@Entry public static int defaultMaxDurability = 512;
	@Entry public static String hookshotRepairItem = "minecraft:iron_ingot";
}
