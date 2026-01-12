package com.min01.acc.capabilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.min01.acc.item.ACCItems;
import com.min01.acc.misc.SmoothAnimationState;
import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdateItemAnimationPacket;
import com.min01.acc.util.ACCUtil;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class ItemAnimationCapabilityImpl implements IItemAnimationCapability
{
	public static final Capability<IItemAnimationCapability> ITEM_ANIMATION = CapabilityManager.get(new CapabilityToken<>() {});
	
	private int animationTick;
	private int animationState;
	private int tickCount;
	
	public float brightness;
	public float brightnessOld;
	public int glowingTicks;
	
	public final SmoothAnimationState overheatAnimationState = new SmoothAnimationState();
	
	public final SmoothAnimationState railgunReloadAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState railgunChargeAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState railgunFireAnimationState = new SmoothAnimationState(0.999F, 0.4F);
	
	public final SmoothAnimationState raybladeSwingAnimationState = new SmoothAnimationState(0.999F, 0.4F);
	
	private final ItemStack stack;
	
	public ItemAnimationCapabilityImpl(ItemStack stack) 
	{
		this.stack = stack;
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("AnimationTick", this.animationTick);
		nbt.putInt("AnimationState", this.animationState);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.setAnimationTick(nbt.getInt("AnimationTick"));
		this.setAnimationState(nbt.getInt("AnimationState"));
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
			this.setAnimationState(0);
			this.setAnimationTick(0);
		}
		if(player.level.isClientSide)
		{
			if(stack.is(ACCItems.MAGNETIC_RAILGUN.get()))
			{
	            ++this.glowingTicks;
	            this.brightness += (0.0F - this.brightness) * 0.8F;
			}
			this.overheatAnimationState.updateWhen(this.getAnimationState() == 1 && stack.is(ACCItems.RADRIFLE.get()), this.tickCount);
			
			this.railgunReloadAnimationState.updateWhen(this.getAnimationState() == 1 && stack.is(ACCItems.MAGNETIC_RAILGUN.get()), this.tickCount);
			this.railgunChargeAnimationState.updateWhen(this.getAnimationState() == 0 && ((LivingEntity)player).isUsingItem() && stack.is(ACCItems.MAGNETIC_RAILGUN.get()), this.tickCount);
			this.railgunFireAnimationState.updateWhen(this.getAnimationState() == 2 && stack.is(ACCItems.MAGNETIC_RAILGUN.get()), this.tickCount);
			
			this.raybladeSwingAnimationState.updateWhen(this.getAnimationState() == 1 && stack.is(ACCItems.RAYBLADE.get()), this.tickCount);
		}
		else
		{
			this.sendUpdatePacket(player);
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
	
	private void sendUpdatePacket(Entity entity) 
	{
		if(!entity.level.isClientSide)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new UpdateItemAnimationPacket(this.stack, entity.getUUID(), this.animationState, this.animationTick));
		}
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		return ITEM_ANIMATION.orEmpty(cap, LazyOptional.of(() -> this));
	}
}
