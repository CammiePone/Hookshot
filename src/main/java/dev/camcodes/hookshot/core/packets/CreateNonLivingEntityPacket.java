package dev.camcodes.hookshot.core.packets;

import dev.camcodes.hookshot.Hookshot;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class CreateNonLivingEntityPacket
{
	public static final Identifier ID = new Identifier(Hookshot.MOD_ID, "create_non_living_entity");

	public static Packet<?> send(Entity entity)
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

					if(entity != null)
					{
						entity.updatePosition(x, y, z);
						entity.updateTrackedPosition(x, y, z);
						entity.pitch = pitch;
						entity.yaw = yaw;
						entity.prevPitch = pitch;
						entity.prevYaw = yaw;
						entity.setEntityId(id);
						entity.setUuid(uuid);
						world.addEntity(id, entity);
					}
				}
			}
		});
	}
}
