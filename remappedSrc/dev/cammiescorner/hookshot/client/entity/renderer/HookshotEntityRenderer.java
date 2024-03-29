package dev.cammiescorner.hookshot.client.entity.renderer;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.client.entity.model.HookshotEntityModel;
import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

public class HookshotEntityRenderer extends EntityRenderer<HookshotEntity>
{
	private static final HookshotEntityModel MODEL = new HookshotEntityModel();
	private static final Identifier TEXTURE = new Identifier(Hookshot.MOD_ID, "textures/entity/hookshot.png");
	private static final Identifier CHAIN_TEXTURE = new Identifier(Hookshot.MOD_ID, "textures/entity/chain.png");
	private static final RenderLayer CHAIN_LAYER = RenderLayer.getEntitySmoothCutout(CHAIN_TEXTURE);

	public HookshotEntityRenderer(final EntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(HookshotEntity hookshot, float yaw, float tickDelta, MatrixStack stack, VertexConsumerProvider provider, int light)
	{
		if(hookshot.getOwner() instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) hookshot.getOwner();

			if(player != null)
			{
				Arm mainArm = MinecraftClient.getInstance().options.mainArm;
				Hand activeHand = player.getActiveHand();

				MODEL.setAngles(hookshot, 0F, 0F, hookshot.age, hookshot.yaw, hookshot.pitch);
				VertexConsumer vertexConsumer = provider.getBuffer(MODEL.getLayer(this.getTexture(hookshot)));
				MODEL.render(stack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

				stack.push();
				boolean rightHandIsActive = (mainArm == Arm.RIGHT && activeHand == Hand.MAIN_HAND) || (mainArm == Arm.LEFT && activeHand == Hand.OFF_HAND);
				double bodyYawToRads = Math.toRadians(player.bodyYaw);
				double radius = rightHandIsActive ? -0.4D : 0.4D;
				double startX = player.getX() + radius * Math.cos(bodyYawToRads);
				double startY = player.getY() + (player.getHeight() / 3D);
				double startZ = player.getZ() + radius * Math.sin(bodyYawToRads);
				float distanceX = (float) (startX - hookshot.getX());
				float distanceY = (float) (startY - hookshot.getY());
				float distanceZ = (float) (startZ - hookshot.getZ());

				renderChain(distanceX, distanceY, distanceZ, tickDelta, hookshot.age, stack, provider, light);
				stack.pop();
			}
		}

		super.render(hookshot, yaw, tickDelta, stack, provider, light);
	}

	public void renderChain(float x, float y, float z, float tickDelta, int age, MatrixStack stack, VertexConsumerProvider provider, int light)
	{
		float lengthXY = MathHelper.sqrt(x * x + z * z);
		float squaredLength = x * x + y * y + z * z;
		float length = MathHelper.sqrt(squaredLength);

		stack.push();
		stack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion((float) (-Math.atan2(z, x)) - 1.5707964F));
		stack.multiply(Vector3f.POSITIVE_X.getRadialQuaternion((float) (-Math.atan2(lengthXY, y)) - 1.5707964F));

		VertexConsumer vertexConsumer = provider.getBuffer(CHAIN_LAYER);
		float h = 0.0F - ((float) age + tickDelta) * 0.01F;
		float i = MathHelper.sqrt(squaredLength) / 32.0F - ((float) age + tickDelta) * 0.01F;
		float k = 0.0F;
		float l = 0.75F;
		float m = 0.0F;
		MatrixStack.Entry entry = stack.peek();
		Matrix4f matrix4f = entry.getModel();
		Matrix3f matrix3f = entry.getNormal();

		for(int n = 1; n <= 8; ++n)
		{
			float o = MathHelper.sin((float) n * 6.2831855F / 8.0F) * 0.125F;
			float p = MathHelper.cos((float) n * 6.2831855F / 8.0F) * 0.125F;
			float q = (float) n / 8.0F;

			vertexConsumer.vertex(matrix4f, k * 0.2F, l * 0.2F, 0.0F).color(0, 0, 0, 255).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, k, l, length).color(255, 255, 255, 255).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, o, p, length).color(255, 255, 255, 255).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, o * 0.2F, p * 0.2F, 0.0F).color(0, 0, 0, 255).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();

			k = o;
			l = p;
			m = q;
		}

		stack.pop();
	}

	@Override
	public Identifier getTexture(HookshotEntity entity)
	{
		return TEXTURE;
	}
}