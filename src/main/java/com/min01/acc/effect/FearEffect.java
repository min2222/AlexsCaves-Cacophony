package com.min01.acc.effect;

import java.util.List;

import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

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
                mob.setTarget(null);
                mob.setLastHurtByMob(null);
                if(mob.onGround())
                {
                    Vec3 randomShake = new Vec3(p_19467_.level.random.nextFloat() - 0.5F, 0, p_19467_.level.random.nextFloat() - 0.5F).scale(0.1F);
                    mob.setDeltaMovement(mob.getDeltaMovement().multiply(0.7F, 1, 0.7F).add(randomShake));
                }
                Vec3 vec = LandRandomPos.getPosAway(mob, 15, 7, p_19467_.position());
                if(vec != null)
                {
                    mob.getNavigation().moveTo(vec.x, vec.y, vec.z, 2.0D);
                }
        	}
		});
	}
}

