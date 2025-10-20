package com.min01.acc.item.model;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.RadrifleItem;
import com.min01.acc.item.animation.RadrifleAnimation;
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

public class ModelRadrifle extends HierarchicalItemModel
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(AlexsCavesCacophony.MODID, "radrifle"), "main");
	private final ModelPart root;

	public ModelRadrifle(ModelPart root) 
	{
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition radrifle = root.addOrReplaceChild("radrifle", CubeListBuilder.create().texOffs(10, 41).addBox(-1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -3.0F, -14.0F, 2.0F, 3.0F, 22.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-1.0F, 0.0F, -14.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 41).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(32, 25).addBox(-1.0F, -3.0F, -14.0F, 2.0F, 5.0F, 11.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -1.0F, 1.0F));

		radrifle.addOrReplaceChild("right_charger", CubeListBuilder.create().texOffs(20, 41).addBox(-1.01F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 41).addBox(-2.0F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -5.0F));

		radrifle.addOrReplaceChild("left_charger", CubeListBuilder.create().texOffs(30, 41).mirror().addBox(1.0F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(20, 41).mirror().addBox(-1.99F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -1.0F, -5.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(ItemStack stack, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(stack, RadrifleItem.RADRIFLE_OVERHEAT, RadrifleAnimation.OVERHEAT, ageInTicks);
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