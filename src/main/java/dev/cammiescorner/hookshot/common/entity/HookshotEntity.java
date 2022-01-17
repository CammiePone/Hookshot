package dev.cammiescorner.hookshot.common.entity;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import dev.cammiescorner.hookshot.core.registry.ModDamageSource;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.registry.ModSoundEvents;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HookshotEntity extends PersistentProjectileEntity {
	private static final Tag<Block> UNHOOKABLE = TagRegistry.block(new Identifier(Hookshot.MOD_ID, "unhookable"));
	private static final TrackedData<Integer> HOOKED_ENTITY_ID = DataTracker.registerData(HookshotEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Float> FORCED_YAW = DataTracker.registerData(HookshotEntity.class, TrackedDataHandlerRegistry.FLOAT);

	private double maxRange = 0D;
	private double maxSpeed = 0D;
	private boolean isPulling = false;
	private PlayerEntity owner;
	private Entity hookedEntity;
	private ItemStack stack;

	public HookshotEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
		super(type, owner, world);
		if (!Hookshot.config.useHookshotGravity)
			this.setNoGravity(true);
		this.setDamage(0);
	}

	public HookshotEntity(World world, double x, double y, double z) {
		super(ModEntities.HOOKSHOT_ENTITY, x, y, z, world);
		if (!Hookshot.config.useHookshotGravity)
			this.setNoGravity(true);
		this.setDamage(0);
	}

	public HookshotEntity(World world) {
		super(ModEntities.HOOKSHOT_ENTITY, world);
		if (!Hookshot.config.useHookshotGravity)
			this.setNoGravity(true);
		this.setDamage(0);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.getDataTracker().startTracking(HOOKED_ENTITY_ID, 0);
		this.getDataTracker().startTracking(FORCED_YAW, 0f);
	}

	@Override
	public void tick() {
		super.tick();

		setYaw(dataTracker.get(FORCED_YAW));

		if(getOwner() instanceof PlayerEntity owner) {
			if(isPulling && age % 2 == 0)
				world.playSound(null, owner.getBlockPos(), ModSoundEvents.HOOKSHOT_REEL, SoundCategory.PLAYERS, 1F, 1F);

			if(!world.isClient) {
				if(owner.isDead() || !((PlayerProperties) owner).hasHook() || !((PlayerProperties) owner).hasHook() || owner.distanceTo(this) > maxRange || !(owner.getMainHandStack().getItem() instanceof HookshotItem || owner.getOffHandStack().getItem() instanceof HookshotItem) || !((PlayerProperties) owner).hasHook())
					kill();

				if(this.hookedEntity != null) {
					if(this.hookedEntity.isRemoved()) {
						this.hookedEntity = null;
						onRemoved();
					}
					else {
						if(UpgradesHelper.hasBleedUpgrade(stack) && age % 20 == 0)
							hookedEntity.damage(ModDamageSource.bleed(this, owner), 1);

						this.updatePosition(this.hookedEntity.getX(), this.hookedEntity.getBodyY(0.8D), this.hookedEntity.getZ());
					}
				}

				if(owner.getMainHandStack() == stack || owner.getOffHandStack() == stack) {
					if(isPulling) {
						Entity target = owner;
						Entity origin = this;

						if(owner.isSneaking() && hookedEntity != null) {
							target = hookedEntity;
							origin = owner;
						}

						Vec3d motion;
						if (Hookshot.config.useSwingingHookshot) {
							float currentDistance = target.distanceTo(origin);
							boolean belowHookshot = target.getY() < origin.getY();
							double ySpringStiffness, xzSpringStiffness;
							ySpringStiffness = xzSpringStiffness = 0.25D;

							// Slow down players once close to the hook
							if (currentDistance < 4) {
								ySpringStiffness = xzSpringStiffness = 0.09D;
							}
							// increase Y stiffness when descending at large speeds
							if (belowHookshot && target.getVelocity().getY() < -1.5) {
								ySpringStiffness = 1.5D;
							}
							if (belowHookshot && target.getVelocity().getY() < -2) {
								ySpringStiffness = 4.0D;
							}
							// Copied from lead code, same result as attaching a lead from hookshot to player
							double xDis = (origin.getX() - target.getX()) / (double)currentDistance;
							double yDis = (origin.getY() - target.getY()) / (double)currentDistance;
							double zDis = (origin.getZ() - target.getZ()) / (double)currentDistance;
							motion = target.getVelocity().add(Math.copySign(xDis * xDis * xzSpringStiffness, xDis), Math.copySign(yDis * yDis * ySpringStiffness, yDis), Math.copySign(zDis * zDis * xzSpringStiffness, zDis));
						}
						else {
							double brakeZone = (6D * ((Hookshot.config.quickModAffectsPullSpeed ? maxSpeed : Hookshot.config.defaultMaxSpeed) / Hookshot.config.defaultMaxSpeed));
							double pullSpeed = (Hookshot.config.quickModAffectsPullSpeed ? maxSpeed : Hookshot.config.defaultMaxSpeed) / 6D;
							Vec3d distance = origin.getPos().subtract(target.getPos().add(0, target.getHeight() / 2, 0));
							motion = distance.normalize().multiply(distance.length() < brakeZone && !UpgradesHelper.hasAutomaticUpgrade(stack) ? (pullSpeed * distance.length()) / brakeZone : pullSpeed);

							if(Math.abs(distance.y) < 0.1D)
								motion = new Vec3d(motion.x, 0, motion.z);
							if(new Vec3d(distance.x, 0, distance.z).length() < new Vec3d(target.getWidth() / 2, 0, target.getWidth() / 2).length() / 1.4)
								motion = new Vec3d(0, motion.y, 0);
						}

						target.setVelocity(motion);
						target.velocityModified = true;

						if(Hookshot.config.hookshotCancelsFallDamage)
							target.fallDistance = 0;

						if(UpgradesHelper.hasAutomaticUpgrade(stack) && owner.distanceTo(this) <= 3D)
							kill();

						if(stack.getMaxDamage() > 0 && age % 20 == 0)
							stack.damage(1, owner, (entity) -> entity.sendToolBreakStatus(owner.getActiveHand()));
					}
				}
				else {
					kill();
				}
			}
		}
		else {
			kill();
		}
	}

	@Override
	public void kill() {
		if(!world.isClient && owner != null) {
			((PlayerProperties) owner).setHasHook(false);
			owner.setNoGravity(false);
		}

		super.kill();
	}

	@Override
	public boolean shouldRender(double distance) {
		return true;
	}

	@Override
	protected float getDragInWater() {
		if(!world.isClient) {
			if(UpgradesHelper.hasAquaticUpgrade(stack))
				return 0.99F;
			else
				return super.getDragInWater();
		}
		else
			return super.getDragInWater();
	}

	@Override
	public boolean canUsePortals() {
		return false;
	}

	@Override
	protected ItemStack asItemStack() {
		return ItemStack.EMPTY;
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		isPulling = true;

		if(!world.isClient && owner != null && hookedEntity == null) {
			if (!Hookshot.config.useSwingingHookshot)
				owner.setNoGravity(true);

			if(Hookshot.config.unhookableBlacklist) {
				if(UNHOOKABLE.contains(world.getBlockState(blockHitResult.getBlockPos()).getBlock())) {
					((PlayerProperties) owner).setHasHook(false);
					isPulling = false;
					onRemoved();
				}
				else {
					if(UpgradesHelper.hasEndericUpgrade(stack)) {
						owner.requestTeleport(getX(), getY(), getZ());
						((PlayerProperties) owner).setHasHook(false);
						owner.fallDistance = 0.0F;
						isPulling = false;
						onRemoved();
					}
				}
			}
			else {
				if(!UNHOOKABLE.contains(world.getBlockState(blockHitResult.getBlockPos()).getBlock())) {
					((PlayerProperties) owner).setHasHook(false);
					isPulling = false;
					onRemoved();
				}
				else {
					if(UpgradesHelper.hasEndericUpgrade(stack)) {
						owner.requestTeleport(getX(), getY(), getZ());
						((PlayerProperties) owner).setHasHook(false);
						owner.fallDistance = 0.0F;
						isPulling = false;
						onRemoved();
					}
				}
			}
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		if(!world.isClient && owner != null && entityHitResult.getEntity() != owner) {
			if((entityHitResult.getEntity() instanceof LivingEntity || entityHitResult.getEntity() instanceof EnderDragonPart) && hookedEntity == null) {
				hookedEntity = entityHitResult.getEntity();
				dataTracker.set(HOOKED_ENTITY_ID, hookedEntity.getId() + 1);
				isPulling = true;
			}

			if(hookedEntity != null && UpgradesHelper.hasBleedUpgrade(stack))
				hookedEntity.damage(ModDamageSource.bleed(this, owner), 1);

			if(UpgradesHelper.hasEndericUpgrade(stack)) {
				owner.requestTeleport(getX(), getY(), getZ());
				owner.fallDistance = 0.0F;
				((PlayerProperties) owner).setHasHook(false);
				isPulling = false;
				onRemoved();
			}
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound tag) {
		super.readCustomDataFromNbt(tag);
		dataTracker.set(FORCED_YAW, tag.getFloat("ForcedYaw"));

		maxRange = tag.getDouble("maxRange");
		maxSpeed = tag.getDouble("maxSpeed");
		isPulling = tag.getBoolean("isPulling");
		stack = ItemStack.fromNbt(tag.getCompound("hookshotItem"));

		if(world.getEntityById(tag.getInt("owner")) instanceof PlayerEntity)
			owner = (PlayerEntity) world.getEntityById(tag.getInt("owner"));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound tag) {
		super.writeCustomDataToNbt(tag);
		tag.putFloat("ForcedYaw", dataTracker.get(FORCED_YAW));
		tag.putDouble("maxRange", maxRange);
		tag.putDouble("maxSpeed", maxSpeed);
		tag.putBoolean("isPulling", isPulling);
		tag.put("hookshotItem", stack.writeNbt(new NbtCompound()));

		if(owner != null)
			tag.putInt("owner", owner.getId());
	}

	public void setProperties(ItemStack stack, double maxRange, double maxVelocity, float pitch, float yaw, float roll, float modifierZ) {
		float f = 0.017453292F;
		float x = -MathHelper.sin(yaw * f) * MathHelper.cos(pitch * f);
		float y = -MathHelper.sin((pitch + roll) * f);
		float z = MathHelper.cos(yaw * f) * MathHelper.cos(pitch * f);
		this.setVelocity(x, y, z, modifierZ, 0);

		this.stack = stack;
		this.maxRange = maxRange;
		this.maxSpeed = maxVelocity;
	}
}
