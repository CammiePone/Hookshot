package dev.cammiescorner.hookshot.client.entity.model;

public class HookshotEntityModelOld// extends EntityModel<HookshotEntity>
{
	/*private final ModelPart hookshotBase;
	private final ModelPart hookBase;
	private final ModelPart hook1;
	private final ModelPart hook2;
	private final ModelPart hook3;
	private final ModelPart hook4;

	public HookshotEntityModelOld()
	{
		textureWidth = 32;
		textureHeight = 32;

		hookshotBase = new ModelPart(this);
		hookshotBase.setPivot(0.0F, 0.0F, 0.0F);

		hookBase = new ModelPart(this);
		hookBase.setPivot(1.0F, -6.0F, 5.0F);
		hookshotBase.addChild(hookBase);
		hookBase.setTextureOffset(14, 0).addCuboid(-2.0F, 5.0F, -6.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		hook2 = new ModelPart(this);
		hook2.setPivot(-1.0F, 4.5F, -6.0F);
		hookBase.addChild(hook2);
		setRotationAngle(hook2, -0.3927F, 0.0F, 0.0F);
		hook2.setTextureOffset(0, 22).addCuboid(-0.5F, -1.0F, -2.9142F, 1.0F, 2.0F, 4.0F, 0.0F, false);

		hook3 = new ModelPart(this);
		hook3.setPivot(-1.0F, 6.5F, -5.0F);
		hookBase.addChild(hook3);
		setRotationAngle(hook3, 0.3927F, 0.0F, 0.0F);
		hook3.setTextureOffset(8, 19).addCuboid(-0.5F, 0.0F, -3.9142F, 1.0F, 2.0F, 4.0F, 0.0F, false);

		hook4 = new ModelPart(this);
		hook4.setPivot(-0.5F, 6.0F, -6.0F);
		hookBase.addChild(hook4);
		setRotationAngle(hook4, 0.0F, -0.3927F, 0.0F);
		hook4.setTextureOffset(18, 0).addCuboid(0.0F, -0.5F, -3.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);

		hook1 = new ModelPart(this);
		hook1.setPivot(-1.5F, 6.0F, -6.0F);
		hookBase.addChild(hook1);
		setRotationAngle(hook1, 0.0F, 0.3927F, 0.0F);
		hook1.setTextureOffset(0, 17).addCuboid(-2.0F, -0.5F, -3.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setAngles(HookshotEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		hookshotBase.pitch = (float) Math.toRadians(-headPitch);
		hookshotBase.yaw = (float) Math.toRadians(-headYaw);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		hookshotBase.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart part, float x, float y, float z)
	{
		part.pitch = x;
		part.yaw = y;
		part.roll = z;
	}*/
}