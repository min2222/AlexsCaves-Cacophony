package com.min01.acc.entity.ai.goal;

import com.min01.acc.entity.living.EntityOvivenator;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class OvivenatorEatEggGoal extends BasicAnimationSkillGoal<EntityOvivenator>
{
	public OvivenatorEatEggGoal(EntityOvivenator mob) 
	{
		super(mob);
	}
	
	@Override
	public void start() 
	{
		super.start();
		this.mob.setAnimationState(2);
	}
	
	@Override
	public boolean canUse() 
	{
		return !this.getMob().isUsingSkill() && this.getMob().tickCount >= this.nextSkillTickCount && this.additionalStartCondition();
	}
	
	@Override
	public boolean stopMovingWhenStart()
	{
		return false;
	}
	
	@Override
	public boolean additionalStartCondition()
	{
		return this.mob.getEggHoldingTick() >= 160 && this.mob.isHoldingEgg() && !this.mob.getCurrentEgg().isEmpty();
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		if(this.mob.getAnimationTick() <= this.getSkillUsingTime() - this.getSkillWarmupTime())
		{
			if(this.mob.tickCount % 5 == 0)
			{
				this.mob.playSound(SoundEvents.GENERIC_EAT);
				this.mob.level.broadcastEntityEvent(this.mob, (byte) 99);
			}
		}
		if(this.mob.getAnimationTick() <= this.getSkillUsingTime() - 15)
		{
			this.mob.setEggHoldingTick(0);
			this.mob.setHoldingEgg(false);
			this.mob.setCurrentEgg(ItemStack.EMPTY);
			this.mob.setEggPos(BlockPos.ZERO);
		}
	}

	@Override
	protected void performSkill() 
	{
		
	}
	
	@Override
	public void stop()
	{
		super.stop();
		this.mob.setAnimationState(0);
	}

	@Override
	protected int getSkillUsingTime() 
	{
		return 35;
	}
	
	@Override
	protected int getSkillWarmupTime()
	{
		return 5;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 100;
	}
}
