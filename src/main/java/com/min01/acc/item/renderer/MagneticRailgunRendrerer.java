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
	public void renderByItem(ItemStack p_108830_, ItemDisplayContext p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_) 
	{
		if(p_108830_.getItem() instanceof MagneticRailgunItem)
		{
			p_108832_.pushPose();
			VertexConsumer vertexconsumer = p_108833_.getBuffer(RenderType.entityCutoutNoCull(new ResourceLocation(AlexsCavesCacophony.MODID, "textures/item/magnetic_railgun.png")));
			p_108832_.scale(-1.0F, -1.0F, 1.0F);
			p_108832_.translate(0.0F, -1.5F, 0.0F);
			p_108832_.translate(-0.5F, -0.515F, 0.6F);
			this.model.setupAnim(p_108830_, 0, 0, ACCUtil.getItemTickCount(p_108830_) + ACCClientUtil.MC.getFrameTime(), 0, 0);
			this.model.renderToBuffer(p_108832_, vertexconsumer, p_108834_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			p_108832_.popPose();
			
			p_108832_.pushPose();
			VertexConsumer vertexconsumer2 = p_108833_.getBuffer(RenderType.eyes(new ResourceLocation(AlexsCavesCacophony.MODID, "textures/item/magnetic_railgun_blue.png")));
			p_108832_.scale(-1.0F, -1.0F, 1.0F);
			p_108832_.translate(0.0F, -1.5F, 0.0F);
			p_108832_.translate(-0.5F, -0.515F, 0.6F);
			this.model.setupAnim(p_108830_, 0, 0, ACCUtil.getItemTickCount(p_108830_) + ACCClientUtil.MC.getFrameTime(), 0, 0);
			
			ItemAnimationCapabilityImpl cap = (ItemAnimationCapabilityImpl) p_108830_.getCapability(ACCCapabilities.ITEM_ANIMATION).orElseGet(() -> new ItemAnimationCapabilityImpl());
			float ageInTicks = ACCUtil.getItemTickCount(p_108830_) + ACCClientUtil.MC.getFrameTime();
	        float strength = 0.5F + Mth.clamp(((float) Math.cos((cap.glowingTicks + ageInTicks) * 0.1F)) - 0.5F, -0.5F, 0.5F);
	        strength += Mth.lerp(ageInTicks, cap.brightnessOld, cap.brightness) * Mth.PI;
	        strength = Mth.clamp(strength, 0.0F, 1.0F);
			if(!MagneticRailgunItem.isRepel(p_108830_) || !MagneticRailgunItem.isFlash(p_108830_))
			{
				strength = 0.0F;
			}
			this.model.renderToBuffer(p_108832_, vertexconsumer2, p_108834_, OverlayTexture.NO_OVERLAY, strength, strength, strength, 1.0F);
			p_108832_.popPose();
			
			p_108832_.pushPose();
			VertexConsumer vertexconsumer3 = p_108833_.getBuffer(RenderType.eyes(new ResourceLocation(AlexsCavesCacophony.MODID, "textures/item/magnetic_railgun_red.png")));
			p_108832_.scale(-1.0F, -1.0F, 1.0F);
			p_108832_.translate(0.0F, -1.5F, 0.0F);
			p_108832_.translate(-0.5F, -0.515F, 0.6F);
			this.model.setupAnim(p_108830_, 0, 0, ACCUtil.getItemTickCount(p_108830_) + ACCClientUtil.MC.getFrameTime(), 0, 0);
			float strength2 = !MagneticRailgunItem.isRepel(p_108830_) && MagneticRailgunItem.isFlash(p_108830_) ? 0.8F : 0.0F;
			this.model.renderToBuffer(p_108832_, vertexconsumer3, p_108834_, OverlayTexture.NO_OVERLAY, strength2, strength2, strength2, 1.0F);
			p_108832_.popPose();
			
			p_108832_.pushPose();
			VertexConsumer vertexconsumer4 = p_108833_.getBuffer(RenderType.eyes(new ResourceLocation(AlexsCavesCacophony.MODID, "textures/item/magnetic_railgun_layer.png")));
			p_108832_.scale(-1.0F, -1.0F, 1.0F);
			p_108832_.translate(0.0F, -1.5F, 0.0F);
			p_108832_.translate(-0.5F, -0.515F, 0.6F);
			this.model.setupAnim(p_108830_, 0, 0, ACCUtil.getItemTickCount(p_108830_) + ACCClientUtil.MC.getFrameTime(), 0, 0);
			this.model.renderToBuffer(p_108832_, vertexconsumer4, p_108834_, OverlayTexture.NO_OVERLAY, 0.8F, 0.8F, 0.8F, 1.0F);
			p_108832_.popPose();
		}
	}
}
