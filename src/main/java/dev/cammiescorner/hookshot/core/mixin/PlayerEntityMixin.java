package dev.cammiescorner.hookshot.core.mixin;

import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.cammiescorner.hookshot.Hookshot.DataTrackers.HOOK_TRACKER;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerProperties {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readNbt(CompoundTag tag, CallbackInfo info) {
		entityData.set(HOOK_TRACKER, tag.getBoolean("hasHook"));
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void writeNbt(CompoundTag tag, CallbackInfo info) {
		tag.putBoolean("hasHook", entityData.get(HOOK_TRACKER));
	}

	@Inject(method = "initDataTracker", at = @At("HEAD"))
	public void initTracker(CallbackInfo info) {
		entityData.define(HOOK_TRACKER, false);
	}

	@Override
	public boolean hasHook() {
		return entityData.get(HOOK_TRACKER);
	}

	@Override
	public void setHasHook(boolean hasHook) {
		entityData.set(HOOK_TRACKER, hasHook);
	}
}
