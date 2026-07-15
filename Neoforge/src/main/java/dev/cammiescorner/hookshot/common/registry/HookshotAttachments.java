package dev.cammiescorner.hookshot.common.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.attachment.HookOwnerAttachment;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class HookshotAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Hookshot.MOD_ID);

    /** Replaces the Cardinal Components {@code hookshot:hook_owner} player component. */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<HookOwnerAttachment>> HOOK_OWNER = ATTACHMENT_TYPES.register("hook_owner",
            () -> AttachmentType.builder(holder -> new HookOwnerAttachment(holder instanceof Player player ? player : null))
                    .serialize(new HookOwnerAttachment.Serializer())
                    .copyOnDeath()
                    .build());
}
