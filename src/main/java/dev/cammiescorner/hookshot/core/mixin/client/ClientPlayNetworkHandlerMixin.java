package dev.cammiescorner.hookshot.core.mixin.client;

import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin
{
	@Shadow
	private ClientWorld world;

	@Inject(method = "onEntitySpawn", at = @At("TAIL"))
	private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo callbackInfo)
	{
		EntityType<?> type = packet.getEntityTypeId();
		double x = packet.getX();
		double y = packet.getY();
		double z = packet.getZ();
		PersistentProjectileEntity entity = null;

		if(type == ModEntities.HOOKSHOT_ENTITY)
			entity = new HookshotEntity(world, x, y, z);

		if(entity != null)
		{
			Entity owner = world.getEntityById(packet.getEntityData());

			if(owner != null)
				entity.setOwner(owner);

			int id = packet.getId();
			entity.updateTrackedPosition(x, y, z);
			entity.refreshPositionAfterTeleport(x, y, z);
			entity.pitch = (packet.getPitch() * 360F) / 256F;
			entity.yaw = (packet.getYaw() * 360F) / 256F;
			entity.setEntityId(id);
			entity.setUuid(packet.getUuid());
			world.addEntity(id, entity);
		}
	}
}
