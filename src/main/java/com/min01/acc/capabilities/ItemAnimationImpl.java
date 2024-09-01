package com.min01.acc.capabilities;

import java.util.HashMap;
import java.util.Map;

import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.ItemAnimationPlayPacket;
import com.min01.acc.network.ItemAnimationSyncPacket;
import com.min01.acc.util.ACCUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class ItemAnimationImpl implements ItemAnimationCapability
{
	private LivingEntity entity;
	private ItemStack stack;
	private final Map<String, AnimationState> animMap = new HashMap<>();
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag nbt = new CompoundTag();
		ListTag list = new ListTag();
		this.animMap.entrySet().forEach(t -> 
		{
			CompoundTag tag = new CompoundTag();
			tag.putString("AnimationName", t.getKey());
			ACCUtil.writeAnimationState(tag, t.getValue());
			list.add(tag);
		});
		nbt.put("Animations", list);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		ListTag list = nbt.getList("Animations", 11);
		for(int i = 0; i < list.size(); ++i)
		{
			CompoundTag tag = list.getCompound(i);
			this.animMap.put(tag.getString("AnimationName"), ACCUtil.readAnimationState(tag));
		}
	}

	@Override
	public void setEntity(LivingEntity entity)
	{
		this.entity = entity;
	}
	
	@Override
	public void setItemStack(ItemStack stack)
	{
		this.stack = stack;
	}
	
	@Override
	public void stopAnimation(String name)
	{
		if(this.animMap.containsKey(name))
		{
			this.animMap.get(name).stop();
		}
	}
	
	@Override
	public void playAnimation(String name, int tickCount)
	{
		if(this.animMap.containsKey(name))
		{
			this.animMap.get(name).startIfStopped(tickCount);
			this.sendPlayPacket(name);
		}
	}
	
	@Override
	public void addAnimationState(String name) 
	{
		if(!this.animMap.containsKey(name))
		{
			AnimationState state = new AnimationState();
			this.animMap.put(name, state);
			this.sendUpdatePacket(name, state);
			System.out.println(this.animMap);
		}
	}
	
	@Override
	public AnimationState getAnimationState(String name) 
	{
		return this.animMap.get(name);
	}
	
	@Override
	public Map<String, AnimationState> getAnimMap()
	{
		return this.animMap;
	}
	
	private void sendPlayPacket(String name) 
	{
		if(this.entity instanceof ServerPlayer)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new ItemAnimationPlayPacket(this.entity, this.stack, name));
		}
	}
	
	private void sendUpdatePacket(String name, AnimationState state) 
	{
		if(this.entity instanceof ServerPlayer)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new ItemAnimationSyncPacket(this.entity, this.stack, name, state));
		}
	}
}
