package com.min01.acc.item.renderer;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.MagneticRailgunItem;
import com.min01.acc.item.model.ModelMagneticRailgun;
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
			p_108832_.translate(-0.5F, -0.5F, 0.2F);
			this.model.setupAnim(p_108830_, 0, 0, ACCUtil.getTickCount(p_108830_) + ACCClientUtil.MC.getFrameTime(), 0, 0);
			this.model.renderToBuffer(p_108832_, vertexconsumer, p_108834_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			p_108832_.popPose();
			
			p_108832_.pushPose();
			VertexConsumer vertexconsumer2 = p_108833_.getBuffer(ACCRenderType.eyesFix(new ResourceLocation(AlexsCavesCacophony.MODID, "textures/item/magnetic_railgun_layer.png")));
			p_108832_.scale(-1.0F, -1.0F, 1.0F);
			p_108832_.translate(0.0F, -1.5F, 0.0F);
			p_108832_.translate(-0.5F, -0.5F, 0.2F);
			this.model.setupAnim(p_108830_, 0, 0, ACCUtil.getTickCount(p_108830_) + ACCClientUtil.MC.getFrameTime(), 0, 0);
			this.model.renderToBuffer(p_108832_, vertexconsumer2, p_108834_, OverlayTexture.NO_OVERLAY, 0.3F, 0.3F, 0.3F, 1.0F);
			p_108832_.popPose();	
		}
	}
}
