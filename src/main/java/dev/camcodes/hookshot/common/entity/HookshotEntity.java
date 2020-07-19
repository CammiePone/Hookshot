package dev.camcodes.hookshot.common.entity;

import dev.camcodes.hookshot.common.item.HookshotItem;
import dev.camcodes.hookshot.core.packets.CreateNonLivingEntityPacket;
import dev.camcodes.hookshot.core.registry.ModEntities;
import dev.camcodes.hookshot.core.util.PlayerProperties;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class HookshotEntity extends PersistentProjectileEntity
{
	private double maxRange = 0D;
	private double maxVelocity = 0D;
	private boolean isPulling = false;
	private float forceYaw;
	private float forcePitch;
	private PlayerEntity owner;

	public HookshotEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world)
	{
		super(type, owner, world);
		this.forceYaw = owner.yaw;
		this.forcePitch = owner.pitch;
		this.setNoGravity(true);
		this.setDamage(0);
	}

	public HookshotEntity(World world)
	{
		super(ModEntities.HOOKSHOT_ENTITY, world);
		this.setNoGravity(true);
		this.setDamage(0);
	}

	@Override
	public void tick()
	{
		super.tick();

		this.prevYaw = forceYaw % 360;
		this.prevPitch = forcePitch % 360;
		this.setRotation(forceYaw, forcePitch);

		if(getOwner() instanceof PlayerEntity)
		{
			owner = (PlayerEntity) getOwner();

			if(!world.isClient)
			{
				if(owner != null)
				{
					if(owner.isDead() ||
							!((PlayerProperties) owner).hasHook() ||
							owner.distanceTo(this) > maxRange ||
							!(owner.getMainHandStack().getItem() instanceof HookshotItem ||
									owner.getOffHandStack().getItem() instanceof HookshotItem))
					{
						kill();
						((PlayerProperties) owner).setHasHook(false);
					}
				}
				else
				{
					kill();
				}

				if(isPulling && owner.distanceTo(this) > 1D)
				{
					owner.fallDistance = 0;
					owner.setVelocity(getPos().subtract(owner.getPos()).normalize().multiply(maxVelocity / 6));
					owner.velocityModified = true;
				}
			}
			else
			{
				System.out.println(((PlayerProperties) owner).hasHook());
			}
		}
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return CreateNonLivingEntityPacket.send(this);
	}

	@Override
	protected ItemStack asItemStack()
	{
		return ItemStack.EMPTY;
	}

	@Override
	protected void checkBlockCollision()
	{
		Box box = this.getBoundingBox();
		BlockPos positionA = new BlockPos(box.minX + 0.001D, box.minY + 0.001D, box.minZ + 0.001D);
		BlockPos positionB = new BlockPos(box.maxX - 0.001D, box.maxY - 0.001D, box.maxZ - 0.001D);
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		if(this.world.isRegionLoaded(positionA, positionB))
		{
			for(int x = positionA.getX(); x <= positionB.getX(); ++x)
			{
				for(int y = positionA.getY(); y <= positionB.getY(); ++y)
				{
					for(int z = positionA.getZ(); z <= positionB.getZ(); ++z)
					{
						mutable.set(x, y, z);
						BlockState blockState = this.world.getBlockState(mutable);

						try
						{
							blockState.onEntityCollision(this.world, mutable, this);
						}
						catch(Throwable oops)
						{
							CrashReport crashReport = CrashReport.create(oops, "Colliding entity with block");
							CrashReportSection crashReportSection = crashReport.addElement("Block being collided with");
							CrashReportSection.addBlockInfo(crashReportSection, mutable, blockState);
							throw new CrashException(crashReport);
						}
					}
				}
			}
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult)
	{
		super.onBlockHit(blockHitResult);
		isPulling = true;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		super.onEntityHit(entityHitResult);

		if(owner instanceof PlayerEntity)
		{
			((PlayerProperties) owner).setHasHook(false);
		}
	}

	public void setProperties(Entity user, double maxRange, double maxVelocity,
							  float pitch, float yaw, float roll, float modifierZ, float modifierXYZ)
	{
		super.setProperties(user, pitch, yaw, roll, modifierZ, modifierXYZ);
		this.forcePitch = pitch;
		this.forceYaw = yaw;
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
