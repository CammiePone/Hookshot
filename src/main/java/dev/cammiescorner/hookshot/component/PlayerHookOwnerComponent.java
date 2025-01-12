package dev.cammiescorner.hookshot.component;

import dev.cammiescorner.hookshot.registry.HookshotComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@SuppressWarnings("UnstableApiUsage")
public class PlayerHookOwnerComponent implements HookOwnerComponent, PlayerComponent<PlayerHookOwnerComponent>, AutoSyncedComponent {

    private final Player player;
    private boolean hasHook;
    private boolean isFloating;

    /**
     * whether the player had the NoGravity attribute before
     */
    private boolean hadNoGravity;

    public PlayerHookOwnerComponent(Player player) {
        this.player = player;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        hasHook = tag.getBoolean("hook");
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean("hook", hasHook);
    }

    @Override
    public boolean hasHook() {
        return hasHook;
    }

    @Override
    public void setHasHook(boolean hasHook) {
        this.hasHook = hasHook;
    }

    @Override
    public void setTempNoGravity(boolean shouldFloat) {
        if(shouldFloat && !isFloating) {
            this.hadNoGravity = player.isNoGravity();
        }
        this.isFloating = shouldFloat;
        player.setNoGravity(shouldFloat || hadNoGravity);
    }

    @Override
    public void sync() {
        player.syncComponent(HookshotComponents.HOOK_OWNER);
    }

    @Override
    public Entity getEntity() {
        return player;
    }
}
