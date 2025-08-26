package dev.cammiescorner.hookshot.common.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class HookshotEntities {

	public static final RegistryHandler<EntityType<?>> ENTITY_TYPES = RegistryHandler.create(Registries.ENTITY_TYPE, Hookshot.MOD_ID);

	public static final RegistrySupplier<EntityType<HookshotEntity>> HOOKSHOT = ENTITY_TYPES.register("hookshot", () -> FabricEntityTypeBuilder.<HookshotEntity>create(MobCategory.MISC, HookshotEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());
}
