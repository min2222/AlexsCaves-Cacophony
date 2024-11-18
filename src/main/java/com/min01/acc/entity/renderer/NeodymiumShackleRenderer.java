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
	public static final ResourceLocation TEXTURE_BLUE = new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/neodymium_shackle_blue.png");
	public static final ResourceLocation TEXTURE_RED = new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/neodymium_shackle_red.png");
	public final ModelNeodymiumShackle model;
	
	public NeodymiumShackleRenderer(Context p_174008_)
	{
		super(p_174008_);
		this.model = new ModelNeodymiumShackle(p_174008_.bakeLayer(ModelNeodymiumShackle.LAYER_LOCATION));
	}
	
	@Override
	public void render(EntityNeodymiumShackle p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) 
	{
		p_114488_.pushPose();
		p_114488_.scale(-1.0F, -1.0F, 1.0F);
		p_114488_.translate(0.0F, -1.5F, 0.0F);
		this.model.setupAnim(p_114485_, 0, 0, p_114485_.tickCount + p_114487_, 0, 0);
		this.model.renderToBuffer(p_114488_, p_114489_.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(p_114485_))), p_114490_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		this.model.renderToBuffer(p_114488_, p_114489_.getBuffer(ACCRenderType.eyesFix(TEXTURE_BLUE)), p_114490_, OverlayTexture.NO_OVERLAY, 0.8F, 0.8F, 0.8F, 1.0F);
		this.model.renderToBuffer(p_114488_, p_114489_.getBuffer(ACCRenderType.eyesFix(TEXTURE_RED)), p_114490_, OverlayTexture.NO_OVERLAY, 0.8F, 0.8F, 0.8F, 1.0F);
		p_114488_.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityNeodymiumShackle p_114482_)
	{
		return new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/neodymium_shackle.png");
	}
}
