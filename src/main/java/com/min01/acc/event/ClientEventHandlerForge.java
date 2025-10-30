package com.min01.acc.event;

import org.joml.Vector4f;

import com.github.alexthe666.citadel.client.render.LightningBoltData;
import com.github.alexthe666.citadel.client.render.LightningRender;
import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.animation.IHierarchicalPlayerModel;
import com.min01.acc.item.animation.IAnimatableItem;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AlexsCavesCacophony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge
{	
	@SubscribeEvent
	public static void onRenderHand(RenderHandEvent event)
	{
		ItemStack itemStack = event.getItemStack();
		if(itemStack.getItem() instanceof IAnimatableItem item && item.isFirstPersonAnim())
		{
			PoseStack stack = event.getPoseStack();
			MultiBufferSource bufferSource = event.getMultiBufferSource();
			renderPlayerArm(stack, bufferSource, event.getPackedLight(), HumanoidArm.RIGHT, itemStack, item, event.getPartialTick());
			renderPlayerArm(stack, bufferSource, event.getPackedLight(), HumanoidArm.LEFT, itemStack, item, event.getPartialTick());
			event.setCanceled(true);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void renderPlayerArm(PoseStack stack, MultiBufferSource bufferSource, int packedLight, HumanoidArm arm, ItemStack itemStack, IAnimatableItem item, float partialTicks)
	{
		stack.pushPose();
		Vec3 offset = item.getOffset();
		boolean flag = arm != HumanoidArm.LEFT;
		AbstractClientPlayer player = ACCClientUtil.MC.player;
		RenderSystem.setShaderTexture(0, player.getSkinTextureLocation());
		PlayerRenderer renderer = (PlayerRenderer) ACCClientUtil.MC.getEntityRenderDispatcher().<AbstractClientPlayer>getRenderer(player);
		stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		stack.translate(offset.x / 16.0F, offset.y / 16.0F, offset.z / 16.0F);
		((IHierarchicalPlayerModel<Player>) renderer.getModel()).setupAnimFirstPerson(player, 0, 0, player.tickCount + partialTicks, 0, 0);
		if(flag)
		{
			stack.pushPose();
			renderer.getModel().translateToHand(arm, stack);
			stack.mulPose(Axis.XP.rotationDegrees(-90.0F));
			stack.mulPose(Axis.YP.rotationDegrees(180.0F));
			stack.translate((float)(flag ? 1 : -1) / 16.0F, 0.125F, -0.625F);
	        ACCClientUtil.MC.getEntityRenderDispatcher().getItemInHandRenderer().renderItem(player, itemStack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, flag, stack, bufferSource, packedLight);
			stack.popPose();
			renderer.getModel().rightArm.render(stack, bufferSource.getBuffer(RenderType.entitySolid(player.getSkinTextureLocation())), packedLight, OverlayTexture.NO_OVERLAY);
			renderer.getModel().rightSleeve.render(stack, bufferSource.getBuffer(RenderType.entityTranslucent(player.getSkinTextureLocation())), packedLight, OverlayTexture.NO_OVERLAY);
		}
		else
		{
			renderer.getModel().leftArm.render(stack, bufferSource.getBuffer(RenderType.entitySolid(player.getSkinTextureLocation())), packedLight, OverlayTexture.NO_OVERLAY);
			renderer.getModel().leftSleeve.render(stack, bufferSource.getBuffer(RenderType.entityTranslucent(player.getSkinTextureLocation())), packedLight, OverlayTexture.NO_OVERLAY);
		}
		stack.popPose();
	}
	
	@SubscribeEvent
	public static void onRenderLivingPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event)
	{
		LivingEntity entity = event.getEntity();
		Entity owner = ACCUtil.getOwner(entity);
		float partialTicks = event.getPartialTick();
		PoseStack stack = event.getPoseStack();
		if(entity instanceof Player)
			return;
		if(owner != null)
		{
			Vec3 pos = entity.getEyePosition(partialTicks);
			Vec3 toVec = owner.getEyePosition(partialTicks);
			toVec = ACCUtil.getLookPos(new Vec2(owner.getViewXRot(partialTicks), owner.getViewYRot(partialTicks)), toVec, 0, 0.5F, 2);
			
			stack.pushPose();
			stack.translate(-pos.x, -pos.y, -pos.z);
			pos = pos.add(0.0F, 0.5F, 0.0F);
            int segCount = Mth.clamp((int) pos.distanceTo(toVec) + 2, 3, 30);
            float spreadFactor = 0.1F;
            LightningBoltData.BoltRenderInfo boltData = new LightningBoltData.BoltRenderInfo(0.0F, spreadFactor, 0.0F, 0.0F, new Vector4f(0.1F, 0.1F, 0.9F, 0.8F), 0.1F);
            LightningBoltData.BoltRenderInfo boltData1 = new LightningBoltData.BoltRenderInfo(0.0F, spreadFactor, 0.0F, 0.0F, new Vector4f(0.9F, 0.1F, 0.1F, 0.8F), 0.1F);
            LightningBoltData bolt1 = new LightningBoltData(boltData, pos, toVec, segCount).size(0.1F).lifespan(1).spawn(LightningBoltData.SpawnFunction.CONSECUTIVE).fade(LightningBoltData.FadeFunction.NONE);
            LightningBoltData bolt2 = new LightningBoltData(boltData1, pos, toVec, segCount).size(0.1F).lifespan(1).spawn(LightningBoltData.SpawnFunction.CONSECUTIVE).fade(LightningBoltData.FadeFunction.NONE);
            LightningRender lightningRender = ACCClientUtil.getLightingRender(entity.getUUID());
            lightningRender.update(1, bolt1, partialTicks);
            lightningRender.update(2, bolt2, partialTicks);
            lightningRender.render(partialTicks, stack, event.getMultiBufferSource());
            stack.popPose();
		}
		else if(ACCClientUtil.LIGHTNING_MAP.containsKey(entity.getUUID())) 
		{
			ACCClientUtil.LIGHTNING_MAP.remove(entity.getUUID());
        }
	}
}
