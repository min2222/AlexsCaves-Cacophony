package com.min01.acc.misc;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ACCTags
{
	public static class ACCItems
	{
		public static final TagKey<Item> OVIVENATOR_TAMEABLE = create("ovivenator_tameable");
		public static final TagKey<Item> OVIVENATOR_CANT_STEAL = create("ovivenator_cant_steal");
		
		private static TagKey<Item> create(String name) 
		{
			return TagKey.create(Registries.ITEM, new ResourceLocation(AlexsCavesCacophony.MODID, name));
		}
	}
}
