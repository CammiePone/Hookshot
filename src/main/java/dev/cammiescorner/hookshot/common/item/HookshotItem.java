package dev.cammiescorner.hookshot.common.item;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import dev.cammiescorner.hookshot.core.integration.HookshotConfig;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.util.Dyeable;
import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import dev.cammiescorner.hookshot.core.util.UpgradesHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import java.util.List;

public class HookshotItem extends Item implements Dyeable {
	private final DyeColor colour;

	public HookshotItem(DyeColor colour) {
		super(new Item.Properties().stacksTo(1).durability(HookshotConfig.defaultMaxDurability));
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.accept(this));
		this.colour = colour;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);

		if(!world.isClientSide) {
			if(!((PlayerProperties) user).hasHook()) {
				double maxRange = HookshotConfig.defaultMaxRange * (UpgradesHelper.hasRangeUpgrade(stack) ? HookshotConfig.rangeMultiplier : 1);
				double maxSpeed = HookshotConfig.defaultMaxSpeed * (UpgradesHelper.hasQuickUpgrade(stack) ? HookshotConfig.quickMultiplier : 1);

				HookshotEntity hookshot = new HookshotEntity(ModEntities.HOOKSHOT_ENTITY, user, world);
				hookshot.setProperties(stack, maxRange, maxSpeed, user.getXRot(), user.getYRot(), 0f, 1.5f * (float) (maxSpeed / 10));
				world.addFreshEntity(hookshot);
			}

			if(!HookshotConfig.useClassicHookshotLogic) {
				user.startUsingItem(hand);
				((PlayerProperties) user).setHasHook(true);
			}
			else {
				((PlayerProperties) user).setHasHook(!((PlayerProperties) user).hasHook());
			}
		}

		if(!((PlayerProperties) user).hasHook())
			world.playSound(user, user.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1F, 1F);

		return super.use(world, user, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if(!HookshotConfig.useClassicHookshotLogic)
			((PlayerProperties) user).setHasHook(false);

		return super.finishUsingItem(stack, world, user);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
		if(!HookshotConfig.useClassicHookshotLogic)
			((PlayerProperties) user).setHasHook(false);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
		return ingredient.getItem() == BuiltInRegistries.ITEM.get(new ResourceLocation(HookshotConfig.hookshotRepairItem));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
		if(UpgradesHelper.hasDurabilityUpgrade(stack))
			tooltip.add(Component.translatable(Hookshot.MOD_ID + ".modifier.durability").withStyle(ChatFormatting.GRAY));
		if(UpgradesHelper.hasAutomaticUpgrade(stack))
			tooltip.add(Component.translatable(Hookshot.MOD_ID + ".modifier.automatic").withStyle(ChatFormatting.GRAY));
		if(UpgradesHelper.hasSwingingUpgrade(stack))
			tooltip.add(Component.translatable(Hookshot.MOD_ID + ".modifier.swinging").withStyle(ChatFormatting.GRAY));
		if(UpgradesHelper.hasAquaticUpgrade(stack))
			tooltip.add(Component.translatable(Hookshot.MOD_ID + ".modifier.aquatic").withStyle(ChatFormatting.GRAY));
		if(UpgradesHelper.hasEndericUpgrade(stack))
			tooltip.add(Component.translatable(Hookshot.MOD_ID + ".modifier.enderic").withStyle(ChatFormatting.GRAY));
		if(UpgradesHelper.hasQuickUpgrade(stack))
			tooltip.add(Component.translatable(Hookshot.MOD_ID + ".modifier.quick").withStyle(ChatFormatting.GRAY));
		if(UpgradesHelper.hasRangeUpgrade(stack))
			tooltip.add(Component.translatable(Hookshot.MOD_ID + ".modifier.range").withStyle(ChatFormatting.GRAY));
		if(UpgradesHelper.hasBleedUpgrade(stack))
			tooltip.add(Component.translatable(Hookshot.MOD_ID + ".modifier.bleed").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public Component getName(ItemStack stack) {
		boolean hasModifiers = UpgradesHelper.hasAquaticUpgrade(stack) || UpgradesHelper.hasEndericUpgrade(stack) || UpgradesHelper.hasQuickUpgrade(stack) || UpgradesHelper.hasRangeUpgrade(stack) || UpgradesHelper.hasAutomaticUpgrade(stack) || UpgradesHelper.hasBleedUpgrade(stack) || UpgradesHelper.hasSwingingUpgrade(stack) || UpgradesHelper.hasDurabilityUpgrade(stack);

		return hasModifiers ? super.getName(stack).copy().withStyle(ChatFormatting.AQUA) : super.getName(stack);
	}

	@Override
	public DyeColor getColour() {
		return colour;
	}
}
