package com.min01.acc.capabilities;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

@AutoRegisterCapability
public interface IOverlayCapability extends ICapabilitySerializable<CompoundTag>
{
	ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "overlay");

	void setOverlayProgress(String name, int value);
	
	int getOverlayProgress(String name);
	
	void tick(LivingEntity player);
}
