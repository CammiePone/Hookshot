package dev.cammiescorner.hookshot.datagen.common;

import dev.cammiescorner.hookshot.common.data.HookshotItemTags;
import dev.cammiescorner.hookshot.common.registry.HookshotItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class HookshotItemTagsProvider extends FabricTagProvider.ItemTagProvider {

    public HookshotItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        getOrCreateTagBuilder(HookshotItemTags.HOOKSHOTS)
                .add(HookshotItems.WHITE_HOOKSHOT.get())
                .add(HookshotItems.ORANGE_HOOKSHOT.get())
                .add(HookshotItems.MAGENTA_HOOKSHOT.get())
                .add(HookshotItems.LIGHT_BLUE_HOOKSHOT.get())
                .add(HookshotItems.YELLOW_HOOKSHOT.get())
                .add(HookshotItems.LIME_HOOKSHOT.get())
                .add(HookshotItems.PINK_HOOKSHOT.get())
                .add(HookshotItems.GRAY_HOOKSHOT.get())
                .add(HookshotItems.LIGHT_GRAY_HOOKSHOT.get())
                .add(HookshotItems.CYAN_HOOKSHOT.get())
                .add(HookshotItems.PURPLE_HOOKSHOT.get())
                .add(HookshotItems.BLUE_HOOKSHOT.get())
                .add(HookshotItems.BROWN_HOOKSHOT.get())
                .add(HookshotItems.GREEN_HOOKSHOT.get())
                .add(HookshotItems.RED_HOOKSHOT.get())
                .add(HookshotItems.BLACK_HOOKSHOT.get());

        getOrCreateTagBuilder(ItemTags.TOOLS)
                .addTag(HookshotItemTags.HOOKSHOTS);

        getOrCreateTagBuilder(HookshotItemTags.HOOKSHOT_REPAIR_ITEMS)
                .forceAddTag(ConventionalItemTags.IRON_INGOTS);
    }
}
