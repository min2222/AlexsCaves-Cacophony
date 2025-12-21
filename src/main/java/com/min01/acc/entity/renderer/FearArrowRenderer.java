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
	
	public FearArrowRenderer(Context pContext) 
	{
		super(pContext);
		this.model = new ModelFearArrow(pContext.bakeLayer(ModelFearArrow.LAYER_LOCATION));
	}
	
	@Override
	public void render(EntityFearArrow pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) 
	{
		pPoseStack.pushPose();
		pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot())));
        pPoseStack.mulPose(Axis.XN.rotationDegrees(Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot())));
        pPoseStack.translate(0.0F, 1.5F, 0.0F);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
		float f9 = (float)pEntity.shakeTime - pPartialTick;
		if(f9 > 0.0F)
		{
			float f10 = -Mth.sin(f9 * 3.0F) * f9;
			pPoseStack.mulPose(Axis.ZP.rotationDegrees(f10));
		}
		VertexConsumer consumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(pEntity)));
		this.model.renderToBuffer(pPoseStack, consumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		pPoseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityFearArrow pEntity)
	{
		return ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/arrow_of_fear.png");
	}
}
