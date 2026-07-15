package dev.cammiescorner.hookshot;

import com.mojang.logging.LogUtils;
import dev.cammiescorner.hookshot.common.network.HookSyncPayload;
import dev.cammiescorner.hookshot.common.registry.*;
import dev.cammiescorner.hookshot.common.upgrade.HookshotUpgrade;
import dev.cammiescorner.hookshot.common.util.UpgradesHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

@Mod(Hookshot.MOD_ID)
public class Hookshot {
    public static final String MOD_ID = "hookshot";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Hookshot(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.STARTUP, HookshotConfig.SPEC);

        HookshotDataComponents.DATA_COMPONENTS.register(modBus);
        HookshotAttachments.ATTACHMENT_TYPES.register(modBus);
        HookshotUpgrades.UPGRADES.register(modBus);
        HookshotEntities.ENTITY_TYPES.register(modBus);
        HookshotItems.ITEMS.register(modBus);
        HookshotRecipeSerializers.RECIPE_SERIALIZERS.register(modBus);
        HookshotSoundEvents.SOUND_EVENTS.register(modBus);

        modBus.addListener(this::registerPayloads);
        modBus.addListener(this::buildCreativeTabContents);
        // game tests are auto-discovered via @GameTestHolder on HookshotGameTests
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(HookSyncPayload.TYPE, HookSyncPayload.STREAM_CODEC, HookSyncPayload::handle);
    }

    private void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.TOOLS_AND_UTILITIES) {
            return;
        }

        event.accept(HookshotItems.WHITE_HOOKSHOT.get());

        for (HookshotUpgrade upgrade : HookshotUpgrades.REGISTRY) {
            var stack = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());
            UpgradesHelper.addUpgrade(stack, upgrade);
            event.accept(stack);
        }

        event.accept(HookshotItems.ORANGE_HOOKSHOT.get());
        event.accept(HookshotItems.MAGENTA_HOOKSHOT.get());
        event.accept(HookshotItems.LIGHT_BLUE_HOOKSHOT.get());
        event.accept(HookshotItems.YELLOW_HOOKSHOT.get());
        event.accept(HookshotItems.LIME_HOOKSHOT.get());
        event.accept(HookshotItems.PINK_HOOKSHOT.get());
        event.accept(HookshotItems.GRAY_HOOKSHOT.get());
        event.accept(HookshotItems.LIGHT_GRAY_HOOKSHOT.get());
        event.accept(HookshotItems.CYAN_HOOKSHOT.get());
        event.accept(HookshotItems.PURPLE_HOOKSHOT.get());
        event.accept(HookshotItems.BLUE_HOOKSHOT.get());
        event.accept(HookshotItems.BROWN_HOOKSHOT.get());
        event.accept(HookshotItems.GREEN_HOOKSHOT.get());
        event.accept(HookshotItems.RED_HOOKSHOT.get());
        event.accept(HookshotItems.BLACK_HOOKSHOT.get());
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
