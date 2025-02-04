package dev.cammiescorner.hookshot.datagen;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.datagen.client.HookshotEnglishLanguageProvider;
import dev.cammiescorner.hookshot.datagen.client.HookshotModelProvider;
import dev.cammiescorner.hookshot.datagen.common.HookshotDamageTagsProvider;
import dev.cammiescorner.hookshot.datagen.common.HookshotDamageTypeProvider;
import dev.cammiescorner.hookshot.datagen.common.HookshotItemTagsProvider;
import dev.cammiescorner.hookshot.datagen.common.HookshotRecipeProvider;
import dev.upcraft.sparkweave.api.datagen.DynamicRegistryEntryProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;

public class HookshotDatagenerator implements DataGeneratorEntrypoint {

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
       DynamicRegistryEntryProvider.builder(Hookshot.MOD_ID)
                .add(HookshotDamageTypeProvider::new)
                .build(registryBuilder);
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var pack = generator.createPack();
        pack.addProvider((output, registriesFuture) -> DynamicRegistryEntryProvider.getGenerator(Hookshot.MOD_ID, output, registriesFuture));

        pack.addProvider(HookshotItemTagsProvider::new);
        pack.addProvider(HookshotDamageTagsProvider::new);
        pack.addProvider(HookshotRecipeProvider::new);

        pack.addProvider(HookshotEnglishLanguageProvider::new);
        pack.addProvider(HookshotModelProvider::new);
    }
}
