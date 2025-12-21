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
	public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/ovivenator.png");
	public static final ResourceLocation TEXTURE_RETRO = ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/ovivenator_retro.png");
	public static final ResourceLocation TEXTURE_TECTONIC = ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/entity/ovivenator_tectonic.png");
	
	public OvivenatorRenderer(Context pContext)
	{
		super(pContext, new ModelOvivenator(pContext.bakeLayer(ModelOvivenator.LAYER_LOCATION)), 0.5F);
		this.addLayer(new OvivenatorEggLayer(this));
	}
	
	@Override
	protected void scale(EntityOvivenator pLivingEntity, PoseStack pPoseStack, float pPartialTickTime)
	{
		if(pLivingEntity.isBaby())
		{
			pPoseStack.scale(0.5F, 0.5F, 0.5F);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityOvivenator pEntity) 
	{
		return pEntity.getAltSkin() == 1 ? TEXTURE_RETRO : pEntity.getAltSkin() == 2 ? TEXTURE_TECTONIC : TEXTURE;
	}
}
