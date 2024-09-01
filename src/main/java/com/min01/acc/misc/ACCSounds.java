package com.min01.acc.misc;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACCSounds
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AlexsCavesCacophony.MODID);
	
	private static RegistryObject<SoundEvent> registerSound(String name) 
	{
		return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(AlexsCavesCacophony.MODID, name)));
    }
}
