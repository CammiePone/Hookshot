package dev.camcodes.hookshot.client.entity.model;

import dev.camcodes.hookshot.common.entity.HookshotEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
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
		super(RenderLayer::getEntitySolid);
		textureWidth = 16;
		textureHeight = 16;

		hookBase = new ModelPart(this);
		hookBase.setPivot(0.0F, 18.5F, 4.5F);
		hookBase.setTextureOffset(0, 0).addCuboid(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);

		hookTop = new ModelPart(this);
		hookTop.setPivot(0.0F, -0.5F, -6.0F);
		hookBase.addChild(hookTop);
		setRotationAngle(hookTop, -0.7854F, 0.0F, 0.0F);
		hookTop.setTextureOffset(0, 9).addCuboid(-0.5F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

		hookBottom = new ModelPart(this);
		hookBottom.setPivot(0.0F, 0.5F, -6.0F);
		hookBase.addChild(hookBottom);
		setRotationAngle(hookBottom, 0.7854F, 0.0F, 0.0F);
		hookBottom.setTextureOffset(0, 9).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

		hookLeft = new ModelPart(this);
		hookLeft.setPivot(0.5F, 0.0F, -6.0F);
		hookBase.addChild(hookLeft);
		setRotationAngle(hookLeft, 0.0F, -0.7854F, 0.0F);
		hookLeft.setTextureOffset(0, 7).addCuboid(0.0F, -0.5F, 0.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);

		hookRight = new ModelPart(this);
		hookRight.setPivot(-0.5F, 0.0F, -6.0F);
		hookBase.addChild(hookRight);
		setRotationAngle(hookRight, 0.0F, 0.7854F, 0.0F);
		hookRight.setTextureOffset(0, 7).addCuboid(-5.0F, -0.5F, 0.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setAngles(HookshotEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{

	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float alpha)
	{
		hookBase.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart ModelPart, float x, float y, float z)
	{
		ModelPart.pivotX = x;
		ModelPart.pivotY = y;
		ModelPart.pivotZ = z;
	}
}