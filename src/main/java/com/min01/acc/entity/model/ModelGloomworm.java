package com.min01.acc.entity.model;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.animation.GloomwormAnimation;
import com.min01.acc.entity.living.EntityGloomworm;
import com.min01.acc.util.ACCClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelGloomworm extends HierarchicalModel<EntityGloomworm>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(AlexsCavesCacophony.MODID, "gloomworm"), "main");
	private final ModelPart root;

	public ModelGloomworm(ModelPart root)
	{
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -1.5F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-4.0F, -3.0F, -1.0F, 8.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-4.0F, -3.0F, 3.0F, 8.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -4.0F));

		PartDefinition abdomen = body.addOrReplaceChild("abdomen", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(20, 26).addBox(-3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(20, 26).addBox(-3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 3.0F));

		abdomen.addOrReplaceChild("legs3", CubeListBuilder.create().texOffs(13, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 4.0F));

		body.addOrReplaceChild("legs", CubeListBuilder.create().texOffs(13, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

		body.addOrReplaceChild("legs2", CubeListBuilder.create().texOffs(13, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 2.0F));

		body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 10).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -1.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(EntityGloomworm entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animateWalk(GloomwormAnimation.GLOOMWORM_CRAWL, limbSwing, limbSwingAmount, 2.5F, 2.5F);
		ACCClientUtil.animateHead(this.root.getChild("body").getChild("head"), netHeadYaw, headPitch);
	}
	
	@Override
	public ModelPart root() 
	{
		return this.root;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}