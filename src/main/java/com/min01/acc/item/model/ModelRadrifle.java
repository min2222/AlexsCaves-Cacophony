package com.min01.acc.item.model;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.RadrifleItem;
import com.min01.acc.item.animation.RadrifleAnimation;
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

		PartDefinition rifle = root.addOrReplaceChild("rifle", CubeListBuilder.create().texOffs(24, 21).addBox(-3.5F, -2.0F, 0.0F, 7.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-3.5F, -2.0F, 0.0F, 7.0F, 4.0F, 7.0F, new CubeDeformation(0.1F))
		.texOffs(0, 21).addBox(-1.5F, -2.0F, -18.0F, 3.0F, 3.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.5F, -2.0F, -18.2F, 3.0F, 3.0F, 18.0F, new CubeDeformation(0.1F))
		.texOffs(10, 4).addBox(0.0F, 1.0F, -4.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 11).addBox(-1.5F, 2.0F, 5.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

		rifle.addOrReplaceChild("rods", CubeListBuilder.create().texOffs(0, 21).addBox(-3.5F, -2.0F, -0.8F, 7.0F, 4.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(0, 8).mirror().addBox(-2.5F, -1.0F, -6.8F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-2.5F, -1.0F, -6.8F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(0, 8).addBox(0.5F, -1.0F, -6.8F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.5F, -1.0F, -6.8F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 14.0F));

		PartDefinition beam = root.addOrReplaceChild("beam", CubeListBuilder.create().texOffs(31, 32).addBox(0.0F, -2.0F, -16.0F, 0.0F, 3.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(23, 32).addBox(-1.5F, -0.5F, -16.0F, 3.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -17.0F));

		beam.addOrReplaceChild("beam_end", CubeListBuilder.create().texOffs(45, 0).addBox(-4.0F, -4.0F, 0.01F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -16.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(ItemStack stack, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(ACCUtil.getItemAnimationState(stack, RadrifleItem.RADRIFLE_FIRE), RadrifleAnimation.RADRIFLE_FIRE, ageInTicks);
		this.root.getChild("beam").visible = ACCUtil.getAnimationTick(stack) >= 8;
		this.root.getChild("beam").zScale = RadrifleItem.getBeamLength(stack);
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