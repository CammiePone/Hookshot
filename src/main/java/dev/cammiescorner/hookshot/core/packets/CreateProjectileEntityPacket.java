package dev.cammiescorner.hookshot.core.packets;

import dev.cammiescorner.hookshot.Hookshot;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class CreateProjectileEntityPacket
{
	public static final Identifier ID = new Identifier(Hookshot.MOD_ID, "create_non_living_entity");

	public static Packet<?> send(PersistentProjectileEntity entity)
	{
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeString(Registry.ENTITY_TYPE.getId(entity.getType()).toString());
		buf.writeUuid(entity.getUuid());
		buf.writeInt(entity.getEntityId());
		buf.writeDouble(entity.getX());
		buf.writeDouble(entity.getY());
		buf.writeDouble(entity.getZ());
		buf.writeByte(MathHelper.floor(entity.pitch * 256 / 360));
		buf.writeByte(MathHelper.floor(entity.yaw * 256 / 360));
		if(entity.getOwner() != null) buf.writeInt(entity.getOwner().getEntityId());

		return ServerSidePacketRegistry.INSTANCE.toPacket(ID, buf);
	}

	public static void handle(PacketContext context, PacketByteBuf buf)
	{
		EntityType<?> type = Registry.ENTITY_TYPE.get(new Identifier(buf.readString()));
		UUID uuid = buf.readUuid();
		int id = buf.readInt();
		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();
		float pitch = (buf.readByte() * 360) / 256f;
		float yaw = (buf.readByte() * 360) / 256f;
		int ownerId = buf.readInt();

		//noinspection Convert2Lambda
		context.getTaskQueue().submit(new Runnable()
		{
			@Override
			public void run()
			{
				ClientWorld world = MinecraftClient.getInstance().world;

				if(world != null)
				{
					Entity entity = type.create(world);

					if(entity instanceof PersistentProjectileEntity)
					{
						PersistentProjectileEntity projectile = (PersistentProjectileEntity) entity;

						projectile.updatePosition(x, y, z);
						projectile.updateTrackedPosition(x, y, z);
						projectile.pitch = pitch;
						projectile.yaw = yaw;
						projectile.prevPitch = pitch;
						projectile.prevYaw = yaw;
						projectile.setEntityId(id);
						projectile.setUuid(uuid);
						projectile.setOwner(world.getEntityById(ownerId));

						world.addEntity(id, projectile);
					}
				}
			}
		});
	}
}
