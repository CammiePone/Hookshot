package dev.cammiescorner.hookshot.datagen.client;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.registry.HookshotItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;
import java.util.function.Supplier;

public class HookshotModelProvider extends FabricModelProvider {

    private static final ModelTemplate HOOKSHOT_BASE = new ModelTemplate(Optional.of(Hookshot.id("item/hookshot")), Optional.empty());

    public HookshotModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        hookshot(generator, HookshotItems.WHITE_HOOKSHOT);
        hookshot(generator, HookshotItems.ORANGE_HOOKSHOT);
        hookshot(generator, HookshotItems.MAGENTA_HOOKSHOT);
        hookshot(generator, HookshotItems.LIGHT_BLUE_HOOKSHOT);
        hookshot(generator, HookshotItems.YELLOW_HOOKSHOT);
        hookshot(generator, HookshotItems.LIME_HOOKSHOT);
        hookshot(generator, HookshotItems.PINK_HOOKSHOT);
        hookshot(generator, HookshotItems.GRAY_HOOKSHOT);
        hookshot(generator, HookshotItems.LIGHT_GRAY_HOOKSHOT);
        hookshot(generator, HookshotItems.CYAN_HOOKSHOT);
        hookshot(generator, HookshotItems.PURPLE_HOOKSHOT);
        hookshot(generator, HookshotItems.BLUE_HOOKSHOT);
        hookshot(generator, HookshotItems.BROWN_HOOKSHOT);
        hookshot(generator, HookshotItems.GREEN_HOOKSHOT);
        hookshot(generator, HookshotItems.RED_HOOKSHOT);
        hookshot(generator, HookshotItems.BLACK_HOOKSHOT);
    }

    private void hookshot(ItemModelGenerators generator, Supplier<? extends ItemLike> item) {
        HOOKSHOT_BASE.create(ModelLocationUtils.getModelLocation(item.get().asItem()), new TextureMapping(), generator.output);
    }
}
