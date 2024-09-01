package com.min01.acc;

import com.min01.acc.entity.ACCEntities;
import com.min01.acc.item.ACCItems;
import com.min01.acc.misc.ACCCreativeModeTabs;
import com.min01.acc.misc.ACCEntityDataSerializers;
import com.min01.acc.misc.ACCSounds;
import com.min01.acc.network.ACCNetwork;

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
		ACCCreativeModeTabs.CREATIVE_MODE_TAB.register(bus);
		ACCItems.ITEMS.register(bus);
		ACCSounds.SOUNDS.register(bus);
		ACCEntityDataSerializers.SERIALIZERS.register(bus);
		
		ACCNetwork.registerMessages();
	}
}
