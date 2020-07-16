package dev.camcodes.hookshot.core.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface IsJumpingAccessor
{
	@Accessor("jumping")
	public boolean isJumping();
}
