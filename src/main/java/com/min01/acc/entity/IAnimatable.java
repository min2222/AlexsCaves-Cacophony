package com.min01.acc.entity;

public interface IAnimatable
{
	boolean isUsingSkill();
	
	void setIsUsingSkill(boolean value);
	
	void setAnimationTick(int value);
	
	int getAnimationTick();
	
	int getMoveStopDelay();
	
	int getPrevAnimationTick();
	
	boolean canMove();
	
	void setCanMove(boolean value);
}
