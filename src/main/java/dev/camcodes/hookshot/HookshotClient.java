package dev.camcodes.hookshot;

import dev.camcodes.hookshot.client.entity.renderer.HookshotRenderer;
import dev.camcodes.hookshot.core.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class HookshotClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(ModEntities.HOOKSHOT_ENTITY, (dispatcher, context) ->
		{
			return new HookshotRenderer(dispatcher);
		});
	}
}
