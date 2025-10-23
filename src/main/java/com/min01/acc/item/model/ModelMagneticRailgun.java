package com.min01.acc.item.model;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.MagneticRailgunItem;
import com.min01.acc.item.animation.MagneticRailgunAnimation;
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

		PartDefinition railgun = root.addOrReplaceChild("railgun", CubeListBuilder.create().texOffs(0, 48).addBox(-1.5F, -3.0F, -6.0F, 3.0F, 4.0F, 7.0F, new CubeDeformation(0.01F))
		.texOffs(32, 16).addBox(-1.5F, -3.0F, -10.0F, 3.0F, 4.0F, 11.0F, new CubeDeformation(0.1F))
		.texOffs(20, 53).addBox(0.0F, 0.0F, -2.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.0F));

		railgun.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(52, 53).addBox(-1.5F, -2.0F, -2.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1895F, 2.3123F, 1.0908F, 0.0F, 0.0F));

		PartDefinition rail = railgun.addOrReplaceChild("rail", CubeListBuilder.create().texOffs(32, 31).addBox(-1.5F, -2.0F, -7.0F, 3.0F, 4.0F, 9.0F, new CubeDeformation(0.011F))
		.texOffs(0, 0).addBox(-1.0F, -2.0F, -21.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-1.0F, -2.0F, -21.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.09F)), PartPose.offset(0.0F, -1.0F, -8.0F));

		rail.addOrReplaceChild("lower", CubeListBuilder.create().texOffs(31, 44).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-1.0F, -1.0F, -18.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-1.0F, -1.0F, -18.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.09F)), PartPose.offset(0.0F, 3.0F, -3.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(ItemStack stack, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(stack, MagneticRailgunItem.RAILGUN_BASE, MagneticRailgunAnimation.RAILGUN_BASE, ageInTicks);
		this.animate(stack, MagneticRailgunItem.RAILGUN_ACTIVATE, MagneticRailgunAnimation.RAILGUN_ACTIVATE, ageInTicks);
		this.animate(stack, MagneticRailgunItem.RAILGUN_ACTIVE, MagneticRailgunAnimation.RAILGUN_ACTIVE, ageInTicks);
		this.animate(stack, MagneticRailgunItem.RAILGUN_FIRE, MagneticRailgunAnimation.RAILGUN_FIRE, ageInTicks);
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