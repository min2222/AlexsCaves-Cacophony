package com.min01.acc.gui.overlay;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.ACCItems;
import com.min01.acc.item.MagneticRailgunItem;
import com.min01.acc.util.ACCClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class PolarityOverlay
{
	public static void draw(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight)
	{
		Minecraft mc = ACCClientUtil.MC;
		Player player = mc.player;
		if(player.isHolding(ACCItems.MAGNETIC_RAILGUN.get()))
		{
			PoseStack poseStack = guiGraphics.pose();
			ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "textures/gui/polarity_overlay.png");
            int forgeGuiY = Math.max(gui.leftHeight, gui.rightHeight);
            
            int y = MagneticRailgunItem.isRepel(player.getMainHandItem()) ? 20 : 0;
            
		    int blitX = (screenWidth / 2) - (19 / 2);
            int blitY = (screenHeight - forgeGuiY) - (20 / 2);
            
		    poseStack.pushPose();
			guiGraphics.blit(texture, blitX, blitY, 0, y, 19, 20, 64, 64);
		    poseStack.popPose();
		}
	}
}
