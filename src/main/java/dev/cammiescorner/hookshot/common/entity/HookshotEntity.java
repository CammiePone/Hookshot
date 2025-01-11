package dev.cammiescorner.hookshot.common.entity;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import dev.cammiescorner.hookshot.core.integration.HookshotConfig;
import dev.cammiescorner.hookshot.core.registry.ModDamageSource;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.registry.ModSoundEvents;
import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import dev.cammiescorner.hookshot.core.util.UpgradesHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class HookshotEntity extends AbstractArrow {
	private static final TagKey<Block> UNHOOKABLE = TagKey.create(Registries.BLOCK, new ResourceLocation(Hookshot.MOD_ID, "unhookable"));
	private static final EntityDataAccessor<Integer> HOOKED_ENTITY_ID = SynchedEntityData.defineId(HookshotEntity.class, EntityDataSerializers.INT);

	private double maxRange = 0D;
	private double maxSpeed = 0D;
	private boolean isPulling = false;
	private Entity hookedEntity;
	private ItemStack stack;

	public HookshotEntity(EntityType<? extends AbstractArrow> type, Player owner, Level world) {
		super(type, owner, world);
		this.setNoGravity(true);
		this.setBaseDamage(0);
	}

	public HookshotEntity(Level world, double x, double y, double z) {
		super(ModEntities.HOOKSHOT_ENTITY, x, y, z, world);
		this.setNoGravity(true);
		this.setBaseDamage(0);
	}

	public HookshotEntity(Level world) {
		super(ModEntities.HOOKSHOT_ENTITY, world);
		this.setNoGravity(true);
		this.setBaseDamage(0);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(HOOKED_ENTITY_ID, 0);
	}

	@Override
	public void tick() {
		super.tick();

		if(getOwner() instanceof Player owner) {
			if(isPulling && tickCount % 2 == 0)
				level.playSound(null, getOwner().blockPosition(), ModSoundEvents.HOOKSHOT_REEL, SoundSource.PLAYERS, 1F, 1F);

			if(!level.isClientSide) {
				if(owner.isDeadOrDying() || !((PlayerProperties) owner).hasHook() || !((PlayerProperties) owner).hasHook() || owner.distanceTo(this) > maxRange || !(owner.getMainHandItem().getItem() instanceof HookshotItem || owner.getOffhandItem().getItem() instanceof HookshotItem) || !((PlayerProperties) owner).hasHook())
					kill();

				if(this.hookedEntity != null) {
					if(this.hookedEntity.isRemoved()) {
						this.hookedEntity = null;
						onClientRemoval();
					}
					else {
						if(UpgradesHelper.hasBleedUpgrade(stack) && tickCount % 20 == 0)
							hookedEntity.hurt(ModDamageSource.bleed(this, owner), 1);

						this.absMoveTo(this.hookedEntity.getX(), this.hookedEntity.getY(0.8D), this.hookedEntity.getZ());
					}
				}

				if(owner.getMainHandItem() == stack || owner.getOffhandItem() == stack) {
					if(isPulling) {
						Entity target = owner;
						Entity origin = this;

						if(owner.isShiftKeyDown() && hookedEntity != null) {
							target = hookedEntity;
							origin = owner;
						}

						double brakeZone = (6D * ((HookshotConfig.quickModAffectsPullSpeed ? maxSpeed : HookshotConfig.defaultMaxSpeed) / HookshotConfig.defaultMaxSpeed));
						double pullSpeed = (HookshotConfig.quickModAffectsPullSpeed ? maxSpeed : HookshotConfig.defaultMaxSpeed) / 6D;
						Vec3 distance = origin.position().subtract(target.position().add(0, target.getBbHeight() / 2, 0));
						Vec3 motion = distance.normalize().scale(distance.length() < brakeZone && !UpgradesHelper.hasAutomaticUpgrade(stack) ? (pullSpeed * distance.length()) / brakeZone : pullSpeed);

						if(Math.abs(distance.y) < 0.1D)
							motion = new Vec3(motion.x, 0, motion.z);
						if(new Vec3(distance.x, 0, distance.z).length() < new Vec3(target.getBbWidth() / 2, 0, target.getBbWidth() / 2).length() / 1.4)
							motion = new Vec3(0, motion.y, 0);

						if(HookshotConfig.hookshotCancelsFallDamage)
							target.fallDistance = 0;

						target.setDeltaMovement(motion);
						target.hurtMarked = true;

						if(UpgradesHelper.hasAutomaticUpgrade(stack) && owner.distanceTo(this) <= 3D)
							kill();

						if(stack.getMaxDamage() > 0 && tickCount % 20 == 0)
							stack.hurtAndBreak(1, owner, (entity) -> entity.broadcastBreakEvent(owner.getUsedItemHand()));
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
		if(!level.isClientSide && getOwner() instanceof Player owner) {
			((PlayerProperties) owner).setHasHook(false);
			owner.setNoGravity(false);
		}

		super.kill();
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		return true;
	}

	@Override
	protected float getWaterInertia() {
		if(!level.isClientSide) {
			if(UpgradesHelper.hasAquaticUpgrade(stack))
				return 0.99F;
			else
				return super.getWaterInertia();
		}
		else
			return super.getWaterInertia();
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	protected ItemStack getPickupItem() {
		return ItemStack.EMPTY;
	}

	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		isPulling = true;

		if(!level.isClientSide && getOwner() instanceof Player owner && hookedEntity == null) {
			owner.setNoGravity(true);

			if(HookshotConfig.unhookableBlacklist) {
				if(level.getBlockState(blockHitResult.getBlockPos()).is(UNHOOKABLE)) {
					((PlayerProperties) owner).setHasHook(false);
					isPulling = false;
					onClientRemoval();
				}
				else {
					if(UpgradesHelper.hasEndericUpgrade(stack)) {
						owner.teleportTo(getX(), getY(), getZ());
						((PlayerProperties) owner).setHasHook(false);
						owner.fallDistance = 0.0F;
						isPulling = false;
						onClientRemoval();
					}
				}
			}
			else {
				if(!level.getBlockState(blockHitResult.getBlockPos()).is(UNHOOKABLE)) {
					((PlayerProperties) owner).setHasHook(false);
					isPulling = false;
					onClientRemoval();
				}
				else {
					if(UpgradesHelper.hasEndericUpgrade(stack)) {
						owner.teleportTo(getX(), getY(), getZ());
						((PlayerProperties) owner).setHasHook(false);
						owner.fallDistance = 0.0F;
						isPulling = false;
						onClientRemoval();
					}
				}
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		if(!level.isClientSide && getOwner() instanceof Player owner && entityHitResult.getEntity() != owner) {
			if((entityHitResult.getEntity() instanceof LivingEntity || entityHitResult.getEntity() instanceof EnderDragonPart) && hookedEntity == null) {
				hookedEntity = entityHitResult.getEntity();
				entityData.set(HOOKED_ENTITY_ID, hookedEntity.getId() + 1);
				isPulling = true;
			}

			if(hookedEntity != null && UpgradesHelper.hasBleedUpgrade(stack))
				hookedEntity.hurt(ModDamageSource.bleed(this, owner), 1);

			if(UpgradesHelper.hasEndericUpgrade(stack)) {
				owner.teleportTo(getX(), getY(), getZ());
				owner.fallDistance = 0.0F;
				((PlayerProperties) owner).setHasHook(false);
				isPulling = false;
				onClientRemoval();
			}
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		maxRange = tag.getDouble("maxRange");
		maxSpeed = tag.getDouble("maxSpeed");
		isPulling = tag.getBoolean("isPulling");
		stack = ItemStack.of(tag.getCompound("hookshotItem"));

		if(level.getEntity(tag.getInt("owner")) instanceof Player owner)
			setOwner(owner);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putDouble("maxRange", maxRange);
		tag.putDouble("maxSpeed", maxSpeed);
		tag.putBoolean("isPulling", isPulling);
		tag.put("hookshotItem", stack.save(new CompoundTag()));

		if(getOwner() instanceof Player owner)
			tag.putInt("owner", owner.getId());
	}

	public void setProperties(ItemStack stack, double maxRange, double maxVelocity, float pitch, float yaw, float roll, float modifierZ) {
		float f = 0.017453292F;
		float x = -Mth.sin(yaw * f) * Mth.cos(pitch * f);
		float y = -Mth.sin((pitch + roll) * f);
		float z = Mth.cos(yaw * f) * Mth.cos(pitch * f);
		this.shoot(x, y, z, modifierZ, 0);

		this.stack = stack;
		this.maxRange = maxRange;
		this.maxSpeed = maxVelocity;
	}
}
