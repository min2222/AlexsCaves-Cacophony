package com.min01.acc.misc;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ACCTags
{
	public static class ACCBlocks
	{
	    public static final TagKey<Block> DINOSAUR_EGGS = registerBlockTag("dinosaur_eggs");
		public static final TagKey<Block> OVIVENATOR_CANT_STEAL = create("ovivenator_cant_steal");
		
		private static TagKey<Block> create(String name) 
		{
			return TagKey.create(Registries.BLOCK, new ResourceLocation(AlexsCavesCacophony.MODID, name));
		}

	    public static TagKey<Block> registerBlockTag(String name)
	    {
	        return TagKey.create(Registries.BLOCK, new ResourceLocation(AlexsCaves.MODID, name));
	    }
	}
    
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
