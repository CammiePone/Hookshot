package dev.cammiescorner.hookshot.common.upgrade;

import dev.cammiescorner.hookshot.common.registry.HookshotRegistries;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HookshotUpgrade {

    private String translationKey;

    public ResourceLocation getId() {
        return HookshotRegistries.HOOKSHOT_UPGRADES_REGISTRY.getKey(this);
    }

    public String getTranslationId() {
        if(translationKey == null) {
            translationKey = Util.makeDescriptionId("hookshot.upgrade", this.getId());
        }

        return translationKey;
    }

    public Component getName() {
        return Component.translatable(this.getTranslationId());
    }
}
