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
	public EntityFearArrow(EntityType<? extends AbstractArrow> p_36858_, Level p_36859_)
	{
		super(p_36858_, p_36859_);
	}

	public EntityFearArrow(Level p_36861_, double p_36862_, double p_36863_, double p_36864_)
	{
		super(ACCEntities.FEAR_ARROW.get(), p_36862_, p_36863_, p_36864_, p_36861_);
	}

	public EntityFearArrow(Level p_36866_, LivingEntity p_36867_) 
	{
		super(ACCEntities.FEAR_ARROW.get(), p_36867_, p_36866_);
	}

	@Override
	protected ItemStack getPickupItem() 
	{
		return new ItemStack(ACCItems.ARROW_OF_FEAR.get());
	}
	
	@Override
	protected void onHitEntity(EntityHitResult p_36757_)
	{
		super.onHitEntity(p_36757_);
		Entity entity = p_36757_.getEntity();
        if(entity instanceof PathfinderMob mob) 
        {
        	if(!mob.getType().is(ACTagRegistry.RESISTS_TREMORSAURUS_ROAR))
        	{
        		ACCUtil.runAway(mob, this.position());
        	}
        }
	}
}
