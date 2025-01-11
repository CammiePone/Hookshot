package dev.cammiescorner.hookshot.client;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.client.entity.model.HookshotEntityModel;
import dev.cammiescorner.hookshot.client.entity.renderer.HookshotEntityRenderer;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.util.ColourHelper;
import dev.cammiescorner.hookshot.core.util.Dyeable;
import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import static dev.cammiescorner.hookshot.core.registry.ModItems.*;

@Environment(EnvType.CLIENT)
public class HookshotClient implements ClientModInitializer {
	public static final ModelLayerLocation HOOKSHOT = new ModelLayerLocation(new ResourceLocation(Hookshot.MOD_ID, "hookshot"), "hookshot");

	@Override
	public void onInitializeClient() {
		// Entity Renderer Registry
		EntityRendererRegistry.register(ModEntities.HOOKSHOT_ENTITY, HookshotEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(HOOKSHOT, HookshotEntityModel::getTexturedModelData);

		// Colour Registry
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> ColourHelper.dyeableToDecimal((Dyeable) stack.getItem()), WHITE_HOOKSHOT, ORANGE_HOOKSHOT, MAGENTA_HOOKSHOT, LIGHT_BLUE_HOOKSHOT, YELLOW_HOOKSHOT, LIME_HOOKSHOT, PINK_HOOKSHOT, GREY_HOOKSHOT, LIGHT_GREY_HOOKSHOT, CYAN_HOOKSHOT, PURPLE_HOOKSHOT, BLUE_HOOKSHOT, BROWN_HOOKSHOT, GREEN_HOOKSHOT, RED_HOOKSHOT, BLACK_HOOKSHOT);

		// Predicate Registry
		ItemProperties.registerGeneric(new ResourceLocation(Hookshot.MOD_ID, "has_hook"), (stack, world, entity, seed) -> {
			if(entity instanceof Player) {
				if(((PlayerProperties) entity).hasHook())
					return 1;
				else
					return 0;
			}

			return 0;
		});
	}
}
