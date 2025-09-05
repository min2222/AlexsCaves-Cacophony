package com.min01.acc.entity.renderer;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.model.ModelRadrifleBeam;
import com.min01.acc.entity.model.ModelRadrifleBeamEnd;
import com.min01.acc.entity.projectile.EntityRadrifleBeam;
import com.min01.acc.misc.ACCRenderType;
import com.min01.acc.util.ACCUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

public class RadrifleBeamRenderer extends EntityRenderer<EntityRadrifleBeam>
{
	private final ModelRadrifleBeam beamSegment;
	private final ModelRadrifleBeamEnd beamEnd;
	
	public RadrifleBeamRenderer(Context p_174008_)
	{
		super(p_174008_);
		this.beamSegment = new ModelRadrifleBeam(p_174008_.bakeLayer(ModelRadrifleBeam.LAYER_LOCATION));
		this.beamEnd = new ModelRadrifleBeamEnd(p_174008_.bakeLayer(ModelRadrifleBeamEnd.LAYER_LOCATION));
	}
	
	@Override
	public void render(EntityRadrifleBeam p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) 
	{
		if(p_114485_.isEnd())
		{
			p_114488_.pushPose();
			p_114488_.mulPose(p_114485_.getEndDir().getRotation());
			p_114488_.scale(-1.0F, -1.0F, 1.0F);
			p_114488_.translate(0.0F, -1.501F, 0.0F);
			this.beamEnd.renderToBuffer(p_114488_, p_114489_.getBuffer(ACCRenderType.eyesFix(this.getTextureLocation(p_114485_))), p_114490_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			p_114488_.popPose();
		}
		else
		{
			float length = (float) p_114485_.position().distanceTo(p_114485_.getEndPos());
			Vec2 rot = ACCUtil.lookAt(p_114485_.position(), p_114485_.getEndPos());
			p_114488_.pushPose();
			p_114488_.mulPose(Axis.YP.rotationDegrees(-rot.y + 180.0F));
			p_114488_.mulPose(Axis.XP.rotationDegrees(-rot.x));
			p_114488_.scale(-1.0F, -1.0F, 1.0F);
			p_114488_.translate(0.0F, -1.5F, -(length / 2.0F));
			p_114488_.scale(1.0F, 1.0F, length);
			this.beamSegment.renderToBuffer(p_114488_, p_114489_.getBuffer(ACCRenderType.eyesFix(this.getTextureLocation(p_114485_))), p_114490_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			p_114488_.popPose();
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityRadrifleBeam p_114482_) 
	{
		if(p_114482_.isEnd())
		{
			if(p_114482_.isGamma())
			{
				return new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/radrifle_beam_end_gamma.png");
			}
			return new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/radrifle_beam_end.png");
		}
		return p_114482_.isGamma() ? new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/radrifle_beam_segment_gamma.png") : new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/radrifle_beam_segment.png");
	}
}
