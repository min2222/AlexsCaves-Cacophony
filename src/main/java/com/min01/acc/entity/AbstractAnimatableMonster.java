package com.min01.acc.entity;

import com.github.alexmodguy.alexscaves.server.entity.ai.AdvancedPathNavigateNoTeleport;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.IAdvancedPathingMob;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class AbstractAnimatableMonster extends Monster implements IAdvancedPathingMob
{
	public static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> ANIMATION_TICK = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> PREV_ANIMATION_TICK = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> MOVE_STOP_DELAY = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> CAN_MOVE = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_USING_SKILL = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> HAS_TARGET = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.BOOLEAN);
	
	public AbstractAnimatableMonster(EntityType<? extends Monster> p_33002_, Level p_33003_) 
	{
		super(p_33002_, p_33003_);
		this.noCulling = true;
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(ANIMATION_STATE, 0);
		this.entityData.define(ANIMATION_TICK, 0);
		this.entityData.define(PREV_ANIMATION_TICK, 0);
		this.entityData.define(MOVE_STOP_DELAY, 0);
		this.entityData.define(CAN_MOVE, true);
		this.entityData.define(IS_USING_SKILL, false);
		this.entityData.define(HAS_TARGET, false);
	}
    
    @Override
    public void tick()
    {
    	super.tick();

		if(!this.level.isClientSide)
		{
			this.setHasTarget(this.getTarget() != null);
		}
		
		if(this.getAnimationTick() > 0)
		{
			this.setAnimationTick(this.getAnimationTick() - 1);
		}
    }
    
	public void stopAllAnimationStates()
	{
		
	}
    
    @Override
    protected PathNavigation createNavigation(Level p_21480_)
    {
    	return new AdvancedPathNavigateNoTeleport(this, p_21480_);
    }
    
	@Override
	public boolean stopTickingPathing()
	{
		return !this.canMove();
	}
	
	public void setHasTarget(boolean value)
	{
		this.entityData.set(HAS_TARGET, value);
	}
	
	public boolean hasTarget()
	{
		return this.entityData.get(HAS_TARGET);
	}
	
	public void setIsUsingSkill(boolean value) 
	{
		this.entityData.set(IS_USING_SKILL, value);
	}
	
	public boolean isUsingSkill() 
	{
		return this.getAnimationTick() > 0 || this.entityData.get(IS_USING_SKILL);
	}
    
    public void setCanMove(boolean value)
    {
    	this.entityData.set(CAN_MOVE, value);
    }
    
    public boolean canMove()
    {
    	return this.entityData.get(CAN_MOVE);
    }
    
    public void setMoveStopDelay(int value)
    {
        this.entityData.set(MOVE_STOP_DELAY, value);
    }
    
    public int getMoveStopDelay()
    {
        return this.entityData.get(MOVE_STOP_DELAY);
    }
    
    public void setPrevAnimationTick(int value)
    {
        this.entityData.set(PREV_ANIMATION_TICK, value);
    }
    
    public int getPrevAnimationTick()
    {
        return this.entityData.get(PREV_ANIMATION_TICK);
    }
    
    public void setAnimationTick(int value)
    {
        this.entityData.set(ANIMATION_TICK, value);
    }
    
    public int getAnimationTick()
    {
        return this.entityData.get(ANIMATION_TICK);
    }
    
    public void setAnimationState(int value)
    {
        this.entityData.set(ANIMATION_STATE, value);
    }
    
    public int getAnimationState()
    {
        return this.entityData.get(ANIMATION_STATE);
    }
}
