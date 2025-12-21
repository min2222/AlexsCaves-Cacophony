package com.min01.acc.entity.renderer;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.model.ModelRadrifleBeam;
import com.min01.acc.entity.model.ModelRadrifleBeamEnd;
import com.min01.acc.entity.model.ModelRadrifleSuperBeam;
import com.min01.acc.entity.projectile.EntityRadrifleBeam;
import com.min01.acc.misc.ACCRenderType;
import com.min01.acc.util.ACCUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

public class RadrifleBeamRenderer extends EntityRenderer<EntityRadrifleBeam>
{
	private final ModelRadrifleBeam beamSegment;
	private final ModelRadrifleBeamEnd beamEnd;
	private final ModelRadrifleSuperBeam superBeam;
	
	public RadrifleBeamRenderer(Context pContext)
	{
		super(pContext);
		this.beamSegment = new ModelRadrifleBeam(pContext.bakeLayer(ModelRadrifleBeam.LAYER_LOCATION));
		this.beamEnd = new ModelRadrifleBeamEnd(pContext.bakeLayer(ModelRadrifleBeamEnd.LAYER_LOCATION));
		this.superBeam = new ModelRadrifleSuperBeam(pContext.bakeLayer(ModelRadrifleSuperBeam.LAYER_LOCATION));
	}
	
	@Override
	public void render(EntityRadrifleBeam pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) 
	{
		float tick = pEntity.tickCount * 0.08F;
		float alpha = Math.max(1.0F - tick, 0.0F);
		if(pEntity.isEnd())
		{
			pPoseStack.pushPose();
			pPoseStack.mulPose(pEntity.getEndDir().getRotation());
			pPoseStack.scale(-1.0F, -1.0F, 1.0F);
			pPoseStack.scale(1.0F + tick, 1.0F + tick, 1.0F + tick);
			pPoseStack.translate(0.0F, -1.501F, 0.0F);
			this.beamEnd.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
			this.beamEnd.renderToBuffer(pPoseStack, pBuffer.getBuffer(ACCRenderType.eyesFix(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 0.3F, 0.3F, 0.3F, alpha);
			pPoseStack.popPose();
		}
		else if(pEntity.isOvercharge())
		{
			float length = (float) pEntity.position().distanceTo(pEntity.getEndPos());
			Vec2 rot = ACCUtil.lookAt(pEntity.position(), pEntity.getEndPos());
			pPoseStack.pushPose();
			pPoseStack.mulPose(Axis.YP.rotationDegrees(-rot.y + 180.0F));
			pPoseStack.mulPose(Axis.XP.rotationDegrees(-rot.x));
			pPoseStack.scale(-1.0F, -1.0F, 1.0F);
			pPoseStack.translate(0.0F, -1.5F, -(length / 2.0F));
			pPoseStack.scale(1.0F, 1.0F, length);
			this.superBeam.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
			this.superBeam.renderToBuffer(pPoseStack, pBuffer.getBuffer(ACCRenderType.eyesFix(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 0.3F, 0.3F, 0.3F, alpha);
			pPoseStack.popPose();
		}
		else
		{
			float length = (float) pEntity.position().distanceTo(pEntity.getEndPos());
			Vec2 rot = ACCUtil.lookAt(pEntity.position(), pEntity.getEndPos());
			pPoseStack.pushPose();
			pPoseStack.mulPose(Axis.YP.rotationDegrees(-rot.y + 180.0F));
			pPoseStack.mulPose(Axis.XP.rotationDegrees(-rot.x));
			pPoseStack.scale(-1.0F, -1.0F, 1.0F);
			pPoseStack.translate(0.0F, -1.5F, -(length / 2.0F));
			pPoseStack.scale(1.0F, 1.0F, length);
			this.beamSegment.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
			this.beamSegment.renderToBuffer(pPoseStack, pBuffer.getBuffer(ACCRenderType.eyesFix(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 0.3F, 0.3F, 0.3F, alpha);
			pPoseStack.popPose();
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityRadrifleBeam pEntity) 
	{
		if(pEntity.isOvercharge())
		{
			return ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/radrifle_beam_segment_overcharged.png");
		}
		if(pEntity.isEnd())
		{
			if(pEntity.isGamma())
			{
				return ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/radrifle_beam_end_gamma.png");
			}
			return ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/radrifle_beam_end.png");
		}
		return pEntity.isGamma() ? ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/radrifle_beam_segment_gamma.png") : ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/radrifle_beam_segment.png");
	}
}
