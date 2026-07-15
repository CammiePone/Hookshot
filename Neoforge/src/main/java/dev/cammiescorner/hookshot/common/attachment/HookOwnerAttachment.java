package dev.cammiescorner.hookshot.common.attachment;

import dev.cammiescorner.hookshot.common.network.HookSyncPayload;
import dev.cammiescorner.hookshot.common.registry.HookshotAttachments;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Port of the Cardinal Components {@code PlayerHookOwnerComponent} to a NeoForge data attachment.
 * The {@code hasHook} flag is synced to tracking clients via {@link HookSyncPayload}.
 */
public class HookOwnerAttachment {

    @Nullable
    private final Player player;
    private boolean hasHook;
    private boolean isFloating;

    /**
     * whether the player had the NoGravity attribute before
     */
    private boolean hadNoGravity;

    public HookOwnerAttachment(@Nullable Player player) {
        this.player = player;
    }

    public static HookOwnerAttachment get(Player player) {
        return player.getData(HookshotAttachments.HOOK_OWNER);
    }

    public static Optional<HookOwnerAttachment> maybeGet(@Nullable Entity entity) {
        return entity instanceof Player player ? Optional.of(get(player)) : Optional.empty();
    }

    public boolean hasHook() {
        return hasHook;
    }

    public void setHasHook(boolean hasHook) {
        this.hasHook = hasHook;
        sync();
    }

    /** Client-side setter used by the sync packet handler; does not re-sync. */
    public void setHasHookClient(boolean hasHook) {
        this.hasHook = hasHook;
    }

    public void setTempNoGravity(boolean shouldFloat) {
        if (player == null) {
            return;
        }

        if (shouldFloat && !isFloating) {
            this.hadNoGravity = player.isNoGravity();
        }
        this.isFloating = shouldFloat;
        player.setNoGravity(shouldFloat || hadNoGravity);
    }

    @Nullable
    public Entity getEntity() {
        return player;
    }

    public void sync() {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(serverPlayer, new HookSyncPayload(serverPlayer.getId(), hasHook));
        }
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, HookOwnerAttachment> {

        @Override
        public HookOwnerAttachment read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
            HookOwnerAttachment attachment = new HookOwnerAttachment(holder instanceof Player player ? player : null);
            attachment.hasHook = tag.getBoolean("hook");
            return attachment;
        }

        @Override
        public CompoundTag write(HookOwnerAttachment attachment, HolderLookup.Provider provider) {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("hook", attachment.hasHook);
            return tag;
        }
    }
}
