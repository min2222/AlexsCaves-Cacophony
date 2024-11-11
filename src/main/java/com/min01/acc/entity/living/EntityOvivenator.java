package com.min01.acc.entity.living;

import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.min01.acc.entity.AbstractAnimatableDinosaur;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EntityOvivenator extends AbstractAnimatableDinosaur
{
	public EntityOvivenator(EntityType<? extends DinosaurEntity> p_33002_, Level p_33003_)
	{
		super(p_33002_, p_33003_);
	}

	@Override
	public BlockState createEggBlockState() 
	{
		return null;
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) 
	{
		return null;
	}
}
