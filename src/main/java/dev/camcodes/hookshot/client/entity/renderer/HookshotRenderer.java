package dev.camcodes.hookshot.client.entity.renderer;

import dev.camcodes.hookshot.Hookshot;
import dev.camcodes.hookshot.common.entity.HookshotEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.Identifier;

public class HookshotRenderer extends EntityRenderer<HookshotEntity>
{
	public HookshotRenderer(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher);
	}

	@Override
	public Identifier getTexture(HookshotEntity entity)
	{
		return new Identifier(Hookshot.MOD_ID, "textures/entity/hookshot.png");
	}
}
