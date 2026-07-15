package dev.cammiescorner.hookshot.common.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HookshotEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Hookshot.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<HookshotEntity>> HOOKSHOT = ENTITY_TYPES.register("hookshot",
            () -> EntityType.Builder.<HookshotEntity>of(HookshotEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(5)
                    .updateInterval(3)
                    .build("hookshot"));
}
