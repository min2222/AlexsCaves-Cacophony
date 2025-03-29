package com.min01.acc.capabilities;

import java.util.UUID;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IOwnerCapability extends INBTSerializable<CompoundTag>
{
	ResourceLocation ID = new ResourceLocation(AlexsCavesCacophony.MODID, "owner");

	void setEntity(Entity entity);
	
	void setOwner(UUID uuid);
	
	Entity getOwner();
}
