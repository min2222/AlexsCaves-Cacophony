package com.min01.acc.capabilities;

import java.util.Optional;
import java.util.UUID;

import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdateOwnerCapabilityPacket;
import com.min01.acc.util.ACCUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

public class OwnerCapabilityImpl implements IOwnerCapability
{
	private Optional<UUID> ownerUUID = Optional.empty();
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		if(this.ownerUUID.isPresent())
		{
			tag.putUUID("OwnerUUID", this.ownerUUID.get());
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		if(nbt.contains("OwnerUUID"))
		{
			this.ownerUUID = Optional.of(nbt.getUUID("OwnerUUID"));
		}
	}
	
	@Override
	public void setOwner(Entity entity)
	{
		if(entity == null)
		{
			this.ownerUUID = Optional.empty();
		}
		else 
		{
			this.ownerUUID = Optional.of(entity.getUUID());
		}
	}
	
	@Override
	public Entity getOwner(Entity entity) 
	{
		if(this.ownerUUID.isPresent())
		{
			return ACCUtil.getEntityByUUID(entity.level, this.ownerUUID.get());
		}
		return null;
	}
	
	@Override
	public void tick(Entity entity) 
	{
		if(!entity.level.isClientSide)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new UpdateOwnerCapabilityPacket(entity.getUUID(), this.ownerUUID));
		}
	}
}
