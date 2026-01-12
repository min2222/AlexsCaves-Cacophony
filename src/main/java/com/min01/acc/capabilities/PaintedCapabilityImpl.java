package com.min01.acc.capabilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdatePaintedCapabilityPacket;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class PaintedCapabilityImpl implements IPaintedCapability
{
	public static final Capability<IPaintedCapability> PAINTED = CapabilityManager.get(new CapabilityToken<>() {});
	
	private final Entity entity;
	private boolean isPainted;
	
	public PaintedCapabilityImpl(Entity entity)
	{
		this.entity = entity;
	}
	
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

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		return PAINTED.orEmpty(cap, LazyOptional.of(() -> this));
	}
}
