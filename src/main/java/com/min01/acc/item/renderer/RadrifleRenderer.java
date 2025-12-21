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
	public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) 
	{
		pPoseStack.pushPose();
		VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(pBuffer, RenderType.entityCutoutNoCull(this.getTexture(pStack)), false, pStack.hasFoil());
		pPoseStack.scale(-1.0F, -1.0F, 1.0F);
		pPoseStack.translate(0.0F, -1.5F, 0.0F);
		pPoseStack.translate(-0.5F, -0.25F, 0.3F);
		this.model.setupAnim(pStack, 0, 0, ACCUtil.getItemTickCount(pStack) + ACCClientUtil.MC.getFrameTime(), 0, 0);
		this.model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		pPoseStack.popPose();
		
		pPoseStack.pushPose();
		VertexConsumer vertexConsumer2 = ItemRenderer.getFoilBuffer(pBuffer, ACCRenderType.eyesFix(this.getLayerTexture(pStack)), false, pStack.hasFoil());
		pPoseStack.scale(-1.0F, -1.0F, 1.0F);
		pPoseStack.translate(0.0F, -1.5F, 0.0F);
		pPoseStack.translate(-0.5F, -0.25F, 0.3F);
		this.model.setupAnim(pStack, 0, 0, ACCUtil.getItemTickCount(pStack) + ACCClientUtil.MC.getFrameTime(), 0, 0);
		this.model.renderToBuffer(pPoseStack, vertexConsumer2, pPackedLight, OverlayTexture.NO_OVERLAY, 0.3F, 0.3F, 0.3F, 1.0F);
		pPoseStack.popPose();
	}
	
	public ResourceLocation getLayerTexture(ItemStack stack)
	{
        boolean gamma = stack.getEnchantmentLevel(ACEnchantmentRegistry.GAMMA_RAY.get()) > 0;
        boolean overcharge = stack.getEnchantmentLevel(ACCEnchantments.OVERCHARGE.get()) > 0;
        if(overcharge)
        {
        	return ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/item/radrifle_overcharged_layer.png");
        }
		return gamma ? ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/item/radrifle_gamma_layer.png") : ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/item/radrifle_layer.png");
	}
	
	public ResourceLocation getTexture(ItemStack stack)
	{
        boolean gamma = stack.getEnchantmentLevel(ACEnchantmentRegistry.GAMMA_RAY.get()) > 0;
        boolean overcharge = stack.getEnchantmentLevel(ACCEnchantments.OVERCHARGE.get()) > 0;
        if(overcharge)
        {
        	return ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/item/radrifle_overcharged.png");
        }
		return gamma ? ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/item/radrifle_gamma.png") : ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/item/radrifle.png");
	}
}
