package dev.cammiescorner.hookshot.core.registry;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.Nullable;

public class ModDamageSource {
	public static DamageSource bleed(AbstractArrow projectile, Entity attacker) {
		return new BleedingDamageSource("hookshotBleeding", projectile, attacker);
	}

	public static class BleedingDamageSource extends IndirectEntityDamageSource {
		public BleedingDamageSource(String name, Entity projectile, @Nullable Entity attacker) {
			super(name, projectile, attacker);
			bypassArmor();
			bypassMagic();
		}
	}
}
