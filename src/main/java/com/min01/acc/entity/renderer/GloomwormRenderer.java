package com.min01.acc.entity.renderer;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.living.EntityGloomworm;
import com.min01.acc.entity.model.ModelGloomworm;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GloomwormRenderer extends MobRenderer<EntityGloomworm, ModelGloomworm>
{
	public static final ResourceLocation GONDAL_TEXTURE = new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/gondal.png");
	public static final ResourceLocation TEXTURE = new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/gloomworm.png");
	
	public GloomwormRenderer(Context p_174304_)
	{
		super(p_174304_, new ModelGloomworm(p_174304_.bakeLayer(ModelGloomworm.LAYER_LOCATION)), 0.25F);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityGloomworm p_115812_) 
	{
		return p_115812_.hasCustomName() && p_115812_.getCustomName().getString().equals("Gondal") ? GONDAL_TEXTURE : TEXTURE;
	}
}
