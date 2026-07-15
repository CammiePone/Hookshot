package dev.cammiescorner.hookshot.common.registry;

import dev.cammiescorner.hookshot.Hookshot;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class HookshotDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Hookshot.MOD_ID);

    /** Replaces the 1.20.1 NBT list at {@code hookshot.Upgrades} with a proper data component. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ResourceLocation>>> UPGRADES = DATA_COMPONENTS.register("upgrades",
            () -> DataComponentType.<List<ResourceLocation>>builder()
                    .persistent(ResourceLocation.CODEC.listOf())
                    .networkSynchronized(ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()))
                    .build());
}
