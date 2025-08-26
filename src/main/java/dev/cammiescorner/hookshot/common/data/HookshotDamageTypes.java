package dev.cammiescorner.hookshot.common.data;

import dev.cammiescorner.hookshot.Hookshot;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;

public class HookshotDamageTypes {

	public static final ResourceKey<DamageType> BLEEDING = ResourceKey.create(Registries.DAMAGE_TYPE, Hookshot.id("bleeding"));

	public static DamageSource getBleedingDamage(Projectile direct, @Nullable Entity trueSource) {
		return direct.damageSources().source(BLEEDING, direct, trueSource);
	}
}
