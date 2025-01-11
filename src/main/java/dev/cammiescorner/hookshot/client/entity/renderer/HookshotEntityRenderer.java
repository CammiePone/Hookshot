package dev.cammiescorner.hookshot.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.client.HookshotClient;
import dev.cammiescorner.hookshot.client.entity.model.HookshotEntityModel;
import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class HookshotEntityRenderer extends ArrowRenderer<HookshotEntity> {
	private static HookshotEntityModel model;
	private static final ResourceLocation TEXTURE = new ResourceLocation(Hookshot.MOD_ID, "textures/entity/hookshot.png");
	private static final ResourceLocation CHAIN_TEXTURE = new ResourceLocation(Hookshot.MOD_ID, "textures/entity/chain.png");
	private static final RenderType CHAIN_LAYER = RenderType.entitySmoothCutout(CHAIN_TEXTURE);

	public HookshotEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new HookshotEntityModel(context.bakeLayer(HookshotClient.HOOKSHOT));
	}

	@Override
	public void render(HookshotEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource provider, int light) {
		if(entity.getOwner() instanceof Player player) {
			HumanoidArm mainArm = Minecraft.getInstance().options.mainHand().get();
			InteractionHand activeHand = player.getUsedItemHand();

			matrices.pushPose();
			matrices.mulPose(Axis.YP.rotationDegrees(Mth.lerp(tickDelta, entity.yRotO, entity.getYRot()) - 180));
			matrices.mulPose(Axis.XP.rotationDegrees(Mth.lerp(tickDelta, entity.xRotO, entity.getXRot())));
			VertexConsumer vertexConsumer = provider.getBuffer(model.renderType(this.getTexture(entity)));
			model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			matrices.popPose();

			matrices.pushPose();
			boolean rightHandIsActive = (mainArm == HumanoidArm.RIGHT && activeHand == InteractionHand.MAIN_HAND) || (mainArm == HumanoidArm.LEFT && activeHand == InteractionHand.OFF_HAND);
			double bodyYawToRads = Math.toRadians(player.yBodyRot);
			double radius = rightHandIsActive ? -0.4D : 0.4D;
			double startX = player.getX() + radius * Math.cos(bodyYawToRads);
			double startY = player.getY() + (player.getBbHeight() / 3D);
			double startZ = player.getZ() + radius * Math.sin(bodyYawToRads);
			float distanceX = (float) (startX - entity.getX());
			float distanceY = (float) (startY - entity.getY());
			float distanceZ = (float) (startZ - entity.getZ());

			renderChain(distanceX, distanceY, distanceZ, tickDelta, entity.tickCount, matrices, provider, light);
			matrices.popPose();
		}
	}

	public void renderChain(float x, float y, float z, float tickDelta, int age, PoseStack stack, MultiBufferSource provider, int light) {
		float lengthXY = Mth.sqrt(x * x + z * z);
		float squaredLength = x * x + y * y + z * z;
		float length = Mth.sqrt(squaredLength);

		stack.pushPose();
		stack.mulPose(Axis.YP.rotation((float) (-Math.atan2(z, x)) - 1.5707964F));
		stack.mulPose(Axis.XP.rotation((float) (-Math.atan2(lengthXY, y)) - 1.5707964F));
		stack.mulPose(Axis.ZP.rotationDegrees(25));
		stack.pushPose();
		stack.translate(0.015, -0.2, 0);

		VertexConsumer vertexConsumer = provider.getBuffer(CHAIN_LAYER);
		float vertX1 = 0F;
		float vertY1 = 0.25F;
		float vertX2 = Mth.sin(6.2831855F) * 0.125F;
		float vertY2 = Mth.cos(6.2831855F) * 0.125F;
		float minU = 0F;
		float maxU = 0.1875F;
		float minV = 0.0F - ((float) age + tickDelta) * 0.01F;
		float maxV = Mth.sqrt(squaredLength) / 8F - ((float) age + tickDelta) * 0.01F;
		PoseStack.Pose entry = stack.last();
		Matrix4f matrix4f = entry.pose();
		Matrix3f matrix3f = entry.normal();

		vertexConsumer.vertex(matrix4f, vertX1, vertY1, 0F).color(0, 0, 0, 255).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		vertexConsumer.vertex(matrix4f, vertX1, vertY1, length).color(255, 255, 255, 255).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		vertexConsumer.vertex(matrix4f, vertX2, vertY2, length).color(255, 255, 255, 255).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		vertexConsumer.vertex(matrix4f, vertX2, vertY2, 0F).color(0, 0, 0, 255).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();

		stack.popPose();
		stack.mulPose(Axis.ZP.rotationDegrees(90));
		stack.translate(-0.015, -0.2, 0);

		entry = stack.last();
		matrix4f = entry.pose();
		matrix3f = entry.normal();

		vertexConsumer.vertex(matrix4f, vertX1, vertY1, 0F).color(0, 0, 0, 255).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		vertexConsumer.vertex(matrix4f, vertX1, vertY1, length).color(255, 255, 255, 255).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		vertexConsumer.vertex(matrix4f, vertX2, vertY2, length).color(255, 255, 255, 255).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		vertexConsumer.vertex(matrix4f, vertX2, vertY2, 0F).color(0, 0, 0, 255).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();

		stack.popPose();
	}

	@Override
	public ResourceLocation getTexture(HookshotEntity entity) {
		return TEXTURE;
	}
}