package dev.cammiescorner.hookshot.client;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.client.entity.model.HookshotEntityModel;
import dev.cammiescorner.hookshot.client.entity.renderer.HookshotEntityRenderer;
import dev.cammiescorner.hookshot.common.attachment.HookOwnerAttachment;
import dev.cammiescorner.hookshot.common.data.HookshotItemTags;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import dev.cammiescorner.hookshot.common.registry.HookshotEntities;
import dev.cammiescorner.hookshot.common.registry.HookshotItems;
import dev.cammiescorner.hookshot.common.util.ColorHelper;
import dev.cammiescorner.hookshot.common.util.Dyeable;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Hookshot.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Hookshot.MOD_ID, value = Dist.CLIENT)
public class HookshotClient {
    public static final ModelLayerLocation HOOKSHOT = new ModelLayerLocation(Hookshot.id("hookshot"), "hookshot");

    public HookshotClient(ModContainer container) {
        // enables the "Config" button in the mods list (NeoForge built-in config UI)
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HOOKSHOT, HookshotEntityModel::getTexturedModelData);
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(HookshotEntities.HOOKSHOT.get(), HookshotEntityRenderer::new);
    }

    @SubscribeEvent
    static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (var holder : HookshotItems.ITEMS.getEntries()) {
            if (holder.get() instanceof Dyeable dyeable) {
                var color = ColorHelper.dyeToDecimal(dyeable.getColor());
                event.register((itemStack, tintIndex) -> tintIndex == 0 ? color : 0xFFFFFFFF, holder.get());
            }
        }
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (var holder : HookshotItems.ITEMS.getEntries()) {
                registerHookModelOverride(holder.get());
            }
        });
    }

    private static void registerHookModelOverride(Item item) {
        ItemProperties.register(item, HookshotItem.USING_HOOK_MODEL_PROPERTY_ID, (stack, world, entity, seed) -> {
            if (entity != null) {
                var hook = HookOwnerAttachment.maybeGet(entity).orElse(null);
                if (hook != null && hook.hasHook()) {
                    var mainHandStack = entity.getMainHandItem();

                    // only remove hook from item that is held AND currently active
                    if (stack == mainHandStack || (!mainHandStack.is(HookshotItemTags.HOOKSHOTS) && stack == entity.getOffhandItem())) {
                        return 1.0F;
                    }
                }
            }

            return 0.0F;
        });
    }
}
