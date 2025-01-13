package dev.cammiescorner.hookshot.client;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.client.entity.model.HookshotEntityModel;
import dev.cammiescorner.hookshot.client.entity.renderer.HookshotEntityRenderer;
import dev.cammiescorner.hookshot.component.HookOwnerComponent;
import dev.cammiescorner.hookshot.data.HookshotItemTags;
import dev.cammiescorner.hookshot.item.HookshotItem;
import dev.cammiescorner.hookshot.registry.HookshotComponents;
import dev.cammiescorner.hookshot.registry.HookshotEntities;
import dev.cammiescorner.hookshot.registry.HookshotItems;
import dev.cammiescorner.hookshot.util.ColorHelper;
import dev.cammiescorner.hookshot.util.Dyeable;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;

@Environment(EnvType.CLIENT)
public class HookshotClient implements ClientModInitializer {
    public static final ModelLayerLocation HOOKSHOT = new ModelLayerLocation(Hookshot.id("hookshot"), "hookshot");

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(HOOKSHOT, HookshotEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(HookshotEntities.HOOKSHOT.get(), HookshotEntityRenderer::new);

        HookshotItems.ITEMS.stream().map(RegistrySupplier::get).forEach(item -> {
            // TODO make items items use individual textures, get rid of color provider
            if (item instanceof Dyeable dyeable) {
                var color = ColorHelper.dyeToDecimal(dyeable.getColor());
                ColorProviderRegistry.ITEM.register((itemStack, tintIndex) -> tintIndex == 0 ? color : 0xFFFFFFFF, item);
            }

            registerHookModelOverride(item);
        });
    }

    private static void registerHookModelOverride(Item item) {
        ItemProperties.register(item, HookshotItem.USING_HOOK_MODEL_PROPERTY_ID, (stack, world, entity, seed) -> {
            if(entity != null) {
                HookOwnerComponent hook = HookshotComponents.HOOK_OWNER.getNullable(entity);
                if (hook != null && hook.hasHook()) {
                    var mainHandStack = entity.getMainHandItem();

                    // only remove hook from item that is held AND currently active
                    if(stack == mainHandStack || (!mainHandStack.is(HookshotItemTags.HOOKSHOTS) && stack == entity.getOffhandItem())) {
                        return 1.0F;
                    }
                }
            }

            return 0.0F;
        });
    }
}
