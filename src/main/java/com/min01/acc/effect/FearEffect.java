package com.min01.acc.effect;

import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

public class FearEffect extends MobEffect
{
	public FearEffect() 
	{
		super(MobEffectCategory.HARMFUL, 16179593);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) 
	{
		return duration > 0;
	}
	
	@Override
	public boolean isInstantenous() 
	{
		return true;
	}
	
	@Override
	public void applyInstantenousEffect(Entity p_19462_, Entity p_19463_, LivingEntity p_19464_, int p_19465_, double p_19466_)
	{
        if(p_19464_ instanceof PathfinderMob mob && (!(mob instanceof TamableAnimal) || !((TamableAnimal) mob).isInSittingPose())) 
        {
        	if(!mob.getType().is(ACTagRegistry.RESISTS_TREMORSAURUS_ROAR))
        	{
                mob.setTarget(null);
                mob.setLastHurtByMob(null);
                if(mob.onGround())
                {
                    Vec3 randomShake = new Vec3(p_19462_.level.random.nextFloat() - 0.5F, 0, p_19462_.level.random.nextFloat() - 0.5F).scale(0.1F);
                    mob.setDeltaMovement(mob.getDeltaMovement().multiply(0.7F, 1, 0.7F).add(randomShake));
                }
                Vec3 vec = LandRandomPos.getPosAway(mob, 15, 7, p_19462_.position());
                if(vec != null)
                {
                    mob.getNavigation().moveTo(vec.x, vec.y, vec.z, 2.0D);
                }
        	}
        }
	}
}

