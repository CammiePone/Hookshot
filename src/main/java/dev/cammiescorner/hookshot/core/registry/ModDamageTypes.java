package dev.cammiescorner.hookshot.core.registry;

import dev.cammiescorner.hookshot.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public interface ModDamageTypes
{
    public static final RegistryKey<DamageType> HOOKBLEEDING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Hookshot.MOD_ID, "hookbleeding"));
 
    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }
}