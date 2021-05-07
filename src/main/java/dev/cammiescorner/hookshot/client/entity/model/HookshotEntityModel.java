package dev.cammiescorner.hookshot.client.entity.model;

import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class HookshotEntityModel extends EntityModel<HookshotEntity>
{
	private final ModelPart hookBase;
	private final ModelPart hookTop;
	private final ModelPart hookBottom;
	private final ModelPart hookLeft;
	private final ModelPart hookRight;

	public HookshotEntityModel()
	{
		textureWidth = 16;
		textureHeight = 16;

		hookBase = new ModelPart(this);
		hookBase.setPivot(-0.5f, 23.5f, 0.5F);
		hookBase.setTextureOffset(1, 2).addCuboid(-0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 6.0f, 0.0f, false);

		hookTop = new ModelPart(this);
		hookTop.setPivot(0.5f, -0.5f, -0.5F);
		setRotationAngle(hookTop, -0.7854f, 0.0f, 0.0F);
		hookTop.setTextureOffset(0, 0).addCuboid(-1.0f, -5.0f, 0.0f, 1.0f, 5.0f, 1.0f, 0.0f, false);

		hookBottom = new ModelPart(this);
		hookBottom.setPivot(0.5f, 0.5f, -0.5F);
		setRotationAngle(hookBottom, -2.3562f, 0.0f, 0.0F);
		hookBottom.setTextureOffset(12, 0).addCuboid(-1.0f, -5.0f, -1.0f, 1.0f, 5.0f, 1.0f, 0.0f, false);

		hookLeft = new ModelPart(this);
		hookLeft.setPivot(-0.5f, 0.5f, -0.5F);
		setRotationAngle(hookLeft, 0.0f, 0.7854f, 0.0F);
		hookLeft.setTextureOffset(2, 11).addCuboid(-5.0f, -1.0f, 0.0f, 5.0f, 1.0f, 1.0f, 0.0f, false);

		hookRight = new ModelPart(this);
		hookRight.setPivot(0.5f, 0.5f, -0.5F);
		setRotationAngle(hookRight, 0.0f, -0.7854f, 0.0F);
		hookRight.setTextureOffset(2, 14).addCuboid(0.0f, -1.0f, 0.0f, 5.0f, 1.0f, 1.0f, 0.0f, false);

		hookBase.addChild(hookTop);
		hookBase.addChild(hookBottom);
		hookBase.addChild(hookLeft);
		hookBase.addChild(hookRight);
	}

	@Override
	public void setAngles(HookshotEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch)
	{
		hookBase.yaw = yaw;
		hookBase.pitch = pitch;
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a)
	{
		hookBase.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart ModelPart, float x, float y, float z) {
		ModelPart.pitch = x;
		ModelPart.yaw = y;
		ModelPart.roll = z;
	}
}