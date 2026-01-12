package com.min01.acc.capabilities;

import java.util.Optional;
import java.util.UUID;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

@AutoRegisterCapability
public interface IOwnerCapability extends ICapabilitySerializable<CompoundTag>
{
	ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "owner");

	void setOwner(Optional<UUID> optional);
	
	Entity getOwner(Entity entity);
}
