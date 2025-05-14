package com.min01.acc.capabilities;

import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdateItemAnimationTickPacket;
import com.min01.acc.network.UpdateItemTickCountPacket;
import com.min01.acc.util.ACCUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class ItemAnimationCapabilityImpl implements IItemAnimationCapability
{
	private ItemStack stack;
	private Entity entity;
	private int tickCount;
	private int animationTick;
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("TickCount", this.tickCount);
		nbt.putInt("AnimationTick", this.animationTick);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.tickCount = nbt.getInt("TickCount");
		this.animationTick = nbt.getInt("AnimationTick");
	}
	
	@Override
	public void setEntity(Entity entity) 
	{
		this.entity = entity;
	}
	
	@Override
	public void setItemStack(ItemStack stack) 
	{
		this.stack = stack;
	}

	@Override
	public void update() 
	{
		this.tickCount++;
		this.animationTick--;
		if(this.entity != null && !this.entity.level.isClientSide)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdateItemTickCountPacket(this.entity.getUUID(), this.stack, this.tickCount));
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdateItemAnimationTickPacket(this.entity.getUUID(), this.stack, this.animationTick));
		}
	}

	@Override
	public void startItemAnimation(String name) 
	{
		AnimationState state = this.getAnimationState(name);
		state.startIfStopped(this.tickCount);
		ACCUtil.writeAnimationTime(this.stack.getOrCreateTag(), name, state);
	}

	@Override
	public void stopItemAnimation(String name) 
	{
		AnimationState state = this.getAnimationState(name);
		state.stop();
		ACCUtil.writeAnimationTime(this.stack.getOrCreateTag(), name, state);
	}
	
	@Override
	public AnimationState getAnimationState(String name)
	{
		AnimationState state = new AnimationState();
		ACCUtil.readAnimationTime(this.stack.getOrCreateTag(), name, state);
		return state;
	}
	
	@Override
	public void setTickCount(int tickCount) 
	{
		this.tickCount = tickCount;
	}
	
	@Override
	public int getTickCount() 
	{
		return this.tickCount;
	}
	
	@Override
	public void setAnimationTick(int tick) 
	{
		this.animationTick = tick;
	}
	
	@Override
	public int getAnimationTick() 
	{
		return this.animationTick;
	}
}
