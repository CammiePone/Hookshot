package dev.cammiescorner.hookshot.core.mixin;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import dev.cammiescorner.hookshot.core.util.UpgradesHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin
{
	@Shadow public abstract Item getItem();

	@Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
	public void getMaxDamage(CallbackInfoReturnable<Integer> info)
	{
		if(getItem() instanceof HookshotItem && UpgradesHelper.hasDurabilityUpgrade((ItemStack) (Object) this))
			info.setReturnValue((int) (getItem().getMaxDamage() * Hookshot.config.durabilityMultiplier));
	}
}
