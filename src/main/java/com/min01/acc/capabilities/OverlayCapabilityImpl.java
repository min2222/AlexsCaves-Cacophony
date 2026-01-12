package com.min01.acc.capabilities;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdateOverlayCapabilityPacket;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class OverlayCapabilityImpl implements IOverlayCapability
{
	public static final Capability<IOverlayCapability> OVERLAY = CapabilityManager.get(new CapabilityToken<>() {});
	
	private final Map<String, Integer> progressMap = new HashMap<>();
	private final Entity entity;
	
	public OverlayCapabilityImpl(Entity entity) 
	{
		this.entity = entity;
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		for(Map.Entry<String, Integer> entry : this.progressMap.entrySet())
		{
			tag.putInt(entry.getKey(), entry.getValue());
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		this.setOverlayProgress("Overheat", nbt.getInt("Overheat"));
		this.setOverlayProgress("Polarity", nbt.getInt("Polarity"));
	}
	
	@Override
	public void setOverlayProgress(String name, int value) 
	{
		this.progressMap.put(name, value);
		this.sendUpdatePacket();
	}
	
	@Override
	public int getOverlayProgress(String name) 
	{
		return this.progressMap.getOrDefault(name, 0);
	}

	@Override
	public void tick(LivingEntity player)
	{
		int value = this.getOverlayProgress("Overheat");
		if(value > 0 && player.getUseItem().isEmpty())
		{
			this.setOverlayProgress("Overheat", value - 1);
		}
	}
	
	private void sendUpdatePacket() 
	{
		if(!this.entity.level.isClientSide)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdateOverlayCapabilityPacket(this.entity.getUUID(), this.progressMap));
		}
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		return OVERLAY.orEmpty(cap, LazyOptional.of(() -> this));
	}
}
