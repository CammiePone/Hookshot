package dev.camcodes.hookshot.common.item;

import dev.camcodes.hookshot.Hookshot;
import dev.camcodes.hookshot.common.entity.HookshotEntity;
import dev.camcodes.hookshot.core.registry.ModEntities;
import dev.camcodes.hookshot.core.util.PlayerProperties;
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
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class HookshotItem extends Item
{
	public HookshotItem(int durability)
	{
		super(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1).maxDamage(durability));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		if(!world.isClient)
		{
			if(!((PlayerProperties) user).hasHook())
			{
				double maxRange = Hookshot.config.defaultMaxRange;
				double maxSpeed = Hookshot.config.defaultMaxSpeed;
				ItemStack stack = user.getStackInHand(hand);

				if(stack.hasTag())
				{
					if(stack.getTag().getBoolean("hasRange")) maxRange *= Hookshot.config.rangeMult;
					if(stack.getTag().getBoolean("hasQuick")) maxSpeed *= Hookshot.config.quickMult;
				}

				HookshotEntity hookshot = new HookshotEntity(ModEntities.HOOKSHOT_ENTITY, user, world);
				hookshot.setProperties(stack, maxRange, maxSpeed, user.pitch, user.headYaw, 0f, 1.5f * (float) (maxSpeed / 10), 1f);
				world.spawnEntity(hookshot);
				((PlayerProperties) user).setHasHook(true);
				if(stack.getMaxDamage() > 0) stack.damage(1, (LivingEntity) user, (entity) -> entity.sendToolBreakStatus(user.getActiveHand()));
			}
			else
			{
				((PlayerProperties) user).setHasHook(false);
			}
		}

		if(!((PlayerProperties) user).hasHook())
		{
			world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS,
					1f, 1.0f / (RANDOM.nextFloat() * 0.4f + 1.2f) + 1.0f * 0.5f);
		}

		return super.use(world, user, hand);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
	{
		if(stack.getOrCreateTag().getBoolean("hasAqua")) tooltip.add(new TranslatableText("hookshot.modifier.aqua").formatted(Formatting.GRAY));
		if(stack.getOrCreateTag().getBoolean("hasEnder")) tooltip.add(new TranslatableText("hookshot.modifier.ender").formatted(Formatting.GRAY));
		if(stack.getOrCreateTag().getBoolean("hasQuick")) tooltip.add(new TranslatableText("hookshot.modifier.quick").formatted(Formatting.GRAY));
		if(stack.getOrCreateTag().getBoolean("hasRange")) tooltip.add(new TranslatableText("hookshot.modifier.range").formatted(Formatting.GRAY));
		if(stack.getOrCreateTag().getBoolean("hasAuto")) tooltip.add(new TranslatableText("hookshot.modifier.automatic").formatted(Formatting.GRAY));
	}

	@Override
	public Text getName(ItemStack stack)
	{
		TranslatableText modifiers = new TranslatableText("");
		TranslatableText name = new TranslatableText("item.hookshot.hookshot", modifiers);

		if(stack.getOrCreateTag().getBoolean("hasAuto")) modifiers.append(new TranslatableText("hookshot.modifier.auto"));

		return hasModifiers(stack) ? name.formatted(Formatting.AQUA) : name;
	}

	public boolean hasModifiers(ItemStack stack)
	{
		assert stack.getTag() != null;

		return stack.getTag().getBoolean("hasAqua") || stack.getTag().getBoolean("hasEnder") || stack.getTag().getBoolean("hasQuick") ||
				stack.getTag().getBoolean("hasRange") || stack.getTag().getBoolean("hasAuto");
	}
}
