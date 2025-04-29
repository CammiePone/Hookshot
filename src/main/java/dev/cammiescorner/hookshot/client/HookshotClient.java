package dev.cammiescorner.hookshot.client;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.client.entity.model.HookshotEntityModel;
import dev.cammiescorner.hookshot.client.entity.renderer.HookshotEntityRenderer;
import dev.cammiescorner.hookshot.component.HookOwnerComponent;
import dev.cammiescorner.hookshot.data.HookshotItemTags;
import dev.cammiescorner.hookshot.item.HookshotItem;
import dev.cammiescorner.hookshot.networking.ModMessages;
import dev.cammiescorner.hookshot.registry.HookshotComponents;
import dev.cammiescorner.hookshot.registry.HookshotEntities;
import dev.cammiescorner.hookshot.registry.HookshotItems;
import dev.cammiescorner.hookshot.registry.HookshotUpgrades;
import dev.cammiescorner.hookshot.util.ColorHelper;
import dev.cammiescorner.hookshot.util.Dyeable;
import dev.cammiescorner.hookshot.util.UpgradesHelper;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class HookshotClient implements ClientModInitializer {
    public static final ModelLayerLocation HOOKSHOT = new ModelLayerLocation(Hookshot.id("hookshot"), "hookshot");

    public static KeyMapping hookshotKey;

    HookshotItem hookshot;

    private static boolean wasUsingHookshot = false;

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

        hookshotKey = new KeyMapping(
                "key.hookshot.use",
                GLFW.GLFW_KEY_R,
                "key.categories.hookshot"
        );

        // Register keybind
        KeyBindingHelper.registerKeyBinding(hookshotKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (hookshotKey.isDown()) {
                if (!wasUsingHookshot) {
                    if (client.player != null && client.level != null) {
                        ItemStack hookshotStack = null;
                        if (hookshot == null) {
                            hookshotStack = getHookshotStack(client.player);

                            if (hookshotStack == null)
                                return;

                            hookshot = getHookshot(hookshotStack);
                        }

                        if (hookshot == null)
                            return;

                        if (!UpgradesHelper.hasUpgrade(hookshotStack, HookshotUpgrades.INVENTORY.get()))
                            return;

                        ClientPlayNetworking.send(ModMessages.CREATE_HOOKSHOT_PACKET_ID, PacketByteBufs.create());
                        wasUsingHookshot = true;
                    }
                }
            }
            else {
                if (wasUsingHookshot) {
                    if (hookshot == null) {
                        return;
                    }

                    ClientPlayNetworking.send(ModMessages.REMOVE_HOOKSHOT_PACKET_ID, PacketByteBufs.create());
                    hookshot = null;
                    wasUsingHookshot = false;
                }
            }
        });
    }

    private ItemStack getHookshotStack(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof HookshotItem) {
                if (stack == player.getOffhandItem())
                    return null;

                return stack;
            }
        }
        return null;
    }

    private HookshotItem getHookshot(ItemStack stack) {
        if (stack.getItem() instanceof HookshotItem hookshotItem) {
            return hookshotItem;
        }
        return null;
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
