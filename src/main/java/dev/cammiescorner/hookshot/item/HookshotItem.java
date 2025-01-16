package dev.cammiescorner.hookshot.item;

import com.mojang.datafixers.util.Pair;
import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.HookshotConfig;
import dev.cammiescorner.hookshot.component.HookOwnerComponent;
import dev.cammiescorner.hookshot.data.HookshotItemTags;
import dev.cammiescorner.hookshot.entity.HookshotEntity;
import dev.cammiescorner.hookshot.registry.HookshotComponents;
import dev.cammiescorner.hookshot.registry.HookshotUpgrades;
import dev.cammiescorner.hookshot.util.Dyeable;
import dev.cammiescorner.hookshot.util.UpgradesHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HookshotItem extends Item implements Dyeable {

	public static final Ingredient REPAIR_INGREDIENT = Ingredient.of(HookshotItemTags.HOOKSHOT_REPAIR_ITEMS);
	public static final ResourceLocation USING_HOOK_MODEL_PROPERTY_ID = Hookshot.id("using_hook");


	private final DyeColor color;

	public HookshotItem(DyeColor color) {
		super(new Item.Properties().stacksTo(1).durability(HookshotConfig.defaultDurability));
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.accept(this));
		this.color = color;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);

		if(user.isUsingItem()) {
			return InteractionResultHolder.consume(stack);
		}

		if(!HookshotConfig.useClassicHookshotLogic) {
			user.startUsingItem(hand);
		}

		HookOwnerComponent hookOwner = user.getComponent(HookshotComponents.HOOK_OWNER);
		if(!level.isClientSide()) {
			if(!hookOwner.hasHook()) {
				double maxRange = HookshotConfig.defaultRange * (UpgradesHelper.hasUpgrade(stack, HookshotUpgrades.RANGE.get()) ? HookshotConfig.rangeUpgradeMultiplier : 1.0D);
				double maxSpeed = HookshotConfig.defaultSpeed * (UpgradesHelper.hasUpgrade(stack, HookshotUpgrades.SPEED.get()) ? HookshotConfig.speedUpgradeMultiplier : 1.0D);

				HookshotEntity hookshot = new HookshotEntity(user, level);
				hookshot.setProperties(stack.copy(), maxRange, maxSpeed, user.getXRot(), user.getYRot(), 0f, 1.5f * (float) (maxSpeed / 10));
				level.addFreshEntity(hookshot);
				level.playSound(user, user.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1F, 1F);
				hookOwner.setHasHook(true);
			}
			else if(HookshotConfig.useClassicHookshotLogic) {
				hookOwner.setHasHook(false);
				if(HookshotConfig.hookshotCooldown > 0) {
					addHookCooldown(user, HookshotConfig.hookshotCooldown);
				}
			}
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
		if(!HookshotConfig.useClassicHookshotLogic) {
			HookshotComponents.HOOK_OWNER.maybeGet(user).ifPresent(hookOwner -> hookOwner.setHasHook(false));
			if(user instanceof Player player && HookshotConfig.hookshotCooldown > 0) {
				addHookCooldown(player, HookshotConfig.hookshotCooldown);
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
		return REPAIR_INGREDIENT.test(repairCandidate);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
		tooltip.addAll(UpgradesHelper.getUpgradesTooltip(stack));
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return UpgradesHelper.getUpgrades(stack).isEmpty() ? super.getRarity(stack) : Rarity.RARE;
	}

	@Override
	public DyeColor getColor() {
		return color;
	}

	@Nullable
	public static Pair<ItemStack, InteractionHand> findHeldHookshot(LivingEntity owner) {
		var mainHandItem = owner.getMainHandItem();
		if(mainHandItem.is(HookshotItemTags.HOOKSHOTS)) {
			return Pair.of(mainHandItem, InteractionHand.MAIN_HAND);
		}

		var offHandItem = owner.getOffhandItem();
		if(offHandItem.is(HookshotItemTags.HOOKSHOTS)) {
			return Pair.of(offHandItem, InteractionHand.OFF_HAND);
		}

		return null;
	}

	public static void addHookCooldown(Player player, int cooldown) {
		var cooldowns = player.getCooldowns();
		BuiltInRegistries.ITEM.getTagOrEmpty(HookshotItemTags.HOOKSHOTS).forEach(holder -> cooldowns.addCooldown(holder.value(), cooldown));
	}
}
