package com.min01.acc.item.renderer;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.RaybladeItem;
import com.min01.acc.item.model.ModelRayblade;
import com.min01.acc.misc.ACCRenderType;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;
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
	public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) 
	{
		if(pStack.getItem() instanceof RaybladeItem)
		{
			pPoseStack.pushPose();
			VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(this.getTexture(pStack)));
			pPoseStack.scale(-1.0F, -1.0F, 1.0F);
			pPoseStack.translate(0.0F, -1.5F, 0.0F);
			pPoseStack.translate(-0.5F, -0.8F, 0.5F);
			this.model.setupAnim(pStack, 0, 0, ACCUtil.getItemTickCount(pStack) + ACCClientUtil.MC.getFrameTime(), 0, 0);
			this.model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			pPoseStack.popPose();
			
			pPoseStack.pushPose();
			VertexConsumer vertexConsumer2 = pBuffer.getBuffer(ACCRenderType.eyesFix(this.getLayerTexture(pStack)));
			pPoseStack.scale(-1.0F, -1.0F, 1.0F);
			pPoseStack.translate(0.0F, -1.5F, 0.0F);
			pPoseStack.translate(-0.5F, -0.8F, 0.5F);
			this.model.setupAnim(pStack, 0, 0, ACCUtil.getItemTickCount(pStack) + ACCClientUtil.MC.getFrameTime(), 0, 0);
			this.model.renderToBuffer(pPoseStack, vertexConsumer2, pPackedLight, OverlayTexture.NO_OVERLAY, 0.3F, 0.3F, 0.3F, 1.0F);
			pPoseStack.popPose();
		}
	}
	
	public ResourceLocation getLayerTexture(ItemStack stack)
	{
		return ResourceLocation.parse(String.format("%s:textures/item/rayblade_layer%d.png", AlexsCavesCacophony.MODID, ACCUtil.getItemTickCount(stack) % 20));
	}
	
	public ResourceLocation getTexture(ItemStack stack)
	{
		return ResourceLocation.parse(String.format("%s:textures/item/rayblade%d.png", AlexsCavesCacophony.MODID, ACCUtil.getItemTickCount(stack) % 20));
	}
}
