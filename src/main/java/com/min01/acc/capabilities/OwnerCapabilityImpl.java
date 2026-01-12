package com.min01.acc.capabilities;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdateOwnerCapabilityPacket;
import com.min01.acc.util.ACCUtil;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class OwnerCapabilityImpl implements IOwnerCapability
{
	public static final Capability<IOwnerCapability> OWNER = CapabilityManager.get(new CapabilityToken<>() {});
	
	private Optional<UUID> ownerUUID = Optional.empty();
	private final Entity entity;
	
	public OwnerCapabilityImpl(Entity entity) 
	{
		this.entity = entity;
	}
	
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
			this.setOwner(Optional.of(nbt.getUUID("OwnerUUID")));
		}
		else
		{
			this.setOwner(Optional.empty());
		}
	}
	
	@Override
	public void setOwner(Optional<UUID> optional)
	{
		this.ownerUUID = optional;
		this.sendUpdatePacket();
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
	
	private void sendUpdatePacket() 
	{
		if(!this.entity.level.isClientSide)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdateOwnerCapabilityPacket(this.entity.getUUID(), this.ownerUUID));
		}
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		return OWNER.orEmpty(cap, LazyOptional.of(() -> this));
	}
}
