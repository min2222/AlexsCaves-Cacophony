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
	public OvivenatorEggLayer(RenderLayerParent<EntityOvivenator, ModelOvivenator> p_117346_)
	{
		super(p_117346_);
	}

	@Override
	public void render(PoseStack p_117349_, MultiBufferSource p_117350_, int p_117351_, EntityOvivenator p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) 
	{
		ItemStack egg = p_117352_.getCurrentEgg();
		if(!egg.isEmpty())
		{
			p_117349_.pushPose();
			this.getParentModel().root().getChild("ovivenator").getChild("body").getChild("left_arm").translateAndRotate(p_117349_);
			p_117349_.translate(-6.5F / 16.0F, 9.0F / 16.0F, -5.0F / 16.0F);
			p_117349_.mulPose(Axis.ZP.rotationDegrees(184.65468F));
			p_117349_.mulPose(Axis.YP.rotationDegrees(-8.86042F));
			p_117349_.mulPose(Axis.XP.rotationDegrees(-45.0F));
			this.itemRenderer.renderItem(p_117352_, egg, ItemDisplayContext.FIXED, false, p_117349_, p_117350_, p_117351_);
			p_117349_.popPose();
		}
	}
}
