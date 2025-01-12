// Made with Model Converter by Globox_Z
// Generate all required imports
package dev.cammiescorner.hookshot.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.hookshot.entity.HookshotEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class HookshotEntityModel extends EntityModel<HookshotEntity> {
	private final ModelPart hookshotBase;
	private final ModelPart hookBase;
	private final ModelPart hook1;
	private final ModelPart hook2;
	private final ModelPart hook3;
	private final ModelPart hook4;

	public HookshotEntityModel(ModelPart root) {
		this.hookshotBase = root.getChild("hookshotBase");
		this.hookBase = this.hookshotBase.getChild("hookBase");
		this.hook1 = this.hookBase.getChild("hook1");
		this.hook4 = this.hookBase.getChild("hook4");
		this.hook3 = this.hookBase.getChild("hook3");
		this.hook2 = this.hookBase.getChild("hook2");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition modelPartData1 = modelPartData.addOrReplaceChild("hookshotBase", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition modelPartData2 = modelPartData1.addOrReplaceChild("hookBase", CubeListBuilder.create().texOffs(14, 0).addBox(-2.0F, 5.0F, -6.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(1.0F, -6.0F, 5.0F));
		modelPartData2.addOrReplaceChild("hook2", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, -1.0F, -2.9142F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(-1.0F, 4.5F, -6.0F, -0.3927F, 0.0F, 0.0F));
		modelPartData2.addOrReplaceChild("hook3", CubeListBuilder.create().texOffs(8, 19).addBox(-0.5F, 0.0F, -3.9142F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(-1.0F, 6.5F, -5.0F, 0.3927F, 0.0F, 0.0F));
		modelPartData2.addOrReplaceChild("hook4", CubeListBuilder.create().texOffs(18, 0).addBox(0.0F, -0.5F, -3.0F, 2.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(-0.5F, 6.0F, -6.0F, 0.0F, -0.3927F, 0.0F));
		modelPartData2.addOrReplaceChild("hook1", CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -0.5F, -3.0F, 2.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(-1.5F, 6.0F, -6.0F, 0.0F, 0.3927F, 0.0F));

		return LayerDefinition.create(modelData, 32, 32);
	}

	@Override
	public void setupAnim(HookshotEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hookshotBase.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
