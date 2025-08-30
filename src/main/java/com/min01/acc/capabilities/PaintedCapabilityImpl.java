package com.min01.acc.capabilities;

import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdatePaintedCapabilityPacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

public class PaintedCapabilityImpl implements IPaintedCapability
{
	private Entity entity;
	private boolean isPainted;
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("isPainted", this.isPainted);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		this.setPainted(nbt.getBoolean("isPainted"));
	}

	@Override
	public void setEntity(Entity entity) 
	{
		this.entity = entity;
	}
	
	@Override
	public void setPainted(boolean value)
	{
		this.isPainted = value;
		this.sendUpdatePacket();
	}
	
	@Override
	public boolean isPainted()
	{
		return this.isPainted;
	}
	
	private void sendUpdatePacket() 
	{
		if(!this.entity.level.isClientSide)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdatePaintedCapabilityPacket(this.entity.getUUID(), this.isPainted));
		}
	}
}
