package com.min01.acc.capabilities;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

@AutoRegisterCapability
public interface IPaintedCapability extends ICapabilitySerializable<CompoundTag>
{
	ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "painted");
	
	void setPainted(boolean value);
	
	boolean isPainted();
}
