package dev.camcodes.hookshot.core.registry;

import dev.camcodes.hookshot.Hookshot;
import dev.camcodes.hookshot.common.entity.HookshotEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ModEntities
{
	//-----Entity Map-----//
	public static final LinkedHashMap<EntityType, Identifier> ENTITIES = new LinkedHashMap<>();

	//-----Entities-----//
	public static final EntityType<HookshotEntity> HOOKSHOT_ENTITY = create("hookshot", FabricEntityTypeBuilder
			.<HookshotEntity>create(SpawnGroup.MISC, (type, world) -> new HookshotEntity(world))
			.dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

	//-----Registry-----//
	public static void register()
	{
		ENTITIES.keySet().forEach(entityType -> Registry.register(Registry.ENTITY_TYPE, ENTITIES.get(entityType), entityType));
	}

	private static <T extends Entity> EntityType<T> create(String name, EntityType<T> type)
	{
		ENTITIES.put(type, new Identifier(Hookshot.MOD_ID, name));
		return type;
	}
}
