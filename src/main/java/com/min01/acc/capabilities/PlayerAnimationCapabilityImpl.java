package com.min01.acc.capabilities;

import java.util.List;

import com.min01.acc.item.ACCItems;
import com.min01.acc.item.RadrifleItem;
import com.min01.acc.item.RaybladeItem;
import com.min01.acc.misc.SmoothAnimationState;
import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdatePlayerAnimationPacket;
import com.min01.acc.util.ACCUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class PlayerAnimationCapabilityImpl implements IPlayerAnimationCapability
{
	private int animationTick;
	private int animationState;
	private int prevAnimationState;
	
	private final SmoothAnimationState radrifleFireAnimationState = new SmoothAnimationState(0.999F);
	private final SmoothAnimationState radrifleHoldAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState radrifleRunningAnimationState = new SmoothAnimationState(0.999F);
	private final SmoothAnimationState radrifleHoldToRunAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState radrifleOverchargeFireAnimationState = new SmoothAnimationState(0.999F);
	private final SmoothAnimationState radrifleOverheatAnimationState = new SmoothAnimationState(0.999F);
	
	private final SmoothAnimationState raybladeDrawRightAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState raybladeSwingRightAnimationState = new SmoothAnimationState();
	
	private final SmoothAnimationState raybladeDrawLeftAnimationState = new SmoothAnimationState();
	private final SmoothAnimationState raybladeSwingLeftAnimationState = new SmoothAnimationState();
	
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
	public void tick(LivingEntity entity) 
	{
		if(entity.level.isClientSide)
		{
			this.radrifleFireAnimationState.updateWhen(this.getAnimationState() == 1 && entity.isHolding(ACCItems.RADRIFLE.get()), entity.tickCount);
			this.radrifleHoldAnimationState.updateWhen(this.getAnimationState() == 0 && entity.isHolding(ACCItems.RADRIFLE.get()) && !entity.isSprinting(), entity.tickCount);
			this.radrifleHoldToRunAnimationState.updateWhen(this.getAnimationState() == 2 && entity.isHolding(ACCItems.RADRIFLE.get()) && entity.isSprinting(), entity.tickCount);
			this.radrifleRunningAnimationState.updateWhen(this.getAnimationState() == 0 && entity.isHolding(ACCItems.RADRIFLE.get()) && entity.isSprinting(), entity.tickCount);
			this.radrifleOverchargeFireAnimationState.updateWhen(this.getAnimationState() == 3 && entity.isHolding(ACCItems.RADRIFLE.get()), entity.tickCount);
			this.radrifleOverheatAnimationState.updateWhen(this.getAnimationState() == 4 && entity.isHolding(ACCItems.RADRIFLE.get()), entity.tickCount);
			
			this.raybladeDrawRightAnimationState.updateWhen(this.getAnimationState() == 0 && entity.getMainHandItem().is(ACCItems.RAYBLADE.get()), entity.tickCount);
			this.raybladeSwingRightAnimationState.updateWhen(this.getAnimationState() == 1 && entity.getMainHandItem().is(ACCItems.RAYBLADE.get()), entity.tickCount);
			this.raybladeDrawLeftAnimationState.updateWhen(this.getAnimationState() == 0 && entity.getOffhandItem().is(ACCItems.RAYBLADE.get()), entity.tickCount);
			this.raybladeSwingLeftAnimationState.updateWhen(this.getAnimationState() == 1 && entity.getOffhandItem().is(ACCItems.RAYBLADE.get()), entity.tickCount);
		}
		else
		{
			if(this.getAnimationState() == 1 && entity.isHolding(ACCItems.RAYBLADE.get()))
			{
				if(this.getAnimationTick() == 25)
				{
					float size = 1.5F;
					Vec3 lookPos = ACCUtil.getLookPos(new Vec2(entity.getXRot(), entity.getYHeadRot()), entity.getEyePosition(), 0, 0, 1.5F);
					AABB aabb = new AABB(-size, -size, -size, size, size, size).move(lookPos);
					List<LivingEntity> list = entity.level.getEntitiesOfClass(LivingEntity.class, aabb, t -> t != entity && !t.isAlliedTo(entity));
					list.forEach(t -> 
					{
						t.hurt(entity.damageSources().mobAttack(entity), entity.getRandom().nextInt(100, 200) + 1);
					});
				}
			}
			if(entity.isSprinting() && this.getAnimationState() == 0 && this.getPrevAnimationState() != 2 && entity.isHolding(ACCItems.RADRIFLE.get()))
			{
				this.setAnimationState(2);
				this.setAnimationTick(10);
			}
			if(this.getAnimationTick() > 0)
			{
				this.setAnimationTick(this.getAnimationTick() - 1);
			}
			else
			{
				if(this.getAnimationState() == 2 && entity.isSprinting())
				{
					this.setPrevAnimationState(0);
				}
				if(!entity.isSprinting() && this.getPrevAnimationState() == 2)	
				{
					this.setPrevAnimationState(0);
				}
				this.setAnimationState(0);
				this.setAnimationTick(0);
			}
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new UpdatePlayerAnimationPacket(entity.getUUID(), this.animationState, this.prevAnimationState, this.animationTick));
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
	public void setPrevAnimationState(int state) 
	{
		this.prevAnimationState = state;
	}

	@Override
	public int getPrevAnimationState() 
	{
		return this.prevAnimationState;
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
		if(name.equals(RaybladeItem.RAYBLADE_DRAW_RIGHT))
		{
			return this.raybladeDrawRightAnimationState;
		}
		if(name.equals(RaybladeItem.RAYBLADE_SWING_RIGHT))
		{
			return this.raybladeSwingRightAnimationState;
		}
		if(name.equals(RaybladeItem.RAYBLADE_DRAW_LEFT))
		{
			return this.raybladeDrawLeftAnimationState;
		}
		if(name.equals(RaybladeItem.RAYBLADE_SWING_LEFT))
		{
			return this.raybladeSwingLeftAnimationState;
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
}
