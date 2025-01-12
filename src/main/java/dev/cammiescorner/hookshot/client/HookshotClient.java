package dev.cammiescorner.hookshot.client;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.client.entity.model.HookshotEntityModel;
import dev.cammiescorner.hookshot.client.entity.renderer.HookshotEntityRenderer;
import dev.cammiescorner.hookshot.component.HookOwnerComponent;
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

@Environment(EnvType.CLIENT)
public class HookshotClient implements ClientModInitializer {
    public static final ModelLayerLocation HOOKSHOT = new ModelLayerLocation(Hookshot.id("hookshot"), "hookshot");

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(HOOKSHOT, HookshotEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(HookshotEntities.HOOKSHOT.get(), HookshotEntityRenderer::new);

        // TODO make items items use individual textures, get rid of color provider
        HookshotItems.ITEMS.stream().map(RegistrySupplier::get).forEach(item -> {
            if (item instanceof Dyeable dyeable) {
                var color = ColorHelper.dyeToDecimal(dyeable.getColor());
                ColorProviderRegistry.ITEM.register((itemStack, tintIndex) -> tintIndex == 0 ? color : 0xFFFFFFFF, item);
            }
        });

        ItemProperties.registerGeneric(Hookshot.id("has_hook"), (stack, world, entity, seed) -> {
            HookOwnerComponent hook = HookshotComponents.HOOK_OWNER.getNullable(entity);
            if (hook != null && !hook.hasHook()) {
                return 0.0F;
            }

            return 1.0F;
        });
    }
}
