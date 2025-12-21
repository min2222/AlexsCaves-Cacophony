package com.min01.acc.advancements;

import com.min01.acc.advancements.critereon.ShootNuclearBombTrigger;

import net.minecraft.advancements.CriteriaTriggers;

public class ACCCriteriaTriggers
{
	public static final ShootNuclearBombTrigger SHOOT_NUCLEAR_BOMB = new ShootNuclearBombTrigger();
	
	public static void init()
	{
		CriteriaTriggers.register(ACCCriteriaTriggers.SHOOT_NUCLEAR_BOMB);
	}
}
