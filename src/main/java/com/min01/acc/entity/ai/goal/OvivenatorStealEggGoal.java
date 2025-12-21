package com.min01.acc.entity.ai.goal;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.min01.acc.entity.living.EntityOvivenator;
import com.min01.acc.misc.ACCTags;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class OvivenatorStealEggGoal extends Goal 
{
	protected final EntityOvivenator mob;
	private double wantedX;
	private double wantedY;
	private double wantedZ;
	private final Level level;

	public OvivenatorStealEggGoal(EntityOvivenator mob) 
	{
		this.mob = mob;
		this.level = mob.level;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() 
	{
		if(this.mob.isHoldingEgg() || this.mob.isDancing() || this.mob.getAnimationState() != 0 || !this.mob.getEggPos().equals(BlockPos.ZERO) || this.mob.isBaby() || this.mob.isTame())
		{
			return false;
		}
		return this.setWantedPos();
	}

	public boolean setWantedPos() 
	{
		Vec3 vec3 = this.findEgg();
		if(vec3 == null) 
		{
			return false;
		} 
		else 
		{
			this.wantedX = vec3.x;
			this.wantedY = vec3.y;
			this.wantedZ = vec3.z;
			return true;
		}
	}

	@Override
	public boolean canContinueToUse()
	{
		return !this.mob.getNavigation().isDone();
	}

	@Override
	public void start() 
	{
		this.mob.getMoveControl().setWantedPosition(this.wantedX, this.wantedY, this.wantedZ, 1.5F);
		this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, 1.5F);
	}

	@Nullable
	public Vec3 findEgg()
	{
		RandomSource random = this.mob.getRandom();
		BlockPos blockPos = this.mob.blockPosition();
		for(int i = 0; i < 10; ++i) 
		{
			BlockPos blockPos1 = blockPos.offset(random.nextInt(20) - 10, 0, random.nextInt(20) - 10);
			BlockState state = this.level.getBlockState(blockPos1);
			if(state.is(ACCTags.ACCBlocks.DINOSAUR_EGGS) && !state.is(ACCTags.ACCBlocks.OVIVENATOR_CANT_STEAL))
			{
				this.mob.setEggPos(blockPos1);
				return Vec3.atBottomCenterOf(blockPos1);
			}
		}
		return null;
	}
}