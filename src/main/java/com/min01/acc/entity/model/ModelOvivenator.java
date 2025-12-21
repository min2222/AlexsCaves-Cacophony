package com.min01.acc.entity.model;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.animation.OvivenatorAnimation;
import com.min01.acc.entity.living.EntityOvivenator;
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

public class ModelOvivenator extends HierarchicalModel<EntityOvivenator>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "ovivenator"), "main");
	private final ModelPart root;

	public ModelOvivenator(ModelPart root)
	{
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition ovivenator = root.addOrReplaceChild("ovivenator", CubeListBuilder.create(), PartPose.offset(0.0F, -19.0F, 0.0F));

		PartDefinition body = ovivenator.addOrReplaceChild("body", CubeListBuilder.create().texOffs(35, 12).addBox(-4.5F, -6.0F, -5.0F, 9.0F, 12.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(64, 57).mirror().addBox(-1.0F, -1.0F, -2.25F, 2.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 21).mirror().addBox(1.0F, -1.0F, 1.75F, 0.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 35).mirror().addBox(1.0F, 12.0F, -8.25F, 0.0F, 6.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(66, 15).mirror().addBox(-1.0F, 7.0F, -5.25F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-3.0F, 7.0F, -8.25F, 4.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 0.0F, -4.0F));

		body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(64, 57).addBox(-1.0F, -1.0F, -2.25F, 2.0F, 13.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-1.0F, -1.0F, 1.75F, 0.0F, 13.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 35).addBox(-1.0F, 12.0F, -8.25F, 0.0F, 6.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(66, 15).addBox(-1.0F, 7.0F, -5.25F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, 7.0F, -8.25F, 4.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, -4.0F));

		body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-4.5F, -2.0F, 8.0F, 9.0F, 8.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(64, 37).addBox(-2.0F, -11.0F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(41, 41).addBox(-4.0F, -5.0F, -6.0F, 8.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, -3.0F, -9.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(66, 3).addBox(-2.0F, -15.0F, -1.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -5.0F));

		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(30, 0).addBox(-3.5F, -6.0F, -4.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(78, 1).addBox(-4.5F, -5.0F, -3.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(78, 1).mirror().addBox(3.5F, -5.0F, -3.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 46).addBox(0.0F, -11.0F, -1.0F, 0.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(42, 57).addBox(-1.5F, -7.0F, -9.0F, 3.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(7, 37).addBox(-1.5F, -2.0F, -9.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 1.0F));

		head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(30, 12).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -4.0F));

		PartDefinition left_leg = ovivenator.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(18, 49).addBox(-1.5F, -1.0F, -3.0F, 4.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 5.0F));

		PartDefinition left_leg_segment = left_leg.addOrReplaceChild("left_leg_segment", CubeListBuilder.create().texOffs(0, 63).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 7.0F, 3.0F));

		left_leg_segment.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(49, 0).addBox(-3.5F, 0.0F, -5.5F, 7.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.5F));

		PartDefinition right_leg = ovivenator.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(18, 49).mirror().addBox(-2.5F, -1.0F, -3.0F, 4.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 0.0F, 5.0F));

		PartDefinition right_leg_segment = right_leg.addOrReplaceChild("right_leg_segment", CubeListBuilder.create().texOffs(0, 63).mirror().addBox(-1.5F, 0.0F, -1.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.5F, 7.0F, 3.0F));

		right_leg_segment.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(49, 0).mirror().addBox(-3.5F, 0.0F, -5.5F, 7.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 12.0F, 0.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(EntityOvivenator entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		ACCClientUtil.animateHead(this.root.getChild("ovivenator").getChild("body").getChild("neck").getChild("head"), netHeadYaw, headPitch);
		entity.idleAnimationState.animate(this, OvivenatorAnimation.IDLE, ageInTicks, limbSwingAmount);
		entity.idleWithEggAnimationState.animate(this, OvivenatorAnimation.IDLE_WITH_EGG, ageInTicks, limbSwingAmount);
		entity.holdAnimationState.animate(this, OvivenatorAnimation.HOLD, ageInTicks);
		entity.noiseAnimationState.animate(this, OvivenatorAnimation.NOISE, ageInTicks);
		entity.scratchRightAnimationState.animate(this, OvivenatorAnimation.SCRATCH_RIGHT, ageInTicks);
		entity.scratchLeftAnimationState.animate(this, OvivenatorAnimation.SCRATCH_LEFT, ageInTicks);
		entity.lookAnimationState.animate(this, OvivenatorAnimation.LOOK, ageInTicks);
		entity.danceAnimationState.animate(this, OvivenatorAnimation.DANCE, ageInTicks);
		entity.pickupAnimationState.animate(this, OvivenatorAnimation.PICK_UP_ITEM, ageInTicks);
		entity.eatAnimationState.animate(this, OvivenatorAnimation.EAT_ITEM, ageInTicks);
		entity.sitAnimationState.animate(this, OvivenatorAnimation.SIT, ageInTicks);
		
		float factor = entity.runAnimationState.factor(ACCClientUtil.MC.getFrameTime());
		
		this.animateWalk(OvivenatorAnimation.WALK, limbSwing, limbSwingAmount * factor, 2.5F, 2.5F);
		this.animateWalk(OvivenatorAnimation.RUN, limbSwing, Math.max(limbSwingAmount - factor, 0.0F), 1.0F, 1.0F);
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