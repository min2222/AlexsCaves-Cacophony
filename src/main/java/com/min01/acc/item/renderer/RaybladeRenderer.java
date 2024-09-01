package com.min01.acc.item.renderer;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.RaybladeItem;
import com.min01.acc.item.model.ModelRayblade;
import com.min01.acc.misc.ACCRenderType;
import com.min01.acc.util.ACCClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RaybladeRenderer extends BlockEntityWithoutLevelRenderer
{
	public final ModelRayblade model;
	
	public RaybladeRenderer(EntityModelSet modelSet)
	{
		super(ACCClientUtil.MC.getBlockEntityRenderDispatcher(), modelSet);
		this.model = new ModelRayblade(modelSet.bakeLayer(ModelRayblade.LAYER_LOCATION));
	}
	
	@Override
	public void renderByItem(ItemStack p_108830_, ItemDisplayContext p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_) 
	{
		p_108832_.pushPose();
		VertexConsumer vertexconsumer = p_108833_.getBuffer(RenderType.entityCutoutNoCull(this.getTexture(p_108830_)));
		p_108832_.scale(-1.0F, -1.0F, 1.0F);
		p_108832_.translate(0.0F, -1.5F, 0.0F);
		p_108832_.translate(-0.5F, -0.8F, 0.5F);
		this.model.setupAnim(p_108830_, 0, 0, ACCClientUtil.MC.player.tickCount + ACCClientUtil.MC.getFrameTime(), 0, 0);
		this.model.renderToBuffer(p_108832_, vertexconsumer, p_108834_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		p_108832_.popPose();
		
		p_108832_.pushPose();
		VertexConsumer vertexconsumer2 = p_108833_.getBuffer(ACCRenderType.eyesFix(this.getLayerTexture(p_108830_)));
		p_108832_.scale(-1.0F, -1.0F, 1.0F);
		p_108832_.translate(0.0F, -1.5F, 0.0F);
		p_108832_.translate(-0.5F, -0.8F, 0.5F);
		this.model.setupAnim(p_108830_, 0, 0, ACCClientUtil.MC.player.tickCount + ACCClientUtil.MC.getFrameTime(), 0, 0);
		this.model.renderToBuffer(p_108832_, vertexconsumer2, p_108834_, OverlayTexture.NO_OVERLAY, 0.3F, 0.3F, 0.3F, 1.0F);
		p_108832_.popPose();
	}
	
	public ResourceLocation getLayerTexture(ItemStack stack)
	{
		return new ResourceLocation(String.format("%s:textures/item/rayblade_layer%d.png", AlexsCavesCacophony.MODID, RaybladeItem.getFrame(stack)));
	}
	
	public ResourceLocation getTexture(ItemStack stack)
	{
		return new ResourceLocation(String.format("%s:textures/item/rayblade%d.png", AlexsCavesCacophony.MODID, RaybladeItem.getFrame(stack)));
	}
}
