package com.min01.acc.entity.ai.goal;

import java.util.List;

import com.min01.acc.entity.living.EntityOvivenator;
import com.min01.acc.misc.ACCTags;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class OvivenatorTickBoostGoal extends Goal 
{
	protected final EntityOvivenator mob;
    protected List<? extends AgeableMob> animals;
	private final Level level;
    private int delay;

	public OvivenatorTickBoostGoal(EntityOvivenator mob) 
	{
		this.mob = mob;
		this.level = mob.level;
	}

	@Override
	public boolean canUse() 
	{
		return this.mob.isTame() && !this.mob.isBaby();
	}
	
	@Override
	public void tick() 
	{
        if(--this.delay <= 0)
        {
            this.delay = this.adjustedTickDelay(1);
            this.animals = this.getNearbyAnimals();
        }
        else
        {
        	this.animals.removeIf(t -> t.isDeadOrDying() || !t.isBaby());
        }
    	if(this.mob.tickCount % 40 == 1)
    	{
            if(!this.animals.isEmpty())
            {
            	for(AgeableMob animal : this.animals)
            	{
            		animal.ageUp(60);
            	}
            }
    	}
		this.boost();
	}

	public void boost()
	{
		RandomSource random = this.mob.getRandom();
		BlockPos blockPos = this.mob.blockPosition();
		for(int i = 0; i < 10; ++i) 
		{
			BlockPos blockPos1 = blockPos.offset(random.nextInt(5), 0, random.nextInt(5));
			BlockState state = this.level.getBlockState(blockPos1);
			if(state.is(ACCTags.ACCBlocks.DINOSAUR_EGGS) || BonemealableBlock.class.isAssignableFrom(state.getBlock().getClass()))
			{
				if(state.isRandomlyTicking())
				{
					state.randomTick((ServerLevel) this.mob.level, blockPos1, random);
				}
			}
		}
	}
	
    public List<? extends AgeableMob> getNearbyAnimals()
    {
        return this.level.getEntitiesOfClass(AgeableMob.class, this.mob.getBoundingBox().inflate(4.0F), t -> t != this.mob && !t.isDeadOrDying() && t.isBaby());
    }
}