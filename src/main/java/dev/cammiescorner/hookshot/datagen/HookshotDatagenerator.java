package dev.cammiescorner.hookshot.datagen;

import dev.cammiescorner.hookshot.datagen.client.HookshotEnglishLanguageProvider;
import dev.cammiescorner.hookshot.datagen.client.HookshotModelProvider;
import dev.cammiescorner.hookshot.datagen.common.HookshotDamageTagsProvider;
import dev.cammiescorner.hookshot.datagen.common.HookshotDamageTypeProvider;
import dev.cammiescorner.hookshot.datagen.common.HookshotItemTagsProvider;
import dev.cammiescorner.hookshot.datagen.common.HookshotRecipeProvider;
import dev.cammiescorner.hookshot.util.datagen.DynamicRegistryEntryProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;

public class HookshotDatagenerator implements DataGeneratorEntrypoint {

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        DynamicRegistryEntryProvider.builder()
                .add(HookshotDamageTypeProvider::new)
                .build(registryBuilder);
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var pack = generator.createPack();
        pack.addProvider(DynamicRegistryEntryProvider::getGenerator);

        pack.addProvider(HookshotItemTagsProvider::new);
        pack.addProvider(HookshotDamageTagsProvider::new);
        pack.addProvider(HookshotRecipeProvider::new);

        pack.addProvider(HookshotEnglishLanguageProvider::new);
        pack.addProvider(HookshotModelProvider::new);
    }
}
