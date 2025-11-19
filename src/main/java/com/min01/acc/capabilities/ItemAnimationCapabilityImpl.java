package com.min01.acc.capabilities;

import com.min01.acc.item.ACCItems;
import com.min01.acc.item.MagneticRailgunItem;
import com.min01.acc.item.RadrifleItem;
import com.min01.acc.item.RaybladeItem;
import com.min01.acc.misc.SmoothAnimationState;
import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdateItemAnimationPacket;
import com.min01.acc.util.ACCUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class ItemAnimationCapabilityImpl implements IItemAnimationCapability
{
	private int animationTick;
	private int animationState;
	private int tickCount;
	
	private final SmoothAnimationState overheatAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState holdAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState inspectAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState chargeAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState fireAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState raybladeSwingAnimationState = new SmoothAnimationState(0.999F, 0.4F);
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("AnimationTick", this.animationTick);
		nbt.putInt("AnimationState", this.animationState);
		this.overheatAnimationState.write(RadrifleItem.RADRIFLE_OVERHEAT, nbt);
		this.holdAnimationState.write(MagneticRailgunItem.RAILGUN_HOLD, nbt);
		this.inspectAnimationState.write(MagneticRailgunItem.RAILGUN_INSPECT, nbt);
		this.chargeAnimationState.write(MagneticRailgunItem.RAILGUN_CHARGE, nbt);
		this.fireAnimationState.write(MagneticRailgunItem.RAILGUN_FIRE, nbt);
		this.raybladeSwingAnimationState.write(RaybladeItem.RAYBLADE_SWING, nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.animationTick = nbt.getInt("AnimationTick");
		this.animationState = nbt.getInt("AnimationState");
		this.overheatAnimationState.read(RadrifleItem.RADRIFLE_OVERHEAT, nbt);
		this.holdAnimationState.read(MagneticRailgunItem.RAILGUN_HOLD, nbt);
		this.inspectAnimationState.read(MagneticRailgunItem.RAILGUN_INSPECT, nbt);
		this.chargeAnimationState.read(MagneticRailgunItem.RAILGUN_CHARGE, nbt);
		this.fireAnimationState.read(MagneticRailgunItem.RAILGUN_FIRE, nbt);
		this.raybladeSwingAnimationState.read(RaybladeItem.RAYBLADE_SWING, nbt);
	}

	@Override
	public void tick(Entity player, ItemStack stack) 
	{
		this.tickCount++;
		if(this.getAnimationTick() > 0)
		{
			this.setAnimationTick(this.getAnimationTick() - 1);
		}
		else
		{
			if(stack.is(ACCItems.RAYBLADE.get()) && this.getAnimationState() == 1)
			{
				ACCUtil.setVisible(stack, false);
			}
			if(stack.is(ACCItems.MAGNETIC_RAILGUN.get()))
			{
				if(this.getAnimationState() != 1 && this.getAnimationState() != 2)
				{
					this.setAnimationState(0);
				}
			}
			else
			{
				this.setAnimationState(0);
			}
		}
		if(player.level.isClientSide)
		{
			this.overheatAnimationState.updateWhen(this.getAnimationState() == 1 && stack.is(ACCItems.RADRIFLE.get()), this.tickCount);
			this.holdAnimationState.updateWhen(this.getAnimationState() == 0 && stack.is(ACCItems.MAGNETIC_RAILGUN.get()), this.tickCount);
			this.inspectAnimationState.updateWhen(this.getAnimationState() == 1 && stack.is(ACCItems.MAGNETIC_RAILGUN.get()), this.tickCount);
			this.chargeAnimationState.updateWhen(this.getAnimationState() == 2 && stack.is(ACCItems.MAGNETIC_RAILGUN.get()), this.tickCount);
			this.fireAnimationState.updateWhen(this.getAnimationState() == 3 && stack.is(ACCItems.MAGNETIC_RAILGUN.get()), this.tickCount);
			
			this.raybladeSwingAnimationState.updateWhen(this.getAnimationState() == 1 && stack.is(ACCItems.RAYBLADE.get()), this.tickCount);
		}
		else
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new UpdateItemAnimationPacket(stack, player.getUUID(), this.animationState, this.animationTick));
		}
	}

	@Override
	public void setAnimationState(int state) 
	{
		this.animationState = state;
	}

	@Override
	public int getAnimationState() 
	{
		return this.animationState;
	}
	
	@Override
	public SmoothAnimationState getAnimationStateByName(String name) 
	{
		if(name.equals(RadrifleItem.RADRIFLE_OVERHEAT))
		{
			return this.overheatAnimationState;
		}
		if(name.equals(MagneticRailgunItem.RAILGUN_HOLD))
		{
			return this.holdAnimationState;
		}
		if(name.equals(MagneticRailgunItem.RAILGUN_INSPECT))
		{
			return this.inspectAnimationState;
		}
		if(name.equals(MagneticRailgunItem.RAILGUN_CHARGE))
		{
			return this.chargeAnimationState;
		}
		if(name.equals(MagneticRailgunItem.RAILGUN_FIRE))
		{
			return this.fireAnimationState;
		}
		if(name.equals(RaybladeItem.RAYBLADE_SWING))
		{
			return this.raybladeSwingAnimationState;
		}
		return new SmoothAnimationState();
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
	
	@Override
	public int getTickCount() 
	{
		return this.tickCount;
	}
}
