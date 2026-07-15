package dev.cammiescorner.hookshot.common.event;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.attachment.HookOwnerAttachment;
import dev.cammiescorner.hookshot.common.network.HookSyncPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Hookshot.MOD_ID)
public class HookshotEvents {

    @SubscribeEvent
    static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player tracked && event.getEntity() instanceof ServerPlayer viewer) {
            PacketDistributor.sendToPlayer(viewer, new HookSyncPayload(tracked.getId(), HookOwnerAttachment.get(tracked).hasHook()));
        }
    }

    @SubscribeEvent
    static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        HookOwnerAttachment.get(event.getEntity()).sync();
    }

    @SubscribeEvent
    static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        HookOwnerAttachment.get(event.getEntity()).setHasHook(false);
    }
}
