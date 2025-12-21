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
		return !this.mob.isUsingSkill() && this.mob.tickCount >= this.nextSkillTickCount && this.canEat();
	}
	
	@Override
	public boolean stopMovingWhenStart()
	{
		return false;
	}
	
	public boolean canEat()
	{
		return this.mob.getEggHoldingTick() >= 160 && this.mob.isHoldingEgg() && !this.mob.getCurrentEgg().isEmpty() && !this.mob.isBaby() && !this.mob.isTame();
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
	public void stop()
	{
		super.stop();
		this.mob.setAnimationState(0);
	}

	@Override
	public int getSkillUsingTime() 
	{
		return 35;
	}
	
	@Override
	public int getSkillWarmupTime()
	{
		return 5;
	}

	@Override
	public int getSkillUsingInterval() 
	{
		return 100;
	}
}
