package com.min01.acc.item.renderer;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.enchantment.ACCEnchantments;
import com.min01.acc.item.model.ModelRadrifle;
import com.min01.acc.misc.ACCRenderType;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RadrifleRenderer extends BlockEntityWithoutLevelRenderer
{
	public final ModelRadrifle model;
	
	public RadrifleRenderer(EntityModelSet modelSet)
	{
		super(ACCClientUtil.MC.getBlockEntityRenderDispatcher(), modelSet);
		this.model = new ModelRadrifle(modelSet.bakeLayer(ModelRadrifle.LAYER_LOCATION));
	}
	
	@Override
	public void renderByItem(ItemStack p_108830_, ItemDisplayContext p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_) 
	{
		p_108832_.pushPose();
		VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(p_108833_, RenderType.entityCutoutNoCull(this.getTexture(p_108830_)), false, p_108830_.hasFoil());
		p_108832_.scale(-1.0F, -1.0F, 1.0F);
		p_108832_.translate(0.0F, -1.5F, 0.0F);
		p_108832_.translate(-0.5F, -0.25F, 0.3F);
		this.model.setupAnim(p_108830_, 0, 0, ACCUtil.getItemTickCount(p_108830_) + ACCClientUtil.MC.getFrameTime(), 0, 0);
		this.model.renderToBuffer(p_108832_, vertexconsumer, p_108834_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		p_108832_.popPose();
		
		p_108832_.pushPose();
		VertexConsumer vertexconsumer2 = ItemRenderer.getFoilBuffer(p_108833_, ACCRenderType.eyesFix(this.getLayerTexture(p_108830_)), false, p_108830_.hasFoil());
		p_108832_.scale(-1.0F, -1.0F, 1.0F);
		p_108832_.translate(0.0F, -1.5F, 0.0F);
		p_108832_.translate(-0.5F, -0.25F, 0.3F);
		this.model.setupAnim(p_108830_, 0, 0, ACCUtil.getItemTickCount(p_108830_) + ACCClientUtil.MC.getFrameTime(), 0, 0);
		this.model.renderToBuffer(p_108832_, vertexconsumer2, p_108834_, OverlayTexture.NO_OVERLAY, 0.3F, 0.3F, 0.3F, 1.0F);
		p_108832_.popPose();
	}
	
	public ResourceLocation getLayerTexture(ItemStack stack)
	{
        boolean gamma = stack.getEnchantmentLevel(ACEnchantmentRegistry.GAMMA_RAY.get()) > 0;
        boolean overcharge = stack.getEnchantmentLevel(ACCEnchantments.OVERCHARGE.get()) > 0;
        if(overcharge)
        {
        	return new ResourceLocation(AlexsCavesCacophony.MODID, "textures/item/radrifle_overcharged_layer.png");
        }
		return gamma ? new ResourceLocation(AlexsCavesCacophony.MODID, "textures/item/radrifle_gamma_layer.png") : new ResourceLocation(AlexsCavesCacophony.MODID, "textures/item/radrifle_layer.png");
	}
	
	public ResourceLocation getTexture(ItemStack stack)
	{
        boolean gamma = stack.getEnchantmentLevel(ACEnchantmentRegistry.GAMMA_RAY.get()) > 0;
        boolean overcharge = stack.getEnchantmentLevel(ACCEnchantments.OVERCHARGE.get()) > 0;
        if(overcharge)
        {
        	return new ResourceLocation(AlexsCavesCacophony.MODID, "textures/item/radrifle_overcharged.png");
        }
		return gamma ? new ResourceLocation(AlexsCavesCacophony.MODID, "textures/item/radrifle_gamma.png") : new ResourceLocation(AlexsCavesCacophony.MODID, "textures/item/radrifle.png");
	}
}
