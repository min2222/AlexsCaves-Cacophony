package com.min01.acc.event;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.animation.IHierarchicalPlayerModel;
import com.min01.acc.item.animation.IAnimatableItem;
import com.min01.acc.util.ACCClientUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
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
}
