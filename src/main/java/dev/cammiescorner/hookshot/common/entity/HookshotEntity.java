package dev.cammiescorner.hookshot.common.entity;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import dev.cammiescorner.hookshot.core.integration.HookshotConfig;
import dev.cammiescorner.hookshot.core.registry.ModDamageSource;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.registry.ModSoundEvents;
import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import dev.cammiescorner.hookshot.core.util.UpgradesHelper;
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
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HookshotEntity extends PersistentProjectileEntity {
	private static final TagKey<Block> UNHOOKABLE = TagKey.of(RegistryKeys.BLOCK, new Identifier(Hookshot.MOD_ID, "unhookable"));
	private static final TrackedData<Integer> HOOKED_ENTITY_ID = DataTracker.registerData(HookshotEntity.class, TrackedDataHandlerRegistry.INTEGER);

	private double maxRange = 0D;
	private double maxSpeed = 0D;
	private boolean isPulling = false;
	private Entity hookedEntity;
	private ItemStack stack;

	public HookshotEntity(EntityType<? extends PersistentProjectileEntity> type, PlayerEntity owner, World world) {
		super(type, owner, world);
		this.setNoGravity(true);
		this.setDamage(0);
	}

	public HookshotEntity(World world, double x, double y, double z) {
		super(ModEntities.HOOKSHOT_ENTITY, x, y, z, world);
		this.setNoGravity(true);
		this.setDamage(0);
	}

	public HookshotEntity(World world) {
		super(ModEntities.HOOKSHOT_ENTITY, world);
		this.setNoGravity(true);
		this.setDamage(0);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.getDataTracker().startTracking(HOOKED_ENTITY_ID, 0);
	}

	@Override
	public void tick() {
		super.tick();

		if(getOwner() instanceof PlayerEntity owner) {
			if(isPulling && age % 2 == 0)
				world.playSound(null, getOwner().getBlockPos(), ModSoundEvents.HOOKSHOT_REEL, SoundCategory.PLAYERS, 1F, 1F);

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

						double brakeZone = (6D * ((HookshotConfig.quickModAffectsPullSpeed ? maxSpeed : HookshotConfig.defaultMaxSpeed) / HookshotConfig.defaultMaxSpeed));
						double pullSpeed = (HookshotConfig.quickModAffectsPullSpeed ? maxSpeed : HookshotConfig.defaultMaxSpeed) / 6D;
						Vec3d distance = origin.getPos().subtract(target.getPos().add(0, target.getHeight() / 2, 0));
						Vec3d motion = distance.normalize().multiply(distance.length() < brakeZone && !UpgradesHelper.hasAutomaticUpgrade(stack) ? (pullSpeed * distance.length()) / brakeZone : pullSpeed);

						if(Math.abs(distance.y) < 0.1D)
							motion = new Vec3d(motion.x, 0, motion.z);
						if(new Vec3d(distance.x, 0, distance.z).length() < new Vec3d(target.getWidth() / 2, 0, target.getWidth() / 2).length() / 1.4)
							motion = new Vec3d(0, motion.y, 0);

						if(HookshotConfig.hookshotCancelsFallDamage)
							target.fallDistance = 0;

						target.setVelocity(motion);
						target.velocityModified = true;

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
		if(!world.isClient && getOwner() instanceof PlayerEntity owner) {
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

		if(!world.isClient && getOwner() instanceof PlayerEntity owner && hookedEntity == null) {
			owner.setNoGravity(true);

			if(HookshotConfig.unhookableBlacklist) {
				if(world.getBlockState(blockHitResult.getBlockPos()).isIn(UNHOOKABLE)) {
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
				if(!world.getBlockState(blockHitResult.getBlockPos()).isIn(UNHOOKABLE)) {
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
		if(!world.isClient && getOwner() instanceof PlayerEntity owner && entityHitResult.getEntity() != owner) {
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

		maxRange = tag.getDouble("maxRange");
		maxSpeed = tag.getDouble("maxSpeed");
		isPulling = tag.getBoolean("isPulling");
		stack = ItemStack.fromNbt(tag.getCompound("hookshotItem"));

		if(world.getEntityById(tag.getInt("owner")) instanceof PlayerEntity owner)
			setOwner(owner);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound tag) {
		super.writeCustomDataToNbt(tag);
		tag.putDouble("maxRange", maxRange);
		tag.putDouble("maxSpeed", maxSpeed);
		tag.putBoolean("isPulling", isPulling);
		tag.put("hookshotItem", stack.writeNbt(new NbtCompound()));

		if(getOwner() instanceof PlayerEntity owner)
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
