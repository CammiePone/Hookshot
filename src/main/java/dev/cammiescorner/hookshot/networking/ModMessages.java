package dev.cammiescorner.hookshot.networking;

import dev.cammiescorner.hookshot.item.HookshotItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class ModMessages {
    public static final ResourceLocation CREATE_HOOKSHOT_PACKET_ID = new ResourceLocation("hookshot", "create_hook");

    public static final ResourceLocation REMOVE_HOOKSHOT_PACKET_ID = new ResourceLocation("hookshot", "remove_hook");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(CREATE_HOOKSHOT_PACKET_ID, HookshotItem::recieveCreate);
        ServerPlayNetworking.registerGlobalReceiver(REMOVE_HOOKSHOT_PACKET_ID, HookshotItem::recieveRemove);
    }
}
