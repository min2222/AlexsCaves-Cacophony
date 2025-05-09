package com.min01.acc.capabilities;

import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdatePlayerAnimationTickPacket;
import com.min01.acc.util.ACCUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class PlayerAnimationCapabilityImpl implements IPlayerAnimationCapability
{
	private Player entity;
	private int animationTick;
	private CompoundTag tag = new CompoundTag();
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("AnimationTick", this.animationTick);
		nbt.put("CompoundTag", this.tag);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.animationTick = nbt.getInt("AnimationTick");
		this.tag = nbt.getCompound("CompoundTag");
	}
	
	@Override
	public void setEntity(Player entity) 
	{
		this.entity = entity;
	}

	@Override
	public void startPlayerAnimation(String name) 
	{
		AnimationState state = this.getAnimationState(name);
		state.startIfStopped(this.entity.tickCount);
		ACCUtil.writeAnimationTime(this.tag, name, state);
	}

	@Override
	public void stopPlayerAnimation(String name) 
	{
		AnimationState state = this.getAnimationState(name);
		state.stop();
		ACCUtil.writeAnimationTime(this.tag, name, state);
	}
	
	@Override
	public AnimationState getAnimationState(String name)
	{
		AnimationState state = new AnimationState();
		ACCUtil.readAnimationTime(this.tag, name, state);
		return state;
	}
	
	@Override
	public void setAnimationTick(int tick) 
	{
		this.animationTick = tick;
		if(this.entity != null && !this.entity.level.isClientSide)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdatePlayerAnimationTickPacket(this.entity.getUUID(), this.animationTick));
		}
	}
	
	@Override
	public int getAnimationTick() 
	{
		return this.animationTick;
	}
	
	@Override
	public CompoundTag getCompoundTag() 
	{
		return this.tag;
	}
}
