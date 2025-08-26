package dev.cammiescorner.hookshot.datagen.common;

import dev.cammiescorner.hookshot.common.data.HookshotBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class HookshotBlockTagsProvider extends FabricTagProvider.BlockTagProvider {

    public HookshotBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(HookshotBlockTags.UNHOOKABLE);
    }
}
