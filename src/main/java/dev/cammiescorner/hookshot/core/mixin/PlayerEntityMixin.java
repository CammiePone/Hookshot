package dev.cammiescorner.hookshot.core.mixin;

import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.Flutterer;
import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.core.integration.ReduceAirFrictionConfig;

import static dev.cammiescorner.hookshot.Hookshot.DataTrackers.HOOK_TRACKER;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerProperties
{
	@Shadow protected abstract Vec3d adjustMovementForSneaking(Vec3d movement, MovementType type);
	private boolean enableAirMovement = false;
	private Vec3d prevVel = Vec3d.ZERO;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readNbt(NbtCompound tag, CallbackInfo info)
	{
		dataTracker.set(HOOK_TRACKER, tag.getBoolean("hasHook"));
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound tag, CallbackInfo info)
	{
		tag.putBoolean("hasHook", dataTracker.get(HOOK_TRACKER));
	}

	@Inject(method = "initDataTracker", at = @At("HEAD"))
	public void initTracker(CallbackInfo info)
	{
		dataTracker.startTracking(HOOK_TRACKER, false);
	}

	@Override
	public boolean hasHook()
	{
		return dataTracker.get(HOOK_TRACKER);
	}

	@Override
	public void setHasHook(boolean hasHook)
	{
		enableAirMovement = !hasHook && !this.isOnGround();
		dataTracker.set(HOOK_TRACKER, hasHook);
	}

	private static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
		double d = movementInput.lengthSquared();
		Vec3d vec3d = (d > 1.0D ? movementInput.normalize() : movementInput).multiply(speed);
		float f = MathHelper.sin(yaw * 0.017453292F);
		float g = MathHelper.cos(yaw * 0.017453292F);
		return new Vec3d(vec3d.x * (double)g - vec3d.z * (double)f, vec3d.y, vec3d.z * (double)g + vec3d.x * (double)f);
	}

	// Increases mobility while in free fall
	// Taken from https://github.com/hatninja/Bhops
	@Inject(method = "travel", at = @At("HEAD"), cancellable = true)
	public void travel(Vec3d movementInput, CallbackInfo ci) {
		if (!enableAirMovement || !Hookshot.config.useSwingingHookshot) { return; }

		if (this.isOnGround() || this.hasHook()) {
			enableAirMovement = false;
		}
		else {
			if (this.isTouchingWater() || this.isInLava() || this.isFallFlying() || !this.isAlive() || this.isInSneakingPose()) { return; }
			if (!this.canMoveVoluntarily() && !this.isLogicalSideForUpdatingMovement()) { return; }

			//I don't have a better clue how to do this atm.
			LivingEntity self = (LivingEntity) this.world.getEntityById(this.getId());

			//Reverse multiplication done by the function that calls this one.
			this.sidewaysSpeed /= 0.98F;
			this.forwardSpeed /= 0.98F;
			double sI = movementInput.x / 0.98F;
			double fI = movementInput.z / 0.98F;

			// Accelerate
			BlockPos blockPos = this.getVelocityAffectingPos();
			if (sI != 0.0F || fI != 0.0F) {
				Vec3d moveDir = movementInputToVelocity(new Vec3d(sI, 0.0F, fI), 1.0F, this.getYaw());
				Vec3d accelVec = this.getVelocity();

				double projVel = new Vec3d(accelVec.x, 0.0F, accelVec.z).dotProduct(moveDir);
				double accelVel = this.onGround ? ReduceAirFrictionConfig.sv_accelerate : ReduceAirFrictionConfig.sv_airaccelerate;
				float maxVel = this.onGround ? this.getMovementSpeed() * ReduceAirFrictionConfig.maxSpeedMul : ReduceAirFrictionConfig.sv_maxairspeed;

				if (projVel + accelVel > maxVel) {
					accelVel = maxVel - projVel;
				}
				Vec3d accelDir = moveDir.multiply(Math.max(accelVel, 0.0F));
				this.setVelocity(accelVec.add(accelDir));
			}

			this.move(MovementType.SELF, this.getVelocity());

			//Ladder Logic
			Vec3d preVel = this.getVelocity();
			if ((this.horizontalCollision || this.jumping) && this.isClimbing()) {
				preVel = new Vec3d(preVel.x * 0.7D, 0.2D, preVel.z * 0.7D);
			}

			//Apply Gravity (If not in Water)
			double yVel = preVel.y;
			double gravity = ReduceAirFrictionConfig.sv_gravity;

			if (preVel.y <= 0.0D && this.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
				gravity = 0.01D;
				this.fallDistance = 0.0F;
			}
			if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
				yVel += (0.05D * (this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1) - preVel.y) * 0.2D;
				this.fallDistance = 0.0F;
			} else if (this.world.isClient && !this.world.isChunkLoaded(blockPos)) {
				yVel = 0.0D;
			} else if (!this.hasNoGravity()) {
				yVel -= gravity;
			}

			this.setVelocity(preVel.x,yVel,preVel.z);
			this.updateLimbs(self, self instanceof Flutterer);
			ci.cancel();
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci) {
		this.prevVel = this.getVelocity();
	}

	@ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float dam2(float amount) {
		// Reduce fall damage if falling slow enough
		if (Hookshot.config.useSwingingHookshot) {
			if (prevVel.y > -1 && prevVel.y < 0 && this.hasHook()) {
				return 2F;
			}
		}
		return amount;
	}

}
