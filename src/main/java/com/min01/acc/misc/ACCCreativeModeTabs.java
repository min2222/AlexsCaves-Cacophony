package com.min01.acc.misc;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ACCCreativeModeTabs 
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AlexsCavesCacophony.MODID);

    public static final RegistryObject<CreativeModeTab> ACC = CREATIVE_MODE_TAB.register("accacophony", () -> CreativeModeTab.builder()
    		.title(Component.translatable("itemGroup.accacophony.accacophony"))
    		.icon(() -> new ItemStack(ACItemRegistry.CAVE_MAP.get()))
    		.displayItems((enabledFeatures, output) -> 
    		{
    			
    		}).build());
}
