package dev.cammiescorner.hookshot.core.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.registry.entry.RegistryEntry;

import org.jetbrains.annotations.Nullable;

public class ModDamageSource {
	public static DamageSource bleed(PersistentProjectileEntity projectile, Entity attacker) {
		return new BleedingDamageSource(projectile, attacker);
	}

	public static class BleedingDamageSource extends DamageSource {
		public BleedingDamageSource(Entity projectile, @Nullable Entity attacker) {
			super((RegistryEntry<DamageType>) projectile, attacker);
			//setBypassesArmor();
			//setUnblockable();
		}
	}
}
