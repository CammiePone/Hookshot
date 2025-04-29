package dev.cammiescorner.hookshot.entity;

import dev.cammiescorner.hookshot.HookshotConfig;
import dev.cammiescorner.hookshot.data.HookshotBlockTags;
import dev.cammiescorner.hookshot.data.HookshotDamageTypes;
import dev.cammiescorner.hookshot.data.HookshotItemTags;
import dev.cammiescorner.hookshot.item.HookshotItem;
import dev.cammiescorner.hookshot.registry.HookshotComponents;
import dev.cammiescorner.hookshot.registry.HookshotEntities;
import dev.cammiescorner.hookshot.registry.HookshotSoundEvents;
import dev.cammiescorner.hookshot.registry.HookshotUpgrades;
import dev.cammiescorner.hookshot.util.UpgradesHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class HookshotEntity extends AbstractArrow {
    private static final EntityDataAccessor<Integer> HOOKED_ENTITY_ID = SynchedEntityData.defineId(HookshotEntity.class, EntityDataSerializers.INT);

    private double maxRange = 0D;
    private double maxSpeed = 0D;
    private boolean isPulling = false;
    @Nullable
    private Entity hookedEntity;
    private ItemStack stack;
    public boolean shotFromInventory;

    public HookshotEntity(Player owner, Level world) {
        super(HookshotEntities.HOOKSHOT.get(), owner, world);
        if (!HookshotConfig.useHookshotGravity)
            this.setNoGravity(true);
        this.setBaseDamage(0);
    }

    public HookshotEntity(EntityType<? extends HookshotEntity> type, Level world) {
        super(type, world);
        if (!HookshotConfig.useHookshotGravity)
            this.setNoGravity(true);
        this.setBaseDamage(0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(HOOKED_ENTITY_ID, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (HOOKED_ENTITY_ID.equals(key)) {
            int hookedEntityId = this.getEntityData().get(HOOKED_ENTITY_ID);
            hookedEntity = hookedEntityId != 0 ? this.level().getEntity(hookedEntityId - 1) : null;
        }

        super.onSyncedDataUpdated(key);
    }

    private boolean shouldStopExisting(LivingEntity owner) {
        if(!owner.isAlive() || owner.distanceToSqr(this) > maxRange * maxRange) {
            return true;
        }
        if(HookshotItem.findHeldHookshot(owner) == null && !shotFromInventory) {
            return true;
        }

        var hookOwner = HookshotComponents.HOOK_OWNER.getNullable(owner);
        return hookOwner != null && !hookOwner.hasHook();
    }

    @Override
    public void tick() {
        super.tick();

        if(!level().isClientSide()) {
            if ((!(getOwner() instanceof LivingEntity owner)) || shouldStopExisting(owner)) {
                discard();
                return;
            }

            if (isPulling && tickCount % 2 == 0) {
                level().playSound(null, owner, HookshotSoundEvents.HOOKSHOT_REEL.get(), owner.getSoundSource(), 1F, 1F);
            }

            if (this.hookedEntity != null) {
                if (this.hookedEntity.isRemoved()) {
                    this.setHookedEntity(null);
                    onClientRemoval();
                } else {
                    if (tickCount % 20 == 0 && UpgradesHelper.hasUpgrade(stack, HookshotUpgrades.BLEED.get())) {
                        hookedEntity.hurt(HookshotDamageTypes.getBleedingDamage(this, owner), 1);
                    }

                    this.absMoveTo(this.hookedEntity.getX(), this.hookedEntity.getY(0.8D), this.hookedEntity.getZ());
                }
            }

            if (isPulling) {
                Entity target = HookshotConfig.hooksAffectVehicles ? owner.getRootVehicle() : owner;
                Entity origin = this;

                if (owner.isShiftKeyDown() && hookedEntity != null) {
                    target = hookedEntity;
                    origin = owner;
                }

                Vec3 motion;
                if (HookshotConfig.useSwingingHookshot) {
                    float currentDistance = target.distanceTo(origin);
                    boolean belowHookshot = target.getY() < origin.getY();
                    double ySpringStiffness, xzSpringStiffness;
                    ySpringStiffness = xzSpringStiffness = 0.25D * HookshotConfig.swingingHookPullUpStrength;

                    // Slow down players once close to the hook
                    if (currentDistance < 2) {
                        ySpringStiffness = 0;
                        xzSpringStiffness = 0;
                    }

                    if (currentDistance < 4) {
                        ySpringStiffness = 0.09D * HookshotConfig.swingingHookPullUpStrength;
                        xzSpringStiffness = 0.09D * HookshotConfig.swingingHookPullSidewaysStrength;
                    }
                    // increase Y stiffness when descending at large speeds
                    if (belowHookshot && target.getDeltaMovement().y < -1.5) {
                        ySpringStiffness = 1.5D * HookshotConfig.swingingHookPullUpStrength;
                    }
                    if (belowHookshot && target.getDeltaMovement().y < -2) {
                        ySpringStiffness = 4.0D * HookshotConfig.swingingHookPullUpStrength;
                    }
                    // Copied from lead code, same result as attaching a lead from hookshot to player
                    double xDis = (origin.getX() - target.getX()) / (double)currentDistance;
                    double yDis = (origin.getY() - target.getY()) / (double)currentDistance;
                    double zDis = (origin.getZ() - target.getZ()) / (double)currentDistance;
                    motion = target.getDeltaMovement().add(Math.copySign(xDis * xDis * xzSpringStiffness, xDis), Math.copySign(yDis * yDis * ySpringStiffness, yDis), Math.copySign(zDis * zDis * xzSpringStiffness, zDis));
                }
                else {
                    double brakeZone = (6D * (maxSpeed / HookshotConfig.defaultSpeed));
                    double pullSpeed = maxSpeed / 12D;
                    Vec3 distance = origin.position().subtract(target.position().add(0, target.getBbHeight() / 2, 0));

                    if (distance.lengthSqr() >= brakeZone * brakeZone && UpgradesHelper.hasUpgrade(stack, HookshotUpgrades.AUTOMATIC.get())) {
                        motion = distance.normalize().scale(pullSpeed);
                    } else {
                        motion = distance.scale(pullSpeed / brakeZone);
                    }
                }

                target.setDeltaMovement(motion);

                if (HookshotConfig.hookshotCancelsFallDamage) {
                    target.fallDistance = 0;
                }

                target.setDeltaMovement(motion);
                target.hurtMarked = true;

                if (owner.distanceToSqr(this) <= 3.0D * 3.0D && UpgradesHelper.hasUpgrade(stack, HookshotUpgrades.AUTOMATIC.get())) {
                    kill();
                }

                var heldStackInfo = HookshotItem.findHeldHookshot(owner);
                if (heldStackInfo != null) {
                    var heldStack = heldStackInfo.getFirst();
                    var hand = heldStackInfo.getSecond();

                    if (heldStack.getMaxDamage() > 0 && tickCount % 20 == 0) {
                        heldStack.hurtAndBreak(1, owner, (entity) -> entity.broadcastBreakEvent(hand));
                    }
                }
            }
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!level().isClientSide()) {
            HookshotComponents.HOOK_OWNER.maybeGet(getOwner()).ifPresent(hookOwner -> {
                hookOwner.setTempNoGravity(false);
                hookOwner.setHasHook(false);
            });
        }
        super.remove(reason);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    protected float getWaterInertia() {
        if (!level().isClientSide() && UpgradesHelper.hasUpgrade(stack, HookshotUpgrades.AQUATIC.get())) {
            return 0.99F;
        }

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

        if (!level().isClientSide() && hookedEntity == null) {
            HookshotComponents.HOOK_OWNER.maybeGet(getOwner()).ifPresent(hookOwner -> {
                hookOwner.setTempNoGravity(true);

                if (level().getBlockState(blockHitResult.getBlockPos()).is(HookshotBlockTags.UNHOOKABLE)) {
                    hookOwner.setHasHook(false);
                    isPulling = false;
                    discard();
                    onClientRemoval();
                } else {
                    if (UpgradesHelper.hasUpgrade(stack, HookshotUpgrades.ENDERIC.get())) {
                        hookOwner.setHasHook(false);
                        var entity = hookOwner.getEntity();
                        entity.teleportTo(getX(), getY(), getZ());
                        entity.fallDistance = 0.0F;
                        isPulling = false;
                        onClientRemoval();
                    }
                }
            });
        }
    }

    @Override
    public void onClientRemoval() {
        var owner = getOwner();
        HookshotComponents.HOOK_OWNER.maybeGet(owner).ifPresent(hookOwner -> hookOwner.setHasHook(false));
        if(owner instanceof LivingEntity living && living.getUseItem().is(HookshotItemTags.HOOKSHOTS)) {
            living.stopUsingItem();
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        @Nullable var owner = getOwner();
        return super.canHitEntity(target) && (owner == null || !owner.isPassengerOfSameVehicle(target));
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!level().isClientSide()) {
            @Nullable var owner = getOwner();
            var hitEntity = entityHitResult.getEntity();

            this.setHookedEntity(hitEntity);
            isPulling = true;

            if (UpgradesHelper.hasUpgrade(stack, HookshotUpgrades.BLEED.get())) {
                hitEntity.hurt(HookshotDamageTypes.getBleedingDamage(this, owner), 1);
            }

            if (UpgradesHelper.hasUpgrade(stack, HookshotUpgrades.ENDERIC.get())) {
                if (owner != null) {
                    owner.teleportTo(getX(), getY(), getZ());
                    owner.fallDistance = 0.0F;
                    HookshotComponents.HOOK_OWNER.maybeGet(owner).ifPresent(hookOwner -> hookOwner.setHasHook(false));
                }

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
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putDouble("maxRange", maxRange);
        tag.putDouble("maxSpeed", maxSpeed);
        tag.putBoolean("isPulling", isPulling);
        tag.put("hookshotItem", stack.save(new CompoundTag()));
    }

    public void setProperties(ItemStack stack, double maxRange, double maxSpeed, float pitch, float yaw, float roll, float modifierZ) {
        float f = 0.017453292F;
        float x = -Mth.sin(yaw * f) * Mth.cos(pitch * f);
        float y = -Mth.sin((pitch + roll) * f);
        float z = Mth.cos(yaw * f) * Mth.cos(pitch * f);
        this.shoot(x, y, z, modifierZ, 0);

        this.stack = stack;
        this.maxRange = maxRange;
        this.maxSpeed = maxSpeed;
    }

    private void setHookedEntity(@Nullable Entity hookedEntity) {
        this.hookedEntity = hookedEntity;
        this.getEntityData().set(HOOKED_ENTITY_ID, hookedEntity == null ? 0 : hookedEntity.getId() + 1);
    }
}
