package dev.camcodes.hookshot.client;

import dev.camcodes.hookshot.client.entity.renderer.HookshotEntityRenderer;
import dev.camcodes.hookshot.core.packets.CreateNonLivingEntityPacket;
import dev.camcodes.hookshot.core.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

@Environment(EnvType.CLIENT)
public class HookshotClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(ModEntities.HOOKSHOT_ENTITY, (dispatcher, context) -> new HookshotEntityRenderer(dispatcher));
		ClientSidePacketRegistry.INSTANCE.register(CreateNonLivingEntityPacket.ID, (packetContext, packetByteBuf) -> CreateNonLivingEntityPacket.handle(packetContext, packetByteBuf));
	}
}
