package dev.cammiescorner.hookshot.core.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import java.util.LinkedHashMap;

public class ModEntities {
	//-----Entity Map-----//
	public static final LinkedHashMap<EntityType, ResourceLocation> ENTITIES = new LinkedHashMap<>();

	//-----Entities-----//
	public static final EntityType<HookshotEntity> HOOKSHOT_ENTITY = create("hookshot", FabricEntityTypeBuilder.<HookshotEntity>create(MobCategory.MISC, (type, world) -> new HookshotEntity(world)).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

	//-----Registry-----//
	public static void register() {
		ENTITIES.keySet().forEach(entityType -> Registry.register(BuiltInRegistries.ENTITY_TYPE, ENTITIES.get(entityType), entityType));
	}

	private static <T extends Entity> EntityType<T> create(String name, EntityType<T> type) {
		ENTITIES.put(type, new ResourceLocation(Hookshot.MOD_ID, name));
		return type;
	}
}
