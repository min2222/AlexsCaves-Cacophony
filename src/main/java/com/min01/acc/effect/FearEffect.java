package com.min01.acc.effect;

import java.util.List;

import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.min01.acc.util.ACCUtil;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;

public class FearEffect extends MobEffect
{
	public FearEffect() 
	{
		super(MobEffectCategory.BENEFICIAL, 16179593);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) 
	{
		return duration > 0;
	}
	
	@Override
	public void applyEffectTick(LivingEntity p_19467_, int p_19468_) 
	{
		int amplifier = p_19468_ + 1;
		List<PathfinderMob> list = p_19467_.level.getEntitiesOfClass(PathfinderMob.class, p_19467_.getBoundingBox().inflate(Math.min(5.0F * amplifier, 20.0F)), t -> !t.isAlliedTo(p_19467_) && !(t instanceof TamableAnimal) || !((TamableAnimal) t).isInSittingPose());
		list.forEach(mob -> 
		{
        	if(!mob.getType().is(ACTagRegistry.RESISTS_TREMORSAURUS_ROAR))
        	{
        		ACCUtil.runAway(mob, p_19467_.position());
        	}
		});
	}
}

