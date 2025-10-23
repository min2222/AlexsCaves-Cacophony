package com.min01.acc.gui.overlay;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.ACCItems;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class PolarityOverlay
{
	public static void draw(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight)
	{
		Minecraft mc = ACCClientUtil.MC;
		Player player = mc.player;
		ItemStack stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
		if(stack.is(ACCItems.MAGNETIC_RAILGUN.get()))
		{
			PoseStack poseStack = guiGraphics.pose();
			ResourceLocation texture = new ResourceLocation(AlexsCavesCacophony.MODID, "textures/gui/polarity_overlay.png");
			int posX = -20;
			int posY = -20;
		    int blitX = posX + (screenWidth / 2) - (20 / 2);
		    int blitY = posY + (screenHeight / 2) - (19 / 2);
		    int polarity = ACCUtil.getOverlayProgress("Polarity", player);
		    poseStack.pushPose();
		    poseStack.scale(2.0F, 2.0F, 2.0F);
			guiGraphics.blit(texture, blitX, blitY, 0, 0, 19 - (int) Math.floor(polarity / 19), 20, 64, 64);
		    poseStack.popPose();
		}
	}
}
