package com.min01.acc.entity.ai.goal;

import com.min01.acc.entity.AbstractAnimatableMonster;

public abstract class BasicAnimationSkillGoal<T extends AbstractAnimatableMonster> extends AbstractAnimationSkillGoal
{
	public T mob;
	
	public BasicAnimationSkillGoal(T mob) 
	{
		this.mob = mob;
	}

	@Override
	public AbstractAnimatableMonster getMob() 
	{
		return this.mob;
	}
}
