package com.min01.acc.entity.renderer;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.model.ModelNeodymiumShackle;
import com.min01.acc.entity.projectile.EntityNeodymiumShackle;
import com.min01.acc.misc.ACCRenderType;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class NeodymiumShackleRenderer extends EntityRenderer<EntityNeodymiumShackle>
{
	public static final ResourceLocation TEXTURE_BLUE = ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/neodymium_shackle_blue.png");
	public static final ResourceLocation TEXTURE_RED = ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/neodymium_shackle_red.png");
	public final ModelNeodymiumShackle model;
	
	public NeodymiumShackleRenderer(Context pContext)
	{
		super(pContext);
		this.model = new ModelNeodymiumShackle(pContext.bakeLayer(ModelNeodymiumShackle.LAYER_LOCATION));
	}
	
	@Override
	public void render(EntityNeodymiumShackle pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) 
	{
		pPoseStack.pushPose();
		pPoseStack.scale(-1.0F, -1.0F, 1.0F);
		pPoseStack.translate(0.0F, -1.5F, 0.0F);
		this.model.setupAnim(pEntity, 0, 0, pEntity.tickCount + pPartialTick, 0, 0);
		this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		pPoseStack.popPose();
		
		pPoseStack.pushPose();
		pPoseStack.scale(-1.0F, -1.0F, 1.0F);
		pPoseStack.translate(0.0F, -1.5F, 0.0F);
		this.model.setupAnim(pEntity, 0, 0, pEntity.tickCount + pPartialTick, 0, 0);
		this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(ACCRenderType.eyesFix(TEXTURE_RED)), pPackedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		pPoseStack.popPose();
		
		pPoseStack.pushPose();
		pPoseStack.scale(-1.0F, -1.0F, 1.0F);
		pPoseStack.translate(0.0F, -1.5F, 0.0F);
		this.model.setupAnim(pEntity, 0, 0, pEntity.tickCount + pPartialTick, 0, 0);
		this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(ACCRenderType.eyesFix(TEXTURE_BLUE)), pPackedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		pPoseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityNeodymiumShackle pEntity)
	{
		return ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/neodymium_shackle.png");
	}
}
