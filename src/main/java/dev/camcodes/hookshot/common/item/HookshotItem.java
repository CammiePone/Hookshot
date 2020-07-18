package dev.camcodes.hookshot.common.item;

import dev.camcodes.hookshot.common.entity.HookshotEntity;
import dev.camcodes.hookshot.core.registry.ModEntities;
import dev.camcodes.hookshot.core.util.PlayerProperties;
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
	private HookshotEntity hookshot;

	public HookshotItem(double maxRange, double maxVelocity)
	{
		super(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));

		this.maxRange = maxRange;
		this.maxVelocity = maxVelocity;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		if(!world.isClient)
		{
			if(!((PlayerProperties) user).hasHook())
			{
				hookshot = new HookshotEntity(ModEntities.HOOKSHOT_ENTITY, user, world);
				hookshot.setProperties(user, maxRange, maxVelocity, user.pitch, user.yaw, 0f, 1.5f * (float) (maxVelocity / 10), 1f);
				world.spawnEntity(hookshot);
				((PlayerProperties) user).setHasHook(true);
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
}
