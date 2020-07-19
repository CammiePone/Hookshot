package dev.camcodes.hookshot.client;

import dev.camcodes.hookshot.Hookshot;
import dev.camcodes.hookshot.client.entity.renderer.HookshotEntityRenderer;
import dev.camcodes.hookshot.core.packets.CreateNonLivingEntityPacket;
import dev.camcodes.hookshot.core.registry.ModEntities;
import dev.camcodes.hookshot.core.util.PlayerProperties;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HookshotClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(ModEntities.HOOKSHOT_ENTITY, (dispatcher, context) -> new HookshotEntityRenderer(dispatcher));
		ClientSidePacketRegistry.INSTANCE.register(CreateNonLivingEntityPacket.ID, (packetContext, packetByteBuf) -> CreateNonLivingEntityPacket.handle(packetContext, packetByteBuf));

		FabricModelPredicateProviderRegistry.register(new Identifier(Hookshot.MOD_ID, "has_hook"), new ModelPredicateProvider()
		{
			@Override
			public float call(ItemStack stack, ClientWorld world, LivingEntity entity)
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
			}
		});
	}
}
