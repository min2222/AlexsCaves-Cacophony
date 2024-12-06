package com.min01.acc.item.model;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.RaybladeItem;
import com.min01.acc.item.animation.RaybladeAnimation;
import com.min01.acc.util.ACCUtil;
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

public class ModelRayblade extends HierarchicalItemModel
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(AlexsCavesCacophony.MODID, "rayblade"), "main");
	private final ModelPart handle;

	public ModelRayblade(ModelPart root) 
	{
		this.handle = root.getChild("handle");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition handle = partdefinition.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.0F, 29.0F, 0.0F));

		PartDefinition hilt = handle.addOrReplaceChild("hilt", CubeListBuilder.create().texOffs(20, 5).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(14, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(0, 0).addBox(-2.0F, -2.0F, -1.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.1F))
		.texOffs(11, 11).addBox(-1.5F, 5.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, 5.0F, -3.5F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		hilt.addOrReplaceChild("guard1", CubeListBuilder.create().texOffs(22, 0).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 7).addBox(-0.5F, -3.0F, 0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

		hilt.addOrReplaceChild("guard2", CubeListBuilder.create().texOffs(22, 0).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 7).addBox(-0.5F, -3.0F, -2.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));

		handle.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -29.0F, -8.5F, 0.0F, 28.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-0.5F, -29.0F, -1.5F, 1.0F, 27.0F, 3.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(ItemStack stack, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(ACCUtil.getItemAnimationState(stack, RaybladeItem.RAYBLADE_SWING), RaybladeAnimation.RAYBLADE_SWING, ageInTicks);
		this.handle.getChild("blade").visible = ACCUtil.isVisible(stack);
	}
	
	@Override
	public ModelPart root()
	{
		return this.handle;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		handle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}