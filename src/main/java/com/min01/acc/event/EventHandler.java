package com.min01.acc.event;

import com.github.alexmodguy.alexscaves.server.misc.ACCreativeTabRegistry;
import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.ACCItems;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AlexsCavesCacophony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler
{
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) 
    {
    	
    }
    
    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event)
    {
    	
    }
    
    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event)
    {
    	if(event.getTabKey() == ACCreativeTabRegistry.TOXIC_CAVES.getKey())
    	{
    		event.accept(ACCItems.RAYBLAE.get());
    		event.accept(ACCItems.RAY_CATALYST.get());
    	}
    	if(event.getTabKey() == ACCreativeTabRegistry.ABYSSAL_CHASM.getKey())
    	{
    		event.accept(ACCItems.HULLBREAKER_TOOTH.get());
    		event.accept(ACCItems.MASTCARVER.get());
    	}
    	if(event.getTabKey() == ACCreativeTabRegistry.PRIMORDIAL_CAVES.getKey())
    	{
    		event.accept(ACCItems.RAW_DINO_DRUMSTICK.get());
    		event.accept(ACCItems.COOKED_DINO_DRUMSTICK.get());
    	}
    }
}
