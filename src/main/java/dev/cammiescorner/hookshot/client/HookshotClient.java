package dev.cammiescorner.hookshot.client;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.client.entity.renderer.HookshotEntityRenderer;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import dev.cammiescorner.hookshot.core.mixin.client.DyeColorAccessor;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import static dev.cammiescorner.hookshot.core.registry.ModItems.*;

@Environment(EnvType.CLIENT)
public class HookshotClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		// Entity Renderer Registry
		EntityRendererRegistry.INSTANCE.register(ModEntities.HOOKSHOT_ENTITY, (dispatcher, context) -> new HookshotEntityRenderer(dispatcher));

		// Colour Registry
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> ((DyeColorAccessor) (Object) ((HookshotItem) stack.getItem()).getColour()).getColour(),
				WHITE_HOOKSHOT, ORANGE_HOOKSHOT, MAGENTA_HOOKSHOT, LIGHT_BLUE_HOOKSHOT, YELLOW_HOOKSHOT,
				LIME_HOOKSHOT, PINK_HOOKSHOT, GREY_HOOKSHOT, LIGHT_GREY_HOOKSHOT, CYAN_HOOKSHOT,
				PURPLE_HOOKSHOT, BLUE_HOOKSHOT, BROWN_HOOKSHOT, GREEN_HOOKSHOT, RED_HOOKSHOT, BLACK_HOOKSHOT);

		// Predicate Registry
		FabricModelPredicateProviderRegistry.register(new Identifier(Hookshot.MOD_ID, "has_hook"), (stack, world, entity) ->
		{
			if(entity instanceof PlayerEntity)
			{
				if(((PlayerProperties) entity).hasHook())
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}

			return 0;
		});
	}
}
