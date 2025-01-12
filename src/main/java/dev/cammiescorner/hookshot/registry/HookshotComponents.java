package dev.cammiescorner.hookshot.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.component.HookOwnerComponent;
import dev.cammiescorner.hookshot.component.PlayerHookOwnerComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.world.entity.player.Player;

public class HookshotComponents implements EntityComponentInitializer {

    public static final ComponentKey<HookOwnerComponent> HOOK_OWNER = ComponentRegistry.getOrCreate(Hookshot.id("hook_owner"), HookOwnerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(Player.class, HOOK_OWNER)
                .impl(PlayerHookOwnerComponent.class)
                .respawnStrategy(RespawnCopyStrategy.INVENTORY)
                .end(PlayerHookOwnerComponent::new);
    }
}
