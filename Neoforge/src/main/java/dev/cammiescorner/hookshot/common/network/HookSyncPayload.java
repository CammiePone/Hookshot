package dev.cammiescorner.hookshot.common.network;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.attachment.HookOwnerAttachment;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Server -&gt; client sync of a player's {@code hasHook} state. */
public record HookSyncPayload(int entityId, boolean hasHook) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<HookSyncPayload> TYPE = new CustomPacketPayload.Type<>(Hookshot.id("hook_sync"));

    public static final StreamCodec<ByteBuf, HookSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, HookSyncPayload::entityId,
            ByteBufCodecs.BOOL, HookSyncPayload::hasHook,
            HookSyncPayload::new
    );

    @Override
    public CustomPacketPayload.Type<HookSyncPayload> type() {
        return TYPE;
    }

    public static void handle(HookSyncPayload payload, IPayloadContext context) {
        if (context.player().level().getEntity(payload.entityId()) instanceof Player player) {
            HookOwnerAttachment.get(player).setHasHookClient(payload.hasHook());
        }
    }
}
