package com.min01.acc;

import com.min01.acc.block.ACCBlocks;
import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.item.ACCItems;
import com.min01.acc.misc.ACCEntityDataSerializers;
import com.min01.acc.misc.ACCSounds;
import com.min01.acc.network.ACCNetwork;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AlexsCavesCacophony.MODID)
public class AlexsCavesCacophony
{
	public static final String MODID = "accacophony";
			
	public AlexsCavesCacophony() 
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ACCEntities.ENTITY_TYPES.register(bus);
		ACCItems.ITEMS.register(bus);
		ACCBlocks.BLOCKS.register(bus);
		ACCBlocks.BLOCK_ENTITIES.register(bus);
		ACCSounds.SOUNDS.register(bus);
		ACCEntityDataSerializers.SERIALIZERS.register(bus);
		
		ACCNetwork.registerMessages();
		MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, ACCCapabilities::attachEntityCapability);
		MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, ACCCapabilities::attachItemStackCapability);
	}
}
