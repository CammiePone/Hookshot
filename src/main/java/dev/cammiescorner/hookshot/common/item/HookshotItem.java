package dev.cammiescorner.hookshot.common.item;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import dev.cammiescorner.hookshot.core.util.UpgradesHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class HookshotItem extends Item
{
	public HookshotItem()
	{
		super(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1).maxDamage(Hookshot.config.durability));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		ItemStack stack = user.getStackInHand(hand);

		if(!world.isClient)
		{
			if(!((PlayerProperties) user).hasHook())
			{
				double maxRange = Hookshot.config.defaultMaxRange * (UpgradesHelper.hasRangeUpgrade(stack) ? Hookshot.config.rangeMultiplier : 1);
				double maxSpeed = Hookshot.config.defaultMaxSpeed * (UpgradesHelper.hasQuickUpgrade(stack) ? Hookshot.config.quickMultiplier : 1);

				HookshotEntity hookshot = new HookshotEntity(ModEntities.HOOKSHOT_ENTITY, user, world);
				hookshot.setProperties(stack, maxRange, maxSpeed, user.pitch, user.headYaw, 0f, 1.5f * (float) (maxSpeed / 10), 1f);
				world.spawnEntity(hookshot);

				if(stack.getMaxDamage() > 0)
					stack.damage(1, (LivingEntity) user, (entity) -> entity.sendToolBreakStatus(user.getActiveHand()));
			}

			if(!Hookshot.config.useClassicHookshotLogic)
			{
				user.setCurrentHand(hand);
				((PlayerProperties) user).setHasHook(true);
			}
			else
			{
				((PlayerProperties) user).setHasHook(!((PlayerProperties) user).hasHook());
			}
		}

		if(!((PlayerProperties) user).hasHook())
		{
			world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS,
					1f, 1.0F / (RANDOM.nextFloat() * 0.4F + 1.2F) + 0.5F);
		}

		return super.use(world, user, hand);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
	{
		if(!Hookshot.config.useClassicHookshotLogic)
			((PlayerProperties) user).setHasHook(false);

		return super.finishUsing(stack, world, user);
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks)
	{
		if(!Hookshot.config.useClassicHookshotLogic)
			((PlayerProperties) user).setHasHook(false);
	}

	@Override
	public int getMaxUseTime(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient)
	{
		return ingredient.getItem() == Registry.ITEM.get(new Identifier(Hookshot.config.hookshotRepairItem));
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
	{
		if(UpgradesHelper.hasAutomaticUpgrade(stack))
			tooltip.add(new TranslatableText(Hookshot.MOD_ID + ".modifier.automatic").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasSwingingUpgrade(stack))
			tooltip.add(new TranslatableText(Hookshot.MOD_ID + ".modifier.swinging").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasAquaticUpgrade(stack))
			tooltip.add(new TranslatableText(Hookshot.MOD_ID + ".modifier.aquatic").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasEndericUpgrade(stack))
			tooltip.add(new TranslatableText(Hookshot.MOD_ID + ".modifier.enderic").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasQuickUpgrade(stack))
			tooltip.add(new TranslatableText(Hookshot.MOD_ID + ".modifier.quick").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasRangeUpgrade(stack))
			tooltip.add(new TranslatableText(Hookshot.MOD_ID + ".modifier.range").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasBleedUpgrade(stack))
			tooltip.add(new TranslatableText(Hookshot.MOD_ID + ".modifier.bleed").formatted(Formatting.GRAY));
	}

	@Override
	public Text getName(ItemStack stack)
	{
		boolean hasModifiers = UpgradesHelper.hasAquaticUpgrade(stack) || UpgradesHelper.hasEndericUpgrade(stack) ||
				UpgradesHelper.hasQuickUpgrade(stack) || UpgradesHelper.hasRangeUpgrade(stack) ||
				UpgradesHelper.hasAutomaticUpgrade(stack) || UpgradesHelper.hasBleedUpgrade(stack) ||
				UpgradesHelper.hasSwingingUpgrade(stack);

		return hasModifiers ? super.getName(stack).copy().formatted(Formatting.AQUA) : super.getName(stack);
	}
}
