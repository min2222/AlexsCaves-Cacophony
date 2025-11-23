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

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(41, 22).addBox(-1.5F, 2.0F, -5.0F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(30, 26).addBox(-2.0F, -1.0F, -6.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.01F))
		.texOffs(46, 30).addBox(1.0F, -1.0F, -5.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.01F))
		.texOffs(37, 30).addBox(-2.0F, -1.0F, -5.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.01F))
		.texOffs(54, 30).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.01F))
		.texOffs(32, 0).addBox(-2.0F, -1.0F, -10.0F, 4.0F, 7.0F, 12.0F, new CubeDeformation(0.01F))
		.texOffs(11, 48).addBox(-2.0F, 2.0F, -10.01F, 4.0F, 4.0F, 12.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, 21.0F, 7.0F));

		root.addOrReplaceChild("rail", CubeListBuilder.create().texOffs(4, 9).addBox(-2.0F, -2.0F, -13.2F, 4.0F, 5.0F, 9.0F, new CubeDeformation(0.1F))
		.texOffs(0, 23).addBox(-2.0F, -2.0F, -13.0F, 4.0F, 5.0F, 13.0F, new CubeDeformation(0.01F))
		.texOffs(38, 38).addBox(-1.5F, 3.0F, -10.0F, 3.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -10.0F));

		PartDefinition cartridge = root.addOrReplaceChild("cartridge", CubeListBuilder.create().texOffs(0, 58).addBox(-0.5F, -1.5F, -1.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.5F, -4.0F));

		cartridge.addOrReplaceChild("ammo", CubeListBuilder.create().texOffs(54, 34).addBox(0.5F, -1.5F, -2.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		root.addOrReplaceChild("left_charger", CubeListBuilder.create().texOffs(45, 60).mirror().addBox(-0.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offset(0.0F, 1.0F, -1.0F));

		root.addOrReplaceChild("right_charger", CubeListBuilder.create().texOffs(45, 60).addBox(-2.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, 1.0F, -1.0F));

		root.addOrReplaceChild("right_charger2", CubeListBuilder.create().texOffs(45, 60).addBox(-3.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(1.0F, 1.0F, -8.0F));

		root.addOrReplaceChild("left_charger2", CubeListBuilder.create().texOffs(45, 60).mirror().addBox(-0.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offset(0.0F, 1.0F, -8.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(ItemStack stack, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(stack, MagneticRailgunItem.RAILGUN_RELOAD, MagneticRailgunAnimation.RAILGUN_RELOAD, ageInTicks / 2.0F);
		this.animate(stack, MagneticRailgunItem.RAILGUN_CHARGE, MagneticRailgunAnimation.RAILGUN_CHARGE, ageInTicks);
		this.animate(stack, MagneticRailgunItem.RAILGUN_FIRE, MagneticRailgunAnimation.RAILGUN_FIRE, ageInTicks / 2.0F);
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