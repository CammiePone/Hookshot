package dev.camcodes.hookshot.common.item;

import dev.camcodes.hookshot.common.entity.HookshotEntity;
import dev.camcodes.hookshot.core.registry.ModEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class HookshotItem extends Item
{
	private double maxRange;
	private double maxVelocity;

	public HookshotItem(double maxRange, double maxVelocity)
	{
		super(new Item.Settings().group(ItemGroup.TOOLS));

		this.maxRange = maxRange;
		this.maxVelocity = maxVelocity;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		if(!world.isClient)
		{
			HookshotEntity hookshot = new HookshotEntity(ModEntities.HOOKSHOT_ENTITY, user, world);
			hookshot.setProperties(user, maxRange, maxVelocity, user.pitch, user.yaw, 0f, 1.5f, 1f);
			world.spawnEntity(hookshot);
		}
		else
		{
			world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1f, 1.0f / (RANDOM.nextFloat() * 0.4f + 1.2f) + 1.0f * 0.5f);
		}

		return super.use(world, user, hand);
	}
}
