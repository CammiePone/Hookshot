package dev.cammiescorner.hookshot.datagen.common;

import dev.cammiescorner.hookshot.data.HookshotDamageTypes;
import dev.upcraft.sparkweave.api.datagen.DynamicRegistryEntryProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class HookshotDamageTypeProvider extends DynamicRegistryEntryProvider {

    @Override
    protected void generate(RegistrySetBuilder builder) {
        builder.add(Registries.DAMAGE_TYPE, bootstapContext -> {
           bootstapContext.register(HookshotDamageTypes.BLEEDING, new DamageType("hookshot.bleeding", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.2f));
        });
    }
}
