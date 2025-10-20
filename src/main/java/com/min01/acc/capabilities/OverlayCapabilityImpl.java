package com.min01.acc.capabilities;

import java.util.HashMap;
import java.util.Map;

import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdateOverlayCapabilityPacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;

public class OverlayCapabilityImpl implements IOverlayCapability
{
	private final Map<String, Integer> progressMap = new HashMap<>();
	
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
		this.progressMap.put("Overheat", nbt.getInt("Overheat"));
		this.progressMap.put("Polarity", nbt.getInt("Polarity"));
	}
	
	@Override
	public void setOverlayProgress(String name, int value) 
	{
		this.progressMap.put(name, value);
	}
	
	@Override
	public int getOverlayProgress(String name) 
	{
		if(!this.progressMap.containsKey(name))
		{
			return 0;
		}
		return this.progressMap.get(name);
	}

	@Override
	public void tick(LivingEntity player)
	{
		int value = this.getOverlayProgress("Overheat");
		if(value > 0 && player.tickCount % 100 == 0 && player.getUseItem().isEmpty())
		{
			this.setOverlayProgress("Overheat", Math.max(value - 100, 0));
		}
		if(!player.level.isClientSide)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new UpdateOverlayCapabilityPacket(player.getUUID(), this.progressMap));
		}
	}
}
