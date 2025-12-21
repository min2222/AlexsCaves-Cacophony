package com.min01.acc.entity.projectile;

import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.item.ACCItems;
import com.min01.acc.util.ACCUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class EntityFearArrow extends AbstractArrow
{
	public EntityFearArrow(EntityType<? extends AbstractArrow> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}

	public EntityFearArrow(Level pLevel, double pX, double pY, double pZ)
	{
		super(ACCEntities.FEAR_ARROW.get(), pX, pY, pZ, pLevel);
	}

	public EntityFearArrow(Level pLevel, LivingEntity pShooter) 
	{
		super(ACCEntities.FEAR_ARROW.get(), pShooter, pLevel);
	}

	@Override
	protected ItemStack getPickupItem() 
	{
		return new ItemStack(ACCItems.ARROW_OF_FEAR.get());
	}
	
	@Override
	protected void onHitEntity(EntityHitResult pResult)
	{
		super.onHitEntity(pResult);
		Entity entity = pResult.getEntity();
        if(entity instanceof PathfinderMob mob) 
        {
        	if(!mob.getType().is(ACTagRegistry.RESISTS_TREMORSAURUS_ROAR))
        	{
        		ACCUtil.runAway(mob, this.position());
        	}
        }
	}
}
