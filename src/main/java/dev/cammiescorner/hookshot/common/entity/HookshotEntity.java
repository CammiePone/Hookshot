package dev.cammiescorner.hookshot.common.entity;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import dev.cammiescorner.hookshot.core.packets.CreateProjectileEntityPacket;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class HookshotEntity extends PersistentProjectileEntity
{
	private static final Tag<Block> UNHOOKABLE = TagRegistry.block(new Identifier(Hookshot.MOD_ID, "unhookable"));

	private double maxRange = 0D;
	private double maxSpeed = 0D;
	private boolean isPulling = false;
	private PlayerEntity owner;
	private ItemStack stack;

	public HookshotEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world)
	{
		super(type, owner, world);
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
							owner.getOffHandStack().getItem() instanceof HookshotItem) ||
							!((PlayerProperties) owner).hasHook())
					{
						kill();
						((PlayerProperties) owner).setHasHook(false);
					}
				}
				else
				{
					kill();
				}

				if(owner.getMainHandStack() == stack || owner.getOffHandStack() == stack)
				{
					if(isPulling)
					{
						if(owner.distanceTo(this) > 1D)
						{
							owner.fallDistance = 0;
							owner.setVelocity(getPos().subtract(owner.getPos()).normalize().multiply(maxSpeed / 6));
							owner.velocityModified = true;
						}

						if(stack.hasTag())
						{
							if(owner.distanceTo(this) <= 3D && stack.getTag().getBoolean("hasAuto"))
							{
								kill();
								((PlayerProperties) owner).setHasHook(false);
							}
						}
					}
				}
				else
				{
					kill();
					((PlayerProperties) owner).setHasHook(false);
				}
			}
		}
	}

	@Override
	public boolean shouldRender(double distance)
	{
		return true;
	}

	@Override
	protected float getDragInWater()
	{
		if(!world.isClient && stack.hasTag())
		{
			if(stack.getTag().getBoolean("hasAqua")) return 0.99F;
			else return super.getDragInWater();
		}
		else return super.getDragInWater();
	}

	@Override
	public boolean canUsePortals()
	{
		return false;
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return CreateProjectileEntityPacket.send(this);
	}

	@Override
	protected ItemStack asItemStack()
	{
		return ItemStack.EMPTY;
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult)
	{
		super.onBlockHit(blockHitResult);
		isPulling = true;

		if(!world.isClient && owner != null)
		{
			if(Hookshot.config.unhookableBlacklist)
			{
				if(UNHOOKABLE.contains(world.getBlockState(blockHitResult.getBlockPos()).getBlock()))
				{
					((PlayerProperties) owner).setHasHook(false);
					isPulling = false;
					remove();
				}
				else
				{
					if(stack.hasTag())
					{
						if(stack.getTag().getBoolean("hasEnder"))
						{
							owner.requestTeleport(getX(), getY(), getZ());
							((PlayerProperties) owner).setHasHook(false);
							owner.fallDistance = 0.0F;
							isPulling = false;
							remove();
						}
					}
				}
			}
			else
			{
				if(!UNHOOKABLE.contains(world.getBlockState(blockHitResult.getBlockPos()).getBlock()))
				{
					((PlayerProperties) owner).setHasHook(false);
					isPulling = false;
					remove();
				}
				else
				{
					if(stack.hasTag())
					{
						if(stack.getTag().getBoolean("hasEnder"))
						{
							owner.requestTeleport(getX(), getY(), getZ());
							((PlayerProperties) owner).setHasHook(false);
							owner.fallDistance = 0.0F;
							isPulling = false;
							remove();
						}
					}
				}
			}
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		super.onEntityHit(entityHitResult);

		if(!world.isClient && owner != null)
		{
			((PlayerProperties) owner).setHasHook(false);

			if(stack.hasTag())
			{
				if(stack.getTag().getBoolean("hasEnder"))
				{
					owner.requestTeleport(getX(), getY(), getZ());
					((PlayerProperties) owner).setHasHook(false);
					owner.fallDistance = 0.0F;
					isPulling = false;
					remove();
				}
			}
		}
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag)
	{
		super.readCustomDataFromTag(tag);
		maxRange = tag.getDouble("maxRange");
		maxSpeed = tag.getDouble("maxSpeed");
		isPulling = tag.getBoolean("isPulling");
		stack = ItemStack.fromTag(tag.getCompound("hookshotItem"));

		if(world.getEntityById(tag.getInt("owner")) instanceof PlayerEntity)
			owner = (PlayerEntity) world.getEntityById(tag.getInt("owner"));
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag)
	{
		super.writeCustomDataToTag(tag);
		tag.putDouble("maxRange", maxRange);
		tag.putDouble("maxSpeed", maxSpeed);
		tag.putBoolean("isPulling", isPulling);
		tag.put("hookshotItem", stack.toTag(new CompoundTag()));
		tag.putInt("owner", owner.getEntityId());
	}

	public void setProperties(ItemStack stack, double maxRange, double maxVelocity,
							  float pitch, float yaw, float roll, float modifierZ, float modifierXYZ)
	{
		float f = 0.0175F;
		float x = -MathHelper.sin(yaw * f) * MathHelper.cos(pitch * f);
		float y = -MathHelper.sin((pitch + roll) * f);
		float z = MathHelper.cos(yaw * f) * MathHelper.cos(pitch * f);
		this.setVelocity(x, y, z, modifierZ, modifierXYZ);

		this.stack = stack;
		this.maxRange = maxRange;
		this.maxSpeed = maxVelocity;
	}
}
