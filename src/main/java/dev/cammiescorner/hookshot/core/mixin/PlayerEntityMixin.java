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

import static dev.cammiescorner.hookshot.Hookshot.DataTrackers.HOOK_TRACKER;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerProperties {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readNbt(NbtCompound tag, CallbackInfo info) {
		dataTracker.set(HOOK_TRACKER, tag.getBoolean("hasHook"));
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound tag, CallbackInfo info) {
		tag.putBoolean("hasHook", dataTracker.get(HOOK_TRACKER));
	}

	@Inject(method = "initDataTracker", at = @At("HEAD"))
	public void initTracker(CallbackInfo info) {
		dataTracker.startTracking(HOOK_TRACKER, false);
	}

	@Override
	public boolean hasHook() {
		return dataTracker.get(HOOK_TRACKER);
	}

	@Override
	public void setHasHook(boolean hasHook) {
		dataTracker.set(HOOK_TRACKER, hasHook);
	}
}
