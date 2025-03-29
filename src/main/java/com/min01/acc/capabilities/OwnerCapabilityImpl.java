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
	private Entity entity;
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
	public void setEntity(Entity entity) 
	{
		this.entity = entity;
	}
	
	@Override
	public void setOwner(UUID uuid)
	{
		if(uuid == null)
		{
			this.ownerUUID = Optional.empty();
			this.sendUpdatePacket();
		}
		else
		{
			this.ownerUUID = Optional.of(uuid);
			this.sendUpdatePacket();
		}
	}
	
	@Override
	public Entity getOwner() 
	{
		if(this.ownerUUID.isPresent())
		{
			return ACCUtil.getEntityByUUID(this.entity.level, this.ownerUUID.get());
		}
		return null;
	}
	
	private void sendUpdatePacket() 
	{
		if(!this.entity.level.isClientSide)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdateOwnerCapabilityPacket(this.entity.getUUID(), this.ownerUUID));
		}
	}
}
