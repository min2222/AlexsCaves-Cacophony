package com.min01.acc.event;

import com.github.alexmodguy.alexscaves.server.entity.living.RelicheirusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AlexsCavesCacophony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge
{
	//TODO
	//@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event)
	{
		Entity entity = event.getEntity();
		if(entity instanceof RelicheirusEntity relicheirus)
		{
			relicheirus.getPersistentData().putBoolean("isPainted", true);
		}
		if(entity instanceof VallumraptorEntity vallum)
		{
			vallum.getPersistentData().putBoolean("isPainted", true);
		}
	}
}
