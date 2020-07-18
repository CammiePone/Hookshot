package dev.camcodes.hookshot.core.mixin;

import dev.camcodes.hookshot.core.util.DataTrackers;
import dev.camcodes.hookshot.core.util.PlayerProperties;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerProperties
{
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	public void readNbt(CompoundTag tag, CallbackInfo info)
	{
		dataTracker.set(DataTrackers.HOOK_TRACKER, tag.getBoolean("hasHook"));
	}

	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	public void writeNbt(CompoundTag tag, CallbackInfo info)
	{
		tag.putBoolean("hasHook", dataTracker.get(DataTrackers.HOOK_TRACKER));
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void initTracker(CallbackInfo info)
	{
		dataTracker.startTracking(DataTrackers.HOOK_TRACKER, true);
	}

	@Override
	public boolean hasHook()
	{
		return dataTracker.get(DataTrackers.HOOK_TRACKER);
	}

	@Override
	public void setHasHook(boolean hasHook)
	{
		dataTracker.set(DataTrackers.HOOK_TRACKER, hasHook);
	}
}
