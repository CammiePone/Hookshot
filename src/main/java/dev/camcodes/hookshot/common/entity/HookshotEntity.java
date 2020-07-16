package dev.camcodes.hookshot.common.entity;

import dev.camcodes.hookshot.core.mixin.IsJumpingAccessor;
import dev.camcodes.hookshot.core.registry.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HookshotEntity extends ThrownItemEntity
{
	private double maxRange = 0D;
	private double maxVelocity = 0D;
	private boolean isPulling = false;
	private PlayerEntity owner = (PlayerEntity) getOwner();

	public HookshotEntity(EntityType<? extends ThrownItemEntity> type, LivingEntity owner, World world)
	{
		super(type, owner, world);
		this.setNoGravity(true);
	}

	@Override
	protected Item getDefaultItem()
	{
		return null;
	}

	public HookshotEntity(World world)
	{
		super(ModEntities.HOOKSHOT_ENTITY, world);
		this.setNoGravity(true);
	}

	@Override
	public void tick()
	{
		super.tick();
		System.out.println(getPos());

		if(getOwner() instanceof PlayerEntity && !world.isClient)
		{
			if(isPulling && ((IsJumpingAccessor) owner).isJumping())
			{
				owner.move(MovementType.SELF, new Vec3d(
						getVelocity().getX() > 0 ? maxVelocity : -maxVelocity,
						getVelocity().getY() > 0 ? maxVelocity : -maxVelocity,
						getVelocity().getZ() > 0 ? maxVelocity : -maxVelocity)
						.normalize());

				owner.velocityModified = true;
			}
		}
	}

	@Override
	protected void onCollision(HitResult hitResult)
	{
		if(hitResult.getType() == HitResult.Type.BLOCK)
		{
			isPulling = true;
		}
	}

	public void setProperties(Entity user, double maxRange, double maxVelocity, float pitch, float yaw, float roll, float modifierZ, float modifierXYZ)
	{
		super.setProperties(user, pitch, yaw, roll, modifierZ, modifierXYZ);
		this.maxRange = maxRange;
		this.maxVelocity = maxVelocity;
	}

	public boolean isPulling()
	{
		return isPulling;
	}

	public void setPulling(boolean pulling)
	{
		isPulling = pulling;
	}
}
