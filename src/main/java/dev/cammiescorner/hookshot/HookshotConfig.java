package dev.cammiescorner.hookshot;

import com.teamresourceful.resourcefulconfig.common.annotations.Config;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;

@Config(Hookshot.MOD_ID)
public final class HookshotConfig {
    @ConfigEntry(id = "use_classic_hookshot_logic", type = EntryType.BOOLEAN, translation = "config.hookshot.use_classic_hookshot_logic")
    public static boolean useClassicHookshotLogic = false;

    @ConfigEntry(id = "hookshot_cancels_fall_damage", type = EntryType.BOOLEAN, translation = "config.hookshot.hookshot_cancels_fall_damage")
    public static boolean hookshotCancelsFallDamage = false;

    @ConfigEntry(id = "hookshot_cooldown", type = EntryType.INTEGER, translation = "config.hookshot.hookshot_cooldown")
    public static int hookshotCooldown = 5;

    @ConfigEntry(id = "hookshot_affects_vehicle", type = EntryType.BOOLEAN, translation = "config.hookshot.hookshot_affects_vehicle")
    public static boolean hooksAffectVehicles = false;

    @ConfigEntry(id = "default_durability", type = EntryType.INTEGER, translation = "config.hookshot.default_durability")
    public static int defaultDurability = 512;

    @ConfigEntry(id = "default_range", type = EntryType.DOUBLE, translation = "config.hookshot.default_range")
    public static double defaultRange = 24D;

    @ConfigEntry(id = "default_speed", type = EntryType.DOUBLE, translation = "config.hookshot.default_speed")
    public static double defaultSpeed = 10D;

    @ConfigEntry(id = "durability_upgrade_multiplier", type = EntryType.DOUBLE, translation = "config.hookshot.durability_upgrade_multiplier")
    public static double durabilityUpgradeMultiplier = 2D;

    @ConfigEntry(id = "range_upgrade_multiplier", type = EntryType.DOUBLE, translation = "config.hookshot.range_upgrade_multiplier")
    public static double rangeUpgradeMultiplier = 2D;

    @ConfigEntry(id = "speed_upgrade_multiplier", type = EntryType.DOUBLE, translation = "config.hookshot.speed_upgrade_multiplier")
    public static double speedUpgradeMultiplier = 1.5D;
}
