package com.min01.acc.entity.renderer;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.model.ModelFearArrow;
import com.min01.acc.entity.projectile.EntityFearArrow;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FearArrowRenderer extends EntityRenderer<EntityFearArrow>
{
	private final ModelFearArrow model;
	
	public FearArrowRenderer(Context p_174008_) 
	{
		super(p_174008_);
		this.model = new ModelFearArrow(p_174008_.bakeLayer(ModelFearArrow.LAYER_LOCATION));
	}
	
	@Override
	public void render(EntityFearArrow p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) 
	{
		p_114488_.pushPose();
		p_114488_.mulPose(Axis.YP.rotationDegrees(Mth.lerp(p_114487_, p_114485_.yRotO, p_114485_.getYRot())));
        p_114488_.mulPose(Axis.XN.rotationDegrees(Mth.lerp(p_114487_, p_114485_.xRotO, p_114485_.getXRot())));
        p_114488_.translate(0.0F, 1.5F, 0.0F);
        p_114488_.mulPose(Axis.XP.rotationDegrees(180.0F));
		float f9 = (float)p_114485_.shakeTime - p_114487_;
		if(f9 > 0.0F)
		{
			float f10 = -Mth.sin(f9 * 3.0F) * f9;
			p_114488_.mulPose(Axis.ZP.rotationDegrees(f10));
		}
		VertexConsumer consumer = p_114489_.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(p_114485_)));
		this.model.renderToBuffer(p_114488_, consumer, p_114490_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		p_114488_.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityFearArrow p_114482_)
	{
		return new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/arrow_of_fear.png");
	}
}
