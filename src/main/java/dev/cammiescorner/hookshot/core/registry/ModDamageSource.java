package dev.cammiescorner.hookshot.core.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.jetbrains.annotations.Nullable;

public class ModDamageSource {
	public static DamageSource bleed(PersistentProjectileEntity projectile, Entity attacker) {
		return new BleedingDamageSource("hookshotBleeding", projectile, attacker);
	}

	public static class BleedingDamageSource extends ProjectileDamageSource {
		public BleedingDamageSource(String name, Entity projectile, @Nullable Entity attacker) {
			super(name, projectile, attacker);
			setBypassesArmor();
			setUnblockable();
		}
	}
}
