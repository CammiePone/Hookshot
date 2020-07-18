package dev.camcodes.hookshot.client.entity.renderer;

import dev.camcodes.hookshot.Hookshot;
import dev.camcodes.hookshot.client.entity.model.HookshotEntityModel;
import dev.camcodes.hookshot.common.entity.HookshotEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HookshotEntityRenderer extends EntityRenderer<HookshotEntity>
{
	private static final HookshotEntityModel MODEL = new HookshotEntityModel();
	private static final Identifier TEXTURE = new Identifier(Hookshot.MOD_ID, "textures/entity/hookshot.png");

	public HookshotEntityRenderer(final EntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(HookshotEntity hookshot, float yaw, float tickDelta, MatrixStack stack, VertexConsumerProvider provider, int light)
	{
		stack.push();
		VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(provider, MODEL.getLayer(this.getTexture(hookshot)), false, false);
		this.MODEL.render(stack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.pop();

		super.render(hookshot, yaw, tickDelta, stack, provider, light);
	}

	@Override
	public Identifier getTexture(HookshotEntity entity)
	{
		return TEXTURE;
	}
}
