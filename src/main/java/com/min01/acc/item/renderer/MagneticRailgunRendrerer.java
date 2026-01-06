package com.min01.acc.item.renderer;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.capabilities.ItemAnimationCapabilityImpl;
import com.min01.acc.item.MagneticRailgunItem;
import com.min01.acc.item.model.ModelMagneticRailgun;
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
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MagneticRailgunRendrerer extends BlockEntityWithoutLevelRenderer
{
	public final ModelMagneticRailgun model;
	
	public MagneticRailgunRendrerer(EntityModelSet modelSet)
	{
		super(ACCClientUtil.MC.getBlockEntityRenderDispatcher(), modelSet);
		this.model = new ModelMagneticRailgun(modelSet.bakeLayer(ModelMagneticRailgun.LAYER_LOCATION));
	}
	
	@Override
	public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) 
	{
		if(pStack.getItem() instanceof MagneticRailgunItem)
		{
			pPoseStack.pushPose();
			VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/item/magnetic_railgun.png")));
			pPoseStack.scale(-1.0F, -1.0F, 1.0F);
			pPoseStack.translate(0.0F, -1.5F, 0.0F);
			pPoseStack.translate(-0.5F, -0.515F, 0.6F);
			this.model.setupAnim(pStack, 0, 0, ACCUtil.getItemTickCount(pStack) + ACCClientUtil.MC.getFrameTime(), 0, 0);
			this.model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			pPoseStack.popPose();
			
			pPoseStack.pushPose();
			VertexConsumer vertexConsumer2 = pBuffer.getBuffer(RenderType.eyes(ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/item/magnetic_railgun_blue.png")));
			pPoseStack.scale(-1.0F, -1.0F, 1.0F);
			pPoseStack.translate(0.0F, -1.5F, 0.0F);
			pPoseStack.translate(-0.5F, -0.515F, 0.6F);
			this.model.setupAnim(pStack, 0, 0, ACCUtil.getItemTickCount(pStack) + ACCClientUtil.MC.getFrameTime(), 0, 0);
			
			ItemAnimationCapabilityImpl cap = (ItemAnimationCapabilityImpl) pStack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElseGet(() -> new ItemAnimationCapabilityImpl());
			float ageInTicks = ACCUtil.getItemTickCount(pStack) + ACCClientUtil.MC.getFrameTime();
	        float strength = 0.5F + Mth.clamp(((float) Math.cos((cap.glowingTicks + ageInTicks) * 0.1F)) - 0.5F, -0.5F, 0.5F);
	        strength += Mth.lerp(ageInTicks, cap.brightnessOld, cap.brightness) * Mth.PI;
	        strength = Mth.clamp(strength, 0.0F, 1.0F);
	        if(!MagneticRailgunItem.isRepel(pStack) || !MagneticRailgunItem.isFlash(pStack))
			{
				strength = 0.0F;
			}
			this.model.renderToBuffer(pPoseStack, vertexConsumer2, pPackedLight, OverlayTexture.NO_OVERLAY, strength, strength, strength, 1.0F);
			pPoseStack.popPose();
			
			pPoseStack.pushPose();
			VertexConsumer vertexConsumer3 = pBuffer.getBuffer(RenderType.eyes(ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/item/magnetic_railgun_red.png")));
			pPoseStack.scale(-1.0F, -1.0F, 1.0F);
			pPoseStack.translate(0.0F, -1.5F, 0.0F);
			pPoseStack.translate(-0.5F, -0.515F, 0.6F);
			this.model.setupAnim(pStack, 0, 0, ACCUtil.getItemTickCount(pStack) + ACCClientUtil.MC.getFrameTime(), 0, 0);
	        float strength1 = 0.5F + Mth.clamp(((float) Math.cos((cap.glowingTicks + ageInTicks) * 0.1F)) - 0.5F, -0.5F, 0.5F);
	        strength1 += Mth.lerp(ageInTicks, cap.brightnessOld, cap.brightness) * Mth.PI;
	        strength1 = Mth.clamp(strength1, 0.0F, 1.0F);
	        if(MagneticRailgunItem.isRepel(pStack) || !MagneticRailgunItem.isFlash(pStack))
			{
				strength1 = 0.0F;
			}
			this.model.renderToBuffer(pPoseStack, vertexConsumer3, pPackedLight, OverlayTexture.NO_OVERLAY, strength1, strength1, strength1, 1.0F);
			pPoseStack.popPose();
			
			pPoseStack.pushPose();
			VertexConsumer vertexConsumer4 = pBuffer.getBuffer(RenderType.eyes(ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/item/magnetic_railgun_layer.png")));
			pPoseStack.scale(-1.0F, -1.0F, 1.0F);
			pPoseStack.translate(0.0F, -1.5F, 0.0F);
			pPoseStack.translate(-0.5F, -0.515F, 0.6F);
			this.model.setupAnim(pStack, 0, 0, ACCUtil.getItemTickCount(pStack) + ACCClientUtil.MC.getFrameTime(), 0, 0);
			this.model.renderToBuffer(pPoseStack, vertexConsumer4, pPackedLight, OverlayTexture.NO_OVERLAY, 0.8F, 0.8F, 0.8F, 1.0F);
			pPoseStack.popPose();
		}
	}
}
