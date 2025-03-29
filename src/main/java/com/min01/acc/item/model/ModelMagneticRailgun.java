package com.min01.acc.item.model;

import com.min01.acc.AlexsCavesCacophony;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ModelMagneticRailgun extends HierarchicalItemModel
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(AlexsCavesCacophony.MODID, "magnetic_railgun"), "main");
	private final ModelPart root;

	public ModelMagneticRailgun(ModelPart root)
	{
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition railgun = root.addOrReplaceChild("railgun", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.0F, -10.0F, 7.0F, 3.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-3.5F, -3.0F, -10.0F, 7.0F, 3.0F, 11.0F, new CubeDeformation(0.1F))
		.texOffs(0, 28).addBox(-3.5F, -3.0F, -23.0F, 2.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(-3.5F, -3.0F, -22.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.1F))
		.texOffs(30, 28).addBox(1.5F, -3.0F, -23.0F, 2.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(14, 44).addBox(1.5F, -3.0F, -22.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.1F))
		.texOffs(36, 0).addBox(0.0F, 0.0F, -2.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.0F));

		railgun.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 11).addBox(-1.5F, -2.0F, -2.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1895F, 2.3123F, 1.0908F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(ItemStack stack, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
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