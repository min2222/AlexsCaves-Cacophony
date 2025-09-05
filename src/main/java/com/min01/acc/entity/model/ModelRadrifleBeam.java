package com.min01.acc.entity.model;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.projectile.EntityRadrifleBeam;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelRadrifleBeam extends EntityModel<EntityRadrifleBeam>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(AlexsCavesCacophony.MODID, "radrifle_beam"), "main");
	private final ModelPart beam;

	public ModelRadrifleBeam(ModelPart root)
	{
		this.beam = root.getChild("beam");
	}

	public static LayerDefinition createBodyLayer() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition beam = partdefinition.addOrReplaceChild("beam", CubeListBuilder.create().texOffs(-16, 0).addBox(-1.5F, 0.0F, -16.0F, 3.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 8.0F));

		beam.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-16, 0).addBox(-1.5F, 0.0F, -8.0F, 3.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.0F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(EntityRadrifleBeam entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		beam.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}