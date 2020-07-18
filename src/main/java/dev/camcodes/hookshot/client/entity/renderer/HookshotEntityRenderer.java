package dev.camcodes.hookshot.client.entity.renderer;

import dev.camcodes.hookshot.Hookshot;
import dev.camcodes.hookshot.client.entity.model.HookshotEntityModel;
import dev.camcodes.hookshot.common.entity.HookshotEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;

public class HookshotEntityRenderer extends ProjectileEntityRenderer<HookshotEntity>
{
	private final HookshotEntityModel model = new HookshotEntityModel();

	public HookshotEntityRenderer(final EntityRenderDispatcher dispatcher, final EntityRendererRegistry.Context context)
	{
		super(dispatcher);
	}

	@Override
	public void render(HookshotEntity hookshot, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light) {
		matrices.push();

		matrices.translate(0.0D, 0.375D, 0.0D);
		matrices.scale(-1.0F, -1.0F, 1.0F);
		matrices.scale(2, 2, 2);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

		this.model.setAngles(hookshot, 0, 0.0F, -0.1F, hookshot.getYaw(tickDelta), hookshot.getPitch(tickDelta));
		VertexConsumer vertexConsumer = provider.getBuffer(this.model.getLayer(this.getTexture(hookshot)));
		this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

		matrices.pop();

		super.render(hookshot, yaw, tickDelta, matrices, provider, light);
	}

	@Override
	public Identifier getTexture(HookshotEntity entity)
	{
		return new Identifier(Hookshot.MOD_ID, "textures/entity/hookshot.png");
	}
}
