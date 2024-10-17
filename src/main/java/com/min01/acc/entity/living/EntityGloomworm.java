package com.min01.acc.entity.living;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.min01.acc.block.ACCBlocks;
import com.min01.acc.block.GloomothCocoonBlock;
import com.min01.acc.entity.AbstractAnimatableCreature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EntityGloomworm extends AbstractAnimatableCreature
{
	public EntityGloomworm(EntityType<? extends PathfinderMob> p_33002_, Level p_33003_) 
	{
		super(p_33002_, p_33003_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
    			.add(Attributes.MAX_HEALTH, 3.0F)
    			.add(Attributes.MOVEMENT_SPEED, 0.1F);
    }
    
	@Override
	protected void registerGoals() 
	{
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0F));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
	}
	
	@Override
	public void tick() 
	{
		super.tick();

		if(this.tickCount >= 6000)
		{
			if(this.isThornWood(this.blockPosition().below()))
			{
				BlockState state = ACCBlocks.GLOOMOTH_COCOON.get().defaultBlockState();
				if(this.hasCustomName() && this.getCustomName().getString().equals("Gondal"))
				{
					state = state.setValue(GloomothCocoonBlock.WORM_NAME, GloomothCocoonBlock.WormName.GONDAL);
				}
				this.level.setBlockAndUpdate(this.blockPosition(), state);
				this.discard();
			}
		}
	}
	
	@Override
	protected void updateWalkAnimation(float p_268283_)
	{
		float f = Math.min(p_268283_ * 20.0F, 1.0F);
		this.walkAnimation.update(f, 0.4F);
	}
	
	public boolean isThornWood(BlockPos pos)
	{
		return this.level.getBlockState(pos).is(ACBlockRegistry.THORNWOOD_WOOD.get()) || this.level.getBlockState(pos).is(ACBlockRegistry.THORNWOOD_LOG.get()) || this.level.getBlockState(pos).is(ACBlockRegistry.THORNWOOD_BRANCH.get());
	}
}
