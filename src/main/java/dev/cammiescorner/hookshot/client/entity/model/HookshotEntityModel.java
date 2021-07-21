// Made with Model Converter by Globox_Z
// Generate all required imports
package dev.cammiescorner.hookshot.client.entity.model;

import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
public class HookshotEntityModel extends EntityModel<HookshotEntity>
{
	private final ModelPart hookshotBase;
	private final ModelPart hookBase;
	private final ModelPart hook1;
	private final ModelPart hook2;
	private final ModelPart hook3;
	private final ModelPart hook4;

	public HookshotEntityModel(ModelPart root)
	{
		this.hookshotBase = root.getChild("hookshotBase");
		this.hookBase = this.hookshotBase.getChild("hookBase");
		this.hook1 = this.hookBase.getChild("hook1");
		this.hook4 = this.hookBase.getChild("hook4");
		this.hook3 = this.hookBase.getChild("hook3");
		this.hook2 = this.hookBase.getChild("hook2");
	}

	public static TexturedModelData getTexturedModelData()
	{
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData modelPartData1 = modelPartData.addChild("hookshotBase", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData modelPartData2 = modelPartData1.addChild("hookBase", ModelPartBuilder.create().uv(14, 0).cuboid(-2.0F, 5.0F, -6.0F, 2.0F, 2.0F, 2.0F), ModelTransform.pivot(1.0F, -6.0F, 5.0F));
		modelPartData2.addChild("hook2", ModelPartBuilder.create().uv(0, 22).cuboid(-0.5F, -1.0F, -2.9142F, 1.0F, 2.0F, 4.0F), ModelTransform.of(-1.0F, 4.5F, -6.0F, -0.3927F, 0.0F, 0.0F));
		modelPartData2.addChild("hook3", ModelPartBuilder.create().uv(8, 19).cuboid(-0.5F, 0.0F, -3.9142F, 1.0F, 2.0F, 4.0F), ModelTransform.of(-1.0F, 6.5F, -5.0F, 0.3927F, 0.0F, 0.0F));
		modelPartData2.addChild("hook4", ModelPartBuilder.create().uv(18, 0).cuboid(0.0F, -0.5F, -3.0F, 2.0F, 1.0F, 4.0F), ModelTransform.of(-0.5F, 6.0F, -6.0F, 0.0F, -0.3927F, 0.0F));
		modelPartData2.addChild("hook1", ModelPartBuilder.create().uv(0, 17).cuboid(-2.0F, -0.5F, -3.0F, 2.0F, 1.0F, 4.0F), ModelTransform.of(-1.5F, 6.0F, -6.0F, 0.0F, 0.3927F, 0.0F));

		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles (HookshotEntity entity,float limbAngle, float limbDistance, float animationProgress,
						   float headYaw, float headPitch)
	{
		hookshotBase.pitch = (float) Math.toRadians(-headPitch);
		hookshotBase.yaw = (float) Math.toRadians(-headYaw);
	}

	@Override
	public void render (MatrixStack matrices, VertexConsumer vertices,int light, int overlay, float red, float green, float blue, float alpha)
	{
		hookshotBase.render(matrices, vertices, light, overlay);
	}
}