package com.min01.acc.entity.renderer.layer;

import com.min01.acc.entity.living.EntityOvivenator;
import com.min01.acc.entity.model.ModelOvivenator;
import com.min01.acc.util.ACCClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class OvivenatorEggLayer extends RenderLayer<EntityOvivenator, ModelOvivenator>
{
	public final ItemInHandRenderer itemRenderer = ACCClientUtil.MC.getEntityRenderDispatcher().getItemInHandRenderer();
	
	public OvivenatorEggLayer(RenderLayerParent<EntityOvivenator, ModelOvivenator> pRenderer)
	{
		super(pRenderer);
	}

	@Override
	public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, EntityOvivenator pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) 
	{
		ItemStack egg = pLivingEntity.getCurrentEgg();
		if(!egg.isEmpty())
		{
			pPoseStack.pushPose();
			this.getParentModel().root().getChild("ovivenator").getChild("body").getChild("left_arm").translateAndRotate(pPoseStack);
			pPoseStack.translate(-6.5F / 16.0F, 9.0F / 16.0F, -5.0F / 16.0F);
			pPoseStack.mulPose(Axis.ZP.rotationDegrees(184.65468F));
			pPoseStack.mulPose(Axis.YP.rotationDegrees(-8.86042F));
			pPoseStack.mulPose(Axis.XP.rotationDegrees(-45.0F));
			this.itemRenderer.renderItem(pLivingEntity, egg, ItemDisplayContext.FIXED, false, pPoseStack, pBuffer, pPackedLight);
			pPoseStack.popPose();
		}
	}
}
