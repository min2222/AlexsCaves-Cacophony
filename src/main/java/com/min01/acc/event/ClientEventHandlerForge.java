package com.min01.acc.event;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.ACCItems;
import com.min01.acc.item.model.ModelRadrifle;
import com.min01.acc.item.renderer.RadrifleRenderer;
import com.min01.acc.misc.ACCRenderType;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AlexsCavesCacophony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge
{
	//FIXME weird position when look above
	@SubscribeEvent
	public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event)
	{
		if(!ACCClientUtil.MC.options.getCameraType().isFirstPerson())
		{
			PoseStack poseStack = event.getPoseStack();
			MultiBufferSource bufferSource = event.getMultiBufferSource();
			int packedLight = event.getPackedLight();
			LivingEntity living = event.getEntity();
			if(living.getMainHandItem().getItem() == ACCItems.RADRIFLE.get())
			{
				ItemStack stack = living.getMainHandItem();
				renderBeam(stack, poseStack, bufferSource, packedLight, event.getPartialTick(), living, new Vec3(0.0F, 0.15F, -0.8F));
			}
			if(living.getOffhandItem().getItem() == ACCItems.RADRIFLE.get())
			{
				ItemStack stack = living.getOffhandItem();
				renderBeam(stack, poseStack, bufferSource, packedLight, event.getPartialTick(), living, new Vec3(0.0F, 0.15F, -0.8F));
			}
		}
	}
	
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event)
    {
		if(ACCClientUtil.MC.options.getCameraType().isFirstPerson())
		{
	    	if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY || event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) 
	        {
	    		PoseStack poseStack = event.getPoseStack();
	    		MultiBufferSource bufferSource = ACCClientUtil.MC.renderBuffers().bufferSource();
	    		Entity entity = event.getCamera().getEntity();
	    		int packedLight = ACCClientUtil.MC.getEntityRenderDispatcher().getPackedLightCoords(entity, event.getPartialTick());
	    		if(entity instanceof LivingEntity living)
	    		{
	    			if(living.getMainHandItem().getItem() == ACCItems.RADRIFLE.get())
	    			{
	    				ItemStack stack = living.getMainHandItem();
	    				renderBeam(stack, poseStack, bufferSource, packedLight, event.getPartialTick(), living, new Vec3(0.5F, -1.57F, -0.8F));
	    			}
	    			if(living.getOffhandItem().getItem() == ACCItems.RADRIFLE.get())
	    			{
	    				ItemStack stack = living.getOffhandItem();
	    				renderBeam(stack, poseStack, bufferSource, packedLight, event.getPartialTick(), living, new Vec3(-0.5F, -1.57F, -0.8F));
	    			}
	    		}
	        }
		}
    }
    
    public static void renderBeam(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick, LivingEntity entity, Vec3 offset)
    {
		ModelRadrifle model = new ModelRadrifle(ACCClientUtil.MC.getEntityModels().bakeLayer(ModelRadrifle.LAYER_LOCATION));
		model.root().getChild("rifle").visible = false;
		model.root().getChild("beam").visible = ACCUtil.getAnimationTick(stack) >= 8;

		float f1 = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
        float f6 = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
		
		poseStack.pushPose();
		VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(RadrifleRenderer.getTexture(stack)));
        poseStack.mulPose(Axis.YP.rotationDegrees(-f1 + 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(-f6));
		poseStack.translate(offset.x, offset.y, offset.z);
		model.setupAnim(stack, 0, 0, entity.tickCount + ACCClientUtil.MC.getFrameTime(), 0, 0);
		model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
		
		poseStack.pushPose();
		VertexConsumer vertexconsumer2 = bufferSource.getBuffer(ACCRenderType.eyesFix(RadrifleRenderer.getLayerTexture(stack)));
        poseStack.mulPose(Axis.YP.rotationDegrees(-f1 + 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(-f6));
		poseStack.translate(offset.x, offset.y, offset.z);
		model.setupAnim(stack, 0, 0, entity.tickCount + ACCClientUtil.MC.getFrameTime(), 0, 0);
		model.renderToBuffer(poseStack, vertexconsumer2, packedLight, OverlayTexture.NO_OVERLAY, 0.3F, 0.3F, 0.3F, 1.0F);
		poseStack.popPose();
    }
}
