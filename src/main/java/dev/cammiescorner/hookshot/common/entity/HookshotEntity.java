package dev.cammiescorner.hookshot.common.entity;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import dev.cammiescorner.hookshot.core.registry.ModDamageSource;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import dev.cammiescorner.hookshot.core.util.UpgradesHelper;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HookshotEntity extends PersistentProjectileEntity
{
	private static final Tag<Block> UNHOOKABLE = TagRegistry.block(new Identifier(Hookshot.MOD_ID, "unhookable"));
	private static final TrackedData<Integer> HOOKED_ENTITY_ID = DataTracker.registerData(HookshotEntity.class, TrackedDataHandlerRegistry.INTEGER);;

	private double maxRange = 0D;
	private double maxSpeed = 0D;
	private boolean isPulling = false;
	private PlayerEntity owner;
	private Entity hookedEntity;
	private ItemStack stack;

	public HookshotEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world)
	{
		super(type, owner, world);
		this.setNoGravity(true);
		this.setDamage(0);
	}

	public HookshotEntity(World world, double x, double y, double z)
	{
		super(ModEntities.HOOKSHOT_ENTITY, x, y, z, world);
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
	protected void initDataTracker()
	{
		super.initDataTracker();
		this.getDataTracker().startTracking(HOOKED_ENTITY_ID, 0);
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
				if(this.hookedEntity != null)
				{
					if(this.hookedEntity.removed)
					{
						this.hookedEntity = null;
						remove();
					}
					else
					{
						if(UpgradesHelper.hasBleedUpgrade(stack) && age % 20 == 0)
							hookedEntity.damage(ModDamageSource.BLEED, 1);

						this.updatePosition(this.hookedEntity.getX(), this.hookedEntity.getBodyY(0.8D), this.hookedEntity.getZ());
					}
				}

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
						Vec3d distance = getPos().subtract(owner.getPos().add(0, owner.getHeight() / 2, 0));
						Vec3d motion = distance.normalize().multiply(distance.length() < 3D && !UpgradesHelper.hasAutomaticUpgrade(stack) ? ((maxSpeed / 6) * distance.length()) / 4D : maxSpeed / 6);

						if(Math.abs(distance.y) < 0.1D)
							motion = new Vec3d(motion.x, 0, motion.z);
						if(new Vec3d(distance.x, 0, distance.z).length() < new Vec3d(owner.getWidth() / 2, 0, owner.getWidth() / 2).length() / 1.4)
							motion = new Vec3d(0, motion.y, 0);

						if(Hookshot.config.hookshotCancelsFallDamage)
							owner.fallDistance = 0;

						owner.setVelocity(motion);
						owner.velocityModified = true;

						if(UpgradesHelper.hasAutomaticUpgrade(stack) && owner.distanceTo(this) <= 3D)
						{
							kill();
							((PlayerProperties) owner).setHasHook(false);
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
	public void kill()
	{
		if(!world.isClient && owner != null)
			owner.setNoGravity(false);

		super.kill();
	}

	@Override
	public boolean shouldRender(double distance)
	{
		return true;
	}

	@Override
	protected float getDragInWater()
	{
		if(!world.isClient)
		{
			if(UpgradesHelper.hasAquaticUpgrade(stack)) return 0.99F;
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
	protected ItemStack asItemStack()
	{
		return ItemStack.EMPTY;
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult)
	{
		super.onBlockHit(blockHitResult);
		isPulling = true;

		if(!world.isClient && owner != null && hookedEntity == null)
		{
			owner.setNoGravity(true);

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
					if(UpgradesHelper.hasEndericUpgrade(stack))
					{
						owner.requestTeleport(getX(), getY(), getZ());
						((PlayerProperties) owner).setHasHook(false);
						owner.fallDistance = 0.0F;
						isPulling = false;
						remove();
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
					if(UpgradesHelper.hasEndericUpgrade(stack))
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

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		if(!world.isClient && owner != null && entityHitResult.getEntity() != owner)
		{
			if((entityHitResult.getEntity() instanceof LivingEntity || entityHitResult.getEntity() instanceof EnderDragonPart) && hookedEntity == null)
			{
				hookedEntity = entityHitResult.getEntity();
				dataTracker.set(HOOKED_ENTITY_ID, hookedEntity.getEntityId() + 1);
				isPulling = true;
			}

			if(UpgradesHelper.hasEndericUpgrade(stack))
			{
				owner.requestTeleport(getX(), getY(), getZ());
				owner.fallDistance = 0.0F;
				((PlayerProperties) owner).setHasHook(false);
				isPulling = false;
				remove();
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
		float f = 0.017453292F;
		float x = -MathHelper.sin(yaw * f) * MathHelper.cos(pitch * f);
		float y = -MathHelper.sin((pitch + roll) * f);
		float z = MathHelper.cos(yaw * f) * MathHelper.cos(pitch * f);
		this.setVelocity(x, y, z, modifierZ, modifierXYZ);

		this.stack = stack;
		this.maxRange = maxRange;
		this.maxSpeed = maxVelocity;
	}
}
