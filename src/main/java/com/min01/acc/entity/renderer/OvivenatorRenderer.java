package com.min01.acc.entity.renderer;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.living.EntityOvivenator;
import com.min01.acc.entity.model.ModelOvivenator;
import com.min01.acc.entity.renderer.layer.OvivenatorEggLayer;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class OvivenatorRenderer extends MobRenderer<EntityOvivenator, ModelOvivenator>
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/ovivenator.png");
	public static final ResourceLocation TEXTURE_RETRO = new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/ovivenator_retro.png");
	public static final ResourceLocation TEXTURE_TECTONIC = new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/ovivenator_tectonic.png");
	
	public OvivenatorRenderer(Context p_174304_)
	{
		super(p_174304_, new ModelOvivenator(p_174304_.bakeLayer(ModelOvivenator.LAYER_LOCATION)), 0.5F);
		this.addLayer(new OvivenatorEggLayer(this));
	}
	
	@Override
	protected void scale(EntityOvivenator p_115314_, PoseStack p_115315_, float p_115316_)
	{
		if(p_115314_.isBaby())
		{
			p_115315_.scale(0.5F, 0.5F, 0.5F);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityOvivenator p_115812_) 
	{
		return p_115812_.getAltSkin() == 1 ? TEXTURE_RETRO : p_115812_.getAltSkin() == 2 ? TEXTURE_TECTONIC : TEXTURE;
	}
}
