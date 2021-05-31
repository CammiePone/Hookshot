package dev.cammiescorner.hookshot.core.registry;

import net.minecraft.entity.damage.DamageSource;

public class ModDamageSource
{
	public static final DamageSource BLEED = new BleedingDamageSource("hookshotBleeding");

	public static class BleedingDamageSource extends DamageSource
	{
		protected BleedingDamageSource(String name)
		{
			super(name);
			setBypassesArmor();
			setUnblockable();
		}
	}
}
