package com.min01.acc.entity.projectile;

import com.min01.acc.entity.ACCEntities;
import com.min01.acc.item.ACCItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
}
