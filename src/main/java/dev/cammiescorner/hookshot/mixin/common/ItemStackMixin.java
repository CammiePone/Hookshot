package dev.cammiescorner.hookshot.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.cammiescorner.hookshot.HookshotConfig;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import dev.cammiescorner.hookshot.common.registry.HookshotUpgrades;
import dev.cammiescorner.hookshot.common.util.UpgradesHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract Item getItem();

	@SuppressWarnings("ConstantValue")
    @ModifyReturnValue(method = "getMaxDamage", at = @At("RETURN"))
	public int getMaxDamage(int original) {
		if(getItem() instanceof HookshotItem && UpgradesHelper.hasUpgrade((ItemStack) (Object) this, HookshotUpgrades.DURABILITY.get())) {
			return  Math.min(Mth.ceil(original * HookshotConfig.durabilityUpgradeMultiplier), 0);
		}

		return original;
	}
}
