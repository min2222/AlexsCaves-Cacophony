package com.min01.acc.capabilities;

import com.min01.acc.item.ACCItems;
import com.min01.acc.item.RadrifleItem;
import com.min01.acc.misc.SmoothAnimationState;
import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdatePlayerAnimationPacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class PlayerAnimationCapabilityImpl implements IPlayerAnimationCapability
{
	private Player entity;
	private int animationTick;
	private int animationState;
	private int prevAnimationState;
	
	private final SmoothAnimationState radrifleFireAnimationState = new SmoothAnimationState(0.999F);
	private final SmoothAnimationState radrifleHoldAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState radrifleRunningAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState radrifleHoldToRunAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState radrifleOverchargeFireAnimationState = new SmoothAnimationState(0.999F);
	private final SmoothAnimationState radrifleOverheatAnimationState = new SmoothAnimationState();
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("AnimationTick", this.animationTick);
		nbt.putInt("AnimationState", this.animationState);
		nbt.putInt("PrevAnimationState", this.prevAnimationState);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.animationTick = nbt.getInt("AnimationTick");
		this.animationState = nbt.getInt("AnimationState");
		this.prevAnimationState = nbt.getInt("PrevAnimationState");
	}
	
	@Override
	public void setEntity(Player entity) 
	{
		this.entity = entity;
	}

	@Override
	public void tick() 
	{
		if(this.entity != null)
		{
			if(this.entity.level.isClientSide)
			{
				this.radrifleFireAnimationState.updateWhen(this.getAnimationState() == 1 && this.entity.isHolding(ACCItems.RADRIFLE.get()), this.entity.tickCount);
				this.radrifleHoldAnimationState.updateWhen(this.getAnimationState() == 0 && this.entity.isHolding(ACCItems.RADRIFLE.get()) && !this.entity.isSprinting(), this.entity.tickCount);
				this.radrifleHoldToRunAnimationState.updateWhen(this.getAnimationState() == 2 && this.entity.isHolding(ACCItems.RADRIFLE.get()) && this.entity.isSprinting(), this.entity.tickCount);
				this.radrifleRunningAnimationState.updateWhen(this.getAnimationState() == 0 && this.entity.isHolding(ACCItems.RADRIFLE.get()) && this.entity.isSprinting(), this.entity.tickCount);
				this.radrifleOverchargeFireAnimationState.updateWhen(this.getAnimationState() == 3 && this.entity.isHolding(ACCItems.RADRIFLE.get()), this.entity.tickCount);
				this.radrifleOverheatAnimationState.updateWhen(this.getAnimationState() == 4 && this.entity.isHolding(ACCItems.RADRIFLE.get()), this.entity.tickCount);
			}
			if(this.entity.isSprinting() && this.getAnimationState() == 0 && this.prevAnimationState != 2)
			{
				this.setAnimationState(2);
				this.setAnimationTick(10);
			}
			if(this.getAnimationTick() >= 0)
			{
				this.setAnimationTick(this.getAnimationTick() - 1);
			}
			else
			{
				if(this.getAnimationState() == 2 && this.entity.isSprinting())
				{
					this.prevAnimationState = 2;
				}
				if(!this.entity.isSprinting() && this.prevAnimationState == 2)
				{
					this.prevAnimationState = 0;
				}
				this.setAnimationState(0);
			}
		}
	}

	@Override
	public void setAnimationState(int state) 
	{
		this.animationState = state;
		this.sendUpdatePacket(true);
	}

	@Override
	public int getAnimationState() 
	{
		return this.animationState;
	}
	
	@Override
	public SmoothAnimationState getAnimationStateByName(String name) 
	{
		if(name.equals(RadrifleItem.RADRIFLE_FIRE))
		{
			return this.radrifleFireAnimationState;
		}
		if(name.equals(RadrifleItem.RADRIFLE_HOLD))
		{
			return this.radrifleHoldAnimationState;
		}
		if(name.equals(RadrifleItem.RADRIFLE_RUNNING))
		{
			return this.radrifleRunningAnimationState;
		}
		if(name.equals(RadrifleItem.RADRIFLE_HOLD_TO_RUN))
		{
			return this.radrifleHoldToRunAnimationState;
		}
		if(name.equals(RadrifleItem.RADRIFLE_OVERCHARGE_FIRE))
		{
			return this.radrifleOverchargeFireAnimationState;
		}
		if(name.equals(RadrifleItem.RADRIFLE_OVERHEAT))
		{
			return this.radrifleOverheatAnimationState;
		}
		return new SmoothAnimationState();
	}
	
	@Override
	public void setAnimationTick(int tick) 
	{
		this.animationTick = tick;
		this.sendUpdatePacket(false);
	}
	
	@Override
	public int getAnimationTick() 
	{
		return this.animationTick;
	}
	
	public void sendUpdatePacket(boolean isState)
	{
		if(this.entity != null && !this.entity.level.isClientSide)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdatePlayerAnimationPacket(this.entity.getUUID(), this.animationState, this.animationTick, isState));
		}
	}
}
