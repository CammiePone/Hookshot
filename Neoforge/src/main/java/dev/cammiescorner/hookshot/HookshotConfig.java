package dev.cammiescorner.hookshot;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Registered as a STARTUP config so that {@link #defaultDurability()} is available
 * during item registration. Changing values requires a game restart.
 */
public final class HookshotConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue USE_CLASSIC_HOOKSHOT_LOGIC = BUILDER
            .translation("config.hookshot.use_classic_hookshot_logic")
            .define("use_classic_hookshot_logic", false);

    public static final ModConfigSpec.BooleanValue HOOKSHOT_CANCELS_FALL_DAMAGE = BUILDER
            .translation("config.hookshot.hookshot_cancels_fall_damage")
            .define("hookshot_cancels_fall_damage", false);

    public static final ModConfigSpec.IntValue HOOKSHOT_COOLDOWN = BUILDER
            .translation("config.hookshot.hookshot_cooldown")
            .defineInRange("hookshot_cooldown", 5, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue HOOKS_AFFECT_VEHICLES = BUILDER
            .translation("config.hookshot.hookshot_affects_vehicle")
            .define("hookshot_affects_vehicle", false);

    public static final ModConfigSpec.IntValue DEFAULT_DURABILITY = BUILDER
            .translation("config.hookshot.default_durability")
            .defineInRange("default_durability", 512, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue DEFAULT_RANGE = BUILDER
            .translation("config.hookshot.default_range")
            .defineInRange("default_range", 24D, 1D, 1024D);

    public static final ModConfigSpec.DoubleValue DEFAULT_SPEED = BUILDER
            .translation("config.hookshot.default_speed")
            .defineInRange("default_speed", 10D, 0.1D, 1024D);

    public static final ModConfigSpec.DoubleValue DURABILITY_UPGRADE_MULTIPLIER = BUILDER
            .translation("config.hookshot.durability_upgrade_multiplier")
            .defineInRange("durability_upgrade_multiplier", 2D, 1D, 1024D);

    public static final ModConfigSpec.DoubleValue RANGE_UPGRADE_MULTIPLIER = BUILDER
            .translation("config.hookshot.range_upgrade_multiplier")
            .defineInRange("range_upgrade_multiplier", 2D, 1D, 1024D);

    public static final ModConfigSpec.DoubleValue SPEED_UPGRADE_MULTIPLIER = BUILDER
            .translation("config.hookshot.speed_upgrade_multiplier")
            .defineInRange("speed_upgrade_multiplier", 1.5D, 1D, 1024D);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean useClassicHookshotLogic() {
        return USE_CLASSIC_HOOKSHOT_LOGIC.get();
    }

    public static boolean hookshotCancelsFallDamage() {
        return HOOKSHOT_CANCELS_FALL_DAMAGE.get();
    }

    public static int hookshotCooldown() {
        return HOOKSHOT_COOLDOWN.get();
    }

    public static boolean hooksAffectVehicles() {
        return HOOKS_AFFECT_VEHICLES.get();
    }

    public static int defaultDurability() {
        return DEFAULT_DURABILITY.get();
    }

    public static double defaultRange() {
        return DEFAULT_RANGE.get();
    }

    public static double defaultSpeed() {
        return DEFAULT_SPEED.get();
    }

    public static double durabilityUpgradeMultiplier() {
        return DURABILITY_UPGRADE_MULTIPLIER.get();
    }

    public static double rangeUpgradeMultiplier() {
        return RANGE_UPGRADE_MULTIPLIER.get();
    }

    public static double speedUpgradeMultiplier() {
        return SPEED_UPGRADE_MULTIPLIER.get();
    }

    private HookshotConfig() {
    }
}
