package dev.cammiescorner.hookshot.datagen.common;

import dev.cammiescorner.hookshot.common.data.HookshotDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class HookshotDamageTagsProvider extends FabricTagProvider<DamageType> {

    public HookshotDamageTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        getOrCreateTagBuilder(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)
                .add(HookshotDamageTypes.BLEEDING);

        getOrCreateTagBuilder(DamageTypeTags.IS_PROJECTILE)
                .add(HookshotDamageTypes.BLEEDING);

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
                .add(HookshotDamageTypes.BLEEDING);

        getOrCreateTagBuilder(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)
                .add(HookshotDamageTypes.BLEEDING);

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ENCHANTMENTS)
                .add(HookshotDamageTypes.BLEEDING);

        getOrCreateTagBuilder(DamageTypeTags.WITCH_RESISTANT_TO)
                .add(HookshotDamageTypes.BLEEDING);
    }
}
