package com.min01.acc.capabilities;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IOwnerCapability extends INBTSerializable<CompoundTag>
{
	ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "owner");

	void setOwner(Entity entity);
	
	Entity getOwner(Entity entity);
	
	void tick(Entity entity);
}
