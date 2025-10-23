package com.min01.acc.entity.model;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.animation.NeodymiumShackleAnimation;
import com.min01.acc.entity.projectile.EntityNeodymiumShackle;
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

public class ModelNeodymiumShackle extends HierarchicalModel<EntityNeodymiumShackle>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(AlexsCavesCacophony.MODID, "neodymium_shackle"), "main");
	private final ModelPart root;

	public ModelNeodymiumShackle(ModelPart root)
	{
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition shackle = root.addOrReplaceChild("shackle", CubeListBuilder.create().texOffs(-3, 3).addBox(-8.0F, 0.0F, -1.5F, 16.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -1.5F, 0.0F, 16.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		shackle.addOrReplaceChild("right_weight", CubeListBuilder.create().texOffs(0, 6).addBox(-8.0F, -3.0F, -3.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, 0.0F));

		shackle.addOrReplaceChild("left_weight", CubeListBuilder.create().texOffs(0, 6).mirror().addBox(0.0F, -3.0F, -3.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(8.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(EntityNeodymiumShackle entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);

		entity.loopAnimationState.animate(this, NeodymiumShackleAnimation.SHACKLE_LOOP, ageInTicks);
		entity.deployAnimationState.animate(this, NeodymiumShackleAnimation.SHACKLE_DEPLOY, ageInTicks);
		entity.impactAnimationState.animate(this, NeodymiumShackleAnimation.SHACKLE_IMPACT, ageInTicks);
		entity.impactCloseAnimationState.animate(this, NeodymiumShackleAnimation.SHACKLE_IMPACT_CLOSE, ageInTicks);
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