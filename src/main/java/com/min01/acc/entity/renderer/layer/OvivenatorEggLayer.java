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
			this.getParentModel().root().translateAndRotate(pPoseStack);
			this.getParentModel().root().getChild("ovivenator").translateAndRotate(pPoseStack);
			this.getParentModel().root().getChild("ovivenator").getChild("body").translateAndRotate(pPoseStack);
			this.getParentModel().root().getChild("ovivenator").getChild("body").getChild("left_arm").translateAndRotate(pPoseStack);
			pPoseStack.translate(-0.35F, 0.5F, -0.5F);
			pPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
			this.itemRenderer.renderItem(pLivingEntity, egg, ItemDisplayContext.FIXED, false, pPoseStack, pBuffer, pPackedLight);
			pPoseStack.popPose();
		}
	}
}
